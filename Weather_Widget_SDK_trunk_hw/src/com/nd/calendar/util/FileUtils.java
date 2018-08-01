/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nd.calendar.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * Tools for managing files. Not for public consumption.
 */
public class FileUtils
{
	public final static int FILE_PERMISSIONS_X = 755;
	public final static int FILE_PERMISSIONS_N = 775;

	public static boolean setPermissions(String sfile, int iPermissions) {
		try {
			String command = "chmod " + Integer.toString(iPermissions) + " " + sfile;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/** Regular expression for safe filenames: no spaces or metacharacters */
	private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");

	// copy a file from srcFile to destFile, return true if succeed, return
	// false if fail
	public static boolean copyFile(File srcFile, File destFile) {
		boolean result = false;
		try {
			InputStream in = new FileInputStream(srcFile);
			try {
				result = copyToFile(in, destFile);
			} finally {
				in.close();
			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	/**
	 * Copy data from a source stream to destFile. Return true if succeed,
	 * return false if failed.
	 */
	public static boolean copyToFile(InputStream inputStream, File destFile) {
		try {
			if (destFile.exists()) {
				destFile.delete();
			}
			FileOutputStream out = new FileOutputStream(destFile);
			try {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				out.flush();
				try {
					out.getFD().sync();
				} catch (IOException e) {
				}
				out.close();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static void checkSoLibs(Context context) {
		File binaryDir = context.getCacheDir().getParentFile();
		binaryDir = new File(binaryDir, "lib");

		
	}
	
	private static void copyNativeBinaryLI(Context context, String sLibName) throws IOException {
		File binaryDir = context.getCacheDir().getParentFile();
		binaryDir = new File(binaryDir, "lib");
		
		File binaryFile = new File(binaryDir, sLibName);
		AssetManager am = context.getAssets();
		InputStream inputStream = am.open(sLibName);

		try {
			FileUtils.setPermissions(binaryDir.getPath(), FileUtils.FILE_PERMISSIONS_N);
			binaryDir.delete();

			if (!binaryDir.exists()) {
				binaryDir.mkdir();
			}

			File tempFile = File.createTempFile("tmp", "tmp", binaryDir);

			if (!FileUtils.copyToFile(inputStream, tempFile)
					|| !tempFile.renameTo(binaryFile)
					|| !FileUtils.setPermissions(binaryFile.getAbsolutePath(),
							FileUtils.FILE_PERMISSIONS_X)) {
				tempFile.delete();
				throw new IOException("Couldn't create cached binary " + binaryFile + " in "
						+ binaryDir);
			}

			FileUtils.setPermissions(binaryDir.getPath(), FileUtils.FILE_PERMISSIONS_X);

		} finally {
			inputStream.close();
		}
	}
}
