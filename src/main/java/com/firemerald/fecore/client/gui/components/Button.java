package com.firemerald.fecore.client.gui.components;

import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class Button extends InteractableComponent
{
    /** The string displayed on this control. */
    public net.minecraft.network.chat.Component displayString;
    public int packedFGColour; //FML
    public Runnable onClick;

    public Button(int x, int y, net.minecraft.network.chat.Component buttonText, Runnable onClick)
    {
        this(x, y, 200, 20, buttonText, onClick);
    }

    public Button(int x, int y, int width, int height, net.minecraft.network.chat.Component buttonText, Runnable onClick)
    {
    	super(x, y, x + width, y + height);
        this.displayString = buttonText;
        this.onClick = onClick;
    }

    public Button setAction(Runnable onClick)
    {
    	this.onClick = onClick;
    	return this;
    }

    public Button setAction(Function<? super Button, Runnable> onClickFunction)
    {
    	return setAction(onClickFunction.apply(this));
    }

    /**
     * Draws this button to the screen.
     */
    @Override
	public void doRender(GuiGraphics guiGraphics, int mx, int my, float partialTicks, boolean canHover)
    {
        boolean hovered = canHover && this.isMouseOver(mx, my);
        guiGraphics.blitSprite(
            RenderType::guiTextured,
            getButtonTexture(hovered),
            getX1(),
            getY1(),
            getWidth(),
            getHeight(),
            0xFFFFFFFF
        );
        renderDecoration(guiGraphics, mx, my, partialTicks, hovered);
    }

    public void renderDecoration(GuiGraphics guiGraphics, int mx, int my, float partialTicks, boolean hovered) {
        Minecraft minecraft = Minecraft.getInstance();
        renderString(guiGraphics, minecraft.font, getTextColor(hovered));
    }

    public ResourceLocation getButtonTexture(boolean hovered) {
        return getTexture(AbstractButton.SPRITES, hovered);
    }

    public void renderString(GuiGraphics guiGraphics, Font font, int color) {
        this.renderScrollingString(guiGraphics, font, 2, color);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output)
    {
    	output.add(NarratedElementType.TITLE, this.createNarrationMessage());
    	if (this.isActive())
    	{
    		if (this.isFocused())
    		{
    			output.add(NarratedElementType.USAGE, net.minecraft.network.chat.Component.translatable("narration.button.usage.focused"));
    		}
    		else
    		{
    			output.add(NarratedElementType.USAGE, net.minecraft.network.chat.Component.translatable("narration.button.usage.hovered"));
    		}
    	}
    }

	@Override
	public Component getMessage() {
		return displayString;
	}

    public int getTextColor(boolean isHovered)
    {
        if (packedFGColour != 0) return packedFGColour;
        else if (!this.renderTextAsActive(isHovered)) return 0xA0A0A0;
        else if (this.renderTextAsFocused(isHovered)) return 0xFFFFA0;
        else return 0xE0E0E0;
    }

    public boolean renderTextAsActive(boolean hovered) {
    	return renderAsActive(hovered);
    }

    public boolean renderTextAsFocused(boolean hovered) {
    	return renderAsFocused(hovered);
    }

    @Override
    public boolean onMouseClicked(double mx, double my, int button)
    {
        if (this.isActive() && button == 0)
        {
        	playPressSound(Minecraft.getInstance().getSoundManager());
        	onClick.run();
        	return true;
        }
        else return false;
    }

    @Override
	public boolean keyPressed(int key, int scancode, int mods)
    {
       if (this.isActive() && this.isVisible())
       {
    	   if (key == 257 || key == 32 || key == 335)
    	   {
    		   playPressSound(Minecraft.getInstance().getSoundManager());
    		   onClick.run();
    		   return true;
    	   }
       }
       return false;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {}

    public void playPressSound(SoundManager soundManager)
    {
    	soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
