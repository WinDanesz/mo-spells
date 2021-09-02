package com.windanesz.mospells.registry;

import com.windanesz.mospells.MoSpells;
import electroblob.wizardry.Wizardry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class responsible for registering Mo' Spells's loot tables. Also handles loot injection.
 * @author WinDanesz
 */
@Mod.EventBusSubscriber
public class MSLoot {

	private static LootTable RARE_ARTEFACTS;
	private static LootTable EPIC_ARTEFACTS;

	private static final List<String> HIGH_BOOK_CHANCE = Arrays.asList(
			"mowziesmobs:entities/barako",
			"mowziesmobs:entities/ferrous_wroughtnaut",
			"mowziesmobs:entities/frostmaw");

	private static final List<String> MEDIUM_BOOK_CHANCE = Arrays.asList(
			"mowziesmobs:entities/barakoa_fury", // = barakoana
			"mowziesmobs:entities/grottol",
			"mowziesmobs:entities/naga ");

	private MSLoot() {} // no instances

	/**
	 * Called from the preInit method in the main mod class to register the custom dungeon loot.
	 */

	public static void preInit() {

		// inject
		LootTableList.register(new ResourceLocation(MoSpells.MODID, "inject/mospells_spell_book_high"));
		LootTableList.register(new ResourceLocation(MoSpells.MODID, "inject/mospells_spell_book_medium"));
		LootTableList.register(new ResourceLocation(MoSpells.MODID, "inject/mospells_spell_book_low"));

		// subsets
		LootTableList.register(new ResourceLocation(MoSpells.MODID, "subsets/rare_artefacts"));
		LootTableList.register(new ResourceLocation(MoSpells.MODID, "subsets/epic_artefacts"));

	}

	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event) {

		// ----------------------------------- INIT -----------------------------------

		// Fortunately the loot tables of Mo'Spells load before wizardry so we can make a static reference to them and reuse it
		// no uncommon artefacts yet
		if (event.getName().toString().equals(MoSpells.MODID + ":subsets/rare_artefacts")) {
			RARE_ARTEFACTS = event.getTable();
		} else if (event.getName().toString().equals(MoSpells.MODID + ":subsets/epic_artefacts")) {
			EPIC_ARTEFACTS = event.getTable();
		}

		// ---------------------------------- INJECT ----------------------------------
		// inject the new spell book type to mowzie mobs as loot
		if (Arrays.asList(MoSpells.settings.spellbookInjectionLocations).contains(event.getName())) {

			String name = event.getName().toString();

			if (HIGH_BOOK_CHANCE.contains(name)) {
				event.getTable().addPool(getAdditive(MoSpells.MODID + ":inject/mospells_spell_book_high", MoSpells.MODID + "_mospells_spell_book"));
			} else if (MEDIUM_BOOK_CHANCE.contains(name)) {
				event.getTable().addPool(getAdditive(MoSpells.MODID + ":inject/mospells_spell_book_medium", MoSpells.MODID + "_mospells_spell_book"));
			} else {
				event.getTable().addPool(getAdditive(MoSpells.MODID + ":inject/mospells_spell_book_low", MoSpells.MODID + "_mospells_spell_book"));
			}

		}

		// inject artefacts to ebwiz tables
		if (Arrays.asList(MoSpells.settings.artefactInjectionLocations).contains(event.getName())) {

			// No uncommon artefacts yet
			if (event.getName().toString().equals(Wizardry.MODID + ":subsets/rare_artefacts") && RARE_ARTEFACTS != null) {
				LootPool targetPool = event.getTable().getPool("rare_artefacts");
				LootPool sourcePool = RARE_ARTEFACTS.getPool("main");

				injectEntries(sourcePool, targetPool);
			}
			if (event.getName().toString().equals(Wizardry.MODID + ":subsets/epic_artefacts") && EPIC_ARTEFACTS != null) {
				LootPool targetPool = event.getTable().getPool("epic_artefacts");
				LootPool sourcePool = EPIC_ARTEFACTS.getPool("main");
				injectEntries(sourcePool, targetPool);
			}
		}
	}

	/**
	 * Injects every element of sourcePool into targetPool
	 */
	private static void injectEntries(LootPool sourcePool, LootPool targetPool) {
		// Accessing {@link net.minecraft.world.storage.loot.LootPool.lootEntries}
		if (sourcePool != null && targetPool != null) {
			List<LootEntry> lootEntries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, sourcePool, "field_186453_a");

			for (LootEntry entry : lootEntries) {
				targetPool.addEntry(entry);
			}
		} else {
			MoSpells.logger.warn("Attempted to inject to null pool source or target.");
		}
	}

	private static LootPool getAdditive(String entryName, String poolName) {
		return new LootPool(new LootEntry[] {getAdditiveEntry(entryName, 1)}, new LootCondition[0],
				new RandomValueRange(1), new RandomValueRange(0, 1), MoSpells.MODID + "_" + poolName);
	}

	private static LootEntryTable getAdditiveEntry(String name, int weight) {
		return new LootEntryTable(new ResourceLocation(name), weight, 0, new LootCondition[0],
				MoSpells.MODID + "_additive_entry");
	}

}
