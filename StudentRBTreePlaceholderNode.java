import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a node in the placeholder BST
 *
 * @param <KeyType>   The type of the key
 * @param <ValueType> The type of the value
 * @author Ruixuan Tu
 */
public class StudentRBTreePlaceholderNode<KeyType extends Comparable<KeyType>, ValueType extends Comparable<ValueType>>
        implements IStudentRBTreeNode<KeyType, ValueType> {
    protected KeyType key;
    protected List<ValueType> values;
    protected IStudentRBTreeNode<KeyType, ValueType> parent;
    protected IStudentRBTreeNode<KeyType, ValueType> left;
    protected IStudentRBTreeNode<KeyType, ValueType> right;
    protected IStudentRBTreeNode<KeyType, ValueType> next;

    public StudentRBTreePlaceholderNode(IStudentRBTreeNode<KeyType, ValueType> next) {
        this.next = next;
    }

    public StudentRBTreePlaceholderNode(KeyType key, List<ValueType> values) {
        this.key = key;
        this.values = values;
    }

    public StudentRBTreePlaceholderNode(KeyType key, List<ValueType> values,
                                        IStudentRBTreeNode<KeyType, ValueType> parent,
                                        IStudentRBTreeNode<KeyType, ValueType> left,
                                        IStudentRBTreeNode<KeyType, ValueType> right,
                                        IStudentRBTreeNode<KeyType, ValueType> next) {
        this.key = key;
        this.values = values;
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.next = next;
    }

    @Override
    public KeyType getKey() {
        return this.key;
    }

    @Override
    public void setKey(KeyType key) {
        this.key = key;
    }

    @Override
    public List<ValueType> getValues() {
        return this.values;
    }

    @Override
    public void setValues(List<ValueType> values) {
        this.values = values;
    }

    @Override
    public boolean addValue(ValueType value) throws IllegalArgumentException {
        return this.values.add(value);
    }

    @Override
    public boolean removeValue(ValueType value) throws NullPointerException, IllegalArgumentException {
        return this.values.remove(value);
    }

    @Override
    public int blackHeight() {
        return 0;
    }

    @Override
    public boolean isLeftChild() {
        try {
            return this.parent.getLeft() == this;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public IStudentRBTreeNode<KeyType, ValueType> getParent() {
        return this.parent;
    }

    @Override
    public void setParent(IStudentRBTreeNode<KeyType, ValueType> node) {
        this.parent = node;
    }

    @Override
    public IStudentRBTreeNode<KeyType, ValueType> getLeft() {
        return this.left;
    }

    @Override
    public void setLeft(IStudentRBTreeNode<KeyType, ValueType> node) {
        this.left = node;
    }

    @Override
    public IStudentRBTreeNode<KeyType, ValueType> getRight() {
        return this.right;
    }

    @Override
    public void setRight(IStudentRBTreeNode<KeyType, ValueType> node) {
        this.right = node;
    }

    @Override
    public IStudentRBTreeNode<KeyType, ValueType> getNext() {
        return this.next;
    }

    @Override
    public void setNext(IStudentRBTreeNode<KeyType, ValueType> node) {
        this.next = node;
    }

    @Override
    public IStudentRBTreeNode<KeyType, ValueType> getSuccessor() {
        if (this.getRight() != null) {
            IStudentRBTreeNode<KeyType, ValueType> successor = this.getRight();
            while (successor.getLeft() != null) successor = successor.getLeft();
            return successor;
        }
        IStudentRBTreeNode<KeyType, ValueType> node = this;
        IStudentRBTreeNode<KeyType, ValueType> parent = this.getParent();
        while (parent != null && node == node.getParent().getRight()) {
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    @Override
    public IStudentRBTreeNode<KeyType, ValueType> getPredecessor() {
        if (this.getLeft() != null) {
            IStudentRBTreeNode<KeyType, ValueType> predecessor = this.getLeft();
            while (predecessor.getRight() != null) predecessor = predecessor.getRight();
            return predecessor;
        }
        IStudentRBTreeNode<KeyType, ValueType> node = this;
        IStudentRBTreeNode<KeyType, ValueType> parent = this.getParent();
        while (parent != null && node == node.getParent().getLeft()) {
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    @Override
    public String toString() {
        return "Node{key=" + key + ", values=" + values + ", parent=" + (parent == null ? "null" : parent.hashCode())
                + ", left=" + (left == null ? "null" : left.hashCode()) + ", right="
                + (right == null ? "null" : right.hashCode()) + ", next="
                + (next == null ? "null" : next.hashCode()) + "}";
    }

    @Override
    public int compareTo(IStudentRBTreeNode<KeyType, ValueType> o) {
        return key.compareTo(o.getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRBTreePlaceholderNode<?, ?> that = (StudentRBTreePlaceholderNode<?, ?>) o;
        return Objects.equals(key, that.key) && Objects.equals(values, that.values) && Objects.equals(parent, that.parent) && Objects.equals(left, that.left) && Objects.equals(right, that.right) && Objects.equals(next, that.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, values, parent, left, right, next);
    }
}
