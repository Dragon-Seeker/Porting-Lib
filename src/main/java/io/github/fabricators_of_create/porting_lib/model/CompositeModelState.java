package io.github.fabricators_of_create.porting_lib.model;

import com.google.common.base.Objects;
import com.mojang.math.Transformation;

import io.github.fabricators_of_create.porting_lib.extensions.ModelStateExtensions;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.ModelState;

/**
 * An {@link ModelState} that combines the transforms from two child {@link ModelState}.
 */
public class CompositeModelState implements ModelState, ModelStateExtensions {
	private final ModelState first;
	private final ModelState second;
	private final boolean uvLock;

	public CompositeModelState(ModelState first, ModelState second) {
		this(first, second, false);
	}

	public CompositeModelState(ModelState first, ModelState second, boolean uvLock) {
		this.first = first;
		this.second = second;
		this.uvLock = uvLock;
	}

	@Override
	public boolean isUvLocked() {
		return uvLock;
	}

	@Override
	public Transformation getRotation() {
		return first.getRotation().compose(second.getRotation());
	}

	@Override
	public Transformation getPartTransformation(Object part) {
		return ((ModelStateExtensions) first).getPartTransformation(part).compose(((ModelStateExtensions) second).getPartTransformation(part));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CompositeModelState that = (CompositeModelState) o;
		return Objects.equal(first, that.first) && Objects.equal(second, that.second);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(first, second);
	}
}
