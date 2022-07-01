import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class StudentRBTree<KeyType extends Comparable<KeyType>, ValueType extends Comparable<ValueType>>
        extends RBTree<IStudentRBTreeNode<KeyType, ValueType>> implements IStudentRBTree<KeyType, ValueType>,
        SortedCollectionInterface<IStudentRBTreeNode<KeyType, ValueType>>{

    protected RBTree<IStudentRBTreeNode<KeyType, ValueType>> tree;
    protected int size;

    public StudentRBTree(){
        tree = new RBTree<>();
        size = 0;
    }
    @Override
    public boolean add(KeyType key, ValueType value) throws IllegalArgumentException {
        if (key == null || value == null) throw new IllegalArgumentException("cannot insert null key/value");
        if (this.keyExist(key)){
            Iterator<IStudentRBTreeNode<KeyType, ValueType>> temp = this.searchExact(key);
            IStudentRBTreeNode<KeyType, ValueType> node = temp.next();
            if (node.getKey().equals(key)){
                node.addValue(value);
                this.size++;
            }
            else return false;// no valid node found, return false
        }
        else{
            IStudentRBTreeNode<KeyType, ValueType> input = new StudentRBTreeNode<KeyType, ValueType>(key);
            input.addValue(value);
            this.insert(input);
            this.size++;
        }
        return true;
    }

    @Override
    public boolean removeByKey(KeyType key) {
        if (key == null) return false;
        if (searchExact(key) == null) return false;
        IStudentRBTreeNode<KeyType, ValueType> node = searchExact(key).next();
        if (node == null) return false;
        tree.remove(node);
        this.size--;
        return true;
    }

    @Override
    public boolean removeByValue(ValueType value) {
        if (value == null) return false;
        Iterator<IStudentRBTreeNode<KeyType, ValueType>> itr = this.iterator();
        IStudentRBTreeNode<KeyType, ValueType> next;
        List<ValueType> values;
        int removed, valueSize;

        while (itr.hasNext()){
            next = itr.next();
            if(next != null){
                values = next.getValues();
                valueSize = values.size();
                removed = 0;
                for (int i = 0; i < valueSize; i++){
                    if (values.get(i - removed).equals(value)) {
                        values.remove(i - removed);
                        this.size--;
                        removed++;
                    }
                }
                if (values.size() == 0) tree.remove(next);
            }
        }
        return true;
    }

    public Iterator<IStudentRBTreeNode<KeyType, ValueType>> searchExact(KeyType key) {
        if (key == null) return null;
        if (this.keyExist(key)){
            Iterator<IStudentRBTreeNode<KeyType, ValueType>> itr = this.iterator();
            IStudentRBTreeNode<KeyType, ValueType> next;
            while(itr.hasNext()){
                next = itr.next();
                if(next != null){
                    if (next.getKey().equals(key)){
                        if (next.getPredecessor() != null)
                            return new StudentRBTreeIterator<KeyType, ValueType>(next.getPredecessor());
                        IStudentRBTreeNode<KeyType, ValueType> tem = new StudentRBTreeNode<>(next);
                        return new StudentRBTreeIterator<KeyType, ValueType>(tem);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Iterator<IStudentRBTreeNode<KeyType, ValueType>> search(KeyType key) {
        Iterator<IStudentRBTreeNode<KeyType, ValueType>> result = searchExact(key);
        if (result != null) return result;
        else{
            Iterator<IStudentRBTreeNode<KeyType, ValueType>> itr = this.iterator();
            IStudentRBTreeNode<KeyType, ValueType> next, temp = new StudentRBTreeNode<>();
            if (itr == null) return null;
            while (itr.hasNext()){
                next = itr.next();
                if (next.getKey().compareTo(key) > 0){
                    temp.setNext(next);
                    return new StudentRBTreeIterator<>(temp);
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(IStudentRBTreeNode<KeyType, ValueType> data) throws NullPointerException, IllegalArgumentException {
        if (data == null) throw new IllegalArgumentException("data could not be null");
        tree.insert(data);
        this.overrideNode();
        return true;
    }

    @Override
    public boolean contains(IStudentRBTreeNode<KeyType, ValueType> data) {
        return tree.contains(data);
    }

    public boolean keyExist(KeyType key){
        if (this.tree.root == null) return false;
        StudentRBTreeIterator<KeyType, ValueType> itr = this.iterator();
        while(itr.hasNext()){
            IStudentRBTreeNode<KeyType, ValueType> next = itr.next();
            if(next != null){
                if (next.getKey().equals(key))
                    return true;
            }
        }
        return false;
    }

    @Override
    public StudentRBTreeIterator<KeyType, ValueType> iterator() {
        if (this.tree.root == null) return null;
        IStudentRBTreeNode<KeyType, ValueType> left = this.tree.root.data;
        if (left == null) return null;
        else {
            while (left.getPredecessor() != null){
                left = left.getPredecessor();
            }
        }
        IStudentRBTreeNode<KeyType, ValueType> pre = new StudentRBTreeNode<KeyType, ValueType>(left);
        return new StudentRBTreeIterator<>(pre);
    }

    @Override
    public String toString(){
        String res = "";
        Iterator<IStudentRBTreeNode<KeyType, ValueType>> itr = this.iterator();
        if (this.tree.root == null) return res;
        else {
            while (itr.hasNext()){
                res += itr.next() + "\n";
            }
        }

        return res;
    }

    private void overrideNode(){
        Iterator<Node<IStudentRBTreeNode<KeyType, ValueType>>> itr = this.tree.Nodeiterator();
        Node<IStudentRBTreeNode<KeyType, ValueType>> next;
        IStudentRBTreeNode<KeyType, ValueType> stu;
        while (itr.hasNext()){
            next = itr.next();
            stu = next.data;
            stu.setParent(null);
            stu.setRight(null);
            stu.setLeft(null);
            if (next.leftChild != null)
                stu.setLeft(next.leftChild.data);
            if (next.rightChild != null)
                stu.setRight(next.rightChild.data);
            if (next.parent != null)
                stu.setParent(next.parent.data);
        }
    }

    public int size(){ return this.size; }
}
