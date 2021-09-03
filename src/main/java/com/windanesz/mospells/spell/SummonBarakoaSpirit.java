package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.entity.EntityBarakoaSpiritMinion;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class SummonBarakoaSpirit extends SpellMinion<EntityBarakoaSpiritMinion> {

	public SummonBarakoaSpirit() {
		super(MoSpells.MODID, "summon_barakoa_spirit", EntityBarakoaSpiritMinion::new);
		this.soundValues(1, 1.1f, 0.1f);
	}

	@Override
	protected void addMinionExtras(EntityBarakoaSpiritMinion minion, BlockPos pos,
			@Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
		if (caster instanceof EntityPlayer) {
			MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(caster, MowziePlayerProperties.class);
			minion.setWeapon(minion.randomizeWeapon());
			property.addPackMember(minion);
		}
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
