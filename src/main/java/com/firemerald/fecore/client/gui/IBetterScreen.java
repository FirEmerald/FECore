package com.firemerald.fecore.client.gui;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

public interface IBetterScreen extends IBetterRenderer, ContainerEventHandler
{
	public abstract <T extends GuiEventListener & Renderable & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addRenderableWidgetBefore(T component, Renderable beforeRenderable, U beforeWidget);

	public default <T extends GuiEventListener & Renderable & NarratableEntry, U extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidgetBefore(T component, U before)
	{
		return addRenderableWidgetBefore(component, before, before);
	}

	public abstract <T extends Renderable> T addRenderableOnlyBefore(T component, Renderable before);

	public abstract <T extends GuiEventListener & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addWidgetBefore(T component, U before);

	public abstract <T extends GuiEventListener & Renderable & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addRenderableWidgetAfter(T component, Renderable afterRenderable, U afterWidget);

	public default <T extends GuiEventListener & Renderable & NarratableEntry, U extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidgetAfter(T component, U after)
	{
		return addRenderableWidgetAfter(component, after, after);
	}

	public abstract <T extends Renderable> T addRenderableOnlyAfter(T component, Renderable after);

	public abstract <T extends GuiEventListener & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addWidgetAfter(T component, U after);
}
