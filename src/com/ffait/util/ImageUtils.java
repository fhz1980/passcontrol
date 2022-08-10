package com.ffait.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.*;


public class ImageUtils {
	
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
	
	 public static BufferedImage drawFace(BufferedImage image){
	        Graphics g = image.getGraphics();

	        Graphics2D g2 = (Graphics2D)g;  //g是Graphics对象
	        g2.setStroke(new BasicStroke(5.0f));
	        //画头
	        g2.drawOval(350, 190, 270, 340);
	        /*//画眼睛
	        g.drawOval(400, 340, 80, 50);
	        g.drawOval(500, 340, 80, 50);
	        //画鼻子
	        g.drawArc(140, 150, 100, 150, -90, 90);
	        g.drawArc(260, 150, 100, 150, 180, 90);
	        //画嘴巴
	        g.drawOval(440, 430, 100, 50);*/
	        return image;
	    }

	    public static BufferedImage drawRect(BufferedImage image,int x,int y,int width,int height) {

	        Graphics g = image.getGraphics();
	        g.setColor(Color.RED);//画笔颜色
	        g.drawRect(x,y,width,height);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
	        //g.dispose();
	        return image;
	    }
	    
	    public static BufferedImage deepCopy(BufferedImage bi) {

	        ColorModel cm = bi.getColorModel();

	        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();

	        WritableRaster raster = bi.copyData(null);

	        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);

	    }

}
