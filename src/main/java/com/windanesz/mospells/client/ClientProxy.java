package com.windanesz.mospells.client;

import com.bobmowzie.mowziesmobs.client.render.entity.RenderFrostmaw;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderIceBall;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderNaga;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderSuperNova;
import com.windanesz.mospells.CommonProxy;
import com.windanesz.mospells.client.render.RenderBarakoaSpirit;
import com.windanesz.mospells.entity.EntityBarakoaSpiritMinion;
import com.windanesz.mospells.entity.EntityFrostmawMinion;
import com.windanesz.mospells.entity.EntityMagicIceBall;
import com.windanesz.mospells.entity.EntityMagicIceBreath;
import com.windanesz.mospells.entity.EntityMagicSuperNova;
import com.windanesz.mospells.entity.EntityNagaMinion;
import com.windanesz.mospells.entity.EntityRupture;
import electroblob.wizardry.client.renderer.entity.RenderBlank;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public void init() {

	}

	public void registerRenderers() {

		RenderingRegistry.registerEntityRenderingHandler(EntityMagicIceBreath.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMagicIceBall.class, RenderIceBall::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNagaMinion.class, RenderNaga::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRupture.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFrostmawMinion.class, RenderFrostmaw::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBarakoaSpiritMinion.class, RenderBarakoaSpirit::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMagicSuperNova.class, RenderSuperNova::new);
	}

}