import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class StartMenu extends JPanel {
	private JButton start_button;
	private JRadioButton three_player, four_player; 
	private JPanel panel;
	public StartMenu(){
      
		setLayout(new FlowLayout());
		
		start_button = new JButton("Start the Game");
		start_button.addActionListener(new startListener());
		add(start_button);
		
		three_player = new JRadioButton("3-player game");
		four_player = new JRadioButton("4-player game");
		three_player.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(three_player);
		group.add(four_player);
		add(three_player);
		add(four_player);
	}
	
	private class startListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(three_player.isSelected())
				GameController.beginGame(3);
			else
				GameController.beginGame(4); 
		}
	}
}
