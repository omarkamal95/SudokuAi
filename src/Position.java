
public class Position implements Comparable<Position>{
	private int row;
	private int col;
	private int domainSize;
	
	public Position(int row, int column) {
		this.row = row;
		this.col = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	public int compareTo(Position o) {
		// TODO Auto-generated method stub
		return new Integer(domainSize).compareTo(o.domainSize);
		
	}
	

}
