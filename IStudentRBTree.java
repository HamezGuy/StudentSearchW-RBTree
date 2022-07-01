import java.util.Iterator;

/**
 * This interface represents a student Red Black tree that will store a list for each node.
 *
 * @param <KeyType>   The type of the key.
 * @param <ValueType> The type of the value.
 */
public interface IStudentRBTree<KeyType extends Comparable<KeyType>, ValueType>
        extends Iterable<IStudentRBTreeNode<KeyType, ValueType>>,
        SortedCollectionInterface<IStudentRBTreeNode<KeyType, ValueType>> {

    public boolean add(KeyType key, ValueType value) throws IllegalArgumentException;

    public boolean removeByKey(KeyType key);

    public boolean removeByValue(ValueType value);

    public int size();

    public Iterator<IStudentRBTreeNode<KeyType, ValueType>> search(KeyType key);

    public Iterator<IStudentRBTreeNode<KeyType, ValueType>> iterator();
}
