package io.github.fabricators_of_create.porting_lib.mixin.common;

import io.github.fabricators_of_create.porting_lib.extensions.MobEffectExtensions;
import net.minecraft.world.effect.MobEffect;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEffect.class)
public class MobEffectMixin implements MobEffectExtensions {
}
