package com.windanesz.mospells;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static electroblob.wizardry.Settings.toResourceLocations;

@Config(modid = MoSpells.MODID, name = "MoSpells") // No fancy configs here so we can use the annotation, hurrah!
public class Settings {

	public ResourceLocation[] spellbookInjectionLocations = toResourceLocations(generalSettings.SPELLBOOK_INJECTION_LOCATIONS);

	@SuppressWarnings("unused")
	@Mod.EventBusSubscriber(modid = MoSpells.MODID)
	private static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(MoSpells.MODID)) {
				ConfigManager.sync(MoSpells.MODID, Config.Type.INSTANCE);
			}
		}
	}

	@Config.Name("General Settings")
	public static GeneralSettings generalSettings = new GeneralSettings();

	public static class GeneralSettings {

		@Config.Name("Spell Book Inject locations")
		@Config.Comment("List of loot tables to inject Mo' Spells spellbooks into.")
		public String[] SPELLBOOK_INJECTION_LOCATIONS = {
				"mowziesmobs:entities/barako",
				"mowziesmobs:entities/barakoa_bliss",
				"mowziesmobs:entities/barakoa_fear",
				"mowziesmobs:entities/barakoa_fury",
				"mowziesmobs:entities/barakoa_misery",
				"mowziesmobs:entities/barakoa_rage",
				"mowziesmobs:entities/ferrous_wroughtnaut",
				"mowziesmobs:entities/foliaath",
				"mowziesmobs:entities/grottol",
				"mowziesmobs:entities/lantern",
				"mowziesmobs:entities/naga",
				"mowziesmobs:entities/frostmaw"
		};
	}
}