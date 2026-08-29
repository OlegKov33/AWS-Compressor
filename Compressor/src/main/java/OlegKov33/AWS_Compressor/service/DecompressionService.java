package OlegKov33.AWS_Compressor.service;

import OlegKov33.AWS_Compressor.logic.decompress.Decompressor;
import OlegKov33.AWS_Compressor.logic.decompress.HuffmanDecompressor;
import OlegKov33.AWS_Compressor.logic.decompress.LZSSDecompressor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class DecompressionService {
    private final Decompressor decompressor;

    public ResponseEntity decompress(MultipartFile file){

        return decompressor
                .decompress(file);
    }

}
