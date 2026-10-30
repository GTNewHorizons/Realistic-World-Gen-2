package rwg2.mixin;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import rwg2.support.WitcheryWorldgenCompat;

/** Stops all standalone Witchery components from leveling or bridging protected RWG terrain. */
@Pseudo
@Mixin(targets = "com.emoniph.witchery.worldgen.WitcheryComponent", remap = false)
public abstract class MixinWitcheryComponent {

    @Inject(method = "calcGroundHeight", at = @At("HEAD"), cancellable = true, remap = false)
    private void rwg2$rejectProtectedTerrain(World world, StructureBoundingBox bounds,
            CallbackInfoReturnable<Integer> callback) {
        if (WitcheryWorldgenCompat.intersectsProtectedTerrain(world, bounds)) {
            callback.setReturnValue(-1);
        }
    }
}
