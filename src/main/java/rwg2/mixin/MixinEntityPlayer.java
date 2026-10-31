package rwg2.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rwg2.data.PlayerSpeedData;

/** Applies the synchronized /speed multiplier without replacing flight speeds supplied by other mods. */
@Mixin(value = EntityPlayer.class, priority = 900)
public abstract class MixinEntityPlayer extends EntityLivingBase {

    @Shadow
    public PlayerCapabilities capabilities;

    protected MixinEntityPlayer(World world) {
        super(world);
    }

    @Inject(
            method = "moveEntityWithHeading",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLivingBase;moveEntityWithHeading(FF)V",
                    ordinal = 0))
    private void rwg2$applyHorizontalFlightSpeed(float strafe, float forward, CallbackInfo callback) {
        if (capabilities.isFlying) {
            jumpMovementFactor *= PlayerSpeedData.getFlightMultiplier((EntityPlayer) (Object) this);
        }
    }
}
