package dynamic_fps.impl.mixin;

import java.util.List;
import java.util.Locale;

import dynamic_fps.impl.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import dynamic_fps.impl.DynamicFPSMod;
import dynamic_fps.impl.PowerState;
import net.minecraft.client.gui.components.DebugScreenOverlay;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
	/**
	 * Show the current power state and effective frame rate below the FPS counter, unless focused.
	 *
	 * As we only slow the client loop to a minimum of 15 TPS the vanilla frame rate counter is inaccurate and confusing.
	 */
	@ModifyReturnValue(method = "getGameInformation", at = @At("RETURN"))
	private List<String> getGameInformation(List<String> result) {
		if (DynamicFPSMod.isDisabled()) {
			String reason = DynamicFPSMod.whyIsTheModNotWorking();
			result.add(2, this.dynamic_fps$format("§c[Dynamic FPS] Inactive! Reason: %s§r", reason));
		} else {
			PowerState status = DynamicFPSMod.powerState();

			if (status != PowerState.FOCUSED) {
				int fps = DynamicFPSMod.targetFrameRate();

				String vsync = DynamicFPSMod.enableVsync() ? " vsync": "";
				String target = fps == Constants.NO_FRAME_RATE_LIMIT ? "inf" : Integer.toString(fps);

				result.add(
					2,
					this.dynamic_fps$format("§c[Dynamic FPS] FPS: %s%s P: %s§r", target, vsync, status.toString().toLowerCase(Locale.ROOT))
				);
			}
		}

		return result;
	}

	@Unique
	private String dynamic_fps$format(String template, Object... args) {
		return String.format(Locale.ROOT, template, args);
	}
}
