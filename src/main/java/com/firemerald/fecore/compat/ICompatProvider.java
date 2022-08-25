package com.firemerald.fecore.compat;

import org.apache.maven.artifact.versioning.ArtifactVersion;

import net.minecraftforge.forgespi.language.IModInfo;

public interface ICompatProvider
{
	public String getModID();

	public boolean isValidVersion(ArtifactVersion version);

	default public boolean isValid(IModInfo modInfo)
	{
		return modInfo.getModId().equals(getModID()) && isValidVersion(modInfo.getVersion());
	}

	public boolean isPresent();

	public void setPresent();
}