package OlegKov33.AWS_Compressor.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class EncodedFile<T> {

    private final List<byte[]> bitStream;
    private final List<Integer> bitLength;
    private final Map<String, T> codeTable;
    private final String fileExtension;
    private final String fileName;

    @Override
    public String toString() {
        return "HuffmanEncoded{" +
                "bitStream='" + bitStream + '\'' +
                ", codeTable=" + codeTable +
                '}';
    }
}
