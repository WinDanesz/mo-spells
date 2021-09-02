package com.windanesz.mospells.spell;

import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.entity.EntityNagaMinion;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.spell.SpellMinion;
import net.minecraft.item.Item;

public class SummonNaga extends SpellMinion<EntityNagaMinion> {

	public SummonNaga() {
		super(MoSpells.MODID, "summon_naga", EntityNagaMinion::new);
		this.soundValues(1, 1.1f, 0.1f);
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
