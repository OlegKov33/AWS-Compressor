package OlegKov33.AWS_Compressor.logic.decompress;

import OlegKov33.AWS_Compressor.dto.lzss_token.LZSSToken;
import OlegKov33.AWS_Compressor.dto.lzss_token.LZSSTokenLiteral;
import OlegKov33.AWS_Compressor.dto.lzss_token.LZSSTokenMatch;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class LZSSDecompressor {

    public String decipher(List<LZSSToken> message){

        StringBuilder output = new StringBuilder();

        for(int i = 0; i < message.size(); i++){

            if(message.get(i).getClass() == LZSSTokenLiteral.class){
                output.append(((LZSSTokenLiteral)message.get(i)).getValue());
            }else{

                LZSSTokenMatch matcher = (LZSSTokenMatch) message.get(i);
                int start = output.length()- matcher.getDistance();

                for(int j = 0; j < matcher.getLength(); j++){
                    output.append(output.charAt(j+start));
                }
            }

        }

        return output.toString();
    }

    public byte[] messageToBytes(List<LZSSToken> message) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (LZSSToken token : message) {
            if (token instanceof LZSSTokenLiteral literal) {
                output.write(literal.getValue());
            } else if (token instanceof LZSSTokenMatch match) {
                byte[] buf = output.toByteArray();
                int start = buf.length - match.getDistance();
                for (int j = 0; j < match.getLength(); j++) {
                    output.write(buf[start + j]);
                }
            }
        }
        return output.toByteArray();
    }
}
