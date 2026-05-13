package com.musclemod.client;

import com.musclemod.MuscleMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model that loads the muscle geometry JSON.
 * The actual player skin is applied separately via the layer renderer,
 * so this model only handles shape — the skin texture from any skin works.
 */
public class MusclePlayerModel<T extends GeoAnimatable> extends GeoModel<T> {

    private static final ResourceLocation MODEL   = new ResourceLocation(MuscleMod.MOD_ID, "geo/muscle_player.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/entity/steve.png"); // fallback, overridden in layer
    private static final ResourceLocation ANIM    = new ResourceLocation(MuscleMod.MOD_ID, "animations/muscle_player.animation.json");

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ANIM;
    }
}
