package com.maniac.tech.proxy;

import net.minecraft.world.World;

public class ProxyServer implements IProxy {

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("This method is client sided");
    }
}
