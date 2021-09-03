package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.registry.MSItems;
import com.windanesz.mospells.registry.MSSpells;
import com.windanesz.mospells.util.Utils;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class WindsOfWinter extends Spell {

	public WindsOfWinter() {
		super(MoSpells.MODID, "winds_of_winter", SpellActions.POINT, true);
		addProperties(DAMAGE);
	}

	@Override
	protected SoundEvent[] createSounds() {
		return this.createContinuousSpellSounds();
	}

	@Override
	protected void playSound(World world, EntityLivingBase entity, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds) {
		this.playSoundLoop(world, entity, ticksInUse);
	}

	@Override
	protected void playSound(World world, double x, double y, double z, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds) {
		this.playSoundLoop(world, x, y, z, ticksInUse, duration);
	}

	public void playWindSound(World world, EntityLivingBase entity, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds) {
		playSound(world, entity, ticksInUse, duration, modifiers, sounds);
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers) {
		playSound(world, caster, ticksInUse, -1, modifiers);
		return castWind(world, caster, hand, ticksInUse, modifiers);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return castWind(world, caster, hand, ticksInUse, modifiers);
	}

	public static boolean castWind(World world, EntityLivingBase caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		boolean hasCharm = caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) caster, MSItems.charm_cold_winds);

		((WindsOfWinter) MSSpells.winds_of_winter).playWindSound(world, caster, ticksInUse, -1, new SpellModifiers());

		if (hasCharm && caster.isBurning()) {
			caster.extinguish();
		}
		List<EntityLivingBase> entities = EntityUtils.getEntitiesWithinRadius(5, caster.posX, caster.posY, caster.posZ, world, EntityLivingBase.class);

		if (hasCharm && !world.isRemote) {
			List<BlockPos> nearbyBlocks = BlockUtils.getBlockSphere(caster.getPosition(), 5);
			for (BlockPos currPos : nearbyBlocks) {
				if (world.getBlockState(currPos).getBlock() == Blocks.FIRE) {
					if (BlockUtils.canBreakBlock(caster, world, currPos)) {
						world.setBlockToAir(currPos);
						world.playSound(null, currPos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 0.7F, 1.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);

					}
				}
			}
		}

		for (EntityLivingBase target : entities) {

			if (target == caster || AllyDesignationSystem.isAllied(caster, target)) {
				continue;
			}

			if (hasCharm && !world.isRemote) {
				EntityUtils.attackEntityWithoutKnockback(target, MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.FROST), 1.5f);
				if (target.isBurning()) {
					target.extinguish();
				}
			}

			target.addPotionEffect(new PotionEffect(WizardryPotions.frost, 40, 0));

			double angle = (Utils.getAngleBetweenEntities(caster, target) + 90) * Math.PI / 180;
			double distance = caster.getDistance(target) - 4;
			target.motionX += Math.min(1 / (distance * distance), 1) * -1 * Math.cos(angle);
			target.motionZ += Math.min(1 / (distance * distance), 1) * -1 * Math.sin(angle);
		}
		if (ticksInUse == -1 || (ticksInUse % 12 == 0) && world.isRemote) {
			int particleCount = 15;
			for (int i = 1; i <= particleCount; i++) {
				double yaw = i * 360 / particleCount;
				double speed = 0.9;
				double xSpeed = speed * Math.cos(Math.toRadians(yaw));
				double zSpeed = speed * Math.sin(Math.toRadians(yaw));
				MMParticle.CLOUD.spawn(world, caster.posX, caster.posY + 1f, caster.posZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, 0d, zSpeed, 0.75d, 0.75d, 1d, 1, 40d, 22, ParticleCloud.EnumCloudBehavior.GROW));
			}
			for (int i = 1; i <= particleCount; i++) {
				double yaw = i * 360 / particleCount;
				double speed = 0.65;
				double xSpeed = speed * Math.cos(Math.toRadians(yaw));
				double zSpeed = speed * Math.sin(Math.toRadians(yaw));
				MMParticle.CLOUD.spawn(world, caster.posX, caster.posY + 1f, caster.posZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, 0d, zSpeed, 0.75d, 0.75d, 1d, 1, 35d, 22, ParticleCloud.EnumCloudBehavior.GROW));
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
