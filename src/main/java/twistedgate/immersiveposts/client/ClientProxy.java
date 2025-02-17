package twistedgate.immersiveposts.client;

import com.electronwill.nightconfig.core.Config;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualElementTable;
import blusunrize.lib.manual.ManualEntry;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.Tree.InnerNode;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.DeferredWorkQueue;
import twistedgate.immersiveposts.IPOMod;
import twistedgate.immersiveposts.client.model.PostBaseLoader;
import twistedgate.immersiveposts.common.CommonProxy;
import twistedgate.immersiveposts.common.IPOConfig;
import twistedgate.immersiveposts.common.IPOContent;

/**
 * @author TwistedGate
 */
public class ClientProxy extends CommonProxy{
	
	@Override
	public void setup(){
		super.setup();
		
		ClientEventHandler handler = new ClientEventHandler();
		((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(handler);
		
		Minecraft.getInstance().getBlockColors().register(new ColorHandler(), IPOContent.Blocks.post_Base);
	}
	
	@Override
	public void construct(){
		super.construct();
		
		if(Minecraft.getInstance() != null){
			ModelLoaderRegistry.registerLoader(PostBaseLoader.LOCATION, new PostBaseLoader());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void completed(){
		DeferredWorkQueue.runLater(() -> ManualHelper.addConfigGetter(str -> {
			switch(str){
				case "maxTrussLength":{
					return IPOConfig.MAIN.maxTrussLength.get();
				}
				default:
					break;
			}
			
			// Last resort
			Config cfg = IPOConfig.getRawConfig();
			if(cfg.contains(str)){
				return cfg.get(str);
			}
			return null;
		}));
		
		
		setupManualPage();
	}
	
	public void setupManualPage(){
		/*
		 * TODO Give src/main/resources/assets/immersiveengineering/manual/autoload.json a shot
		 */
		
		ManualInstance man = ManualHelper.getManual();
		
		InnerNode<ResourceLocation, ManualEntry> cat = man.getRoot().getOrCreateSubnode(modLoc("main"), 100);
		
		man.addEntry(cat, modLoc("postbase"), 0);
		man.addEntry(cat, modLoc("usage"), 1);
		
		{
			ITextComponent[][] index0 = new ITextComponent[13][2];
			ITextComponent[][] index1 = new ITextComponent[4][2];
			
			int page = 1;
			for(int i = 0;i < index0.length;i++){
				index0[i][0] = new StringTextComponent(Integer.toString(page++));
				index0[i][1] = new TranslationTextComponent("index.page_0_entry." + Integer.toString(i + 1));
			}
			for(int i = 0;i < index1.length;i++){
				index1[i][0] = new StringTextComponent(Integer.toString(page++));
				index1[i][1] = new TranslationTextComponent("index.page_1_entry." + Integer.toString(i + 1));
			}
			
			ManualEntry.ManualEntryBuilder builder = new ManualEntry.ManualEntryBuilder(man);
			builder.addSpecialElement("index0", 0, new ManualElementTable(man, index0, false));
			builder.addSpecialElement("index1", 0, new ManualElementTable(man, index1, false));
			builder.readFromFile(modLoc("posts"));
			man.addEntry(cat, builder.create(), 2);
		}
	}
	
	private ResourceLocation modLoc(String str){
		return new ResourceLocation(IPOMod.ID, str);
	}
}
