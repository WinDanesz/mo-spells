package com.windanesz.mospells.util;

import com.windanesz.mospells.registry.MSItems;
import com.windanesz.mospells.registry.MSSounds;
import com.windanesz.mospells.spell.WindsOfWinter;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class MSEventHandler {

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event) {

		if (event.getSource().getTrueSource() instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			ItemStack mainhandItem = player.getHeldItemMainhand();
			World world = player.world;

			for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)) {

				if (artefact == MSItems.ring_wind_touch && !player.getCooldownTracker().hasCooldown(MSItems.ring_wind_touch)) {

					if (EntityUtils.isMeleeDamage(event.getSource()) && mainhandItem.getItem() instanceof ItemWand
							&& ((ItemWand) mainhandItem.getItem()).element == Element.ICE) {

						player.world.playSound(null, player.getPosition(), MSSounds.ARCTIC_RING_ACTIVATE, SoundCategory.PLAYERS, 1, 1);

						WindsOfWinter.castWind(world, player, EnumHand.MAIN_HAND, -1, new SpellModifiers());
						player.getCooldownTracker().setCooldown(MSItems.ring_wind_touch, 120);
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onSpellCastEventPre(SpellCastEvent.Pre event) {
		if (event.getCaster() instanceof EntityPlayer) {

			if (!event.getWorld().isRemote) {
				for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts((EntityPlayer) event.getCaster())) {

					if (artefact == MSItems.amulet_frostmaw) {

						if (event.getSpell().getElement() == Element.ICE) {

							EntityPlayerMP playerMP = (EntityPlayerMP) event.getCaster();

							// artifact effect
							if (playerMP.getServer() != null) {

								// check frostmaw kill
								Advancement frostmawKill = playerMP.getServer().getAdvancementManager().getAdvancement(new ResourceLocation("mowziesmobs:kill_frostmaw"));
								if (frostmawKill != null && playerMP.getAdvancements().getProgress(frostmawKill).isDone()) {

									// 25% potency bonus
									float potency = event.getModifiers().get(SpellModifiers.POTENCY);
									event.getModifiers().set(SpellModifiers.POTENCY, 1.25f * potency, false);
									// 5 seconds of Ice Shroud bonus
									event.getCaster().addPotionEffect(new PotionEffect(WizardryPotions.ice_shroud, 120, 0));
								}
							}
						}
					} else if (artefact == MSItems.amulet_golden_sun) {

						if (event.getSpell().getElement() == Element.FIRE) {

							EntityPlayerMP playerMP = (EntityPlayerMP) event.getCaster();

							// artifact effect
							if (playerMP.getServer() != null) {

								// check Barako kill
								Advancement barakoKill = playerMP.getServer().getAdvancementManager().getAdvancement(new ResourceLocation("mowziesmobs:kill_barako"));
								if (barakoKill != null && playerMP.getAdvancements().getProgress(barakoKill).isDone()) {

									// 25% potency bonus
									float potency = event.getModifiers().get(SpellModifiers.POTENCY);
									event.getModifiers().set(SpellModifiers.POTENCY, 1.25f * potency, false);
									// 5 seconds of Fire Res and Fireskin
									event.getCaster().addPotionEffect(new PotionEffect(WizardryPotions.fireskin, 100, 0));
									event.getCaster().addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 100, 0));
								}
							}
						}
					}
				}

			}
		}
	}
}
