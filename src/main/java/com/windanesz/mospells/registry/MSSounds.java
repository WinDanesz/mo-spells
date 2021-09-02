package com.windanesz.mospells.registry;

import com.windanesz.mospells.MoSpells;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(MoSpells.MODID)
@Mod.EventBusSubscriber(modid = MoSpells.MODID)
public class MSSounds {
	private MSSounds() {}

	public static final SoundEvent FROZEN_ORB_EXPLODE = createSound("frozen_orb_explode");
	public static final SoundEvent ARCTIC_RING_ACTIVATE = createSound("arctic_ring_activate");

	public static final SoundEvent ENTITY_BARAKOA_GHOST_ANGRY_1 = createSound("barakoa_ghost.angry1");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_ANGRY_2 = createSound("barakoa_ghost.angry2");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_ANGRY_3 = createSound("barakoa_ghost.angry3");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_ANGRY_4 = createSound("barakoa_ghost.angry4");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_ANGRY_5 = createSound("barakoa_ghost.angry5");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_ANGRY_6 = createSound("barakoa_ghost.angry6");

	public static final SoundEvent ENTITY_BARAKOA_GHOST_TALK_1 = createSound("barakoa_ghost.talk1");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_TALK_2 = createSound("barakoa_ghost.talk2");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_TALK_3 = createSound("barakoa_ghost.talk3");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_TALK_4 = createSound("barakoa_ghost.talk4");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_TALK_5 = createSound("barakoa_ghost.talk5");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_TALK_6 = createSound("barakoa_ghost.talk6");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_TALK_7 = createSound("barakoa_ghost.talk7");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_TALK_8 = createSound("barakoa_ghost.talk8");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_TALK_9 = createSound("barakoa_ghost.talk9");

	public static final SoundEvent ENTITY_BARAKOA_GHOST_HURT = createSound("barakoa_ghost.hurt");
	public static final SoundEvent ENTITY_BARAKOA_GHOST_DIE = createSound("barakoa_ghost.die");

	public static SoundEvent createSound(String name) {
		return createSound(MoSpells.MODID, name);
	}

	/**
	 * Creates a sound with the given name, to be read from {@code assets/[modID]/sounds.json}.
	 */
	public static SoundEvent createSound(String modID, String name) {
		// All the setRegistryName methods delegate to this one, it doesn't matter which you use.
		return new SoundEvent(new ResourceLocation(modID, name)).setRegistryName(name);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(FROZEN_ORB_EXPLODE);
		event.getRegistry().register(ARCTIC_RING_ACTIVATE);

		event.getRegistry().register(ENTITY_BARAKOA_GHOST_ANGRY_1);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_ANGRY_2);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_ANGRY_3);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_ANGRY_4);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_ANGRY_5);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_ANGRY_6);

		event.getRegistry().register(ENTITY_BARAKOA_GHOST_TALK_1);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_TALK_2);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_TALK_3);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_TALK_4);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_TALK_5);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_TALK_6);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_TALK_7);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_TALK_8);
		event.getRegistry().register(ENTITY_BARAKOA_GHOST_TALK_9);

		event.getRegistry().register(ENTITY_BARAKOA_GHOST_HURT);

		event.getRegistry().register(ENTITY_BARAKOA_GHOST_DIE);
	}

}