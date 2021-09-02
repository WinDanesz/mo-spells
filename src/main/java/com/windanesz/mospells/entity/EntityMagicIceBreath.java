package com.windanesz.mospells.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBlockSwapper;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityMagicEffect;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.windanesz.mospells.registry.MSItems;
import com.windanesz.mospells.registry.MSSpells;
import com.windanesz.mospells.spell.FrostBreath;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

/**
 * This class is a slightly modified version of {@link com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath} by WinDanesz
 * Author: Created by Josh on 5/25/2017. (Mowzie's Mobs)
 * Refer to LICENSE.md for more details
 */
public class EntityMagicIceBreath extends EntityMagicEffect {
	private static final int RANGE = 10;
	private static final int ARC = 45;
	private static final int DAMAGE_PER_HIT = 1;

	private float dam = -1;

	public void setDamage(float damage) {
		this.dam = damage;
	}

	public float getDamage() {
		return dam == -1 ? MSSpells.frozen_orb.getProperty(Spell.DAMAGE).floatValue() : dam;
	}

	public EntityMagicIceBreath(World world) {
		super(world);
		setSize(0, 0);
	}

	public EntityMagicIceBreath(World world, EntityLivingBase caster) {
		super(world);
		setSize(0, 0);
		if (!world.isRemote) {
			this.setCasterID(caster.getEntityId());
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (caster == null) {
			this.setDead();
		}

		if (ticksExisted == 1) {
			if (world.isRemote) {
				MowziesMobs.PROXY.playIceBreathSound(this);
			}
		}
		if (caster != null && caster.isDead) { this.setDead(); }
		if (ticksExisted == 1) { playSound(MMSounds.ENTITY_FROSTMAW_ICEBREATH_START, 1, 0.6f); }
		if (caster instanceof EntityPlayer) {
			rotationYaw = ((EntityPlayer) caster).rotationYaw;
			rotationPitch = ((EntityPlayer) caster).rotationPitch;
			posX = ((EntityPlayer) caster).posX;
			posY = ((EntityPlayer) caster).posY + ((EntityPlayer) caster).eyeHeight - 0.5f;
			posZ = ((EntityPlayer) caster).posZ;
		}

		float yaw = (float) Math.toRadians(-rotationYaw);
		float pitch = (float) Math.toRadians(-rotationPitch);
		float spread = 0.25f;
		float speed = 0.56f;
		float xComp = (float) (Math.sin(yaw) * Math.cos(pitch));
		float yComp = (float) (Math.sin(pitch));
		float zComp = (float) (Math.cos(yaw) * Math.cos(pitch));
		if (world.isRemote) {
			if (ticksExisted % 8 == 0) {
				if (world.isRemote) {
					MMParticle.RING.spawn(world, posX, posY, posZ, ParticleFactory.ParticleArgs.get().withData(yaw, -pitch, 40, 1f, 1f, 1f, 1f, 110f * spread, false, 0.5f * xComp, 0.5f * yComp, 0.5f * zComp));
				}
			}

			for (int i = 0; i < 6; i++) {
				double xSpeed = speed * 1f * xComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(xComp)));
				double ySpeed = speed * 1f * yComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(yComp)));
				double zSpeed = speed * 1f * zComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(zComp)));
				MMParticle.SNOWFLAKE.spawn(world, posX, posY, posZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 37d, 1d));
			}
			for (int i = 0; i < 5; i++) {
				double xSpeed = speed * xComp + (spread * 0.7 * (rand.nextFloat() * 2 - 1) * (Math.sqrt(1 - xComp * xComp)));
				double ySpeed = speed * yComp + (spread * 0.7 * (rand.nextFloat() * 2 - 1) * (Math.sqrt(1 - yComp * yComp)));
				double zSpeed = speed * zComp + (spread * 0.7 * (rand.nextFloat() * 2 - 1) * (Math.sqrt(1 - zComp * zComp)));
				double value = rand.nextFloat() * 0.15f;
				MMParticle.CLOUD.spawn(world, posX, posY, posZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 0.75d + value, 0.75d + value, 1d, 1, 10d + rand.nextDouble() * 20d, 40, ParticleCloud.EnumCloudBehavior.GROW));
			}
		}
		if (ticksExisted > 10) { hitEntities(); }
		if (ticksExisted > 10) { freezeBlocks(); }

		//noinspection ConstantConditions
		if (!caster.isHandActive() || !(caster.getHeldItem(caster.getActiveHand()).getItem() instanceof ISpellCastingItem)) {
			WizardData.get((EntityPlayer) caster).setVariable(FrostBreath.FROST_BREATH_KEY, null);
			this.setDead();
		}
		//		if (ticksExisted > 65 && !(caster instanceof EntityPlayer)) { setDead(); }
	}

	public void hitEntities() {

		// artifact effect
		if (!world.isRemote && caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) caster, MSItems.ring_ice_crystal)) {

			List<Entity> entities = getEntitiesNearby(Entity.class, RANGE, RANGE, RANGE, RANGE);

			for (Entity entityHit : entities) {
				if (entityHit instanceof IProjectile) {
					Vec3d centre = caster.getPositionEyes(0).subtract(0, 0.1, 0);
					Vec3d vec = entityHit.getPositionVector().subtract(centre).normalize().scale(0.2);
					entityHit.addVelocity(vec.x, vec.y, vec.z);
				}
			}

		}

		List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(RANGE, RANGE, RANGE, RANGE);
		for (EntityLivingBase entityHit : entitiesHit) {
			if (entityHit == caster) { continue; }

			List<String> freezeImmune = Arrays.asList(ConfigHandler.GENERAL.freeze_blacklist);

			ResourceLocation mobName = EntityList.getKey(entityHit);
			if (mobName != null && freezeImmune.contains(mobName.toString())) { continue; }

			float entityHitYaw = (float) ((Math.atan2(entityHit.posZ - posZ, entityHit.posX - posX) * (180 / Math.PI) - 90) % 360);
			float entityAttackingYaw = rotationYaw % 360;
			if (entityHitYaw < 0) {
				entityHitYaw += 360;
			}
			if (entityAttackingYaw < 0) {
				entityAttackingYaw += 360;
			}
			float entityRelativeYaw = entityHitYaw - entityAttackingYaw;

			float xzDistance = (float) Math.sqrt((entityHit.posZ - posZ) * (entityHit.posZ - posZ) + (entityHit.posX - posX) * (entityHit.posX - posX));
			float entityHitPitch = (float) ((Math.atan2((entityHit.posY - posY), xzDistance) * (180 / Math.PI)) % 360);
			float entityAttackingPitch = -rotationPitch % 360;
			if (entityHitPitch < 0) {
				entityHitPitch += 360;
			}
			if (entityAttackingPitch < 0) {
				entityAttackingPitch += 360;
			}
			float entityRelativePitch = entityHitPitch - entityAttackingPitch;

			float entityHitDistance = (float) Math.sqrt((entityHit.posZ - posZ) * (entityHit.posZ - posZ) + (entityHit.posX - posX) * (entityHit.posX - posX) + (entityHit.posY - posY) * (entityHit.posY - posY));

			boolean inRange = entityHitDistance <= RANGE;
			boolean yawCheck = (entityRelativeYaw <= ARC / 2f && entityRelativeYaw >= -ARC / 2f) || (entityRelativeYaw >= 360 - ARC / 2f || entityRelativeYaw <= -360 + ARC / 2f);
			boolean pitchCheck = (entityRelativePitch <= ARC / 2f && entityRelativePitch >= -ARC / 2f) || (entityRelativePitch >= 360 - ARC / 2f || entityRelativePitch <= -360 + ARC / 2f);
			if (inRange && yawCheck && pitchCheck) {

				if (MagicDamage.isEntityImmune(MagicDamage.DamageType.FROST, entityHit)) {
					if (!world.isRemote && ticksExisted % 20 == 0 && caster instanceof EntityPlayer) {
						((EntityPlayer) caster)
								.sendStatusMessage(new TextComponentTranslation("spell.resist", entityHit.getName(),
										MSSpells.frost_breath.getNameForTranslationFormatted()), true);
					}
				} else {
					if (entityHit.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), getDamage())) {
						entityHit.motionZ *= 0.5;
						entityHit.motionX *= 0.5;
						entityHit.addPotionEffect(new PotionEffect(WizardryPotions.frost, 20, 0));
						MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entityHit, MowzieLivingProperties.class);
						if (property != null) { property.addFreezeProgress(entityHit, 0.23f); }
					}
				}
			}
		}
	}

	public void freezeBlocks() {
		int checkDist = 10;
		for (int i = (int) posX - checkDist; i < (int) posX + checkDist; i++) {
			for (int j = (int) posY - checkDist; j < (int) posY + checkDist; j++) {
				for (int k = (int) posZ - checkDist; k < (int) posZ + checkDist; k++) {
					BlockPos pos = new BlockPos(i, j, k);

					IBlockState blockState = world.getBlockState(pos);
					IBlockState blockStateAbove = world.getBlockState(pos.up());
					if (blockState.getBlock() != Blocks.WATER || blockStateAbove.getBlock() != Blocks.AIR) {
						continue;
					}

					float blockHitYaw = (float) ((Math.atan2(pos.getZ() - posZ, pos.getX() - posX) * (180 / Math.PI) - 90) % 360);
					float entityAttackingYaw = rotationYaw % 360;
					if (blockHitYaw < 0) {
						blockHitYaw += 360;
					}
					if (entityAttackingYaw < 0) {
						entityAttackingYaw += 360;
					}
					float blockRelativeYaw = blockHitYaw - entityAttackingYaw;

					float xzDistance = (float) Math.sqrt((pos.getZ() - posZ) * (pos.getZ() - posZ) + (pos.getX() - posX) * (pos.getX() - posX));
					float blockHitPitch = (float) ((Math.atan2((pos.getY() - posY), xzDistance) * (180 / Math.PI)) % 360);
					float entityAttackingPitch = -rotationPitch % 360;
					if (blockHitPitch < 0) {
						blockHitPitch += 360;
					}
					if (entityAttackingPitch < 0) {
						entityAttackingPitch += 360;
					}
					float blockRelativePitch = blockHitPitch - entityAttackingPitch;

					float blockHitDistance = (float) Math.sqrt((pos.getZ() - posZ) * (pos.getZ() - posZ) + (pos.getX() - posX) * (pos.getX() - posX) + (pos.getY() - posY) * (pos.getY() - posY));

					boolean inRange = blockHitDistance <= RANGE;
					boolean yawCheck = (blockRelativeYaw <= ARC / 2f && blockRelativeYaw >= -ARC / 2f) || (blockRelativeYaw >= 360 - ARC / 2f || blockRelativeYaw <= -360 + ARC / 2f);
					boolean pitchCheck = (blockRelativePitch <= ARC / 2f && blockRelativePitch >= -ARC / 2f) || (blockRelativePitch >= 360 - ARC / 2f || blockRelativePitch <= -360 + ARC / 2f);
					if (inRange && yawCheck && pitchCheck) {
						EntityBlockSwapper.swapBlock(world, pos, Blocks.ICE.getDefaultState(), 140, false, false);
					}
				}
			}
		}
	}

	public List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
		return getEntitiesNearby(EntityLivingBase.class, distanceX, distanceY, distanceZ, radius);
	}

	public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
		return world.getEntitiesWithinAABB(entityClass, getEntityBoundingBox().grow(dX, dY, dZ), e -> e != this && getDistance(e) <= r + e.width / 2f && e.posY <= posY + dY);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}
}
