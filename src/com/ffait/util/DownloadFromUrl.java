package com.ffait.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;

public class DownloadFromUrl {
	
	//通过URL下载图片
	public static BufferedImage downloadBufferedImageFromUrl(String url,String type) {
        try {
//        	byte[] bs = url.getBytes("GBK");
//        	String url1 = new String(bs, Charset.forName("utf-8"));
//        	System.out.println(url1);
            HttpURLConnection httpUrl = (HttpURLConnection) new URL(url).openConnection();
            httpUrl.connect();
            File file = new File("tmpPhoto."+type);
            OutputStream os = new FileOutputStream(file);
            java.io.InputStream ins = httpUrl.getInputStream();
            int bytesRead;
            int len = 8192;
            byte[] buffer = new byte[len];
            while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
            httpUrl.disconnect();
            return ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
