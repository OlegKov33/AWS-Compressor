package OlegKov33.AWS_Compressor.dto.lzss_token;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public final class LZSSTokenMatch implements LZSSToken{
    private int distance;
    private int length;

    @Override
    public String toString() {
        return "(" + distance +
                " " + length+")";
    }
}
