package OlegKov33.AWS_Compressor.dto.lzss_token;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LZSSTokenLiteralTest {

    @Test
    void testingLiteralTokenImplementation(){
        Assert.assertTrue(
                new LZSSTokenLiteral(new Byte("0")) instanceof LZSSToken
        );
    }
}
