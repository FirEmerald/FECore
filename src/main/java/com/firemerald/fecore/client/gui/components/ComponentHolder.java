package com.firemerald.fecore.client.gui.components;

import java.util.List;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.gui.IComponentHolder;
import com.firemerald.fecore.client.gui.IInteractableComponentHolder;
import com.firemerald.fecore.util.ReadOnlyListView;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ComponentHolder extends InteractableComponent implements IInteractableComponentHolder
{
	private boolean dragging;
	protected GuiEventListener focused;
	private final List<IComponent> components = Lists.newArrayList();
	public final List<IComponent> componentsList = new ReadOnlyListView<>(components);
	private final List<IInteractableComponent> interactables = Lists.newArrayList();
	public final List<IInteractableComponent> interactablesList = new ReadOnlyListView<>(interactables);
	@Nullable
	private NarratableEntry lastNarratable;

	public ComponentHolder(int x1, int y1, int x2, int y2)
	{
		super(x1, y1, x2, y2);
	}

	public ComponentHolder(IComponentHolder parent, int x1, int y1, int x2, int y2)
	{
		super(parent, x1, y1, x2, y2);
	}

	public void addComponent(IComponent component)
	{
		components.add(component);
		onComponentAdded(component);
	}

	public void addComponentBefore(IComponent component, IComponent before)
	{
		int index = components.indexOf(before);
		if (index < 0) index = 0;
		components.add(index, component);
		onComponentAdded(component);
	}

	public void addComponentAfter(IComponent component, IComponent after)
	{
		int index = components.lastIndexOf(after);
		if (index < 0) index = components.size();
		else index++;
		components.add(index, component);
		onComponentAdded(component);
	}

	protected void onComponentAdded(IComponent component)
	{
		component.setHolder(this);
		if (component instanceof IInteractableComponent) interactables.add((IInteractableComponent) component);
		updateComponentSize();
	}

	public void removeComponent(IComponent component)
	{
		components.remove(component);
		component.setHolder(null);
		if (component instanceof IInteractableComponent) interactables.remove(component);
		updateComponentSize();
	}

	protected void clearWidgets()
	{
		this.components.clear();
		this.interactables.clear();
		updateComponentSize();
	}

	public void updateComponentSize() {}

	@Override
	public boolean isActive()
	{
		return this.components.stream().anyMatch(NarratableEntry::isActive);
	}

	@Override
	public NarratableEntry.NarrationPriority narrationPriority()
	{
		return this.components.stream().map(NarratableEntry::narrationPriority).max((c1, c2) -> c1.compareTo(c2)).orElse(NarrationPriority.NONE);
		//return NarrationPriority.NONE;//this.isFocused() ? NarrationPriority.FOCUSED : this.isHovered() ? NarrationPriority.HOVERED : NarrationPriority.NONE;
	}

	@Override
	public void doRender(GuiGraphics guiGraphics, int mx, int my, float partialticks, boolean canHover)
	{
		preRender(guiGraphics);
		final int x = (int) Math.floor(adjX(mx)), y = (int) Math.floor(adjY(my));
		final boolean canHoverComponents = canHover && canHoverComponents(mx, my);
		this.components.forEach(c -> c.render(guiGraphics, x, y, partialticks, canHoverComponents));
		postRender(guiGraphics);
	}

	public boolean canHoverComponents(int mx, int my)
	{
		return this.isMouseOver(mx, my);
	}

	public void preRender(GuiGraphics guiGraphics)
	{
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(getX1(), getY1(), 0);
		guiGraphics.enableScissor(0, 0, getWidth(), getHeight());
	}

	public void postRender(GuiGraphics guiGraphics)
	{
		guiGraphics.pose().popPose();
		guiGraphics.disableScissor();
	}

	@Override
	public boolean isDragging()
	{
		return dragging;
	}

	@Override
	public void setDragging(boolean dragging)
	{
		this.dragging = dragging;
	}

	@Override
	@Nullable
	public GuiEventListener getFocused()
	{
		return focused;
	}


	@Override
	public void setFocused(GuiEventListener focused) {
		if (this.focused != focused) {
			if (this.focused != null) this.focused.setFocused(false);
			this.focused = focused;
			if (focused != null) {
				this.setFocused(true);
				focused.setFocused(true);
				this.ensureInView(focused);
			}
		}
	}

	public void setInitialFocus(@Nullable GuiEventListener focused)
	{
		this.setFocused(focused);
	}

	@Override
	public void tick()
	{
		this.components.forEach(IComponent::tick);
	}

	@Override
	public void setFocused(boolean focused)
	{
		super.setFocused(focused);
		if (!focused) setFocused(null);
	}

	public void ensureInView(GuiEventListener component) {
		if (component instanceof IComponent icomponent) ensureInView(icomponent.getX1(), icomponent.getY1(), icomponent.getX2(), icomponent.getY2());
		else {
			ScreenRectangle bounds = component.getRectangle();
			ensureInView(bounds.left(), bounds.top(), bounds.right(), bounds.bottom());
		}
	}

	public void ensureInView(int minX, int minY, int maxX, int maxY) {}

	@Override
	public void updateWidgetNarration(NarrationElementOutput output)
	{
		ImmutableList<NarratableEntry> components = this.components.stream().filter(NarratableEntry::isActive).collect(ImmutableList.toImmutableList());
		Screen.NarratableSearchResult searchResult = Screen.findNarratableWidget(components, this.lastNarratable);
		if (searchResult != null)
		{
			if (searchResult.priority.isTerminal()) this.lastNarratable = searchResult.entry;
			if (components.size() > 1)
			{
				output.add(NarratedElementType.POSITION, net.minecraft.network.chat.Component.translatable("narrator.position.componentholder", searchResult.index + 1, components.size()));
				if (searchResult.priority == NarratableEntry.NarrationPriority.FOCUSED)
				{
					output.add(NarratedElementType.USAGE, net.minecraft.network.chat.Component.translatable("narration.component_list.usage"));
				}
			}
			searchResult.entry.updateNarration(output.nest());
		}
	}

	@Override
	public Component getMessage() {
		return null;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return IInteractableComponentHolder.super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public List<? extends IComponent> components() {
		return components;
	}

	@Override
	public List<? extends IInteractableComponent> interactables() {
		return interactables;
	}

    @Override
	public ComponentPath nextFocusPath(FocusNavigationEvent event) {
    	return IInteractableComponentHolder.super.nextFocusPath(event);
    }
}
