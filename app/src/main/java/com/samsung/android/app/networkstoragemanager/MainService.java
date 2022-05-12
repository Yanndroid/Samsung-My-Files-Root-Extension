package com.samsung.android.app.networkstoragemanager;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import com.samsung.android.app.networkstoragemanager.libsupport.IProgressCallback;
import com.samsung.android.app.networkstoragemanager.libsupport.IRequestInterface;
import com.samsung.android.app.networkstoragemanager.libsupport.IResultCallback;
import com.samsung.android.app.networkstoragemanager.libsupport.RequestCode;
import com.topjohnwu.superuser.Shell;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainService extends Service implements RequestCode {

    private ArrayList<Bundle> storageLocations = new ArrayList<>();
    private IResultCallback mCallback;
    private IProgressCallback mProgressCallback;
    private Map<Long, RequestInfo> mRequestInfoMap = new HashMap();

    private final IRequestInterface.Stub mBinder = new IRequestInterface.Stub() {

        public void asyncRequest(long serverId, String type, int reqCode, Bundle extras) {
            (new Thread(() -> syncRequest(serverId, type, reqCode, extras))).start();
        }

        public boolean cancel(long serverId) {
            RequestInfo requestInfo = mRequestInfoMap.get(serverId);
            if (requestInfo != null) {
                requestInfo.mCanceled.set(true);
                return true;
            } else {
                return false;
            }
        }

        public boolean registerProgressCallback(IProgressCallback var1) {
            MainService.this.mProgressCallback = var1;
            return true;
        }

        public boolean registerResultCallback(IResultCallback var1) {
            MainService.this.mCallback = var1;
            return true;
        }

        public void retryRequest(long serverId) {
            RequestInfo requestInfo = mRequestInfoMap.get(serverId);
            if (requestInfo != null) {
                asyncRequest(requestInfo.mServerId, requestInfo.mType, requestInfo.mReqCode, requestInfo.mExtras);
            }
        }

        public Bundle syncRequest(long serverId, String type, int reqCode, Bundle extras) {
            RequestInfo requestInfo = new RequestInfo(serverId, type, reqCode, extras);
            mRequestInfoMap.put(serverId, requestInfo);
            Bundle result = new Bundle();
            handleRequest(requestInfo, result);
            return result;
        }

        public boolean unregisterProgressCallback(IProgressCallback var1) {
            MainService.this.mProgressCallback = null;
            return true;
        }

        public boolean unregisterResultCallback(IResultCallback var1) {
            MainService.this.mCallback = null;
            return true;
        }
    };

    private void handleRequest(RequestInfo requestInfo, Bundle result) {
        result.putBoolean("isSuccess", true);
        result.putBoolean("isValidRequest", true);

        Log.e("handleRequest", requestInfo.mReqCode + " " + requestInfo.mType);
        Bundle extras = requestInfo.mExtras;
        for (String s : extras.keySet()) Log.i(s, String.valueOf(extras.get(s)));

        switch (requestInfo.mReqCode) {
            case CONNECT:
                //0
                break;
            case GET_SERVER_LIST:
                //1 (per type!!)
                if (requestInfo.mType.equals("SMB")) {
                    result.putParcelableArrayList("serverList", storageLocations);
                }
                result.putBoolean("result", true);
                break;
            case ADD_SERVER:
                //2
                if (requestInfo.mType.equals("SMB")) {
                    long newServerId = storageLocations.size() > 0 ? storageLocations.get(storageLocations.size() - 1).getLong("serverId") + 1 : 1; //new id

                    Bundle newLocation = new Bundle(extras);
                    newLocation.putLong("serverId", newServerId);
                    storageLocations.add(newLocation);

                    result.putBoolean("result", true);
                    result.putLong("serverId", newServerId);
                } else {
                    result.putBoolean("result", false); //only allow smb
                }
                break;
            case UPDATE_SERVER:
                //4
                result.putBoolean("result", false);
                for (Bundle location : storageLocations) {
                    if (location.getLong("serverId") == extras.getLong("serverId")) {
                        location.putAll(extras);
                        result.putBoolean("result", true);
                        break;
                    }
                }
                break;
            case DELETE_SERVER:
                //6
                result.putBoolean("result", false);
                for (Bundle location : storageLocations) {
                    if (location.getLong("serverId") == extras.getLong("serverId")) {
                        storageLocations.remove(location);
                        result.putBoolean("result", true);
                        break;
                    }
                }
                break;
            case FIND_SERVER:
                //7 (when opening smb scan dialog)
                result.putParcelableArrayList("serverList", LocationList.getDefaultList(false));
                result.putBoolean("result", true);
                break;
            case GET_SHARED_FOLDER:
                //8 (aka root folder)
                //result.putParcelableArrayList("sharedFolderList", FileManager.getFileList("/", extras.getLong("serverId")));
                result.putParcelableArrayList("sharedFolderList", FileManager.getSharedFolderRootDir(extras.getLong("serverId")));
                result.putBoolean("result", true);
                break;
            case GET_FILE_LIST:
                //9
                //result.putParcelableArrayList("fileList", FileManager.getFileList(extras.getString("filePath"), extras.getLong("serverId")));
                result.putParcelableArrayList("fileList", FileManager.getFileListWithCache(extras.getString("filePath"), extras.getLong("serverId")));
                result.putBoolean("result", true);
                break;
            case GET_FILE_OBJECT:
                //10
                result.putParcelable("fileObject", FileManager.getFileObject(extras.getString("filePath"), extras.getLong("serverId")));
                result.putBoolean("result", true);
                break;
            case GET_STRING_MAP:
                //11
                Field[] fields = R.string.class.getDeclaredFields();
                Bundle bFiled = new Bundle();
                for (Field field : fields) {
                    try {
                        bFiled.putString(field.getName(), getResources().getString(field.getInt(null)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        bFiled.putString(field.getName(), "");
                    }
                }
                result.putBundle("result", bFiled);
                break;
            case GET_RESOURCE:
                //12
                break;
            case VERIFY_SERVER_INFO:
                //13 (init server info)
                break;
            case CHECK_PERMISSION:
                //14 (before opening)
                Shell.getShell(); //request root
                break;
            case GET_SERVER_COUNT:
                //15
                break;
            case REMOVE_MONITOR:
                //16 (delete request-info)
                /*mRequestInfoMap.remove(requestInfo.mServerId);
                result.putBoolean("result", true);*/
                break;
            case REMOVE_CACHED_FILE_LIST:
                //17
                CachedFileList.clear();
                result.putBoolean("result", true);
                break;
            case CREATE_FOLDER:
                //121
                result.putBoolean("isSuccess", FileManager.newFolder(extras.getString("parentPath"), extras.getString("newName")));
                result.putBoolean("result", true);
                break;
            case RENAME:
                //122
                result.putBoolean("isSuccess", FileManager.renameFile(extras.getString("sourcePath"), extras.getString("newName")));
                result.putBoolean("result", true);
                break;
            case UPLOAD:
                //123 (copy)
                result.putBoolean("isSuccess", FileManager.copy((ParcelFileDescriptor) extras.getParcelable("fileDescriptor"), extras.getString("dstFolderPath"), extras.getString("dstFileName"), mProgressCallback, requestInfo.mServerId, requestInfo.mReqCode));
                result.putBoolean("result", true);
                break;
            case GET_FILE_DESCRIPTOR:
                //124 (click, copy, move)
                result.putParcelable("fileDescriptor", FileManager.getFileDescriptor(extras.getString("sourcePath")));
                result.putBoolean("result", true);
                break;
            case DELETE:
                //125
                result.putBoolean("isSuccess", FileManager.deleteFile(extras.getString("sourcePath")));
                result.putBoolean("result", true);
                break;
            case INTERNAL_COPY:
                //126
                result.putBoolean("isSuccess", FileManager.copy(extras.getString("sourcePath"), extras.getString("dstFolderPath"), extras.getString("dstFileName"), mProgressCallback, requestInfo.mServerId, requestInfo.mReqCode, 0).isSuccess);
                result.putBoolean("result", true);
                break;
            case INTERNAL_MOVE:
                //127
                boolean isSuccess = FileManager.copy(extras.getString("sourcePath"), extras.getString("dstFolderPath"), extras.getString("dstFileName"), mProgressCallback, requestInfo.mServerId, requestInfo.mReqCode, 0).isSuccess;
                result.putBoolean("isSuccess", isSuccess && FileManager.deleteFile(extras.getString("sourcePath")));
                result.putBoolean("result", true);
                break;
            case EXTERNAL_COPY:
                //128
                break;
            case EXTERNAL_MOVE:
                //129
                break;
            case EXIST:
                //130
                result.putBoolean("result", FileManager.exists(extras.getString("sourcePath")));
                break;
        }

        mRequestInfoMap.remove(requestInfo.mServerId);
        if (requestInfo.mCanceled.get()) return;

        try {
            this.mCallback.onSuccess(requestInfo.mServerId, requestInfo.mReqCode, result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public IBinder onBind(Intent var1) {
        return this.mBinder;
    }

    public void onCreate() {
        if (Shell.getCachedShell() == null) {
            Shell.setDefaultBuilder(Shell.Builder.create().setFlags(Shell.FLAG_MOUNT_MASTER));
        }
        storageLocations = LocationList.loadList(this);
    }

    public void onDestroy() {
        super.onDestroy();
        LocationList.saveList(this, storageLocations);
    }

    public static class RequestInfo {
        public final AtomicBoolean mCanceled;
        public final Bundle mExtras;
        public final int mReqCode;
        public final long mServerId;
        public final String mType;

        private RequestInfo(long serverId, String type, int requestCode, Bundle extras) {
            this.mCanceled = new AtomicBoolean(false);
            this.mServerId = serverId;
            this.mType = type;
            this.mReqCode = requestCode;
            this.mExtras = extras;
        }
    }

}
