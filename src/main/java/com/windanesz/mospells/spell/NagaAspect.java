package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.spell.SpellBuff;
import net.minecraft.item.Item;

public class NagaAspect extends SpellBuff {
	public NagaAspect() {
		super(MoSpells.MODID, "naga_aspect", 0.4f, 1.0f, 0.2f, () -> PotionHandler.POISON_RESIST);
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
