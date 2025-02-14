package com.firemerald.fecore.item;

import java.util.List;

import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.boundingshapes.BoundingShapeBoxPositions;
import com.firemerald.fecore.boundingshapes.BoundingShapeDefinition;
import com.firemerald.fecore.boundingshapes.IConfigurableBoundingShape;
import com.firemerald.fecore.init.FECoreDataComponents;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ShapeToolItem extends Item {
	public ShapeToolItem(Properties properties) {
		super(properties.component(FECoreDataComponents.HELD_SHAPE, new BoundingShapeBoxPositions(false)).component(FECoreDataComponents.HELD_SHAPE_INDEX, 0));
	}

	//right-click on block = add pos
	@Override
    public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player instanceof ServerPlayer serverPlayer) {
			ItemStack stack = context.getItemInHand();
			BoundingShape shape;
			BoundingShape s = stack.get(FECoreDataComponents.HELD_SHAPE);
			int posIndex = stack.get(FECoreDataComponents.HELD_SHAPE_INDEX);
			boolean isNew = false;
			if (s instanceof IConfigurableBoundingShape) shape = s;
			else {
				isNew = true;
				shape = new BoundingShapeBoxPositions(false);
				posIndex = 0;
				if (s != null) //was invalid shape
					serverPlayer.sendSystemMessage(Component.translatable("fecore.shapetool.invalid", Component.translatable(s.getUnlocalizedName()), Component.translatable(shape.getUnlocalizedName())));
				else //no shape selected
					serverPlayer.sendSystemMessage(Component.translatable("fecore.shapetool.new", Component.translatable(shape.getUnlocalizedName())));
			}
			if (shape != s) shape.getPropertiesFrom(s);
			if (isNew) {
				stack.set(FECoreDataComponents.HELD_SHAPE, shape);
				stack.set(FECoreDataComponents.HELD_SHAPE_INDEX, 0);
			}
			else {
				posIndex = ((IConfigurableBoundingShape) shape).addPosition(serverPlayer, context.getClickedPos(), posIndex);
				stack.set(FECoreDataComponents.HELD_SHAPE, shape);
				stack.set(FECoreDataComponents.HELD_SHAPE_INDEX, posIndex);
			}
		}
		return InteractionResult.SUCCESS;
	}

	//right-click empty = remove pos
	//shift-right-click on empty = change mode
	@Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer serverPlayer) {
			ItemStack stack = player.getItemInHand(hand);
			BoundingShape shape;
			BoundingShape s = stack.get(FECoreDataComponents.HELD_SHAPE);
			int posIndex = stack.get(FECoreDataComponents.HELD_SHAPE_INDEX);
			boolean isNew = false;
			if (s instanceof IConfigurableBoundingShape) shape = s;
			else {
				isNew = true;
				shape = new BoundingShapeBoxPositions();
				((BoundingShapeBoxPositions) shape).isRelative = false;
				posIndex = 0;
				if (s != null) //was invalid shape
					serverPlayer.sendSystemMessage(Component.translatable("fecore.shapetool.invalid", Component.translatable(s.getUnlocalizedName()), Component.translatable(shape.getUnlocalizedName())));
				else //no shape selected
					serverPlayer.sendSystemMessage(Component.translatable("fecore.shapetool.new", Component.translatable(shape.getUnlocalizedName())));
			}
			if (serverPlayer.isShiftKeyDown()) {
				if (!isNew) {
					List<BoundingShapeDefinition<?>> shapeDefs = BoundingShape.getConfigurableShapeDefinitions().toList();
					int index = shapeDefs.indexOf(shape.definition());
					int newIndex = (index + 1) % shapeDefs.size();
					if (index != newIndex) {
						shape = shapeDefs.get(newIndex).newShape();
						isNew = true;
						serverPlayer.sendSystemMessage(Component.translatable("fecore.shapetool.mode", Component.translatable(shape.getUnlocalizedName())));
					}
				}
			}
			else if (!isNew) posIndex = ((IConfigurableBoundingShape) shape).removePosition(player, posIndex);
			if (shape != s) shape.getPropertiesFrom(s);
			if (isNew) {
				stack.set(FECoreDataComponents.HELD_SHAPE, shape);
				stack.set(FECoreDataComponents.HELD_SHAPE_INDEX, 0);
			} else {
				stack.set(FECoreDataComponents.HELD_SHAPE, shape);
				stack.set(FECoreDataComponents.HELD_SHAPE_INDEX, posIndex);
			}
		}
		return InteractionResult.SUCCESS;
	}

    @Override
    public Component getName(ItemStack stack) {
    	BoundingShape shape = stack.get(FECoreDataComponents.HELD_SHAPE);
    	return Component.translatable(this.getDescriptionId(), shape == null ? Component.translatable("fecore.shape.none") : shape.getLocalizedName());
    }

	@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.add_pos"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.remove_pos"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.change_mode"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.open_gui"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.bypass_block_activation"));
		BoundingShape shape = stack.get(FECoreDataComponents.HELD_SHAPE);
		if (shape instanceof IConfigurableBoundingShape configurable) configurable.addInformation(stack, context, tooltipComponents, tooltipFlag);
    }
}