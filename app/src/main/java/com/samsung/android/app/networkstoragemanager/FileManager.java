package com.samsung.android.app.networkstoragemanager;

import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.samsung.android.app.networkstoragemanager.libsupport.IProgressCallback;
import com.topjohnwu.superuser.io.SuFile;
import com.topjohnwu.superuser.io.SuFileInputStream;
import com.topjohnwu.superuser.io.SuFileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class FileManager {

    public static boolean exists(String filePath) {
        return (new SuFile(filePath)).exists();
    }

    public static boolean renameFile(String filePath, String newName) {
        clearPathCache(filePath, true);
        SuFile file = new SuFile(filePath);
        return file.renameTo(new SuFile(file.getParent() + "/" + newName));
    }

    public static boolean newFolder(String path, String name) {
        clearPathCache(path, false);
        SuFile dir = new SuFile(path + "/" + name);
        return dir.mkdirs();
    }

    public static boolean deleteFile(String filePath) {
        clearPathCache(filePath, true);
        SuFile file = new SuFile(filePath);
        return file.deleteRecursive();
    }

    public static twoReturn copy(String sourcePath, String dstFolderPath, String dstFileName, IProgressCallback mProgressCallback, long requestId, int reqCode, long progress) {
        clearPathCache(dstFolderPath, false);
        SuFile file = new SuFile(sourcePath);
        Log.e("copy", sourcePath + " to " + dstFolderPath + "/" + dstFileName);
        if (file.isDirectory()) {
            boolean isSuccess = newFolder(dstFolderPath, dstFileName);
            if (isSuccess) {
                for (String fileName : file.list()) {
                    twoReturn t = copy(sourcePath + "/" + fileName, dstFolderPath + "/" + dstFileName, fileName, mProgressCallback, requestId, reqCode, progress);
                    isSuccess &= t.isSuccess;
                    progress += t.progress;
                }
            }
            return new twoReturn(isSuccess, progress);
        } else {
            try {
                InputStream in = SuFileInputStream.open(sourcePath);
                OutputStream out = SuFileOutputStream.open(dstFolderPath + "/" + dstFileName);

                byte[] buf = new byte[4096];
                int len;
                long total = progress;
                Bundle bundle = new Bundle();
                while ((len = in.read(buf)) > 0) {
                    bundle.putLong("handledSize", total += len);
                    mProgressCallback.onProgress(requestId, reqCode, bundle);
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
                return new twoReturn(true, total);
            } catch (Exception e) {
                e.printStackTrace();
                return new twoReturn(false, progress);
            }
        }
    }

    static class twoReturn { //python is definitely better for this :(
        public boolean isSuccess;
        public long progress;

        twoReturn(boolean isSuccess, long progress) {
            this.isSuccess = isSuccess;
            this.progress = progress;
        }
    }

    public static boolean copy(ParcelFileDescriptor fileDescriptor, String dstFolderPath, String dstFileName, IProgressCallback mProgressCallback, long requestId, int reqCode) {
        clearPathCache(dstFolderPath, false);
        try {
            InputStream in = new ParcelFileDescriptor.AutoCloseInputStream(fileDescriptor);
            OutputStream out = SuFileOutputStream.open(dstFolderPath + "/" + dstFileName);

            byte[] buf = new byte[4096];
            int len;
            long total = 0;
            Bundle progress = new Bundle();
            while ((len = in.read(buf)) > 0) {
                progress.putLong("handledSize", total += len);
                mProgressCallback.onProgress(requestId, reqCode, progress);
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Bundle> getFileList(String filePath, long serverId) {
        ArrayList<Bundle> fileList = new ArrayList<>();
        SuFile[] files = new SuFile(filePath).listFiles();
        if (files == null) return fileList;
        for (SuFile file : files)
            fileList.add(getFileObject(file, serverId));
        return fileList;
    }

    public static ArrayList<Bundle> getFileListWithCache(String filePath, long serverId) {
        if (CachedFileList.contains(filePath)) {
            //return cached file list and update it in async
            Executors.newSingleThreadExecutor().execute(() -> CachedFileList.saveFileList(filePath, getFileList(filePath, serverId)));
            return CachedFileList.get(filePath);
        } else {
            //return file list and cache it
            return CachedFileList.saveFileList(filePath, getFileList(filePath, serverId));
        }
    }

    public static void clearPathCache(String path, boolean parent) {
        if (parent) path = path.substring(0, path.lastIndexOf("/"));
        if (path == null) return;
        Log.e("clearCache", path);
        CachedFileList.removeFileList(path);
    }

    public static Bundle getFileObject(String filePath, long serverId) {
        return getFileObject(new SuFile(filePath), serverId);
    }

    private static Bundle getFileObject(SuFile file, long serverId) {
        Bundle bFile = new Bundle();
        bFile.putLong("serverId", serverId);
        bFile.putString("filePath", file.getPath());
        bFile.putString("fileName", file.getName());
        bFile.putBoolean("isDirectory", !file.isFile());
        if (file.isFile()) bFile.putLong("fileSize", file.length());
        bFile.putLong("fileDate", file.lastModified());
        return bFile;
    }

    public static ParcelFileDescriptor getFileDescriptor(String filePath) {
        try {
            InputStream inputStream = SuFileInputStream.open(filePath);
            ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
            new TransferThread(inputStream, new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1])).start();
            return pipe[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Bundle> getSharedFolderRootDir(long serverId) {
        ArrayList<Bundle> fileList = new ArrayList<>();
        Bundle bRootFile = new Bundle();
        bRootFile.putLong("serverId", serverId);
        bRootFile.putString("filePath", "/");
        bRootFile.putBoolean("isDirectory", true);
        fileList.add(bRootFile);
        return fileList;
    }

    public static String getSDCardPath() {
        SuFile storages = new SuFile("storage");
        String[] list = storages.list((file, s) -> s.matches("([A-Z0-9]){4}-([A-Z0-9]){4}"));
        return list.length > 0 ? list[0] : null;
    }

    private static class TransferThread extends Thread {
        final InputStream mIn;
        final OutputStream mOut;

        TransferThread(InputStream in, OutputStream out) {
            super("IPC Transfer Thread");
            mIn = in;
            mOut = out;
            setDaemon(true);
        }

        @Override
        public void run() {
            byte[] buf = new byte[4096];
            int len;

            try {
                while ((len = mIn.read(buf)) > 0) {
                    mOut.write(buf, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    mIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}