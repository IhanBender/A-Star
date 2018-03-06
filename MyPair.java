
public class MyPair {
	private int i;
	private int j;
	
	public MyPair(int i, int j){
		this.i = i;
		this.j = j;
	}
	
	public int geti(){
		return i;
	}
	
	public int getj(){
		return j;
	}
	
	public void seti(int i){
		this.i = i;
	}
	
	public void setj(int j){
		this.j = j;
	}
	
	// "Manhattam distance"
	public int getDistanceTo(MyPair n){
		return Math.abs(this.geti() - n.geti()) + Math.abs(this.getj() - n.getj()); 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		result = prime * result + j;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyPair other = (MyPair) obj;
		if (i != other.i)
			return false;
		if (j != other.j)
			return false;
		return true;
	}

	
	
}
