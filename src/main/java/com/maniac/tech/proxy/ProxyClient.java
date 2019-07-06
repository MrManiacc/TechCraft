package com.maniac.tech.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ProxyClient implements IProxy{
    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }
}
