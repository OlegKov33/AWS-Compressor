package OlegKov33.AWS_Compressor.controller;

import OlegKov33.AWS_Compressor.service.CompressionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/compress")
@RequiredArgsConstructor
public class CompressionController {

    private final CompressionService compressionService;

    @PostMapping("/file")
    public ResponseEntity compressFile(@RequestParam("file") MultipartFile file){
        return compressionService.compressFile(file);
    }

}
