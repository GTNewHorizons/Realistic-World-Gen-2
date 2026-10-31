package rwg2.mixin;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rwg2.data.PlayerSpeedData;

/** Applies the RWG2 flight multiplier to vanilla vertical creative-flight acceleration. */
@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {

    private static final double VANILLA_VERTICAL_FLIGHT_SPEED = 0.15d;

    @Shadow
    public MovementInput movementInput;

    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    private void rwg2$applyVerticalFlightSpeed(CallbackInfo callback) {
        EntityPlayerSP player = (EntityPlayerSP) (Object) this;
        if (!player.capabilities.isFlying) {
            return;
        }

        double extraAcceleration = VANILLA_VERTICAL_FLIGHT_SPEED * (PlayerSpeedData.getFlightMultiplier(player) - 1.0f);
        if (movementInput.jump) {
            player.motionY += extraAcceleration;
        }
        if (movementInput.sneak) {
            player.motionY -= extraAcceleration;
        }
    }
}
