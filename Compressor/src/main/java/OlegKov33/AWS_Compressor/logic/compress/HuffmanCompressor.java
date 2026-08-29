package OlegKov33.AWS_Compressor.logic.compress;

import OlegKov33.AWS_Compressor.dto.EncodedFile;
import OlegKov33.AWS_Compressor.logic.compress.structure.Node;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HuffmanCompressor<T> {
    private final PriorityQueue<Node<T>> root = new PriorityQueue<>();
    private final Map<T, String> nodePath = new HashMap<>();
    private final List<Integer> bitLengthOut = new ArrayList<>();

    public EncodedFile<T> turningIntoBinary(List<T> data){

        root.clear();
        nodePath.clear();

        addAll(data);

        Map<String, T> codeTable = new HashMap<>();
        StringBuilder bitStream = new StringBuilder();
        for(var elem : data){

            bitStream.append(nodePath.get(elem));
        }

        for(T node : nodePath.keySet()){

            codeTable.put(nodePath.get(node), node);
        }



//        HuffmanEncoded<T> encoder = new HuffmanEncoded<>(stringToByteArray(bitStream.toString()) , codeTable);
//        return encoder;
        return null;
    }

    public byte[] compress(List<T> data){

        StringBuilder bitStream = new StringBuilder();
        for(var elem : data){

            bitStream.append(nodePath.get(elem));
        }

        bitLengthOut.add(bitStream.length());

        return stringToByteArray(bitStream.toString());
    }

    public Map<String, T> getCodeTable(){
        Map<String, T> codeTable = new HashMap<>();
        for(T node : nodePath.keySet()){

            codeTable.put(nodePath.get(node), node);
        }
        return codeTable;

    }

    public List<Integer> getBitLength(){
        return bitLengthOut;
    }

    public void configureTree(Map<T, Integer> data){

        root.clear();
        nodePath.clear();
        bitLengthOut.clear();

        for(var token : data.keySet()){
            root.add((Node<T>) Node.builder()
                            .data(token)
                            .value(data.get(token))
                            .left(null)
                            .right(null)
                    .build());
        }

        while (root.size() > 1){
            Node<T> leftNode = root.poll();
            Node<T> rightNode = root.poll();
            root.add((Node<T>) Node.builder()
                    .data(null)
                    .value(leftNode.getValue() + rightNode.getValue())
                    .left((Node<Object>) leftNode)
                    .right((Node<Object>) rightNode)
                    .build()
            );
        }
        fillingTableWithPath("", root.peek());
    }
    private void addAll(List<T> data){

        Map<T, Integer> valueCount = new HashMap<>();

        for(var entry : data){
            valueCount.put(
                    entry,
                    valueCount.getOrDefault(entry, 0)+1);
        }


        for(var entry : valueCount.keySet()){
            root.add((Node<T>) Node.builder()
                    .data(entry)
                    .value(valueCount.get(entry))
                    .left(null)
                    .right(null)
                    .build()
            );
        }

        // now that i have full queue of nodes
        while(root.size() > 1){
            Node<T> leftNode = root.poll();
            Node<T> rightNode = root.poll();
            root.add((Node<T>) Node.builder()
                    .data(null)
                    .value(leftNode.getValue() + rightNode.getValue())
                    .left((Node<Object>) leftNode)
                    .right((Node<Object>) rightNode)
                    .build()
            );
        }

        fillingTableWithPath("", root.peek());

    }

    private void fillingTableWithPath(String path, Node<T> nodesTree){
        if(nodesTree.getLeft() == null && nodesTree.getRight() == null){
            nodePath.put(nodesTree.getData(), path);
        }else{
            fillingTableWithPath(path+"1", nodesTree.getRight());
            fillingTableWithPath(path+"0", nodesTree.getLeft());
        }
    }

    private byte[] stringToByteArray(String data){

        byte[] result = new byte[(int)(Math.ceil( data.length()/8.0))];

        for(int i = 0; i < result.length; i++){

            int value = Integer.parseInt(data.substring(i * 8, Math.min(i*8+8, data.length())), 2);
            int bitCount = Math.min(8, data.length() - i * 8);
            value = value << (8 - bitCount);
            result[i] = (byte) value;
        }

        return result;
    }




}
