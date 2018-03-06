import lejos.nxt.*;

public class Main {

	public static void main(String[] args) {
		MyPair inicialPosition = new MyPair(4,3);	// Posicao inicial do robo
		MyPair finalPosition = new MyPair(0,0);	// Posicao final do robo
		MyPair mapSize = new MyPair(5, 5);
		
		LCD.drawString("Press any Button", 0, 0);
		Button.waitForAnyPress();
		
		LCD.clear();
		
		AStar aStar = new AStar(mapSize, inicialPosition, finalPosition);
		aStar.runAStar();
		
		
	}

}
