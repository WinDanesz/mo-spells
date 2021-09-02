package com.windanesz.mospells.entity;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityRupture extends EntityAxeAttack {

	public EntityRupture(World world) {
		super(world);
	}

	public EntityRupture(World world, EntityLivingBase caster, boolean vertical) {
		super(world, caster, vertical);
	}

}
