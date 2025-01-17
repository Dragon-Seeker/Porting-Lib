package io.github.fabricators_of_create.porting_lib.mixin.common;

import com.google.common.collect.Multimap;

import io.github.fabricators_of_create.porting_lib.event.common.ItemAttributeModifierCallback;
import io.github.fabricators_of_create.porting_lib.extensions.ItemStackExtensions;

import io.github.fabricators_of_create.porting_lib.item.ToolActionCheckingItem;
import io.github.fabricators_of_create.porting_lib.util.DamageableItem;
import io.github.fabricators_of_create.porting_lib.util.ToolAction;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.fabricators_of_create.porting_lib.item.CustomMaxCountItem;
import io.github.fabricators_of_create.porting_lib.util.INBTSerializable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements INBTSerializable<CompoundTag>, ItemStackExtensions {

	@Shadow
	public abstract CompoundTag save(CompoundTag compoundTag);

	@Shadow
	public abstract void setTag(@Nullable CompoundTag compoundTag);

	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract boolean hasTag();

	@Shadow
	private @Nullable CompoundTag tag;

	@Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
	public void port_lib$onGetMaxCount(CallbackInfoReturnable<Integer> cir) {
		ItemStack self = (ItemStack) (Object) this;
		Item item = self.getItem();
		if (item instanceof CustomMaxCountItem) {
			cir.setReturnValue(((CustomMaxCountItem) item).getItemStackLimit(self));
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		this.save(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.setTag(ItemStack.of(nbt).getTag());
	}

	@Unique
	@Override
	public boolean canPerformAction(ToolAction toolAction) {
		if (this.getItem() instanceof ToolActionCheckingItem checking) {
			return checking.canPerformAction((ItemStack) (Object) this, toolAction);
		}
		return false;
	}

	@Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
	public void port_lib$modifierItem(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
		ItemAttributeModifierCallback.EVENT.invoker().onItemStackModifiers((ItemStack) (Object) this, slot, cir.getReturnValue());
	}

	@Inject(method = "setDamageValue", at = @At("HEAD"), cancellable = true)
	public void port_lib$itemSetDamage(int damage, CallbackInfo ci) {
		if(getItem() instanceof DamageableItem damagableItem) {
			damagableItem.setDamage((ItemStack) (Object) this, damage);
			ci.cancel();
		}
	}

	@Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
	public void port_lib$itemMaxDamage(CallbackInfoReturnable<Integer> cir) {
		if(getItem() instanceof DamageableItem damagableItem) {
			cir.setReturnValue(damagableItem.getMaxDamage((ItemStack) (Object) this));
		}
	}

	@Inject(method = "getDamageValue", at = @At("HEAD"), cancellable = true)
	public void port_lib$itemDamage(CallbackInfoReturnable<Integer> cir) {
		if(getItem() instanceof DamageableItem damagableItem) {
			cir.setReturnValue(damagableItem.getDamage((ItemStack) (Object) this));
		}
	}
}
