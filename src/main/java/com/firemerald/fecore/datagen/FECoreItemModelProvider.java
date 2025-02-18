package com.firemerald.fecore.datagen;

import com.firemerald.fecore.FECoreMod;
import com.firemerald.fecore.init.FECoreObjects;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FECoreItemModelProvider extends ItemModelProvider {
	public FECoreItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, FECoreMod.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerModels() {
		this.basicItem(FECoreObjects.SHAPE_TOOL.getItem());
	}

}
