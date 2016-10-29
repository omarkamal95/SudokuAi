import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class SudokuMain {

	static ArrayList<Position> initialEmptyPositions;


	public static void depthFirstSearch(Path file) {
		Node root = populateRoot(file);
		initialEmptyPositions = root.getFreePositions();

		if(DFSHelper(root)){
			System.out.println("Done with DFS");
		}
		else{
			System.out.println("WRROONNG DFS");
		}
	}

	public static boolean DFSHelper(Node node) {
		// If full check if goal state
		if (checkIfFull(node)) {
			if (goalState(node)) {
				printFinalResult(node);
				testPrint(node);
				System.out.println("Success, DFS done successfully");
				return true;
			} else {
				return false;
			}

		}
		else{
//			testPrint(node);
//			System.out.println("================================");
			node.randomPopulateChildren();
			ArrayList<Node> children = node.getChildren();

			for (Node child : children) {
				if (DFSHelper(child)) {
					return true;
				}
			}
			return false;
		}
	}


	public static void BreadthFirstSearch(Path file) {
		Node root = populateRoot(file);
		initialEmptyPositions = root.getFreePositions();
		ArrayList<Node> level = new ArrayList<Node>();
		level.add(root);
		BFSHelper(level);

	}

	public static boolean BFSHelper(ArrayList<Node> level) {

		Node node = level.get(0);
		// If full check if goal state
		if (checkIfFull(node)) {
			if (goalState(node)) {
				printFinalResult(node);
				System.out.print("Success, BFS done successfully");
				return true;
			} else {
				if (level.size() > 1) {
					level.remove(node);
					BFSHelper(level);
				} else {
					return false;
				}
			}
		} else {
			node.randomPopulateChildren();
			ArrayList<Node> children = node.getChildren();
			for (Node child : children) {
				level.add(child);
			}
			level.remove(node);
			BFSHelper(level);
		}
		return false;
	}

	
	public static boolean checkIfFull(Node node){
		ArrayList<Position> freePositions= node.getFreePositions();
//		System.out.println(freePositions.toString());
		if(freePositions.isEmpty()){
			return true;
		}
		return false;
	}

	public static boolean goalState(Node node) {
		Cell[][] sudoku = node.getSudoku();
		for (Position p : initialEmptyPositions) {
			if (!satisfiedConstraintsOnPosition(p.getRow(), p.getCol(), sudoku)) {
				return false;
			}
		}
		return true;
	}


	public static boolean satisfiedConstraintsOnPosition(int row, int col, Cell[][] sudoku) {
		ArrayList<Integer> checker = produceCheckerArray();

		// check for uniqueness in row
		for (int i = 0; i < 9; i++) {
			int test = sudoku[row][i].getValue();

			if(test !=8 && test != 9){
				if(checker.contains(test)){
					checker.remove(new Integer(test));
				} 
				else{
					return false;
				}
			}
		}

		// check for uniqueness in column
		checker = produceCheckerArray();
		for (int i = 0; i < 9; i++) {
			int test = sudoku[i][col].getValue();

			if(test !=8 && test != 9){
				if(checker.contains(test)){
					checker.remove(new Integer(test));
				} 
				else{
					return false;
				}
			}
		}

		// check which subgrid it's in
		int startRow = -1;
		int endRow = -1;
		int startCol = -1;
		int endCol = -1;

		if (row >= 0 && row <= 2) {
			startRow = 0;
			endRow = 2;
		} else if (row >= 3 && row <= 5) {
			startRow = 3;
			endRow = 5;
		} else if (row >= 6 && row <= 8) {
			startRow = 6;
			endRow = 8;
		}

		if (col >= 0 && col <= 2) {
			startCol = 0;
			endCol = 2;
		} else if (col >= 3 && col <= 5) {
			startCol = 3;
			endCol = 5;
		} else if (col >= 6 && col <= 8) {
			startCol = 6;
			endCol = 8;
		}

		// Check uniqueness of subgrid

		checker = produceCheckerArray();
		for (int i = startRow; i <= endRow; i++) {
			for (int j = startCol; j <= endCol; j++) {
				int test = sudoku[i][j].getValue();

				if(test !=8 && test != 9){
					if(checker.contains(test)){
						checker.remove(new Integer(test));
					} 
					else{
						return false;
					}
				}
			}
		}

		return true;
	}

	public static ArrayList<Integer> produceCheckerArray() {
		ArrayList<Integer> checker = new ArrayList<Integer>();
		for (int i = 1; i <= 7; i++) {
			checker.add(i);
		}
		return checker;
	}

	public static void printFinalResult(Node node) {

	}

	public static void breadthFirstSearch() {

	}

	public static void mostConstrained(Path file) {
		Node root = populateRoot(file);
		initialEmptyPositions = root.getFreePositions();
		root.setFreePositions(preprocessingSort(initialEmptyPositions, root.getSudoku()));
		DFSHelper(root);
	}

	public static ArrayList<Position> preprocessingSort(ArrayList<Position> freePositions, Cell[][] sudoku) {
		for (Position freeP : freePositions) {
			// check constraints to determine order
			refineDomain(freeP, sudoku);
		}
		//Collections.sort(freePositions);
		/*for (Position freeP : freePositions) {
			//compare domains to determine order
			Cell targetCell = sudoku[freeP.getRow()][freeP.getCol()];
			targetCell.getDomain().size();
			for (Position freeP : freePositions){
				
			}*/
		return doSelectionSort(freePositions, sudoku);
		
		
	}
	
	public static ArrayList<Position> doSelectionSort(ArrayList<Position> arr, Cell[][] sudoku){
        
        for (int i = 0; i < arr.size() - 1; i++)
        {
            int index = i;
            for (int j = i + 1; j < arr.size(); j++){
            	Position positionInIndex = arr.get(index);
            	Position positionInJ = arr.get(j);
            	Position positionInI = arr.get(i);
            	Cell cellIndex = sudoku[positionInIndex.getRow()][positionInIndex.getCol()];
            	Cell cellJ = sudoku[positionInJ.getRow()][positionInJ.getCol()];
            	Cell cellI = sudoku[positionInI.getRow()][positionInI.getCol()];
            	int domainOfIndex = cellIndex.getDomain().size();
            	int domainOfJ = cellJ.getDomain().size();
            	int domainOfI = cellI.getDomain().size();
                if (domainOfJ < domainOfIndex) 
                    index = j;
            }
            Position smallerDomain = arr.get(index);
            arr.set(index, arr.get(i));
            arr.set(i, smallerDomain);
        }
        return arr;
    }
	
	
	
	public static void refineDomain(Position p, Cell[][] sudoku){
		Cell targetCell = sudoku[p.getRow()][p.getCol()];
		for (Integer trialValue : targetCell.getDomain()) {
			targetCell.setValue(trialValue);
			if(!(satisfiedConstraintsOnPosition(p.getRow(), p.getCol(), sudoku))){
				targetCell.removeFromDomain(trialValue);
			}
		}
	}

	public static void forwardChecking() {

	}

	public static void arcConsistency() {
		Node root = populateRoot(file);
		initialEmptyPositions = root.getFreePositions();
		//to start with the most constrained node
		preprocessingSort(initialEmptyPositions, root.getSudoku());
	}

	
	public static void testPrint(Node node){
		Cell [][] sudoku = node.getSudoku();
		for(int i = 0; i<9 ; i++){
			for(int j =0; j<9; j++){
				System.out.print(sudoku[i][j].getValue());
			}
			System.out.print("\n");
		}
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
		File outputFile = new File("src/output.txt");
		depthFirstSearch(Paths.get("src/test1.txt"));
		BreadthFirstSearch(Paths.get("src/test1.txt"));
	}

}
