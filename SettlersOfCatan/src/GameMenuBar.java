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
   }
   public GameMenuBar(int p){
      board = Board.getInstance();
      frame = GameController.frame;
      deck=new Deck();
      turn=(int)(Math.random()*p);
      players = new ArrayList<HumanPlayer>();
      num_players = p;
      if(num_players==3)
         victory_points=10;
      else{
         players.add(new HumanPlayer(Color.ORANGE));
         victory_points=12;
      }
      
      players.add(new HumanPlayer(Color.RED));
      players.add(new HumanPlayer(Color.BLUE));
      players.add(new HumanPlayer(Color.GREEN));
     
      current_player=players.get(turn);
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
      
      add(end_turn);
   }
	
   private void nextTurn(){
      giveLongestRoad();
      giveLargestArmy();
      if(current_player.get_points()>=victory_points)
         GameController.endGame(current_player);
      else{
         current_player=players.get((turn++)%num_players);
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
   
   private void giveLongestRoad(){
      for(HumanPlayer p:players)
         p.set_longest_road(false);
      //calculate who has the longest continuous road. if no tie and the highest
      //is at least 5 long, do set_longest_road(true) on that player
   }
   private void giveLargestArmy(){
      for(HumanPlayer p:players)
         p.set_largest_army(false);
      int max=0;
      for(int k=1;k<players.size();k++)
         if(players.get(k).get_num_knights()>players.get(max).get_num_knights())
            max=k;
            
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
   
   public void doRobber(int r,int c){
      game_gui.setState(game_gui.DEFAULT_STATE);
      System.out.println("doRobber");
      ArrayList<HumanPlayer> list = new ArrayList<HumanPlayer>();
      for(HumanPlayer p:players)
         for (TownNode town : p.getTowns()) 
            for (Tile tile : town.getAdjacentTiles()) 
               if (tile.equals(board.getTileAt(r,c))&& !p.equals(current_player)) 
                  list.add(p);
      if(list.isEmpty())
         return;
      HumanPlayer[] options = (HumanPlayer[])(list.toArray());
      HumanPlayer stealing_from;
      do{
         stealing_from = (HumanPlayer)JOptionPane.showInputDialog(frame,
               "From which player will you steal? ","Stealing a resource",
               JOptionPane.QUESTION_MESSAGE,null,options,options[0]);  
      }while (stealing_from==null);
         
      ArrayList<Integer> cards = stealing_from.getResourceCards();
      int stolen = cards.get((int)(Math.random()*cards.size()));
      int[] taking = {stolen};
      int[] giving = {};
      current_player.trade(giving,taking);
      stealing_from.trade(taking,giving);
   }
	
   public void buildTown(TownNode town){
      if(town.isBuildable(current_player)){
         if(town.get_level()==0){
            int[] paid={1,0,1,1,1};
            current_player.trade(paid,null);
         }
         else{
            int[] paid={0,3,0,0,2};
            current_player.trade(paid,null);
         }
         town.buildUp(current_player);
         game_gui.setState(GameGUI.DEFAULT_STATE);
      }
      else
         JOptionPane.showMessageDialog(frame,"You can't build there. Please try another location");
   }
  
	public void buildRoad(RoadNode road) {
       current_player.build_road(road);
       int[] to_remove = {1,0,1,0,0};
       if (!current_player.trade(to_remove, new int[0])) {
          System.out.println("Player hand not tested correctly");
       }
    }
    
   
   public void startingTowns(){ 
   if(turn<0){
      turn=0;
      return;
   }
      current_player = players.get(turn%players.size());
      if(current_player.getTowns().size()>0){
         townsTwo();
         return;
      }
      game_gui.setState(GameGUI.STARTING_TOWN_STATE);
      JOptionPane.showMessageDialog(frame,current_player+", please build your first town");
   }   
      
   public void buildStartTown(TownNode town){
      if(town.startBuildable(current_player)){
         current_player.build_town(town);
         game_gui.update(game_gui.getGraphics());
         game_gui.setState(GameGUI.STARTING_ROAD_STATE);
         JOptionPane.showMessageDialog(frame,current_player + ", please build a road");
      }   
   }   
  
   public void buildStartRoad(RoadNode road){
      if(road.startBuildable(current_player)){
         current_player.build_road(road);
         game_gui.update(game_gui.getGraphics());
         if(current_player.getTowns().size()>1)
            turn--;
         else
            turn++;
         startingTowns();
      }     
   }
   
   public void townsTwo(){ 
      turn--;
      if(turn<0){
         turn=0;
         return;
      }
      current_player = players.get(turn%players.size());
      if(current_player.getTowns().size()>1)
         return;
      JOptionPane.showMessageDialog(frame,current_player+", please build your second town");
      game_gui.setState(GameGUI.STARTING_TOWN_STATE);
   }
      
   private class RoadListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         int[] to_remove = {1,0,1,0,0};
         if (current_player.hasResources(to_remove)) {
           game_gui.setState(GameGUI.ROAD_STATE);
         }
      }
   }
   private class SettlementListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         int[] cost = {1,0,1,1,1};
         if (current_player.hasResources(cost)){ 
            current_player.trade(cost,null);
            game_gui.setState(GameGUI.TOWN_STATE); 
         }
      }
   }
   private class CityListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         int[] cost= {0,3,0,0,2};
         if (current_player.hasResources(cost)){ 
            current_player.trade(cost,null); 
            game_gui.setState(GameGUI.CITY_STATE);
         }
      }
   }

   private class BankListener implements ActionListener{
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
         if(!current_player.trade(sold,bought))
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
         int will_purchase = JOptionPane.showConfirmDialog(frame,"A development card costs one ore, one wool, and one grain\n"+
            "Do you want to purchase one?","Development Card Purchase",JOptionPane.YES_NO_OPTION);
         if(will_purchase==JOptionPane.YES_OPTION){
            int[] paid = new int[3];
            paid[0] = 1;
            paid[1] = 3;
            paid[2] = 4;
            if(!current_player.trade(paid,new int[0]))
               JOptionPane.showMessageDialog(frame,"You don't have the resources to buy a Development Card");
            else
               current_player.addDevelopmentCard(deck.deal());
         }
      }
   }
   private class UseDevelopmentListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
         ArrayList<Card> int_options = current_player.getDevelopmentCards();
         ArrayList<String> options = new ArrayList<String>();
         for (int i=0;i<int_options.size();i++) {
            switch (int_options.get(i).get_suit()) {
               case 0: 
                  {
                     options.add("Knight");
                     break;
                  }
               case 1: 
                  {
                     options.add("Free Point");
                     break;
                  }
               case 2: 
                  {
                     options.add("Monopoly");
                     break;
                  }
               case 3: 
                  {
                     options.add("Road Building");
                     break;
                  }
               case 4: 
                  {
                     options.add("Year of Plenty");
                     break;
                  }
            }
         }
         Object[] option_arr = options.toArray();
         String full_input = (String)JOptionPane.showInputDialog(frame, "what card would you like to use?",
            "development card usage",JOptionPane.QUESTION_MESSAGE,null,option_arr,option_arr[0]);
         String input = full_input.substring(0,1);
       
         int use;
         if (input.equals("K")) {
            use = 0;
         }
         if (input.equals("F")) {
            use = 1;
         }
         if (input.equals("M")) {
            use = 2;
         }
         if (input.equals("R")) {
            use = 3;
         }
         if (input.equals("Y")) {
            use = 4;
         }
         else use = -1;
         switch (use) {
            case 0: 
               {
                  current_player.add_knight();
                  game_gui.setState(GameGUI.ROBBER_STATE);
                  break;
               }           
            case 1: 
               {
                  current_player.free_points_plus();
                  break;
               }       
            case 2: 
               {           
                  String[] les_options = {"Lumber","Ore","Brick","Wool","Grain"};
                  String plenty;
                  do{
                     plenty =  (String)JOptionPane.showInputDialog(frame, "you used Monopoly, what would you like to steal?",
                        "Monopoly",JOptionPane.QUESTION_MESSAGE,null,les_options,les_options[0]);
                  }while(plenty==null);
               
                  int resource = translate(plenty);
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
               }
            case 3: 
               {
                  RoadNode place1 = null;
               //player input @ TODO SIR ROBERT
                  current_player.build_road(place1);
               
                  RoadNode place2 = null;
               //player input
                  current_player.build_road(place2);
                  break;
               }           
            case 4: 
               {                        
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
   }
   private class ViewDevCardListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
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
         nextTurn();
      }
   }
}
