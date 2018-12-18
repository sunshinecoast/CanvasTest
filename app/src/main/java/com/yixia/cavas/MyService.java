package com.yixia.cavas;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by zhaoxiaopo on 2018/6/20.
 */
public class MyService extends Service {

    private IBinder mIBinder;


    @Override
    public void onCreate() {
        super.onCreate();

        mIBinder = new IMyAidlInterface.Stub() {
            @Override
            public int basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                    double aDouble,
                    String aString) {
                return 0;
            }
        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return super.bindService(service, conn, flags);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }


}
