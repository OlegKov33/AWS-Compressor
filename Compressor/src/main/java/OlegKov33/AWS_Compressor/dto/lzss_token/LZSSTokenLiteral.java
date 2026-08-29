package OlegKov33.AWS_Compressor.dto.lzss_token;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
public final class LZSSTokenLiteral implements LZSSToken{
    private Byte value;

    @Override
    public String toString() {
        return ""+value;
    }
}
