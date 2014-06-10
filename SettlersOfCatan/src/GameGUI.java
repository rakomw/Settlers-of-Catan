import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class GameGUI extends Canvas implements MouseListener{
   //tiles holds the BufferedImage representations of each type of tile. Index corresponds to the constants defined in Tile
   private BufferedImage[] tiles;
   //dice holds the BufferedImage representations of the numbers 2-12. Indices 0 and 1 purposefully null
   private BufferedImage[] dice;
   //2D array of the actual tiles in the board
   private BufferedImage[][] hexes = {new BufferedImage[3], new BufferedImage[4], new BufferedImage[5],new BufferedImage[4],new BufferedImage[3]};
   //2D array to hold the actual roll values of the hexes on the board
   private BufferedImage[][] rolls = {new BufferedImage[3], new BufferedImage[4], new BufferedImage[5],new BufferedImage[4],new BufferedImage[3]};
   
   private Board board;
   //spacing for tiles: 0 for both is edges touching, 2 and 4 is preferred to make road and town areas clearer
   private final int HORIZONTAL_GAP=2,VERTICAL_GAP=4;
   
   
   public GameGUI(){
      board = new Board();
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
         
         for(int k=2;k<=12;k++)
            dice[k] = ImageIO.read(new File("resources/dice"+k+".gif"));
      }
      catch(IOException e){
      }
      
      
      
      for (int r=0; r < hexes.length; r++){ 
         for (int c=0; c < hexes[r].length; c++){ 
            hexes[r][c] = tiles[board.getTileAt(r,c).resource];
            rolls[r][c] = dice[board.getTileAt(r,c).roll];
         }
      }
   }
   
   public void paint(Graphics g){
      //TODO draw water tiles,harbors,roads,towns,and robber
      //draws all of the land hexes and their rolls
      for (int r=0; r < hexes.length; r++){ 
         for (int c=0; c < hexes[r].length; c++){
            int x = (c*(55+HORIZONTAL_GAP))+(200-((2-(Math.abs(r-2)))*(55+HORIZONTAL_GAP)/2));
            int y = (r*(45+VERTICAL_GAP))+50;
            g.drawImage(hexes[r][c],x,y,this);
            g.drawImage(rolls[r][c],x+15,y+20,this);
         } 
      }
   }
   
   public void mouseEntered(MouseEvent e) {
       //won't do anything, but required by the interface
   }

   public void mouseExited(MouseEvent e) {
       //won't do anything, but required by the interface
   }

   public void mouseClicked(MouseEvent e) {
      //this was for testing purposes
      System.out.println(e.getX()+" "+e.getY());

      /*I'm working on this right now- basic idea is 1: have a switch to determine if we're building roads, 
      building settlements, upgrading settlements, or moving the robber. 2: map the coordinates of the mouse click
      to the appropriate node or tile(give ~10px hitbox) 3: call functions from other classes as needed 4: set mode
      back to inactive */
   } 
   public void mouseReleased(MouseEvent e){
      //won't do anything, but required by the interface
   }
   public void mousePressed(MouseEvent e){
      //won't do anything, but required by the interface
   }
    
}
