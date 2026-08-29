package OlegKov33.AWS_Compressor.controller;

import OlegKov33.AWS_Compressor.service.DecompressionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/decompress")
@AllArgsConstructor
public class DecompressController {

    private final DecompressionService decompressionService;

    @PostMapping("/from-json")
    public ResponseEntity decompressFromJson(@RequestParam("file") MultipartFile file){
        return decompressionService.decompress(file);
    }
}
