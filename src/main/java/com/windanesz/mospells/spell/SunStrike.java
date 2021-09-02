package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SunStrike extends SpellRay {

	public SunStrike() {
		super(MoSpells.MODID, "sunstrike", SpellActions.POINT_UP, false);
	}

	@Override
	protected boolean onEntityHit(World world, Entity entity, Vec3d vec3d,
			@Nullable EntityLivingBase entityLivingBase, Vec3d vec3d1, int i, SpellModifiers spellModifiers) {
		return spawnSunStrike(world, entityLivingBase, new BlockPos(entity.posX, entity.posY, entity.posZ), vec3d, spellModifiers);
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos blockPos, EnumFacing enumFacing, Vec3d vec3d,
			@Nullable EntityLivingBase entityLivingBase, Vec3d vec3d1, int i, SpellModifiers spellModifiers) {
		return spawnSunStrike(world, entityLivingBase, blockPos, vec3d, spellModifiers);
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase entityLivingBase, Vec3d vec3d, Vec3d vec3d1, int i, SpellModifiers spellModifiers) {
		return false;
	}

	private boolean spawnSunStrike(World world, @Nullable EntityLivingBase caster, @Nullable BlockPos blockPos, Vec3d vec3d, SpellModifiers spellModifiers) {
		if (!world.canBlockSeeSky(new BlockPos(vec3d))) { return false; }

		EntitySunstrike sunstrike = new EntitySunstrike(world, caster, (int) vec3d.x - 1, (int) vec3d.y, (int) vec3d.z - 1);
		sunstrike.onSummon();
		if (!world.isRemote) { world.spawnEntity(sunstrike); }
		return true;
	}

	@Override
	public boolean applicableForItem(Item item) {
		return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll;
	}
}
