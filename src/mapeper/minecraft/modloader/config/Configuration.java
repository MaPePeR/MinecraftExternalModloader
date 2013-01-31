package mapeper.minecraft.modloader.config;

import java.net.URL;

public class Configuration extends AbstractConfiguration {
	public void setJavaExecutable(String javaExecutable) {
		this.javaExecutable = javaExecutable;
	}

	public void setMinecraftBaseFolder(String minecraftBaseFolder) {
		this.minecraftBaseFolder = minecraftBaseFolder;
	}

	public void setMinMemory(int minMemory) {
		this.minMemory = minMemory;
	}

	public void setMaxMemory(int maxMemory) {
		this.maxMemory = maxMemory;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public void setModURLs(String[] modURLs)
	{
		this.modURLs=modURLs;
	}

}
