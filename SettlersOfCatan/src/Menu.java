import java.util.*;
import javax.swing.*;

public abstract class Menu {
	protected boolean visible;
	protected ArrayList<JButton> buttons = new ArrayList<JButton>();
	
	private int good_kudos; // just got an A++, look at that amazing form
	
	public Menu() {
		good_kudos = 100;
	}
	
	public void setVisible(boolean state) {
		visible = state;
	}
	public boolean isVisible() {
		return visible;
	}
}