package com.kenny.baselibrary.utils.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作工具类
 * @author kenny
 * @time 2016/2/17 22:39
 */
public class FileUtil {

	private Context context;

	public FileUtil(Context context) {
		this.context = context;
	}

	/**
	 * 复制文件,存在则删除
	 * 
	 * @param sourcePath
	 *            assets文件夹下的相对路径
	 * @param destPath
	 *            目标文件夹的绝对路径
	 * @return
	 */
	public static boolean copyFileFromAsset(Context context, String sourcePath, String destPath) {
		boolean result = false;
		try {
			File destFile = null;
			String destDir = destPath.substring(0, destPath.lastIndexOf("/"));
			File dir = new File(destDir);
			if (dir.exists()) {
				destFile = new File(destPath);
				if (destFile.exists()) {
					destFile.delete();
				}
			} else {
				dir.mkdirs();
			}

			if (!dir.exists()) {
				Log.e("DB", "数据库目录创建失败，请确认SDK是否有效");
				return result;
			}

			// 获得数据库文件的InputStream对象
			InputStream file = context.getAssets().open(sourcePath);
			// FileOutputStream a=new FileOutputStream(ToFile);
			FileOutputStream fos = new FileOutputStream(destPath);
			byte[] buffer = new byte[8192];
			int count = 0;
			// 开始复制FILENAME文件
			while ((count = file.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			file.close();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * 复制文件,存在则删除
	 * 
	 * @param sourceFilePathName
	 *            assets文件夹下的相对路径
	 * @param destPath
	 *            目标文件夹的绝对路径
	 * @param fileName
	 * @return
	 */
	public boolean copyFileFromAsset(String sourceFilePathName, String destPath, String fileName) {
		boolean result = true;
		try {
			File dir = new File(destPath);
			if (!dir.exists()) {
				result = dir.mkdirs();
				Log.i("File Util Create Dir", result + "");
			}
			File ToFile = (new File(destPath + "/" + fileName));
			if (ToFile.exists()) {
				ToFile.delete();
			}
			// 获得数据库文件的InputStream对象
			AssetManager assets = context.getAssets();
			InputStream file = assets.open(sourceFilePathName);
			// FileOutputStream a=new FileOutputStream(ToFile);
			FileOutputStream fos = new FileOutputStream(destPath + "/" + fileName);
			byte[] buffer = new byte[8192];
			int count = 0;
			// 开始复制FILENAME文件
			while ((count = file.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			file.close();
			result = true;
			Log.i(this.getClass().getName(), "拷贝文件成功：" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * 从源文件夹复制文件到目标文件夹
	 * 
	 * @param sourceFilePathName
	 *            源文件夹
	 * @param destPath
	 *            目标文件夹
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public boolean copyFileFromTo(String sourceFilePathName, String destPath, String fileName) {
		try {
			File dir = new File(destPath);
			if (!dir.exists()) {
				dir.mkdir();
			}
			File ToFile = (new File(destPath + "/" + fileName));
			if (ToFile.exists()) {
				ToFile.delete();
			}
			// 获得数据库文件的InputStream对象
			InputStream file = new FileInputStream(sourceFilePathName);
			FileOutputStream fos = new FileOutputStream(destPath + "/" + fileName);
			byte[] buffer = new byte[8192];
			int count = 0;
			// 开始复制FILENAME文件
			while ((count = file.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			file.close();
			return true;

		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("删除文件失败：" + fileName + "文件不存在");
			return false;
		} else {
			if (file.isFile()) {

				return deleteFile(fileName);
			} else {
				return deleteDirectory(fileName);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true,否则返回false
	 */
	public boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
//			System.out.println("删除单个文件" + fileName + "成功！");
			return true;
		} else {
//			System.out.println("删除单个文件" + fileName + "失败！");
			return false;
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param dir
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true,否则返回false
	 */
	public boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			System.out.println("删除目录失败" + dir + "目录不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			System.out.println("删除目录失败");
			return false;
		}

		// 删除当前目录
		if (dirFile.delete()) {
			System.out.println("删除目录" + dir + "成功！");
			return true;
		} else {
			System.out.println("删除目录" + dir + "失败！");
			return false;
		}
	}

	/**
	 * 根据文件名和文件路径创建文件的操作方法
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @param fileContent
	 *            文件内容
	 */
	public void createFile(String filePath, String fileName, String fileContent) {
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		File dirFile = new File(filePath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			System.out.println("创建文件失败" + dirFile + "目录不存在！");
			return;
		}

		File contentFile = new File(filePath + fileName);
		try {
			contentFile.createNewFile();
			FileWriter fw = new FileWriter(contentFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(fileContent);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改文件名称
	 * 
	 * @param oldFilePath
	 *            旧文件的绝对路径
	 * @param newFileName
	 *            新文件的名称
	 * @return 修改成功返回true,修改失败返回false
	 */
	public boolean renameFile(String oldFilePath, String newFileName) {
		boolean ret = false;
		File oldFile = new File(oldFilePath);
		try {
			if (!oldFile.exists()) {
				oldFile.createNewFile();
			}
			File newFile = new File(oldFile.getParent() + "/" + newFileName);
			ret = oldFile.renameTo(newFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return ret;
	}

	/**
	 * 追加写入文件
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @param content
	 *            追加内容
	 */
	public void appendWriteFile(String filePath, String content) {
		File file = new File(filePath);
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file, true);
			content = "\r\n" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date()) + "------------------\r\n" + content
					+ "\r\n";
			fos.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 追加写入文件
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @param content
	 *            追加内容
	 * @param fileMaxSize
	 *            文件最大容量(byte)
	 * 
	 */
	public void appendWriteFile(String filePath, String content, long fileMaxSize) {
		File file = new File(filePath);
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			long fileSize = getFileSize(file);
			// 如果文件大小大于最大，则新建一份文件追加
			if (fileSize >= fileMaxSize) {
				file = new File(file.getParent() + "/copy_" + file.getName());
				if (!file.exists()) {
					file.createNewFile();
				}
			}
			fos = new FileOutputStream(file, true);
			content = "\r\n" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date()) + "------------------\r\n" + content
					+ "\r\n";
			fos.write(content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取文件大小
	 * 
	 * @param file
	 *            文件
	 * @return 文件大小(byte)
	 */
	public long getFileSize(File file) {
		long size = 0l;
		if (file.exists()) {
			try {
				size = new FileInputStream(file).available();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return size;
	}

	/**
	 * 根据文件路径获取文件的大小
	 * 
	 * @param filePath
	 * @return
	 */
	public long getFileSizeByFilePath(String filePath) {
		long size = 0l;
		File file = new File(filePath);
		if (file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 根据文件路径获取文件的大小
	 * 
	 * @param filePath
	 * @return
	 */
	public String getFileNameByFilePath(String filePath) {
		String name = null;
		File file = new File(filePath);
		if (file.exists()) {
			name = file.getName();
		}
		return name;
	}

	/**
	 * 解压缩功能. 将zip文件解压到folderPath目录下.
	 * 
	 * @param zipFilePath
	 *            zip源文件的路径
	 * @param folderPath
	 *            解压之后的路径
	 * @throws Exception
	 */
	public boolean upZipFile(String zipFilePath, String folderPath) {
		File dir = new File(folderPath);
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		ZipFile zfile = null;
		try {
			zfile = new ZipFile(new File(zipFilePath), ZipFile.OPEN_READ);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		Enumeration<?> zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				Log.d("upZipFile", "ze.getName() = " + ze.getName());
				String dirstr = folderPath + File.separator + ze.getName();
				try {
					dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return false;
				}
				Log.d("upZipFile", "path = " + dirstr);
				File f = new File(dirstr);
				f.mkdirs();
				continue;
			}
			Log.d("upZipFile", "ze.getName() = " + ze.getName());
			OutputStream os = null;
			try {
				os = new BufferedOutputStream(new FileOutputStream(folderPath + File.separator + ze.getName()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			InputStream is = null;
			try {
				is = new BufferedInputStream(zfile.getInputStream(ze));
			} catch (IOException e) {
				e.printStackTrace();
				// return false;
			}
			int readLen = 0;
			try {
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					os.write(buf, 0, readLen);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			try {
				is.close();
				os.close();
				zfile.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		Log.d("upZipFile", "finish");
		return true;
	}

	/**
	 * 
	 * @param path
	 *            网络的url
	 * @param filePath
	 *            本地存储路径
	 * @return
	 */
	public static File downFileFromWeb(String path, String filePath) throws Exception {

		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);

		if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			File file = new File(filePath);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
				fos.flush();
			}
			fos.close();
			is.close();
			return file;
		}

		return null;

	}

	/**
	 * 把一个inputstream里面的内容转化成一个byte[]
	 */
	public static byte[] getBytes(InputStream is) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		is.close();
		bos.flush();
		byte[] result = bos.toByteArray();
		bos.close();
		return result;
	}

	/**
	 * 获取Html的模板字符串
	 * 
	 * @return
	 */
	public String getHTMLTemplate() {
		try {
			InputStream in = context.getResources().getAssets().open("web/template.html");
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			// 选择合适的编码，如果不调整会乱码
			String res = new String(buffer, Charset.forName("UTF-8"));
			// 关闭资源
			in.close();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 根据文件路径，获取到文件内容
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String getFileString(String path) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			L.d("找不到文件！");
		}
		int length = 0;
		byte[] buffer = null;
		try {
			length = fin.available();
			buffer = new byte[length];
			fin.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			L.d("读取文件失败！");
		} finally {
			try {
				fin.close();// 关闭资源
			} catch (IOException e) {
				e.printStackTrace();
				L.d("关闭文件输入流失败！");
			}
		}
		String ret = EncodingUtils.getString(buffer, "UTF-8") + "<br/><br/>";
		return ret;
	}

	/**
	 * 把流写成文件
	 * @param is　文件流
	 * @param path　文件名(全路径)
	 * @return
	 */
	public static boolean downFileForInputStream(InputStream is, String path) {

		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		try {
			bis = new BufferedInputStream(is);
			fos = new FileOutputStream(path);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				fos.flush();
			}
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (null != bis) {
					bis.close();
				}
				if (null != fos) {
					fos.close();
				}
			} catch (Exception e) {

			}
		}
		return true;
	}
	
	/**
	 * 对单个文件进行压缩
	 * 
	 * @param zipAfterFileName
	 *            　压缩后的文件名(全路径)
	 * @param targetFileName
	 *            　　要压缩的文件名(全路径)
	 * @return
	 */
	public static boolean zipFile(String zipAfterFileName, String targetFileName) {
		try {
			int buffer = 2048;
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipAfterFileName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
			byte data[] = new byte[buffer];
			File f = new File(targetFileName);
			FileInputStream fi = new FileInputStream(f);
			origin = new BufferedInputStream(fi, buffer);
			ZipEntry entry = new ZipEntry(f.getName());
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, buffer)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 解zip 文件
	 * 
	 * @param zipFileName
	 *            zip文件路径(全路径)
	 * @param fileDir
	 *            　解压后的文件的存放的目录
	 * @return
	 */
	public static boolean unZipFile(String zipFileName, String fileDir) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		ZipFile zipFile = null;
		try {
			int buffer = 1024 * 2;
			zipFile = new ZipFile(zipFileName);
			Enumeration emu = zipFile.entries();
			while (emu.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) emu.nextElement();
				// 会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
				if (entry.isDirectory()) {
					new File(fileDir + entry.getName()).mkdirs();
					continue;
				}
				bis = new BufferedInputStream(zipFile.getInputStream(entry));
				File file = new File(fileDir + entry.getName());
				// 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
				// 而这个文件所在的目录还没有出现过，所以要建出目录来。
				File parent = file.getParentFile();
				if (parent != null && (!parent.exists())) {
					parent.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, buffer);
				int count;
				byte data[] = new byte[buffer];
				while ((count = bis.read(data, 0, buffer)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
			}
			return true;
		} catch (Exception e) {
		} finally {
			try {
				if (null != bis) {
					bis.close();
				}
				if (null != bos) {
					bos.close();
				}
				if (null != zipFile) {
					zipFile.close();
				}
			} catch (Exception e) {

			}
		}
		return false;
	}
	
	/**
	 * 删除指定文件外的所有文件
	 * @return
	 */
	public static boolean deleteFilesOutsideOfSpecifiedFile(String dir,File specifiedFile){
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		if(!dirFile.exists() ||  !dirFile.isDirectory() || !specifiedFile.isFile()){
			return false;
		}
		
		
		return false;
	}
	
	

}