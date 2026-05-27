package snakepac;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JDialog;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GamePanel extends JPanel implements ActionListener{
	
	//declared static immutable integer variables; screen size / resolution
	static final int screenWid = 640;
	static final int screenHght = 480;
	//"object" size, amount, game / snake speed (75 "base" for avg game);
	static final int unitSize = 20;
	static final int gameUnits = (screenWid / unitSize) * (screenHght / unitSize);
	static final int delay = 75;
	//stored re-used color object variable for graphical optimization purposes...
	static final Color BODY_COLOR = new Color(45, 180, 0);
	//snake starting length; referenced for score calculation and reset
	static final int INITIAL_SIZE = 5;
	//snake position, size, location
	final int x[] = new int[gameUnits];
	final int y[] = new int[gameUnits];
	//mutable variables; snake initial size and apples eaten
	int snkUnits = INITIAL_SIZE;
	int fruitEaten;
	//spawn location of apple
	int appleX;
	int appleY;
	//snake initial direction ('D' right)
	char direction = 'D';
	private BufferedImage gridImage;
	JLabel txtScore;
	JDialog gameOverDialog;

	//run instance, timer, and random import declaration
	//Timer javax.swing.Timer; Random java.util.Random
	boolean running = false;
	Timer timer;
	Random random;


	//create game window with scoreboard panel method
	GamePanel(JLabel theTextField) {
		random = new Random();
		this.setPreferredSize(new Dimension(screenWid, screenHght));
		this.setBackground(Color.black);
		createGridImage();
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		txtScore = theTextField;
		txtScore.setText("0");
		
	}

	private void createGridImage() {
		gridImage = new BufferedImage(screenWid, screenHght, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = gridImage.createGraphics();
		g2.setColor(new Color(30, 30, 30));
		for (int i = 0; i <= screenWid / unitSize; i++) {
			g2.drawLine(i * unitSize, 0, i * unitSize, screenHght);
			g2.drawLine(0, i * unitSize, screenWid, i * unitSize);
		}
		g2.dispose();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(delay, this);
		timer.start();
	}

	public void restartGame() {
		if (gameOverDialog != null) {
			gameOverDialog.dispose();
			gameOverDialog = null;
		}

		snkUnits = INITIAL_SIZE;
		direction = 'D';
		fruitEaten = 0;

		// clear all snake positions back to origin
		for (int i = 0; i < x.length; i++) {
			x[i] = 0;
			y[i] = 0;
		}

		if (txtScore != null) {
			txtScore.setText("0");
		}

		newApple();
		running = true;
		timer.start();
		this.requestFocusInWindow(); //CRITICAL, return focus to game
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		//for loop to create grid for game "objects" of unit size equal to 1
		/*for(int i=0; i<=screenWid/unitSize; i++) {
			//x & y grid line declarations
			g.drawLine(i*unitSize, 0, i*unitSize, screenHght);
			g.drawLine(0, i*unitSize, screenWid, i*unitSize);
		}*/
		g.drawImage(gridImage, 0, 0, null);
		g.setColor(Color.red);
		g.fillOval(appleX, appleY, unitSize, unitSize);
		
		for(int i = 0; i < snkUnits; i++) {
			if(i == 0) {
				g.setColor(Color.green);
				g.fillRect(x[i], y[i], unitSize, unitSize);
			}
			else {
				g.setColor(BODY_COLOR);
				g.fillRect(x[i], y[i], unitSize, unitSize);
			}
		}
	}
	
	public void move() {
		//for loop to "shift" snkUnits around x, y[] coordinates array
		for(int i = snkUnits; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch (direction) {
		case 'W':
			y[0] = y[0] - unitSize;
			break;
		case 'S':
			y[0] = y[0] + unitSize;
			break;
		case 'A':
			x[0] = x[0] - unitSize;
			break;
		case 'D':
			x[0] = x[0] + unitSize;
			break;
		}
	}

	//typecast random spawn pos of apple as integer for stability
	public void newApple() {
		//a boolean flag to check whether apple spawns on / in snake
		boolean onSnake;
		do {
			appleX = random.nextInt((screenWid / unitSize) - 1) * unitSize;
			appleY = random.nextInt((screenHght / unitSize) - 1) * unitSize;

			onSnake = false;
			for (int i = 0; i < snkUnits; i++) {
				/*run conditional check w/ boolean to see if apple
				happened to spawn within the snake itself*/
				if (appleX == x[i] && appleY == y[i]) {
					onSnake = true;
					break;
				}
			}
		} while (onSnake);
	}
	
	public void checkApple() {
		if(x[0] == appleX && y[0] == appleY) {
			newApple();
			snkUnits++;
			this.txtScore.setText(" " + (snkUnits - INITIAL_SIZE));
			}			
	}
	
	public void checkCollisions() {
		//for loop to check collision of snake head with body
		for(int i = snkUnits; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches right border
		if(x[0] >= screenWid) {
			running = false;
		}
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] >= screenHght) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
			showGameOverDialog();
		}
		
	}

	public void showGameOverDialog() {
		JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		gameOverDialog = new JDialog(parentFrame, "Game Over", false);

		gameOverDialog.setSize(320, 240);
		gameOverDialog.setLocationRelativeTo(parentFrame);
		gameOverDialog.setResizable(false);
		gameOverDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.black);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel gameOverLabel = new JLabel("GAME OVER");
		gameOverLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
		gameOverLabel.setForeground(Color.red);
		gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel scoreLabel = new JLabel("Final Score: " + (snkUnits - INITIAL_SIZE));
		scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		scoreLabel.setForeground(Color.white);
		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel restartLabel = new JLabel("Press SPACE to Restart");
		restartLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		restartLabel.setForeground(Color.gray);
		restartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel.add(Box.createVerticalGlue());
		panel.add(gameOverLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 15)));
		panel.add(scoreLabel);
		panel.add(Box.createRigidArea(new Dimension(0, 20)));
		panel.add(restartLabel);
		JLabel exitLabel = new JLabel("Press ESC or ENTER to Exit");
		exitLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		exitLabel.setForeground(Color.gray);
		exitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(exitLabel);
		panel.add(Box.createVerticalGlue());

		gameOverDialog.setContentPane(panel);

		gameOverDialog.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					restartGame();
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_ENTER) {
					System.exit(0);
				}
			}
		});

		gameOverDialog.setVisible(true);
		gameOverDialog.requestFocusInWindow();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();

	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction != 'D') {
						direction = 'A';
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (direction != 'A') {
						direction = 'D';
					}
					break;
				case KeyEvent.VK_UP:
					if (direction != 'S') {
						direction = 'W';
					}
					break;
				case KeyEvent.VK_DOWN:
					if (direction != 'W') {
						direction = 'S';
					}
					break;
				case KeyEvent.VK_SPACE:
					if (!running) {
						restartGame();
					}
					break;
				case KeyEvent.VK_ESCAPE:
				case KeyEvent.VK_ENTER:
					if (!running) {
						System.exit(0);
					}
					break;
			}
		}
	}
}
