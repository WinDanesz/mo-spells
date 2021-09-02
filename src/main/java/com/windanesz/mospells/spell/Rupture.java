package com.windanesz.mospells.spell;

import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.entity.EntityRupture;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class Rupture extends Spell {

	public Rupture() {
		super(MoSpells.MODID, "rupture", SpellActions.POINT_UP, true);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (caster != null && caster.onGround) {
			EntityRupture rupture = new EntityRupture(world, caster, true);
			rupture.setPositionAndRotation(caster.posX, caster.posY, caster.posZ, caster.rotationYaw, caster.rotationPitch);
			if (!world.isRemote) { world.spawnEntity(rupture); }
			return true;
		}
		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return false;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
