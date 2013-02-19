package mapeper.minecraft.modloader.config;

import java.io.File;

public class DefaultConfiguration extends AbstractConfiguration
{
	private static final DefaultConfiguration instance = new DefaultConfiguration();
	public static AbstractConfiguration getInstance()
	{
		return instance;
	}
	public DefaultConfiguration()
	{
		this.classname="net.minecraft.LauncherFrame";
		this.javaExecutable="java";
		this.minecraftJarLauncher="./minecraft.jar";
		this.minMemory=0;
		this.maxMemory=1024;
		this.playerName="";
		this.minecraftBaseFolder=getMinecraftWorkingDir().toURI().getPath();
		this.modURLs=new String[]{"file://"+this.minecraftBaseFolder+"mod/"};
		
	}
	private static File getMinecraftWorkingDir()
	{
			String osName = System.getProperty("os.name").toLowerCase();
		    String userHome = System.getProperty("user.home", ".");
		    File minecraftWorkingDir;
		    //Solaris & Linux
		    if(osName.contains("solaris")||osName.contains("sunos")||osName.contains("linux")||osName.contains("unix"))
		    {
		      minecraftWorkingDir = new File(userHome, ".minecraft/");
		    }
		    //Windows
		    else if(osName.contains("win"))
		    {
		      String appdata = System.getenv("APPDATA");
		      String appdataOrUserHome = appdata != null ? appdata : userHome;
		      minecraftWorkingDir = new File(appdataOrUserHome, ".minecraft/");
		    }
		    //MacOS
		    else if(osName.contains("mac"))
		    {
		      minecraftWorkingDir = new File(userHome, "Library/Application Support/minecraft");
		    }
		    else
		    {
		      minecraftWorkingDir = new File(userHome, "minecraft/");
		    }
		    if ((!minecraftWorkingDir.exists()) && (!minecraftWorkingDir.mkdirs()))
		      throw new RuntimeException("The working directory could not be created: " + minecraftWorkingDir);
		    return minecraftWorkingDir;
	}
}