package com.windanesz.mospells.item;

import com.windanesz.mospells.MoSpells;
import electroblob.wizardry.item.ItemSpellBook;
import electroblob.wizardry.spell.Spell;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemMSSpellBook extends ItemSpellBook {

	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(MoSpells.MODID, "textures/gui/spell_book_mospells.png");

	@Override
	public ResourceLocation getGuiTexture(Spell spell) {
		return GUI_TEXTURE;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
}