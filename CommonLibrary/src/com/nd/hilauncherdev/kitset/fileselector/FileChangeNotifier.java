package com.nd.hilauncherdev.kitset.fileselector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 文件更改通知
 */
public class FileChangeNotifier implements Runnable {
	private final ArrayAdapter<SDCardFile> mFileList;
	private FileController fileController = FileController.getInstance();

	private static final int UI_NOTIFICATION_RATE = 4;
	private TextView mTextView;

	private List<SDCardFile> mBuffer;

	FileChangeNotifier(ArrayAdapter<SDCardFile> fileList, TextView textView) {
		mFileList = fileList;
		mTextView = textView;
		mBuffer = new ArrayList<SDCardFile>(UI_NOTIFICATION_RATE);
	}

	public void run() {
		final List<SDCardFile> buffer = mBuffer;
		final ArrayAdapter<SDCardFile> fileList = mFileList;
		final int count = buffer.size();
		fileList.clear();
		for (int i = 0; i < count; i++) {
			fileList.setNotifyOnChange(false);
			fileList.add(buffer.get(i));
		}
		fileList.notifyDataSetChanged();
		mTextView.setText(fileController.getCurrentDirPath());
		buffer.clear();
	}

	public void setSDCarFiles(List<SDCardFile> sdcardFiles) {
		mBuffer = sdcardFiles;
	}

	void add(SDCardFile file) {
		mBuffer.add(file);
	}

	void sort(Comparator<SDCardFile> comparator) {
		Collections.sort(mBuffer, comparator);
	}
}
