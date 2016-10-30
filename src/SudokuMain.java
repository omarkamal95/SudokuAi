import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class SudokuMain {

	static ArrayList<Position> initialEmptyPositions;

	public static void depthFirstSearch(Path file) {
		Node root = populateRoot(file);
		initialEmptyPositions = root.getFreePositions();

		if (DFSHelper(root)) {
			System.out.println("Done with DFS");
		} else {
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

		} else {
			// testPrint(node);
			// System.out.println("================================");
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
				testPrint(node);
				System.out.println("Success, BFS done successfully");
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

	public static boolean checkIfFull(Node node) {
		ArrayList<Position> freePositions = node.getFreePositions();
		// System.out.println(freePositions.toString());
		if (freePositions.isEmpty()) {
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

			if (test != 8 && test != 9 && test != 0) {
				if (checker.contains(test)) {
					checker.remove(new Integer(test));
				} else {
					return false;
				}
			}
		}

		// check for uniqueness in column
		checker = produceCheckerArray();
		for (int i = 0; i < 9; i++) {
			int test = sudoku[i][col].getValue();

			if (test != 8 && test != 9 && test != 0) {
				if (checker.contains(test)) {
					checker.remove(new Integer(test));
				} else {
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

				if (test != 8 && test != 9) {
					if (checker.contains(test)) {
						checker.remove(new Integer(test));
					} else {
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
		PrintWriter writer;
		try {
			writer = new PrintWriter("src/1.sol", "UTF-8");
			//Print Final Sudoku
			Cell[][] sudoku = node.getSudoku();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					writer.print(""+sudoku[i][j].getValue()+ " ");
				}
				writer.print("\n");
			}
			
			writer.println("=================");
			writer.println("      Steps      ");

			//Print steps to final result
			Node temp = node;
			ArrayList<String> stepsStrings = new ArrayList<String>();
			while(temp.getParent() != null){
				int val = temp.getChangedPositionValue();
				Position pos = temp.getChangedPosition();
				int x = pos.getRow() +1;
				int y = pos.getCol() +1;
				String step = "" + x + " " + y + " " + val;
				stepsStrings.add(step);
				temp = temp.getParent();
			}

			while(!stepsStrings.isEmpty()){
				int lastIndex = stepsStrings.size() - 1;
				writer.println(stepsStrings.get(lastIndex));
				stepsStrings.remove(lastIndex);
			}

		    writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("Error while printing final result");
			e.printStackTrace();
		}
	    
	}

	public static void breadthFirstSearch() {

	}

	public static void mostConstrained(Path file) {
		Node root = populateRoot(file);
		initialEmptyPositions = root.getFreePositions();
		ArrayList<Position> sorted = preprocessingSort(initialEmptyPositions, root.getSudoku());
		root.setFreePositions(sorted);
		DFSHelper(root);
	}

	public static ArrayList<Position> preprocessingSort(ArrayList<Position> freePositions, Cell[][] sudoku) {
		for (Position freeP : freePositions) {
			// check constraints to determine order
			refineDomain(freeP, sudoku);
//			System.out.println("New Domain size is " + sudoku[freeP.getRow()][freeP.getCol()].getDomain().size());
		}
		return doSelectionSort(freePositions, sudoku);

	}

	public static ArrayList<Position> doSelectionSort(ArrayList<Position> arr, Cell[][] sudoku) {

		for (int i = 0; i < arr.size() - 1; i++) {
			int index = i;
			for (int j = i + 1; j < arr.size(); j++) {
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

	public static void refineDomain(Position p, Cell[][] sudoku) {
		Cell targetCell = sudoku[p.getRow()][p.getCol()];
		Iterator<Integer> iter = targetCell.getDomain().iterator();
		while (iter.hasNext()) {
			Integer trialValue = iter.next();
			sudoku[p.getRow()][p.getCol()].setValue(trialValue);
			boolean flag = (satisfiedConstraintsOnPosition(p.getRow(), p.getCol(), sudoku));
			if (!flag) {
				iter.remove();
			}
		}

	}

	public static void forwardChecking() {

	}

	public static void arcConsistency(Path file) {
		Node root = populateRoot(file);
		initialEmptyPositions = root.getFreePositions();
		// to start with the most constrained node
		ArrayList<Position> sortedList = preprocessingSort(initialEmptyPositions, root.getSudoku());
		for (int i = 0; i < sortedList.size(); i++) {
			tryValue(sortedList, sortedList.get(i), root);
		}
		testPrint(root);
	}

	public static void tryValue(ArrayList<Position> sortedList, Position initial, Node root) {
		Cell[][] sudoku = root.getSudoku();
		Cell targetCell = sudoku[initial.getRow()][initial.getCol()];
		for (int trialValue : targetCell.getDomain()) {
			if (propagateToNeighbors(sudoku, root, initial, trialValue)) {
				targetCell.setValue(trialValue);
				break;
			}
		}
	}

	public static boolean propagateToNeighbors(Cell[][] sudoku, Node root, Position p, int value) {
		Cell targetCell = sudoku[p.getRow()][p.getCol()];
		int y = p.getRow();
		int x = p.getCol();

		ArrayList<Position> l = root.getFreePositions();
		l.remove(p);
		Iterator<Position> iter = l.iterator();
		while (iter.hasNext()) {
			Position neighbor = iter.next();
			if (neighbor.getRow() == x || neighbor.getCol() == y
					|| checkGrids(x, y, neighbor.getRow(), neighbor.getCol())) {
				if (sudoku[neighbor.getRow()][neighbor.getCol()].getDomain().isEmpty()) {
					return false;
				} else {
					if (sudoku[neighbor.getRow()][neighbor.getCol()].getDomain().contains(value)) {
						iter.remove();
						propagateToNeighbors(sudoku, root, neighbor, value);
						sudoku[neighbor.getRow()][neighbor.getCol()].removeFromDomain(value);
					}
				}
			}

		}
		return true;
	}

	public static boolean checkGrids(int row, int col, int row2, int col2) {
		int startRow = -1;
		int endRow = -1;
		int startCol = -1;
		int endCol = -1;
		boolean srow = false;
		boolean scol = false;

		if (row >= 0 && row <= 2 && row2 >= 0 && row2 <= 2) {
			srow = true;
		} else if (row >= 3 && row <= 5 && row2 >= 3 && row2 <= 5) {
			srow = true;
		} else if (row >= 6 && row <= 8 && row2 >= 6 && row2 <= 8) {
			srow = true;
		}

		if (col >= 0 && col <= 2 && col2 >= 0 && col2 <= 2) {
			scol = srow;
		} else if (col >= 3 && col <= 5 && col2 >= 3 && col2 <= 5) {
			scol = srow;
		} else if (col >= 6 && col <= 8 && col2 >= 6 && col2 <= 8) {
			scol = srow;
		}
		return scol;
	}

	public static void testPrint(Node node) {
		Cell[][] sudoku = node.getSudoku();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(sudoku[i][j].getValue());
			}
			System.out.print("\n");
		}
	}

	public static Node populateRoot(Path file) {
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
//		File outputFile = new File("src/1.sud");
//		depthFirstSearch(Paths.get("src/1.sud"));
//		BreadthFirstSearch(Paths.get("src/1.sud"));
//		mostConstrained(Paths.get("src/1.sud"));
		arcConsistency(Paths.get("src/1.sud"));
	}

}
