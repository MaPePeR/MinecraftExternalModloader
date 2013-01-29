package mapeper.minecraft.modloader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import mapeper.minecraft.modloader.config.AbstractConfiguration;

public class GenerateStartCommand {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//Get the Util Class that is used to determine the place of the minecraft Folder
		ClassLoader loader = GenerateStartCommand.class.getClassLoader();
		Class<?> utilClass;
		try {
			utilClass=loader.loadClass("net.minecraft.Util");
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Minecraft Launcher not Found in Classpath!", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			return;
		}
		
		//File workingDir = Util.getWorkingDirectory();
		File workingDir = (File)utilClass.getMethod("getWorkingDirectory").invoke(null);
		//This should contain the Path to the minecraft installation on all platforms
		String workDir = workingDir.toURI().getPath();
		
		
		//Generating the Classpath
        StringBuilder classpath = new StringBuilder();
       
        String[] cpath = {
        		//Path to myself:
        		//LoaderLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(),
        		//The classpath that i used to find the minecraft launcher
        		//Does contain myself :D
        		System.getProperty("java.class.path"),
        		//Minecraft-Standard-Libs
        		workDir+"bin/jinput.jar",
        		workDir+"bin/lwjgl.jar",
        		workDir+"bin/lwjgl_util.jar",
        		workDir+"bin/minecraft.jar",
        		};
        String pathSep=System.getProperty("path.separator");
        for(String s: cpath)
        {
        	classpath.append(s);
        	classpath.append(pathSep);
        }
        
        
        ArrayList<String> localArrayList = new ArrayList<String>();
       	localArrayList.add("java");
        //Memory Parameter
        localArrayList.add("-Xmx1024m");

        localArrayList.add("-Djava.library.path=\""+workDir+"bin/natives\"");
        //Parameter the normal Minecraft Launcher uses:
        localArrayList.add("-Dsun.java2d.noddraw=true");
        localArrayList.add("-Dsun.java2d.d3d=false");
        localArrayList.add("-Dsun.java2d.opengl=false");
        localArrayList.add("-Dsun.java2d.pmoffscreen=false");
        //Classpath:
        localArrayList.add("-classpath");
        localArrayList.add(classpath.toString());
        //Start another Class which then injects Classes from its command-line-argument-urls
        localArrayList.add(StartMinecraft.class.getName());
        //The Folders to search for replacing classes:
        localArrayList.add("file:"+workDir+"mod/");

        if(args.length>0 && args[0].equals("-dummy"))
        {
        	for(String s:localArrayList)
        	{
        		System.out.print(s);
        		System.out.print(" ");
        	}
        	System.out.println();
        }
        else
        {
	        ProcessBuilder localProcessBuilder = new ProcessBuilder(localArrayList);
	        //For Terminal-Output
	        localProcessBuilder.inheritIO();
	        //Starting it!
			localProcessBuilder.start();
        }
	}
	public static ArrayList<String> fromConfiguration(AbstractConfiguration conf)
	{
        StringBuilder classpath = new StringBuilder();
        
        String[] cpath = {
        		//Path to myself:
        		//LoaderLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(),
        		//The classpath that i used to find the minecraft launcher
        		//Does contain myself :D
        		System.getProperty("java.class.path"),
        		//Minecraft-Standard-Libs
        		conf.getMinecraftBaseFolder()+"bin/jinput.jar",
        		conf.getMinecraftBaseFolder()+"bin/lwjgl.jar",
        		conf.getMinecraftBaseFolder()+"bin/lwjgl_util.jar",
        		conf.getMinecraftBaseFolder()+"bin/minecraft.jar",
        		};
        String pathSep=System.getProperty("path.separator");
        for(String s: cpath)
        {
        	classpath.append(s);
        	classpath.append(pathSep);
        }
        
        
        ArrayList<String> localArrayList = new ArrayList<String>();
       	localArrayList.add(conf.getJavaExecutable());
        //Memory Parameter
       	if(conf.getMaxMemory()>0)
       		localArrayList.add("-Xmx"+conf.getMaxMemory()+"m");
       	if(conf.getMinMemory()>0)
       		localArrayList.add("-Xms"+conf.getMinMemory()+"m");

        //localArrayList.add("-Djava.library.path=\"/C:/Users/Matthias/AppData/Roaming/.minecraft/natives\"");
        localArrayList.add("-Djava.library.path=\""+conf.getMinecraftBaseFolder()+"bin/natives\"");
        //Parameter the normal Minecraft Launcher uses:
     /*   localArrayList.add("-Dsun.java2d.noddraw=true");
        localArrayList.add("-Dsun.java2d.d3d=false");
        localArrayList.add("-Dsun.java2d.opengl=false");
        localArrayList.add("-Dsun.java2d.pmoffscreen=false");*/
        //Classpath:
        localArrayList.add("-classpath");
        localArrayList.add(classpath.toString());
        //Start another Class which then injects Classes from its command-line-argument-urls
        localArrayList.add(StartMinecraft.class.getName());
        localArrayList.add("-c");
        localArrayList.add(conf.getClassname());
        //The Folders to search for replacing classes:
        localArrayList.add("file:"+conf.getMinecraftBaseFolder()+"mod/");
        localArrayList.add("--");
        if(!conf.getPlayerName().equals(""))
        {
        	localArrayList.add(conf.getPlayerName());
        }
        return localArrayList;
	}
	
	//Sorted!
	private static final char[] safechars = "%+,-./0123456789:=@ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".toCharArray();
	public static String stringify(String[] command)
	{
		StringBuilder sb = new StringBuilder();
		for(String s:command)
			sb.append(quote(s)).append(" ");
		return sb.toString();
		
	}
	private static String quote(String file)
	{
		return '"'+file+'"';
		//Something is broken:
		/*for(char c:file.toCharArray())
		{
			if(Arrays.binarySearch(safechars, c)<0)
			{//file does contain an unsafe char
				return "'" + file.replace("'", "'\"'\"'") + "'";
			}
		}
		if(file=="")
			return "''";
		return file;*/
	}

}
