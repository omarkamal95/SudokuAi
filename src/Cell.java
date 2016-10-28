import java.util.ArrayList;

public class Cell {
	private int value;
	private ArrayList<Integer> domain;
	
	public Cell(char val) {
		if(val == '*'){
			this.value = 0;
			this.domain = new ArrayList<Integer>();
			// Add default domain
			for(int i= 1; i<= 9; i++){
				this.domain.add(i);
			}
		} 
		else{
			this.value = Character.getNumericValue(val);
			this.domain = null;
		}
	}
	
	public Cell(int val){
		this.value = val;
		this.domain = null;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public ArrayList<Integer> getDomain() {
		return domain;
	}

	public void setDomain(ArrayList<Integer> domain) {
		this.domain = domain;
	}

}
