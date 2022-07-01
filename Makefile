run: SISApp.class
	cp -f data-orig.xml data.xml
	java -cp .:dom4j-2.1.3.jar SISApp
clean:
	rm -f *.class data.xml
runTests: runDataWranglerTests runFrontendDeveloperTests runAlgorithmEngineerTests runBackendDeveloperTests
runFrontendDeveloperTests:
	javac -cp .:junit5.jar FrontendDeveloperTests.java -Xlint
	java -jar junit5.jar --class-path . --scan-classpath -n FrontendDeveloperTests
runBackendDeveloperTests: BackendDeveloperTests.class data-orig.xml
	cp -f data-orig.xml data.xml
	java -jar junit5.jar -cp .:dom4j-2.1.3.jar --class-path . --scan-classpath --disable-banner -n BackendDeveloperTests
runDataWranglerTests:
	@echo "Data Wrangler does not have any test"
runAlgorithmEngineerTests: AlgorithmEngineerTests.class
	java -jar junit5.jar -cp .:dom4j-2.1.3.jar --class-path . --scan-classpath --disable-banner -n AlgorithmEngineerTests
BackendDeveloperTests.class: junit5.jar BackendDeveloperTests.java SISBackend.class SISApp.class Student.class IStudent.class
	javac -cp .:junit5.jar BackendDeveloperTests.java -Xlint
SISBackend.class: SISBackend.java StudentWrapper.class StudentRBTreePlaceholder.class IBackend.class IStudent.class IStudentRBTree.class IStudentRBTreeNode.class
	javac SISBackend.java
IStudent.class: IStudent.java
	javac IStudent.java
Student.class: Student.java
	javac Student.java
StudentRBTreePlaceholder.class: StudentRBTreePlaceholder.java StudentRBTreePlaceholderNode.class StudentRBTreePlaceholderIterator.class IStudentRBTree.class IStudentRBTreeNode.class
	javac StudentRBTreePlaceholder.java
StudentRBTreePlaceholderNode.class: StudentRBTreePlaceholderNode.java IStudentRBTreeNode.class
	javac StudentRBTreePlaceholderNode.java
StudentRBTreePlaceholderIterator.class: StudentRBTreePlaceholderIterator.java IStudentRBTreeNode.class
	javac StudentRBTreePlaceholderIterator.java
IStudentRBTree.class: IStudentRBTree.java IStudentRBTreeNode.class
	javac IStudentRBTree.java
IStudentRBTreeNode.class: IStudentRBTreeNode.java SortedCollectionInterface.class
	javac IStudentRBTreeNode.java
IBackend.class: IBackend.java IStudent.class
	javac IBackend.java
StudentWrapper.class: StudentWrapper.java IStudent.class
	javac StudentWrapper.java
SortedCollectionInterface.class: SortedCollectionInterface.java
	javac SortedCollectionInterface.java
SISApp.class: SISApp.java IDataLoader.class IBackend.class IFrontend.class DataLoaderPlaceholder.class SISBackend.class FrontendPlaceholder.class
	javac SISApp.java
DataLoaderPlaceholder.class: dom4j-2.1.3.jar DataLoaderPlaceholder.java IDataLoader.class Student.class
	javac -cp .:dom4j-2.1.3.jar DataLoaderPlaceholder.java -Xlint
FrontendPlaceholder.class: FrontendPlaceholder.java IFrontend.class IDataLoader.class IBackend.class Student.class
	javac FrontendPlaceholder.java
IDataLoader.class: IDataLoader.java
	javac IDataLoader.java
IFrontend.class: IFrontend.java
	javac IFrontend.java
AlgorithmEngineerTests.class: junit5.jar AlgorithmEngineerTests.java
	javac -cp .:junit5.jar AlgorithmEngineerTests.java -Xlint
StudentRBTree.class: RBTree.class StudentRBTreeIterator.class
	javac StudentRBTree.java
RBTree.class: RBTree.java
	javac RBTree.java
StudentRBTreeIterator.class: StudentRBTreeIterator.java
	javac StudentRBTreeIterator.java
StudentRBTreeNode.class: StudentRBTreeNode.java
	javac StudentRBTreeNode.java
