package com.firemerald.fecore.client.gui.screen;

import java.awt.Rectangle;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.function.TriFunction;

import com.firemerald.fecore.client.gui.components.Button;
import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.client.gui.components.scrolling.VerticalScrollableComponentPane;

import net.minecraft.network.chat.Component;

public class SelectorPopupScreen extends PopupScreen {
	public final Runnable onCancel;
	public IComponent from;
	public final VerticalScrollableComponentPane pane;
	public final int numVals;

	public SelectorPopupScreen(IComponent from, String[] values, BiConsumer<Integer, String> action) {
		this(from, values, action, (Runnable) null);
	}

	public <T> SelectorPopupScreen(IComponent from, T[] values, BiConsumer<Integer, T> action, TriFunction<T, Rectangle, Runnable, IComponent> newButton) {
		this(from, values, action, null, newButton);
	}

	public SelectorPopupScreen(IComponent from, String[] values, BiConsumer<Integer, String> action, Runnable onCancel) {
		this(from, values, action, onCancel, (str, size, onRelease) -> new Button(size.x, size.y, size.width, size.height, Component.literal(str), onRelease));
	}

	public <T> SelectorPopupScreen(IComponent from, T[] values, BiConsumer<Integer, T> action, Runnable onCancel, TriFunction<T, Rectangle, Runnable, IComponent> newButton) {
		super(Component.empty());
		//this.setBlitOffset(this.getBlitOffset() + 1000); //TODO
		this.onCancel = onCancel;
		this.from = from;
		numVals = values.length;
		int x1 = from.getSelectorX1(this);
		int y1 = from.getSelectorY1(this);
		int x2 = from.getSelectorX2(this);
		int y2 = from.getSelectorY2(this);
		this.addRenderableWidget(pane = new VerticalScrollableComponentPane(x1, y1, x2, y2));
		int w = x2 - x1;
		int h = y2 - y1;
		int y = 0;
		for (int i = 0; i < values.length; i++) {
			final int j = i;
			final T val = values[i];
			pane.addComponent(newButton.apply(val, new Rectangle(0, y, w, h), () -> {
				deactivate();
				action.accept(j, val);
			}));
			y += h;
		}
		pane.updateComponentSize();
	}

	@Override
	public void init() {
		super.init();
		int x1 = from.getSelectorX1(this);
		int y1 = from.getSelectorY1(this);
		int x2 = from.getSelectorX2(this);
		int y2 = from.getSelectorY2(this);
		int h = y2 - y1;
		int sY, eY;
		if ((y2 + y1) > height) { //on bottom
			sY = Math.max(0, y2 - h * numVals);
			eY = y2;
		}
		else {
			sY = y1;
			eY = Math.min(height, y1 + h * numVals);
		}
		pane.setSize(x1, sY, x2, eY);
		this.addRenderableWidget(pane);
	}

	public void cancel() {
		if (onCancel == null) deactivate();
		else onCancel.run();
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		boolean flag = super.mouseClicked(mx, my, button);
		if (button == 0 && this.getFocused() == null) deactivate();
		return flag;
	}
}