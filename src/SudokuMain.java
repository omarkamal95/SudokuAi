import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SudokuMain {

	static ArrayList<Position> initialEmptyPositions;

	public static void deapthFirstSearch(Path file) {
		Node root = populateRoot(file);
		initialEmptyPositions = root.getFreePositions();
		DFSHelper(root);

	}

	public static boolean DFSHelper(Node node) {
		// If full check if goal state
		if (checkIfFull(node)) {
			if (goalState(node)) {
				printFinalResult(node);
				System.out.print("Success, DFS done successfully");
				return true;
			} else {
				return false;
			}
		} else {
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

	public static boolean checkIfFull(Node node) {
		ArrayList<Position> freePositions = node.getFreePositions();
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
			if (test != 8 && test != 9) {
				if (checker.contains(test)) {
					checker.remove(test);
				} else {
					return false;
				}
			}
		}

		// check for uniqueness in column
		checker = produceCheckerArray();
		for (int i = 0; i < 9; i++) {
			int test = sudoku[i][col].getValue();
			if (test != 8 && test != 9) {
				if (checker.contains(test)) {
					checker.remove(test);
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
						checker.remove(test);
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

	}

	public static void breadthFirstSearch() {

	}

	public static void mostConstrained() {

	}

	public static void forwardChecking() {

	}

	public static void arcConsistency() {

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
		BreadthFirstSearch(Paths.get("src/test1.txt"));
	}

}
