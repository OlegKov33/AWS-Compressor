package OlegKov33.AWS_Compressor.dto.lzss_token;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LZSSTokenMatchTest {

    @Test
    void testingMatchTokenImplementation(){
        Assert.assertTrue(
                new LZSSTokenMatch(0, 0) instanceof LZSSToken
        );
    }
}
