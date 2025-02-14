package com.firemerald.fecore.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SpriteButton extends Button {
    protected final WidgetSprites sprites;
    protected final int spriteWidth;
    protected final int spriteHeight;

    public SpriteButton(int x, int y, int width, int height, net.minecraft.network.chat.Component buttonText, WidgetSprites sprites, int spriteWidth, int spriteHeight, Runnable onClick) {
    	super(x, y, width, height, buttonText, onClick);
        this.sprites = sprites;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }

    public SpriteButton(int x, int y, int width, int height,  WidgetSprites sprites, int spriteWidth, int spriteHeight, Runnable onClick) {
        this(x, y, width, height, net.minecraft.network.chat.Component.empty(), sprites, spriteWidth, spriteHeight, onClick);
    }

    public SpriteButton(int x, int y, int width, int height, net.minecraft.network.chat.Component buttonText, ResourceLocation sprite, int spriteWidth, int spriteHeight, Runnable onClick) {
        this(x, y, width, height, buttonText, new WidgetSprites(sprite, sprite), spriteWidth, spriteHeight, onClick);
    }

    public SpriteButton(int x, int y, int width, int height,  ResourceLocation sprite, int spriteWidth, int spriteHeight, Runnable onClick) {
        this(x, y, width, height, net.minecraft.network.chat.Component.empty(), new WidgetSprites(sprite, sprite), spriteWidth, spriteHeight, onClick);
    }

    public SpriteButton(int x, int y, net.minecraft.network.chat.Component buttonText, WidgetSprites sprites, int spriteWidth, int spriteHeight, Runnable onClick) {
        super(x, y, buttonText, onClick);
        this.sprites = sprites;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }

    public SpriteButton(int x, int y,  WidgetSprites sprites, int spriteWidth, int spriteHeight, Runnable onClick) {
        this(x, y, net.minecraft.network.chat.Component.empty(), sprites, spriteWidth, spriteHeight, onClick);
    }

    public SpriteButton(int x, int y, net.minecraft.network.chat.Component buttonText, ResourceLocation sprite, int spriteWidth, int spriteHeight, Runnable onClick) {
        this(x, y, buttonText, new WidgetSprites(sprite, sprite), spriteWidth, spriteHeight, onClick);
    }

    public SpriteButton(int x, int y,  ResourceLocation sprite, int spriteWidth, int spriteHeight, Runnable onClick) {
        this(x, y, net.minecraft.network.chat.Component.empty(), new WidgetSprites(sprite, sprite), spriteWidth, spriteHeight, onClick);
    }

    @Override
    public void renderDecoration(GuiGraphics guiGraphics, int mx, int my, float partialTicks, boolean hovered) {
        int spriteX = this.getX1() + (this.getWidth()  - this.spriteWidth) / 2;
        int spriteY = this.getY1() + (this.getHeight() - this.spriteHeight) / 2;
        guiGraphics.blitSprite(RenderType::guiTextured, getTexture(sprites, hovered), spriteX, spriteY, this.spriteWidth, this.spriteHeight);
    	super.renderDecoration(guiGraphics, mx, my, partialTicks, hovered);
    }
}
