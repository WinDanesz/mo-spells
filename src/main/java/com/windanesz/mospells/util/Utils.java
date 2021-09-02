package com.windanesz.mospells.util;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.entity.Entity;

public final class Utils {
	private Utils() {}

	/**
	 * Adapted from {@link MowzieEntity#getAngleBetweenEntities(net.minecraft.entity.Entity, net.minecraft.entity.Entity)}
	 * Author: Bob Mowzie
	 */
	public static double getAngleBetweenEntities(Entity first, Entity second) {
		return Math.atan2(second.posZ - first.posZ, second.posX - first.posX) * (180 / Math.PI) + 90;
	}
}
