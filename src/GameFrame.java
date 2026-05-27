package snakepac;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class GameFrame extends JFrame{

//First personal usage of constructor class, take notes on syntax!
	protected JLabel textField;
	
	public GameFrame(){
		
//Variable panel, take notice, call at appropriate times!! Troubleshoot if need be!
		Font font0 = new Font("SansSerif", Font.BOLD, 16);

		JPanel scoreboard = new JPanel(new BorderLayout());
		scoreboard.add(new JLabel("Score"), BorderLayout.WEST);
		textField = new JLabel("0");
		textField.setFont(font0);
		scoreboard.add(textField, BorderLayout.CENTER);
		this.add(scoreboard, BorderLayout.SOUTH);
		
		this.add(new GamePanel(textField));
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		

	}

}
