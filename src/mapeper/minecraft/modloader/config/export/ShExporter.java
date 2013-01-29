package mapeper.minecraft.modloader.config.export;

import java.io.IOException;
import java.io.OutputStream;

import mapeper.minecraft.modloader.GenerateStartCommand;

public class ShExporter implements Exporter {

	@Override
	public void export(OutputStream o, String[] command) throws IOException {
		o.write("#!/bin/sh\n".getBytes());
		o.write(GenerateStartCommand.stringify(command).getBytes());
		o.write('\n');
		o.close();
	}

	@Override
	public String getExtension() {
		return "sh";
	}

}
