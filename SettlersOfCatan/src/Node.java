import java.lang.Exception;

public abstract class Node {
	protected HumanPlayer owner;
	protected int level;
	
	public abstract boolean isBuildable(HumanPlayer prospector);
	public abstract void buildUp(HumanPlayer prospector) throws Exception;
	

	public int getBuildLevel() {
		return level;
	}
	
	public HumanPlayer getOwner() {
		return owner;
	}
}
