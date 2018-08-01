package com.nd.hilauncherdev.kitset.fileselector;

import java.io.File;
import java.io.FileFilter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Environment;

/**
 * 文件操作控制
 */
public class FileController {
	private final Collator sCollator = Collator.getInstance();

	private String currentDirPath = "/";

	private String currentFilePath = "";

	private static final String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

	private SDCardFile currentSDCardFile = new SDCardFile(new File(
			currentDirPath));

	private File rootFiles = new File(currentDirPath);

	private static final FileController fileController = new FileController();

	private FileChangeNotifier mNotifier;

	private Activity mActivity;

	private FileController() {

	}

/*	public void cleanController() {
		currentDirPath = "/";
		currentFilePath = "";
		currentSDCardFile = new SDCardFile(new File(currentDirPath));
		rootFiles = new File(currentDirPath);
	}*/

	public String getCurrentDirPath() {
		return currentDirPath;
	}

	public void setCurrentDirPath(String currentPath) {
		this.currentDirPath = currentPath;
	}

	public static FileController getInstance() {
		return fileController;
	}

	public void setNoftifier(Activity activity, FileChangeNotifier notifier) {
		mNotifier = notifier;
		mActivity = activity;
	}

	public String returnParent(SDCardFile file, FileFilter fileFilter) {
		if (null != file && !file.isNULLFile()) {
			// if(file.getFile().getParentFile().getName().equalsIgnoreCase("/")){
			// System.out.println("file.getFile().getParentFile().getName()"+file.getFile().getParentFile().getName());
			// return currentFilePath;
			// }
			file.setFile(file.getFile().getParentFile());
			clickFile(file, fileFilter);
		}
		return currentFilePath;
	}

	public String returnParent(FileFilter fileFilter) {
		if (sdcard.equalsIgnoreCase(currentDirPath)) {
			return sdcard;
		}
		File currentFile = new File(currentDirPath);
		currentSDCardFile.setFile(currentFile);
		return returnParent(currentSDCardFile, fileFilter);
	}

	// 点击文件
	public Object clickFile(SDCardFile file, FileFilter fileFilter) {
		if (null != file && !file.isNULLFile()) {
			if (file.getFile().isDirectory()) {
				currentDirPath = file.getFile().getAbsolutePath();
				mNotifier.setSDCarFiles(getFiles(currentDirPath, fileFilter));
				mNotifier.sort(new Comparator<SDCardFile>() {
					public final int compare(SDCardFile a, SDCardFile b) {
						if (a.getFile().isDirectory()
								&& b.getFile().isDirectory()) {
							return sCollator.compare(a.getTitle().toString(), b
									.getTitle().toString());
						} else if (a.getFile().isDirectory()) {
							return -1;
						} else if (b.getFile().isDirectory()) {
							return 1;
						} else {
							return sCollator.compare(a.getTitle().toString(), b
									.getTitle().toString());
						}
					}
				});
				if (null != mActivity) {
					mActivity.runOnUiThread(mNotifier);
				}
			}
			currentFilePath = file.getFile().getAbsolutePath();
		}
		return currentFilePath;
	}

	/**
	 * 获取根文件。
	 * 
	 * @return 返回sd卡根文件。
	 */
	/*public List<SDCardFile> getRootFiles() {
		File[] files = rootFiles.listFiles();
		List<SDCardFile> sdCardFiles = new ArrayList<SDCardFile>();
		for (File file : files) {
			SDCardFile sdCardFile = new SDCardFile(file);
			sdCardFile.setTitle(file.getName());
			sdCardFiles.add(sdCardFile);
		}
		return sdCardFiles;
	}*/

	// public List<SDCardFile> getFiles(String path,String s) {
	// File files = new File(path);
	// List<SDCardFile> sdCardFiles = new ArrayList<SDCardFile>();
	// if (null != files && files.isDirectory()) {
	// for (File file : files.listFiles()) {
	// SDCardFile sdCardFile = new SDCardFile(file);
	// sdCardFile.setTitle(file.getName());
	// sdCardFiles.add(sdCardFile);
	// }
	// }
	// return sdCardFiles;
	// }
	/**
	 * 取得文件列表。
	 * 
	 * @param path
	 * @return List<SDCardFile>
	 */
	public List<SDCardFile> getFiles(String path, FileFilter fileFilter) {
		File files = new File(path);
		List<SDCardFile> sdCardFiles = new ArrayList<SDCardFile>();
		if (null != files && files.isDirectory()) {
			if (null != fileFilter) {
				File[] fileByFilter = files.listFiles(fileFilter);
				if (null != fileByFilter) {
					for (File file : fileByFilter) {
						SDCardFile sdCardFile = new SDCardFile(file);
						sdCardFile.setTitle(file.getName());
						sdCardFiles.add(sdCardFile);
					}
				}
			} else {
				for (File file : files.listFiles()) {
					SDCardFile sdCardFile = new SDCardFile(file);
					sdCardFile.setTitle(file.getName());
					sdCardFiles.add(sdCardFile);
				}
			}
		}
		return sdCardFiles;
	}
}
