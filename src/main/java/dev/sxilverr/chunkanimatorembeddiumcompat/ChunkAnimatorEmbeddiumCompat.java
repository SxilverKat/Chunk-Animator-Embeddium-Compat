package dev.sxilverr.chunkanimatorembeddiumcompat;

import dev.sxilverr.chunkanimatorembeddiumcompat.config.ChunkAnimatorEmbeddiumCompatConfig;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;

@Mod(ChunkAnimatorEmbeddiumCompat.MOD_ID)
public final class ChunkAnimatorEmbeddiumCompat {

    public static final String MOD_ID = "chunkanimatorembeddiumcompat";

    public ChunkAnimatorEmbeddiumCompat() {
        ModLoadingContext context = ModLoadingContext.get();
        context.registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true)
        );
        context.registerConfig(ModConfig.Type.CLIENT, ChunkAnimatorEmbeddiumCompatConfig.SPEC);
    }
}
