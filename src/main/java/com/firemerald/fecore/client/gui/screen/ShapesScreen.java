package com.firemerald.fecore.client.gui.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.boundingshapes.IShapeGui;
import com.firemerald.fecore.capabilities.IShapeHolder;
import com.firemerald.fecore.client.gui.components.Button;
import com.firemerald.fecore.client.gui.components.ButtonShape;
import com.firemerald.fecore.client.gui.components.IComponent;
import com.firemerald.fecore.client.gui.components.scrolling.VerticalScrollBar;
import com.firemerald.fecore.client.gui.components.scrolling.VerticalScrollableComponentPane;
import com.firemerald.fecore.networking.server.ShapeToolSetPacket;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShapesScreen extends PopupScreen implements IShapeGui
{
    public final Stack<Pair<BoundingShape, Consumer<BoundingShape>>> prevShapes = new Stack<>();
    public Consumer<BoundingShape> onAccept;
    public final ButtonShape currentShape;
    public final ShapeToolButton fromToolMain, toToolMain, fromToolOffhand, toToolOffhand;
    public final Button okay, cancel;
    public final VerticalScrollableComponentPane entityButtons;
    public final VerticalScrollBar entityButtonsScroll;
    private final List<IComponent> addedElements = new ArrayList<>();
    public final Vec3 pos;
    public final Player player;

    public static class ShapeToolButton extends Button
    {
    	public final ShapesScreen gui;
    	public final Player player;
    	public final InteractionHand hand;

    	public ShapeToolButton(int x, int y, Component buttonText, ShapesScreen gui, Player player, InteractionHand hand, Runnable onClick)
    	{
    		super(x, y, buttonText, onClick);
    		this.gui = gui;
    		this.player = player;
    		this.hand = hand;
    	}

    	public ShapeToolButton(int x, int y, int widthIn, int heightIn, Component buttonText, ShapesScreen gui, Player player, InteractionHand hand, Runnable onClick)
    	{
    		super(x, y, widthIn, heightIn, buttonText, onClick);
    		this.gui = gui;
    		this.player = player;
    		this.hand = hand;
    	}

    	@Override
    	public void tick()
    	{
    		ItemStack held = player.getItemInHand(hand);
			this.enabled = !held.isEmpty() && IShapeHolder.get(held).filter(holder -> holder.canAcceptShape(gui.currentShape.shape)).isPresent();
    	}
    }

	@SuppressWarnings("resource")
	public ShapesScreen(Vec3 pos, BoundingShape shape, Consumer<BoundingShape> onAccept)
	{
		super(new TranslatableComponent("fecore.shapesgui"));
		this.pos = pos;
		this.font = Minecraft.getInstance().font;
		this.onAccept = onAccept;
		int y = 0;
		if ((player = Minecraft.getInstance().player) != null)
		{
			y += 20;
			y += 20;
			currentShape = new ButtonShape(100, y, 200, 20, shape, (s) -> updateGuiButtonsList());
			y -= 20;
			addRenderableWidget(fromToolMain = new ShapeToolButton(0, 0, 200, 20, new TranslatableComponent("fecore.shapesgui.mainhand.from"), this, player, InteractionHand.MAIN_HAND, () -> {
	    		ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
	    		if (!held.isEmpty())
	    		{
	    			IShapeHolder.get(held).lazyMap(IShapeHolder::getShape).ifPresent(s -> {
		    			this.currentShape.setShape(s);
		    			this.updateGuiButtonsList();
	    			});
	    		}
			}));
			addRenderableWidget(fromToolOffhand = new ShapeToolButton(200, 0, 200, 20, new TranslatableComponent("fecore.shapesgui.offhand.from"), this, player, InteractionHand.OFF_HAND, () -> {
	    		ItemStack held = player.getItemInHand(InteractionHand.OFF_HAND);
	    		if (!held.isEmpty())
	    		{
	    			IShapeHolder.get(held).lazyMap(IShapeHolder::getShape).ifPresent(s -> {
		    			this.currentShape.setShape(s);
		    			this.updateGuiButtonsList();
	    			});
	    		}
			}));
			y += 20;
			addRenderableWidget(toToolMain = new ShapeToolButton(0, 0, 200, 20, new TranslatableComponent("fecore.shapesgui.mainhand.to"), this, player, InteractionHand.MAIN_HAND, () -> FECoreMod.NETWORK.sendToServer(new ShapeToolSetPacket(InteractionHand.MAIN_HAND, this.currentShape.shape))));
			addRenderableWidget(toToolOffhand = new ShapeToolButton(200, 0, 200, 20, new TranslatableComponent("fecore.shapesgui.offhand.to"), this, player, InteractionHand.OFF_HAND, () -> FECoreMod.NETWORK.sendToServer(new ShapeToolSetPacket(InteractionHand.OFF_HAND, this.currentShape.shape))));
			addRenderableWidget(currentShape);
		}
		else
		{
			fromToolMain = fromToolOffhand = toToolMain = toToolOffhand = null;
			addRenderableWidget(currentShape = new ButtonShape(100, y, 200, 20, shape, (s) -> updateGuiButtonsList()));
		}
		y += 20;
		addRenderableWidget(entityButtons = new VerticalScrollableComponentPane(0, y, 390, y + 100));
		addRenderableWidget(entityButtonsScroll = new VerticalScrollBar(390, y, 400, y + 100, entityButtons));
		entityButtons.setScrollBar(entityButtonsScroll);
		y += 100;
		addRenderableWidget(okay = new Button(0, y, 200, 20, new TranslatableComponent("fecore.gui.done"), () -> {
			this.onAccept.accept(currentShape.shape);
			if (prevShapes.isEmpty()) this.deactivate();
			else
			{
				Pair<BoundingShape, Consumer<BoundingShape>> pair = prevShapes.pop();
				currentShape.setShape(pair.getLeft());
				this.onAccept = pair.getRight();
				updateGuiButtonsList();
			}
		}));
		addRenderableWidget(cancel = new Button(200, y, 400, 20, new TranslatableComponent("fecore.gui.cancel"), () -> {
			if (prevShapes.isEmpty()) this.deactivate();
			else
			{
				Pair<BoundingShape, Consumer<BoundingShape>> pair = prevShapes.pop();
				currentShape.setShape(pair.getLeft());
				this.onAccept = pair.getRight();
				updateGuiButtonsList();
			}
		}));
		updateGuiButtonsList();
	}

	@SuppressWarnings("resource")
	@Override
	public void init()
	{
		int offX = (width - 400) >> 1;
		int y = 0;
		if (fromToolMain != null)
		{
			fromToolMain.setSize(offX, y, offX + 200, y + 20);
			fromToolOffhand.setSize(offX + 200, y, offX + 400, y + 20);
			y += 20;
			toToolMain.setSize(offX, y, offX + 200, y + 20);
			toToolOffhand.setSize(offX + 200, y, offX + 400, y + 20);
			y += 20;
		}
		currentShape.setSize(offX + 95, y, offX + 295, y + 20);
		y += 20;
		entityButtons.setSize(offX, y, offX + 390, height - 20);
		entityButtonsScroll.setSize(offX + 390, y, offX + 400, height - 20);
		okay.setSize(offX, height - 20, offX + 200, height);
		cancel.setSize(offX + 200, height - 20, offX + 400, height);
		if (player != null)
		{
			addRenderableWidget(fromToolMain);
			addRenderableWidget(fromToolOffhand);
			addRenderableWidget(toToolMain);
			addRenderableWidget(toToolOffhand);
			addRenderableWidget(currentShape);
		}
		else
		{
			addRenderableWidget(currentShape);
		}
		addRenderableWidget(entityButtons);
		addRenderableWidget(entityButtonsScroll);
		addRenderableWidget(okay);
		addRenderableWidget(cancel);
		updateGuiButtonsList();
	}

	@Override
	public boolean keyPressed(int key, int scancode, int mods)
    {
        if (key == InputConstants.KEY_ESCAPE)
        {
        	cancel.onClick.run();
        	return true;
        }
        else return super.keyPressed(key, scancode, mods);
    }

	public boolean shouldCloseOnEsc()
	{
		return false;
	}

	@Override
	public void updateGuiButtonsList()
	{
		addedElements.forEach(entityButtons::removeComponent);
		this.currentShape.shape.addGuiElements(pos, this, font, ((Consumer<IComponent>) entityButtons::addComponent).andThen(addedElements::add), entityButtons.x2 - entityButtons.x1);
		entityButtons.updateComponentSize();
		entityButtons.updateScrollSize();
	}

	@Override
	public void openShape(BoundingShape shape, Consumer<BoundingShape> onAccepted)
	{
		this.prevShapes.push(Pair.of(this.currentShape.shape, this.onAccept));
		this.onAccept = onAccepted;
		this.currentShape.setShape(shape);
		updateGuiButtonsList();
	}

	@Override
	public void renderBackground(PoseStack pose, int mx, int my, float partialTicks, boolean canHover)
	{
        //this.fillGradient(pose, 0, 0, this.width, this.height, -1072689136, -804253680);

		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Matrix4f mat = pose.last().pose();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		Tesselator t = Tesselator.getInstance();
		BufferBuilder b = t.getBuilder();
		b.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		b.vertex(mat, 0    , height, 0).color(0, 0, 0, .5f).endVertex();
		b.vertex(mat, width, height, 0).color(0, 0, 0, .5f).endVertex();
		b.vertex(mat, width, 0     , 0).color(0, 0, 0, .5f).endVertex();
		b.vertex(mat, 0    , 0     , 0).color(0, 0, 0, .5f).endVertex();
		t.end();
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		
	}
}