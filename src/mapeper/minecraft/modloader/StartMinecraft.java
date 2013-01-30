package mapeper.minecraft.modloader;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import mapeper.minecraft.modloader.plugin.MEModloaderPlugin;
import mapeper.minecraft.modloader.plugin.PluginFailedException;
public class StartMinecraft {
	public static void main(String[] args) {
		LinkedList<URL> urls = new LinkedList<URL>();
		LinkedList<String> pluginClasses = new LinkedList<String>();
		LinkedList<String> pluginArguments = new LinkedList<String>();
		String classname = "net.minecraft.LauncherFrame";
		int argumentstart=args.length;
		if(args.length>=1)
		{
			int i=0;
			if(args[0].equals("-h")||args[0].equals("--help"))
			{
				printUsage();
				System.exit(0);
			}
			if(args[0].equals("-c"))
			{
				if(args.length>=2)
				{
					classname=args[1];
					i=2;
				}
				else
				{
					printUsage();
					System.exit(1);
				}
			}
			for(;i<args.length;i++)
			{
				if(args[i].equals("-p"))
				{
					if(i+1<args.length)
					{
						String[] pluginString = args[i+1].split("~",2);
						if(pluginString.length!=2)
							System.err.println("Error Parsing PluginString");
						else
						{
							pluginClasses.add(pluginString[0]);
							pluginArguments.add(pluginString[1]);
						}
						i++;
						continue;
					}
					else
					{
						System.err.println("Missing Argument for -p");
					}
				}
				else if(args[i].equals("--"))
				{
					argumentstart=i+1;
					break;
				}
				try
				{
					urls.add(new URL(args[i]));
				}
				catch(MalformedURLException e)
				{
					System.err.println("Error while parsing Argument "+i);
					e.printStackTrace();
				}
			}
		}
		//Inject our ClassLoader with the custom urls
		URL[] urlarray=new URL[urls.size()];
		urls.toArray(urlarray);
		try {
			ReplacingClassLoader.inject(urlarray);
		} catch (Exception e) {
			System.err.println("Could not inject ReplacingClassLoader");
			e.printStackTrace();
			System.exit(1);
		}
		//and then start the normal launcher
		//System.out.println(Arrays.deepToString(urlarray));
		
		Iterator<String> pluginClassIterator = pluginClasses.iterator();
		Iterator<String> pluginArgumentsIterator = pluginArguments.iterator();
		ClassLoader classLoader = StartMinecraft.class.getClassLoader();
		MEModloaderPlugin pluginInstance;
		while(pluginClassIterator.hasNext()&&pluginArgumentsIterator.hasNext())
		{
			String pluginClassName = pluginClassIterator.next();
			String pluginArgumentString = pluginArgumentsIterator.next();
			try {
				System.out.println("Loading plugin: "+pluginClassName);
				Class<?> pluginClass=classLoader.loadClass(pluginClassName);
				pluginInstance = (MEModloaderPlugin)pluginClass.newInstance();
				String operationClass = pluginInstance.getPluginClass();
				System.out.println("Loading Class for Plugin: "+operationClass);
				pluginInstance.operate(StartMinecraft.class.getClassLoader().loadClass(operationClass), pluginArgumentString);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch(ClassCastException e) {
				e.printStackTrace();
			} catch (PluginFailedException e) {
				e.printStackTrace();
			}
		}
		
		startMainMethod(classname, Arrays.copyOfRange(args, argumentstart, args.length));
		//.main(new String[0]);
	}
	private static void startMainMethod(String classname, String[] arguments)
	{
		
		ClassLoader classLoader = StartMinecraft.class.getClassLoader();
		Class<?> clazz;
		try {
			clazz = classLoader.loadClass(classname);
			Method method = clazz.getMethod("main", String[].class);
			System.out.println("Invoking "+classname+".main("+Arrays.deepToString(arguments)+")");
			method.invoke(null, (Object)arguments);
		} catch (ClassNotFoundException e) {
			System.err.println("Could not load class: "+classname);
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.err.println("No main(String[])-Method in class "+classname);
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error invoking main-Method");
			e.printStackTrace();
		}

	}
	private static void printUsage()
	{
		System.out.println("USAGE:");
		System.out.println("java [arguments for java] mapeper.minecraft.modloader.StartMinecraft [-c CLASSNAME] [URL1, URL2, ...] [-- [Arguments for main-Method]]");
		System.out.println("-c CLASSNAME");
		System.out.println("\tInvokes the main Method of the specified class instead of net.minecraft.LauncherFrame");
		System.out.println("URLs:");
		System.out.println("\tURLs to scan for replacement-class-files");
		System.out.println("Examples:");
		System.out.println("\tfile:/home/username/.minecraft/mod");
		System.out.println("\thttp://myserver.123/mods.zip");
		System.out.println("\tfile:/C:/Users/username/AppData/Roaming/.minecraft/mod.zip");
	}
}
