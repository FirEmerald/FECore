package com.firemerald.fecore.client.gui.components;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.gui.IComponentHolder;

import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class InteractableComponent extends Component implements IInteractableComponent
{
	private boolean focused = false;
    private int tabOrderGroup;

	public InteractableComponent(int x1, int y1, int x2, int y2)
	{
		super(x1, y1, x2, y2);
	}

	public InteractableComponent(IComponentHolder parent, int x1, int y1, int x2, int y2)
	{
		super(parent, x1, y1, x2, y2);
	}

	@Override
	public boolean isMouseOver(double x, double y)
	{
		return super.isMouseOver(x, y);
	}

	@Override
	public boolean isFocused()
	{
		return focused;
	}

	@Override
	public void setFocused(boolean focused)
	{
		this.focused = focused;
	}

	@Override
	public NarratableEntry.NarrationPriority narrationPriority()
	{
		return this.isFocused() ? NarratableEntry.NarrationPriority.FOCUSED : super.narrationPriority();
	}

    protected net.minecraft.network.chat.Component createNarrationMessage() {
        return wrapDefaultNarrationMessage(this.getMessage());
    }

    public static net.minecraft.network.chat.Component wrapDefaultNarrationMessage(net.minecraft.network.chat.Component message) {
        return net.minecraft.network.chat.Component.translatable("gui.narrate.button", message);
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent event) {
    	if (!this.isActive() || !this.isVisible() || this.isFocused()) return null;
    	else return ComponentPath.leaf(this);
    }

    public static void playButtonClickSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public int getTabOrderGroup() {
        return this.tabOrderGroup;
    }

    public void setTabOrderGroup(int tabOrderGroup) {
        this.tabOrderGroup = tabOrderGroup;
    }

    protected void defaultButtonNarrationText(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.isActive()) {
            if (this.isFocused()) narrationElementOutput.add(NarratedElementType.USAGE, net.minecraft.network.chat.Component.translatable("narration.button.usage.focused"));
            else narrationElementOutput.add(NarratedElementType.USAGE, net.minecraft.network.chat.Component.translatable("narration.button.usage.hovered"));
        }
    }
}