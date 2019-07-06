package com.maniac.tech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapeCube;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockGenerator extends Block {
    private static final VoxelShape AABB_COLLISION = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);
    private static final BooleanProperty BURNING = BooleanProperty.create("burning");

    public BlockGenerator() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5.0f).lightValue(0));
        setRegistryName("GENERATOR");
        this.setDefaultState(this.stateContainer.getBaseState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH).with(BURNING, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB_COLLISION;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof INamedContainerProvider) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
            return true;
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(stateIn.get(BURNING)){
            double x = ((double) pos.getX() + 0.2) + (rand.nextFloat() * 0.5);
            double y = (double) pos.getY() + 0.5;
            double z = ((double) pos.getZ() + 0.2) + (rand.nextFloat() * 0.5);
            if(rand.nextFloat() < 0.2)
                worldIn.playSound(x, y, z, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.10F, 0.1F, false);

            double x1 = ((double) pos.getX() + 0.5);
            double y1 = (double) pos.getY() + 0.4;
            double z1 = ((double) pos.getZ() + 0.5);
            worldIn.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.05D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, x + 0.05, y, z + 0.05, 0.0D, 0.05D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, x1, y1, z1, 0.0D, 0D, 0.0D);
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockGeneratorTileEntity();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.HORIZONTAL_FACING, BURNING);
    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        worldIn.setBlockState(pos, worldIn.getBlockState(pos).with(BURNING, true));
    }

    @Override
    public int getLightValue(BlockState state) {
        return (state.get(BURNING)) ? 12 : 0;
    }
}
