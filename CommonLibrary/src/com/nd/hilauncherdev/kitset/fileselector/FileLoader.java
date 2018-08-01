package com.nd.hilauncherdev.kitset.fileselector;

import java.io.FileFilter;
import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Process;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 文件加载
 */
public class FileLoader implements Runnable {

    private final Collator sCollator = Collator.getInstance();

    private static final long FILE_NOT_RESPONDING_TIMEOUT = 5000;

    private final WeakReference<Activity> mLauncher;

    private volatile boolean mStopped;

    private volatile boolean mRunning;

    private final ArrayAdapter<SDCardFile> fileAdapter;

    private TextView mTextView;

    public FileLoader(Activity launcher, ArrayAdapter<SDCardFile> fileList, TextView textView, boolean fileLoaded) {
        mLauncher = new WeakReference<Activity>(launcher);
        mTextView = textView;
        fileAdapter = fileList;
    }

    public void stop() {
        mStopped = true;
    }

    public boolean isRunning() {
        return mRunning;
    }

    private Thread mFileLoaderThread;

    public void run() {
        mRunning = true;
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        final Activity launcher = mLauncher.get();
        List<SDCardFile> sdCardFiles = mSDCardFiles;

        if (sdCardFiles != null && !mStopped) {
            final int count = sdCardFiles.size();

            FileChangeNotifier action = new FileChangeNotifier(fileAdapter, mTextView);

            for (int i = 0; i < count && !mStopped; i++) {
                SDCardFile file = sdCardFiles.get(i);
                action.add(file);
            }

            action.sort(new Comparator<SDCardFile>() {
                public final int compare(SDCardFile a, SDCardFile b) {
                    if (a.getFile().isDirectory() && b.getFile().isDirectory()) {
                        return sCollator.compare(a.getTitle().toString(), b.getTitle().toString());
                    } else if (a.getFile().isDirectory()) {
                        return -1;
                    } else if (b.getFile().isDirectory()) {
                        return 1;
                    } else {
                        return sCollator.compare(a.getTitle().toString(), b.getTitle().toString());
                    }
                }
            });

            if (!mStopped) {
                launcher.runOnUiThread(action);
            }
            FileController.getInstance().setNoftifier(launcher, action);
        }
        mRunning = false;
    }

    /**
     * �����ļ����߳̿��Ƶȴ�״̬��
     * 
     * @param filePath
     */
    public void startFileLoader(String filePath, FileFilter fileFilter) {
        mSDCardFiles = FileController.getInstance().getFiles(filePath, fileFilter);
        mFileLoaderThread = new Thread(this, "File Loader");
        try {
            mFileLoaderThread.join(FILE_NOT_RESPONDING_TIMEOUT);
        } catch (InterruptedException e) {
            // Empty
        }
        mFileLoaderThread.start();
    }

    private List<SDCardFile> mSDCardFiles;

    /*public void setSDCardFiles(List<SDCardFile> sdCardFile) {
        mSDCardFiles = sdCardFile;
    }*/
}
