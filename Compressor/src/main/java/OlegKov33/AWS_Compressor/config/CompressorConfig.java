package OlegKov33.AWS_Compressor.config;

import OlegKov33.AWS_Compressor.logic.compress.Compressor;
import OlegKov33.AWS_Compressor.logic.compress.HuffmanCompressor;
import OlegKov33.AWS_Compressor.logic.compress.LZSSCompressor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CompressorConfig {

    private final LZSSCompressor lzssCompressor;
    private final HuffmanCompressor huffmanCompressor;

    @Bean
    public Compressor compressor(){
        return new Compressor(lzssCompressor, huffmanCompressor);
    }
}
