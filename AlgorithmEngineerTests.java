import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AlgorithmEngineerTests {
    protected StudentRBTree<Integer, Integer> tree;
    protected StudentRBTree<Double, StudentWrapper<Double>> scoreTree;
    protected SISBackend backend;

    @BeforeEach
    public void setup(){
        tree = new StudentRBTree<>();
        scoreTree = new StudentRBTree<>();
        backend = new SISBackend();
    }

    /**
     * This method test if this RBTree can add and remove one student
     *
     */
    @Test
    public void test1(){
        tree.add(1, 1);
        IStudentRBTreeNode<Integer, Integer> node1 = new StudentRBTreeNode<>(1);
        node1.addValue(1);
        assertEquals(true, tree.contains(node1));
        assertEquals("level order: [ key: 1 values: [1] ]\n" +
                "in order: [ key: 1 values: [1] ]", tree.tree.toString());
        tree.removeByKey(1);
        assertEquals("", tree.tree.toString());
    }

    /**
     * This method test if this RBTree can search in a tree only contains one student
     *
     */
    @Test
    public void test2(){
        tree.add(1, 1);
        Iterator<IStudentRBTreeNode<Integer, Integer>> itr = tree.search(1);
        IStudentRBTreeNode<Integer, Integer> node = new StudentRBTreeNode<>();
        node.setKey(1);
        node.addValue(1);
        assertEquals(node.toString(), itr.next().toString());
    }

    /**
     * This method test if this RBTree can add and remove multiple students
     *
     */
    @Test
    public void test3(){
        tree.add(1, 1);
        assertEquals("key: 1 values: [1]\n", tree.toString());
        tree.add(2, 3);
        assertEquals("key: 1 values: [1]\nkey: 2 values: [3]\n", tree.toString());
        tree.add(3, 2);
        assertEquals("key: 1 values: [1]\n" +
                "key: 2 values: [3]\n" +
                "key: 3 values: [2]\n", tree.toString());
        tree.add(4, 2);
        assertEquals("key: 1 values: [1]\n" +
                "key: 2 values: [3]\n" +
                "key: 3 values: [2]\n" +
                "key: 4 values: [2]\n", tree.toString());
        tree.add(5, 2);
        assertEquals("key: 1 values: [1]\n" +
                "key: 2 values: [3]\n" +
                "key: 3 values: [2]\n" +
                "key: 4 values: [2]\n" +
                "key: 5 values: [2]\n", tree.toString());
        tree.removeByKey(3);
        assertEquals("key: 1 values: [1]\n" +
                "key: 2 values: [3]\n" +
                "key: 3 values: [2]\n" +
                "key: 4 values: [2]\n" +
                "key: 5 values: [2]\n", tree.toString());
        tree.removeByValue(2);
        assertEquals("level order: [ key: 2 values: [3], key: 1 values: [1] ]\n" +
                "in order: [ key: 1 values: [1], key: 2 values: [3] ]", tree.tree.toString());
    }

    /**
     * This method test if this RBTree can search in a tree contains many students
     *
     */
    @Test
    public void test4(){
        tree.add(1, 1);
        tree.add(5, 2);
        tree.add(4, 2);
        tree.add(2, 3);
        Iterator<IStudentRBTreeNode<Integer, Integer>> itr = tree.search(3);
        // fuzzy search
        IStudentRBTreeNode<Integer, Integer> node = new StudentRBTreeNode<>();
        node.setKey(4);
        node.addValue(2);
        assertEquals(node.toString(), itr.next().toString());
    }

    /**
     * This method test if this RBTree can add, remove, search in a complex tree
     *
     */
    @Test
    public void test5(){
        tree.add(1, 1);
        tree.add(5, 2);
        tree.add(4, 2);
        tree.add(2, 3);
        tree.add(2, 4);
        tree.add(1, 2);
        tree.add(5, 3);
        tree.add(3, 2);
        tree.add(1, 6);
        Iterator<IStudentRBTreeNode<Integer, Integer>> itr = tree.search(1);
        IStudentRBTreeNode<Integer, Integer> node = new StudentRBTreeNode<>();
        node.setKey(1);
        node.addValue(1);
        node.addValue(2);
        node.addValue(6);
        assertEquals(node.toString(), itr.next().toString());

        tree.removeByValue(3);
        assertEquals("key: 1 values: [1, 2, 6]\n" +
                "key: 2 values: [4]\n" +
                "key: 3 values: [2]\n" +
                "key: 4 values: [2]\n" +
                "key: 5 values: [2]\n", tree.toString());
        tree.removeByValue(1);
        assertEquals("key: 1 values: [2, 6]\n" +
                "key: 2 values: [4]\n" +
                "key: 3 values: [2]\n" +
                "key: 4 values: [2]\n" +
                "key: 5 values: [2]\n", tree.toString());

        itr = tree.search(1);
        node = new StudentRBTreeNode<>();
        node.setKey(1);
        node.addValue(2);
        node.addValue(6);
        assertEquals(node.toString(), itr.next().toString());
    }

    /**
     * This method test if this RBTree can add and remove after integration
     */
    @Test
    public void test6(){
        Student a = new Student("A", 95, "1");
        StudentWrapper<Double> aWrap = new StudentWrapper<Double>(a.score, a);
        scoreTree.add(a.score, aWrap);
        assertEquals("key: 95.0 values: [StudentWrapper{key=95.0, student=Student{name='A'," +
                " score=95.0, bio='1'}}]\n", scoreTree.toString());
        scoreTree.removeByKey(a.score);
        assertEquals("", tree.toString());
    }

    /**
     * This method test if this RBTree can search correct things after integration
     */
    @Test
    public void test7(){
        Student a = new Student("A", 95, "1");
        StudentWrapper<Double> aWrap = new StudentWrapper<Double>(a.score, a);
        Student b = new Student("B", 95, "2");
        StudentWrapper<Double> bWrap = new StudentWrapper<Double>(b.score, b);
        Student c = new Student("C", 94, "3");
        StudentWrapper<Double> cWrap = new StudentWrapper<Double>(c.score, c);
        scoreTree.add(a.score, aWrap);
        scoreTree.add(b.score, bWrap);
        scoreTree.add(c.score, cWrap);
        Iterator<IStudentRBTreeNode<Double, StudentWrapper<Double>>> itr = scoreTree.search(95.0);
        assertEquals("key: 95.0 values: [StudentWrapper{key=95.0, student=Student{name='A', " +
                "score=95.0, bio='1'}}, StudentWrapper{key=95.0, student=Student{name='B', " +
                "score=95.0, bio='2'}}]", itr.next().toString());
        itr = scoreTree.search(94.0);
        assertEquals("key: 94.0 values: [StudentWrapper{key=94.0, student=Student{name='C', " +
                "score=94.0, bio='3'}}]", itr.next().toString());
        // fuzzy search
        itr = scoreTree.search(92.0);
        assertEquals("key: 94.0 values: [StudentWrapper{key=94.0, student=Student{name='C', " +
                "score=94.0, bio='3'}}]", itr.next().toString());
    }

    /**
     * This method test if Backend works correctly on add and remove
     */
    @Test
    public void test8(){
        Student a = new Student("A", 95, "1");
        Student b = new Student("B", 95, "2");
        Student c = new Student("C", 94, "3");
        backend.addStudent(a);
        backend.addStudent(b);
        backend.addStudent(c);
        assertEquals(3, backend.getNumberOfStudents());
        backend.removeByScore(95, 96);
        assertEquals(1, backend.getNumberOfStudents());
    }

    /**
     * This method test if Backend works correctly on search
     */
    @Test
    public void test9(){
        Student a = new Student("A", 95, "1");
        Student b = new Student("B", 95, "2");
        Student c = new Student("C", 94, "3");
        Student d = new Student("D", 93, "EveryDayOneCat");
        backend.addStudent(b);
        backend.addStudent(a);
        backend.addStudent(c);
        backend.addStudent(d);
        List<IStudent> actual = backend.searchByName("A");
        assertEquals("[Student{name='A', score=95.0, bio='1'}]", actual.toString());
        actual = backend.searchByScoreInterval(94, 95);
        assertEquals("[Student{name='C', score=94.0, bio='3'}, Student{name='B'" +
                ", score=95.0, bio='2'}, Student{name='A', score=95.0, bio='1'}]", actual.toString());
    }
}
