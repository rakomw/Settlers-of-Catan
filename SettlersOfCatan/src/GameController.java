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
   private static Deck deck;
	
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
      deck=new Deck();
		turn=(int)(Math.random()*p);
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
      current_player=players.get(turn);
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
         String harbors_message = "";
         
         boolean lumber_h = hasHarbor(current_player,0);
         boolean ore_h = hasHarbor(current_player,1);
         boolean brick_h = hasHarbor(current_player,2);
         boolean wool_h = hasHarbor(current_player,3);
         boolean grain_h = hasHarbor(current_player,4);
         boolean generic_h = hasHarbor(current_player,5);
         
         if(lumber_h)
            harbors_message+="You can sell Lumber at 2:1\n";
         if(ore_h)            
            harbors_message+="You can sell Ore at 2:1\n";
         if(brick_h)
            harbors_message+="You can sell Brick at 2:1\n";
         if(wool_h)
            harbors_message+="You can sell Wool at 2:1\n";
         if(grain_h)
            harbors_message+="You can sell Grain at 2:1\n";
         if(generic_h)
            harbors_message+="You can sell any resource at 3:1\n";
         else
            harbors_message+="You can sell any resource at 4:1\n";
            
         JOptionPane.showMessageDialog(frame,harbors_message);
			String[] buy_options = {"Lumber","Ore","Brick","Wool","Grain"};
         String resource_buying = (String)JOptionPane.showInputDialog(frame,
            "Which resouce will you buy?", "Trading Overseas",JOptionPane.QUESTION_MESSAGE,
            null,buy_options,buy_options[0]);
            
         String[] sell_options = {"Lumber", "Ore", "Brick", "Wool", "Grain"};
         String resource_selling = (String)JOptionPane.showInputDialog(frame,
         "Which resource will you sell for " + resource_buying + "?","Trading Overseas",
         JOptionPane.QUESTION_MESSAGE,null,sell_options,sell_options[0]);        
         
         if(resource_buying.equals(resource_selling)){
            JOptionPane.showMessageDialog(frame,"Selling a resource for a smaller amount of the same resource seems like a bad idea.\n"+
            "I'm just going to assume that there was a mistake in there.");
            return;
         }
         int bought_type = translate(resource_buying);
         int sold_type = translate(resource_selling);
         int[] sold;
         int[] bought = new int[1];
         bought[0] = bought_type;
         if(hasHarbor(current_player,sold_type))
            sold = new int[2];
            
         else if(hasHarbor(current_player,5))
            sold = new int[3];
         else
            sold = new int[4];
         
         for(int k=0;k<sold.length;k++)
               sold[k]=sold_type;
         if(!current_player.trade(sold,bought))
            JOptionPane.showMessageDialog(frame,"Can't trade what you don't have.");
		}
      private static int translate(String s){
         if(s.equals("Lumber"))
             return 0;
         if(s.equals("Ore"))
            return 1;
         if(s.equals("Brick"))
            return 2;
         if(s.equals("Wool"))
            return 3;
         return 4;
      }
      private static boolean hasHarbor(HumanPlayer p, int harbor_type){
         ArrayList<Integer> ports = p.get_ports();
         for(Integer i:ports)
          if(harbor_type==i)
             return true;
       return false;
      }
	}
	private static class TradeListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
		}
	}
	private static class BuyDevelopmentListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
         int will_purchase = JOptionPane.showConfirmDialog(frame,"A development card costs one ore, one wool, and one grain\n"+
         "Do you want to purchase one?","Development Card Purchase",JOptionPane.YES_NO_OPTION);
         if(will_purchase==JOptionPane.YES_OPTION){
			int[] paid = new int[3];
         paid[0] = 1;
         paid[1] = 3;
         paid[2] = 4;
         if(!current_player.trade(paid,null))
            JOptionPane.showMessageDialog(frame,"You don't have the resources to buy a Development Card");
         else
            current_player.addDevelopmentCard(deck.deal());
         }
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
}
