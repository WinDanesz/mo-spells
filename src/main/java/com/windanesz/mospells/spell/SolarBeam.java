package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.data.IVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SolarBeam extends Spell {

	public static final IVariable<EntitySolarBeam> SOLAR_BEAM_KEY = new IVariable.Variable<>(Persistence.NEVER);

	public SolarBeam() {
		super(MoSpells.MODID, "solar_beam", SpellActions.POINT, true);
	}

	@Override
	public boolean cast(World world, EntityPlayer player, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (ticksInUse == 0  && WizardData.get(player).getVariable(SOLAR_BEAM_KEY) != null) {
			WizardData data = WizardData.get((EntityPlayer) player);
			if (data != null && data.getVariable(SOLAR_BEAM_KEY) != null) {
				EntitySolarBeam beamToRemove = data.getVariable(SOLAR_BEAM_KEY);
				if (beamToRemove != null) {
					beamToRemove.setDead();
				}
				data.setVariable(SOLAR_BEAM_KEY, null);
			}
		}
		if (ticksInUse > 10 && WizardData.get(player).getVariable(SOLAR_BEAM_KEY) == null) {

			EntitySolarBeam solarBeam = new EntitySolarBeam(world, player, player.posX, player.posY + 1.2f, player.posZ, (float) ((player.rotationYawHead + 90) * Math.PI / 180), (float) (-player.rotationPitch * Math.PI / 180), 55);
			solarBeam.setHasPlayer(true);

			WizardData.get(player).setVariable(SOLAR_BEAM_KEY, solarBeam);
			if (!world.isRemote) {
				world.spawnEntity(solarBeam);
			}
		}

		return true;
	}

	@Override
	public void finishCasting(World world,
			@Nullable EntityLivingBase caster, double x, double y, double z, @Nullable EnumFacing direction, int duration, SpellModifiers modifiers) {
		if (caster instanceof EntityPlayer) {
			WizardData data = WizardData.get((EntityPlayer) caster);
			if (data != null && data.getVariable(SOLAR_BEAM_KEY) != null) {
				EntitySolarBeam beamToRemove = data.getVariable(SOLAR_BEAM_KEY);
				if (beamToRemove != null) {
					beamToRemove.setDead();
				}
				data.setVariable(SOLAR_BEAM_KEY, null);
			}
		}
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
