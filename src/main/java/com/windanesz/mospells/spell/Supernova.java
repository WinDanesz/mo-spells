package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.entity.EntityMagicSuperNova;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class Supernova extends Spell {

	public Supernova() {
		super(MoSpells.MODID, "supernova", SpellActions.SUMMON, true);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return createNova(world, caster, hand, ticksInUse, modifiers);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return createNova(world, caster, hand, ticksInUse, modifiers);
	}

	public boolean createNova(World world, EntityLivingBase caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {

		if (world.isRemote) {
			for (int i = 0; i < 20; i++) {

				///

				ParticleBuilder.create(ParticleBuilder.Type.FLASH).clr(0xffe15c).entity(caster).pos(-0.5 + world.rand.nextDouble(), 2.5 + world.rand.nextDouble(), -0.5 + world.rand.nextDouble()).scale(0.01f * ticksInUse)
						.spin(0.5, 0.1).spawn(world);

				///

				ParticleBuilder.create(ParticleBuilder.Type.FLASH).clr(0xffe15c).entity(caster).pos(-0.5 + world.rand.nextDouble(), 2.5 + world.rand.nextDouble(), -0.5 + world.rand.nextDouble()).scale(0.01f * ticksInUse)
						.spin(0.5, 0.1).spawn(world);
				ParticleBuilder.create(ParticleBuilder.Type.FLASH).clr(0xfff3bf).entity(caster).pos(-0.5 + world.rand.nextDouble(), 2.5 + world.rand.nextDouble(), -0.5 + world.rand.nextDouble()).scale(0.001f * ticksInUse)
						.spin(0.5, 0.1).spawn(world);
			}
			ParticleBuilder.create(ParticleBuilder.Type.FLASH).clr(0xebad05).entity(caster).pos(0, 3, 0).spawn(world);
			ParticleBuilder.create(ParticleBuilder.Type.FLASH).clr(0xfcfcf7).entity(caster).scale(0.5f).pos(0, 3, 0).spawn(world);
		}

		if (ticksInUse == 0) {
			world.playSound(null, caster.getPosition(), MMSounds.ENTITY_SUPERNOVA_BLACKHOLE, SoundCategory.PLAYERS, 2f, 1.2f);

		}

		if (ticksInUse < 70) {
			List<EntityLivingBase> entities = EntityUtils.getEntitiesWithinRadius(8, caster.posX, caster.posY, caster.posZ, world, EntityLivingBase.class);
			for (EntityLivingBase inRange : entities) {

				if (inRange != caster && AllyDesignationSystem.isValidTarget(caster, inRange)) {

					if (inRange instanceof LeaderSunstrikeImmune) { continue; }
					if (inRange instanceof EntityPlayer && ((EntityPlayer) inRange).capabilities.disableDamage) { continue; }
					Vec3d diff = inRange.getPositionVector().subtract(caster.getPositionVector().add(0, 3, 0));
					diff = diff.normalize().scale(0.03);
					inRange.motionX += -diff.x;
					inRange.motionZ += -diff.z;
					inRange.motionY += -diff.y;

					if (inRange.posY < caster.posY + 4) { inRange.motionY += 0.1; }
				}
			}
		}

		if (!world.isRemote && ticksInUse == 60) {
			Vec3d offset = new Vec3d(1.1f, 0, 0);
			offset = offset.rotateYaw((float) Math.toRadians(-caster.rotationYaw - 90));
			EntityMagicSuperNova superNova = new EntityMagicSuperNova(world, caster, caster.posX + offset.x, caster.posY + 2.05, caster.posZ + offset.z);
			world.spawnEntity(superNova);

		}

		if (ticksInUse > 60) {
			caster.stopActiveHand();
		}
		return true;

	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
