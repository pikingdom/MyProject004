package com.nd.hilauncherdev.kitset.fileselector;

import java.io.File;
import java.io.FileFilter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Environment;

import com.nd.hilauncherdev.kitset.fileselector.IconModel.IconPack;
import com.nd.hilauncherdev.launcher.config.BaseConfig;

/**
 * 图标文件控制器
 */
public class IconFileController {
	private final Collator sCollator = Collator.getInstance();

	private String currentDirPath = "/";

	private String currentFilePath = "";

	private static final String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

	private SDCardFile currentSDCardFile = new SDCardFile(new File(
			currentDirPath));

	private File rootFiles = new File(currentDirPath);

	private static final IconFileController fileController = new IconFileController();

	private FileChangeNotifier mNotifier;

	private Activity mActivity;

	private IconFileController() {

	}

	/**
	 * 重置控制器
	 */
	public void cleanController() {
		currentDirPath = "/";
		currentFilePath = "";
		currentSDCardFile = new SDCardFile(new File(currentDirPath));
		rootFiles = new File(currentDirPath);
	}

	/**
	 * 获取当前路径
	 * @return 路径
	 */
	public String getCurrentDirPath() {
		return currentDirPath;
	}

	/**
	 * 设置当前路径
	 * @param currentPath 路径
	 */
	public void setCurrentDirPath(String currentPath) {
		this.currentDirPath = currentPath;
	}

	public static IconFileController getInstance() {
		return fileController;
	}

	/**
	 * 设置FileChangeNotifier
	 * @param activity
	 * @param notifier
	 */
	public void setNoftifier(Activity activity, FileChangeNotifier notifier) {
		mNotifier = notifier;
		mActivity = activity;
	}

	/**
	 * returnParent
	 * @param file
	 * @param fileFilter
	 * @return String
	 */
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

	/**
	 * returnParent
	 * @param fileFilter
	 * @return String
	 */
	public String returnParent(FileFilter fileFilter) {
		if (sdcard.equalsIgnoreCase(currentDirPath)) {
			return sdcard;
		}
		File currentFile = new File(currentDirPath);
		currentSDCardFile.setFile(currentFile);
		return returnParent(currentSDCardFile, fileFilter);
	}

	/**
	 * 点击文件
	 * @param file
	 * @param fileFilter
	 */
	public Object clickFile(SDCardFile file, FileFilter fileFilter) {
		if (file.type > 0) {
			List<SDCardFile> mSDCardFiles = new ArrayList<SDCardFile>();
			switch (file.type) {
			case SDCardFile.ICON_FILE_DIR:
				List<IconPack> packs = IconFactory.getIconPacks(BaseConfig.getApplicationContext());
				for (IconPack pack : packs) {
					SDCardFile sf = new SDCardFile(SDCardFile.ICON_FILE_PACK);
					sf.setTag(pack);
					mSDCardFiles.add(sf);
				}
				currentDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/|icons|";
				break;
			case SDCardFile.ICON_FILE_PACK:
				IconPack pack = (IconPack) file.getTag();
				List<String> icons = IconFactory.getPackIcons(BaseConfig.getApplicationContext(), pack);
				for (String icon : icons) {
					SDCardFile sf = new SDCardFile(SDCardFile.ICON_FILE);
					sf.setTag(pack.packName + "|" + icon);
					mSDCardFiles.add(sf);
				}
				currentDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/|icons|/"
						+ ((IconPack) file.getTag()).packName;
				break;
			}
			mNotifier.setSDCarFiles(mSDCardFiles);
			if (null != mActivity) {
				mActivity.runOnUiThread(mNotifier);
			}
			return currentDirPath;
		} else if (null != file && !file.isNULLFile()) {
			if (file.getFile().isDirectory()) {
				currentDirPath = file.getFile().getAbsolutePath();
				List<SDCardFile> mSDCardFiles = new ArrayList<SDCardFile>();
				if (Environment.getExternalStorageDirectory().getAbsolutePath().equalsIgnoreCase(currentDirPath)
						|| (Environment.getExternalStorageDirectory().getAbsolutePath()+"/").equalsIgnoreCase(currentDirPath)) {
					SDCardFile sf = new SDCardFile(SDCardFile.ICON_FILE_DIR);
					mSDCardFiles.add(sf);
				}
				mSDCardFiles.addAll(getFiles(currentDirPath, fileFilter));
				mNotifier.setSDCarFiles(mSDCardFiles);
				mNotifier.sort(new Comparator<SDCardFile>() {
					public final int compare(SDCardFile a, SDCardFile b) {
						if (a.type > b.type) {
							return -1;
						} else if (a.type < b.type) {
							return 1;
						}
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
	 * @return 返回sd卡根文件。
	 */
	public List<SDCardFile> getRootFiles() {
		File[] files = rootFiles.listFiles();
		List<SDCardFile> sdCardFiles = new ArrayList<SDCardFile>();
		for (File file : files) {
			SDCardFile sdCardFile = new SDCardFile(file);
			sdCardFile.setTitle(file.getName());
			sdCardFiles.add(sdCardFile);
		}
		return sdCardFiles;
	}

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
	 * @param path
	 * @param fileFilter
	 * @return 文件列表
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
