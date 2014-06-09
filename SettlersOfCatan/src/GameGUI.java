import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class GameGUI extends Canvas implements MouseListener{
   private BufferedImage[] tiles;
   private Board board;
   private BufferedImage[][] hexes = {new BufferedImage[3], new BufferedImage[4], new BufferedImage[5],new BufferedImage[4],new BufferedImage[3]};
   public GameGUI(){
      board = new Board();
               
            

      tiles = new BufferedImage[6];
      
      try{
      tiles[Tile.LUMBER] = ImageIO.read(new File(("resources/lumber.gif")));
      tiles[Tile.ORE] = ImageIO.read(new File(("resources/ore.gif")));
      tiles[Tile.BRICK] = ImageIO.read(new File(("resources/brick.gif")));
      tiles[Tile.WOOL] = ImageIO.read(new File(("resources/wool.gif")));
      tiles[Tile.GRAIN] = ImageIO.read(new File(("resources/grain.gif")));
      tiles[Tile.DESERT] = ImageIO.read(new File(("resources/desert.gif")));
      }
      catch(IOException e){
      }
      
      
      
      /*for (int r=0; r < hexes.length; r++){ 
         for (int c=0; c < hexes[r].length; c++){ 
            //labels[r][c] = new JLabel(tiles[board.getTileAt(r,c).resource]);
            hexes[r][c] = tiles[(int)(Math.random()*6)];
         }
      }*/
   }
    public void mouseEntered(MouseEvent e) {
       
    }

    public void mouseExited(MouseEvent e) {
       
    }

    public void mouseClicked(MouseEvent e) {
    
    }
    public void mouseReleased(MouseEvent e){
    
    }
    public void mousePressed(MouseEvent e){
    
    }
    
}
