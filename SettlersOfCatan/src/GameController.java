import javax.swing.*;

import java.awt.Color;
import java.awt.event.*;
import java.util.*;

public class GameController {
	private static JFrame frame;
	private static StartMenu start_menu;
	private static GameGUI game_gui;
	private static int num_players;
	private static int victory_points;
	private static ArrayList<HumanPlayer> players;
	private static int turn;
   private static HumanPlayer current_player;
	private static JMenuBar menu_bar;
	
	public static void main(String[] args) {
		/*String message = "This game is still in progress.\nDon't worry though, we have highly trained specialists hard at work";
		JOptionPane.showMessageDialog(null, message);*/
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
		turn=0;
		start_menu.setVisible(false);
		players= new ArrayList<HumanPlayer>();
		num_players = p;
		if(num_players==3)
			victory_points=10;
		else{
			players.add(new HumanPlayer(Color.ORANGE));
			victory_points=12;
		}
		frame.getContentPane().removeAll();

		players.add(new HumanPlayer(Color.RED));
		players.add(new HumanPlayer(Color.BLUE));
		players.add(new HumanPlayer(Color.GREEN));
		setUpMainGUI();
	}
	
	private static void setUpMainGUI(){
		menu_bar = new JMenuBar();
		
		JMenu build_menu = new JMenu("Build");
		JMenuItem build_road = new JMenuItem("Build a road");
		JMenuItem build_settlement = new JMenuItem("Build a settlement");
		JMenuItem build_city = new JMenuItem("Build a city");
		
		build_road.addActionListener(new RoadListener());
		build_settlement.addActionListener(new SettlementListener());
		build_city.addActionListener(new CityListener());
		
		build_menu.add(build_road);
		build_menu.add(build_settlement);
		build_menu.add(build_city);
		menu_bar.add(build_menu);
		
		
		JMenu trade_menu = new JMenu("Trade");
		JMenuItem bank_trade = new JMenuItem("Trade Overseas");
		JMenuItem player_trade = new JMenuItem("Trade with another player");
		JMenuItem view_resource = new JMenuItem("View Your Resource Cards");
		
		bank_trade.addActionListener(new BankListener());
		player_trade.addActionListener(new TradeListener());
		view_resource.addActionListener(new ViewResourceCardListener());
		
		trade_menu.add(bank_trade);
		trade_menu.add(player_trade);
		trade_menu.add(view_resource);
		menu_bar.add(trade_menu);
		
		
		JMenu card_menu = new JMenu("Development cards");
		JMenuItem view_development = new JMenuItem("View Development Cards");
		JMenuItem buy_development = new JMenuItem("Buy a development card");
		JMenuItem play_development = new JMenuItem("Play a development card");
		
		view_development.addActionListener(new ViewDevCardListener());
		buy_development.addActionListener(new BuyDevelopmentListener());
		play_development.addActionListener(new UseDevelopmentListener());
		
		card_menu.add(view_development);
		card_menu.add(buy_development);
		card_menu.add(play_development);
		menu_bar.add(card_menu);
		
		frame.setJMenuBar(menu_bar);
	}
	
   public static void nextTurn(){
      current_player=players.get(turn++);
   }
	
	
	private static class RoadListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			//TODO Allow clicking of road nodes
		}
	}
	private static class SettlementListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			//TODO Allow clicking of town nodes
		}
	}
	private static class CityListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			//TODO Allow clicking of player's settlements
		}
	}
	private static class BankListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
         /*String harbors_message = "";
         if(current_player.hasGenericHarbor())
            harbors_message+="You can sell any resource at 3:1";
         if(current_player.hasLumberHarbor())
            harbors_message+="You can sell Lumber at 2:1";
         if(current_player.hasOreHarbor())
            harbors_message+="You can sell Ore at 2:1";
         if(current_player.hasBrickHarbor())
            harbors_message+="You can sell Brick at 2:1";
         if(current_player.hasWoolHarbor())
         
         if(current_player.hasGrainHarbor())
         */
			String[] buy_options = {"Lumber","Ore","Brick","Wool","Grain"};
         String resource_buying = (String)JOptionPane.showInputDialog(frame,
            "Which resouce will you buy?", "Trading Overseas",JOptionPane.QUESTION_MESSAGE,
            null,buy_options,buy_options[0]);
         String[] sell_options = {"Lumber", "Ore", "Brick", "Wool", "Grain"};
         String resource_selling = (String)JOptionPane.showInputDialog(frame,
         "Which resource will you sell for " + resource_buying + "?","Trading Overseas",
         JOptionPane.QUESTION_MESSAGE,null,sell_options,sell_options[0]);        
         
		}
	}
	private static class TradeListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
		}
	}
	private static class BuyDevelopmentListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
		}
	}
	private static class UseDevelopmentListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
		}
	}
	private static class ViewDevCardListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
		}
	}
	private static class ViewResourceCardListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
		}
	}
   private static boolean hasHarbor(HumanPlayer p, int harbor_type){
      ArrayList<Integer> ports = p.get_ports();
      return true;
   }
}
