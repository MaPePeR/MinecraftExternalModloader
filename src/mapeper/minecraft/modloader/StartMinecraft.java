package mapeper.minecraft.modloader;

import java.net.MalformedURLException;
import java.net.URL;
public class StartMinecraft {
	public static void main(String[] args) throws Exception {
		URL[] urls = new URL[args.length];
		for(int i=0;i<args.length;i++)
		{
			try
			{
				urls[i]=new URL(args[i]);
			}
			catch(MalformedURLException e)
			{
				System.err.println("Error while parsing Argument "+i);
				e.printStackTrace();
			}
		}
		//Inject our ClassLoader with the custom urls
		ReplacingClassLoader.inject(urls);
		//and then start the normal launcher
		net.minecraft.LauncherFrame.main(new String[0]);
	}
}
