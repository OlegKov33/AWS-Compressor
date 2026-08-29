package OlegKov33.AWS_Compressor.config;

import OlegKov33.AWS_Compressor.logic.compress.Compressor;
import OlegKov33.AWS_Compressor.logic.compress.HuffmanCompressor;
import OlegKov33.AWS_Compressor.logic.compress.LZSSCompressor;
import OlegKov33.AWS_Compressor.logic.decompress.Decompressor;
import OlegKov33.AWS_Compressor.logic.decompress.HuffmanDecompressor;
import OlegKov33.AWS_Compressor.logic.decompress.LZSSDecompressor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
@AllArgsConstructor
public class DecompressorConfig {

    private final LZSSDecompressor lzssDecompressor;
    private final HuffmanDecompressor huffmanDecompressor;
    private final ObjectMapper objectMapper;

    @Bean
    public Decompressor decompressor(){
        return new Decompressor(lzssDecompressor, huffmanDecompressor, objectMapper);
    }

}
