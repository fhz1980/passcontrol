package com.ffait.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ParameterOperate {
	public static String extract(String key) {
		String str = null;
		try {
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(
					new FileInputStream("C:\\parameter\\passcontrol.properties"));
			prop.load(in);
			str = prop.getProperty(key);

		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 重指定配置文件中获取key对应的value
	 * @param key  配置文件中的key
	 * @param url  配置文件的地址
	 * @return
	 */
	public static String extract(String key,String url) {
		String str = null;
		try {
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream(
					new FileInputStream(url));
			prop.load(in);
			str = prop.getProperty(key);

		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

}
