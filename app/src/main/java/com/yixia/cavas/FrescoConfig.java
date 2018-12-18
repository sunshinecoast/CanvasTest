package com.yixia.cavas;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaoxiaopo on 2018/7/16.
 */
public class FrescoConfig {
    private static final int MAX_DISK_CACHE_VERYLOW_SIZE = 10485760;
    private static final int MAX_DISK_CACHE_LOW_SIZE = 52428800;
    private static final int MAX_DISK_CACHE_SIZE = 83886080;

    public FrescoConfig() {
    }

    public void initFrescoConfig(Context context) {
        File baseDirectory = this.getBaseDirectory(context);
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context.getApplicationContext())
                .setBaseDirectoryPath(baseDirectory)
                .setBaseDirectoryName("s_image_cache").build();
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context.getApplicationContext())
                .setBaseDirectoryPath(baseDirectory)
                .setBaseDirectoryName("image_cache")
                .setMaxCacheSize(83886080L)
                .setMaxCacheSizeOnLowDiskSpace(52428800L)
                .setMaxCacheSizeOnVeryLowDiskSpace(10485760L)
                .build();
        MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            public void trim(MemoryTrimType trimType) {
                double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio()
                        == suggestedTrimRatio ||
                        MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio()
                                == suggestedTrimRatio ||
                        MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio()
                                == suggestedTrimRatio) {
                    Fresco.getImagePipeline().clearMemoryCaches();
                    Log.d("zhaoxiaopo", "fresco自动清理完成");
                }

            }
        });
        Set<RequestListener> requestListeners = new HashSet();
        requestListeners.add(new RequestListener() {
            @Override
            public void onRequestStart(ImageRequest request, Object callerContext, String requestId,
                    boolean isPrefetch) {

            }

            @Override
            public void onRequestSuccess(ImageRequest request, String requestId,
                    boolean isPrefetch) {

            }

            @Override
            public void onRequestFailure(ImageRequest request, String requestId,
                    Throwable throwable,
                    boolean isPrefetch) {

            }

            @Override
            public void onRequestCancellation(String requestId) {

            }

            @Override
            public void onProducerStart(String requestId, String producerName) {

            }

            @Override
            public void onProducerEvent(String requestId, String producerName, String eventName) {

            }

            @Override
            public void onProducerFinishWithSuccess(String requestId, String producerName,
                    @Nullable Map<String, String> extraMap) {

            }

            @Override
            public void onProducerFinishWithFailure(String requestId, String producerName,
                    Throwable t,
                    @Nullable Map<String, String> extraMap) {

            }

            @Override
            public void onProducerFinishWithCancellation(String requestId, String producerName,
                    @Nullable Map<String, String> extraMap) {

            }

            @Override
            public void onUltimateProducerReached(String requestId, String producerName,
                    boolean successful) {

            }

            @Override
            public boolean requiresExtraMap(String requestId) {
                return false;
            }
        });
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                .setBitmapMemoryCacheParamsSupplier(new FrescoConfig.FrescoCacheParams((ActivityManager) context.getSystemService("activity")))
                .setDownsampleEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .setRequestListeners(requestListeners)
                .build();
        Fresco.initialize(context.getApplicationContext(), imagePipelineConfig);
    }

    private File getBaseDirectory(Context context) {
        return SDCardUtils.isSDCardEnable() ? context.getExternalCacheDir() : context.getCacheDir();
    }

    private class FrescoCacheParams implements Supplier<MemoryCacheParams> {
        private ActivityManager activityManager;

        private FrescoCacheParams(ActivityManager activityManager) {
            this.activityManager = activityManager;
        }

        public MemoryCacheParams get() {
            return Build.VERSION.SDK_INT >= 21 ? new MemoryCacheParams(this.getMaxCacheSize(), 64,
                    20971520, 200, 1048576) : new MemoryCacheParams(this.getMaxCacheSize(), 125,
                    2147483647, 2147483647, 2147483647);
        }

        private int getMaxCacheSize() {
            int maxMemory = Math.min(this.activityManager.getMemoryClass() * 1048576, 2147483647);
            if (maxMemory < 33554432) {
                return 4194304;
            } else {
                return maxMemory < 67108864 ? 6291456 : maxMemory / 6;
            }
        }
    }
}
