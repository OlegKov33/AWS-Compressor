package OlegKov33.AWS_Compressor.service;

import OlegKov33.AWS_Compressor.logic.compress.Compressor;
import OlegKov33.AWS_Compressor.logic.compress.HuffmanCompressor;
import OlegKov33.AWS_Compressor.logic.compress.LZSSCompressor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CompressionService {

    private final Compressor compressor;

    public ResponseEntity compressFile(MultipartFile file){

        return compressor
                .compressFile(file);
    }
}
