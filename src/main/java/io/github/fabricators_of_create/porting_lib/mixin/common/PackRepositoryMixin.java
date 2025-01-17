package io.github.fabricators_of_create.porting_lib.mixin.common;

import io.github.fabricators_of_create.porting_lib.event.common.AddPackFindersCallback;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;

import net.minecraft.server.packs.repository.RepositorySource;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(PackRepository.class)
public class PackRepositoryMixin {
	@Shadow
	@Final
	private Set<RepositorySource> sources;

	@Inject(method = "<init>(Lnet/minecraft/server/packs/PackType;[Lnet/minecraft/server/packs/repository/RepositorySource;)V", at = @At("TAIL"))
	public void port_lib$addModdedPacks(PackType packType, RepositorySource[] repositorySources, CallbackInfo ci) {
		AddPackFindersCallback.EVENT.invoker().addPack(packType, sources::add);
	}
}
