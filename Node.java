
public class Node {
	private int g;
	private int h;
	private MyPair coordinates;
	private Node father;	// Nodo usado pra alcançá-lo
	
	/**
	  * Construtor de Node.
	  * 
	  * @param g		Distancia da do nodo para a posicao inicial.
	  * @param h		Distancia heuristica do nodo para a posicão final.
	  * @param i		Linha do Nodo no mapa.
	  * @param j		Coluna do Nodo no mapa.
	  * @param father	Nodo usado para chegar ate este.
	  */
	public Node(int g, int h, MyPair coordenadas, Node father){
		this.g = g;
		this.h = h;
		this.coordinates = coordenadas;
		this.father = father;
	}
	
	public MyPair getCoordinates(){
		return coordinates;
	}

	public int getG(){
		return g;
	}
	
	public int getH(){
		return h;
	}
	
	public int getF(){
		return g + h;
	}
	
	public Node getFather(){
		return father;
	}

	public void setG(int g){
		this.g = g;
	}
	
	public void setH(int h){
		this.h = h;
	}
	
	public void setFather(Node n){
		father = n;
	}
	
	public boolean isPathTo(Node n){
		
		Node aux = n;
		
		while(aux.getCoordinates() != aux.getFather().getCoordinates() && aux.getCoordinates() != this.getCoordinates()){
			aux = aux.getFather();
		}
		
		
		if(this.getCoordinates() == aux.getCoordinates()){
			return true;
		}
		
		return false;
		
	}
	
	public boolean isAdjacent(Node n){
		if(this.getCoordinates().getDistanceTo(n.getCoordinates()) == 1){
			return true;
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
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
		Node other = (Node) obj;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.equals(other.coordinates))
			return false;
		return true;
	}

	
	
}
