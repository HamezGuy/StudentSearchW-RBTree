import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator for the placeholder BST
 *
 * @param <KeyType>   The type of the key
 * @param <ValueType> The type of the value
 * @author Ruixuan Tu
 */
public class StudentRBTreePlaceholderIterator
        <KeyType extends Comparable<KeyType>, ValueType extends Comparable<ValueType>>
        implements Iterator<IStudentRBTreeNode<KeyType, ValueType>> {
    protected IStudentRBTreeNode<KeyType, ValueType> node;

    StudentRBTreePlaceholderIterator(IStudentRBTreeNode<KeyType, ValueType> node) {
        this.node = node;
    }

    @Override
    public boolean hasNext() {
        return this.node != null && (this.node.getNext() != null || this.node.getSuccessor() != null);
    }

    @Override
    public IStudentRBTreeNode<KeyType, ValueType> next() {
        if (this.node == null) return null;
        if (this.node.getNext() != null) {
            IStudentRBTreeNode<KeyType, ValueType> result = this.node.getNext();
            this.node.setNext(null);
            this.node = result;
        } else {
            if (this.node.getSuccessor() == null)
                throw new NoSuchElementException("the iteration has no more tiles");
            this.node = this.node.getSuccessor();
        }
        return this.node;
    }

}
