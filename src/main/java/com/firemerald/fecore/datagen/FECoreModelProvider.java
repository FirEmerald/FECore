package com.firemerald.fecore.datagen;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.init.FECoreObjects;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class FECoreModelProvider extends ModelProvider {
	public FECoreModelProvider(PackOutput output) {
		super(output, FECoreMod.MOD_ID);
	}

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    	itemModels.generateFlatItem(FECoreObjects.SHAPE_TOOL.getItem(), ModelTemplates.FLAT_ITEM);
    }

}
