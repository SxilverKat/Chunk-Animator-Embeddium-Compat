package dev.sxilverr.chunkanimatorembeddiumcompat.animation;

import dev.sxilverr.chunkanimatorembeddiumcompat.config.ChunkAnimatorEmbeddiumCompatConfig;
import dev.sxilverr.chunkanimatorembeddiumcompat.config.EasingFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.SectionPos;

import java.util.concurrent.ConcurrentHashMap;

public final class SectionAnimationTracker {

    private static final ConcurrentHashMap<Long, Long> startTimes = new ConcurrentHashMap<>();

    private SectionAnimationTracker() {}

    public static void markBuilt(int chunkX, int chunkY, int chunkZ) {
        if (!isConfigReady() || !ChunkAnimatorEmbeddiumCompatConfig.ENABLED.get()) {
            return;
        }
        if (isInsidePlayerRadius(chunkX, chunkY, chunkZ)) {
            return;
        }
        startTimes.putIfAbsent(SectionPos.asLong(chunkX, chunkY, chunkZ), System.currentTimeMillis());
    }

    public static void clear(int chunkX, int chunkY, int chunkZ) {
        startTimes.remove(SectionPos.asLong(chunkX, chunkY, chunkZ));
    }

    public static boolean isAnimating(int chunkX, int chunkY, int chunkZ) {
        Long start = startTimes.get(SectionPos.asLong(chunkX, chunkY, chunkZ));
        if (start == null) {
            return false;
        }
        return System.currentTimeMillis() - start < durationMs();
    }

    public static float getOffsetX(int chunkX, int chunkY, int chunkZ) {
        return computeOffset(chunkX, chunkY, chunkZ, ChunkAnimatorEmbeddiumCompatConfig.START_OFFSET_X.get().floatValue());
    }

    public static float getOffsetY(int chunkX, int chunkY, int chunkZ) {
        return computeOffset(chunkX, chunkY, chunkZ, ChunkAnimatorEmbeddiumCompatConfig.START_OFFSET_Y.get().floatValue());
    }

    public static float getOffsetZ(int chunkX, int chunkY, int chunkZ) {
        return computeOffset(chunkX, chunkY, chunkZ, ChunkAnimatorEmbeddiumCompatConfig.START_OFFSET_Z.get().floatValue());
    }

    private static float computeOffset(int chunkX, int chunkY, int chunkZ, float startOffset) {
        if (startOffset == 0.0f) {
            return 0.0f;
        }
        long key = SectionPos.asLong(chunkX, chunkY, chunkZ);
        Long start = startTimes.get(key);
        if (start == null) {
            return 0.0f;
        }
        long elapsed = System.currentTimeMillis() - start;
        long duration = durationMs();
        if (elapsed >= duration) {
            startTimes.remove(key);
            return 0.0f;
        }
        float t = elapsed / (float) duration;
        EasingFunction easing = ChunkAnimatorEmbeddiumCompatConfig.EASING.get();
        float eased = easing.apply(t);
        return startOffset * (1.0f - eased);
    }

    private static long durationMs() {
        if (!isConfigReady()) {
            return 250L;
        }
        return ChunkAnimatorEmbeddiumCompatConfig.ANIMATION_DURATION_MS.get();
    }

    private static boolean isConfigReady() {
        return ChunkAnimatorEmbeddiumCompatConfig.SPEC.isLoaded();
    }

    private static boolean isInsidePlayerRadius(int chunkX, int chunkY, int chunkZ) {
        if (!ChunkAnimatorEmbeddiumCompatConfig.DISABLE_AROUND_PLAYER.get()) {
            return false;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        }
        int radius = ChunkAnimatorEmbeddiumCompatConfig.PLAYER_RADIUS.get();
        if (radius <= 0) {
            return false;
        }
        double cx = (chunkX << 4) + 8.0;
        double cy = (chunkY << 4) + 8.0;
        double cz = (chunkZ << 4) + 8.0;
        double dx = cx - player.getX();
        double dy = cy - player.getY();
        double dz = cz - player.getZ();
        double distSq = dx * dx + dy * dy + dz * dz;
        return distSq <= (double) radius * radius;
    }
}
