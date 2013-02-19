package mapeper.minecraft.modloader.config.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ModConfigGUI extends JPanel implements ActionListener {
	DirtyState dirty;
	
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	JList<String> modList=new JList<String>(listModel);
	JScrollPane scrollPane= new JScrollPane(modList);
	JFileChooser fileChooser = new JFileChooser();
	
	JButton deleteButton=new JButton("DELETE");
	JButton moveUpButton=new JButton(" UP ");
	JButton moveDownButton=new JButton("DOWN");
	JButton addURLButton=new JButton("Add URL");
	JButton addFileButton=new JButton("Add File");
	public ModConfigGUI(DirtyState dirty)
	{
		this.dirty=dirty;
		this.setLayout(new GridBagLayout());
		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setFileHidingEnabled(false);
		
		modList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		deleteButton.addActionListener(this);
		moveUpButton.addActionListener(this);
		moveDownButton.addActionListener(this);
		addURLButton.addActionListener(this);
		addFileButton.addActionListener(this);
		
		
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridy=1;
		c.gridx=GridBagConstraints.RELATIVE;
		this.add(deleteButton,c);
		this.add(moveDownButton,c);
		this.add(moveUpButton,c);
		this.add(addURLButton,c);
		this.add(addFileButton,c);
		
		c.fill=GridBagConstraints.BOTH;
		c.gridx=0; c.gridy=0;
		c.weightx=1;c.weighty=1;
		c.gridwidth=5;
		this.add(scrollPane, c);

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object s =e.getSource();
		if(s==deleteButton)
		{
			int min=modList.getMinSelectionIndex();
			for(int i=modList.getMaxSelectionIndex();i>=min;i--)
			{
				listModel.remove(i);
			}
		}
		else if(s==moveUpButton)
		{
			if(modList.getMinSelectionIndex()>0)
			{
				int maxSelection=modList.getMaxSelectionIndex();
				int minSelection=modList.getMinSelectionIndex();
				String toMove = listModel.remove(modList.getMinSelectionIndex()-1);
				listModel.add(modList.getMaxSelectionIndex()+1, toMove);
				modList.setSelectionInterval(minSelection-1, maxSelection-1);
			}
		}
		else if(s==moveDownButton)
		{
			if(modList.getMaxSelectionIndex()+1<listModel.getSize())
			{
				int maxSelection=modList.getMaxSelectionIndex();
				int minSelection=modList.getMinSelectionIndex();
				String toMove = listModel.remove(maxSelection+1);
				listModel.add(minSelection, toMove);
				modList.setSelectionInterval(minSelection+1, maxSelection+1);
			}
		}
		else if(s==addFileButton)
		{
			int selection = fileChooser.showDialog(this, "Select");
			if(selection==JFileChooser.APPROVE_OPTION)
			{
				try {
					listModel.addElement(fileChooser.getSelectedFile().toURI().toURL().toString());
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		}
		else if(s==addURLButton)
		{
			String url = JOptionPane.showInputDialog(this, "Type URL", "");
			if(url!=null)
			{
				listModel.addElement(url);
			}
		}
		else
			return;
		dirty.setDirty();
	}
	public void setModURLs(String[] strings)
	{
		listModel.clear();
		for(String element:strings)
			listModel.addElement(element);
	}
	public String[] getModURLS()
	{
		String[] urls = new String[listModel.size()];
		listModel.copyInto(urls);
		return urls;
	}
}
