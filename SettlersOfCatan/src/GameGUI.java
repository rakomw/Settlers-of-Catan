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
   //robber
   private BufferedImage robber;
   
   //2D array of the actual tiles in the board
   private BufferedImage[][] hexes = {new BufferedImage[3], new BufferedImage[4], new BufferedImage[5],new BufferedImage[4],new BufferedImage[3]};
   //2D array to hold the actual roll values of the hexes on the board
   private BufferedImage[][] rolls = {new BufferedImage[3], new BufferedImage[4], new BufferedImage[5],new BufferedImage[4],new BufferedImage[3]};
   
   private TownNode[][] towns;
   private RoadNode[][] roads;
   private Board board;
   private GameMenuBar menu_bar;
   //spacing for tiles: 0 for both is edges touching, 2 and 4 is preferred to make road and town areas clearer
   private final int HORIZONTAL_GAP=2,VERTICAL_GAP=4;
   public static int DEFAULT_STATE = 0, ROAD_STATE = 1, TOWN_STATE = 2, CITY_STATE = 3, ROBBER_STATE = 4;
   private int state;
   
   
   public void setMenuBar(GameMenuBar bar){
      menu_bar=bar;
   }
   public GameGUI(){
      frame = GameController.frame;
      board = Board.getInstance();
      towns = board.getTowns();
      roads = board.getRoads();
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
   
   public void paint(Graphics g){
      //TODO draw water tiles(maybe),roads,towns
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
      g.drawImage(northwestPort,172,3,this);
      g.drawImage(northeastPort,369,51,this);
      g.drawImage(eastPort,426,148,this);
      g.drawImage(southwestPort,173,294,this);
      
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
               y = (r*50)+30+(15*(c%2));
            }
            else{
               y = (r*50)+25+(15*(c%2));
            }
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
            /*For Testing
            System.out.println("Row: "+r+"\tCol: "+c+"\tX: "+x+"\tY: "+y);
            g.drawImage(settlementRed,x,y,this);*/
            
         }
      }
      
   }

   public void mouseClicked(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      System.out.println("Click at "+x+","+y);
      System.out.println("State:"+state);
      switch(state){
         case 0:
            if(e.isMetaDown()){
               int[] coords = mapToTile(x,y);
               if(coords[0]>=0 && coords[1]>=0)
                  JOptionPane.showMessageDialog(frame,board.getTileAt(coords[0],coords[1]));
            }
            break;
            
         case 1:
            //TODO
            break;
            
         case 2:
            //TODO
            break;
            
         case 3:
            //TODO
            break;
         
         case 4:
            System.out.println("Entered robber case");
            System.out.println("Coords are " + x + " " + y);
            int[] coords = mapToTile(x,y);
            if(coords[0]>=0 && coords[1]>=0){
               System.out.println("coords made");
               board.moveRobber(coords[0],coords[1]);
               System.out.println("board move done");
               menu_bar.doRobber(coords[0],coords[1]);
               System.out.println("doneRobber");
            }
            else
               System.out.println("Invalid click");
            update(getGraphics());
            break;
      }
   } 
     
   public void setState(int s){
      state=s;
      System.out.println("State has been set to "  +state);
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
               
               
      if(x>155 && x<185){
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
}
