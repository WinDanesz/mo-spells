package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PoisonBall extends Spell {

	public PoisonBall() {
		super(MoSpells.MODID, "poison_ball", SpellActions.POINT, false);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {

		Vec3d projectilePos = new Vec3d(1, 0, 0);
		projectilePos = projectilePos.rotateYaw((float) Math.toRadians(-caster.rotationYaw - 90));
		projectilePos = projectilePos.add(caster.getPositionVector());
		projectilePos = projectilePos.add(new Vec3d(0, 0, 1).rotatePitch((float) Math.toRadians(-caster.rotationPitch)).rotateYaw((float) Math.toRadians(-caster.rotationYawHead)));
		projectilePos = projectilePos.add(new Vec3d(0, 0.3, 0));
		EntityPoisonBall poisonBall = new EntityPoisonBall(caster.world, caster);
		poisonBall.setPosition(projectilePos.x, projectilePos.y, projectilePos.z);

		float f = -MathHelper.sin(caster.rotationYaw * 0.017453292F) * MathHelper.cos(caster.rotationPitch * 0.017453292F);
		float f1 = -MathHelper.sin(pitch * 0.017453292F);
		float f2 = MathHelper.cos(caster.rotationYaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		poisonBall.shoot((double) f, 0.5 + (double) f1 * 2, (double) f2, 0.8f, 0f);

		if (!world.isRemote) {
			world.spawnEntity(poisonBall);
		}

		world.playSound(null, caster.getPosition(), MMSounds.ENTITY_NAGA_ACID_SPIT, SoundCategory.PLAYERS, 2, 1);
		world.playSound(null, caster.getPosition(), MMSounds.ENTITY_NAGA_ACID_SPIT_HISS, SoundCategory.PLAYERS, 2, 1);
		return true;
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
