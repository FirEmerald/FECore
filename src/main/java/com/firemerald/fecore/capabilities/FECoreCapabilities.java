package com.firemerald.fecore.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class FECoreCapabilities
{
	public static final Capability<IShapeHolder> SHAPE_HOLDER = CapabilityManager.get(new CapabilityToken<>(){});
	public static final Capability<IShapeTool> SHAPE_TOOL = CapabilityManager.get(new CapabilityToken<>(){});
}