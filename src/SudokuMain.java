import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SudokuMain {
	
	public static void deapthFirstSearch(Path file){
		Node root = populateRoot(file);
	}
	
	public static void breadthFirstSearch(){
		
	}
	
	public static void mostConstrained(){
		
	}
	
	public static void forwardChecking(){
		
	}
	
	public static void arcConsistency(){
		
	}
	
	public static Node populateRoot(Path file){
		List<String> lines;
		try {
			lines = Files.readAllLines(file);
			Node root = new Node(lines);
			return root;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print("Error while Reading File");
			return null;
		}
	}
	
	public static void main(String[] args) {
		deapthFirstSearch(Paths.get("src/test1.txt"));
	}

}
