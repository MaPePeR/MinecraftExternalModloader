package mapeper.minecraft.modloader.plugin;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;




public class PortableMinecraftPlugin implements MEModloaderPlugin {
	@Override
	public String getPluginClass() {
		return "net.minecraft.client.Minecraft";
		//return "java.lang.String";
	}

	@Override
	public void operate(Class<?> clazz, String argument) throws PluginFailedException {
		LinkedList<Field> possibleFields = new LinkedList<Field>();
		for(Field f: clazz.getDeclaredFields())
		{
			if(f.getType()==java.io.File.class&&Modifier.isStatic(f.getModifiers()))
			{
				possibleFields.add(f);
			}
		}
		if(possibleFields.size()==1)
		{
			File dir = new File(argument);
			if(dir.isDirectory())
			{
				try {
					Field f = possibleFields.get(0);
					f.setAccessible(true);
					f.set(null, dir);
					System.out.println("Minecraft should now save stuff in  "+argument);
				} catch (IllegalArgumentException e) {
					throw new PluginFailedException(e);
				} catch (IllegalAccessException e) {
					throw new PluginFailedException(e);
				}
			}
			else
			{
				throw new PluginFailedException("Argument is not a Directory: "+argument);
			}
		}
		else
		{
			throw new PluginFailedException("Coudn't find Field! Found: "+possibleFields.size());
		}
	}

}
