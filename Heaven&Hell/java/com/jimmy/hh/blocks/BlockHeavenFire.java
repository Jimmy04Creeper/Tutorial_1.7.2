package com.jimmy.hh.blocks;

import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.Random;

import javax.swing.Icon;

import com.jimmy.hh.reference.Reference;
import com.jimmy.hh.tabs.HHCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHeavenFire extends BlockFire
{
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;

	public BlockHeavenFire(String blockName)
	{
		super();
		setTickRandomly(true);
		this.setBlockName(blockName);
		setCreativeTab(HHCreativeTabs.hhItems);
		this.setLightLevel(1.0F);
	}

	public void initializeBlock()
	{
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.planks), 5, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.double_wooden_slab), 5, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.wooden_slab), 5, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.fence), 5, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.oak_stairs), 5, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.birch_stairs), 5, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.spruce_stairs), 5, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.jungle_stairs), 5, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.log), 5, 5);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.log2), 5, 5);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.leaves), 30, 60);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.leaves2), 30, 60);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.bookshelf), 30, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.tnt), 15, 100);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.tallgrass), 60, 100);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.double_plant), 60, 100);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.yellow_flower), 60, 100);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.red_flower), 60, 100);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.wool), 30, 60);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.vine), 15, 100);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.coal_block), 5, 5);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.hay_block), 60, 20);
		Blocks.fire.func_149842_a(getIdFromBlock(Blocks.carpet), 60, 20);
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		if (par1World.getGameRules().getGameRuleBooleanValue("doFireTick"))
		{
			boolean flag = par1World.getBlock(par2, par3 - 1, par4).isFireSource(par1World, par2, par3 - 1, par4, UP);
			if (!canPlaceBlockAt(par1World, par2, par3, par4))
			{
				par1World.setBlockToAir(par2, par3, par4);
			}
			if ((!flag) && (par1World.isRaining()) && ((par1World.canLightningStrikeAt(par2, par3, par4)) || (par1World.canLightningStrikeAt(par2 - 1, par3, par4)) || (par1World.canLightningStrikeAt(par2 + 1, par3, par4)) || (par1World.canLightningStrikeAt(par2, par3, par4 - 1)) || (par1World.canLightningStrikeAt(par2, par3, par4 + 1))))
			{
				par1World.setBlockToAir(par2, par3, par4);
			}
			else
			{
				int l = par1World.getBlockMetadata(par2, par3, par4);
				if (l < 15)
				{
					par1World.setBlockMetadataWithNotify(par2, par3, par4, l + par5Random.nextInt(3) / 2, 4);
				}
				par1World.scheduleBlockUpdate(par2, par3, par4, this, tickRate(par1World) + par5Random.nextInt(10));
				if ((!flag) && (!canNeighborBurn(par1World, par2, par3, par4)))
				{
					if ((!par1World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4)) || (l > 3))
					{
						par1World.setBlockToAir(par2, par3, par4);
					}
				}
				else if ((!flag) && (!canBlockCatchFire(par1World, par2, par3 - 1, par4, ForgeDirection.UP)) && (l == 15) && (par5Random.nextInt(4) == 0))
				{
					par1World.setBlockToAir(par2, par3, par4);
				}
				else
				{
					boolean flag1 = par1World.isBlockHighHumidity(par2, par3, par4);
					byte b0 = 0;
					if (flag1)
					{
						b0 = -50;
					}
					tryToCatchBlockOnFire(par1World, par2 + 1, par3, par4, 300 + b0, par5Random, l, ForgeDirection.WEST);
					tryToCatchBlockOnFire(par1World, par2 - 1, par3, par4, 300 + b0, par5Random, l, ForgeDirection.EAST);
					tryToCatchBlockOnFire(par1World, par2, par3 - 1, par4, 250 + b0, par5Random, l, ForgeDirection.UP);
					tryToCatchBlockOnFire(par1World, par2, par3 + 1, par4, 250 + b0, par5Random, l, ForgeDirection.DOWN);
					tryToCatchBlockOnFire(par1World, par2, par3, par4 - 1, 300 + b0, par5Random, l, ForgeDirection.SOUTH);
					tryToCatchBlockOnFire(par1World, par2, par3, par4 + 1, 300 + b0, par5Random, l, ForgeDirection.NORTH);
					for (int i1 = par2 - 1; i1 <= par2 + 1; i1++)
					{
						for (int j1 = par4 - 1; j1 <= par4 + 1; j1++)
						{
							for (int k1 = par3 - 1; k1 <= par3 + 4; k1++)
							{
								if ((i1 != par2) || (k1 != par3) || (j1 != par4))
								{
									int l1 = 100;
									if (k1 > par3 + 1)
									{
										l1 += (k1 - (par3 + 1)) * 100;
									}
									int i2 = getChanceOfNeighborsEncouragingFire(par1World, i1, k1, j1);
									if (i2 > 0)
									{
										int j2 = (i2 + 40 + par1World.difficultySetting.getDifficultyId() * 7) / (l + 30);
										if (flag1)
										{
											j2 /= 2;
										}
										if ((j2 > 0) && (par5Random.nextInt(l1) <= j2) && ((!par1World.isRaining()) || (!par1World.canLightningStrikeAt(i1, k1, j1))) && (!par1World.canLightningStrikeAt(i1 - 1, k1, par4)) && (!par1World.canLightningStrikeAt(i1 + 1, k1, j1)) && (!par1World.canLightningStrikeAt(i1, k1, j1 - 1)) && (!par1World.canLightningStrikeAt(i1, k1, j1 + 1)))
										{
											int k2 = l + par5Random.nextInt(5) / 4;
											if (k2 > 15)
											{
												k2 = 15;
											}
											par1World.setBlock(i1, k1, j1, this, k2, 3);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void tryToCatchBlockOnFire(World par1World, int par2, int par3, int par4, int par5, Random par6Random, int par7, ForgeDirection face) {
		int j1 = par1World.getBlock(par2, par3, par4).getFlammability(par1World, par2, par3, par4, face);
		if (par6Random.nextInt(par5) < j1)
		{
			boolean flag = par1World.getBlock(par2, par3, par4) == Blocks.tnt;

			if (par6Random.nextInt(par5) < j1)
			{
				if ((par6Random.nextInt(par7 + 10) < 5) && (!par1World.canLightningStrikeAt(par2, par3, par4)))
				{
					int k1 = par7 + par6Random.nextInt(5) / 4;
					if (k1 > 15)
					{
						k1 = 15;
					}
					par1World.setBlock(par2, par3, par4, this, k1, 3);
				}
				else
				{
					par1World.setBlockToAir(par2, par3, par4);
				}
				if (flag)
				{
					Blocks.tnt.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
				}
			}
		}
	}

	private boolean canNeighborBurn(World par1World, int par2, int par3, int par4)
	{
		return (canBlockCatchFire(par1World, par2 + 1, par3, par4, ForgeDirection.WEST)) ||
				(canBlockCatchFire(par1World, par2 - 1, par3, par4, ForgeDirection.EAST)) ||
				(canBlockCatchFire(par1World, par2, par3 - 1, par4, ForgeDirection.UP)) ||
				(canBlockCatchFire(par1World, par2, par3 + 1, par4, ForgeDirection.DOWN)) ||
				(canBlockCatchFire(par1World, par2, par3, par4 - 1, ForgeDirection.SOUTH)) ||
				(canBlockCatchFire(par1World, par2, par3, par4 + 1, ForgeDirection.NORTH));
	}

	private int getChanceOfNeighborsEncouragingFire(World par1World, int par2, int par3, int par4)
	{
		byte b0 = 0;
		if (!par1World.isAirBlock(par2, par3, par4))
		{
			return 0;
		}
		int l = getChanceToEncourageFire(par1World, par2 + 1, par3, par4, b0, ForgeDirection.WEST);
		l = getChanceToEncourageFire(par1World, par2 - 1, par3, par4, l, ForgeDirection.EAST);
		l = getChanceToEncourageFire(par1World, par2, par3 - 1, par4, l, ForgeDirection.UP);
		l = getChanceToEncourageFire(par1World, par2, par3 + 1, par4, l, ForgeDirection.DOWN);
		l = getChanceToEncourageFire(par1World, par2, par3, par4 - 1, l, ForgeDirection.SOUTH);
		l = getChanceToEncourageFire(par1World, par2, par3, par4 + 1, l, ForgeDirection.NORTH);
		return l;
	}

	public boolean isCollidable()
	{
		return false;
	}

	@Deprecated
	public boolean canBlockCatchFire(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		return canBlockCatchFire(par1IBlockAccess, par2, par3, par4, ForgeDirection.UP);
	}

	@Deprecated
	public int getChanceToEncourageFire(World par1World, int par2, int par3, int par4, int par5)
	{
		return getChanceToEncourageFire(par1World, par2, par3, par4, par5, ForgeDirection.UP);
	}

	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		return (par1World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4)) || (canNeighborBurn(par1World, par2, par3, par4));
	}

	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	{
		if ((!par1World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4)) && (!canNeighborBurn(par1World, par2, par3, par4)))
		{
			par1World.setBlockToAir(par2, par3, par4);
		}
	}

	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		if(par1World.provider.dimensionId > 0 || !HHBlocks.heavenPortal.func_150000_e(par1World, par2, par3, par4)){
			if (!World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4) && !this.canNeighborBurn(par1World, par2, par3, par4))
            {
                par1World.setBlockToAir(par2, par3, par4);
            }
            else
            {
                par1World.scheduleBlockUpdate(par2, par3, par4, this, this.tickRate(par1World) + par1World.rand.nextInt(10));
            }
		}
		
		
		
		/** Change these to your portal frame and Portal block **/
		if ((par1World.getBlock(par2, par3 - 1, par4) != Blocks.diamond_block) || (!((BlockHeavenPortal) HHBlocks.heavenPortal).tryToCreatePortal(par1World, par2, par3, par4)))
		{
			if ((!par1World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4)) && (!canNeighborBurn(par1World, par2, par3, par4)))
			{
				par1World.setBlockToAir(par2, par3, par4);
			}
			else
			{
				par1World.scheduleBlockUpdate(par2, par3, par4, this, tickRate(par1World) + par1World.rand.nextInt(10));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		if (par5Random.nextInt(24) == 0)
		{
			par1World.playSound(par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "fire.fire", 1.0F + par5Random.nextFloat(), par5Random.nextFloat() * 0.7F + 0.3F, false);
		}
		if ((!par1World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4)) && (!((BlockHeavenFire) HHBlocks.heavenFire).canBlockCatchFire(par1World, par2, par3 - 1, par4, ForgeDirection.UP)))
		{
			if (((BlockHeavenFire) HHBlocks.heavenFire).canBlockCatchFire(par1World, par2 - 1, par3, par4, ForgeDirection.EAST))
			{
				for (int l = 0; l < 2; l++)
				{
					float f = par2 + par5Random.nextFloat() * 0.1F;
					float f1 = par3 + par5Random.nextFloat();
					float f2 = par4 + par5Random.nextFloat();
					par1World.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}
			if (((BlockHeavenFire) HHBlocks.heavenFire).canBlockCatchFire(par1World, par2 + 1, par3, par4, ForgeDirection.WEST))
			{
				for (int l = 0; l < 2; l++)
				{
					float f = par2 + 1 - par5Random.nextFloat() * 0.1F;
					float f1 = par3 + par5Random.nextFloat();
					float f2 = par4 + par5Random.nextFloat();
					par1World.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}
			if (((BlockHeavenFire) HHBlocks.heavenFire).canBlockCatchFire(par1World, par2, par3, par4 - 1, ForgeDirection.SOUTH))
			{
				for (int l = 0; l < 2; l++)
				{
					float f = par2 + par5Random.nextFloat();
					float f1 = par3 + par5Random.nextFloat();
					float f2 = par4 + par5Random.nextFloat() * 0.1F;
					par1World.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}
			if (((BlockHeavenFire) HHBlocks.heavenFire).canBlockCatchFire(par1World, par2, par3, par4 + 1, ForgeDirection.NORTH))
			{
				for (int l = 0; l < 2; l++)
				{
					float f = par2 + par5Random.nextFloat();
					float f1 = par3 + par5Random.nextFloat();
					float f2 = par4 + 1 - par5Random.nextFloat() * 0.1F;
					par1World.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}
			if (((BlockHeavenFire) HHBlocks.heavenFire).canBlockCatchFire(par1World, par2, par3 + 1, par4, ForgeDirection.DOWN))
			{
				for (int l = 0; l < 2; l++)
				{
					float f = par2 + par5Random.nextFloat();
					float f1 = par3 + 1 - par5Random.nextFloat() * 0.1F;
					float f2 = par4 + par5Random.nextFloat();
					par1World.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
				}
			}
		}
		else
		{
			for (int l = 0; l < 3; l++)
			{
				float f = par2 + par5Random.nextFloat();
				float f1 = par3 + par5Random.nextFloat() * 0.5F + 0.5F;
				float f2 = par4 + par5Random.nextFloat();
				par1World.spawnParticle("largesmoke", f, f1, f2, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public boolean canBlockCatchFire(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return canCatchFire(world, x, y, z, UP);
	}

	public int getChanceToEncourageFire(IBlockAccess world, int x, int y, int z, int oldChance, ForgeDirection face)
    {
        int newChance = world.getBlock(x, y, z).getFireSpreadSpeed(world, x, y, z, face);
        return (newChance > oldChance ? newChance : oldChance);
    }

	/** registers Icons, set textures here **/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + "heavenFire_0");
		
		this.iconArray = new IIcon[] { 
				par1IconRegister.registerIcon(Reference.MOD_ID + ":" + "heavenFire_0"), par1IconRegister.registerIcon(Reference.MOD_ID + ":" + "heavenFire_1")
				};
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getFireIcon(int p_149840_1_)
    {
        return this.iconArray[p_149840_1_];
    }


	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return this.iconArray[par1];
	}
}