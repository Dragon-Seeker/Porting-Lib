package io.github.fabricators_of_create.porting_lib.mixin.common;

import io.github.fabricators_of_create.porting_lib.block.CustomExpBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SpawnerBlock;

import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpawnerBlock.class)
public abstract class SpawnerBlockMixin implements CustomExpBlock {

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.level.LevelReader world, BlockPos pos, int fortune, int silktouch) {
		return 15 + ((Level)world).getRandom().nextInt(15) + ((Level)world).getRandom().nextInt(15);
	}
}
