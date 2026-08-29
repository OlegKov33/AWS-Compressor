package OlegKov33.AWS_Compressor.logic.decompress;

import OlegKov33.AWS_Compressor.dto.EncodedFile;
import OlegKov33.AWS_Compressor.dto.lzss_token.LZSSToken;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@AllArgsConstructor
public class Decompressor {
    private final LZSSDecompressor lzssDecompressor;
    private final HuffmanDecompressor<LZSSToken> huffmanDecompressor;
    private final ObjectMapper objectMapper;
    public ResponseEntity decompress(MultipartFile file){

        try{
            EncodedFile<LZSSToken> data = objectMapper
                    .readValue(
                            file.getInputStream(),
                            new TypeReference<EncodedFile<LZSSToken>>() {}
                    );


            huffmanDecompressor.setReferenceTable(data.getCodeTable());

            ByteArrayOutputStream finalFile = new ByteArrayOutputStream();
            List<byte[]> chunks = data.getBitStream();
            List<Integer> lengths = data.getBitLength();
            int prevChunkLength = 0;

            for (int j = 0; j < chunks.size(); j++) {
                String bits = byteToString(chunks.get(j), lengths.get(j));
                List<LZSSToken> chunkTokens = huffmanDecompressor.turningBinaryToOriginal(bits);
                byte[] chunkBytes = lzssDecompressor.messageToBytes(chunkTokens);

                int offset = 0;
                if (j > 0) {
                    int tailSize = Math.min(16, prevChunkLength);
                    if (chunkBytes.length >= tailSize) {
                        offset = tailSize;
                    }
                }
                finalFile.write(chunkBytes, offset, chunkBytes.length - offset);
                prevChunkLength = chunkBytes.length;
            }

            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(data.getFileName() + data.getFileExtension(), StandardCharsets.UTF_8)
                    .build();

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(finalFile.toByteArray());


        } catch (StreamReadException | IOException e) {
            return ResponseEntity
                    .badRequest()
                    .body("The file provided was not compressed by this program");
        }
    }

    private String byteToString(byte[] base64, int realBitCount){

        StringBuilder builder = new StringBuilder();
        for(byte b : base64){
            for(int i = 7; i >= 0; i--){
                if(builder.length() >= realBitCount){
                    return builder.toString();
                }
                builder.append((b >> i) & 1);
            }
        }

        return builder.toString();
    }
}
