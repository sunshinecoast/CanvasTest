package com.yixia.cavas;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by zhaoxiaopo on 2018/6/20.
 */
public class CustomBinder extends Binder implements CustomInterface {
    public static final String DESCRIPTOR = "";
    public CustomBinder() {
        attachInterface(this, DESCRIPTOR);
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags)
            throws RemoteException {
        switch (code) {
            case 1:
                data.enforceInterface(DESCRIPTOR);
                add(0, 0);
                reply.writeNoException();
                break;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public void add(int a, int b) {

    }

    @Override
    public IBinder asBinder() {
        return this;
    }
}
