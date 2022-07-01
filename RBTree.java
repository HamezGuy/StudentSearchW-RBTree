// --== CS400 Project two File Header ==--
// Name: Zhilin Du
// CSL Username: zdu
// Email: zdu48@wisc.edu
// Lecture #: 004
// Notes to Grader: <any optional extra notes to your grader>

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Red-Black Tree implementation with a Node inner class for representing
 * the nodes of the tree. Currently, this implements a Binary Search Tree that
 * we will turn into a red black tree by modifying the insert functionality.
 * In this activity, we will start with implementing rotations for the binary
 * search tree insert algorithm. You can use this class' insert method to build
 * a regular binary search tree, and its toString method to display a level-order
 * traversal of the tree.
 */
public class RBTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

    /**
     * This class represents a node holding a single value within a binary tree
     * the parent, left, and right child references are always maintained.
     */
    protected static class Node<T> {
        public T data;
        public Node<T> parent; // null for root node
        public Node<T> leftChild;
        public Node<T> rightChild;
        public int blackHeight; // 0 = red, 1 = black, 2 = double black
        public Node(T data) {
            this.data = data;
            this.blackHeight = 0;
        }
        /**
         * @return true when this node has a parent and is the left child of
         * that parent, otherwise return false
         */
        public boolean isLeftChild() {
            return parent != null && parent.leftChild == this;
        }

    }

    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    /**
     * Performs a naive insertion into a binary search tree: adding the input
     * data value to a new node in a leaf position within the tree. After  
     * this insertion, no attempt is made to restructure or balance the tree.
     * This tree will not hold null references, nor duplicate data values.
     * @param data to be added into this binary search tree
     * @return true if the value was inserted, false if not
     * @throws NullPointerException when the provided data argument is null
     * @throws IllegalArgumentException when the newNode and subtree contain
     *      equal data references
     */
    @Override
    public boolean insert(T data) throws NullPointerException, IllegalArgumentException {
        // null references cannot be stored within this tree
        if(data == null) throw new NullPointerException(
                "This RedBlackTree cannot store null references.");

        Node<T> newNode = new Node<>(data);
        if(root == null) {
            root = newNode;
            size++;
            this.root.blackHeight = 1; // add first node to an empty tree
            return true;
        }
        else{
            boolean returnValue = insertHelper(newNode,root); // recursively insert into subtree
            if (returnValue) size++;
            else throw new IllegalArgumentException(
                    "This RedBlackTree already contains that value.");
            this.root.blackHeight = 1; // root is always black
            return returnValue;
        }
    }

    /**
     * Recursive helper method to find the subtree with a null reference in the
     * position that the newNode should be inserted, and then extend this tree
     * by the newNode in that position.
     * @param newNode is the new node that is being added to this tree
     * @param subtree is the reference to a node within this tree which the 
     *      newNode should be inserted as a descenedent beneath
     * @return true is the value was inserted in subtree, false if not
     */
    private boolean insertHelper(Node<T> newNode, Node<T> subtree) {
        int compare = newNode.data.compareTo(subtree.data);
        // do not allow duplicate values to be stored within this tree
        if(compare == 0) return false;

            // store newNode within left subtree of subtree
        else if(compare < 0) {
            if(subtree.leftChild == null) { // left subtree empty, add here
                subtree.leftChild = newNode;
                newNode.parent = subtree;
                enforceRBTreePropertiesAfterInsert(subtree.leftChild);
                return true;
                // otherwise continue recursive search for location to insert
            } else return insertHelper(newNode, subtree.leftChild);
        }

        // store newNode within the right subtree of subtree
        else {
            if(subtree.rightChild == null) { // right subtree empty, add here
                subtree.rightChild = newNode;
                newNode.parent = subtree;
                enforceRBTreePropertiesAfterInsert(subtree.rightChild);
                return true;
                // otherwise continue recursive search for location to insert
            } else return insertHelper(newNode, subtree.rightChild);
        }
    }

    /**
     * This method recursively checks if the tree will stay valid after insert new node, and fix the error if needed.
     *
     * @param child is the node that need to check if violation happens
     */
    protected void enforceRBTreePropertiesAfterInsert(Node<T> child) {
        int temp;

        // if child == null, return
        if (child == null) return;

            // if parent is root, stop recursion
        else if (child.equals(root)) return;

            // if parent or child is black, no violation happens
        else if (child.parent.blackHeight != 0 || child.blackHeight != 0) return;

            // if child has grandparent
            // case 3: if parent and its sibling are red, set both parents to black and set grandparent to red
        else if (child.parent.parent != null &&
                child.parent.parent.leftChild != null && child.parent.parent.rightChild != null &&
                child.parent.parent.leftChild.blackHeight == 0 && child.parent.parent.rightChild.blackHeight == 0){
            Node<T> grandparent = child.parent.parent;
            grandparent.leftChild.blackHeight = 1;
            grandparent.rightChild.blackHeight = 1;
            grandparent.blackHeight = 0;
            enforceRBTreePropertiesAfterInsert(child.parent.parent);
            // finish checking invalid node, return
            return;
        }

        // case 1: if child is same side as parent's sibling, rotate child and parent and go to case 2
        else {
            if ((child.parent.isLeftChild() && !child.isLeftChild())){
                // parent left, child right
                rotate(child, child.parent);
                child = child.leftChild;
            }
            else if (!child.parent.isLeftChild() && child.isLeftChild()){
                rotate(child, child.parent);
                child = child.rightChild;
            }

            // case 2: if child is opposite side as parent's sibling, rotate parent and grandparent, then change color
            if ((child.parent.isLeftChild() && child.isLeftChild()) ||
                    (!child.parent.isLeftChild() && !child.isLeftChild())){
                rotate(child.parent, child.parent.parent);
                // recursively go upper level, then check parent and grandparent's relation
                enforceRBTreePropertiesAfterInsert(child.parent.parent);
            }
        }
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a leftChild of the provided parent, this
     * method will perform a right rotation. When the provided child is a
     * rightChild of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this method
     * will throw an IllegalArgumentException.
     * @param child is the node being rotated from child to parent position
     *      (between these two node arguments)
     * @param parent is the node being rotated from parent to child position
     *      (between these two node arguments)
     * @throws IllegalArgumentException when the provided child and parent
     *      node references are not initially (pre-rotation) related that way
     * @return the rotated child node
     */
    private void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
        if (child == null || parent == null)
            throw new IllegalArgumentException("The provided child or parent node references are null.");

        if (!(child.parent.equals(parent)))
            throw new IllegalArgumentException("The provided nodes are not parent child relationships.");

        Node<T> grandparent = parent.parent;
        Node<T> newChild = null;
        int childH = child.blackHeight;
        int parentH = parent.blackHeight;
        child.blackHeight = parentH;
        parent.blackHeight = childH;

        // rotate to right
        if(parent.leftChild != null && parent.leftChild.equals(child)){
            // if grandparent exist, set child as grandparent's child, else set child as root of the tree
            if (grandparent != null){
                child.parent = grandparent;
                if (grandparent.leftChild != null && grandparent.leftChild.equals(parent)){
                    grandparent.leftChild = child;
                }
                else{
                    grandparent.rightChild = child;
                }
            }
            else {
                root = child; // grandparent == null, child is new root
                child.parent = null;
            }

            // if child's right child is not null, set it as parent's left child
            parent.leftChild = child.rightChild;
            if (child.rightChild != null){
                child.rightChild.parent = parent;
            }

            // swap child and parent's relation
            child.rightChild = parent;
            parent.parent = child;
        } // rotate to left
        else if(parent.rightChild != null && parent.rightChild.equals(child)){
            // if grandparent exist, set child as grandparent's child, else set child as root of the tree
            if (grandparent != null){
                child.parent = grandparent;
                if (grandparent.leftChild != null && grandparent.leftChild.equals(parent)){
                    grandparent.leftChild = child;
                }
                else{
                    grandparent.rightChild = child;
                }
            }
            else {
                child.parent = null;
                root = child; // grandparent == null, child is new root
            }

            // if child's left child is not null, set it as parent's right child
            parent.rightChild = child.leftChild;
            if (child.leftChild != null){
                child.leftChild.parent = parent;
            }

            // swap parent and child's relation
            parent.parent = child;
            child.leftChild = parent;
        }
        else throw new IllegalArgumentException("Cannot rotate these two nodes");
    }

    /**
     * Get the size of the tree (its number of nodes).
     * @return the number of nodes in the tree
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Method to check if the tree is empty (does not contain any node).
     * @return true of this.size() return 0, false if this.size() > 0
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Checks whether the tree contains the value *data*.
     * @param data the data value to test for
     * @return true if *data* is in the tree, false if it is not in the tree
     */
    @Override
    public boolean contains(T data) {
        // null references will not be stored within this tree
        if(data == null) throw new NullPointerException(
                "This RedBlackTree cannot store null references.");
        return this.containsHelper(data, root);
    }

    /**
     * Recursive helper method that recurses through the tree and looks
     * for the value *data*.
     * @param data the data value to look for
     * @param subtree the subtree to search through
     * @return true of the value is in the subtree, false if not
     */
    private boolean containsHelper(T data, Node<T> subtree) {
        if (subtree == null) {
            // we are at a null child, value is not in tree
            return false;
        } else {
            int compare = data.compareTo(subtree.data);
            if (compare < 0) {
                // go left in the tree
                return containsHelper(data, subtree.leftChild);
            } else if (compare > 0) {
                // go right in the tree
                return containsHelper(data, subtree.rightChild);
            } else {
                // we found it :)
                return true;
            }
        }
    }

    /**
     * Returns an iterator over the values in in-order (sorted) order.
     * @return iterator object that traverses the tree in in-order sequence
     */
    @Override
    public Iterator<T> iterator() {
        // use an anonymous class here that implements the Iterator interface
        // we create a new on-off object of this class everytime the iterator
        // method is called
        return new Iterator<T>() {
            // a stack and current reference store the progress of the traversal
            // so that we can return one value at a time with the Iterator
            Stack<Node<T>> stack = null;
            Node<T> current = root;

            /**
             * The next method is called for each value in the traversal sequence.
             * It returns one value at a time.
             * @return next value in the sequence of the traversal
             * @throws NoSuchElementException if there is no more elements in the sequence
             */
            public T next() {
                // if stack == null, we need to initialize the stack and current element
                if (stack == null) {
                    stack = new Stack<Node<T>>();
                    current = root;
                }
                // go left as far as possible in the sub tree we are in un8til we hit a null
                // leaf (current is null), pushing all the nodes we fund on our way onto the
                // stack to process later
                while (current != null) {
                    stack.push(current);
                    current = current.leftChild;
                }
                // as long as the stack is not empty, we haven't finished the traversal yet;
                // take the next element from the stack and return it, then start to step down
                // its right subtree (set its right sub tree to current)
                if (!stack.isEmpty()) {
                    Node<T> processedNode = stack.pop();
                    current = processedNode.rightChild;
                    return processedNode.data;
                } else {
                    // if the stack is empty, we are done with our traversal
                    throw new NoSuchElementException("There are no more elements in the tree");
                }

            }

            /**
             * Returns a boolean that indicates if the iterator has more elements (true),
             * or if the traversal has finished (false)
             * @return boolean indicating whether there are more elements / steps for the traversal
             */
            public boolean hasNext() {
                // return true if we either still have a current reference, or the stack
                // is not empty yet
                return !(current == null && (stack == null || stack.isEmpty()) );
            }

        };
    }

    public Iterator<Node<T>> Nodeiterator() {
        // use an anonymous class here that implements the Iterator interface
        // we create a new on-off object of this class everytime the iterator
        // method is called
        return new Iterator<Node<T>>() {
            // a stack and current reference store the progress of the traversal
            // so that we can return one value at a time with the Iterator
            Stack<Node<T>> stack = null;
            Node<T> current = root;

            /**
             * The next method is called for each value in the traversal sequence.
             * It returns one value at a time.
             * @return next value in the sequence of the traversal
             * @throws NoSuchElementException if there is no more elements in the sequence
             */
            public Node<T> next() {
                // if stack == null, we need to initialize the stack and current element
                if (stack == null) {
                    stack = new Stack<Node<T>>();
                    current = root;
                }
                // go left as far as possible in the sub tree we are in un8til we hit a null
                // leaf (current is null), pushing all the nodes we fund on our way onto the
                // stack to process later
                while (current != null) {
                    stack.push(current);
                    current = current.leftChild;
                }
                // as long as the stack is not empty, we haven't finished the traversal yet;
                // take the next element from the stack and return it, then start to step down
                // its right subtree (set its right sub tree to current)
                if (!stack.isEmpty()) {
                    Node<T> processedNode = stack.pop();
                    current = processedNode.rightChild;
                    return processedNode;
                } else {
                    // if the stack is empty, we are done with our traversal
                    throw new NoSuchElementException("There are no more elements in the tree");
                }

            }

            /**
             * Returns a boolean that indicates if the iterator has more elements (true),
             * or if the traversal has finished (false)
             * @return boolean indicating whether there are more elements / steps for the traversal
             */
            public boolean hasNext() {
                // return true if we either still have a current reference, or the stack
                // is not empty yet
                return !(current == null && (stack == null || stack.isEmpty()) );
            }

        };
    }

    /**
     * This method performs an inorder traversal of the tree. The string 
     * representations of each data value within this tree are assembled into a
     * comma separated string within brackets (similar to many implementations 
     * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
     * Note that this RedBlackTree class implementation of toString generates an
     * inorder traversal. The toString of the Node class class above
     * produces a level order traversal of the nodes / values of the tree.
     * @return string containing the ordered values of this tree (in-order traversal)
     */
    public String toInOrderString() {
        // use the inorder Iterator that we get by calling the iterator method above
        // to generate a string of all values of the tree in (ordered) in-order
        // traversal sequence
        Iterator<T> treeNodeIterator = this.iterator();
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (treeNodeIterator.hasNext()) {
            T data = treeNodeIterator.next();
            if (data != null) {
                sb.append(data);
            }
            else{
                while(treeNodeIterator.hasNext()){
                    data = treeNodeIterator.next();
                    if (data != null){
                        sb.append(data.toString());
                        break;
                    }
                }
            }
        }
        while (treeNodeIterator.hasNext()) {
            T data = treeNodeIterator.next();
            if (data != null){
                sb.append(", ");
                sb.append(data.toString());
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public boolean remove(T data){
        if (data == null) return false;
        if (this.contains(data) == false) return false;
        return removeHelper(data, root);
    }

    private boolean removeHelper(T data, Node<T> subtree){
        if (subtree == null) {
            // we are at a null child, value is not in tree
            return false;
        } else {
            int compare = data.compareTo(subtree.data);
            if (compare < 0) {
                // go left
                return removeHelper(data, subtree.leftChild);
            } else if (compare > 0) {
                // go right
                return removeHelper(data, subtree.rightChild);
            } else {
                // we found it
                return delNodeBT(subtree);
            }
        }
    }
    private boolean delNodeBT(Node<T> node){
        if (node == this.root) {
            this.root = null;
            return true;
        }
        // case 1: 0 child
        else if (node.rightChild == null && node.leftChild == null){
            if (node.isLeftChild())
                node.parent.leftChild = null;
            else
                node.parent.rightChild = null;
        }
        else if (node.rightChild != null && node.leftChild == null){
            Node<T> tParent = node.parent;
            Node<T> tChild = node.rightChild;
            if (node.isLeftChild()){
                tParent.leftChild = tChild;
            }
            else{
                tParent.rightChild = tChild;
            }
            tChild.parent = tParent;
            return true;
        }
        else if (node.rightChild == null && node.leftChild != null){
            Node<T> tParent = node.parent;
            Node<T> tChild = node.leftChild;
            if (node.isLeftChild()){
                tParent.leftChild = tChild;
            }
            else{
                tParent.rightChild = tChild;
            }
            tChild.parent = tParent;
            return true;
        }
        else{
            Node<T> pre = node.leftChild;
            while(pre.rightChild != null){
                pre = pre.rightChild;
            }
            node.data = pre.data;
            if (pre.isLeftChild()){
                pre.parent.leftChild = null;
            }
            else
                pre.parent.rightChild = null;
            return true;
        }
        return false;
    }
    private boolean delNode(Node<T> node){
        // case 1: 0 child
        if (node.rightChild == null && node.leftChild == null){
            // check color, if red, just remove it
            if (node.blackHeight == 0){
                // if node isLeftChild, remove p's leftChild, else remove rightChild
                if (node.isLeftChild())
                    node.parent.leftChild = null;
                else
                    node.parent.rightChild = null;
                return true;
            }
            // if black, check if both child & parent are black
            if (node.blackHeight == 1){
                // if parent is red, set parent to black and remove it
                if (node.parent.blackHeight == 0){
                    node.parent.blackHeight = 1;
                    if (node.isLeftChild())
                        node.parent.leftChild = null;
                    else
                        node.parent.rightChild = null;
                    return true;
                }
                if (node.parent.blackHeight == 1) {
                    node.data = null;
                    node.blackHeight = 2;
                    doubleBlackEnforce(node);
                    return true;
                }
            }
        }
        // case 2: 1 child
        else if (node.rightChild != null && node.leftChild == null){
            // only have right child, set color of child to black and then remove node
            node.rightChild.blackHeight = 1;
            Node<T> tParent = node.parent;
            Node<T> tChild = node.rightChild;
            if (node.isLeftChild()){
                tParent.leftChild = tChild;
            }
            else{
                tParent.rightChild = tChild;
            }
            tChild.parent = tParent;
            return true;
        }
        else if (node.rightChild == null && node.leftChild != null){
            // only have left child, set color of child to black and then remove node
            // if node black, child must be red. If node red, child must be black
            node.leftChild.blackHeight = 1;
            Node<T> tParent = node.parent;
            Node<T> tChild = node.leftChild;
            if (node.isLeftChild()){
                tParent.leftChild = tChild;
            }
            else{
                tParent.rightChild = tChild;
            }
            tChild.parent = tParent;
            return true;
        }
        // case 3: 2 children
        else {
            // use predecessor to replace it and call this method again to delete the predecessor node
            Node<T> pre = node.leftChild;
            while(pre.rightChild != null){
                pre = pre.rightChild;
            }
            // set data in predecessor to original node
            node.data = pre.data;
            // delete the predecessor
            delNode(pre);
            return true;
        }
        return false;
    }

    private void doubleBlackEnforce(Node<T> node){
        node.blackHeight = 2;
        // check if node is root, if is, return an empty tree
        if (node.equals(this.root)) {
            node.blackHeight--;
            return;
        }
        Node<T> sibling;
        Node<T> parent = node.parent;
        int cHeight = 0;
        if (node.parent.leftChild == null || node.parent.rightChild == null) return; // no sibling, not a valid RBTree

        if (node.parent.leftChild.blackHeight == 1 && node.parent.rightChild.blackHeight == 1){
            // case 1: node's sibling is black and its child has at least a red
            // case 2: node's sibling is black and its both children are black
            if (node.isLeftChild()){
                // node left, sib right
                sibling = node.parent.rightChild;
                if (sibling.leftChild != null && sibling.leftChild.blackHeight == 0){
                    // case 1: RL
                    // sibling's leftChild set to parent's color
                    Node<T> sibChild = sibling.leftChild;
                    sibling.leftChild.blackHeight = parent.blackHeight;
                    // rotate sibling's leftChild and sibling
                    rotate(sibling.leftChild, sibling);
                    // rotate parent and sibling
                    rotate(sibChild, parent);
                    parent.blackHeight = 1;
                    if (node.data == null) {
                        if (node.isLeftChild())
                            parent.leftChild = null;
                        else
                            parent.rightChild = null;
                    }
                    return;
                }
                if (sibling.rightChild != null && sibling.rightChild.blackHeight == 0){
                    // case 1: RR
                    // sibling's right child's color set as sibling's color, sibling set as parent's color
                    sibling.rightChild.blackHeight = sibling.blackHeight;
                    sibling.blackHeight = parent.blackHeight;
                    // rotate sibling and parent
                    rotate(sibling, parent);
                    parent.blackHeight = 1;
                    if (node.data == null) {
                        if (node.isLeftChild())
                            parent.leftChild = null;
                        else
                            parent.rightChild = null;
                    }
                    return;
                }

                // case 2: sibling's both children are black or null

                case2Check(node, sibling, parent, cHeight);
            }
            else{
                // node right, sib left
                sibling = node.parent.leftChild;
                if (sibling.leftChild != null && sibling.leftChild.blackHeight == 0){
                    // case 1: LL, remove node with null data
                    // sibling's left child set as sibling's color, sibling set as parent's color
                    sibling.leftChild.blackHeight = sibling.blackHeight;
                    sibling.blackHeight = parent.blackHeight;
                    // rotate sibling and parent
                    rotate(sibling, sibling.parent);
                    parent.blackHeight = 1;
                    if (node.data == null) {
                        if (node.isLeftChild())
                            parent.leftChild = null;
                        else
                            parent.rightChild = null;
                    }
                    return;
                }
                if (sibling.rightChild != null && sibling.rightChild.blackHeight == 0){
                    // case 1: LR, set sibling's rightChild to parent's color
                    Node<T> sibChild = sibling.rightChild;
                    sibling.rightChild.blackHeight = parent.blackHeight;
                    // rotate sibling's right child and sibling
                    rotate(sibling.rightChild, sibling);
                    // rotate new sibling (previous child) and parent
                    rotate(sibChild, parent);
                    parent.blackHeight = 1;
                    if (node.data == null) {
                        if (node.isLeftChild())
                            parent.leftChild = null;
                        else
                            parent.rightChild = null;
                    }
                    return;
                }

                // case 2: sibling's both children are black or null
                case2Check(node, sibling, parent, cHeight);
            }

        }
        if (node.parent.leftChild.blackHeight == 0 || node.parent.rightChild.blackHeight == 0){
            // case 3: node's sibling is red
            if (node.isLeftChild()){
                // node left, sibling right
                sibling = node.parent.rightChild;
            }
            else {
                // node right, sibling left
                sibling = node.parent.leftChild;

            }
            // rotate sibling and parent
            // sibling to red, parent to black, double black to black
            rotate(sibling, parent);
            sibling.blackHeight = 0;
            parent.blackHeight = 1;
            node.blackHeight = 1;
            return;
        }
    }

    private void case2Check(Node<T> node, Node<T> sibling, Node<T> parent, int cHeight) {
        if (sibling.rightChild == null)
            cHeight++;
        else if (sibling.rightChild.blackHeight == 1)
            cHeight++;
        if (sibling.leftChild == null)
            cHeight++;
        else if (sibling.leftChild.blackHeight == 1)
            cHeight++;

        if (cHeight == 2){
            // case 2: sibling's both children are black
            if (parent.blackHeight == 1){
                // parent' color is black
                // sibling's color change to red
                sibling.blackHeight = 0;
                // parent's color change to double black and recursively call it
                parent.blackHeight = 2;
                if (node.data == null) {
                    if (node.isLeftChild())
                        parent.leftChild = null;
                    else
                        parent.rightChild = null;
                }
                doubleBlackEnforce(parent);
            }
            else {
                // parent's color is red, sibling to red, parent to black, double black to black
                sibling.blackHeight = 0;
                parent.blackHeight = 1;
                node.blackHeight = 1;
                return;
            }
        }
    }

    /**
     * This method performs a level order traversal of the tree rooted
     * at the current node. The string representations of each data value
     * within this tree are assembled into a comma separated string within
     * brackets (similar to many implementations of java.util.Collection).
     * Note that the Node's implementation of toString generates a level
     * order traversal. The toString of the RedBlackTree class below
     * produces an inorder traversal of the nodes / values of the tree.
     * This method will be helpful as a helper for the debugging and testing
     * of your rotation implementation.
     * @return string containing the values of this tree in level order
     */
    public String toLevelOrderString() {
        String output = "[ ";
        LinkedList<Node<T>> q = new LinkedList<>();
        q.add(this.root);
        while(!q.isEmpty()) {
            Node<T> next = q.removeFirst();
            if(next.leftChild != null) q.add(next.leftChild);
            if(next.rightChild != null) q.add(next.rightChild);
            if(next.data != null){
                output += next.data.toString();
                if(!q.isEmpty()) output += ", ";
            }
        }
        return output + " ]";
    }

    @Override
    public String toString() {
        if (this.root == null) return "";
        return "level order: " + this.toLevelOrderString() +
                "\nin order: " + this.toInOrderString();
    }
}