package com.firemerald.fecore.client.gui.components;

import net.minecraft.resources.ResourceLocation;

public class WidgetSprites {
	private final ResourceLocation enabled, disabled, enabledFocused, disabledFocused;

	private static ResourceLocation convert(ResourceLocation texture) {
		return texture.withPath(name -> "textures/gui/sprites/" + name + ".png");
	}

	public WidgetSprites(ResourceLocation enabled, ResourceLocation disabled, ResourceLocation enabledFocused, ResourceLocation disabledFocused) {
		this.enabled = convert(enabled);
		this.disabled = convert(disabled);
		this.enabledFocused = convert(enabledFocused);
		this.disabledFocused = convert(disabledFocused);
	}

    public WidgetSprites(ResourceLocation unfocused, ResourceLocation focused) {
        this(unfocused, unfocused, focused, focused);
    }

    public WidgetSprites(ResourceLocation enabled, ResourceLocation disabled, ResourceLocation focused) {
        this(enabled, disabled, focused, disabled);
    }

    public ResourceLocation get(boolean enabled, boolean focused) {
        if (enabled) {
            return focused ? this.enabledFocused : this.enabled;
        } else {
            return focused ? this.disabledFocused : this.disabled;
        }
    }
}
