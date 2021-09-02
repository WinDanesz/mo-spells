package com.windanesz.mospells.spell;

import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.entity.EntityMagicIceBreath;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.data.IVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class FrostBreath extends Spell {

	public static final IVariable<EntityMagicIceBreath> FROST_BREATH_KEY = new IVariable.Variable<>(Persistence.NEVER);

	public FrostBreath() {
		super(MoSpells.MODID, "frost_breath", SpellActions.POINT, true);
		addProperties(DAMAGE);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {

		if (WizardData.get(caster).getVariable(FROST_BREATH_KEY) == null) {

			EntityMagicIceBreath iceBreath = new EntityMagicIceBreath(world, caster);
			float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			iceBreath.setDamage(damage);
			iceBreath.setPositionAndRotation(caster.posX, caster.posY + caster.getEyeHeight() - 0.3f, caster.posZ, caster.rotationYaw, caster.rotationPitch);

			WizardData.get(caster).setVariable(FROST_BREATH_KEY, iceBreath);
			if (!world.isRemote) {
				world.spawnEntity(iceBreath);
			}
		}

		return true;
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
