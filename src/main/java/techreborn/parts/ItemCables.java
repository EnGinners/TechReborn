package techreborn.parts;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;
import techreborn.items.ItemTextureBase;
import techreborn.lib.ModInfo;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by Mark on 27/02/2016.
 */
public class ItemCables extends ItemMultiPart implements ITexturedItem {
    
    public static ItemStack getCableByName(String name, int count) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equalsIgnoreCase(name)) {
                return new ItemStack(TechRebornParts.cables, count, i);
            }
        }
        throw new InvalidParameterException("The cabel " + name + " could not be found.");
    }

    public static ItemStack getCableByName(String name) {
        return getCableByName(name, 1);
    }

    public static final String[] types = new String[]
            {"copper", "tin", "gold", "hv", "glassfiber", "insulatedcopper", "insulatedgold", "insulatedhv"};
    //NOTE FOR MODMUSS copper tin and gold cables that are not insulated should hurt the player if they tuch them 


    public ItemCables() {
        setCreativeTab(TechRebornCreativeTab.instance);
        setHasSubtypes(true);
        setUnlocalizedName("techreborn.cable");
        setNoRepair();
        RebornCore.jsonDestroyer.registerObject(this);
    }

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3 hit, ItemStack stack, EntityPlayer player) {
        return new CableMultipart();
    }


    @Override
    // gets Unlocalized Name depending on meta data
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= types.length) {
            meta = 0;
        }

        return super.getUnlocalizedName() + "." + types[meta];
    }

    // Adds Dusts SubItems To Creative Tab
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < types.length; ++meta) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    @Override
    public String getTextureName(int damage) {
        return ModInfo.MOD_ID + ":items/cables/" + types[damage];
    }

    @Override
    public int getMaxMeta() {
        return types.length;
    }

    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
        return new ModelResourceLocation(ModInfo.MOD_ID + ":" + getUnlocalizedName(stack).substring(5), "inventory");
    }
}