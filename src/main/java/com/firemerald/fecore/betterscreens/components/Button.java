package com.firemerald.fecore.betterscreens.components;

import java.util.function.Function;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;

public class Button extends InteractableComponent
{
    /** Button width in pixels */
    public int width;
    /** Button height in pixels */
    public int height;
    /** The string displayed on this control. */
    public net.minecraft.network.chat.Component displayString;
    /** True if this control is enabled, false to disable. */
    public boolean enabled;
    public int packedFGColour; //FML
    public Runnable onClick;

    public Button(int x, int y, net.minecraft.network.chat.Component buttonText, Runnable onClick)
    {
        this(x, y, 200, 20, buttonText, onClick);
    }

    public Button(int x, int y, int width, int height, net.minecraft.network.chat.Component buttonText, Runnable onClick)
    {
    	super(x, y, x + width, y + height);
        this.enabled = true;
        this.width = width;
        this.height = height;
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
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
    	return this.enabled ? (this.focused || mouseOver) ? 2 : 1 : 0;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
	public void doRender(PoseStack pose, int mx, int my, float partialTicks, boolean canHover)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, AbstractWidget.WIDGETS_LOCATION);
        boolean hovered = canHover && this.isMouseOver(mx, my);
        int i = this.getHoverState(hovered);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
        RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        if (this.height == 20)
        {
        	if (this.width == 200)
        	{
                this.blit(pose, this.x1, this.y1, 0, 46 + i * 20, this.width, this.height);
        	}
        	else
        	{
                this.blit(pose, this.x1                 , this.y1, 0                   , 46 + i * 20, this.width / 2, this.height);
                this.blit(pose, this.x1 + this.width / 2, this.y1, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        	}
        }
        else
        {
        	if (this.width == 200)
        	{
                this.blit(pose, this.x1, this.y1                  , 0, 46 + i * 20                  , this.width, this.height / 2);
                this.blit(pose, this.x1, this.y1 + this.height / 2, 0, 66 + i * 20 - this.height / 2, this.width, this.height / 2);
        	}
        	else
        	{
                this.blit(pose, this.x1                 , this.y1                  , 0                   , 46 + i * 20                  , this.width / 2, this.height / 2);
                this.blit(pose, this.x1 + this.width / 2, this.y1                  , 200 - this.width / 2, 46 + i * 20                  , this.width / 2, this.height / 2);
                this.blit(pose, this.x1                 , this.y1 + this.height / 2, 0                   , 66 + i * 20 - this.height / 2, this.width / 2, this.height / 2);
                this.blit(pose, this.x1 + this.width / 2, this.y1 + this.height / 2, 200 - this.width / 2, 66 + i * 20 - this.height / 2, this.width / 2, this.height / 2);
        	}
        }
        drawCenteredString(pose, font, this.displayString, this.x1 + this.width / 2, this.y1 + (this.height - 8) / 2, getTextColor(hovered));
    }

    @Override
    public void updateNarration(NarrationElementOutput output)
    {
    	output.add(NarratedElementType.TITLE, this.createNarrationMessage());
    	if (this.enabled)
    	{
    		if (this.isFocused())
    		{
    			output.add(NarratedElementType.USAGE, new TranslatableComponent("narration.button.usage.focused"));
    		}
    		else
    		{
    			output.add(NarratedElementType.USAGE, new TranslatableComponent("narration.button.usage.hovered"));
    		}
    	}
    }
    
    protected MutableComponent createNarrationMessage()
    {
        return AbstractWidget.wrapDefaultNarrationMessage(displayString);
    }
    
    public int getTextColor(boolean isHovered)
    {
        if (packedFGColour != 0) return packedFGColour;
        else if (!this.enabled) return 0xA0A0A0;
        else if (isHovered) return 0xFFFFA0;
        else return 0xE0E0E0;
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    @Override
    public boolean onMouseClicked(double mx, double my, int button)
    {
        if (this.enabled && button == 0)
        {
        	playPressSound(Minecraft.getInstance().getSoundManager());
        	onClick.run();
        	return true;
        }
        else return false;
    }

    public boolean keyPressed(int key, int scancode, int mods)
    {
       if (this.enabled && this.isVisible())
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

	@Override
	public void setSize(int x1, int y1, int x2, int y2)
	{
		super.setSize(x1, y1, x2, y2);
		this.width = x2 - x1 + 1;
		this.height = y2 - y1;
	}
}
