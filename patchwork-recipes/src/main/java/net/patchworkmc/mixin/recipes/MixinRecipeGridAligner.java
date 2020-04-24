/*
 * Minecraft Forge, Patchwork Project
 * Copyright (c) 2016-2020, 2019-2020
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.patchworkmc.mixin.recipes;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraftforge.common.crafting.IShapedRecipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeGridAligner;

@Mixin(RecipeGridAligner.class)
public interface MixinRecipeGridAligner {
	@ModifyVariable(
			method = "alignRecipeToGrid",
			at = @At("HEAD"),
			ordinal = 0
	)
	default int modifyGridWidth(int gridWidth, int gridHeight, int gridOutputSlot, Recipe<?> recipe, Iterator<?> inputs, int amount) {
		if (recipe instanceof IShapedRecipe) {
			return ((IShapedRecipe<?>) recipe).getRecipeWidth();
		}

		return gridWidth;
	}

	@ModifyVariable(
			method = "alignRecipeToGrid",
			at = @At("HEAD"),
			ordinal = 1
	)
	default int modifyGridHeight(int gridWidth, int gridHeight, int gridOutputSlot, Recipe<?> recipe, Iterator<?> inputs, int amount) {
		if (recipe instanceof IShapedRecipe) {
			return ((IShapedRecipe<?>) recipe).getRecipeHeight();
		}

		return gridHeight;
	}
}
