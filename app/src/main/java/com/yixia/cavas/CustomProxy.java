package com.yixia.cavas;

import android.os.IBinder;
import android.os.IInterface;

/**
 * Created by zhaoxiaopo on 2018/6/20.
 */
public class CustomProxy {
    CustomBinder mCustomBinder = new CustomBinder();

    public CustomInterface getCustomBinder(IBinder binder) {
        if (binder == null) {
            return null;
        }
        IInterface iInterface = binder.queryLocalInterface("");
        if (iInterface != null && iInterface instanceof  CustomInterface) {
            return (CustomInterface) iInterface;
        }
        return new Proxy(binder);
    }

    public static class Proxy implements CustomInterface {

        private IBinder mIInterface;

        public Proxy(IBinder binder) {
            mIInterface = binder;
        }

        @Override
        public void add(int a, int b) {

        }

        public String getInterfaceDescriptor() {
            return "";
        }

        @Override
        public IBinder asBinder() {
            return mIInterface;
        }
    }
}
