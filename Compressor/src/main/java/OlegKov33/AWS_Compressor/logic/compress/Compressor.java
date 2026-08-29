package OlegKov33.AWS_Compressor.logic.compress;

import OlegKov33.AWS_Compressor.dto.EncodedFile;
import OlegKov33.AWS_Compressor.dto.lzss_token.LZSSToken;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@AllArgsConstructor
public class Compressor {
    private final LZSSCompressor lzssCompressor;
    private final HuffmanCompressor huffmanCompressor;
    public ResponseEntity compressFile(MultipartFile file){

        List<byte[]> finalList;
        try {

            InputStream stream = file.getInputStream();
            Map<LZSSToken, Integer> frequencyCount = tokenCount(stream);
            huffmanCompressor.configureTree(frequencyCount);

            stream = file.getInputStream();
            finalList = tokenProcessor(stream, frequencyCount);

            String extension = ".txt";
            String name = "file";

            String originalFileName = file.getOriginalFilename();

            if(originalFileName != null && originalFileName.contains(".")){
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                name = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            }


            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(name + extension, StandardCharsets.UTF_8)
                    .build();

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new EncodedFile(finalList,
                    huffmanCompressor.getBitLength(),
                    huffmanCompressor.getCodeTable(),
                    extension,
                    name));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Map<LZSSToken, Integer> tokenCount(InputStream stream) {
        try {
            byte[] buffer = new byte[65536];
            int bytesRead;
            byte[] savedTail = null;
            Map<LZSSToken, Integer> frequencyCount = new HashMap<>();

            while ((bytesRead = stream.read(buffer)) != -1) {
                byte[] chunk = Arrays.copyOf(buffer, bytesRead);
                byte[] combined = combineByteArray(savedTail, chunk);

                List<LZSSToken> chunkTokens = lzssCompressor.compress(combined);

                for (LZSSToken token : chunkTokens) {
                    frequencyCount.merge(token, 1, Integer::sum);
                }

                int tailLen = Math.min(16, chunk.length);
                savedTail = Arrays.copyOfRange(chunk, chunk.length - tailLen, chunk.length);
            }
            return frequencyCount;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<byte[]> tokenProcessor(InputStream stream, Map<LZSSToken, Integer> frequencyCount) {
        List<byte[]> finalList = new ArrayList<>();
        huffmanCompressor.configureTree(frequencyCount);

        try {
            byte[] buffer = new byte[65536];
            int bytesRead;
            byte[] savedTail = null;

            while ((bytesRead = stream.read(buffer)) != -1) {
                byte[] chunk = Arrays.copyOf(buffer, bytesRead);
                byte[] combined = combineByteArray(savedTail, chunk);

                List<LZSSToken> chunkTokens = lzssCompressor.compress(combined);
                finalList.add(huffmanCompressor.compress(chunkTokens));

                int tailLen = Math.min(16, chunk.length);
                savedTail = Arrays.copyOfRange(chunk, chunk.length - tailLen, chunk.length);
            }
            return finalList;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] combineByteArray(byte[] savedTail, byte[] chunk) {
        if (savedTail == null || savedTail.length == 0) {
            return chunk;
        }
        byte[] combined = new byte[savedTail.length + chunk.length];
        System.arraycopy(savedTail, 0, combined, 0, savedTail.length);
        System.arraycopy(chunk, 0, combined, savedTail.length, chunk.length);
        return combined;
    }

}
