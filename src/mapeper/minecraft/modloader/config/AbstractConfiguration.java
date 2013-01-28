package mapeper.minecraft.modloader.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class AbstractConfiguration {

	protected String javaExecutable;
	protected String minecraftBaseFolder;
	protected int minMemory;
	protected int maxMemory;
	protected String classname;
	protected String playerName;

	public static Configuration loadFromFile(File f)
			throws FileNotFoundException, IOException {
				FileInputStream fin=null;
				try
				{
					fin = new FileInputStream(f);
					Configuration conf = new Configuration();
					Properties prop = new Properties();
					prop.load(fin);
					
					conf.fromProperties(prop);
					return conf;
				}
				finally
				{
					if(fin!=null)
						fin.close();
				}
			}

	public String getJavaExecutable() {
		return javaExecutable;
	}

	public String getMinecraftBaseFolder() {
		return minecraftBaseFolder;
	}

	public int getMinMemory() {
		return minMemory;
	}

	public int getMaxMemory() {
		return maxMemory;
	}

	public String getClassname() {
		return classname;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void fromProperties(Properties prop) {
		classname=prop.getProperty("mem.classname");
		try	{
			minMemory=Integer.parseInt(prop.getProperty("mem.minMemory"));
		}
		catch(NumberFormatException e){System.err.println("Could not read minMemory!");minMemory=0;}
		try {
			maxMemory=Integer.parseInt(prop.getProperty("mem.maxMemory"));
		}catch(NumberFormatException e){System.err.println("Could not read maxMemory!");maxMemory=0;}
		javaExecutable=prop.getProperty("mem.java");
		minecraftBaseFolder=prop.getProperty("mem.baseFolder");
		playerName=prop.getProperty("mem.playername");
		checkForNulls();
	}

	private void checkForNulls() {
		if(classname==null||javaExecutable==null||minecraftBaseFolder==null)
		{
			throw new RuntimeException("Could not read File");
		}
	}

	public AbstractConfiguration() {
		super();
	}

	public Properties toProperties() {
		Properties prop =new Properties();
		prop.setProperty("mem.classname", classname);
		prop.setProperty("mem.minMemory", ""+minMemory);
		prop.setProperty("mem.maxMemory", ""+maxMemory);
		prop.setProperty("mem.java", ""+javaExecutable);
		prop.setProperty("mem.baseFolder", minecraftBaseFolder);
		prop.setProperty("mem.playername", playerName);
		return prop;
	}

	public void storeToFile(File f) throws FileNotFoundException,
			IOException {
				FileOutputStream fout=null;
				try
				{
					Properties prop = this.toProperties();
					fout = new FileOutputStream(f);
					prop.store(fout, "Configuration for Minecraft External Modloader by MaPePeR\nhttp://www.minecraftforum.net/topic/1639674-");
				}
				finally
				{
					if(fout!=null)
						fout.close();
				}
			}

}