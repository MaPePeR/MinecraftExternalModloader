package mapeper.minecraft.modloader.config.gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DirtyState implements DocumentListener, ChangeListener{
	private boolean isDirty=false;
	public void setDirty()
	{
		System.out.println("setDirty");
		isDirty=true;
	}
	public void clear()
	{
		System.out.println("clean");
		isDirty=false;
	}
	public boolean isDirty()
	{
		return isDirty;
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		setDirty();
		
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		setDirty();
		
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		setDirty();
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		setDirty();
	}
}
