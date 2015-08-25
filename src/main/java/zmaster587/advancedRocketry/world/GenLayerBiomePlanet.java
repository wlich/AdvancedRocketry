package zmaster587.advancedRocketry.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import zmaster587.advancedRocketry.AdvancedRocketry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

public class GenLayerBiomePlanet extends GenLayer {

	private List<BiomeGenBase> biomes;
	private static List<BiomeEntry> biomeEntries;

	int biomeLimiter = -1;

	public GenLayerBiomePlanet(long p_i2122_1_, GenLayer p_i2122_3_, WorldType worldType)
	{
		super(p_i2122_1_);

		this.parent = p_i2122_3_;

		biomeEntries = new ArrayList<BiomeManager.BiomeEntry>();

	}

	//Used to set the usableBiomes
	public static synchronized void setupBiomesForUse(List<BiomeEntry> entries) {
		biomeEntries = entries;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	 * amounts, or biomeList[] indices based on the particular GenLayer subclass.
	 * @param properties 
	 */
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_)
	{
		//int[] aint = this.parent.getInts(p_75904_1_, p_75904_2_, p_75904_3_, p_75904_4_);
		int[] aint1 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);
		
		for (int i1 = 0; i1 < p_75904_4_; ++i1)
		{
			for (int j1 = 0; j1 < p_75904_3_; ++j1)
			{
				this.initChunkSeed((long)(j1 + p_75904_1_), (long)(i1 + p_75904_2_));
				
				aint1[j1 + i1 * p_75904_3_] = getWeightedBiomeEntry().biome.biomeID;
			
			}
		}

		//TODO: DEBUG:
		//Arrays.fill(aint1, BiomeGenBase.desert.biomeID);
		
		return aint1;
	}

	protected BiomeEntry getWeightedBiomeEntry()
	{
		if(biomeEntries == null || biomeEntries.isEmpty())
			return new BiomeEntry(BiomeGenBase.ocean, 100);

		List<BiomeEntry> biomeList = biomeEntries;
		int totalWeight = WeightedRandom.getTotalWeight(biomeList);
		int weight = nextInt(totalWeight / 10) * 10;
		return (BiomeEntry)WeightedRandom.getItem(biomeList, weight);
	}
}