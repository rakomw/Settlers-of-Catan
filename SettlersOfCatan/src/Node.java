import java.awt.*;
public abstract class Node {
	protected HumanPlayer owner;
	protected int level;
	
	public abstract boolean isBuildable(HumanPlayer prospector);
	public abstract void buildUp(HumanPlayer prospector);
	

	public int getBuildLevel() {
		return level;
	}
	
	public HumanPlayer getOwner() {
		return owner;
	}
   
   public Color getColor(){
      if(owner==null)
         return null;
      return owner.getColor();
   }
}
