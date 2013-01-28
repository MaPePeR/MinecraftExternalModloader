package mapeper.minecraft.modloader.config.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mapeper.minecraft.modloader.config.DefaultConfiguration;

public class ClassAndPlayernameConfigGUI extends JPanel implements ActionListener {
	
	private static final String skipLauncherClassname="net.minecraft.client.Minecraft";
	private static final String launcherClassname=DefaultConfiguration.getInstance().getClassname();
	JLabel label = new JLabel("Playername:");
	JCheckBox checkBox = new JCheckBox("Skip Launcher", false);
	JTextField textField = new JTextField();
	DirtyState dirty;
	public ClassAndPlayernameConfigGUI(DirtyState dirty)
	{
		this.dirty=dirty;
		this.setBorder(BorderFactory.createTitledBorder(""));
		this.setLayout(new GridBagLayout());
		checkBox.addActionListener(this);
		textField.getDocument().addDocumentListener(dirty);
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth=2;
		c.weightx=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		this.add(checkBox,c);
		
		c.gridwidth=1;
		c.gridy=1;
		c.gridx=1;
		this.add(textField,c);
		c.fill=GridBagConstraints.NONE;
		c.weightx=0;
		c.gridx=0;
		
		label.setEnabled(false);
		this.add(label,c);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean b = checkBox.isSelected();
		label.setEnabled(b);
		textField.setEnabled(b);
	}
	public void setNameAndClass(String classname, String playername)
	{
		//Skip Launcher
		if(skipLauncherClassname.equals(classname))
		{
			checkBox.setEnabled(true);
			checkBox.setSelected(true);
			textField.setEnabled(true);
			label.setEnabled(true);
			textField.setText(playername);
		}
		//Dont skip launcher
		else if(launcherClassname.equals(classname))
		{
			checkBox.setEnabled(true);
			checkBox.setSelected(false);
			textField.setEnabled(false);
			label.setEnabled(false);
			textField.setText("");
		}
		//Other Class
		else
		{
			textField.setText(classname);
			checkBox.setEnabled(false);
			label.setEnabled(false);
			textField.setEnabled(false);
		}
		dirty.setDirty();
	}
	public String getClassname()
	{
		//Normal Configuration
		if(checkBox.isEnabled())
		{
			return checkBox.isSelected()?skipLauncherClassname:launcherClassname;
		}
		else
		{
			return textField.getText();
		}
	}
	public String getPlayername()
	{
		if(checkBox.isEnabled()&&checkBox.isSelected())
			return textField.getText();
		else 
			return "";
	}
}
