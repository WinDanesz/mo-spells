package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityRing;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

public class LesserGeomancy extends SpellRay {

	private static final Field travelling = ObfuscationReflectionHelper.findField(EntityBoulder.class, "travelling");
	private static final Field ridingEntities = ObfuscationReflectionHelper.findField(EntityBoulder.class, "ridingEntities");

	public LesserGeomancy() {
		super(MoSpells.MODID, "lesser_geomancy", SpellActions.POINT, false);
		this.soundValues(1, 1.1f, 0.2f);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (target instanceof EntityBoulder) {
			return spellHitBoulder((EntityBoulder) target, caster, 0);
		}
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return spawnBoulder(world, caster, pos, 0);
	}

	@Override
	protected boolean onMiss(World world, EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	/**
	 * Adapted from {@link com.bobmowzie.mowziesmobs.server.property.power.PowerGeomancy#isBlockDiggable(net.minecraft.block.state.IBlockState)} as we don't
	 * always have a player to get a "Power" instance and couldn't reference the non static method
	 * Author: Bob Mowzie
	 */
	public static boolean isBlockDiggable(IBlockState blockState) {
		Material mat = blockState.getMaterial();
		if (mat != Material.GRASS
				&& mat != Material.GROUND
				&& mat != Material.ROCK
				&& mat != Material.CLAY
				&& mat != Material.SAND
		) {
			return false;
		}
		if (blockState == Blocks.HAY_BLOCK
				|| blockState.getBlock() == Blocks.NETHER_WART_BLOCK
				|| blockState.getBlock() instanceof BlockFence
				|| blockState.getBlock() == Blocks.MOB_SPAWNER
				|| blockState.getBlock() == Blocks.BONE_BLOCK
				|| blockState.getBlock() == Blocks.ENCHANTING_TABLE
				|| blockState.getBlock() == Blocks.END_PORTAL_FRAME
				|| blockState.getBlock() == Blocks.ENDER_CHEST
				|| blockState.getBlock() == Blocks.SLIME_BLOCK
		) {
			return false;
		}
		return true;
	}

	public static boolean spawnBoulder(World world, EntityLivingBase caster, int size) {
		return spawnBoulder(world, caster, new BlockPos(caster.posX, caster.posY - 1, caster.posZ), size);
	}

	public static boolean spawnBoulder(World world, EntityLivingBase caster, BlockPos pos, int size) {
		if (!caster.onGround) { return false; }

		IBlockState spawnBoulderBlock = world.getBlockState(pos);

		if (!isBlockDiggable(spawnBoulderBlock)) { return false; }

		EntityBoulder boulder = new EntityBoulder(caster.world, caster, size, spawnBoulderBlock);
		boulder.setPosition(pos.getX() + 0.5F, pos.getY() + 2, pos.getZ() + 0.5F);
		if (!world.isRemote && boulder.checkCanSpawn()) {
			world.spawnEntity(boulder);
		}
		return true;
	}

	/**
	 * Adapted from {@link com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder#hitByEntity(net.minecraft.entity.Entity)}
	 * Author: Bob Mowzie, WinDanesz
	 */
	public static boolean spellHitBoulder(EntityBoulder boulder, EntityLivingBase entityIn, int maxSize) {
		if (boulder.ticksExisted < 10) {
			return false;
		}

		float speed = 1.5f;
		if (entityIn instanceof EntityPlayer) {
			boolean riding = false;
			EntityPlayer player = (EntityPlayer) entityIn;
			if (ItemArtefact.isArtefactActive(player, MSItems.amulet_earth)) {
				maxSize = 3;
			}

			if (boulder.boulderSize > maxSize) { return false; }

			try {
				boulder.setDeathTime(60);
				travelling.set(boulder, true);
				if (ridingEntities.get(boulder) instanceof List) {
					riding = ((List<?>) ridingEntities.get(boulder)).contains(player);
				}
			}
			catch (IllegalAccessException e) {
				MoSpells.logger.error("Error during reflective access of EntityBoulder properties: ", e);
				return false;
			}

			if (riding) {
				Vec3d lateralLookVec = Vec3d.fromPitchYaw(0, player.rotationYaw).normalize();
				boulder.motionX = speed * 0.5 * lateralLookVec.x;
				boulder.motionZ = speed * 0.5 * lateralLookVec.z;
			} else {
				boulder.motionX = speed * 0.5 * player.getLookVec().x;
				boulder.motionY = speed * 0.5 * player.getLookVec().y;
				boulder.motionZ = speed * 0.5 * player.getLookVec().z;
			}

			if (boulder.boulderSize == 0) {
				boulder.playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL, 1.5f, 1.3f);
				boulder.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1.5f, 0.9f);
			} else if (boulder.boulderSize == 1) {
				boulder.playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL, 1.5f, 0.9f);
				boulder.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1.5f, 0.5f);
			} else if (boulder.boulderSize == 2) {
				boulder.playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL, 1.5f, 0.5f);
				boulder.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG, 1.5f, 1.3f);
			} else if (boulder.boulderSize == 3) {
				boulder.playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1, 1.5f, 1f);
				boulder.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG, 1.5f, 0.9f);
			}

			Vec3d ringOffset = new Vec3d(boulder.motionX, boulder.motionY, boulder.motionZ).normalize().scale(-1);
			EntityRing ring = new EntityRing(entityIn.world, (float) boulder.posX + (float) ringOffset.x, (float) boulder.posY + 0.5f + (float) ringOffset.y, (float) boulder.posZ + (float) ringOffset.z, ringOffset.normalize(), (int) (4 + 1 * 1), 0.83f, 1, 0.39f, 1f, 1.0f + 0.5f * 1, false);
			entityIn.world.spawnEntity(ring);
		}
		return true;
	}

	@Override
	public boolean applicableForItem(Item item) {return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll;}
}