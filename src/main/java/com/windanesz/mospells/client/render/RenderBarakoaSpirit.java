package com.windanesz.mospells.client.render;

import com.bobmowzie.mowziesmobs.client.render.entity.RenderBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.windanesz.mospells.MoSpells;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.EnumMap;

public class RenderBarakoaSpirit extends RenderBarakoa {
	private static final EnumMap<MaskType, ResourceLocation> TEXTURES = MaskType.newEnumMap(ResourceLocation.class);

	public RenderBarakoaSpirit(RenderManager mgr) {
		super(mgr);
	}

	@Override
	protected void preRenderCallback(EntityBarakoa entity, float partialTickTime) {
		super.preRenderCallback(entity, partialTickTime);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1, 1, 1, 0.5f);
	}

	static {
		for (MaskType mask : MaskType.values()) {
			TEXTURES.put(mask, new ResourceLocation(MoSpells.MODID, "textures/entity/barakoa_spirit_" + mask.name + ".png"));
		}
	}

	@Override
	protected float getDeathMaxRotation(EntityBarakoa entity) {
		return 0;
	}

	@Override
	public ResourceLocation getEntityTexture(EntityBarakoa entity) {
		return TEXTURES.get(entity.getMask());
	}
}

