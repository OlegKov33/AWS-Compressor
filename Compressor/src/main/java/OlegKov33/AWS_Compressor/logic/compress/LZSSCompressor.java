package OlegKov33.AWS_Compressor.logic.compress;

import OlegKov33.AWS_Compressor.dto.lzss_token.LZSSToken;
import OlegKov33.AWS_Compressor.dto.lzss_token.LZSSTokenLiteral;
import OlegKov33.AWS_Compressor.dto.lzss_token.LZSSTokenMatch;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class LZSSCompressor {

    private final int WINDOW_SIZE = 2048;
    private final int HASH_SIZE = 1 << 15; // int = 32768
    private final int HASH_MASK = HASH_SIZE -1;


    private final List<LZSSToken> finalOutput = new ArrayList<>();

    public List<LZSSToken> compress(byte[] inputBytes) {
        finalOutput.clear();

        byte[] innerArray = inputBytes;
        int pos = 0;

        int[] head = new int[HASH_SIZE];
        int[] prev = new int[innerArray.length];
        Arrays.fill(head, -1);
        Arrays.fill(prev, -1);

        while (pos < innerArray.length) {
            int lookAheadEnd = Math.min(pos + WINDOW_SIZE, innerArray.length);

            LZSSToken token = matchResult(innerArray, pos, lookAheadEnd, head, prev);
            finalOutput.add(token);

            if (token instanceof LZSSTokenMatch match) {
                pos += match.getLength();
            } else {
                pos += 1;
            }
        }

        return finalOutput;
    }

    public String toString(List<LZSSToken> data){
        StringBuilder builder = new StringBuilder();

        for(var elem : data){
            if(elem.getClass() == LZSSTokenLiteral.class){
                builder.append(((LZSSTokenLiteral) elem).getValue());
            }else{
                LZSSTokenMatch match = (LZSSTokenMatch) elem;
                builder.append(match.getDistance()).append(match.getLength());
            }
        }

        return builder.toString();
    }

    private LZSSToken matchResult(byte[] data,
                                  int pos, int lookAheadEnd,
                                  int[] head, int[] prev) {

        int bestLength = 0;
        int bestCandidate = -1;

        if(pos + 2 < lookAheadEnd){
            int hashedValue = hash(data, pos);
            int candidateHead = head[hashedValue];
            int searchStart = Math.max(0, pos - WINDOW_SIZE);
            int chainLimit = 128;// 32

            while(candidateHead >= searchStart && chainLimit-- > 0){
                int matchLength = 0;
                while (candidateHead + matchLength < pos
                        && pos + matchLength < lookAheadEnd
                        && data[candidateHead + matchLength] == data[pos + matchLength]) {
                    matchLength++;
                }
                if(matchLength > bestLength){
                    bestLength = matchLength;
                    bestCandidate = candidateHead;
                }
                candidateHead = prev[candidateHead];
            }

            prev[pos] = head[hashedValue];
            head[hashedValue] = pos;
        }

        if(bestLength >= 2){
            return new LZSSTokenMatch(pos-bestCandidate, bestLength);
        }else{
            return new LZSSTokenLiteral(data[pos]);
        }

    }

    private int hash(byte[] data, int pos){
        int hashedIndex = (data[pos] << 10) ^ (data[pos+1] << 5) ^ data[pos+2];
        return hashedIndex & HASH_MASK;
    }

    public byte[] decipher(List<LZSSToken> message) {
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