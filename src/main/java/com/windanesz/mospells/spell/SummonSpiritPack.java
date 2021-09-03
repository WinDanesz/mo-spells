package com.windanesz.mospells.spell;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.entity.EntityBarakoaSpiritMinion;
import com.windanesz.mospells.registry.MSItems;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class SummonSpiritPack extends SpellMinion<EntityBarakoaSpiritMinion> {

	public SummonSpiritPack() {
		super(MoSpells.MODID, "summon_spirit_pack", EntityBarakoaSpiritMinion::new);
		this.soundValues(1, 1.1f, 0.1f);
	}

	@Override
	protected void addMinionExtras(EntityBarakoaSpiritMinion minion, BlockPos pos,
			@Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
		if (caster instanceof EntityPlayer) {
			MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(caster, MowziePlayerProperties.class);
			minion.setWeapon(minion.randomizeWeapon());
			property.addPackMember(minion);

			int count = getProperty(MINION_COUNT).intValue() - 1;
			if (alreadySpawned == count) {

				EntityBarakoaSpiritMinion barakoana = new EntityBarakoaSpiritMinion(minion.world, (EntityPlayer) caster);
				barakoana.setCaster(caster);
				// In this case we don't care whether the minions can fly or not.
				barakoana.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				barakoana.setLifetime((int) (getProperty(MINION_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
				IAttributeInstance attribute = barakoana.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
				if (attribute != null) {
					attribute.applyModifier( // Apparently some things don't have an attack damage
							new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.get(SpellModifiers.POTENCY) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
				}
				// This is only used for artefacts, but it's a nice example of custom spell modifiers
				barakoana.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(
						new AttributeModifier(HEALTH_MODIFIER, modifiers.get(HEALTH_MODIFIER) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
				barakoana.setHealth(barakoana.getMaxHealth()); // Need to set this because we may have just modified the value
				minion.setDropItemsWhenDead(false);
				property.addPackMember(barakoana);
				barakoana.setMask(MaskType.FURY);

				if (!minion.world.isRemote) {
					minion.world.spawnEntity(barakoana);
				}
			}
		}
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return true;
	}

	@Override
	public boolean applicableForItem(Item item) { return item == MSItems.mospells_spell_book || item == MSItems.mospells_scroll; }
}
