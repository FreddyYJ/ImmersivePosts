package twistedgate.immersiveposts.common.data;

import blusunrize.immersiveengineering.common.blocks.IEBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.common.IPOContent.Blocks.Fences;
import twistedgate.immersiveposts.common.IPOTags;

public class IPOBlockTags extends BlockTagsProvider{
	public IPOBlockTags(DataGenerator dataGen, ExistingFileHelper exFileHelper) {
		super(dataGen, IPOMod.ID, exFileHelper);
	}
	
	@Override
	protected void registerTags(){
		getOrCreateBuilder(IPOTags.IGNORED_BY_POSTARM)
			.add(IEBlocks.Connectors.postTransformer)
			.add(IEBlocks.Connectors.transformer)
			.add(IEBlocks.Connectors.transformerHV);
		
		getOrCreateBuilder(IPOTags.Fences.ALL)
			.add(Fences.iron)
			.add(Fences.gold)
			.add(Fences.copper)
			.add(Fences.lead)
			.add(Fences.silver)
			.add(Fences.nickel)
			.add(Fences.constantan)
			.add(Fences.electrum)
			.add(Fences.uranium);
	}
}
