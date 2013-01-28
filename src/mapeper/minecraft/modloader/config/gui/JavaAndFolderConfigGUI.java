package mapeper.minecraft.modloader.config.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import mapeper.minecraft.modloader.config.DefaultConfiguration;

public class JavaAndFolderConfigGUI extends JPanel implements ActionListener{
	DirtyState dirty;
	
	JCheckBox  javaBinaryCheckBox=new JCheckBox("Java-Binary",true);
	FilePicker javaBinaryFilePicker;
	JCheckBox  minecraftFolderCheckBox=new JCheckBox("Minecraft-Folder",true);
	FilePicker minecraftFolderFilePicker;
	public JavaAndFolderConfigGUI(DirtyState dirty)
	{
		this.dirty=dirty;
		javaBinaryFilePicker=new FilePicker(dirty,false);
		minecraftFolderFilePicker=new FilePicker(dirty,true);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder("Files and Folders"));
		
		javaBinaryCheckBox.addActionListener(this);
		minecraftFolderCheckBox.addActionListener(this);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=0;
		c.gridx=0;
		this.add(javaBinaryCheckBox,c);
		c.gridy=1;
		this.add(minecraftFolderCheckBox,c);
		c.gridx=1;
		c.gridy=0;
		c.weightx=1;
		this.add(javaBinaryFilePicker,c);
		c.gridy=1;
		this.add(minecraftFolderFilePicker,c);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==javaBinaryCheckBox){
			javaBinaryFilePicker.setEnabled(javaBinaryCheckBox.isSelected());
			if(!javaBinaryCheckBox.isSelected())
			{
				javaBinaryFilePicker.setFile(DefaultConfiguration.getInstance().getJavaExecutable());
			}
		}
		else if(e.getSource()==minecraftFolderCheckBox)
		{
			minecraftFolderFilePicker.setEnabled(minecraftFolderCheckBox.isSelected());
			if(!minecraftFolderCheckBox.isSelected())
			{
				minecraftFolderFilePicker.setFile(DefaultConfiguration.getInstance().getMinecraftBaseFolder());
			}
				
		}
		
	}
	public String getJavaExecutable()
	{
		return javaBinaryFilePicker.getFile();
	}
	public String getMinecraftFolder()
	{
		return minecraftFolderFilePicker.getFile();
	}
	public void setJavaExecutable(String s)
	{
		javaBinaryCheckBox.setSelected(!s.equals(DefaultConfiguration.getInstance().getJavaExecutable()));
		javaBinaryFilePicker.setEnabled(!s.equals(DefaultConfiguration.getInstance().getJavaExecutable()));

		javaBinaryFilePicker.setFile(s);
	}
	public void setMinecraftFolder(String s)
	{
		minecraftFolderCheckBox.setSelected(!s.equals(DefaultConfiguration.getInstance().getMinecraftBaseFolder()));
		minecraftFolderFilePicker.setEnabled(!s.equals(DefaultConfiguration.getInstance().getMinecraftBaseFolder()));
		minecraftFolderFilePicker.setFile(s);
	}
}
