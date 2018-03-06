import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot {
	private Node currentPosition;
	int direction = 0;	// 0 - Norte / 1 - Leste / 2 - Sul / 3 - Oeste 
	UltrasonicSensor sound = new UltrasonicSensor(SensorPort.S4);
	DifferentialPilot piloto = new DifferentialPilot(1.22047f, 7.345005f, Motor.A, Motor.B);
	
	public Robot(Node inicialPosition){
		piloto.setTravelSpeed(5);
		piloto.setRotateSpeed(20);
		piloto.setAcceleration(15);
		this.currentPosition = inicialPosition;
	}
	
	public void setPosition(Node newPosition){
		this.currentPosition = newPosition;
	}
	
	public Node getCurrentPosition(){
		return currentPosition;
	}
	
	public int getDirection(){
		return direction;
	}
	
	// Robo avança 25 centímetros
	public void walkFoward(){
		piloto.travel(9.48f);
	}
	
	public boolean lookRight(){
		float distance;
		
		Motor.C.rotate(-90);
		distance = sound.getRange();
		Motor.C.rotate(90);	
		if(distance < 25)
			return true;
		return false;
	}
	
	public boolean lookLeft(){
		float distance;
	
		Motor.C.rotate(90);
		distance = sound.getRange();
		Motor.C.rotate(-90);
		if(distance < 20)
			return true;
		return false;
	}
	
	public boolean lookAhead(){
		float distance  = sound.getRange();
		
		if(distance  < 20)
			return true;
		return false;
	}
	
	public boolean lookBack(){
		float distance = 0;
		
		Motor.C.rotate(180);
		distance = sound.getRange();
		Motor.C.rotate(-180);
		
		if(distance < 25)
			return true;
		return false;
	}	
	
	public void setDirection(int newDirection){
		int n = newDirection - this.getDirection();
		
			if(Math.abs(n) == 3){
				n = -n/3;
			}
			
			piloto.rotate(90 * n);
			this.direction = newDirection;
	}
	
	public void moveTo(MyPair n){
		// Testa adjacencia ou igualdade
		if(n.getDistanceTo(this.getCurrentPosition().getCoordinates()) > 1){
			// Nao adjacentes, nem iguais
			System.exit(1);
		}
		
		if(n.getDistanceTo(this.getCurrentPosition().getCoordinates()) == 0){
			// Ja esta no nodo correto
			return;
		}
		
		if(n.geti() == this.getCurrentPosition().getCoordinates().geti()){
			// Mesma Linha
			if(n.getj() > this.getCurrentPosition().getCoordinates().getj()){
				// Deve ir para leste
				this.setDirection(1);
			}
			else{
				// Deve ir para Oeste
				this.setDirection(3);
			}
		}
		else{
			// Mesma Coluna
			if(n.geti() < this.getCurrentPosition().getCoordinates().geti()){
				// Deve ir para Norte
				this.setDirection(0);
			}
			else{
				// Deve ir para Sul
				this.setDirection(2);
			}
		}
		
		this.walkFoward();
		// position é atualizada na funçao travel
	}
}
