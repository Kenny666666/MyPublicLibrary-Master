package com.kenny.baselibrary.utils.common;

import java.util.UUID;

/**
 * @author kenny
 * @version 创建时间：2015-2-12 
 * 类说明：生成表主键的工具类
 */
public class UUIDUtil {
	
	public static String generateHexUUID() {
		String strUUID = UUID.randomUUID().toString().replaceAll("-", "");

		return strUUID;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
}
