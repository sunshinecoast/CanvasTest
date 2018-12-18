package com.yixia.cavas;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.DiskStorageCacheFactory;
import com.facebook.imagepipeline.core.FileCacheFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.Map;

import okhttp3.internal.cache.CacheRequest;

/**
 * Created by 庞永康 on 2018/6/21.
 */
public class FrescoCacheUtil {

    private FrescoCacheUtil() {
    }

    /**
     * 预加载图片可减少用户等待的时间，如果预加载的图片用户没有真正呈现给用户，那么就浪费了用户的流量，电量，
     * 内存等资源了。大多数应用，并不需要预加载。
     * 需要注意的是，你可以预加载图片到磁盘、内存缓存中，但是它们在并不会被立即解码（除非马上要显示），
     * 这样会节省一部分CPU操作。
     *
     * @param url
     */
    @Deprecated
    public static void prefetchBitmap(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                //.setResizeOptions(new ResizeOptions(75, 75))
                .build();
        Fresco.getImagePipeline().prefetchToBitmapCache(request, null);
    }

    private static boolean existBitmap(String url) {
        if (TextUtils.isEmpty(url)) {
            // 如果URL为空，可能是非图片消息类型，不能直接过滤
            return false;
        }

        boolean result = Fresco.getImagePipeline().isInBitmapMemoryCache(ImageRequest.fromUri(url));
//        Log.i("FrescoCacheUtil", "image exist in memory cache --->> " + result);
        return result;
//        return Fresco.getImagePipeline().isInBitmapMemoryCache(Uri.parse(url));
    }

    /**
     * fetch the bitmap
     *
     * @param url
     * @param callback
     */
    public static void getBitmap(final String url, int width, int height, final IFetchCallback callback) {
        if (callback == null) {
            return;
        }
        if (existBitmap(url)) {
            callback.loadSucess(getBitmapFromCache(url, width, height));
        } else {
            downLoadUrl(url, width, height, callback);
        }
    }

    /**
     * 获取缓存图片
     *
     * @param url 图片地址
     * @return 图片
     */
    @Nullable
    private static Bitmap getBitmapFromCache(final String url, int width, int height) {
//        Log.i("FrescoCacheUtil", "get image URL : " + url);
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url));
        if (width > 0 && height > 0) {
            imageRequestBuilder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        Bitmap result;
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequestBuilder.build(), null);
        CloseableReference<CloseableImage> imageReference = imagePipeline.getBitmapMemoryCache().get(cacheKey);
        try {
            if (imageReference == null) {
                cacheKey = DefaultCacheKeyFactory.getInstance().getPostprocessedBitmapCacheKey(imageRequestBuilder.build(), null);
                imageReference = imagePipeline.getBitmapMemoryCache().get(cacheKey);
            }
            if (imageReference != null) {
//                Log.i("FrescoCacheUtil", "get image success!!!!");
                CloseableImage image = imageReference.get();
//                try {
                    if (image instanceof CloseableBitmap) {
//                        result = Bitmap.createBitmap(((CloseableBitmap) image).getUnderlyingBitmap());
                        return ((CloseableBitmap) image).getUnderlyingBitmap();
                    }
//                } finally {
                // 这里容易导致 "ava.lang.RuntimeException: Canvas: trying to use a recycled bitmap android.graphics"
//                    image.close();
//                }
            }
        } finally {
            CloseableReference.closeSafely(imageReference);
        }
        return null;
    }

    private static void downLoadUrl(final String url, final int width, final int height, final IFetchCallback loadImageCallBack) {
//        Log.i("FrescoCacheUtil", "download image : " + url);
        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true);
        if (width > 0 && height > 0) {
            imageRequestBuilder.setResizeOptions(new ResizeOptions(width, height));
        }
        final ImageRequest imageRequest = imageRequestBuilder.build();
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, null);
        /*
         * 1、UiThreadImmediateExecutorService.getInstance() : 如果你想要在回调中进行任何UI操作，你需要使用
         *
         * 2、CallerThreadExecutor.getInstance() : 如果回调里面做的事情比较少，并且不涉及UI，可以使用.
         * 这个 Executor 会在调用者线程中执行回调。这个回调的执行线程是得不到保证的，所以需要谨慎使用这个Executor。
         * 重申一遍，只有少量工作、没有UI操作的回调才适合在这个Executor中操作。
         */
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(Bitmap bitmap) {
//                Log.i("FrescoCacheUtil", "download image  success");
                if (loadImageCallBack != null) {
                    if (bitmap != null) {
                        loadImageCallBack.loadSucess(getBitmapFromCache(url, width, height));
                    } else if (loadImageCallBack != null) {
                        loadImageCallBack.loadFailure();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
//                Log.i("FrescoCacheUtil", "download image  fail");
                if (loadImageCallBack != null) {
                    loadImageCallBack.loadFailure();
                }
            }
        }, CallerThreadExecutor.getInstance());
    }

    public interface  IFetchCallback {
        /**
         * @param bitmap 缓存中的图片
         */
        void loadSucess(@Nullable Bitmap bitmap);

        void loadFailure();
    }
}
