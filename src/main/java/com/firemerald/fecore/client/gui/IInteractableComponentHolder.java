package com.firemerald.fecore.client.gui;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.client.gui.components.IComponentHolderComponent;
import com.firemerald.fecore.client.gui.components.IInteractableComponent;

import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;

public interface IInteractableComponentHolder extends IComponentHolderComponent, IInteractableComponent, ContainerEventHandler {
	public List<? extends IComponent> components();

	public List<? extends IInteractableComponent> interactables();

	public default Optional<? extends IComponent> getComponentAt(double x, double y) {
		return getComponentAt(components(), x, y);
	}

	public default Optional<? extends IInteractableComponent> getInteractableAt(double x, double y) {
		return getComponentAt(interactables(), x, y);
	}

	public default <T extends IComponent> Optional<T> getComponentAt(Collection<T> values, double x, double y) {
		final double ax = adjX(x), ay = adjY(y);
		return values.stream().filter(c -> c.isMouseOver(ax, ay)).findFirst();
	}

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
    	if (!this.isMouseOver(mouseX, mouseY)) return false;
    	else return ContainerEventHandler.super.mouseClicked(this.adjX(mouseX), this.adjY(mouseY), button);
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
    	return ContainerEventHandler.super.mouseReleased(this.adjX(mouseX), this.adjY(mouseY), button);
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
    	return ContainerEventHandler.super.mouseDragged(this.adjX(mouseX), this.adjY(mouseY), button, dragX, dragY);
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
    	return IInteractableComponent.super.mouseScrolled(this.adjX(mouseX), this.adjY(mouseY), scrollY);
    }

	@Override
	default double mouseScrolledX(double mx, double my, double scroll) {
		Optional<? extends IInteractableComponent> at = this.getInteractableAt(mx, my);
		if (at.isPresent()) return at.get().mouseScrolledX(adjX(mx), adjY(my), scroll);
		else return scroll;
	}

	@Override
	default double mouseScrolledY(double mx, double my, double scroll) {
		Optional<? extends IInteractableComponent> at = this.getInteractableAt(mx, my);
		if (at.isPresent()) return at.get().mouseScrolledY(adjX(mx), adjY(my), scroll);
		else return scroll;
	}

    @Override
	public default ComponentPath nextFocusPath(FocusNavigationEvent event) {
    	return ContainerEventHandler.super.nextFocusPath(event);
    }

    @Override
    public default List<? extends GuiEventListener> children() {
    	return interactables();
    }
}