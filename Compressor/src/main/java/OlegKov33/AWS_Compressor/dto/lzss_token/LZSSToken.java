package OlegKov33.AWS_Compressor.dto.lzss_token;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(LZSSTokenLiteral.class),
        @JsonSubTypes.Type(LZSSTokenMatch.class)
})
public sealed interface LZSSToken permits LZSSTokenMatch, LZSSTokenLiteral{
}
