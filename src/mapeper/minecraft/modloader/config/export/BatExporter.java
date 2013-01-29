package mapeper.minecraft.modloader.config.export;

import java.io.IOException;
import java.io.OutputStream;

import mapeper.minecraft.modloader.GenerateStartCommand;

public class BatExporter implements Exporter {

	@Override
	public void export(OutputStream o, String[] command) throws IOException {
		o.write(GenerateStartCommand.stringify(command).getBytes());
		o.write("\r\n".getBytes());
		o.close();
	}

	@Override
	public String getExtension() {
		return "bat";
	}
	

}
