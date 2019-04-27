package twistedgate.immersiveposts;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import twistedgate.immersiveposts.common.blocks.BlockMetalFence;
import twistedgate.immersiveposts.common.blocks.BlockPost;
import twistedgate.immersiveposts.common.blocks.BlockPostBase;
import twistedgate.immersiveposts.common.items.MetalRods;
import twistedgate.immersiveposts.common.items.MultiMetaItem;
import twistedgate.immersiveposts.enums.EnumPostMaterial;
import twistedgate.immersiveposts.utils.StringUtils;

@Mod.EventBusSubscriber(modid=IPOMod.ID)
public class IPOStuff{
	public static final ArrayList<Block> BLOCKS=new ArrayList<>();
	public static final ArrayList<Item> ITEMS=new ArrayList<>();
	
	public static final BlockFence ironFence;
	public static final BlockFence goldFence;
//	public static final BlockFence diamondFence;
	public static final BlockFence copperFence;
	public static final BlockFence leadFence;
	public static final BlockFence silverFence;
	public static final BlockFence nickelFence;
	public static final BlockFence constantanFence;
	public static final BlockFence electrumFence;
	public static final BlockFence uraniumFence;
	
	public static final BlockPostBase postBase;
	public static final BlockPost woodPost;
	public static final BlockPost ironPost;
	public static final BlockPost goldPost;
//	public static final BlockPost diamondPost;
	public static final BlockPost copperPost;
	public static final BlockPost leadPost;
	public static final BlockPost silverPost;
	public static final BlockPost nickelPost;
	public static final BlockPost constantanPost;
	public static final BlockPost electrumPost;
	public static final BlockPost uraniumPost;
	public static final BlockPost netherPost;
	public static final BlockPost aluPost;
	public static final BlockPost steelPost;
	
	public static final MultiMetaItem multiItemTest;
	
	static{
		postBase=(BlockPostBase)new BlockPostBase();
		
		// =========================================================================
		// Fences
		
		ironFence=new BlockMetalFence("fence_iron");
		goldFence=new BlockMetalFence("fence_gold");
//		diamondFence=new BlockMetalFence("fence_diamond");
		copperFence=new BlockMetalFence("fence_copper");
		leadFence=new BlockMetalFence("fence_lead");
		silverFence=new BlockMetalFence("fence_silver");
		nickelFence=new BlockMetalFence("fence_nickel");
		constantanFence=new BlockMetalFence("fence_constantan");
		electrumFence=new BlockMetalFence("fence_electrum");
		uraniumFence=new BlockMetalFence("fence_uranium");
		
		// =========================================================================
		// Posts
		
		woodPost		=new BlockPost(Material.WOOD, EnumPostMaterial.WOOD);
		
		ironPost		=createMetalPost(EnumPostMaterial.IRON);
		goldPost		=createMetalPost(EnumPostMaterial.GOLD);
//		diamondPost		=createMetalPost(EnumPostMaterial.DIAMOND);
		copperPost		=createMetalPost(EnumPostMaterial.COPPER);
		leadPost		=createMetalPost(EnumPostMaterial.LEAD);
		silverPost		=createMetalPost(EnumPostMaterial.SILVER);
		nickelPost		=createMetalPost(EnumPostMaterial.NICKEL);
		constantanPost	=createMetalPost(EnumPostMaterial.CONSTANTAN);
		electrumPost	=createMetalPost(EnumPostMaterial.ELECTRUM);
		uraniumPost		=createMetalPost(EnumPostMaterial.URANIUM);
		netherPost		=createMetalPost(EnumPostMaterial.NETHERBRICK);
		aluPost			=createMetalPost(EnumPostMaterial.ALUMINIUM);
		steelPost		=createMetalPost(EnumPostMaterial.STEEL);
		
		// =========================================================================
		// Items
		
		multiItemTest=new MetalRods();
		
	}
	
	private static BlockPost createMetalPost(EnumPostMaterial postMat){
		return new BlockPost(Material.IRON, postMat);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event){
		for(Block block:BLOCKS){
			event.getRegistry().register(block);
		}
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event){
		for(Item item:ITEMS){
			event.getRegistry().register(item);
		}
		
		registerFenceOres();
		registerStickOres();
	}
	
	private static void registerFenceOres(){
		String prefix="fence";
		for(EnumPostMaterial mat:EnumPostMaterial.values()){
			switch(mat){
				case WOOD:case NETHERBRICK:case IRON:case ALUMINIUM:case STEEL:continue;
				default:
					OreDictionary.registerOre(prefix+StringUtils.upperCaseFirst(mat.toString()), mat.getFenceBlock());
			}
		}
	}
	
	private static void registerStickOres(){
		String prefix="stick";
		String rep="stick_";
		for(Item item:ITEMS){
			if(item instanceof MetalRods){
				MultiMetaItem mItem=(MultiMetaItem)item;
				for(int i=0;i<mItem.getSubItemCount();i++){
					if(mItem.getName(i).contains(rep)){
						String oreName=prefix+StringUtils.upperCaseFirst(mItem.getName(i).substring(rep.length()));
						
						OreDictionary.registerOre(oreName, new ItemStack(mItem, 1, i));
					}
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void regModels(ModelRegistryEvent event){
		for(Block block:BLOCKS){
			if(!(block instanceof BlockPost)){ // Prevent posts from getting an item
				Item item=Item.getItemFromBlock(block);
				ModelResourceLocation loc=new ModelResourceLocation(block.getRegistryName(), "inventory");
				ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			}
		}
		
		for(Item item:ITEMS){
			if(item instanceof ItemBlock) continue;
			
			if(item instanceof MultiMetaItem){
				MultiMetaItem mItem=(MultiMetaItem)item;
				for(int i=0;i<mItem.getSubItemCount();i++){
					ResourceLocation loc=new ResourceLocation(IPOMod.ID, mItem.regName+"/"+mItem.getName(i));
					ModelBakery.registerItemVariants(mItem, loc);
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(loc, "inventory"));
				}
				
			}else{
				ModelResourceLocation loc=new ModelResourceLocation(item.getRegistryName(), "inventory");
				ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			}
		}
	}
}
