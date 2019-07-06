package com.maniac.tech.blocks;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class BlockGeneratorTileEntity extends TileEntity implements ITickableTileEntity {

    public BlockGeneratorTileEntity() {
        super(Blocks.GENERATOR_TILE);
        System.out.println("POS: " + pos.toString());
    }

    @Override
    public void tick() {
        System.out.println("Yum");
    }


}
