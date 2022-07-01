// --== CS400 Project two File Header ==--
// Name: Zhilin Du
// CSL Username: zdu
// Email: zdu48@wisc.edu
// Lecture #: 004
// Notes to Grader: Referred to the Iterator in P2W2 RedBlackTree provided by professor

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class StudentRBTreeIterator<KeyType extends Comparable<KeyType>, ValueType extends Comparable<ValueType>>
        implements Iterator<IStudentRBTreeNode<KeyType, ValueType>> {
    private IStudentRBTreeNode<KeyType, ValueType> current;

    public StudentRBTreeIterator(IStudentRBTreeNode<KeyType, ValueType> node){
        this.current = node;
    }
    /**
     * The next method is called for each value in the traversal sequence.
     * It returns one value at a time.
     * @return next value in the sequence of the traversal
     * @throws NoSuchElementException if there is no more elements in the sequence
     */
    public IStudentRBTreeNode<KeyType, ValueType> next() {
        if (current.getNext() != null){
            IStudentRBTreeNode<KeyType, ValueType> res = current.getNext();
            this.current.setNext(null);
            this.current = res;
            return res;
        }
        else {
            if (current.getSuccessor() == null) throw new NoSuchElementException("There are no more elements in the tree");
            else {
                current = current.getSuccessor();
                return current;
            }
        }
    }


    /**
     * Returns a boolean that indicates if the iterator has more elements (true),
     * or if the traversal has finished (false)
     * @return boolean indicating whether there are more elements / steps for the traversal
     */
    public boolean hasNext() {
        return this.current != null && (this.current.getNext() != null || this.current.getSuccessor() != null);
    }
}
