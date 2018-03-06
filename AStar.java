import java.util.*;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class AStar {
	Robot robot;		// Robo que ira percorrer o mapa
	Node inicialNode;	// Nodo de inicio
	MyPair finalCoords;
	MyPair mapSize;		// Dados de largura e altura do mapa
	ArrayList<Node> open = new ArrayList<Node>();
	ArrayList<Node> closed = new ArrayList<Node>();
	Stack<MyPair> pilha = new Stack<MyPair>();
	LinkedList<MyPair> fila = new LinkedList<MyPair>();
	
	public AStar(MyPair mapSize, MyPair inicialCoords, MyPair finalCoords){
		this.mapSize = mapSize;
		
		this.inicialNode = new Node(0, inicialCoords.getDistanceTo(finalCoords), inicialCoords, inicialNode);
		inicialNode.setFather(inicialNode);
		
		this.finalCoords = finalCoords;
		robot = new Robot(inicialNode);
	}	
	
	public void runAStar(){
		
		open.add(robot.getCurrentPosition());
		
		while(!robot.getCurrentPosition().getCoordinates().equals(finalCoords)){
			lookAround();
			MyPair dest = this.getNextDestination();
			getRoute(dest);
			travel();
		}
		
	}
	
	public MyPair getNextDestination(){
		// Seta as variaveis de comparação com valores arbitrariamente altos
		int lowestF = Integer.MAX_VALUE;	
		int lowestH = Integer.MAX_VALUE;
		Node returnNode = open.get(0);	// Valor diferente de 
		
		for(Node node: open){
			if(node.getF() < lowestF){
				// Caso o nodo analisado tenha valor de f menor que o menor ja encontrado, atualiza os dados de menor valor de f
				lowestF = node.getF();
				lowestH = node.getH();
				returnNode = node;
			}
			else{
				if(node.getF() == lowestF && node.getH() < lowestH){
					// Caso tenha o mesmo valor de f, mas um h menor, realiza o mesmo procedimento
					lowestF = node.getF();
					lowestH = node.getH();
					returnNode = node;
				}
			}
		}
		
		/*LCD.drawString("i: " + returnNode.getCoordinates().geti() + "\nj: " + returnNode.getCoordinates().getj() + "", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		System.exit(0);*/
		
		return returnNode.getCoordinates();
	}
	
	public void getRoute(MyPair n){
		
		
		Node robotNode = robot.getCurrentPosition();
		Node auxNode = null;
		
		this.fila.clear();
		this.pilha.removeAllElements();
		auxNode = getNode(open, n);
		// E possivel chegar de robotNode ate n sabendo apenas o pai de n
		
		while(!robotNode.isPathTo(auxNode)){
			fila.add(robotNode.getCoordinates());
			robotNode = robotNode.getFather();
		}
		fila.add(robotNode.getCoordinates());	// Adiciona na fila o pai em comum

		// Empilha os nodos a partir do destino ate o pai em comum
		while(auxNode != robotNode){
			pilha.push(auxNode.getCoordinates());
			auxNode = auxNode.getFather();
		}	
	}

	public Node getNode(ArrayList<Node> lista, MyPair coords){
		Node returnNode = null;
		
		for(Node node: lista){
			if(node.getCoordinates().equals(coords)){
				return node;
			}
		}
		
		return returnNode;	// Should not happen
	}
	

	public void lookSide(int i, int j){
		int cost = 0;
		Node openNode = null;
		Node closedNode = null;
		MyPair auxPair = null;
		
		auxPair = new MyPair(robot.getCurrentPosition().getCoordinates().geti() + i, robot.getCurrentPosition().getCoordinates().getj() + j);
		openNode = getNode(open, auxPair);		// Novo nodo (caso esteja em open)
		closedNode = getNode(closed, auxPair);		// Novo nodo (caso esteja em closed)
		cost = robot.getCurrentPosition().getG() + 1;	// Custo do novo nodo
	
		// Caso ja exista
		if(openNode != null){
			if(openNode.getG() > cost){
				open.remove(openNode);
			}
		}
		if(closedNode != null){
			if(closedNode.getG() > cost){
				open.remove(openNode);
			}
		}
		
		openNode = getNode(open, auxPair);		// Novo nodo (caso esteja em open)
		closedNode = getNode(closed, auxPair);		// Novo nodo (caso esteja em closed)
		if(openNode == null && closedNode == null ){
			Node newNode = new Node(cost, finalCoords.getDistanceTo(auxPair), auxPair, robot.getCurrentPosition());
			newNode.setFather(robot.getCurrentPosition());
			open.add(newNode);
		}
		
	}
	
	public void lookAround(){
		
		robot.setDirection(0);	// Robo sempre olha para o norte para se localizar
		// Confere norte
		if(robot.getCurrentPosition().getCoordinates().geti() != 0 && robot.lookAhead() == false){
			lookSide(-1, 0);
		}
		// Confere oeste
		if(robot.getCurrentPosition().getCoordinates().getj() != 0 && robot.lookLeft() == false){
			lookSide(0, -1);
		}
		// Confere sul
		if(robot.getCurrentPosition().getCoordinates().geti() != this.mapSize.geti() - 1 && robot.lookBack() == false){
			lookSide(1, 0);
		}
		// Confere leste
		if(robot.getCurrentPosition().getCoordinates().getj() != this.mapSize.getj() - 1 && robot.lookRight() == false){
			lookSide(0, 1);
		}
		
		open.remove(robot.getCurrentPosition());
		closed.add(robot.getCurrentPosition());
	}
	
	public void analiseNode(int cost, Node aux){
		if(open.contains(aux) && cost < aux.getG()){
			open.remove(aux);
		}
		if(closed.contains(aux) && cost < aux.getG()){
			closed.remove(aux);
		}
		if(!open.contains(aux) && !closed.contains(aux)){
			aux.setG(cost);
			open.add(aux);
			aux.setFather(robot.getCurrentPosition());
		}
	}
	
	public void travel(){
		MyPair aux = null;
		
		while(!fila.isEmpty()){
			aux = fila.remove(0);
			robot.moveTo(aux);
			this.atualizaCurrentNode(aux);
		}
		
		while(!pilha.empty()){
			aux = pilha.pop();
			robot.moveTo(aux);
			this.atualizaCurrentNode(aux);
		}
	}
	
	public void atualizaCurrentNode(MyPair pair){
		
		Node openNode = getNode(open, pair);
		Node closedNode = getNode(closed, pair);
		
		if(openNode != null){
			robot.setPosition(openNode);
			return;
		}
		
		if(closedNode != null){
			robot.setPosition(closedNode);
			return;
		}
	
		LCD.drawString("Error: could not find position", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		System.exit(1);
			
	}
	
}
