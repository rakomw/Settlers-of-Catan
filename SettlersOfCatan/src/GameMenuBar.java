import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GameMenuBar extends JMenuBar{
   private  HumanPlayer current_player;
   private  ArrayList<HumanPlayer> players;
   private int turn,num_players,victory_points;
   private  JFrame frame;
   private Deck deck;
   private GameGUI game_gui;
   public Board board;
   
   public void setGUI(GameGUI gui){
      game_gui=gui;
      game_gui.setCurrentPlayer(current_player);
   }
   public GameMenuBar(int p){
      board = Board.getInstance();
      frame = GameController.frame;
      deck=new Deck();
      turn=(int)(Math.random()*p)+20;//padding
      players = new ArrayList<HumanPlayer>();
      
      num_players = p;
      victory_points = 10;
      
      players.add(new HumanPlayer(Color.RED));
      players.add(new HumanPlayer(Color.BLUE));
      
      if(num_players>2)
         players.add(new HumanPlayer(Color.GREEN));
         
      if(num_players>3){
         players.add(new HumanPlayer(Color.ORANGE));
         victory_points=12;
      }
     
      current_player=players.get(turn%players.size());
      createMenus();
   }
   private void createMenus(){
   
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
      
      add(build_menu);
   	
   	
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
      
      add(trade_menu);
   	
   	
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
      
      add(card_menu);
      
      JMenuItem end_turn = new JMenuItem("End turn");
      end_turn.addActionListener(new EndTurnListener());
      JMenuItem refresh = new JMenuItem("Refresh screen");
      refresh.addActionListener(new RefreshListener());
      
      add(end_turn);
      add(refresh);
   }
	
   public void nextTurn(){
      giveLongestRoad();
      giveLargestArmy();
      if(current_player.get_points()>=victory_points)
         GameController.endGame(current_player);
      else{
         current_player=players.get((turn++)%num_players);
         game_gui.setCurrentPlayer(current_player);
         int roll = (int)(Math.random()*11)+2;
         JOptionPane.showMessageDialog(frame,current_player + " has rolled " + roll);
         if(roll==7){
         
            System.out.println("setting state to " + GameGUI.ROBBER_STATE);
            game_gui.setState(GameGUI.ROBBER_STATE);
            JOptionPane.showMessageDialog(frame,"Please move the robber");
         }   
         else
            for(HumanPlayer p:players)
               p.resourceProduction(roll);
      }
   }
   public ArrayList<HumanPlayer> getPlayers(){
      return players;
   }
   private void giveLongestRoad(){
      for(HumanPlayer p:players)
         p.set_longest_road(false);
      //calculate who has the longest continuous road. if no tie and the highest
      //is at least 5 long, do set_longest_road(true) on that player
   }
   private void giveLargestArmy(){
      //reset largest army to false for all players
      for(HumanPlayer p:players)
         p.set_largest_army(false);
      //find player with greatest number of knights
      int max=0;
      for(int k=1;k<players.size();k++)
         if(players.get(k).get_num_knights()>players.get(max).get_num_knights())
            max=k;
      //largest army only applies if the army is at least three knights
      if(players.get(max).get_num_knights()>=3)
         players.get(max).set_largest_army(true);   
   }
   public void discard(){
      for(HumanPlayer p:players){
         if(p.getResourceCards().size()>7){
            JOptionPane.showMessageDialog(frame,"Please pass the computer to "+p);
            while(true){
               JOptionPane.showMessageDialog(frame,"You must discard " + p.getResourceCards().size()/2 + " resource cards.");
               
               ArrayList<Integer> cards = current_player.getResourceCards();
               String message = "You have:\n";
               int[] count = {0,0,0,0,0};
               for(int i:cards)
                  count[i]++;
            
               for(int k=0;k<count.length;k++)
                  message+=count[k]+" " + translate(k)+"\n";
            
               JOptionPane.showMessageDialog(frame,message);
               
               int[] discarding = new int[p.getResourceCards().size()/2];
               String[] options = {"Lumber","Ore","Brick","Wool","Grain"};
               for(int k=0;k<discarding.length;k++){
                  String trashing = (String)JOptionPane.showInputDialog(frame,
                     "Which resouce will you discard?", "Discarding Resources",JOptionPane.QUESTION_MESSAGE,
                     null,options,options[0]);
                  if(trashing==null){
                     k--;
                     continue;
                  }
                     
                  discarding[k]=translate(trashing);
               }
               if(p.hasResources(discarding)){
                  p.trade(discarding,null);
                  break;
               }
               else{
                  JOptionPane.showMessageDialog(frame,"You don't have the resources you selected to discard. Try again");
               }
            }
         } 
      }   
   }
   public static int translate(String s){
      if(s.equals("Lumber"))
         return 0;
      if(s.equals("Ore"))
         return 1;
      if(s.equals("Brick"))
         return 2;
      if(s.equals("Wool"))
         return 3;
      if(s.equals("Grain"))
         return 4;
      return -1;
   }
    
   public static String translate(int n){
      switch(n){
         case 0:
            return "Lumber";
         case 1:
            return "Ore";
         case 2:
            return "Brick";
         case 3:
            return "Wool";
         case 4:
            return "Grain";
         case 5: 
            return "Desert";
         case 6:
            return "Water";
         default:
            return "";
      }  
   }

   private class RoadListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()!=GameGUI.DEFAULT_STATE)
            return;
         int will_purchase = JOptionPane.showConfirmDialog(frame,"A Road costs one lumber and one brick\n"+
            "Do you want to purchase one?","Road Purchase",JOptionPane.YES_NO_OPTION);
         if(will_purchase==JOptionPane.YES_OPTION){
            System.out.println("Will buy road");
            int[] to_remove = {0,2};
            if (current_player.hasResources(to_remove)) {
               System.out.println("has resources");
               current_player.trade(to_remove,new int[0]);
               game_gui.setState(GameGUI.ROAD_STATE);
               System.out.println("Road state set");
            }
            else
               JOptionPane.showMessageDialog(frame,"You don't have the resources to do that");
         }
      }
   }
   private class SettlementListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()!=GameGUI.DEFAULT_STATE)
            return;
         int will_purchase = JOptionPane.showConfirmDialog(frame,"A Town costs one lumber, one brick, one wool, and one grain\n"+
            "Do you want to purchase one?","Town Purchase",JOptionPane.YES_NO_OPTION);
         if(will_purchase==JOptionPane.YES_OPTION){
            int[] cost = {0,2,3,4};
            if (current_player.hasResources(cost)){ 
               current_player.trade(cost,new int[0]);
               game_gui.setState(GameGUI.TOWN_STATE); 
            }
            else
               JOptionPane.showMessageDialog(frame,"You don't have the resources to do that");
         }
      }
   }
   private class CityListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()!=GameGUI.DEFAULT_STATE)
            return;
         int will_purchase = JOptionPane.showConfirmDialog(frame,"A City costs three ore and two grain\n"+
            "Do you want to purchase one?","City Purchase",JOptionPane.YES_NO_OPTION);
         if(will_purchase==JOptionPane.YES_OPTION){
            int[] cost= {1,1,1,4,4};
            if (current_player.hasResources(cost)){ 
               current_player.trade(cost,new int[0]); 
               game_gui.setState(GameGUI.CITY_STATE);
            }
            else
               JOptionPane.showMessageDialog(frame,"You don't have the resources to do that");
         }
      }
   }

   private class BankListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()!=GameGUI.DEFAULT_STATE)
            return;
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
         
         if(resource_buying==null)
            return;
            
         String[] sell_options = {"Lumber", "Ore", "Brick", "Wool", "Grain"};
         String resource_selling = (String)JOptionPane.showInputDialog(frame,
            "Which resource will you sell for " + resource_buying + "?","Trading Overseas",
            JOptionPane.QUESTION_MESSAGE,null,sell_options,sell_options[0]);
                    
         if(resource_selling==null)
            return;
            
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
         if(current_player.hasResources(sold))
            current_player.trade(sold,bought);
         else
            JOptionPane.showMessageDialog(frame,"Can't trade what you don't have.");
      }
      
      private boolean hasHarbor(HumanPlayer p, int harbor_type){
         ArrayList<Integer> ports = p.get_ports();
         for(Integer i:ports)
            if(harbor_type==i)
               return true;
         return false;
      }
   }
   private class TradeListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()!=GameGUI.DEFAULT_STATE)
            return;
         ArrayList<HumanPlayer> playerOptions = new ArrayList<HumanPlayer>();
      
         for(HumanPlayer p:players)
            if(!p.equals(current_player)) 
               playerOptions.add(p);
      
         HumanPlayer trading_with = (HumanPlayer)JOptionPane.showInputDialog(frame,
            "With which player to you want to trade? ","Trading with a player",
            JOptionPane.QUESTION_MESSAGE,null,playerOptions.toArray(),playerOptions.get(0));   
         
         if(trading_with==null)
            return;
            
         String[] buy_options = {"Lumber","Ore","Brick","Wool","Grain"};
         String resource_buying = (String)JOptionPane.showInputDialog(frame,
            "Which resouce will you offer to buy?", "Trading with a player",JOptionPane.QUESTION_MESSAGE,
            null,buy_options,buy_options[0]);
         
         if(resource_buying==null)
            return;
            
         int num_buying;
         try{
            num_buying = Integer.parseInt(JOptionPane.showInputDialog(frame,"How many " + resource_buying + " are you offering to buy?"));
         }
         catch(NumberFormatException ex){
            return;
         }
         
         String[] sell_options = {"Lumber", "Ore", "Brick", "Wool", "Grain"};
         String resource_selling = (String)JOptionPane.showInputDialog(frame,
            "Which resource will you offer to sell for " + resource_buying + "?","Trading with a player",
            JOptionPane.QUESTION_MESSAGE,null,sell_options,sell_options[0]);        
         
         if(resource_selling==null)
            return;
        
         int num_selling;
         try{
            num_selling = Integer.parseInt(JOptionPane.showInputDialog(frame,"How many " + resource_selling + " are you offering to sell?"));
         }
         catch(NumberFormatException ex){
            return;
         }
             
         int[] buying = new int[num_buying];
         for(int k=0;k<buying.length;k++)
            buying[k]=translate(resource_buying);
            
         int[] selling = new int[num_selling];
         for(int k=0;k<selling.length;k++)
            selling[k]=translate(resource_selling);
            
         if(current_player.hasResources(selling)){
            JOptionPane.showMessageDialog(frame,"Please pass the computer to " + trading_with + ".");
            int will_trade = JOptionPane.showConfirmDialog(frame,"You have been offered " + num_selling + " " + resource_selling +
               " in exchange for " + num_buying + " " + resource_buying + ".\nWill you make this trade?",
               "Trade with a player",JOptionPane.YES_NO_OPTION);
            if(will_trade==JOptionPane.YES_OPTION){
               if(trading_with.hasResources(buying)){
                  current_player.trade(selling,buying);
                  trading_with.trade(buying,selling);
               }
               else
                  JOptionPane.showMessageDialog(frame,"You don't have the necessary resources to make this trade");
            }
            else
               JOptionPane.showMessageDialog(frame,"The trade was not accepted");
            JOptionPane.showMessageDialog(frame,"Please pass the computer back to " + current_player + " so they may finish their turn");
         }
         else
            JOptionPane.showMessageDialog(frame,"You don't have the necessary resources to make this trade");
         
      }
   }
   private class BuyDevelopmentListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()!=GameGUI.DEFAULT_STATE)
            return;
         int will_purchase = JOptionPane.showConfirmDialog(frame,"A development card costs one ore, one wool, and one grain\n"+
            "Do you want to purchase one?","Development Card Purchase",JOptionPane.YES_NO_OPTION);
         if(will_purchase==JOptionPane.YES_OPTION){
            int[] paid = new int[3];
            paid[0] = 1;
            paid[1] = 3;
            paid[2] = 4;
            if(!current_player.hasResources(paid))
               JOptionPane.showMessageDialog(frame,"You don't have the resources to buy a Development Card");
            else{
               current_player.trade(paid,new int[0]);
               current_player.addDevelopmentCard(deck.deal());
            }
         }
      }
   }
   private class UseDevelopmentListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()!=GameGUI.DEFAULT_STATE)
            return;
         System.out.println("Development listener triggered");
         ArrayList<Card> options = current_player.getDevelopmentCards();
         Object[] option_arr = options.toArray();
         System.out.println("Options array is currently: ");
         for(Object obj:option_arr)
            System.out.print(obj+"\t");
         
         Card choice = (Card)JOptionPane.showInputDialog(frame, "what card would you like to use?",
            "development card usage",JOptionPane.QUESTION_MESSAGE,null,option_arr,option_arr[0]);
         if(choice==null)
            return;
         options.remove(choice);
         System.out.println("Chosen Card is " + choice);
         System.out.println("Suit is " + choice.get_suit());
         switch (choice.get_suit()) {
            case 0: 
                  System.out.println("Entered knight case");
                  current_player.add_knight();
                  JOptionPane.showMessageDialog(frame,"Please move the robber");
                  game_gui.setState(GameGUI.ROBBER_STATE);
                  System.out.println("Robber state set");
                  break;          
            case 1:
                  current_player.free_points_plus();
                  break;      
            case 2:         
                  System.out.println("Monopoly case entered");
                  String[] les_options = {"Lumber","Ore","Brick","Wool","Grain"};
                  String monopoly;
                  do{
                     monopoly =  (String)JOptionPane.showInputDialog(frame, "you used Monopoly, what would you like to steal?",
                        "Monopoly",JOptionPane.QUESTION_MESSAGE,null,les_options,les_options[0]);
                  }while(monopoly==null);
                  System.out.println(monopoly+" chosen");
                  int resource = translate(monopoly);
                  int[] res = {resource};
               
                  for (int i=0;i<players.size();i++) {
                     if (!players.get(i).equals(current_player)) {
                        while (players.get(i).hasResources(res)) {
                           players.get(i).trade(res, new int[0]); 
                           current_player.trade(new int[0], res);
                        }
                     }
                  }           
                  break;
            case 3: 
                  JOptionPane.showMessageDialog(frame,"Please select the location for your first free road");
                  System.out.println("Free road case entered");
                  game_gui.twoFreeRoads();
                  break;       
            case 4:           
                  System.out.println("Year of Plenty case entered");              
                  String[] the_options = {"Lumber","Ore","Brick","Wool","Grain"};
                  int[] trades = new int[2];
                  for(int k=0;k<2;k++){
                     String plenty;
                     do{
                        plenty =  (String)JOptionPane.showInputDialog(frame, "you used Year of Plenty: choose your resource",
                           "Year of Plenty",JOptionPane.QUESTION_MESSAGE,null,the_options,the_options[0]);
                     }while(plenty==null);
                  
                     trades[k] = translate(plenty);
                  
                  }
                  current_player.trade(new int[0], trades);
                  break;
         } 
      }
   }
   private class ViewDevCardListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()!=GameGUI.DEFAULT_STATE)
            return;
         ArrayList<Card> cards = current_player.getDevelopmentCards();
         String message = "You have:\n";
         if(cards.size()==0)
            message+="No development cards";
         for(Card c:cards)
            message+=c;
         JOptionPane.showMessageDialog(frame,message);
      }
   }
   private class ViewResourceCardListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()!=GameGUI.DEFAULT_STATE)
            return;
         ArrayList<Integer> cards = current_player.getResourceCards();
         String message = "You have:\n";
         int[] count = {0,0,0,0,0};
         for(int i:cards)
            count[i]++;
         
         for(int k=0;k<count.length;k++)
            message+=count[k]+" " + translate(k)+"\n";
         
         JOptionPane.showMessageDialog(frame,message);
      }
   }
   
   private class EndTurnListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         if(game_gui.getState()==game_gui.DEFAULT_STATE)
            nextTurn();
         else
            JOptionPane.showMessageDialog(frame,"You can't end your turn yet");
      }
   }
   private class RefreshListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         //force an update if canvas image is behind for some reason
         game_gui.update(game_gui.getGraphics());
      }
   }
}
