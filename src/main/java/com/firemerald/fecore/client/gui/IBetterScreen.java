package com.firemerald.fecore.client.gui;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

public interface IBetterScreen extends IBetterRenderer
{
	public abstract boolean mouseScrolledX(double mx, double my, double scroll);
	
	public abstract <T extends GuiEventListener & Widget & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addRenderableWidgetBefore(T component, Widget beforeRenderable, U beforeWidget);
	
	public default <T extends GuiEventListener & Widget & NarratableEntry, U extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidgetBefore(T component, U before)
	{
		return addRenderableWidgetBefore(component, before, before);
	}
	
	public abstract <T extends Widget> T addRenderableOnlyBefore(T component, Widget before);
	
	public abstract <T extends GuiEventListener & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addWidgetBefore(T component, U before);
	
	public abstract <T extends GuiEventListener & Widget & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addRenderableWidgetAfter(T component, Widget afterRenderable, U afterWidget);
	
	public default <T extends GuiEventListener & Widget & NarratableEntry, U extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidgetAfter(T component, U after)
	{
		return addRenderableWidgetAfter(component, after, after);
	}
	
	public abstract <T extends Widget> T addRenderableOnlyAfter(T component, Widget after);
	
	public abstract <T extends GuiEventListener & NarratableEntry, U extends GuiEventListener & NarratableEntry> T addWidgetAfter(T component, U after);

}
