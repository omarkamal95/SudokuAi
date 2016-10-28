import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {
	private Cell [][] sudoku;
	private Node parent;
	private ArrayList<Node> children;
	private ArrayList<Position> freePositions;
	
	public Node(List<String> stringSudoku) {
		this.sudoku = new Cell [9][9];
		this.freePositions = new ArrayList<Position>();
		// Check if rows = 9
		if(stringSudoku.size() != 9){
			System.out.print("Error: Rows aren't = 9");
		}

		// Get each cell in each row
		int row = 0;
		for(String s: stringSudoku){
			String [] columns = s.split(" ");

			//Check if columns = 9
			if(columns.length != 9){
				System.out.print("Error: Rows aren't = 9");
			}
			
			//Loop over each column
			
			for(int col = 0; col < columns.length; col++){
				char value = columns[col].charAt(0);
				//Add Free Position
				if(value == '*'){
					Position p = new Position(row, col);
					freePositions.add(p);
				}
				Cell currentCell = new Cell(value);
				this.sudoku[row][col] = currentCell;
			}
			
			//Go to next row
			row++;

		}
		
		this.parent = null;
		this.children = new ArrayList<Node>();
		for(int i = 0; i<9 ; i++){
			for(int j = 0; j<9; j++){
				System.out.print(this.sudoku[i][j].getValue());
			}
			System.out.print("\n");
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
	
	

}
