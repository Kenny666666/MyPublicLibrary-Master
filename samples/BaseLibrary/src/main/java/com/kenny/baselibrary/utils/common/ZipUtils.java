
package com.kenny.baselibrary.utils.common;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 功能： 1 、实现把指定文件夹下的所有文件压缩为指定文件夹下指定 zip 文件 2 、实现把指定文件夹下的 zip 文件解压到指定目录下
 * 
 * @author jsx
 */

public class ZipUtils {

	/**
	 * 解压缩多个目录文件
	 * @param zipFile
	 * @param targetDir
	 */
	public static void UnzipMoreFile(File zipFile, String targetDir) {
		int BUFFER = 4096; // 这里缓冲区我们使ｿKBｿ
		String strEntry; // 保存每个zip的条目名ｿ

		try {
			BufferedOutputStream dest = null; // 缓冲输出ｿ
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(
					new BufferedInputStream(fis));
			ZipEntry entry; // 每个zip条目的实ｿ

			while ((entry = zis.getNextEntry()) != null) {

				try {

					int count;
					byte data[] = new byte[BUFFER];
					strEntry = entry.getName();
					if(strEntry!=null){
						strEntry=strEntry.replace("\\","/");
					}
					File entryFile = new File(targetDir + File.separator
							+ strEntry);
					File entryDir = new File(entryFile.getParent());
					// File entryDir = new File(entryFile.getPath());
					if (!entryDir.exists()) {
						entryDir.mkdirs();
					}

					FileOutputStream fos = new FileOutputStream(entryFile);
					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = zis.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			zis.close();
		} catch (Exception cwj) {
			cwj.printStackTrace();
		}
	}
    /**
     * 功能：把 sourceDir 目录下的所有文件进行 zip 格式的压缩，保存为指定 zip 文件 create date:2009- 6- 9 author:Administrator
     * 
     * @param sourceDirE:// 我的备份
     * @param zipFile 格式： E://stu //zipFile.zip 注意：加入 zipFile 我们传入的字符串值是： "E://stu //" 或者 "E://stu " 如果 E 盘已经存在 stu 这个文件夹的话，那么就会出现
     *            java.io.FileNotFoundException: E:/stu ( 拒绝访问。 ) 这个异常，所以要注意正确传参调用本函数哦
     */
    public static void zip(String sourceDir, String zipFile) {
        OutputStream os;
        try {
            os = new FileOutputStream(zipFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            ZipOutputStream zos = new ZipOutputStream(bos);
            File file = new File(sourceDir);
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
            zos.closeEntry();
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * @param source
     * @param basePath
     * @param zos
     */
    private static void zipFile(File source, String basePath, ZipOutputStream zos) {
        
        File[] files = new File[0];
        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }
        String pathName;
        byte[] buf = new byte[1024];
        int length = 0;
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    pathName = file.getPath().substring(basePath.length() + 1) + "/";
                    zos.putNextEntry(new ZipEntry(pathName));
                    zipFile(file, basePath, zos);
                } else {
                    pathName = file.getPath().substring(basePath.length() + 1);
                    InputStream is = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    zos.putNextEntry(new ZipEntry(pathName));
                    while ((length = bis.read(buf)) > 0) {
                        zos.write(buf, 0, length);
                    }
                    is.close();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 解压 zip 文件，注意不能解压 rar 文件哦，只能解压 zip 文件 解压 rar 文件 会出现 java.io.IOException: Negative seek offset 异常 create date:2009- 6- 9 author:Administrator
     * 
     * @param zipfile zip 文件，注意要是正宗的 zip 文件哦，不能是把 rar 的直接改为 zip 这样会出现 java.io.IOException:Negative seek offset 异常
     * @param destDir
     * @throws IOException
     */
    
    public static void unZipSpecifyFiles(String zipfile, String destDir, String[] fileNames) {
        
        destDir = destDir.endsWith("//") ? destDir : destDir + "//";
        File pDir = new File(destDir);
        if (!pDir.exists()) {
            pDir.mkdirs();
        }
        byte b[] = new byte[1024];
        int length;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(new File(zipfile));
            Enumeration enumeration = zipFile.entries();
            ZipEntry zipEntry = null;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                File loadFile = new File(destDir + zipEntry.getName());
                if (fileNames != null && fileNames.length > 0) {
                    for (String fileName : fileNames) {
                        if (fileName != null && fileName.equals(zipEntry.getName())) {
                            if (!loadFile.getParentFile().exists()) {
                                loadFile.getParentFile().mkdirs();
                            }
                            OutputStream outputStream = new FileOutputStream(loadFile);
                            InputStream inputStream = zipFile.getInputStream(zipEntry);
                            while ((length = inputStream.read(b)) > 0)
                                outputStream.write(b, 0, length);
                        }
                    }
                }
                
            }
            System.out.println(" 文件解压成功 ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 解压 zip 文件，注意不能解压 rar 文件哦，只能解压 zip 文件 解压 rar 文件 会出现 java.io.IOException: Negative seek offset 异常 create date:2009- 6- 9 author:Administrator
     * 
     * @param zipfile zip 文件，注意要是正宗的 zip 文件哦，不能是把 rar 的直接改为 zip 这样会出现 java.io.IOException:Negative seek offset 异常
     * @param destDir
     * @throws IOException
     */
    
    public static void unZip(String zipfile, String destDir) {
        
        destDir = destDir.endsWith("//") ? destDir : destDir + "//";
        byte b[] = new byte[1024];
        int length;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(new File(zipfile));
            Enumeration enumeration = zipFile.entries();
            ZipEntry zipEntry = null;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                File loadFile = new File(destDir + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    // 这段都可以不要，因为每次都貌似从最底层开始遍历的
                    loadFile.mkdirs();
                } else {
                    if (!loadFile.getParentFile().exists()) {
                        loadFile.getParentFile().mkdirs();
                    }
                    OutputStream outputStream = new FileOutputStream(loadFile);
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    while ((length = inputStream.read(b)) > 0)
                        outputStream.write(b, 0, length);
                }
            }
            System.out.println(" 文件解压成功 ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	/**
	 * zip压缩
	 * 
	 * @param data
	 *            二进制数据
	 * @param fileName
	 *            zip文件里的文件名
	 * @return 压缩之后的二进制数据
	 * @throws Exception
	 */
	public static byte[] zip(byte[] data, String fileName) throws Exception {
		Validate.notNull(data, "二进制数据不能为null");
		Validate.notNull(fileName, "zip文件里的文件名不能为null");

		byte[] bt = null;
		ByteArrayOutputStream bos =null;
		ZipOutputStream zos=null;
		try {
			bos = new ByteArrayOutputStream();
			zos = new ZipOutputStream(bos);
			ZipEntry entry = new ZipEntry(fileName);
			entry.setSize(data.length);
			zos.putNextEntry(entry);
			zos.write(data);
			zos.closeEntry();
			zos.close();
			bt = bos.toByteArray();
			bos.close();
		} catch (Exception e) {
			throw e;
		}  finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(bos);
        }
		return bt;
	}

	/**
	 * zip压缩
	 * 
	 * @param files
	 *            文件Map,key:文件名;value:文件的byte内容.
	 * @return
	 * @throws Exception
	 */
	public static byte[] zip(Map<String, byte[]> datas) throws Exception {
		Validate.notNull(datas, "文件流不能为null");
		ByteArrayOutputStream bos=null;
		ZipOutputStream zos =null;
		byte[] bt = null;
		
		try {
			bos = new ByteArrayOutputStream();
			zos = new ZipOutputStream(bos);

			for (Map.Entry<String, byte[]> entry : datas.entrySet()) {
				byte[] data = entry.getValue();
				ZipEntry zipEntry = new ZipEntry(entry.getKey());
				zipEntry.setSize(data.length);
				zos.putNextEntry(zipEntry);
				zos.write(data);
				zos.closeEntry();
			}

			zos.close();
			bt = bos.toByteArray();
		} catch (IOException e) {
			throw e;
		}  finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(bos);
        }
		return bt;
	}

	/**
	 * zip压缩
	 * 
	 * @param files
	 *            文件Map,key:文件名;value:文件的byte内容.
	 * @param zipFile
	 *            保存的zip文件,例如:c:/test/test.zip
	 * @return
	 * @throws Exception
	 */
	public static void zip(Map<String, byte[]> datas, String zipFile) throws Exception {
		Validate.notNull(datas, "文件流不能为null");
		Validate.notNull(zipFile, " 保存的zip文件不能为null");
		FileOutputStream out=null;
		ByteArrayOutputStream bos=null;
		ZipOutputStream zos=null;
		
		try {
			out = new FileOutputStream(zipFile);
			bos = new ByteArrayOutputStream();
			zos = new ZipOutputStream(bos);

			for (Map.Entry<String, byte[]> entry : datas.entrySet()) {
				byte[] data = entry.getValue();
				ZipEntry zipEntry = new ZipEntry(entry.getKey());
				zipEntry.setSize(data.length);
				zos.putNextEntry(zipEntry);
				zos.write(data);
				zos.closeEntry();
			}

			zos.close();// important
			out.write(bos.toByteArray());
			out.close();
		} catch (Exception e) {
			throw e;
		}  finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(zos);
        }
	}

	/**
	 * zip压缩
	 * 
	 * @param data
	 *            二进制数据
	 * @param srcFileName
	 *            zip文件里的文件名
	 * @param destFilePathAndName
	 *            压缩之后的zip文件路径和名称
	 * @throws Exception
	 */
	public static void zip(byte[] data, String srcFileName, String destFilePathAndName) throws Exception {
		Validate.notNull(data, " 二进制数据不能为null");
		Validate.notNull(srcFileName, "zip文件里的文件名不能为null");
		Validate.notNull(destFilePathAndName, "压缩之后的zip文件路径和名称不能为null");
		FileOutputStream out=null;
		
		try {
			byte[] bt = zip(data, srcFileName);
			File file = new File(destFilePathAndName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			out = new FileOutputStream(file);
			out.write(bt);
			out.close();
		} catch (Exception e) {
			throw e;
		} finally {
            IOUtils.closeQuietly(out);
        }
	}

	/**
	 * zip解压缩
	 * 
	 * @param data
	 *            zip二进制数据
	 * @return 解压缩之后的二进制数据
	 * @throws Exception
	 */
	public static byte[] unZip(byte[] data) throws Exception {
		Validate.notNull(data, "zip二进制数据不能为null");

		ByteArrayInputStream bis =null;
		ZipInputStream zis =null;
		byte[] bt = null;
		try {
			bis = new ByteArrayInputStream(data);
			zis = new ZipInputStream(bis);
			while (zis.getNextEntry() != null) {
				byte[] buf = new byte[1024];
				int temp;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				while ((temp = zis.read(buf, 0, buf.length)) != -1) {
					bos.write(buf, 0, temp);
				}
				bt = bos.toByteArray();
				bos.close();
			}
			bis.close();
			zis.close();
		} catch (Exception e) {
			throw e;
		}  finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(zis);
        }
		return bt;
	}

	/**
	 * zip解压缩（注意:本方法暂时不支持中文文件夹名称以及文件名）
	 * 
	 * @param zipFile
	 *            需要解压的zip文件,例如:c:/test.zip
	 * @param targetFilePath
	 *            解压到目录,例如c:/test
	 */
	@SuppressWarnings("unchecked")
	public static void unZipFile(String zipFile, String targetFilePath) {
		Validate.notNull("zipFile", "需要解压的zip文件不能为null");
		Validate.notNull("targetFilePath", "解压到目录不能为null");
		FileOutputStream out=null;
		InputStream in =null;
		
		try {
			ZipFile zip = new ZipFile(zipFile);
			Enumeration<ZipEntry> entryEnum = (Enumeration<ZipEntry>) zip.entries();
			// 创建解压到目录
			File targetDir = new File(targetFilePath);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}

			while (entryEnum.hasMoreElements()) {
				ZipEntry entry = entryEnum.nextElement();
				String file = targetFilePath + File.separator + entry.getName();

				// 如果是目录
				if (entry.isDirectory()) {
					File dir = new File(file);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					continue;
				}

				// 如果是文件
				in = zip.getInputStream(entry);
				out = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int temp;
				while ((temp = in.read(buf, 0, buf.length)) != -1) {
					out.write(buf, 0, temp);
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
	}

	/**
	 * zip压缩文件夹
	 * 
	 * @param filePath
	 *            需要压缩的文件夹路径(包含要压缩的文件夹名称) 例如：d:/temp/test
	 * @param zipFileName
	 *            压缩后的需要保存的路径以及zip的名称 例如：d:/temp/test.zip
	 */
	public static void zipFile(String folderPathAndName, String zipFilePathAndName) {
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipFilePathAndName));
			File file = new File(folderPathAndName);
			zipFolder(zos, file, "");
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对文件夹进行压缩操作
	 * 
	 * @param zos
	 * @param file
	 * @param base
	 * @throws Exception
	 */
	private static void zipFolder(ZipOutputStream zos, File file, String base) throws Exception {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			//这一行代码会在压缩文件的每一级文件夹下添加一个空文件（去掉的话终端解析压缩包有问题）
			zos.putNextEntry(new ZipEntry(base + File.separator));
			base = base.length() == 0 ? "" : base + File.separator;
			for (int i = 0; i < files.length; i++) {
				zipFolder(zos, files[i], base + files[i].getName().replace("–","-"));
			}
		} else {
			zos.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(file);
			int b;
			while ((b = in.read()) != -1) {
				zos.write(b);
			}
			in.close();
		}
	}
}
