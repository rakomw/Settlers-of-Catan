import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GameController {
	public static JFrame frame;
	public static StartMenu start_menu;
	public static GameGUI game_gui;
   public static GameMenuBar menu_bar;
	
	public static void main(String[] args) {
      frame = new JFrame("Settlers of Catan");
	   frame.setLocation(150,0);
	   frame.setSize(600,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createStartMenu();
	}

	private static void createStartMenu(){
		start_menu = new StartMenu();
		frame.getContentPane().add(start_menu);
		frame.setVisible(true);
	}
   
   public static void beginGame(int p){
      start_menu.setVisible(false);
      frame.getContentPane().removeAll();
      
      menu_bar = new GameMenuBar(p);
      frame.setJMenuBar(menu_bar); 
      
      game_gui = new GameGUI();
      frame.getContentPane().add(game_gui);
      
      game_gui.setMenuBar(menu_bar);
      menu_bar.setGUI(game_gui);
   }
   
   public static void endGame(HumanPlayer p){
      game_gui.setVisible(false);
      frame.getContentPane().removeAll();
      frame.setJMenuBar(null);
      
      JPanel end = new JPanel();
      end.setLayout(new FlowLayout());
 
      JLabel label = new JLabel("Congratulations to " + p + " for winning the game!");
      end.add(label);
            
      JButton again_button = new JButton("Play again");
      again_button.addActionListener(new playAgainListener());
      end.add(again_button);
      
      JButton leave_button = new JButton("Exit");
      leave_button.addActionListener(new EndGameListener());
      end.add(leave_button);
      
      end.setVisible(true);
      
      frame.getContentPane().add(end);
      frame.setVisible(true);
 
   }
   
   private static class playAgainListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         frame.setVisible(false);
         frame.getContentPane().removeAll();
         createStartMenu();
      }
  }
  private static class EndGameListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         System.exit(0);
      }
  }
}