package com.firemerald.fecore.client.gui.screen;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.gui.IBetterRenderer;
import com.firemerald.fecore.client.gui.IBetterScreen;
import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.client.gui.components.IInteractableComponent;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BetterScreen extends Screen implements IBetterScreen
{
	protected BetterScreen(Component title)
	{
		super(title);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mx, int my, float partialTick)
	{
		render(guiGraphics, mx, my, partialTick, true);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mx, int my, float partialTick, boolean canHover)
	{
		for (Renderable renderable : this.renderables)
		{
			if (renderable instanceof IBetterRenderer betterRenderable) betterRenderable.render(guiGraphics, mx, my, partialTick, canHover);
			else renderable.render(guiGraphics, mx, my, partialTick);
		}
	}

	@Override
	public boolean mouseReleased(double mx, double my, int button) //fix MC firing this event only for object under the cursor instead of the clicked object. I mean, WHY?
	{
		this.setDragging(false);
		return this.getFocused() != null && this.getFocused().mouseReleased(mx, my, button);
	}

	@Override
	public boolean mouseScrolledX(double mx, double my, double scroll)
	{
		return this.getChildAt(mx, my).filter(c -> {
			return c instanceof IInteractableComponent interactable && interactable.mouseScrolledX(mx, my, scroll) != scroll;
		}).isPresent();
	}

	@Override
	public void tick()
	{
		this.renderables.forEach(c -> {
			if (c instanceof IComponent component) component.tick();
		});
	}

	@Override
	public <T extends GuiEventListener & Renderable & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addRenderableWidgetBefore(T component, Renderable beforeRenderable, U beforeWidget)
	{
		int index = renderables.indexOf(beforeRenderable);
		if (index < 0) index = 0;
		renderables.add(index, component);
		return this.addWidgetBefore(component, beforeWidget);
	}

	@Override
	public <T extends Renderable> T addRenderableOnlyBefore(T component, Renderable before)
	{
		int index = renderables.indexOf(before);
		if (index < 0) index = 0;
		renderables.add(index, component);
		return component;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public <T extends GuiEventListener & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addWidgetBefore(T component, U before)
	{
		int index = children.indexOf(before);
		if (index < 0) index = 0;
		children.add(index, component);
		index = narratables.indexOf(before);
		if (index < 0) index = 0;
		narratables.add(index, component);
		return component;
	}

	@Override
	public <T extends GuiEventListener & Renderable & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addRenderableWidgetAfter(T component, Renderable afterRenderable, U afterWidget)
	{
		int index = renderables.lastIndexOf(afterRenderable);
		if (index < 0) index = renderables.size();
		else index++;
		renderables.add(index, component);
		return this.addWidgetAfter(component, afterWidget);
	}

	@Override
	public <T extends Renderable> T addRenderableOnlyAfter(T component, Renderable after)
	{
		int index = renderables.lastIndexOf(after);
		if (index < 0) index = renderables.size();
		else index++;
		renderables.add(index, component);
		return component;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public <T extends GuiEventListener & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addWidgetAfter(T component, U after)
	{
		int index = children.lastIndexOf(after);
		if (index < 0) index = children.size();
		else index++;
		children.add(index, component);
		index = narratables.lastIndexOf(after);
		if (index < 0) index = narratables.size();
		else index++;
		narratables.add(index, component);
		return component;
	}

    @Override
    public void setFocused(@Nullable GuiEventListener listener) {
    	if (listener != this.getFocused()) super.setFocused(listener);
    }
}