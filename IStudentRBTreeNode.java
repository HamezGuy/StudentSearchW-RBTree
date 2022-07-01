import java.util.List;

/**
 * Interface for a student RBTreeNode.
 *
 * @param <KeyType>   The type of the key.
 * @param <ValueType> The type of the value.
 */
public interface IStudentRBTreeNode<KeyType extends Comparable<KeyType>, ValueType>
        extends Comparable<IStudentRBTreeNode<KeyType, ValueType>> {

    // include some public data fields to store Parent, left Child and red Child information.

    public KeyType getKey();

    public void setKey(KeyType key);

    public List<ValueType> getValues();

    public void setValues(List<ValueType> values);

    public boolean addValue(ValueType value) throws IllegalArgumentException;

    public boolean removeValue(ValueType value) throws NullPointerException, IllegalArgumentException;

    public int blackHeight(); // 0 = red, 1 = black, 2 = double black

    public boolean isLeftChild();

    public IStudentRBTreeNode<KeyType, ValueType> getParent();

    public void setParent(IStudentRBTreeNode<KeyType, ValueType> node);

    public IStudentRBTreeNode<KeyType, ValueType> getLeft();

    public void setLeft(IStudentRBTreeNode<KeyType, ValueType> node);

    public IStudentRBTreeNode<KeyType, ValueType> getRight();

    public void setRight(IStudentRBTreeNode<KeyType, ValueType> node);

    public IStudentRBTreeNode<KeyType, ValueType> getNext();

    public void setNext(IStudentRBTreeNode<KeyType, ValueType> node);

    public IStudentRBTreeNode<KeyType, ValueType> getSuccessor();

    public IStudentRBTreeNode<KeyType, ValueType> getPredecessor();

    // compareTo() is used to compare the keys
}
