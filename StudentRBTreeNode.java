import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class StudentRBTreeNode<KeyType extends Comparable<KeyType>, ValueType extends Comparable<ValueType>>
        implements IStudentRBTreeNode<KeyType, ValueType> {

    public KeyType key;
    public List<ValueType> valueList;
    public int blackHeight; // 0 is red, 1 is black, 2 is double black
    public IStudentRBTreeNode<KeyType, ValueType> parent;
    public IStudentRBTreeNode<KeyType, ValueType> leftChild;
    public IStudentRBTreeNode<KeyType, ValueType> rightChild;
    public IStudentRBTreeNode<KeyType,ValueType> next;

    public StudentRBTreeNode(){
        this.key = null;
        this.valueList = new ArrayList<>();
        this.blackHeight = 0;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.next = null;
    }

    public StudentRBTreeNode(KeyType key){
        this();
        this.key = key;
    }

    public StudentRBTreeNode(IStudentRBTreeNode<KeyType, ValueType> next){
        this();
        this.next = next;
    }

    public StudentRBTreeNode(KeyType key, List<ValueType> values) {
        this();
        this.key = key;
        this.valueList = values;
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
        return this.valueList;
    }

    @Override
    public void setValues(List<ValueType> values) {
        this.valueList = values;
    }

    @Override
    public boolean addValue(ValueType value) throws IllegalArgumentException {
        if (value == null) throw new IllegalArgumentException("can't add null value into list");
        this.valueList.add(value);
        return true;
    }

    @Override
    public boolean removeValue(ValueType value) throws NullPointerException, IllegalArgumentException {
        if (value == null) throw new IllegalArgumentException("can't find null value in list");
        this.valueList.remove(value);
        return true;
    }

    @Override
    public int blackHeight() {
        return this.blackHeight;
    }

    @Override
    public boolean isLeftChild() {
        return this.parent != null && parent.getLeft() == this;
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
        return this.leftChild;
    }

    @Override
    public void setLeft(IStudentRBTreeNode<KeyType, ValueType> node) {
        this.leftChild = node;
    }

    @Override
    public IStudentRBTreeNode<KeyType, ValueType> getRight() {
        return this.rightChild;
    }

    @Override
    public void setRight(IStudentRBTreeNode<KeyType, ValueType> node) {
        this.rightChild = node;
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
    public int compareTo(IStudentRBTreeNode<KeyType, ValueType> o) {
        return key.compareTo(o.getKey());
    }

    @Override
    public String toString(){
        return "key: " + this.key.toString() + " values: " + this.valueList.toString();
    }
}