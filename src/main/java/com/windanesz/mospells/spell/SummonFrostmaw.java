package com.windanesz.mospells.spell;

import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.entity.EntityFrostmawMinion;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SummonFrostmaw extends SpellMinion<EntityFrostmawMinion> {

	public static final IStoredVariable<Integer> SUMMON_FROSTMAW_LAST_USAGE_DAY = IStoredVariable.StoredVariable.ofInt("summonFrostmawLastUsageDay", Persistence.ALWAYS);

	public SummonFrostmaw() {
		super(MoSpells.MODID, "summon_frostmaw", EntityFrostmawMinion::new);
		this.soundValues(1, 1.1f, 0.1f);
		WizardData.registerStoredVariables(SUMMON_FROSTMAW_LAST_USAGE_DAY);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!(caster.getHeldItem(hand).getItem() instanceof ItemWand) || ((ItemWand) caster.getHeldItem(hand).getItem()).element != Element.ICE) {
			if (!world.isRemote) { caster.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".non_ice_wand"), true); }
			return false;
		}

		if (canUseToday(caster)) {
			if (super.cast(world, caster, hand, ticksInUse, modifiers)) {
				updateUsageData(caster);
				return true;
			}
		} else {
			if (!world.isRemote) { caster.sendStatusMessage(new TextComponentTranslation("cannot_use_spell_again_today"), true); }
		}
		return false;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers) {
		return false;
	}

	public void updateUsageData(EntityPlayer player) {
		WizardData data = WizardData.get(player);
		if (data != null) {
			int today = (int) (player.world.getWorldTime() / 24000L % 2147483647L);
			data.setVariable(SUMMON_FROSTMAW_LAST_USAGE_DAY, today);

		}
	}

	public boolean canUseToday(EntityPlayer player) {
		WizardData data = WizardData.get(player);
		if (data != null) {
			Integer lastUse = data.getVariable(SUMMON_FROSTMAW_LAST_USAGE_DAY);

			// first use
			if (lastUse == null) { return true;}

			// not the first use
			int today = (int) (player.world.getWorldTime() / 24000L % 2147483647L);
			return lastUse != today;
		}

		return true;
	}

	@Override
	protected void addMinionExtras(EntityFrostmawMinion minion, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
		// nerfing the frostmaw a bit with slowness
		if (!minion.world.isRemote) {
			minion.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 6000, 1));
		}
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
