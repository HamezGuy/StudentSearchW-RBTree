import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements a simple BST that is placeholder for the RBTree implementation
 *
 * @param <KeyType>   the type of the key
 * @param <ValueType> the type of the value
 * @author Ruixuan Tu
 */
public class StudentRBTreePlaceholder<KeyType extends Comparable<KeyType>, ValueType extends Comparable<ValueType>>
        implements IStudentRBTree<KeyType, ValueType>, SortedCollectionInterface<IStudentRBTreeNode<KeyType, ValueType>> {
    protected IStudentRBTreeNode<KeyType, ValueType> root; // The root node of the BST
    protected int size = 0; // The number of nodes currently contained in the BST

    @Override
    public boolean add(KeyType key, ValueType value) throws IllegalArgumentException {
        if (key == null || value == null)
            throw new IllegalArgumentException();
        if (this.isEmpty()) {
            List<ValueType> values = new ArrayList<>();
            this.root = new StudentRBTreePlaceholderNode<>(key, values);
            this.root.getValues().add(value);
        } else
            addHelper(key, value, this.root);
        this.size++;
        return true;
    }

    protected void addHelper(KeyType key, ValueType value, IStudentRBTreeNode<KeyType, ValueType> current)
            throws IllegalArgumentException {
        int compare = key.compareTo(current.getKey());
        if (compare == 0) { // key = current, value is already in BST
            current.getValues().add(value);
        } else if (compare < 0) { // key < current, go left
            if (current.getLeft() == null) {
                List<ValueType> values = new ArrayList<>();
                current.setLeft(new StudentRBTreePlaceholderNode<>(key, values));
                current.getLeft().getValues().add(value);
                current.getLeft().setParent(current);
            } else {
                addHelper(key, value, current.getLeft());
            }
        } else if (compare > 0) { // key > current, go right
            if (current.getRight() == null) {
                List<ValueType> values = new ArrayList<>();
                current.setRight(new StudentRBTreePlaceholderNode<>(key, values));
                current.getRight().getValues().add(value);
                current.getRight().setParent(current);
            } else {
                addHelper(key, value, current.getRight());
            }
        }
    }

    @Override
    public boolean insert(IStudentRBTreeNode<KeyType, ValueType> data) throws NullPointerException, IllegalArgumentException {
        try {
            data.getValues().forEach(value -> add(data.getKey(), value));
        } catch (NullPointerException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean contains(IStudentRBTreeNode<KeyType, ValueType> data) {
        for (IStudentRBTreeNode<KeyType, ValueType> next : this) {
            if (next.equals(data)) return true;
        }
        return false;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        if (this.size == 0 || this.root == null) {
            this.clear();
            return true;
        }
        return false;
    }

    public ArrayList<ValueType> get(KeyType key) {
        if (this.isEmpty())
            return new ArrayList<>();
        return getHelper(key, this.root);
    }

    protected ArrayList<ValueType> getHelper(KeyType key, IStudentRBTreeNode<KeyType, ValueType> current) {
        ArrayList<ValueType> result = new ArrayList<>();
        if (current.getKey().equals(key))
            result.addAll(current.getValues());
        // use of BST properties as it is sorted by AUTHOR as the first attribute
        if (current.getLeft() != null)
            if (key.compareTo(current.getLeft().getKey()) <= 0) // go left
                result.addAll(getHelper(key, current.getLeft()));
        if (current.getRight() != null)
            if (key.compareTo(current.getRight().getKey()) >= 0) // go right
                result.addAll(getHelper(key, current.getRight()));
        return result;
    }

    public String toString() {
        Iterator<IStudentRBTreeNode<KeyType, ValueType>> it = this.iterator();
        String result = "";
        while (it.hasNext()) {
            result += it.next() + "\n";
        }
        return result;
    }

    protected IStudentRBTreeNode<KeyType, ValueType> getRoot() {
        return this.root;
    }

    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean removeByKey(KeyType key) {
        Iterator<IStudentRBTreeNode<KeyType, ValueType>> search = search(key);
        if (!search.hasNext()) return false;
        IStudentRBTreeNode<KeyType, ValueType> toRemove = search.next();
        if (toRemove == null) return false;
        if (!toRemove.getKey().equals(key)) return false;
        this.size -= toRemove.getValues().size();
        this.root = removeHelper(root, toRemove.getKey());
        return true;
    }

    @Override
    public boolean removeByValue(ValueType value) {
        Iterator<IStudentRBTreeNode<KeyType, ValueType>> it = this.iterator();
        while (it.hasNext()) {
            IStudentRBTreeNode<KeyType, ValueType> toRemove = it.next();
            while (toRemove.getValues().contains(value)) {
                if (!toRemove.getValues().remove(value)) return false;
                this.size--;
            }
            if (toRemove.getValues().isEmpty()) {
                this.root = removeHelper(root, toRemove.getKey());
                it = this.iterator();
            }
        }
        return true;
    }

    // modified from: https://pages.cs.wisc.edu/~cs400/readings/Binary-Search-Trees/
    protected IStudentRBTreeNode<KeyType, ValueType> removeHelper(IStudentRBTreeNode<KeyType, ValueType> n,
                                                                  KeyType key) {
        if (n == null) {
            return null;
        }

        if (key.equals(n.getKey())) {
            // n is the node to be removed
            if (n.getLeft() == null && n.getRight() == null) {
                updateParent(n.getParent(), n, null);
                return null;
            }
            if (n.getLeft() == null) {
                n.getRight().setParent(n.getParent());
                updateParent(n.getParent(), n, n.getRight());
                return n.getRight();
            }
            if (n.getRight() == null) {
                n.getLeft().setParent(n.getParent());
                updateParent(n.getParent(), n, n.getLeft());
                return n.getLeft();
            }

            // if we get here, then n has 2 children
            IStudentRBTreeNode<KeyType, ValueType> smallNode = smallest(n.getRight());
            n.setKey(smallNode.getKey());
            n.setValues(smallNode.getValues());
            n.setRight(removeHelper(n.getRight(), smallNode.getKey()));
            if (n.getRight() != null) n.getRight().setParent(n);
            return n;
        } else if (key.compareTo(n.getKey()) < 0) {
            n.setLeft(removeHelper(n.getLeft(), key));
            return n;
        } else {
            n.setRight(removeHelper(n.getRight(), key));
            return n;
        }
    }

    protected void updateParent(IStudentRBTreeNode<KeyType, ValueType> parent,
                                IStudentRBTreeNode<KeyType, ValueType> origChild,
                                IStudentRBTreeNode<KeyType, ValueType> newChild) {
        if (parent == null) return;
        if (parent.getLeft() == origChild)
            parent.setLeft(newChild);
        else if (parent.getRight() == origChild)
            parent.setRight(newChild);
    }

    protected IStudentRBTreeNode<KeyType, ValueType> smallest(IStudentRBTreeNode<KeyType, ValueType> current) {
        while (current.getLeft() != null) current = current.getLeft();
        return current;
    }

    @Override
    public Iterator<IStudentRBTreeNode<KeyType, ValueType>> search(KeyType key) {
        if (key == null || this.isEmpty()) return new StudentRBTreePlaceholderIterator<>(null);
        Iterator<IStudentRBTreeNode<KeyType, ValueType>> exactSearchResult = exactSearchHelper(key, this.root);
        if (exactSearchResult != null) return exactSearchResult;
        else return fuzzySearchHelper(key);
    }

    protected Iterator<IStudentRBTreeNode<KeyType, ValueType>> exactSearchHelper
            (KeyType key, IStudentRBTreeNode<KeyType, ValueType> current) {
        int compare = key.compareTo(current.getKey());
        // base case
        if (compare == 0) { // key = current, equal
            IStudentRBTreeNode<KeyType, ValueType> resultNode = current;
            while (resultNode.getPredecessor() != null && resultNode.getPredecessor().getKey() == key)
                resultNode = resultNode.getPredecessor();
            return new StudentRBTreePlaceholderIterator<>(new StudentRBTreePlaceholderNode<>(resultNode));
        }
        // recursive case
        else if (compare < 0) { // key < current, go left
            if (current.getLeft() == null)
                return null;
            return exactSearchHelper(key, current.getLeft());
        } else if (compare > 0) { // key > current, go right
            if (current.getRight() == null)
                return null;
            return exactSearchHelper(key, current.getRight());
        }
        // not found
        return null;
    }

    protected Iterator<IStudentRBTreeNode<KeyType, ValueType>> fuzzySearchHelper(KeyType key) {
        Iterator<IStudentRBTreeNode<KeyType, ValueType>> it = this.iterator();
        IStudentRBTreeNode<KeyType, ValueType> curr;
        IStudentRBTreeNode<KeyType, ValueType> tempNode = new StudentRBTreePlaceholderNode<>(null, null);
        while (it.hasNext()) {
            curr = it.next();
            if (curr.getKey().compareTo(key) > 0) {
                tempNode.setNext(curr);
                return new StudentRBTreePlaceholderIterator<>(tempNode);
            }
        }
        return new StudentRBTreePlaceholderIterator<>(tempNode);
    }

    @Override
    public Iterator<IStudentRBTreeNode<KeyType, ValueType>> iterator() {
        IStudentRBTreeNode<KeyType, ValueType> leftmostNode = root;
        if (leftmostNode != null)
            while (leftmostNode.getPredecessor() != null)
                leftmostNode = leftmostNode.getPredecessor();
        IStudentRBTreeNode<KeyType, ValueType> tempNode = new StudentRBTreePlaceholderNode<>(leftmostNode);
        return new StudentRBTreePlaceholderIterator<>(tempNode);
    }
}
