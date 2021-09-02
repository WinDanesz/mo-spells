package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.registry.MSItems;
import com.windanesz.mospells.registry.MSSpells;
import com.windanesz.mospells.util.Utils;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class SolarFlare extends Spell {

	public static final String KNOCKBACK = "knockback";

	public SolarFlare() {
		super(MoSpells.MODID, "solar_flare", SpellActions.THRUST, true);
		addProperties(DAMAGE, BLAST_RADIUS, BURN_DURATION, KNOCKBACK);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		return createSolarFlare(world, caster, hand, ticksInUse, modifiers);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return createSolarFlare(world, caster, hand, ticksInUse, modifiers);
	}

	/**
	 * Adapted from { com.bobmowzie.mowziesmobs.server.ai.animation.AnimationRadiusAttack and EntityBarako} Author: Bob Mowzie
	 */
	public static boolean createSolarFlare(World world, EntityLivingBase caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (ticksInUse == 1) {
			world.playSound(null, caster.getPosition(), new SoundEvent(new ResourceLocation("ebwizardry:spell.fire_breath.start")), WizardrySounds.SPELLS, 1f, 1f);
		}
		float radius = MSSpells.solar_flare.getProperty(BLAST_RADIUS).floatValue() * modifiers.get(WizardryItems.blast_upgrade);

		if (world.isRemote && ticksInUse < 6) {

			int particleCount = 3 * MSSpells.solar_flare.getProperty(BLAST_RADIUS).intValue();
			while (--particleCount != 0) {
				double yaw = world.rand.nextFloat() * 2 * Math.PI;
				double pitch = world.rand.nextFloat() * 2 * Math.PI;
				double ox = radius * Math.sin(yaw) * Math.sin(pitch);
				double oy = radius * Math.cos(pitch);
				double oz = radius * Math.cos(yaw) * Math.sin(pitch);
				double offsetX = -0.3 * Math.sin(caster.rotationYaw * Math.PI / 180);
				double offsetZ = -0.3 * Math.cos(caster.rotationYaw * Math.PI / 180);
				double offsetY = 1;
				MMParticle.ORB.spawn(world, caster.posX + ox + offsetX, caster.posY + offsetY + oy, caster.posZ + oz + offsetZ, ParticleFactory.ParticleArgs.get().withData(caster.posX + offsetX, caster.posY + offsetY, caster.posZ + offsetZ, 6));
			}
		}

		if (ticksInUse == 10) {
			if (world.isRemote) {
				for (int i = 0; i < 30; i++) {
					final float velocity = 0.25F;
					float yaw = i * (MathUtils.TAU / 30);
					float vy = world.rand.nextFloat() * 0.1F - 0.05f;
					float vx = velocity * MathHelper.cos(yaw);
					float vz = velocity * MathHelper.sin(yaw);
					world.spawnParticle(EnumParticleTypes.FLAME, caster.posX, caster.posY + 1, caster.posZ, vx, vy, vz);
				}
			}

			//caster.playSound(MMSounds.ENTITY_BARAKO_ATTACK, 1.7f, 0.9f);
		}


		float damage = MSSpells.solar_flare.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float knockback = MSSpells.solar_flare.getProperty(KNOCKBACK).floatValue() * modifiers.get(SpellModifiers.POTENCY);

		if (ticksInUse == 8) {
			if (world.isRemote) {

				double particleX, particleZ;

				for (int i = 0; i < 40 * modifiers.get(WizardryItems.blast_upgrade); i++) {

					particleX = caster.posX - 1.0d + 2 * world.rand.nextDouble();
					particleZ = caster.posZ - 1.0d + 2 * world.rand.nextDouble();
					ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE).scale(1.1f).pos(particleX, caster.getPosition().getY() + 1, particleZ)
							.vel(particleX - caster.posX, 0, particleZ - caster.posZ).spawn(world);

					particleX = caster.posX - 1.0d + 2 * world.rand.nextDouble();
					particleZ = caster.posZ - 1.0d + 2 * world.rand.nextDouble();
					ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(particleX, caster.getPosition().getY() +1, particleZ)
							.vel(particleX - caster.posX, 0, particleZ - caster.posZ).time(30).clr(0xe8ba00).spawn(world);

					particleX = caster.posX - 1.0d + 2 * world.rand.nextDouble();
					particleZ = caster.posZ - 1.0d + 2 * world.rand.nextDouble();

					IBlockState block = world.getBlockState(new BlockPos(caster.posX, caster.getPosition().getY() - 0.5 + 1, caster.posZ));

					if (block != null) {
						world.spawnParticle(EnumParticleTypes.BLOCK_DUST, particleX, caster.getPosition().getY() + 1,
								particleZ, particleX - caster.posX, 0, particleZ - caster.posZ, Block.getStateId(block));
					}
				}
			}
		}
		if (ticksInUse == 12) {


			world.playSound(null, caster.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, WizardrySounds.SPELLS, 1f, 1f);

			List<EntityLivingBase> hit = EntityUtils.getEntitiesWithinRadius(radius, caster.posX, caster.posY, caster.posZ, world, EntityLivingBase.class);
			for (EntityLivingBase entityHit : hit) {
				if (entityHit instanceof LeaderSunstrikeImmune || entityHit == caster || AllyDesignationSystem.isAllied(caster, entityHit)) {
					continue;
				}

				entityHit.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FIRE), damage);
				entityHit.setFire(2);
				double angle = Utils.getAngleBetweenEntities(caster, entityHit);
				entityHit.motionX = knockback * Math.cos(Math.toRadians(angle - 90));
				entityHit.motionZ = knockback * Math.sin(Math.toRadians(angle - 90));
			}
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
