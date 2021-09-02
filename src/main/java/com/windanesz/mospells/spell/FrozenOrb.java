package com.windanesz.mospells.spell;

import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.entity.EntityMagicIceBall;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FrozenOrb extends Spell {

	public static final String ICE_SHARDS = "ice_shards";

	public FrozenOrb() {
		super(MoSpells.MODID, "frozen_orb", SpellActions.POINT, false);
		addProperties(DAMAGE, ICE_SHARDS);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		Vec3d projectilePos = new Vec3d(1.0, 1, 0);
		projectilePos = projectilePos.rotateYaw((float) Math.toRadians(-caster.rotationYaw - 90));
		projectilePos = projectilePos.add(caster.getPositionVector());
		projectilePos = projectilePos.add(new Vec3d(0, 0, 1).rotatePitch((float) Math.toRadians(-caster.rotationPitch)).rotateYaw((float) Math.toRadians(-caster.rotationYawHead)));

		EntityMagicIceBall iceBall = new EntityMagicIceBall(world, caster);
		iceBall.setPositionAndRotation(projectilePos.x, projectilePos.y, projectilePos.z, caster.rotationYawHead, caster.rotationPitch + 10);
		float projSpeed = 0.3f;

		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		iceBall.setDamage(damage);
		iceBall.shoot(caster.getLookVec().x, caster.getLookVec().y, caster.getLookVec().z, projSpeed, 0);
		if (!world.isRemote) { world.spawnEntity(iceBall); }
		return true;
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
