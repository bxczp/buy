package com.bx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @date 2016年3月18日 PropertiseUtil.java
 * @author CZP
 * @parameter
 */
public class PropertiseUtil {

	public static String getValue(String key) {
		Properties properties = new Properties();
		// 不要忘了 正斜杠
		InputStream inputStream = new PropertiseUtil().getClass().getResourceAsStream("/ebuy.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String value = properties.getProperty(key);
		return value;
	}

}
