package com.windanesz.mospells.registry;

import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.item.ItemMSSpellBook;
import com.windanesz.wizardryutils.registry.ItemRegistry;
import electroblob.wizardry.block.BlockBookshelf;
import electroblob.wizardry.inventory.ContainerBookshelf;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemScroll;
import electroblob.wizardry.misc.BehaviourSpellDispense;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

/**
 * @author WinDanesz, standard registry methods: Electroblob
 * @since Mo' Spells Pack 1.0
 */

@ObjectHolder(MoSpells.MODID)
@Mod.EventBusSubscriber
public final class MSItems {

	private MSItems() {}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	public static final Item mospells_spell_book = placeholder();
	public static final Item mospells_scroll = placeholder();

	public static final Item ring_wind_touch = placeholder();
	public static final Item ring_ice_crystal = placeholder();

	public static final Item amulet_earth = placeholder();
	public static final Item amulet_frostmaw = placeholder();
	public static final Item amulet_golden_sun = placeholder();

	public static final Item charm_cold_winds = placeholder();
	public static final Item charm_monster_tome = placeholder();

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> registry = event.getRegistry();

		registerItem(registry, "mospells_spell_book", new ItemMSSpellBook());
		registerItem(registry, "mospells_scroll", new ItemScroll());

		ItemRegistry.registerItemArtefact(registry, "ring_wind_touch", MoSpells.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.RING).setCreativeTab(WizardryTabs.GEAR));
		ItemRegistry.registerItemArtefact(registry, "ring_ice_crystal", MoSpells.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING).setCreativeTab(WizardryTabs.GEAR));

		ItemRegistry.registerItemArtefact(registry, "amulet_earth", MoSpells.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET).setCreativeTab(WizardryTabs.GEAR));
		ItemRegistry.registerItemArtefact(registry, "amulet_frostmaw", MoSpells.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET).setCreativeTab(WizardryTabs.GEAR));
		ItemRegistry.registerItemArtefact(registry, "amulet_golden_sun", MoSpells.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET).setCreativeTab(WizardryTabs.GEAR));

		ItemRegistry.registerItemArtefact(registry, "charm_cold_winds", MoSpells.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.CHARM).setCreativeTab(WizardryTabs.GEAR));
		ItemRegistry.registerItemArtefact(registry, "charm_monster_tome", MoSpells.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.CHARM).setCreativeTab(WizardryTabs.GEAR));
	}

	/**
	 * Sets both the registry and unlocalised names of the given item, then registers it with the given registry. Use
	 * this instead of {@link Item#setRegistryName(String)} and {@link Item#setTranslationKey(String)} during
	 * construction, for convenience and consistency. As of wizardry 4.2, this also automatically adds it to the order
	 * list for its creative tab if that tab is a {@link WizardryTabs.CreativeTabListed}, meaning the order can be defined simply
	 * by the order in which the items are registered in this class.
	 *
	 * @param registry The registry to register the given item to.
	 * @param name     The name of the item, without the mod ID or the .name stuff. The registry name will be
	 *                 {@code MoSpellsPack:[name]}. The unlocalised name will be {@code item.MoSpellsPack:[name].name}.
	 * @param item     The item to register.
	 * @author: Electroblob
	 */
	// It now makes sense to have the name first, since it's shorter than an entire item declaration.
	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item) {
		registerItem(registry, name, item, false);
	}

	/**
	 * Sets both the registry and unlocalised names of the given item, then registers it with the given registry. Use
	 * this instead of {@link Item#setRegistryName(String)} and {@link Item#setTranslationKey(String)} during
	 * construction, for convenience and consistency. As of wizardry 4.2, this also automatically adds it to the order
	 * list for its creative tab if that tab is a {@link WizardryTabs.CreativeTabListed}, meaning the order can be defined simply
	 * by the order in which the items are registered in this class.
	 *
	 * @param registry   The registry to register the given item to.
	 * @param name       The name of the item, without the mod ID or the .name stuff. The registry name will be
	 *                   {@code MoSpellsPack:[name]}. The unlocalised name will be {@code item.MoSpellsPack:[name].name}.
	 * @param item       The item to register.
	 * @param setTabIcon True to set this item as the icon for its creative tab.
	 * @author: Electroblob
	 */
	// It now makes sense to have the name first, since it's shorter than an entire item declaration.
	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item, boolean setTabIcon) {

		item.setRegistryName(MoSpells.MODID, name);
		item.setTranslationKey(item.getRegistryName().toString());
		registry.register(item);

		if (setTabIcon && item.getCreativeTab() instanceof WizardryTabs.CreativeTabSorted) {
			((WizardryTabs.CreativeTabSorted) item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if (item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed) {
			((WizardryTabs.CreativeTabListed) item.getCreativeTab()).order.add(item);
		}
	}

	/**
	 * Called from init() in the main mod class to register mo' spells pack's dispenser behaviours.
	 */
	public static void registerDispenseBehaviours() {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(mospells_scroll, new BehaviourSpellDispense());
	}

	/**
	 * Called from preInit() in the main mod class to register Mo' Spells Pack's bookshelf model textures.
	 */
	public static void registerBookshelfModelTextures() {
		BlockBookshelf.registerBookModelTexture(() -> MSItems.mospells_spell_book, new ResourceLocation(MoSpells.MODID, "blocks/books_mospells"));
		BlockBookshelf.registerBookModelTexture(() -> MSItems.mospells_scroll, new ResourceLocation(MoSpells.MODID, "blocks/scrolls_mospells"));
	}

	/**
	 * Called from init() in the main mod class to register Mo' Spells Pack's book items with wizardry's bookshelves.
	 */
	public static void registerBookshelfItems() {
		ContainerBookshelf.registerBookItem(MSItems.mospells_spell_book);
		ContainerBookshelf.registerBookItem(MSItems.mospells_scroll);
	}

}
