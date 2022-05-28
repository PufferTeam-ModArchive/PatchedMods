package letiu.modbase.entities;

import java.util.ArrayList;

import cpw.mods.fml.common.registry.EntityRegistry;
import java.util.ArrayList;
import letiu.modbase.core.ModClass;
import letiu.pistronics.data.EntityData;
import net.minecraft.entity.Entity;

public class EntityCollector {

	public static void registerEntities() {
		
		ArrayList<Class<? extends Entity>> entities = EntityData.getEntityData();
		
		int id = 0;
		
		for (Class clazz : entities) {
			EntityRegistry.registerModEntity(clazz, clazz.getSimpleName(), id++, ModClass.instance, 80, 1, false);
		}
		
	}
	
}
