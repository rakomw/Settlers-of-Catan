import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class GameGUI extends Canvas implements MouseListener{
   private JFrame frame;
   //tiles holds the BufferedImage representations of each type of tile. Index corresponds to the constants defined in Tile
   private BufferedImage[] tiles;
   //dice holds the BufferedImage representations of the numbers 2-12. Indices 0 and 1 purposefully null
   private BufferedImage[] dice;
   //settlements and cities of each color
   private BufferedImage settlementBlue,settlementRed,settlementOrange,settlementGreen;
   private BufferedImage cityBlue,cityRed,cityOrange,cityGreen;
   //generic 3:1 harbors
   private BufferedImage northwestPort, northeastPort, southwestPort, eastPort;
   //specific resource 2:1 harbors
   private BufferedImage woolPort,orePort,lumberPort,grainPort,brickPort;
   
   private BufferedImage robber;
   
   //2D array of the actual tiles in the board
   private BufferedImage[][] hexes = {new BufferedImage[3], new BufferedImage[4], new BufferedImage[5],new BufferedImage[4],new BufferedImage[3]};
   //2D array to hold the actual roll values of the hexes on the board
   private BufferedImage[][] rolls = {new BufferedImage[3], new BufferedImage[4], new BufferedImage[5],new BufferedImage[4],new BufferedImage[3]};
   
   //all of the potential town locations on the board
   private TownNode[][] towns;
   //all of the potential road locations on the board
   private RoadNode[][] roads;
   //Polygon representations of the hitboxes of all RoadNodes
   private Polygon[][] road_hitboxes;
   private Board board;
   private GameMenuBar menu_bar;
   //spacing for tiles: 0 for both is edges touching, 2 and 4 are preferred to make road and town areas clearer
   //changing the gaps is not recommended right now because current hitboxes probably won't map correctly with different gaps 
   private static final int HORIZONTAL_GAP=2,VERTICAL_GAP=4;
   //various states that determine how the mouselistener responds to clicks
   public static final int DEFAULT_STATE = 0, ROAD_STATE = 1, TOWN_STATE = 2, CITY_STATE = 3, ROBBER_STATE = 4, STARTING_TOWN_STATE = 5,STARTING_ROAD_STATE = 6, FREE_ROADS_STATE = 7;
   //state is the current state of the GUI, stage is the step that the GUI is on in either 
   //initial town and road placement or in the two free roads development card, turn is the location in the player list
   private int state, stage, turn;
   private ArrayList<HumanPlayer> players;
   private HumanPlayer current_player;

   
   
   public void setMenuBar(GameMenuBar bar){
      menu_bar=bar;
      players=menu_bar.getPlayers();
   }
   public GameGUI(){
      //stage starts at 0 for initial road and town placement
      stage=0;
      frame = GameController.frame;
      board = Board.getInstance();
      towns = board.getTowns();
      roads = board.getRoads();
      constructRoadHitboxes();
      addMouseListener(this);      
      
      tiles = new BufferedImage[7];
      dice = new BufferedImage[13];
      
      try{
         tiles[Tile.LUMBER] = ImageIO.read(new File("resources/lumber.gif"));
         tiles[Tile.ORE] = ImageIO.read(new File("resources/ore.gif"));
         tiles[Tile.BRICK] = ImageIO.read(new File("resources/brick.gif"));
         tiles[Tile.WOOL] = ImageIO.read(new File("resources/wool.gif"));
         tiles[Tile.GRAIN] = ImageIO.read(new File("resources/grain.gif"));
         tiles[Tile.DESERT] = ImageIO.read(new File("resources/desert.gif"));
         tiles[Tile.WATER] = ImageIO.read(new File("resources/water.gif"));
        
         northwestPort = ImageIO.read(new File("resources/northwestPort.gif"));
         northeastPort = ImageIO.read(new File("resources/northeastPort.gif"));
         southwestPort = ImageIO.read(new File("resources/southwestPort.gif"));
         eastPort = ImageIO.read(new File("resources/eastPort.gif"));
         
         woolPort = ImageIO.read(new File("resources/portSheep.gif"));
         orePort = ImageIO.read(new File("resources/portOre.gif"));
         lumberPort = ImageIO.read(new File("resources/portLumber.gif"));
         grainPort = ImageIO.read(new File("resources/portGrain.gif"));
         brickPort = ImageIO.read(new File("resources/portBrick.gif"));
        
         settlementBlue = ImageIO.read(new File("resources/settlementBlue.gif"));
         settlementRed = ImageIO.read(new File("resources/settlementRed.gif"));
         settlementOrange = ImageIO.read(new File("resources/settlementOrange.gif"));
         settlementGreen = ImageIO.read(new File("resources/settlementGreen.gif")); 
        
         cityBlue = ImageIO.read(new File("resources/cityBlue.gif"));
         cityRed = ImageIO.read(new File("resources/cityRed.gif"));
         cityOrange = ImageIO.read(new File("resources/cityOrange.gif"));
         cityGreen = ImageIO.read(new File("resources/cityGreen.gif"));
      
         robber = ImageIO.read(new File("resources/robber.gif"));
         
         for(int k=2;k<=12;k++)
            dice[k] = ImageIO.read(new File("resources/dice"+k+".gif"));
      }
      catch(IOException e){
         System.out.println("The images did not load correctly, so expect everything to be wrong from here on out");
      }
      
      
      
      for (int r=0; r < hexes.length; r++){ 
         for (int c=0; c < hexes[r].length; c++){ 
            hexes[r][c] = tiles[board.getTileAt(r,c).resource];
            rolls[r][c] = dice[board.getTileAt(r,c).roll];
         }
      }
      
      state = DEFAULT_STATE;
   }
   private void constructRoadHitboxes() {
      road_hitboxes = new Polygon[roads.length][];
      for (int r = 0; r < road_hitboxes.length; r++) {
         road_hitboxes[r] = new Polygon[roads[r].length];
      }
      
      // create the road polygons
      for (int r = 0; r < roads.length; r++) {
         for (int c = 0; c < roads[r].length; c++) {
            // variable decs.
            final int starting_y = 66, // the y value of the top-left-most pixel of the roads
                     vert_height = 30, // the height of vertical roads
                     diag_height = 17, // the height of diagonal roads
                     diag_width = 27, // the width of diagonal roads
                     road_thickness = 3; // half the entire width of the road
                     // the left-most pixel of each row of roads
            int[] left_bounds = {200, 200, 172, 172, 143, 143, 143, 172, 172, 200, 200};
            // find the end points of the road
            
            // in vertical roads, the top point corresponds to index 0
            int[] end_x = new int[2]; 
            int[] end_y = new int[2];
            
            if (r % 2 == 0) { // if diagonal road
               end_x[0] = left_bounds[r] + (diag_width + 1) * c ;
               end_x[1] = end_x[0] + diag_width;
               if (c % 2 == 0) { // if upward road
                  if (r < 5) { // if north
                     end_y[0] = starting_y + ((diag_height + vert_height + 2) * (r / 2));
                     end_y[1] = end_y[0] - diag_height;
                  }
                  else { // if south
                     end_y[0] = starting_y - diag_height + ((diag_height + vert_height + 2) * (r / 2));
                     end_y[1] = end_y[0] + diag_height;
                  }
               }
               else { // if downward road
                  if (r < 5) { // if north
                     end_y[0] = starting_y - diag_height + ((diag_height + vert_height + 2) * (r / 2));
                     end_y[1] = end_y[0] + diag_height;
                  }
                  else { // if south
                     end_y[0] = starting_y + ((diag_height + vert_height + 2) * (r / 2));
                     end_y[1] = end_y[0] - diag_height;
                  }
               }
            }
            else { // if vertical road
               end_x[0] = left_bounds[r] + ((diag_width * 2 + 2) * c);
               end_x[1] = end_x[0];
               end_y[0] = starting_y + ((diag_height + vert_height + 2) * (r / 2));
               end_y[1] = end_y[0] + vert_height;
            }
            
            // find the corners of the polygon
            double factor;
            int[] slope = new int[2]; // index 0 is numerator, 1 denominator
            // get road slope
            slope[0] = end_y[1] - end_y[0];
            slope[1] = end_x[1] - end_x[0];
            // perpendicularize the slope
            int[] perp_slope = {-1 * slope[1], slope[0]};
            factor=(double)road_thickness/Math.sqrt((double)(perp_slope[0]*perp_slope[0])+
                                                    (double)(perp_slope[1]*perp_slope[1]));
            
            int[] corner_xs = new int[4];
            int[] corner_ys = new int[4];
            corner_xs[0] = (int)((double)end_x[0] - (double)perp_slope[1] * factor);
            corner_ys[0] = (int)((double)end_y[0] - (double)perp_slope[0] * factor);
            corner_xs[1] = (int)((double)end_x[0] + (double)perp_slope[1] * factor);
            corner_ys[1] = (int)((double)end_y[0] + (double)perp_slope[0] * factor);
            corner_xs[2] = (int)((double)end_x[1] + (double)perp_slope[1] * factor);
            corner_ys[2] = (int)((double)end_y[1] + (double)perp_slope[0] * factor);
            corner_xs[3] = (int)((double)end_x[1] - (double)perp_slope[1] * factor);
            corner_ys[3] = (int)((double)end_y[1] - (double)perp_slope[0] * factor);
            
            // produce final product
            road_hitboxes[r][c] = new Polygon(corner_xs, corner_ys, 4);
         }
      }
   }

   public void paint(Graphics g){
      setBackground(new Color(59,85,146));
      
      //draws all of the land hexes and their rolls
      for (int r=0; r < hexes.length; r++){ 
         for (int c=0; c < hexes[r].length; c++){
            int x = (c*(55+HORIZONTAL_GAP)) + (200 - ((2 - (Math.abs(r-2)))*(55+HORIZONTAL_GAP)/2));
            int y = (r*(45+VERTICAL_GAP)) + 50;
            g.drawImage(hexes[r][c],x,y,this);
            
            if(board.getTileAt(r,c).hasRobber()){
               g.drawImage(robber,x+15,y+20,this);
            }
            else
               g.drawImage(rolls[r][c],x+15,y+20,this);
         } 
      }
      //draw 3:1 ports
      g.drawImage(northwestPort,171,2,this);
      g.drawImage(northeastPort,370,50,this);
      g.drawImage(eastPort,427,148,this);
      g.drawImage(southwestPort,172,295,this);
      //draw 2:1 ports
      g.drawImage(woolPort,286,2,this);
      g.drawImage(orePort,116,99,this);
      g.drawImage(lumberPort,285,295,this);
      g.drawImage(grainPort,116,197,this);
      g.drawImage(brickPort,371,246,this);
      
      //draw all roads
      for (int r = 0; r < roads.length; r++) {
         for (int c = 0; c < roads[r].length; c++) {
            if (roads[r][c].getOwner() != null) {
               g.setColor(roads[r][c].getOwner().getColor());
               g.fillPolygon(road_hitboxes[r][c]);
            }
         }
      }
      
      //draw settlements/cities
      for(int r=0; r < towns.length; r++){
         for(int c=0;c < towns[r].length; c++){
            TownNode tempTown = towns[r][c];
            if(tempTown.get_level()==TownNode.NOTHING)
               continue;
            int y,x;
            x = (c*27)+(195-(27*(int)(2.5-Math.abs(r-2.5))));
            if(r<3){
               y = (r*50) + 50 - (15*(c%2));
            }
            else if(r==3){
               y = (r*50)+25+(15*(c%2));
            }
            else{
               y = (r*50)+25+(15*(c%2));
            }
            //check for towns. if found, draw appropriate colored settlement
            if(tempTown.get_level()==TownNode.TOWN){
               if(tempTown.getColor().equals(Color.RED))
                  g.drawImage(settlementRed,x,y,this);
               else if(tempTown.getColor().equals(Color.GREEN))
                  g.drawImage(settlementGreen,x,y,this);
               else if(tempTown.getColor().equals(Color.ORANGE))
                  g.drawImage(settlementOrange,x,y,this);
               else if(tempTown.getColor().equals(Color.BLUE))
                  g.drawImage(settlementBlue,x,y,this);      
            }
            //check for cities
            else if(tempTown.get_level()==TownNode.CITY){
               if(tempTown.getColor().equals(Color.RED))
                  g.drawImage(cityRed,x,y,this);
               else if(tempTown.getColor().equals(Color.GREEN))
                  g.drawImage(cityGreen,x,y,this);
               else if(tempTown.getColor().equals(Color.ORANGE))
                  g.drawImage(cityOrange,x,y,this);
               else if(tempTown.getColor().equals(Color.BLUE))
                  g.drawImage(cityBlue,x,y,this); 
            }
            //Testing purposes
            //System.out.println("Row: "+r+"\tCol: "+c+"\tX: "+x+"\tY: "+y);
            //g.drawImage(settlementRed,x,y,this);
            
         }
      }
      
   }

   public void mouseClicked(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      int[] coords;
      System.out.println("Click at "+x+","+y);
      System.out.println("State:"+state);
      switch(state){
         case DEFAULT_STATE:
            if(e.isMetaDown()){
               coords = mapToTile(x,y);
               if(coords[0]>=0 && coords[1]>=0)
                  JOptionPane.showMessageDialog(frame,board.getTileAt(coords[0],coords[1]));
               
            }
            break;
            
         case ROAD_STATE:
            System.out.println("Entered road case");
            System.out.println("Coords are " + x + " " + y);
            for (int r = 0; r < roads.length; r++) {
               for (int c = 0; c < roads[r].length; c++) {
                  if (road_hitboxes[r][c].contains(x, y)) {
                     System.out.println("Road clicked");
                     if(roads[r][c].isBuildable(current_player)){
                        System.out.println("Road buildable");
                        current_player.build_road(roads[r][c]);
                        setState(DEFAULT_STATE);
                        update(getGraphics());
                     }
                     else
                        JOptionPane.showMessageDialog(frame,"You can't build there. Please try another location");
                  }
               }
            }
            break;
            
         case TOWN_STATE:
            coords = mapToTown(x,y);
            
            if(coords[0]<0 || coords[1]<0){
               System.out.println("No town node clicked");
               return;
            }
               
            if(towns[coords[0]][coords[1]].get_level()>0){
               System.out.println("Trying to build a town on another town");
               return;
            }
            
            if(towns[coords[0]][coords[1]].isBuildable(current_player)){
               towns[coords[0]][coords[1]].buildUp(current_player);
               setState(DEFAULT_STATE);
               update(getGraphics());
            }
            else
               JOptionPane.showMessageDialog(frame,"You can't build there. Please try another location");
                  
            update(getGraphics());
            break;
            
         case CITY_STATE:
            coords = mapToTown(x,y);
            if(coords[0]<0 || coords[1]<0){
               System.out.println("No town node clicked");
               return;
            }
                
            if(towns[coords[0]][coords[1]].get_level()!=1){
               System.out.println("Not clicking a town to upgrade to a city");
               return;
            }
            
            if(towns[coords[0]][coords[1]].isBuildable(current_player)){
               towns[coords[0]][coords[1]].buildUp(current_player);
               setState(DEFAULT_STATE);
               update(getGraphics());
            }
            else
               JOptionPane.showMessageDialog(frame,"You can't build there. Please try another location");
                  
            break;
         
         case ROBBER_STATE:
            coords = mapToTile(x,y);
            //if a valid location was selected, perform all robber-associated actions
            if(coords[0]>=0 && coords[1]>=0){
               //first, move the robber to its new location
               board.moveRobber(coords[0],coords[1]);
               //put gui back in default state to prevent extra clicks
               setState(DEFAULT_STATE);
               System.out.println("doRobber");
               ArrayList<HumanPlayer> list = new ArrayList<HumanPlayer>();
               //find all players with towns/cities at the robber's new tile
               for(HumanPlayer p:players)
                  for (TownNode town : p.getTowns()) 
                     for (Tile tile : town.getAdjacentTiles()) 
                        if (tile!=null&&tile.equals(board.getTileAt(coords[0],coords[1]))&& !p.equals(current_player)) 
                           list.add(p);
               if(list.isEmpty())
                  return;
               Object[] options = list.toArray();
               //the player who moved the robber must steal from one of these players
               HumanPlayer stealing_from;
               do{
                  stealing_from = (HumanPlayer)JOptionPane.showInputDialog(frame,
                     "From which player will you steal? ","Stealing a resource",
                     JOptionPane.QUESTION_MESSAGE,null,options,options[0]);  
               }while (stealing_from==null);
               //make sure the victim has something to actually take
               ArrayList<Integer> cards = stealing_from.getResourceCards();
               if(cards.isEmpty())
                  return;
               //select a random resource and transfer it
               int stolen = cards.get((int)(Math.random()*cards.size()));
               int[] taking = {stolen};
               int[] giving = {};
               current_player.trade(giving,taking);
               stealing_from.trade(taking,giving);
               JOptionPane.showMessageDialog(frame,"You stole " + GameMenuBar.translate(stolen));
            }  
            update(getGraphics());
            break;
          
         case STARTING_TOWN_STATE:
            System.out.println("Entered starting town case");
            coords = mapToTown(x,y);
            
            if(coords[0]<0 || coords[1]<0){
               System.out.println("No town node clicked");
               return;
            }
            TownNode town = towns[coords[0]][coords[1]];
            if(town.startBuildable(current_player)){
               current_player.build_town(town);
               update(getGraphics());
               setState(STARTING_ROAD_STATE);
            
               if(stage>=players.size()){
                  int[] temp = new int[1];
                  System.out.println("Ready to give resources");
                  System.out.println(town.getAdjacentTiles()[0]);
                  Tile[] tiles = town.getAdjacentTiles();
                  for(int k=0;k<tiles.length;k++){
                     temp[0]=tiles[k].resource;
                     if(temp[0]<=Tile.GRAIN)
                        current_player.trade(new int[0],temp);
                  }     
               }
            }  
            break;  
            
         case STARTING_ROAD_STATE:
            System.out.println("Entered starting road case");
            
            for (int r = 0; r < roads.length; r++) {
               for (int c = 0; c < roads[r].length; c++) {
                  RoadNode road = roads[r][c];
                  if (road_hitboxes[r][c].contains(x, y)) {
                     if(road.startBuildable(current_player)){
                        current_player.build_road(road);
                        setState(DEFAULT_STATE);
                        update(getGraphics());
                        stage++;
                        if(stage<players.size())
                           turn++;
                        else if(stage>players.size())
                           turn--;
                        initBuild();
                     }     
                     
                     update(getGraphics());
                  }
               }
            }
            break;
         case FREE_ROADS_STATE:
            System.out.println("Entered two free roads case");
            for (int r = 0; r < roads.length; r++) {
               for (int c = 0; c < roads[r].length; c++) {
                  if (road_hitboxes[r][c].contains(x, y)) {
                     System.out.println("Road clicked");
                     if(roads[r][c].isBuildable(current_player)){
                        current_player.build_road(roads[r][c]);
                        update(getGraphics());
                        if(stage==1)
                           setState(DEFAULT_STATE);
                        else{
                           stage++;
                           JOptionPane.showMessageDialog(frame,"Please choose a location for your second free road");
                        }
                     }
                     else
                        JOptionPane.showMessageDialog(frame,"You can't build there. Please try another location");
                  }
               }
            }   
      }
   } 
   
   public void initBuild(){
      if(stage==players.size()*2){
         turn++;
         menu_bar.nextTurn();
         return;
      }
      current_player = players.get(turn%players.size());
      update(getGraphics());
      JOptionPane.showMessageDialog(frame,current_player+": Please build a town and then a road");
      setState(STARTING_TOWN_STATE);
         
   }
   public void twoFreeRoads(){
      setState(FREE_ROADS_STATE);
      stage=0;
   }
   public void setState(int s){
      state=s;
      System.out.println("State has been set to "  +state);
   }
   
   public void setCurrentPlayer(HumanPlayer p){
      current_player=p;
      turn=players.indexOf(p);
   }
   //The following are required by the interface, but are not used by this particular class
   public void mouseReleased(MouseEvent e){
   }
   public void mousePressed(MouseEvent e){
   }
   public void mouseEntered(MouseEvent e) {
   }
   public void mouseExited(MouseEvent e) {
   
   }    
   
   private int[] mapToTile(int x,int y){
      int[] coords = {-1,-1};
            
      if(y>65 && y<100)
         coords[0]=0;
      else if(y>115 && y<145)
         coords[0]=1;
      else if(y>165 && y<195)
         coords[0]=2;
      else if(y>210 && y<245)
         coords[0]=3;
      else if(y>260 && y<295)
         coords[0]=4;
               
               
      if(x<155){
         return coords;
      }
      else if(x<185){
         if(coords[0]==2)
            coords[1]=0;
      }  
      else if(x<215){
         if(coords[0]==1||coords[0]==3)
            coords[1]=0;
      }
      else if(x<245){
         if(coords[0]==0||coords[0]==4)
            coords[1]=0;
         else if(coords[0]==2)
            coords[1]=1;
      }
      else if(x<270){
         if(coords[0]==1||coords[0]==3)
            coords[1]=1;
      }
      else if(x<300){
         if(coords[0]==0||coords[0]==4)
            coords[1]=1;
         else if(coords[0]==2)
            coords[1]=2;           
      }
      else if(x<330){
         if(coords[0]==1||coords[0]==3)
            coords[1]=2;
      }
      else if(x<360){
         if(coords[0]==0||coords[0]==4)
            coords[1]=2;
         else if(coords[0]==2)
            coords[1]=3;
      }
      else if(x<390){
         if(coords[0]==1||coords[0]==3)
            coords[1]=3;
      }
      else if(x<420){
         if(coords[0]==2)
            coords[1]=4;
      }
      return coords;
   }
   
   private int[] mapToTown(int x,int y){
      int[] coords = {-1,-1};
      if(y>=45 && y<=75)
         coords[0]=0;
      else if(y>=95 && y<=125)
         coords[0]=1;
      else if(y>=145 && y<=175)
         coords[0]=2;
      else if(y>=190 && y<=220)
         coords[0]=3;
      else if(y>=240 && y<=270)
         coords[0]=4;
      else if(y>=285 && y<=315)
         coords[0]=5;
         
      if(coords[0]<0)
         return coords;
         
      if(x>=135 && x<=155){
         if(coords[0]==2 || coords[0]==3)
            coords[1]=0;
      }
      else if(x>=165 && x<=180){
         if(coords[0]==1 || coords[0]==4)
            coords[1]=0;
         else if(coords[0]==2 || coords[0]==3)  
            coords[1]=1;
      }
      else if(x>=190 && x<=210){
         coords[1]=(int)(2.5-Math.abs(coords[0]-2.5));
      }
      else if(x>=220 && x<=235){
         coords[1]=(int)(3.5-Math.abs(coords[0]-2.5));
      }
      else if(x>=245 && x<=265){
         coords[1]=(int)(4.5-Math.abs(coords[0]-2.5));
      }
      else if(x>=275 && x<=295){
         coords[1]=(int)(5.5-Math.abs(coords[0]-2.5));
      }
      else if(x>=305 && x<=325){
         coords[1]=(int)(6.5-Math.abs(coords[0]-2.5));
      }
      else if(x>=335 && x<=350){
         coords[1]=(int)(7.5-Math.abs(coords[0]-2.5));
      }
      else if(x>=360 && x<=380){
         coords[1]=(int)(8.5-Math.abs(coords[0]-2.5));
      }
      else if(x>=390 && x<=405){
         if(coords[0]==1 || coords[0]==4)
            coords[1]=8;
         else if(coords[0]==2 || coords[0]==3)
            coords[1]=9;
      }
      else if(x>=415 && x<=430){
         if(coords[0]==2 || coords[0]==3)
            coords[1]=10;
      }
   
      return coords;
   }
   
   public int getState(){
      return state;
   }
   
   public void update(Graphics g){
      for(int k=0;k<3;k++)
         super.update(g);
   }
}
