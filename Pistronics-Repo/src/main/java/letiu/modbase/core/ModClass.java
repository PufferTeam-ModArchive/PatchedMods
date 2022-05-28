package letiu.modbase.core;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import letiu.modbase.blocks.BlockCollector;
import letiu.modbase.blocks.VanillaData;
import letiu.modbase.config.ConfigHandler;
import letiu.modbase.entities.EntityCollector;
import letiu.modbase.events.ArrowEventHandler;
import letiu.modbase.integration.nei.RecipeHandlers;
import letiu.modbase.items.ItemCollector;
import letiu.modbase.proxies.IProxy;
import letiu.modbase.render.TextureMapper;
import letiu.pistronics.config.ConfigData;
import letiu.pistronics.data.BlockData;
import letiu.pistronics.data.EntityData;
import letiu.pistronics.data.GuiHandler;
import letiu.pistronics.data.ItemData;
import letiu.pistronics.data.PacketData;
import letiu.pistronics.recipes.Recipes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "Pistronics2", name = "Pistronics 2", version = "0.6.3")
public class ModClass {

	@Instance(ModInformation.ID)
	public static ModClass instance;
	
	@SidedProxy(clientSide = ModBaseInfo.CLIENT_PROXY, serverSide = ModBaseInfo.SERVER_PROXY)
	public static IProxy proxy;
	
	public static CreativeTabs modTap = new ModCreativeTab();
	
	public static ArrowEventHandler arrowEventHandler = new ArrowEventHandler();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigData.init();
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		ConfigData.load();

		VanillaData.init();
		TextureMapper.init();
		BlockCollector.init();
		ItemCollector.init();
		BlockData.init();
		ItemData.init();
		EntityData.init();
		BlockCollector.createBlocks();
		ItemCollector.createItems();
		EntityCollector.registerEntities();

		proxy.init();
		proxy.registerRenderers();
		proxy.registerEvents();
		Recipes.registerRecipes();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerTileEntities();
		PacketData.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, (IGuiHandler)new GuiHandler());
		
		MinecraftForge.EVENT_BUS.register(arrowEventHandler);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (event.getSide() == Side.CLIENT && Loader.isModLoaded("NotEnoughItems")) {
			RecipeHandlers.registerHandlers();
		}
	}
	static {
		modTap = new ModCreativeTab();
		arrowEventHandler = new ArrowEventHandler();
	}
}