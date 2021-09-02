package com.windanesz.mospells.registry;

import com.windanesz.mospells.MoSpells;
import com.windanesz.mospells.spell.FrostBreath;
import com.windanesz.mospells.spell.FrozenOrb;
import com.windanesz.mospells.spell.LesserGeomancy;
import com.windanesz.mospells.spell.NagaAspect;
import com.windanesz.mospells.spell.Pillar;
import com.windanesz.mospells.spell.PoisonBall;
import com.windanesz.mospells.spell.Rupture;
import com.windanesz.mospells.spell.SolarBeam;
import com.windanesz.mospells.spell.SolarFlare;
import com.windanesz.mospells.spell.SummonBarakoaSpirit;
import com.windanesz.mospells.spell.SummonFrostmaw;
import com.windanesz.mospells.spell.SummonNaga;
import com.windanesz.mospells.spell.SummonSpiritPack;
import com.windanesz.mospells.spell.SunStrike;
import com.windanesz.mospells.spell.SunsBlessing;
import com.windanesz.mospells.spell.Supernova;
import com.windanesz.mospells.spell.WindsOfWinter;
import electroblob.wizardry.spell.Spell;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@EventBusSubscriber
@ObjectHolder(MoSpells.MODID)
public final class MSSpells {

	private MSSpells() {} // no instances

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	public static final Spell frost_breath = placeholder();
	public static final Spell frozen_orb = placeholder();
	public static final Spell lesser_geomancy = placeholder();
	public static final Spell naga_aspect = placeholder();
	public static final Spell pillar = placeholder();
	public static final Spell rupture = placeholder();
	public static final Spell solar_beam = placeholder();
	public static final Spell solar_flare = placeholder();
	public static final Spell summon_barakoa_spirit = placeholder();
	public static final Spell summon_frostmaw = placeholder();
	public static final Spell summon_naga = placeholder();
	public static final Spell summon_spirit_pack = placeholder();
	public static final Spell suns_blessing = placeholder();
	public static final Spell sunstrike = placeholder();
	public static final Spell supernova = placeholder();
	public static final Spell winds_of_winter = placeholder();

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event) {

		IForgeRegistry<Spell> registry = event.getRegistry();

		// 1.0.0 Spells
		registry.register(new FrostBreath());
		registry.register(new FrozenOrb());
		registry.register(new LesserGeomancy());
		registry.register(new NagaAspect());
		registry.register(new Pillar());
		registry.register(new PoisonBall());
		registry.register(new Rupture());
		registry.register(new SolarBeam());
		registry.register(new SolarFlare());
		registry.register(new SummonBarakoaSpirit());
		registry.register(new SummonFrostmaw());
		registry.register(new SummonNaga());
		registry.register(new SummonSpiritPack());
		registry.register(new SunStrike());
		registry.register(new SunsBlessing());
		registry.register(new Supernova());
		registry.register(new WindsOfWinter());
	}
}
