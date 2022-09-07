package com.ffait.util;

import com.ffait.pass.FaceService;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Auther: NF
 * @Date: 2022/09/04/19:18
 * @Description:
 */
public class ImageBlur {
    static FaceService fs=new FaceService();

    public static void blur(Mat src) {
        //克隆，数据,大小，类型等同于输入图像
        Mat image = src.clone();
        Imgproc.blur(src, image, new Size(100,100),new Point(1,1));
        HighGui.imshow("均值模糊(降噪)", image);
        HighGui.waitKey();
    }

    public static BufferedImage gausssianBlur(Mat src) {
        //返回一个恒等指定大小和类型矩阵,数据全部有0填充。
        Mat image = Mat.eye(src.size(),src.type());
        Imgproc.GaussianBlur(src, image, new Size(11,11), 7,7);
        return fs.mat2BI(image);
    }

    //虚化图片后将人脸在画上去
    public static BufferedImage drawFace(BufferedImage image,BufferedImage face){
        Graphics g = image.getGraphics();

        g.drawImage(face,350,190,270,340,null);

        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(5.0f));
        g2.drawOval(350, 190, 270, 340);

        return image;
    }




    public static void mediaBlur(Mat src) {
        //返回一个恒等指定大小和类型矩阵,数据全部有0填充。
        Mat image = Mat.zeros(src.size(),src.type());
        Imgproc.medianBlur(src, image, 9);
        HighGui.imshow("中值滤波(降噪)", image);
        HighGui.waitKey();
    }

    public static void bilateralFilter(Mat src) {
        //返回一个恒等指定大小和类型矩阵,数据全部有BGR分别由100填充。
        Mat image = Mat.ones(src.size(),src.type());
        Imgproc.bilateralFilter(src, image, 2, 200, 5);
        HighGui.imshow("双边滤波(降噪)", image);
        HighGui.waitKey();
    }

    public static ImageIcon imageScaled(BufferedImage image, int targetW,int targetH) throws IOException {
        BufferedImage copy = ImageUtils.deepCopy(image);

        int h = image.getHeight();
        int w = image.getWidth();

        double scale=w/(double)h;
        double targetScale = targetW/(double)targetH;

        if(scale == targetScale){
            return new ImageIcon(copy.getScaledInstance(targetW,targetH,Image.SCALE_DEFAULT));
        }
        //切去部分高度
        else if(targetScale > scale){
            int cutH = (int) ((targetScale-scale) * h)/2;
            BufferedImage subimage = copy.getSubimage(0, cutH, w, h - cutH);

            ImageIO.write(subimage,"jpg",new File("C:\\Users\\86187\\Pictures\\bj\\a.jpg"));

            return new ImageIcon(subimage.getScaledInstance(targetW,targetH,Image.SCALE_DEFAULT));


        }else if(targetScale < scale){

            int cutW = (int) ((scale-targetScale) * w)/2;
            BufferedImage subimage = copy.getSubimage( cutW,0 , w - cutW , h );
            ImageIO.write(subimage,"jpg",new File("C:\\Users\\86187\\Pictures\\bj\\a.jpg"));

            return new ImageIcon(subimage.getScaledInstance(targetW,targetH,Image.SCALE_DEFAULT));
        }


        return null;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage read = ImageIO.read(new File("C:\\Users\\86187\\Pictures\\bj\\0bc73f4215SrF3v.jpg"));

        ImageIcon imageIcon = imageScaled(read, 1960, 1080);

        JFrame jFrame = new JFrame();
        jFrame.setSize(1960,1080);
        jFrame.setVisible(true);

        JLabel label = new JLabel();
        label.setSize(1960,1080);
        label.setVisible(true);
        jFrame.add(label);
        label.setIcon(imageIcon);


    }

}

