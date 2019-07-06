package com.maniac.tech.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class Blocks {
    @ObjectHolder("techcraft:generator")
    public static BlockGenerator GENERATOR;

    @ObjectHolder("techcraft:generator")
    public static TileEntityType<?> GENERATOR_TILE;
}
