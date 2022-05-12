package com.samsung.android.app.networkstoragemanager.libsupport;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public interface IRequestInterface extends IInterface {
    void asyncRequest(long var1, String var3, int var4, Bundle var5) throws RemoteException;

    boolean cancel(long var1) throws RemoteException;

    boolean registerProgressCallback(IProgressCallback var1) throws RemoteException;

    boolean registerResultCallback(IResultCallback var1) throws RemoteException;

    void retryRequest(long var1) throws RemoteException;

    Bundle syncRequest(long var1, String var3, int var4, Bundle var5) throws RemoteException;

    boolean unregisterProgressCallback(IProgressCallback var1) throws RemoteException;

    boolean unregisterResultCallback(IResultCallback var1) throws RemoteException;

    abstract class Stub extends Binder implements IRequestInterface {
        private static final String DESCRIPTOR = "com.samsung.android.app.networkstoragemanager.libsupport.IRequestInterface";

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IRequestInterface getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
            if (var1 != 1598968902) {
                long var5;
                boolean var8;
                Bundle var9;
                switch (var1) {
                    case 1:
                        var2.enforceInterface(DESCRIPTOR);
                        var8 = this.registerResultCallback(IResultCallback.Stub.asInterface(var2.readStrongBinder()));
                        var3.writeNoException();
                        var3.writeInt(var8 ? 1 : 0);
                        return true;
                    case 2:
                        var2.enforceInterface(DESCRIPTOR);
                        var8 = this.unregisterResultCallback(IResultCallback.Stub.asInterface(var2.readStrongBinder()));
                        var3.writeNoException();
                        var3.writeInt(var8 ? 1 : 0);
                        return true;
                    case 3:
                        var2.enforceInterface(DESCRIPTOR);
                        var8 = this.registerProgressCallback(IProgressCallback.Stub.asInterface(var2.readStrongBinder()));
                        var3.writeNoException();
                        var3.writeInt(var8 ? 1 : 0);
                        return true;
                    case 4:
                        var2.enforceInterface(DESCRIPTOR);
                        var8 = this.unregisterProgressCallback(IProgressCallback.Stub.asInterface(var2.readStrongBinder()));
                        var3.writeNoException();
                        var3.writeInt(var8 ? 1 : 0);
                        return true;
                    case 5:
                        var2.enforceInterface(DESCRIPTOR);
                        var5 = var2.readLong();
                        String var7 = var2.readString();
                        var1 = var2.readInt();
                        if (var2.readInt() != 0) {
                            var9 = Bundle.CREATOR.createFromParcel(var2);
                        } else {
                            var9 = null;
                        }

                        var9 = this.syncRequest(var5, var7, var1, var9);
                        var3.writeNoException();
                        if (var9 != null) {
                            var3.writeInt(1);
                            var9.writeToParcel(var3, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        } else {
                            var3.writeInt(0);
                        }

                        return true;
                    case 6:
                        var2.enforceInterface(DESCRIPTOR);
                        var5 = var2.readLong();
                        String var10 = var2.readString();
                        var1 = var2.readInt();
                        if (var2.readInt() != 0) {
                            var9 = Bundle.CREATOR.createFromParcel(var2);
                        } else {
                            var9 = null;
                        }

                        this.asyncRequest(var5, var10, var1, var9);
                        return true;
                    case 7:
                        var2.enforceInterface(DESCRIPTOR);
                        var8 = this.cancel(var2.readLong());
                        var3.writeNoException();
                        var3.writeInt(var8 ? 1 : 0);
                        return true;
                    case 8:
                        var2.enforceInterface(DESCRIPTOR);
                        this.retryRequest(var2.readLong());
                        var3.writeNoException();
                        return true;
                    default:
                        return super.onTransact(var1, var2, var3, var4);
                }
            } else {
                var3.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IRequestInterface {
            public static IRequestInterface sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder var1) {
                this.mRemote = var1;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void asyncRequest(long var1, String var3, int var4, Bundle var5) {
                Parcel var6 = Parcel.obtain();

                label215:
                {
                    try {
                        var6.writeInterfaceToken(DESCRIPTOR);
                        var6.writeLong(var1);
                        var6.writeString(var3);
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
                            if (!this.mRemote.transact(6, var6, (Parcel) null, 1) && Stub.getDefaultImpl() != null) {
                                Stub.getDefaultImpl().asyncRequest(var1, var3, var4, var5);
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

            public boolean cancel(long var1) {
                Parcel var3 = Parcel.obtain();
                Parcel var4 = Parcel.obtain();

                label171:
                {
                    IBinder var5;
                    try {
                        var3.writeInterfaceToken(DESCRIPTOR);
                        var3.writeLong(var1);
                        var5 = this.mRemote;
                    } catch (Throwable var18) {
                        break label171;
                    }

                    boolean var6 = false;

                    label172:
                    {
                        try {
                            if (!var5.transact(7, var3, var4, 0) && Stub.getDefaultImpl() != null) {
                                var6 = Stub.getDefaultImpl().cancel(var1);
                                break label172;
                            }
                        } catch (Throwable var19) {
                            break label171;
                        }

                        int var7;
                        try {
                            var4.readException();
                            var7 = var4.readInt();
                        } catch (Throwable var17) {
                            break label171;
                        }

                        if (var7 != 0) {
                            var6 = true;
                        }

                        var4.recycle();
                        var3.recycle();
                        return var6;
                    }

                    var4.recycle();
                    var3.recycle();
                    return var6;
                }

                var4.recycle();
                var3.recycle();
                return false;
            }

            public boolean registerProgressCallback(IProgressCallback var1) {
                Parcel var2 = Parcel.obtain();
                Parcel var3 = Parcel.obtain();

                label335:
                {
                    try {
                        var2.writeInterfaceToken(DESCRIPTOR);
                    } catch (Throwable var35) {
                        break label335;
                    }

                    IBinder var4;
                    if (var1 != null) {
                        try {
                            var4 = var1.asBinder();
                        } catch (Throwable var34) {
                            break label335;
                        }
                    } else {
                        var4 = null;
                    }

                    try {
                        var2.writeStrongBinder(var4);
                        var4 = this.mRemote;
                    } catch (Throwable var33) {
                        break label335;
                    }

                    boolean var5 = false;

                    label329:
                    {
                        try {
                            if (var4.transact(3, var2, var3, 0) || Stub.getDefaultImpl() == null) {
                                break label329;
                            }

                            var5 = Stub.getDefaultImpl().registerProgressCallback(var1);
                        } catch (Throwable var36) {
                            break label335;
                        }

                        var3.recycle();
                        var2.recycle();
                        return var5;
                    }

                    int var6;
                    try {
                        var3.readException();
                        var6 = var3.readInt();
                    } catch (Throwable var32) {
                        break label335;
                    }

                    if (var6 != 0) {
                        var5 = true;
                    }

                    var3.recycle();
                    var2.recycle();
                    return var5;
                }

                var3.recycle();
                var2.recycle();
                return false;
            }

            public boolean registerResultCallback(IResultCallback var1) {
                Parcel var2 = Parcel.obtain();
                Parcel var3 = Parcel.obtain();

                label335:
                {
                    try {
                        var2.writeInterfaceToken(DESCRIPTOR);
                    } catch (Throwable var35) {
                        break label335;
                    }

                    IBinder var4;
                    if (var1 != null) {
                        try {
                            var4 = var1.asBinder();
                        } catch (Throwable var34) {
                            break label335;
                        }
                    } else {
                        var4 = null;
                    }

                    try {
                        var2.writeStrongBinder(var4);
                        var4 = this.mRemote;
                    } catch (Throwable var33) {
                        break label335;
                    }

                    boolean var5 = false;

                    label329:
                    {
                        try {
                            if (var4.transact(1, var2, var3, 0) || Stub.getDefaultImpl() == null) {
                                break label329;
                            }

                            var5 = Stub.getDefaultImpl().registerResultCallback(var1);
                        } catch (Throwable var36) {
                            break label335;
                        }

                        var3.recycle();
                        var2.recycle();
                        return var5;
                    }

                    int var6;
                    try {
                        var3.readException();
                        var6 = var3.readInt();
                    } catch (Throwable var32) {
                        break label335;
                    }

                    if (var6 != 0) {
                        var5 = true;
                    }

                    var3.recycle();
                    var2.recycle();
                    return var5;
                }

                var3.recycle();
                var2.recycle();
                return false;
            }

            public void retryRequest(long var1) throws RemoteException {
                Parcel var3 = Parcel.obtain();
                Parcel var4 = Parcel.obtain();

                try {
                    var3.writeInterfaceToken(DESCRIPTOR);
                    var3.writeLong(var1);
                    if (this.mRemote.transact(8, var3, var4, 0) || Stub.getDefaultImpl() == null) {
                        var4.readException();
                        return;
                    }

                    Stub.getDefaultImpl().retryRequest(var1);
                } finally {
                    var4.recycle();
                    var3.recycle();
                }

            }

            public Bundle syncRequest(long var1, String var3, int var4, Bundle var5) {
                Parcel var6 = Parcel.obtain();
                Parcel var7 = Parcel.obtain();

                label330:
                {
                    try {
                        var6.writeInterfaceToken(DESCRIPTOR);
                        var6.writeLong(var1);
                        var6.writeString(var3);
                        var6.writeInt(var4);
                    } catch (Throwable var36) {
                        break label330;
                    }

                    if (var5 != null) {
                        try {
                            var6.writeInt(1);
                            var5.writeToParcel(var6, 0);
                        } catch (Throwable var35) {
                            break label330;
                        }
                    } else {
                        try {
                            var6.writeInt(0);
                        } catch (Throwable var34) {
                            break label330;
                        }
                    }

                    Bundle var38;
                    label324:
                    {
                        try {
                            if (this.mRemote.transact(5, var6, var7, 0) || Stub.getDefaultImpl() == null) {
                                break label324;
                            }

                            var38 = Stub.getDefaultImpl().syncRequest(var1, var3, var4, var5);
                        } catch (Throwable var37) {
                            break label330;
                        }

                        var7.recycle();
                        var6.recycle();
                        return var38;
                    }

                    label308:
                    {
                        try {
                            var7.readException();
                            if (var7.readInt() != 0) {
                                var38 = (Bundle) Bundle.CREATOR.createFromParcel(var7);
                                break label308;
                            }
                        } catch (Throwable var33) {
                            break label330;
                        }

                        var38 = null;
                    }

                    var7.recycle();
                    var6.recycle();
                    return var38;
                }

                var7.recycle();
                var6.recycle();
                return null;
            }

            public boolean unregisterProgressCallback(IProgressCallback var1) {
                Parcel var2 = Parcel.obtain();
                Parcel var3 = Parcel.obtain();

                label335:
                {
                    try {
                        var2.writeInterfaceToken(DESCRIPTOR);
                    } catch (Throwable var35) {
                        break label335;
                    }

                    IBinder var4;
                    if (var1 != null) {
                        try {
                            var4 = var1.asBinder();
                        } catch (Throwable var34) {
                            break label335;
                        }
                    } else {
                        var4 = null;
                    }

                    try {
                        var2.writeStrongBinder(var4);
                        var4 = this.mRemote;
                    } catch (Throwable var33) {
                        break label335;
                    }

                    boolean var5 = false;

                    label329:
                    {
                        try {
                            if (var4.transact(4, var2, var3, 0) || Stub.getDefaultImpl() == null) {
                                break label329;
                            }

                            var5 = Stub.getDefaultImpl().unregisterProgressCallback(var1);
                        } catch (Throwable var36) {
                            break label335;
                        }

                        var3.recycle();
                        var2.recycle();
                        return var5;
                    }

                    int var6;
                    try {
                        var3.readException();
                        var6 = var3.readInt();
                    } catch (Throwable var32) {
                        break label335;
                    }

                    if (var6 != 0) {
                        var5 = true;
                    }

                    var3.recycle();
                    var2.recycle();
                    return var5;
                }

                var3.recycle();
                var2.recycle();
                return false;
            }

            public boolean unregisterResultCallback(IResultCallback var1) {
                Parcel var2 = Parcel.obtain();
                Parcel var3 = Parcel.obtain();

                label335:
                {
                    try {
                        var2.writeInterfaceToken(DESCRIPTOR);
                    } catch (Throwable var35) {
                        break label335;
                    }

                    IBinder var4;
                    if (var1 != null) {
                        try {
                            var4 = var1.asBinder();
                        } catch (Throwable var34) {
                            break label335;
                        }
                    } else {
                        var4 = null;
                    }

                    try {
                        var2.writeStrongBinder(var4);
                        var4 = this.mRemote;
                    } catch (Throwable var33) {
                        break label335;
                    }

                    boolean var5 = false;

                    label329:
                    {
                        try {
                            if (var4.transact(2, var2, var3, 0) || Stub.getDefaultImpl() == null) {
                                break label329;
                            }

                            var5 = Stub.getDefaultImpl().unregisterResultCallback(var1);
                        } catch (Throwable var36) {
                            break label335;
                        }

                        var3.recycle();
                        var2.recycle();
                        return var5;
                    }

                    int var6;
                    try {
                        var3.readException();
                        var6 = var3.readInt();
                    } catch (Throwable var32) {
                        break label335;
                    }

                    if (var6 != 0) {
                        var5 = true;
                    }

                    var3.recycle();
                    var2.recycle();
                    return var5;
                }

                var3.recycle();
                var2.recycle();
                return false;
            }
        }
    }
}
