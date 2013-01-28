package mapeper.minecraft.modloader.config.gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ModConfigGUI extends JPanel {
	DirtyState dirty;
	public ModConfigGUI(DirtyState dirty)
	{
		this.dirty=dirty;
		this.setBorder(BorderFactory.createTitledBorder("Mod Locations"));
	}
}
