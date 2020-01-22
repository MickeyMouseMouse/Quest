import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class CrazyFace {
	Controller cont;

	final Canvas sky = new Canvas(750, 480);
	final Canvas ground = new Canvas(750, 50);
	
	final Canvas face = new Canvas(50, 50);

	final Button startButton = new Button("Start");
	final Label scoreLabel = new Label("0/10");
	
	final Canvas barrier1 = new Canvas(90, 550);
	final Canvas barrier2 = new Canvas(90, 550);
	
	private int modeBarrier1;
	private int modeBarrier2;
	private boolean orderOfBarriers = true; // true (barrier1 before bird), false (barrier2 before bird)
	
	private int score = 0;
	
	private double upToY; // remember y-coordinate to stop face uplift
	
	final private int heightFace = 50; // face: 50x50
	final private int xCoorL = 150; // x-coordinate Of Left Corners Of face
	final private int xCoorR = 200; // x-coordinate Of Right Corners Of face

	CrazyFace(Controller controller) { cont = controller; }

	public void drawSkyAndGround() {
		GraphicsContext picture = sky.getGraphicsContext2D();
		
		picture.setFill(Color.LIGHTBLUE);
		picture.fillRect(0, 0, 750, 480);

		picture = ground.getGraphicsContext2D();
		
		picture.setFill(Color.GREEN);
		picture.fillRect(0, 0, 750, 50);
	}
	
	// emotion = true (smile face) / false (angry face)
	public void drawFace(boolean emotion) {
		GraphicsContext picture = face.getGraphicsContext2D();

		picture.setFill(Color.LIGHTSEAGREEN);
		picture.fillRect(0, 0, 50, 50);
		
		picture.setFill(Color.YELLOW);
		picture.fillOval(3, 5, 20, 20);
		picture.fillOval(27, 5, 20, 20);
		
		picture.setFill(Color.BLACK);
		picture.fillOval(9, 11, 8, 8);
		picture.fillOval(34, 11, 8, 8);
		
		
		picture.setStroke(Color.BLACK);
		picture.setLineWidth(3.0);
		picture.strokeRect(0, 0, 50, 50);
		
		picture.setFill(Color.DARKRED);
		if (emotion)
			picture.fillPolygon(new double[] {5.0, 20.0, 30.0, 45.0, 25.0}, new double[] {30.0, 35.0, 35.0, 30.0, 45.0}, 5);
		else
			picture.fillOval(13, 24, 22, 22);
	
	}
	
	// draw one of the 4 variants of barrier
	public void drawBarrier(int numberOfBarrier, int mode) {
		GraphicsContext picture;
		
		switch (numberOfBarrier) {
			case 1:
				picture = barrier1.getGraphicsContext2D();
				modeBarrier1 = mode;
				
				break;
				
			case 2:
				picture = barrier2.getGraphicsContext2D();
				modeBarrier2 = mode;
				
				break;
				
			default:
				return;
		}
		
		switch (mode) {
			case 0:
				picture.setFill(Color.BLUEVIOLET);
				picture.fillRect(0, 0, 90, 60);
				picture.fillRect(0, 190, 90, 300);
				picture.setFill(Color.LIGHTBLUE);
				picture.fillRect(0, 60, 90, 130);			
				
				break;
				
			case 1:
				picture.setFill(Color.BLUEVIOLET);
				picture.fillRect(0, 0, 90, 150);
				picture.fillRect(0, 280, 90, 210);
				picture.setFill(Color.LIGHTBLUE);
				picture.fillRect(0, 150, 90, 130);			
				
				break;
				
			case 2:
				picture.setFill(Color.BLUEVIOLET);
				picture.fillRect(0, 0, 90, 200);
				picture.fillRect(0, 330, 90, 160);
				picture.setFill(Color.LIGHTBLUE);
				picture.fillRect(0, 200, 90, 130);			
				
				break;
				
			case 3:
				picture.setFill(Color.BLUEVIOLET);
				picture.fillRect(0, 0, 90, 300);
				picture.fillRect(0, 430, 90, 60);
				picture.setFill(Color.LIGHTBLUE);
				picture.fillRect(0, 300, 90, 130);			
				
				break;
				
			default:
		}				
	}

	// movements of barriers
	private AnimationTimer timerBarriers = new AnimationTimer() {
		@Override
		public void handle(long now) {
			makeBarriersLeft();
		}
	};	
	
	// movements of face down
	private AnimationTimer timerFaceDown = new AnimationTimer() {
		@Override
		public void handle(long now) {
			makeFaceDown();
		}
	};
	
	// movements of face up
	private AnimationTimer timerFaceUp = new AnimationTimer() {
		@Override
		public void handle(long now) {
			uplift();
			
			if (face.getLayoutY() <= upToY) {
				timerFaceDown.start();
				timerFaceUp.stop();
			}
		}
	};
	
	// default settings
	public void start() {
		score = 0;
		scoreLabel.setText("0/10");
		scoreLabel.setStyle("-fx-font-size: 35px");
		
		startButton.setVisible(false);
		
		face.setLayoutY(280);
		drawFace(true);
		
		barrier1.setLayoutX(545);
		barrier2.setLayoutX(895);
		orderOfBarriers = true; 
		drawBarrier(1, (int) (Math.random() * 10) % 4);
		drawBarrier(2, (int) (Math.random() * 10) % 4);
		
		timerBarriers.start();
		timerFaceDown.start();
	}
	
	// for mouse or keys event
	public void upliftFace() {
		upToY = face.getLayoutY() - 45;
		timerFaceDown.stop();
		timerFaceUp.start();		
	}

	// for AnimationTimer
	public void uplift() {		
		face.setLayoutY(face.getLayoutY() - 4.5);	
		if (face.getLayoutY() <= -15 || crash()) gameOver();
	}	
	
	// for AnimationTimer
	public void makeFaceDown() {
		face.setLayoutY(face.getLayoutY() + 3);
		if (face.getLayoutY() >= 440 || crash()) gameOver();
	}
	
	// for AnimationTimer
	public void makeBarriersLeft() {
		// moving the barrier1 to starting position and redrawing
		if (barrier1.getLayoutX() <= -90) {
			barrier1.setLayoutX(barrier2.getLayoutX() + 350);
			drawBarrier(1, (int) (Math.random() * 10) % 4);
		}

		barrier1.setLayoutX(barrier1.getLayoutX() - 2.5);
		
		// moving the barrier2 to starting position and redrawing
		if (barrier2.getLayoutX() <= -90) {
			barrier2.setLayoutX(barrier1.getLayoutX() + 350);
			drawBarrier(2, (int) (Math.random() * 10) % 4);
		}

		barrier2.setLayoutX(barrier2.getLayoutX() - 2.5);
		
		updateScore();
		if (crash()) gameOver();
	}
	
	// crash = collision of face with the barrier
	public boolean crash() {
		int barrierX1;
		int barrierY1;
		int barrierX2;
		int barrierY3;
		
		if (orderOfBarriers) {
			/*				*
			 * 				*
			 * 				*
			X1 Y1--------X2 (Y2)
				-> face ->
			(X4)(Y4)----(X3) Y3
			*				*
			*				*
			*				*/		
			switch (modeBarrier1) {
				case 0:
					barrierX1 = (int) barrier1.getLayoutX();
					barrierY1 = 60;
					barrierX2 = barrierX1 + 90;
					barrierY3 = 190;
					
					break;
				case 1:
					barrierX1 = (int) barrier1.getLayoutX();
					barrierY1 = 150;
					barrierX2 = barrierX1 + 90;
					barrierY3 = 280;
					
					break;
				case 2:
					barrierX1 = (int) barrier1.getLayoutX();
					barrierY1 = 200;
					barrierX2 = barrierX1 + 90;
					barrierY3 = 330;
					
					break;
				case 3:
					barrierX1 = (int) barrier1.getLayoutX();
					barrierY1 = 300;
					barrierX2 = barrierX1 + 90;
					barrierY3 = 430;
					
					break;
				default:
					return true;
			}
		} else {	
			switch (modeBarrier2) {
				case 0:
					barrierX1 = (int) barrier2.getLayoutX();
					barrierY1 = 60;
					barrierX2 = barrierX1 + 90;
					barrierY3 = 190;
					
					break;
				case 1:
					barrierX1 = (int) barrier2.getLayoutX();
					barrierY1 = 150;
					barrierX2 = barrierX1 + 90;
					barrierY3 = 280;
					
					break;
				case 2:
					barrierX1 = (int) barrier2.getLayoutX();
					barrierY1 = 200;
					barrierX2 = barrierX1 + 90;
					barrierY3 = 330;
					
					break;
				case 3:
					barrierX1 = (int) barrier2.getLayoutX();
					barrierY1 = 300;
					barrierX2 = barrierX1 + 90;
					barrierY3 = 430;
					
					break;
				default:
					return true;
			}
		}
		
		// necessary conditions for crash
		return ( ((xCoorR > barrierX1 && xCoorR < barrierX2) ||
			  (xCoorL > barrierX1 && xCoorL < barrierX2)) &&
			 ((face.getLayoutY() < barrierY1) || 
			  (face.getLayoutY() + heightFace > barrierY3)) );
	}
	
	public void updateScore() {
		if (orderOfBarriers) {
			if (xCoorL >= barrier1.getLayoutX() + 90) {
				
				score += 1;

				scoreLabel.setText(((Integer)score).toString() + "/10");
				
				orderOfBarriers = false;
			}
		} else {
			if (xCoorL >= barrier2.getLayoutX() + 90) {
				
				score += 1;
				scoreLabel.setText(((Integer)score).toString() + "/10");
				
				orderOfBarriers = true;
			}
		}

		if (score == 10) {
			cont.crazyFaceCompleted();
			gameOver();
		}
	}
	 
	public void gameOver() {
		timerBarriers.stop();
		timerFaceDown.stop();
		timerFaceUp.stop();
		
		startButton.setVisible(true);
		startButton.setText("Restart");
		drawFace(false);
	}
}