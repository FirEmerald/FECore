package com.firemerald.fecore.item;

import java.util.List;

import javax.annotation.Nullable;

import com.firemerald.fecore.boundingshapes.BoundingShape;
import com.firemerald.fecore.boundingshapes.BoundingShapeBoxPositions;
import com.firemerald.fecore.boundingshapes.BoundingShapeDefinition;
import com.firemerald.fecore.boundingshapes.IConfigurableBoundingShape;
import com.firemerald.fecore.capabilities.IShapeHolder;
import com.firemerald.fecore.capabilities.IShapeTool;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class ShapeToolItem extends Item implements ICapSynchronizedItem<IShapeTool> {
	public ShapeToolItem(Properties properties) {
		super(properties);
	}

	//right-click on block = add pos
	@Override
    public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player instanceof ServerPlayer serverPlayer) {
			ItemStack stack = context.getItemInHand();
			IShapeTool.get(stack).ifPresent(tool -> {
				BoundingShape shape;
				BoundingShape s = tool.getShape();
				int posIndex = tool.getConfigurationIndex();
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
					tool.setShape(shape);
					tool.setConfigurationIndex(0);
				}
				else {
					posIndex = ((IConfigurableBoundingShape) shape).addPosition(serverPlayer, context.getClickedPos(), posIndex);
					tool.setShape(shape);
					tool.setConfigurationIndex(posIndex);
				}
			});
		}
		return InteractionResult.SUCCESS;
	}

	//right-click empty = remove pos
	//shift-right-click on empty = change mode
	@Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player instanceof ServerPlayer serverPlayer) {
			IShapeTool.get(stack).ifPresent(tool -> {
				BoundingShape shape;
				BoundingShape s = tool.getShape();
				int posIndex = tool.getConfigurationIndex();
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
					tool.setShape(shape);
					tool.setConfigurationIndex(0);
				} else {
					tool.setShape(shape);
					tool.setConfigurationIndex(posIndex);
				}
			});
		}
		return InteractionResultHolder.success(stack);
	}

	public static BoundingShape getShape(ItemStack stack)
	{
		IShapeHolder holder = IShapeHolder.get(stack).orElse(null);
		return holder == null ? null : holder.getShape();
	}

    @Override
    public Component getName(ItemStack stack) {
    	BoundingShape shape = getShape(stack);
    	return Component.translatable(this.getDescriptionId(), shape == null ? Component.translatable("fecore.shape.none") : shape.getLocalizedName());
    }

	@Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.add_pos"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.remove_pos"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.change_mode"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.open_gui"));
		tooltipComponents.add(Component.translatable("fecore.shapetool.tooltip.bypass_block_activation"));
		BoundingShape shape = getShape(stack);
		if (shape instanceof IConfigurableBoundingShape configurable) configurable.addInformation(stack, level, tooltipComponents, tooltipFlag);
    }

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		IShapeTool cap = new IShapeTool.Impl();
		if (nbt != null) cap.deserializeNBT(nbt);
		return cap;
	}

	@Override
	public LazyOptional<IShapeTool> getCap(ItemStack stack)
	{
		return IShapeTool.get(stack);
	}

	@Override
	public CompoundTag writeCap(IShapeTool cap, ItemStack stack)
	{
		return cap.serializeNBT();
	}

	@Override
	public void readCap(IShapeTool cap, ItemStack stack, CompoundTag tag)
	{
		cap.deserializeNBT(tag);
	}
}