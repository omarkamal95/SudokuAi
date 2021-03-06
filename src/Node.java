import java.util.*;

public class Node {
	private Cell[][] sudoku;
	private Node parent;
	private ArrayList<Node> children;
	private ArrayList<Position> freePositions;
	private Position changedPosition;
	private int changedPositionValue;

	public Node(List<String> stringSudoku) {
		this.sudoku = new Cell[9][9];
		this.freePositions = new ArrayList<Position>();
		// Check if rows = 9
		if (stringSudoku.size() != 9) {
			System.out.print("Error: Rows aren't = 9");
		}

		// Get each cell in each row
		int row = 0;
		for (String s : stringSudoku) {
			String[] columns = s.split(" ");

			// Check if columns = 9
			if (columns.length != 9) {
				System.out.print("Error: Rows aren't = 9");
			}

			// Loop over each column

			for (int col = 0; col < columns.length; col++) {
				char value = columns[col].charAt(0);
				// Add Free Position
				if (value == '*') {
					Position p = new Position(row, col);
					freePositions.add(p);
				}
				Cell currentCell = new Cell(value);
				this.sudoku[row][col] = currentCell;
			}

			// Go to next row
			row++;

		}

		this.parent = null;
		this.children = new ArrayList<Node>();
		this.changedPosition = null;
		this.changedPositionValue = -1;

		// for(int i = 0; i<9 ; i++){
		// for(int j = 0; j<9; j++){
		// System.out.print(this.sudoku[i][j].getValue());
		// }
		// System.out.print("\n");
		// }
	}

	public Node(Node parent, Position changedPos, int posValue) {
		this.parent = parent;
		this.children = new ArrayList<Node>();

		ArrayList<Position> newFreePos = (ArrayList<Position>) parent.getFreePositions().clone();
		newFreePos.remove(changedPos);
		this.freePositions = newFreePos;
		
		this.changedPosition = changedPos;
		this.changedPositionValue = posValue;


		this.sudoku = new Cell[9][9];
		// clone in new sudoku except for changed positions
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (i == changedPos.getRow() && j == changedPos.getCol()) {
					this.sudoku[i][j] = new Cell(posValue);
					// System.out.println("i "+i+" j "+j +" s"+
					// this.sudoku[i][j].getValue());
				} else {
					this.sudoku[i][j] = parent.getSudoku()[i][j];
				}
			}
		}

	}

	public Cell[][] getSudoku() {
		return sudoku;
	}

	public void setSudoku(Cell[][] sudoku) {
		this.sudoku = sudoku;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void addChild(Node child) {
		this.children.add(child);
	}

	public ArrayList<Position> getFreePositions() {
		return freePositions;
	}

	public void setFreePositions(ArrayList<Position> freePositions) {
		this.freePositions = freePositions;
	}

	public void randomPopulateChildren() {
		Position freePos = this.freePositions.get(0);
		int row = freePos.getRow();
		int col = freePos.getCol();
		Cell selectedCell = sudoku[row][col];
		for (int trialValue : selectedCell.getDomain()) {
			Node child = new Node(this, freePos, trialValue);
			// SudokuMain.testPrint(child);
			// System.out.println("================================");
			this.children.add(child);
		}
	}
	
	public void forwardCheckPopulateChildren(){
		Position freePos = this.freePositions.get(0);
		int row = freePos.getRow();
		int col = freePos.getCol();
		Cell selectedCell = sudoku[row][col];
		for(int trialValue :selectedCell.getDomain()){
			boolean isGoodValue= true;
			for(int i= 1; i<freePositions.size(); i++ ){
				Position testPos = freePositions.get(i);
				int testPosRow = testPos.getRow();
				int testPosCol = testPos.getCol();
				if(row == testPosRow || col == testPosCol 
						|| SudokuMain.checkGrids(row, col, testPosRow, testPosCol)){
					Cell affectedNeighbourCell = this.sudoku[testPosRow][testPosCol];
					ArrayList<Integer> affectedDomain = (ArrayList<Integer>) affectedNeighbourCell.getDomain().clone();
					affectedDomain.remove(new Integer(trialValue));
					if(affectedDomain.isEmpty()){
						isGoodValue = false;
						break;
					}
				}
			}
			if(isGoodValue){
				Node child = new Node(this, freePos, trialValue);
				this.children.add(child);
			}
		}
	}

	public Position getChangedPosition() {
		return changedPosition;
	}

	public void setChangedPosition(Position changedPosition) {
		this.changedPosition = changedPosition;
	}

	public int getChangedPositionValue() {
		return changedPositionValue;
	}

	public void setChangedPositionValue(int changedPositionValue) {
		this.changedPositionValue = changedPositionValue;
	}
	
	

}
