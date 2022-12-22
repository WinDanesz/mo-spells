package com.windanesz.mospells.registry;

import com.windanesz.mospells.MoSpells;
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

import java.util.Arrays;
import java.util.List;

/**
 * Class responsible for registering Mo' Spells's loot tables. Also handles loot injection.
 * @author WinDanesz
 */
@Mod.EventBusSubscriber
public class MSLoot {

	private static LootTable SPELL_BOOK_HIGH;
	private static LootTable SPELL_BOOK_MEDIUM;
	private static LootTable SPELL_BOOK_LOW;

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

	}

	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event) {

		// ----------------------------------- INIT -----------------------------------

		if (event.getName().toString().equals(MoSpells.MODID + ":inject/mospells_spell_book_high")) {
			SPELL_BOOK_HIGH = event.getTable();
		} else if (event.getName().toString().equals(MoSpells.MODID + ":inject/mospells_spell_book_medium")) {
			SPELL_BOOK_MEDIUM = event.getTable();
		} else if (event.getName().toString().equals(MoSpells.MODID + ":inject/mospells_spell_book_low")) {
			SPELL_BOOK_LOW = event.getTable();
		}

		// ---------------------------------- INJECT ----------------------------------
		// inject the new spell book type to mowzie mobs as loot
		if (Arrays.asList(MoSpells.settings.spellbookInjectionLocations).contains(event.getName())) {

			String name = event.getName().toString();

			if (HIGH_BOOK_CHANCE.contains(name) && SPELL_BOOK_HIGH != null) {
				LootPool sourcePool = SPELL_BOOK_HIGH.getPool("mospells_with_artefact");
				event.getTable().addPool(sourcePool);
				LootPool sourcePool2 = SPELL_BOOK_HIGH.getPool("mospells_without_artefact");
				event.getTable().addPool(sourcePool2);
			} else if (MEDIUM_BOOK_CHANCE.contains(name) && SPELL_BOOK_MEDIUM != null) {
				LootPool sourcePool = SPELL_BOOK_MEDIUM.getPool("mospells_with_artefact");
				event.getTable().addPool(sourcePool);
				LootPool sourcePool2 = SPELL_BOOK_MEDIUM.getPool("mospells_without_artefact");
				event.getTable().addPool(sourcePool2);
			} else if(SPELL_BOOK_LOW != null) {
				LootPool sourcePool = SPELL_BOOK_LOW.getPool("mospells_with_artefact");
				event.getTable().addPool(sourcePool);
				LootPool sourcePool2 = SPELL_BOOK_LOW.getPool("mospells_without_artefact");
				event.getTable().addPool(sourcePool2);
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
