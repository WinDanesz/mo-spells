package com.windanesz.mospells.entity;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.windanesz.mospells.registry.MSSounds;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.ParticleBuilder;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityBarakoaSpiritMinion extends EntityBarakoanToPlayer implements ISummonedCreature {

	public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKOA_GHOST_ANGRY = ImmutableList.of(
			() -> MSSounds.ENTITY_BARAKOA_GHOST_ANGRY_1,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_ANGRY_2,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_ANGRY_3,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_ANGRY_4,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_ANGRY_5,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_ANGRY_6
	);
	public static final ImmutableList<Supplier<SoundEvent>> ENTITY_BARAKOA_GHOST_TALK = ImmutableList.of(
			() -> MSSounds.ENTITY_BARAKOA_GHOST_TALK_1,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_TALK_2,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_TALK_3,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_TALK_4,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_TALK_5,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_TALK_6,
			() -> MSSounds.ENTITY_BARAKOA_GHOST_TALK_7
	);
	private UUID casterUUID;

	// Field implementations
	private int lifetime = -1;

	public EntityBarakoaSpiritMinion(World world) {
		super(world);
		this.experienceValue = 0;
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 0, false, true, this.getTargetSelector()));
	}

	public EntityBarakoaSpiritMinion(World world, EntityPlayer caster) {
		super(world, caster);
	}

	// Setter + getter implementations
	@Override
	public int getLifetime() { return lifetime; }

	@Override
	public void setLifetime(int lifetime) { this.lifetime = lifetime; }

	@Override
	public void setOwnerId(UUID uuid) { this.casterUUID = uuid; }

	@Nullable
	@Override

	// Provides override for both ISummonedCreature getOwnerId and IEntityOwnable getOwnerId
	public UUID getOwnerId() {
		return casterUUID;
	}

	@Nullable
	@Override
	public Entity getOwner() {
		return ((ISummonedCreature) this).getCaster();
	}

	@Override
	public void setRevengeTarget(EntityLivingBase entity) {
		if (this.shouldRevengeTarget(entity)) { super.setRevengeTarget(entity); }
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	public void onSpawn() {
		this.spawnParticleEffect();
	}

	@Override
	public void onDespawn() {
		this.spawnParticleEffect();
	}

	private void spawnParticleEffect() {
		if (this.world.isRemote) {
			for (int i = 0; i < 15; i++) {
				ParticleBuilder.create(ParticleBuilder.Type.DARK_MAGIC)
						.pos(this.posX + this.rand.nextFloat(), this.posY + this.rand.nextFloat(), this.posZ + this.rand.nextFloat())
						.clr(0.1f, 0.2f, 0.0f)
						.spawn(world);
			}
		}
	}

	@Override
	public boolean hasParticleEffect() {
		return true;
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		// In this case, the delegate method determines whether super is called.
		// Rather handily, we can make use of Java's short-circuiting method of evaluating OR statements.
		return this.interactDelegate(player, hand) || super.processInteract(player, hand);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		this.writeNBTDelegate(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.readNBTDelegate(nbttagcompound);
	}

	// Recommended overrides

	@Override
	protected int getExperiencePoints(EntityPlayer player) { return 0; }

	@Override
	protected boolean canDropLoot() { return false; }

	@Override
	protected Item getDropItem() { return null; }

	@Override
	protected ResourceLocation getLootTable() { return null; }

	@Override
	public boolean canPickUpLoot() { return false; }

	// This vanilla method has nothing to do with the custom despawn() method.
	@Override
	protected boolean canDespawn() {
		return getCaster() == null && getOwnerId() == null;
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	@Override
	public ITextComponent getDisplayName() {
		if (getCaster() != null) {
			return new TextComponentTranslation(NAMEPLATE_TRANSLATION_KEY, getCaster().getName(),
					new TextComponentTranslation("entity." + this.getEntityString() + ".name"));
		} else {
			return super.getDisplayName();
		}
	}

	@Nullable
	@Override
	public EntityLivingBase getCaster() {
		if (this instanceof Entity) { // Bit of a cheat but it saves having yet another method just to get the world

			Entity entity = EntityUtils.getEntityByUUID(((Entity) this).world, (((ISummonedCreature) this).getOwnerId()));

			if (entity != null && !(entity instanceof EntityLivingBase)) { // Should never happen
				Wizardry.logger.warn("{} has a non-living owner!", this);
				return null;
			}

			return (EntityLivingBase) entity;

		} else {
			Wizardry.logger.warn("{} implements ISummonedCreature but is not an SoundLoopSpellEntity!", this.getClass());
			return null;
		}
	}

	@Override
	public boolean hasCustomName() {
		// If this returns true, the renderer will show the nameplate when looking directly at the entity
		return Wizardry.settings.summonedCreatureNames && getCaster() != null;
	}

	@Override
	/**
	 * Author: Bob Mowzie
	 */
	protected SoundEvent getAmbientSound() {
		if (getAnimation() == DEACTIVATE_ANIMATION) {
			return null;
		}
		if (!active || getDancing() || (getEntitiesNearby(EntityBarakoa.class, 8, 3, 8, 8).isEmpty() && getEntitiesNearby(EntityBarako.class, 8, 3, 8, 8).isEmpty() && getEntitiesNearby(EntityPlayer.class, 8, 3, 8, 8).isEmpty())) {
			return null;
		}
		if (getAttackTarget() == null) {
			int i = MathHelper.getInt(rand, 0, 11);
			if (i < ENTITY_BARAKOA_GHOST_TALK.size()) {
				playSound(ENTITY_BARAKOA_GHOST_TALK.get(i).get(), 1, 1.5f);
				AnimationHandler.INSTANCE.sendAnimationMessage(this, IDLE_ANIMATION);
			}
		} else {
			int i = MathHelper.getInt(rand, 0, 7);
			if (i < ENTITY_BARAKOA_GHOST_ANGRY.size()) {
				playSound(ENTITY_BARAKOA_GHOST_ANGRY.get(i).get(), 1, 1.6f);
			}
		}
		return null;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return active ? MSSounds.ENTITY_BARAKOA_GHOST_HURT : null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return MSSounds.ENTITY_BARAKOA_GHOST_DIE;
	}
}
