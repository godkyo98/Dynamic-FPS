package net.lostluma.dynamic_fps.impl.forge.service;

import dynamic_fps.impl.service.ModCompat;
import dynamic_fps.impl.service.Platform;
import net.minecraftforge.fml.ModList;

public class ForgeModCompat implements ModCompat {
	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public boolean disableOverlayOptimization() {
		return Platform.getInstance().isModLoaded("rrls");
	}
}
