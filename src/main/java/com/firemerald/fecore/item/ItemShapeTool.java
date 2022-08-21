package com.firemerald.fecore.item;

import java.util.List;

import javax.annotation.Nullable;

import com.firemerald.fecore.capabilities.FECoreCapabilities;
import com.firemerald.fecore.capabilities.IShapeHolder;
import com.firemerald.fecore.capabilities.IShapeTool;
import com.firemerald.fecore.selectionshapes.BoundingShape;
import com.firemerald.fecore.selectionshapes.BoundingShapeBoxPositions;
import com.firemerald.fecore.selectionshapes.BoundingShapeConfigurable;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.common.util.LazyOptional;

public class ItemShapeTool extends Item
{
	public ItemShapeTool()
	{
		super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS));
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player)
	{
		return true;
	}

	//right-click on block = add pos
	//shift-right-click on block = default action
	@SuppressWarnings("resource")
	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		Player player = context.getPlayer();
		if (player.isCrouching()) return InteractionResult.PASS;
		else if (!context.getLevel().isClientSide)
		{
			//ItemStack stack = context.getItemInHand();
			LazyOptional<IShapeTool> toolOpt = stack.getCapability(FECoreCapabilities.SHAPE_TOOL);
			toolOpt.ifPresent(tool -> {
				BoundingShapeConfigurable shape;
				int posIndex = tool.getConfigurationIndex();
				BoundingShape s = tool.getShape();
				boolean isNew = false;
				if (s instanceof BoundingShapeConfigurable) shape = (BoundingShapeConfigurable) s;
				else
				{
					isNew = true;
					shape = new BoundingShapeBoxPositions();
					posIndex = 0;
					if (s != null) //was invalid shape
					{
						player.sendMessage(new TranslatableComponent("fecore.shapetool.invalid", new TranslatableComponent(s.getUnlocalizedName()), new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
					}
					else //no shape selected
					{
						player.sendMessage(new TranslatableComponent("fecore.shapetool.new", new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
					}
				}
				if (isNew) tool.setShape(shape);
				else
				{
					posIndex = shape.addPosition(player, context.getClickedPos(), posIndex);
					tool.setConfigurationIndex(posIndex);
				}
			});
		}
		return InteractionResult.SUCCESS;
	}

	//right-click empty = remove pos
	//shift-right-click on empty = change mode
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide)
		{
			LazyOptional<IShapeTool> toolOpt = stack.getCapability(FECoreCapabilities.SHAPE_TOOL);
			toolOpt.ifPresent(tool -> {
				BoundingShapeConfigurable shape;
				int posIndex = tool.getConfigurationIndex();
				BoundingShape s = tool.getShape();
				boolean isNew = false;
				if (s instanceof BoundingShapeConfigurable) shape = (BoundingShapeConfigurable) s;
				else
				{
					isNew = true;
					shape = new BoundingShapeBoxPositions();
					posIndex = 0;
					if (s != null) //was invalid shape
					{
						player.sendMessage(new TranslatableComponent("fecore.shapetool.invalid", new TranslatableComponent(s.getUnlocalizedName()), new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
					}
					else //no shape selected
					{
						player.sendMessage(new TranslatableComponent("fecore.shapetool.new", new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
					}
				}
				if (player.isCrouching())
				{
					if (!isNew)
					{
						List<BoundingShapeConfigurable> shapes = BoundingShape.getConfigurableShapeList(shape);
						int index = shapes.indexOf(shape);
						int newIndex = (index + 1) % shapes.size();
						if (index != newIndex)
						{
							shape = shapes.get(newIndex);
							isNew = true;
							player.sendMessage(new TranslatableComponent("fecore.shapetool.mode", new TranslatableComponent(shape.getUnlocalizedName())), Util.NIL_UUID);
						}
					}
				}
				else if (!isNew) posIndex = shape.removePosition(player, posIndex);
				if (isNew) tool.setShape(shape);
				else tool.setConfigurationIndex(posIndex);
			});
			
		}
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}
	
	/*
	//TODO we need a better method
	@Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
		if (entity instanceof ServerPlayer)
		{
			LazyOptional<IShapeHolder> cap = stack.getCapability(FECoreCapabilities.SHAPE_HOLDER);
			if (cap.isPresent())
			{
				IShapeHolder holder = cap.resolve().get();
				BoundingShape shape = holder.getShape();
				if (shape == null) shape = new BoundingShapeBoxPositions();
				ServerPlayer player = (ServerPlayer) entity;
				FECoreNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new ShapeToolScreenPacket(player.position(), player.getMainHandItem() == stack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, shape));
			}
		}
        return true;
    }
    */
	
	public static BoundingShape getShape(ItemStack stack)
	{
		LazyOptional<IShapeHolder> cap = stack.getCapability(FECoreCapabilities.SHAPE_HOLDER);
		return cap.isPresent() ? cap.resolve().get().getShape() : null;
	}

    @Override
    public Component getName(ItemStack stack)
    {
    	BoundingShape shape = getShape(stack);
    	return new TranslatableComponent(this.getDescriptionId(stack), shape == null ? new TranslatableComponent("fecore.shape.none") : shape.getLocalizedName());
    }

	@Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.add_pos"));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.remove_pos"));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.change_mode"));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.use_block"));
		tooltip.add(new TranslatableComponent("fecore.shapetool.tooltip.open_gui"));
		BoundingShape shape = getShape(stack);
		if (shape instanceof BoundingShapeConfigurable) ((BoundingShapeConfigurable) shape).addInformation(stack, worldIn, tooltip, flagIn);
    }
}