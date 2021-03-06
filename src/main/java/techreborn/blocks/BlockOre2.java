/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.blocks.PropertyString;
import reborncore.common.util.ArrayUtils;
import reborncore.common.util.StringUtils;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.world.config.IOreNameProvider;

import java.security.InvalidParameterException;
import java.util.List;

public class BlockOre2 extends Block implements IOreNameProvider {

	public static final String[] ores = new String[] { "copper", "tin" };
	static List<String> oreNamesList = Lists.newArrayList(ArrayUtils.arrayToLowercase(ores));
	public PropertyString VARIANTS;

	public BlockOre2() {
		super(Material.ROCK);
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(2.0f);
		setHarvestLevel("pickaxe", 1);
		this.setDefaultState(this.getStateFromMeta(0));
		for (int i = 0; i < ores.length; i++) {
			ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, i).setInvVariant("type=" + ores[i]).setFileName("ores"));
		}
		TRRecipeHandler.hideEntry(this);
	}

	public static ItemStack getOreByName(String name, int count) {
		for (int i = 0; i < ores.length; i++) {
			if (ores[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModBlocks.ORE2, count, i);
			}
		}
		throw new InvalidParameterException("The ore block " + name + " could not be found.");
	}

	public static ItemStack getOreByName(String name) {
		return getOreByName(name, 1);
	}

	public IBlockState getBlockStateFromName(String name) {
		int index = -1;
		for (int i = 0; i < ores.length; i++) {
			if (ores[i].equalsIgnoreCase(name)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			throw new InvalidParameterException("The ore block " + name + " could not be found.");
		}
		return getStateFromMeta(index);
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		for (int meta = 0; meta < ores.length; meta++) {
			list.add(new ItemStack(this, 1, meta));
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
	                              EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(state));
	}

	//	@Override
	//	public int damageDropped(IBlockState state)
	//	{
	//		int meta = getMetaFromState(state);
	//		if (meta == 2)
	//		{
	//			return 0;
	//		} else if (meta == 3)
	//		{
	//			return 1;
	//		} else if (meta == 5)
	//		{
	//			return 60;
	//		}
	//		return meta;
	//	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > ores.length) {
			meta = 0;
		}
		return getBlockState().getBaseState().withProperty(VARIANTS, oreNamesList.get(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return oreNamesList.indexOf(state.getValue(VARIANTS));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		VARIANTS = new PropertyString("type", oreNamesList);
		return new BlockStateContainer(this, VARIANTS);
	}

	@Override
	public String getUserLoclisedName(IBlockState state) {
		return StringUtils.toFirstCapital(oreNamesList.get(getMetaFromState(state)));
	}
}
