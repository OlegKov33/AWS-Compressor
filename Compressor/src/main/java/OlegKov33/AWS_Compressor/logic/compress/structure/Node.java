package OlegKov33.AWS_Compressor.logic.compress.structure;

import lombok.*;
import org.jspecify.annotations.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Node<T> implements Comparable<Node<T>> {
    private T data;
    private int value;
    private String path;
    private Node<T> left;
    private Node<T> right;

    /**
     *
     * @param o the object to be compared.
     * @return returns smallest to largest nodes
     */
    @Override
    public int compareTo(@NonNull Node o) {
        return Integer.compare(value, o.value);
    }

}
