/***************************************************************************
 *
 * This document contains confidential and proprietary information 
 * subject to non-disclosure agreements with Cloudaxis. This 
 * information shall not be distributed or copied without written 
 * permission from the Cloudaxis.
 *
 ***************************************************************************/

package com.cloudaxis.agsc.portal.helpers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***************************************************************************
 * <PRE>
 *  Project Name    : cloudaxis-portal
 * 
 *  Package Name    : com.cloudaxis.portal.helpers
 * 
 *  File Name       : FileUtil.java
 * 
 *  Creation Date   : 2015
 * 
 *  Author          : Contributor
 * 
 *  Purpose         : TODO
 * 
 * 
 *  History         : TODO
 * 
 * </PRE>
 ***************************************************************************/
public final class FileUtil {

	private static final Log LOGGER = LogFactory.getLog(FileUtil.class);

	private FileUtil() {
	}

	/**
	 * @param file
	 */
	public static boolean ensureFileNotExists(String file) {
		File f = new File(file);
		return ensureFileNotExists(f);
	}

	/**
	 * @param file
	 */
	public static boolean ensureFileNotExists(File file) {
		if (!file.exists()) {
			return true;
		}
		return file.delete();
	}

	/**
	 * @param dir
	 */
	public static boolean ensureDirectoryNotExists(String dir) {
		return ensureDirectoryNotExists(new File(dir));
	}

	/**
	 * @param dir
	 */
	public static boolean ensureDirectoryNotExists(File dir) {
		if (!dir.exists()) {
			return true;
		}

		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return dir.delete();
		}

		boolean result = true;
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				result = result && ensureDirectoryNotExists(f);
			}
			else {
				result = result && ensureFileNotExists(f);
			}
		}

		if (result) {
			return dir.delete();
		}
		else {
			return false;
		}
	}

	/**
	 * @param path
	 * @return
	 */
	public static boolean isAbsPath(String path) {
		File file = new File(path);
		return file.isAbsolute();
	}

	/**
	 * @param path
	 * @return
	 */
	public static boolean isPathExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * @param f
	 * @return
	 */
	public static boolean isFileExist(File f) {
		return f.exists();
	}

	/**
	 * Check if has the files match the regulation expression under the given
	 * dir.
	 * 
	 * @param dir
	 *            given dir
	 * @param regex
	 *            regulation expression
	 * @return true the file exists; false the file not exists.
	 */
	public static boolean isFileExistByRegex(String dir, final String regex) {
		if (regex == null) {
			return false;
		}

		File file = new File(dir);

		if (!file.isDirectory()) {
			return file.exists();
		}

		String[] str = getFileListByRegex(dir, regex);
		if (str == null || str.length < 1) {
			return false;
		}
		return true;
	}

	/**
	 * Get the File list under the given directory by the regex. If the
	 * parameter dir is not a directory, return null.
	 * 
	 * @param dir
	 *            the given directory
	 * @param regex
	 *            the given regulate expression.
	 * @return
	 */
	public static String[] getFileListByRegex(String dir, final String regex) {
		File file = new File(dir);

		if (!file.isDirectory()) {
			return null;
		}

		FilenameFilter filter = new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.matches(regex);
			}
		};

		return file.list(filter);
	}

	/**
	 * Get the extension of the given file name.
	 * 
	 * @param fileName
	 *            the given file name.
	 * @return
	 */
	public static String getExtension(String fileName) {
		if (fileName == null || "".equals(fileName.trim())) {
			return null;
		}

		int index = fileName.lastIndexOf(".");
		if (index == -1) {
			return null;
		}

		return fileName.substring(index + 1);
	}

	/**
	 * Get the extension of the given file.
	 * 
	 * @param file
	 *            the given file.
	 * @return
	 */
	public static String getExtension(File file) {
		if (file == null)
			return null;
		String fileName = file.getName();
		return getExtension(fileName);
	}

	public static boolean isDirectory(String path) {
		File file = new File(path);

		return file.isDirectory();
	}

	/**
	 * @param path
	 * @return
	 */
	public static boolean createNewFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}

		boolean isSuccess = true;
		try {
			File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.exists()) {
					isSuccess = isSuccess && parent.mkdirs();
				}
			}
			isSuccess = isSuccess && file.createNewFile();
		}
		catch (IOException e) {
			LOGGER.error("createNewFile:: ", e);
			isSuccess = false;
		}

		return isSuccess;
	}

	/**
	 * @param path
	 * @return
	 */
	public static boolean mkdirs(String path) {
		File file = new File(path);

		if (!file.exists()) {
			return file.mkdirs();
		}
		else {
			return true;
		}
	}

	/**
	 * @param filePath
	 * @return Parent directory already exists or created successfully
	 */
	public static boolean createParentDirIfNotExist(String filePath) {
		boolean isSuccess = true;
		File file = new File(filePath);
		File parent = file.getParentFile();
		if (parent != null) {
			if (!parent.exists()) {
				isSuccess = parent.mkdirs();
			}
		}
		return isSuccess;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getNameWithoutExtention(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (fileName.lastIndexOf('.') > 0) {
			return fileName.substring(0, fileName.lastIndexOf('.'));
		}
		else {
			return fileName;
		}

	}

	/**
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean rename(String from, String to) {
		File fFrom = new File(from);
		File fTo = new File(to);

		if (!fFrom.exists()) {
			return false;
		}
		else {
			return fFrom.renameTo(fTo);
		}
	}

	/**
	 * @param filePath
	 */
	public static String getFilename(String filePath) {
		File f = new File(filePath);
		return f.getName();
	}

	/**
	 * @param path
	 * @return
	 */
	public static String getParentPath(String path) {
		File f = new File(path);
		return f.getParent();
	}

	/**
	 * Delete files in list
	 * 
	 * @param attachmnentList
	 */
	public static void removeFiles(List<File> attachmnentList) {
		if (attachmnentList != null) {
			for (File file : attachmnentList) {
				try {
					ensureFileNotExists(file);
				}
				catch (Exception e) {
					LOGGER.error("Failed to delete file " + file);
				}
			}
		}
	}
}
