package rwg2.mixin;

import java.util.Random;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import rwg2.support.WorldgenDiagnostics;

/** Traces BOP sacred-oak writes without changing chunk load or population order. */
@Pseudo
@Mixin(targets = "biomesoplenty.common.world.features.trees.WorldGenSacredOak")
public abstract class MixinBOPSacredOak {

    @Inject(method = { "generate", "func_76484_a" }, at = @At("HEAD"), remap = false)
    private void rwg2$beginTreeTrace(World world, Random random, int x, int y, int z,
            CallbackInfoReturnable<Boolean> callback) {
        WorldgenDiagnostics.beginTree("Biomes O' Plenty Sacred Oak", world, x, y, z);
    }

    @Inject(method = { "generate", "func_76484_a" }, at = @At("RETURN"), remap = false)
    private void rwg2$endTreeTrace(World world, Random random, int x, int y, int z,
            CallbackInfoReturnable<Boolean> callback) {
        WorldgenDiagnostics.endTree(callback.getReturnValue());
    }
}
