package com.samsung.android.app.networkstoragemanager.libsupport;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IProgressCallback extends IInterface {
    void onProgress(long var1, int var3, Bundle var4) throws RemoteException;

    abstract class Stub extends Binder implements IProgressCallback {
        private static final String DESCRIPTOR = "com.samsung.android.app.networkstoragemanager.libsupport.IProgressCallback";

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IProgressCallback asInterface(IBinder var0) {
            if (var0 == null) {
                return null;
            } else {
                IInterface var1 = var0.queryLocalInterface(DESCRIPTOR);
                return var1 != null && var1 instanceof IProgressCallback ? (IProgressCallback) var1 : new Proxy(var0);
            }
        }

        public static IProgressCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
            if (var1 != 1) {
                if (var1 != 1598968902) {
                    return super.onTransact(var1, var2, var3, var4);
                } else {
                    var3.writeString(DESCRIPTOR);
                    return true;
                }
            } else {
                var2.enforceInterface(DESCRIPTOR);
                long var5 = var2.readLong();
                var1 = var2.readInt();
                Bundle var7;
                if (var2.readInt() != 0) {
                    var7 = Bundle.CREATOR.createFromParcel(var2);
                } else {
                    var7 = null;
                }

                this.onProgress(var5, var1, var7);
                return true;
            }
        }

        private static class Proxy implements IProgressCallback {
            public static IProgressCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder var1) {
                this.mRemote = var1;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onProgress(long var1, int var3, Bundle var4) {
                Parcel var5 = Parcel.obtain();

                label215:
                {
                    try {
                        var5.writeInterfaceToken(DESCRIPTOR);
                        var5.writeLong(var1);
                        var5.writeInt(var3);
                    } catch (Throwable var25) {
                        break label215;
                    }

                    if (var4 != null) {
                        try {
                            var5.writeInt(1);
                            var4.writeToParcel(var5, 0);
                        } catch (Throwable var24) {
                            break label215;
                        }
                    } else {
                        try {
                            var5.writeInt(0);
                        } catch (Throwable var23) {
                            break label215;
                        }
                    }

                    label201:
                    {
                        try {
                            if (!this.mRemote.transact(1, var5, (Parcel) null, 1) && Stub.getDefaultImpl() != null) {
                                Stub.getDefaultImpl().onProgress(var1, var3, var4);
                                break label201;
                            }
                        } catch (Throwable var22) {
                            break label215;
                        }

                        var5.recycle();
                        return;
                    }

                    var5.recycle();
                    return;
                }

                var5.recycle();
            }
        }
    }
}
