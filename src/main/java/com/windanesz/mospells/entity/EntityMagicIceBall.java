package com.windanesz.mospells.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particles.ParticleRing;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.windanesz.mospells.registry.MSSounds;
import com.windanesz.mospells.registry.MSSpells;
import electroblob.wizardry.entity.projectile.EntityIceShard;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

import static com.windanesz.mospells.spell.FrozenOrb.ICE_SHARDS;

/**
 * This class is a slightly modified version of {@link com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall} by WinDanesz
 * Author: Josh/Bob Mowzie (Mowzie's Mobs)
 * Refer to LICENSE.md for more details
 */
public class EntityMagicIceBall extends EntityIceBall {

	private float dam = -1;

	public void setDamage(float damage) {
		this.dam = damage;
	}

	public float getDamage() {
		return dam == -1 ? MSSpells.frost_breath.getProperty(Spell.DAMAGE).floatValue() : dam;
	}

	public EntityMagicIceBall(World worldIn) {
		super(worldIn);
	}

	public EntityMagicIceBall(World worldIn, EntityLivingBase caster) {
		super(worldIn, caster);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		move(MoverType.SELF, motionX, motionY, motionZ);

		if (ticksExisted == 1) {
			if (world.isRemote) {
				MowziesMobs.PROXY.playIceBreathSound(this);
			}
		}

		List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(2);
		if (!entitiesHit.isEmpty()) {
			for (EntityLivingBase entityHit : entitiesHit) {
				if (entityHit == caster) { continue; }
				List<String> freezeImmune = Arrays.asList(ConfigHandler.GENERAL.freeze_blacklist);
				ResourceLocation mobName = EntityList.getKey(entityHit);

				// Added Ebwiz immunity check
				if (mobName != null && (MagicDamage.isEntityImmune(MagicDamage.DamageType.FROST, entityHit)
						|| freezeImmune.contains(mobName.toString()))) { continue; }

				if (entityHit.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), getDamage())) {
					MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entityHit, MowzieLivingProperties.class);
					if (property != null) { property.addFreezeProgress(entityHit, 1); }
					entityHit.addPotionEffect(new PotionEffect(WizardryPotions.frost, 100, 0));
				}
			}
		}

		if (world.collidesWithAnyBlock(getEntityBoundingBox().grow(0.15))) {
			explode();
		}

		if (world.isRemote) {
			float scale = 2f;
			double x = posX;
			double y = posY + height / 2;
			double z = posZ;
			for (int i = 0; i < 4; i++) {
				double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
				double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
				double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
				double value = rand.nextFloat() * 0.15f;
				MMParticle.CLOUD.spawn(world, x + xSpeed, y + ySpeed, z + zSpeed, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 0.75d + value, 0.75d + value, 1d, 1, scale * (10d + rand.nextDouble() * 20d), 20, ParticleCloud.EnumCloudBehavior.SHRINK));
			}
			for (int i = 0; i < 1; i++) {
				double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
				double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
				double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
				MMParticle.CLOUD.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 1d, 1d, 1d, 1, scale * (5d + rand.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK));
			}

			for (int i = 0; i < 5; i++) {
				double xSpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
				double ySpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
				double zSpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
				MMParticle.SNOWFLAKE.spawn(world, x - 20 * (xSpeed) + motionX, y - 20 * ySpeed + motionY, z - 20 * zSpeed + motionZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed));
			}

			if (ticksExisted % 3 == 0) {
				float yaw = (float) Math.atan2(motionX, motionZ);
				float pitch = (float) (Math.acos(motionY / Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) + Math.PI / 2);
				MMParticle.RING.spawn(world, x + 1.5f * motionX, y + 1.5f * motionY, z + 1.5f * motionZ, ParticleFactory.ParticleArgs.get().withData(yaw, pitch, 40, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, 0f, 0f, 0f, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK));
			}

			if (ticksExisted == 1) {
				float yaw = (float) Math.atan2(motionX, motionZ);
				float pitch = (float) (Math.acos(motionY / Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) + Math.PI / 2);
				MMParticle.RING.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(yaw, pitch, 20, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, 0f, 0f, 0f, ParticleRing.EnumRingBehavior.GROW));
			}
		}
		if (ticksExisted > 50) { setDead(); }
	}

	private void explode() {
		world.playSound(this.posX, this.posY, this.posZ, MSSounds.FROZEN_ORB_EXPLODE, SoundCategory.HOSTILE, 1, 1, false);

		// Adapted from {@link electroblob.wizardry.entity.projectile.EntityIceCharge.onImpact}, Author: Electroblob
		if (world.isRemote) {
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 0, 0, 0);
			for (int i = 0; i < 30; i++) {

				ParticleBuilder.create(ParticleBuilder.Type.ICE, rand, this.posX, this.posY, this.posZ, 2, false)
						.time(35).gravity(true).spawn(world);

				float brightness = 0.4f + rand.nextFloat() * 0.5f;
				ParticleBuilder.create(ParticleBuilder.Type.DARK_MAGIC, rand, this.posX, this.posY, this.posZ, 2, false)
						.clr(brightness, brightness + 0.1f, 1.0f).spawn(world);
			}
		}

		if (!this.world.isRemote) {

			this.playSound(WizardrySounds.ENTITY_ICE_CHARGE_SMASH, 1.5f, rand.nextFloat() * 0.4f + 0.6f);
			this.playSound(WizardrySounds.ENTITY_ICE_CHARGE_ICE, 1.2f, rand.nextFloat() * 0.4f + 1.2f);

			double radius = Spells.ice_charge.getProperty(Spell.EFFECT_RADIUS).floatValue();

			List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(radius, this.posX, this.posY,
					this.posZ, this.world);

			// Slows targets
			for (EntityLivingBase target : targets) {
				if (target != this.caster) {
					if (!MagicDamage.isEntityImmune(MagicDamage.DamageType.FROST, target)) {
						target.addPotionEffect(new PotionEffect(WizardryPotions.frost,
								Spells.ice_charge.getProperty(Spell.SPLASH_EFFECT_DURATION).intValue(),
								Spells.ice_charge.getProperty(Spell.SPLASH_EFFECT_STRENGTH).intValue()));
					}
				}
			}

			// Places snow and ice on ground.
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {

					BlockPos pos = new BlockPos(this.posX + i, this.posY, this.posZ + j);

					Integer y = BlockUtils.getNearestSurface(world, pos, EnumFacing.UP, 7, true,
							BlockUtils.SurfaceCriteria.SOLID_LIQUID_TO_AIR);

					if (y != null) {

						pos = new BlockPos(pos.getX(), y, pos.getZ());

						double dist = this.getDistance(pos.getX(), pos.getY(), pos.getZ());

						// Randomised with weighting so that the nearer the block the more likely it is to be snowed.
						if (rand.nextInt((int) dist * 2 + 1) < 1 && dist < 2) {
							if (world.getBlockState(pos.down()).getBlock() == Blocks.WATER) {
								world.setBlockState(pos.down(), Blocks.ICE.getDefaultState());
							} else {
								// Don't need to check whether the block at pos can be replaced since getNearestFloorLevelB
								// only ever returns floors with air above them.
								world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
							}
						}
					}
				}
			}

			// Releases shards
			for (int i = 0; i < Spells.ice_charge.getProperty(ICE_SHARDS).intValue(); i++) {
				double dx = rand.nextDouble() - 0.5;
				double dy = rand.nextDouble() - 0.5;
				double dz = rand.nextDouble() - 0.5;
				EntityIceShard iceshard = new EntityIceShard(world);
				iceshard.setPosition(this.posX + dx, this.posY + dy, this.posZ + dz);
				iceshard.motionX = dx * 1.5;
				iceshard.motionY = dy * 1.5;
				iceshard.motionZ = dz * 1.5;
				iceshard.setCaster(caster);
				iceshard.damageMultiplier = 1;
				world.spawnEntity(iceshard);
			}

			if (world.isRemote) {
				for (int i = 0; i < 8; i++) {
					Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.3, 0, 0);
					particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
					particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
					double value = rand.nextFloat() * 0.15f;
					MMParticle.CLOUD.spawn(world, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x, particlePos.y, particlePos.z, 0.75d + value, 0.75d + value, 1d, 1, 10d + rand.nextDouble() * 20d, 40, ParticleCloud.EnumCloudBehavior.GROW));
				}
				for (int i = 0; i < 10; i++) {
					Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.3, 0, 0);
					particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
					particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
					MMParticle.SNOWFLAKE.spawn(world, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x, particlePos.y, particlePos.z));
				}
			}
			setDead();
		}
	}
}
