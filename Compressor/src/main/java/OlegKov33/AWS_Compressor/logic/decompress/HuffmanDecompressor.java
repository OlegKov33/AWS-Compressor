package OlegKov33.AWS_Compressor.logic.decompress;

import OlegKov33.AWS_Compressor.dto.lzss_token.LZSSToken;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class HuffmanDecompressor<T> {

    private Map<String, T> innerTable = new HashMap<>();
    public List<LZSSToken> turningBinaryToOriginal(String data){

        List<LZSSToken> resultList = new ArrayList<>();
        StringBuilder path = new StringBuilder();


        for(var c :data.toCharArray()){
            path.append(c);

            if(innerTable.containsKey(path.toString())){
                resultList.add((LZSSToken) innerTable.get(path.toString()));
                path.setLength(0);
            }
        }

        return resultList;
    }

    public void setReferenceTable(Map<String, T> codeTable){
        innerTable.clear();
        innerTable = codeTable;
    }
}
