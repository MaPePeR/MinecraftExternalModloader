package memplugin;

/**
 * Interface for defining Plugins that will operate on uninstantiated Classes.<br/>
 * Classes for Plugins will then be pre-loaded.
 * @author Matthias
 *
 */
public interface MEModloaderPlugin {
	/**
	 * Defines which Class this Plugin will operate on
	 * @return full package name of the class
	 */
	public String getPluginClass();
	public void operate(Class<?> clazz, String argument) throws PluginFailedException;
	
}
