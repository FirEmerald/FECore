package com.firemerald.fecore.client.gui.components;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.firemerald.fecore.client.gui.IComponentHolder;
import com.firemerald.fecore.client.gui.ScissorUtil;
import com.firemerald.fecore.util.ReadOnlyListView;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ComponentHolder extends InteractableComponent implements IComponentHolderComponent
{
	private boolean dragging;
	protected IInteractableComponent focused;
	private final List<IComponent> components = Lists.newArrayList();
	public final ReadOnlyListView<IComponent> componentsList = new ReadOnlyListView<>(components);
	private final List<IInteractableComponent> interactables = Lists.newArrayList();
	public final ReadOnlyListView<IInteractableComponent> interactablesList = new ReadOnlyListView<>(interactables);
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

	@Override
	public int getComponentOffsetX()
	{
		return IComponentHolderComponent.super.getComponentOffsetX() + x1;
	}

	@Override
	public int getComponentOffsetY()
	{
		return IComponentHolderComponent.super.getComponentOffsetY() + y1;
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
	public void doRender(PoseStack pose, int mx, int my, float partialticks, boolean canHover)
	{
		preRender(pose);
		final int x = (int) Math.floor(adjX(mx)), y = (int) Math.floor(adjY(my));
		final boolean canHoverComponents = canHover && canHoverComponents(mx, my);
		this.components.forEach(c -> c.render(pose, x, y, partialticks, canHoverComponents));
		postRender(pose);
	}

	public boolean canHoverComponents(int mx, int my)
	{
		return this.isMouseOver(mx, my);
	}

	public void preRender(PoseStack pose)
	{
		this.setScissor(0, 0, x2 - x1, y2 - y1);
		pose.pushPose();
		pose.translate(x1, y1, 0);
	}

	public void postRender(PoseStack pose)
	{
		pose.popPose();
		ScissorUtil.popScissor();
	}

	public double adjX(double x)
	{
		return x - x1;
	}

	public double adjY(double y)
	{
		return y - y1;
	}

	public Optional<IComponent> getComponentAt(double x, double y)
	{
		final double xx = adjX(x), yy = adjY(y);
		return components.stream().filter(c -> c.isMouseOver(xx, yy)).findFirst();
	}

	public Optional<IInteractableComponent> getInteractableAt(double x, double y)
	{
		final double xx = adjX(x), yy = adjY(y);
		return interactables.stream().filter(c -> c.isMouseOver(xx, yy)).findFirst();
	}

	@Override
	public boolean onMouseClicked(double mx, double my, int button)
	{
		final double x = adjX(mx), y = adjY(my);
		IInteractableComponent clicked = null;
		for (IInteractableComponent c : interactables) if (c.mouseClicked(x, y, button))
		{
			clicked = c;
			break;
		}
		if (clicked != null)
		{
			this.setFocused(clicked);
			if (button == 0) this.setDragging(true);
			return true;
		}
		else
		{
			this.setFocused(null);
			return false;
		}
	}

	@Override
	public boolean mouseReleased(double mx, double my, int button)
	{
		final double x = adjX(mx), y = adjY(my);
		this.setDragging(false);
		return this.getFocused() != null && this.getFocused().mouseReleased(x, y, button);
	}

	@Override
	public boolean mouseDragged(double mx, double my, int button, double dx, double dy)
	{
		final double x = adjX(mx), y = adjY(my);
		return this.getFocused() != null && this.isDragging() && button == 0 ? this.getFocused().mouseDragged(x, y, button, dx, dy) : false;
	}

	public boolean isDragging()
	{
		return dragging;
	}

	public void setDragging(boolean dragging)
	{
		this.dragging = dragging;
	}

	@Override
	public double mouseScrolledX(double mx, double my, double scroll)
	{
		final double x = adjX(mx), y = adjY(my);
		Optional<IInteractableComponent> at = this.getInteractableAt(x, y);
		if (at.isPresent()) return at.get().mouseScrolledX(x, y, scroll);
		else return scroll;
	}

	@Override
	public double mouseScrolledY(double mx, double my, double scroll)
	{
		final double x = adjX(mx), y = adjY(my);
		Optional<IInteractableComponent> at = this.getInteractableAt(x, y);
		if (at.isPresent()) return at.get().mouseScrolledY(x, y, scroll);
		else return scroll;
	}

	@Override
	public boolean keyPressed(int key, int scancode, int mods)
	{
		return this.getFocused() != null && this.getFocused().keyPressed(key, scancode, mods);
	}

	@Override
	public boolean keyReleased(int key, int scancode, int mods)
	{
		return this.getFocused() != null && this.getFocused().keyReleased(key, scancode, mods);
	}

	@Override
	public boolean charTyped(char chr, int mods)
	{
		return this.getFocused() != null && this.getFocused().charTyped(chr, mods);
	}

	@Nullable
	IInteractableComponent getFocused()
	{
		return focused;
	}

	public void setFocused(@Nullable IInteractableComponent focused)
	{
		if (this.focused != focused)
		{
			if (this.focused != null) this.focused.setFocused(false);
			this.focused = focused;
			if (focused != null)
			{
				this.setFocused(true);
				focused.setFocused(true);
				this.ensureInView(focused);
			}
		}
	}

	public void setInitialFocus(@Nullable IInteractableComponent focused)
	{
		this.setFocused(focused);
	}

	@Override
	public boolean changeFocus(boolean initial)
	{
		IInteractableComponent guieventlistener = this.getFocused();
		boolean flag = guieventlistener != null;
		if (flag && guieventlistener.changeFocus(initial))
		{
			this.setFocused(guieventlistener);
			return true;
		}
		else
		{
			int j = interactables.indexOf(guieventlistener);
			int i;
			if (flag && j >= 0) i = j + (initial ? 1 : 0);
			else if (initial) i = 0;
			else i = interactables.size();
			ListIterator<? extends IInteractableComponent> listiterator = interactables.listIterator(i);
			BooleanSupplier booleansupplier = initial ? listiterator::hasNext : listiterator::hasPrevious;
			Supplier<? extends IInteractableComponent> supplier = initial ? listiterator::next : listiterator::previous;
			while (booleansupplier.getAsBoolean())
			{
				IInteractableComponent guieventlistener1 = supplier.get();
				if (guieventlistener1.changeFocus(initial))
				{
					this.setFocused(guieventlistener1);
					return true;
				}
			}
			this.setFocused(false);
			return false;
		}
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

	public void ensureInView(IInteractableComponent component) {}

	@Override
	public void updateNarration(NarrationElementOutput output)
	{
		ImmutableList<NarratableEntry> components = this.components.stream().filter(NarratableEntry::isActive).collect(ImmutableList.toImmutableList());
		Screen.NarratableSearchResult searchResult = Screen.findNarratableWidget(components, this.lastNarratable);
		if (searchResult != null)
		{
			if (searchResult.priority.isTerminal()) this.lastNarratable = searchResult.entry;
			if (components.size() > 1)
			{
				output.add(NarratedElementType.POSITION, new TranslatableComponent("narrator.position.componentholder", searchResult.index + 1, components.size()));
				if (searchResult.priority == NarratableEntry.NarrationPriority.FOCUSED)
				{
					output.add(NarratedElementType.USAGE, new TranslatableComponent("narration.component_list.usage"));
				}
			}
			searchResult.entry.updateNarration(output.nest());
		}
	}
}
