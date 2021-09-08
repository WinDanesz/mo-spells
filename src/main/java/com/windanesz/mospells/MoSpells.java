package com.windanesz.mospells;

import com.windanesz.mospells.registry.MSItems;
import com.windanesz.mospells.registry.MSLoot;
import electroblob.wizardry.spell.Spell;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

@Mod(
		modid = MoSpells.MODID,
		name = MoSpells.MOD_NAME,
		version = MoSpells.VERSION,
		acceptedMinecraftVersions = "[@MCVERSION@]",
		dependencies = "required-after:ebwizardry@[@WIZARDRY_VERSION@,4.4);required:mowziesmobs@[@MOWZIES_VERSION@,1.6);before:ancientspellcraft"
)
public class MoSpells {

	public static final String MODID = "mospells";
	public static final String MOD_NAME = "Mo' Spells";
	public static final String VERSION = "1.0.1";

	/**
	 * This is the instance of your mod as created by Forge. It will never be null.
	 */
	@Mod.Instance(MODID)
	public static MoSpells instance;

	public static Logger logger;

	// Location of the proxy code, used by Forge.
	@SidedProxy(clientSide = "com.windanesz.mospells.client.ClientProxy", serverSide = "com.windanesz.mospells.CommonProxy")
	public static CommonProxy proxy;

	/**
	 * Static instance of the {@link Settings} object for Mo' Spells.
	 */
	public static Settings settings = new Settings();

	/**
	 * This is the first initialization event. Register tile entities here.
	 * The registry events below will have fired prior to entry to this method.
	 */
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		settings = new Settings();

		proxy.registerRenderers();
		MSItems.registerBookshelfModelTextures();
		MSLoot.preInit();
	}

	/**
	 * This is the second initialization event. Register custom recipes
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(instance); // Since there's already an instance we might as well use it
		MSItems.registerDispenseBehaviours();

		proxy.init();
		MSItems.registerBookshelfItems();
	}

	/**
	 * This is the final initialization event. Register actions from other mods here
	 */
	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) {}
}
