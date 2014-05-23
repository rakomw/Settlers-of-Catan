import javax.swing.*;

public class GameController {
	private static JFrame frame;
	private static StartMenu start_menu;
	//private MainGameGUI game_gui;
	private static int num_players;
	private static int victory_points;
	
	public static void main(String[] args) {
		/*String message = "This game is still in progress.\nDon't worry though, we have highly trained specialists hard at work";
		JOptionPane.showMessageDialog(null, message);*/
		createStartMenu();
	}

	HumanPlayer[] players;

	private static void createStartMenu(){
		frame = new JFrame("Settlers of Catan");
	    frame.setLocation(300,150);
	    frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		start_menu = new StartMenu();
		frame.getContentPane().add(start_menu);
		frame.setVisible(true);
	}
	public static void beginGame(int x){
		start_menu.setVisible(false);
		num_players = x;
		if(num_players==3)
			victory_points=10;
		else
			victory_points=12;
		frame.getContentPane().removeAll();
	}
	
	private static void gameplay() {
		//check for longest road and most knights, check everyones points
	}

	private static void endGame() {

	}
}
