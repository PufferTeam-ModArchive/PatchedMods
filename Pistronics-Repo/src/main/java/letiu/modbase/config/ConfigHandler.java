package letiu.modbase.config;

import java.io.File;
import java.util.Set;
import letiu.pistronics.config.ConfigData;
import letiu.pistronics.config.ConfigObject;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
	
	public static void init(File configFile) {
		
		// create configuration object from the given configuration file
		Configuration configuration = new Configuration(configFile);
	
		String TWEAKS = "TWEAKS";
		
		try {
			// Load the configuration file
			configuration.load();
			
			// Read in properties from configuration file
			// TODO: ...
			Set keys = ConfigData.getKeySet();
			
			for (Object key : keys) {
				ConfigObject co = ConfigData.getConfigObject((String) key);
				if (co.value instanceof Integer) {
					co.value = configuration.get(TWEAKS, (String) key, (Integer) co.value, co.comment).getInt();
				}
				else if (co.value instanceof Boolean) {
					co.value = configuration.get(TWEAKS, (String) key, (Boolean) co.value, co.comment).getBoolean((Boolean) co.value);
				}
				else if (co.value instanceof Double) {
					co.value = configuration.get(TWEAKS, (String) key, (Double) co.value, co.comment).getDouble(6.0D);
				}
				else if (co.value instanceof String) {
					co.value = configuration.get(TWEAKS, (String) key, (String) co.value, co.comment).getString();
				}
			}
			
			
		}
		catch(Exception e) {
			// Log the exception
		}
		finally {
			// Save the configuration file
			configuration.save();
		}
	}
	
}
