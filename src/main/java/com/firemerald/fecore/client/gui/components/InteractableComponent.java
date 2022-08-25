package com.firemerald.fecore.client.gui.components;

import com.firemerald.fecore.client.gui.IComponentHolder;

import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class InteractableComponent extends Component implements IInteractableComponent
{
	public boolean focused = false;
	
	public InteractableComponent(int x1, int y1, int x2, int y2)
	{
		super(x1, y1, x2, y2);
	}

	public InteractableComponent(IComponentHolder parent, int x1, int y1, int x2, int y2)
	{
		super(parent, x1, y1, x2, y2);
	}

	@Override
	public boolean isMouseOver(double x, double y)
	{
		return super.isMouseOver(x, y);
	}

	@Override
	public boolean isFocused()
	{
		return focused;
	}

	@Override
	public void setFocused(boolean focused)
	{
		this.focused = focused;
	}
	
	@Override
	public NarratableEntry.NarrationPriority narrationPriority()
	{
		return this.focused ? NarratableEntry.NarrationPriority.FOCUSED : super.narrationPriority();
	}
}