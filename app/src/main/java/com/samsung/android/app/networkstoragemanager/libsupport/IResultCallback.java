package com.samsung.android.app.networkstoragemanager.libsupport;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IResultCallback extends IInterface {
    void onError(long var1, int var3, int var4, Bundle var5) throws RemoteException;

    void onSuccess(long var1, int var3, Bundle var4) throws RemoteException;

    abstract class Stub extends Binder implements IResultCallback {
        private static final String DESCRIPTOR = "com.samsung.android.app.networkstoragemanager.libsupport.IResultCallback";

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IResultCallback asInterface(IBinder var0) {
            if (var0 == null) {
                return null;
            } else {
                IInterface var1 = var0.queryLocalInterface(DESCRIPTOR);
                return var1 != null && var1 instanceof IResultCallback ? (IResultCallback) var1 : new Proxy(var0);
            }
        }

        public static IResultCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
            Object var5 = null;
            Object var6 = null;
            long var7;
            Bundle var9;
            if (var1 != 1) {
                if (var1 != 2) {
                    if (var1 != 1598968902) {
                        return super.onTransact(var1, var2, var3, var4);
                    } else {
                        var3.writeString(DESCRIPTOR);
                        return true;
                    }
                } else {
                    var2.enforceInterface(DESCRIPTOR);
                    var7 = var2.readLong();
                    var4 = var2.readInt();
                    var1 = var2.readInt();
                    var9 = (Bundle) var6;
                    if (var2.readInt() != 0) {
                        var9 = Bundle.CREATOR.createFromParcel(var2);
                    }

                    this.onError(var7, var4, var1, var9);
                    return true;
                }
            } else {
                var2.enforceInterface(DESCRIPTOR);
                var7 = var2.readLong();
                var1 = var2.readInt();
                var9 = (Bundle) var5;
                if (var2.readInt() != 0) {
                    var9 = Bundle.CREATOR.createFromParcel(var2);
                }

                this.onSuccess(var7, var1, var9);
                return true;
            }
        }

        private static class Proxy implements IResultCallback {
            public static IResultCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder var1) {
                this.mRemote = var1;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onError(long var1, int var3, int var4, Bundle var5) {
                Parcel var6 = Parcel.obtain();

                label215:
                {
                    try {
                        var6.writeInterfaceToken(DESCRIPTOR);
                        var6.writeLong(var1);
                        var6.writeInt(var3);
                        var6.writeInt(var4);
                    } catch (Throwable var26) {
                        break label215;
                    }

                    if (var5 != null) {
                        try {
                            var6.writeInt(1);
                            var5.writeToParcel(var6, 0);
                        } catch (Throwable var25) {
                            break label215;
                        }
                    } else {
                        try {
                            var6.writeInt(0);
                        } catch (Throwable var24) {
                            break label215;
                        }
                    }

                    label201:
                    {
                        try {
                            if (!this.mRemote.transact(2, var6, (Parcel) null, 1) && Stub.getDefaultImpl() != null) {
                                Stub.getDefaultImpl().onError(var1, var3, var4, var5);
                                break label201;
                            }
                        } catch (Throwable var23) {
                            break label215;
                        }

                        var6.recycle();
                        return;
                    }

                    var6.recycle();
                    return;
                }

                var6.recycle();
            }

            public void onSuccess(long var1, int var3, Bundle var4) {
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
                                Stub.getDefaultImpl().onSuccess(var1, var3, var4);
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
