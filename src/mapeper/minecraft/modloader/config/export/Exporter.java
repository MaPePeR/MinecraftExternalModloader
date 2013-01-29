package mapeper.minecraft.modloader.config.export;

import java.io.IOException;
import java.io.OutputStream;

public interface Exporter {
	public String getExtension();
	public void export(OutputStream o, String[] command) throws IOException;
}
