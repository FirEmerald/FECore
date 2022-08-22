package com.firemerald.fecore.util;

import java.util.function.Predicate;

import org.apache.maven.artifact.versioning.ArtifactVersion;

public abstract class CompatProviderBase implements ICompatProvider
{
	public final String modId;
	public final Predicate<ArtifactVersion> version;
	protected boolean isPresent = false;

	public CompatProviderBase(String modId, Predicate<ArtifactVersion> version)
	{
		this.modId = modId;
		this.version = version;
	}

	public CompatProviderBase(String modId)
	{
		this.modId = modId;
		this.version = v -> true;
	}

	public CompatProviderBase(String modId, ArtifactVersion minVersion)
	{
		this.modId = modId;
		this.version = v -> minVersion.compareTo(v) >= 0;
	}

	public CompatProviderBase(String modId, ArtifactVersion minVersion, ArtifactVersion maxVersion)
	{
		this.modId = modId;
		this.version = v -> minVersion.compareTo(v) >= 0 && v.compareTo(maxVersion) >= 0;
	}
	
	public String getModID()
	{
		return modId;
	}
	
	public boolean isValidVersion(ArtifactVersion version)
	{
		return this.version.test(version);
	}

	@Override
	public boolean isPresent()
	{
		return isPresent;
	}

	@Override
	public void setPresent()
	{
		isPresent = true;
		registerEventHandlers();
	}
	
	public abstract void registerEventHandlers();
}