package net.patchworkmc.mixin.extensions.block;

import java.util.Locale;

import net.minecraftforge.common.extensions.IForgeBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;

/**
 * See the comment in BaseToContextMapper in patchwork-extensions
 */
@Mixin(ChunkRegion.class)
public class MixinChunkRegion {
	@Unique
	private final ThreadLocal<BlockState> blockContext = ThreadLocal.withInitial(() -> null);

	@Inject(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockEntityProvider;createBlockEntity(Lnet/minecraft/world/BlockView;)Lnet/minecraft/block/entity/BlockEntity;",
			ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void captureBE(BlockPos pos, BlockState state, int arg2, int arg3, CallbackInfoReturnable<Boolean> cir, Chunk chunk, BlockState blockState) {
		blockContext.set(state);
	}
	@Redirect(method = "Lnet/minecraft/world/ChunkRegion;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockEntityProvider;createBlockEntity(Lnet/minecraft/world/BlockView;)Lnet/minecraft/block/entity/BlockEntity;", ordinal = 0))
	private BlockEntity redirectCreateBlockEntity(BlockEntityProvider blockEntityProvider, BlockView world) {
		BlockEntity ret = ((IForgeBlockState) blockContext.get()).createTileEntity(world);
		blockContext.set(null);
		return ret;
	}
}