package letiu.modbase.proxies;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayerFactory;
import letiu.modbase.render.FakeWorld;
import letiu.modbase.tiles.BaseTile;
import letiu.modbase.tiles.InventoryTile;
import letiu.modbase.tiles.SRInventoryTile;
import letiu.modbase.tiles.SpecialRenderTile;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {

	protected FakeWorld fakeWorld, logicFakeWorld;
	protected EntityPlayerMP fakePlayer;

	@Override
	public EntityPlayerMP getFakePlayerMP() {
		if (this.fakePlayer == null) {
			World world = DimensionManager.getProvider((int)0).worldObj;
			this.fakePlayer = FakePlayerFactory.get((WorldServer)((Object)world), new GameProfile(new UUID(1L, 2L), "packetFakePlayer"));
		}
		return this.fakePlayer;
	}

	@Override
	public FakeWorld getFakeWorld(World realWorld) {
		if (this.fakeWorld == null) {
			this.fakeWorld = new FakeWorld(realWorld);
		} else {
			this.fakeWorld.setRealWorld(realWorld);
		}
		return this.fakeWorld;
	}

	@Override
	public FakeWorld getLogicFakeWorld(World realWorld) {
		if (this.logicFakeWorld == null) {
			this.logicFakeWorld = new FakeWorld(realWorld);
		} else {
			this.logicFakeWorld.setRealWorld(realWorld);
		}
		return this.logicFakeWorld;
	}
	
	@Override
	public void registerRenderers() {}
	
	@Override
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(BaseTile.class, "tlBaseTile");
		GameRegistry.registerTileEntity(SpecialRenderTile.class, "tlSpecialRenderTile");
		GameRegistry.registerTileEntity(InventoryTile.class, "tlInventoryTile");
		GameRegistry.registerTileEntity(SRInventoryTile.class, "tlSRInventoryTile");
	}
	
	@Override
	public void registerRecipes() {
		
	}
}
