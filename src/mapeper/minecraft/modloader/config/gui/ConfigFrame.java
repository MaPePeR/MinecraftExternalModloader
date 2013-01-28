package mapeper.minecraft.modloader.config.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import mapeper.minecraft.modloader.config.AbstractConfiguration;
import mapeper.minecraft.modloader.config.Configuration;
import mapeper.minecraft.modloader.config.DefaultConfiguration;

public class ConfigFrame extends JFrame implements ActionListener {
	public static void main(String[] s)	{new ConfigFrame();}
	public static final String menuTextNew="New";
	public static final String menuTextOpen="Open";
	public static final String menuTextSave="Save";
	public static final String menuTextSaveAs="Save as...";
	DirtyState dirty = new DirtyState();
	File currentFile=null;
	JFileChooser fileChooser=new JFileChooser();
	
	MemoryConfigGUI memChooser = new MemoryConfigGUI(dirty);
	ClassAndPlayernameConfigGUI nameAndClassChooser = new ClassAndPlayernameConfigGUI(dirty);
	JavaAndFolderConfigGUI filesAndFolderChooser = new JavaAndFolderConfigGUI(dirty);
	ModConfigGUI mods = new ModConfigGUI(dirty);
	public ConfigFrame()
	{
		super("Minecraft External Modloader Configuration");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new ConfigFrameWindowListener());
		this.setLayout(new BorderLayout());
		
		//MenuBar
		JMenuBar mb = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem menuNew=new JMenuItem(menuTextNew);
		JMenuItem menuOpen=new JMenuItem(menuTextOpen);
		JMenuItem menuSave=new JMenuItem(menuTextSave);
		JMenuItem menuSaveAs=new JMenuItem(menuTextSaveAs);
		
		menuNew.setAccelerator(KeyStroke.getKeyStroke("control N"));
		menuOpen.setAccelerator(KeyStroke.getKeyStroke("control O"));
		menuSave.setAccelerator(KeyStroke.getKeyStroke("control S"));
		menuSaveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
		
		menuNew.addActionListener(this);
		menuOpen.addActionListener(this);
		menuSave.addActionListener(this);
		menuSaveAs.addActionListener(this);

		menu.add(menuNew);
		menu.add(menuOpen);
		menu.add(menuSave);
		menu.add(menuSaveAs);
		mb.add(menu);
		this.setJMenuBar(mb);
		//MenuBar finish
		
		JPanel left = new JPanel();
		left.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=1;
		left.add(filesAndFolderChooser,c);
		c.gridy=1;
		left.add(memChooser,c);
		c.gridy=2;

		left.add(nameAndClassChooser,c);
		c.gridy=3;
		c.fill=GridBagConstraints.BOTH;
		c.weighty=1;
		left.add(new JLabel(),c);
		
		openConfiguration(DefaultConfiguration.getInstance());
		this.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left,mods),BorderLayout.CENTER);
		dirty.clear();
		//this.setSize(400,400);
		this.pack();
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()==menuTextNew)
		{
			if(closeConfiguration())
				openConfiguration(DefaultConfiguration.getInstance());
		}
		else if(e.getActionCommand()==menuTextOpen)
		{
			//Unsaved Changes
			if(closeConfiguration())
				openConfiguration();
		}
		else if(e.getActionCommand()==menuTextSave)
		{
			saveCurrentConfiguration();
		}
		else if(e.getActionCommand()==menuTextSaveAs)
		{
			File buf = currentFile;
			currentFile=null;
			if(!saveCurrentConfiguration())
				currentFile=buf;
		}
	}
	private boolean closeConfiguration()
	{
		if(dirty.isDirty())
		{
			int selection = JOptionPane.showConfirmDialog(this, "Do you want to save your current Configuration before closing?", "Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			switch(selection)
			{
			case JOptionPane.YES_OPTION: return saveCurrentConfiguration();
			case JOptionPane.NO_OPTION: return true;
			case JOptionPane.CANCEL_OPTION: return false;
			default: throw new RuntimeException("This will never happen...");
			}
		}
		return true;
	}
	private void openConfiguration() {
		int selection = fileChooser.showOpenDialog(this);
		if(selection==JFileChooser.APPROVE_OPTION)
			openConfigurationFromFile(fileChooser.getSelectedFile());
	}

	private void openConfigurationFromFile(File f)
	{
		try {
			AbstractConfiguration conf = Configuration.loadFromFile(f);
			openConfiguration(conf);
			currentFile=f;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,"Could not read File","Error!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void openConfiguration(AbstractConfiguration conf)
	{
		currentFile=null;
		nameAndClassChooser.setNameAndClass(conf.getClassname(), conf.getPlayerName());
		filesAndFolderChooser.setJavaExecutable(conf.getJavaExecutable());
		filesAndFolderChooser.setMinecraftFolder(conf.getMinecraftBaseFolder());
		memChooser.setMemory(conf.getMinMemory(), conf.getMaxMemory());	
		dirty.clear();

	}
	private boolean saveCurrentConfiguration()
	{
		//Configuration was not saved before
		if(currentFile==null)
		{
			int selection=fileChooser.showSaveDialog(this);
			if(selection==JFileChooser.APPROVE_OPTION)
			{
				currentFile=fileChooser.getSelectedFile();
			}
			else
				return false;
		}
		
		try
		{
			AbstractConfiguration conf = createConfiguration();
			conf.storeToFile(currentFile);
			dirty.clear();
			return true;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error saving Configuration", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		}
		
	}
	private AbstractConfiguration createConfiguration()
	{
		Configuration conf = new Configuration();
		conf.setClassname(nameAndClassChooser.getClassname());
		conf.setPlayerName(nameAndClassChooser.getPlayername());
		conf.setJavaExecutable(filesAndFolderChooser.getJavaExecutable());
		conf.setMinecraftBaseFolder(filesAndFolderChooser.getMinecraftFolder());
		conf.setMinMemory(memChooser.getMinMemory());
		conf.setMaxMemory(memChooser.getMaxMemory());
		return conf;
	}
	
	
	private class ConfigFrameWindowListener extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e) {
			if(closeConfiguration())
			{
				System.exit(0);
			}
		}
	}
}
