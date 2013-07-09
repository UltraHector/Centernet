package com.TroyEmpire.Centernet.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class IOUtil {

	/**
	 * @param object
	 *            an Serialized object which need to be converted into byte[]
	 * @return byte[] convert an Serialized Object into byte[]
	 */
	public static byte[] convertObjectToBytes(Object object) throws IOException {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo;
		oo = new ObjectOutputStream(bStream);
		oo.writeObject(object);
		oo.close();
		byte[] serializedMessage = bStream.toByteArray();
		return serializedMessage;
	}

	/**
	 * convert byte[] data into an object
	 * 
	 * @throws IOException
	 * @throws StreamCorruptedException
	 * @throws ClassNotFoundException
	 */
	public static <T> T convertBytesToObject(byte[] byteData)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		ObjectInputStream iStream;
		iStream = new ObjectInputStream(new ByteArrayInputStream(byteData));
		@SuppressWarnings("unchecked")
		T object = (T) iStream.readObject();
		iStream.close();
		return object;
	}

	/**
	 * convert a inputstream into an object
	 * 
	 * @throws IOException
	 * @throws StreamCorruptedException
	 * @throws ClassNotFoundException
	 */
	public static <T> T convertStreamToObject(InputStream inputStream)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		ObjectInputStream iStream;
		iStream = new ObjectInputStream(inputStream);
		@SuppressWarnings("unchecked")
		T object = (T) iStream.readObject();
		iStream.close();
		return object;

	}

	/**
	 * @param sourceFile
	 *            the filename of zip file including the absolute path
	 * @param destFileFolder
	 *            the folder in which the zipped files will be put in
	 */
	public static boolean unzipFile(String sourceFile, String destFileFolder) {
		try {
			File zipFile = new File(destFileFolder);
			if (!zipFile.exists())
				zipFile.mkdirs();
			ZipInputStream zipFileStream = new ZipInputStream(
					new FileInputStream(sourceFile));
			ZipEntry entry = zipFileStream.getNextEntry();
			while (entry != null) {
				String fileName = entry.getName();
				File newFile = new File(destFileFolder + File.separator
						+ fileName);
				if (entry.isDirectory())
					new File(newFile.getParent()).mkdirs();
				else {
					new File(newFile.getParent()).mkdirs();
					FileOutputStream out = new FileOutputStream(newFile);
					IOUtils.copy(zipFileStream, out);
				}
				entry = zipFileStream.getNextEntry();
			}
			zipFileStream.closeEntry();

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void deleteFolderContents(File folder) {
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files != null) { // some JVMs return null for empty dirs
				for (File f : files) {
					if (f.isDirectory()) {
						deleteFolderContents(f);
					} else {
						// if need to delete the folder, uncommonent the
						// following line
						// f.delete();
					}
				}
			}
			folder.delete();
		} else {
			// the folder does not exist
		}
	}

}
