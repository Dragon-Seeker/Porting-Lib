package io.github.fabricators_of_create.porting_lib.mixin.common;

import io.github.fabricators_of_create.porting_lib.extensions.MapDecorationExtensions;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(MapDecoration.class)
public class MapDecorationMixin implements MapDecorationExtensions {
}
