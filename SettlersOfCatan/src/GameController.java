import javax.swing.*;

import java.awt.Color;
import java.awt.event.*;
import java.util.*;

public class GameController {
	public static JFrame frame;
	private static StartMenu start_menu;
	private static GameGUI game_gui;
	
	public static void main(String[] args) {
		createStartMenu();
	}

	private static void createStartMenu(){
		frame = new JFrame("Settlers of Catan");
	   frame.setLocation(150,0);
	   frame.setSize(1000,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		start_menu = new StartMenu();
		frame.getContentPane().add(start_menu);
		frame.setVisible(true);
	}
   
   public static void beginGame(int p){
      start_menu.setVisible(false);
      frame.getContentPane().removeAll();
      game_gui.setUpMainGUI(p);
   }

}