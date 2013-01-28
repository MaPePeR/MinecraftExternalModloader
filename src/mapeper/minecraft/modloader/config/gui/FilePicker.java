package mapeper.minecraft.modloader.config.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FilePicker extends JPanel implements ActionListener, DocumentListener {
	private static final long serialVersionUID = -3601039687471868003L;
	Timer refreshTimer = new Timer(750, this);
	JTextField textField = new JTextField();
	JButton button = new JButton("Select...");
	JFileChooser fileChooser = new JFileChooser();
	DirtyState dirty;
	public FilePicker(DirtyState dirty, boolean directories)
	{
		this.dirty=dirty;
		refreshTimer.setRepeats(false);
		if(directories)
		{
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		fileChooser.setFileHidingEnabled(false);
		textField.setInputVerifier(new FileVerifier(directories));
		textField.getDocument().addDocumentListener(this);
		//fileChooser.addActionListener(this);
		button.addActionListener(this);
		
		this.setLayout(new BorderLayout());
		this.add(textField,BorderLayout.CENTER);
		this.add(button, BorderLayout.EAST);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button)
		{
			if(new File(textField.getText()).exists())
			{
				fileChooser.setCurrentDirectory(new File(textField.getText()));
			}
			int selection = fileChooser.showDialog(this, "Select...");
			if(selection==JFileChooser.APPROVE_OPTION)
			{
				textField.setText(fileChooser.getSelectedFile().toString());
				textField.getInputVerifier().verify(textField);
			}
		}
		else if(e.getSource()==refreshTimer)
		{
			textField.getInputVerifier().verify(textField);

		}
	}
	private class FileVerifier extends InputVerifier
	{

		final boolean onlyDirectories;
		public FileVerifier(boolean onlyDirectories)
		{
			this.onlyDirectories=onlyDirectories;
		}
		@Override
		public boolean verify(JComponent input) {
			try {
				JTextField field = (JTextField) input;
				File f = new File(field.getText());
				if((onlyDirectories&&f.isDirectory())||(!onlyDirectories&&f.isFile()))
				{
					field.setForeground(Color.BLACK);
					field.setBackground(Color.WHITE);
					return true;
				}
				else
				{
					field.setBackground(Color.RED);
					field.setForeground(Color.white);
					return true; 
				}
				
			}
			catch(Exception e)
			{
				return false;
			}
			
		}
	}
	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		textField.setEnabled(b);
		button.setEnabled(b);
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		changedUpdate(e);
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		changedUpdate(e);
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		dirty.setDirty();
		refreshTimer.stop();
		refreshTimer.start();
	}

	public void setFile(String file)
	{
		textField.setText(file);
		textField.getInputVerifier().verify(textField);
	}
	public String getFile() {
		return textField.getText();
	}
	
}
