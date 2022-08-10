package com.ffait.pass;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.ffait.util.ParameterOperate;

public class FaceService {

    //返回image 的用户id和姓名
    public String judgeMember(BufferedImage image) {
		return HttpService.faceFea(ParameterOperate.extract("mainServiceJudge"), image);
		
	}
  //返回报告分析
    public String reportAnalysis(String userID,String projectIds) {
		return HttpService.getReport(ParameterOperate.extract("mainServiceReport")
				+"?userID="+ userID +"&projectIds="+projectIds);
	}
    //注册
    public String registerMember(BufferedImage image,String idNum,String name) {
    	if((idNum != null && !"".equals(idNum) && !"".equals(name) && name != null)) {
    		return HttpService.faceRegister(ParameterOperate.extract("mainServiceRegister"), image,idNum,name);
    	}
    	return null;
	}
    
	public BufferedImage mat2BI(Mat mat){
		//Mat mat=srcmat.clone();
		double scale=960.0/mat.cols();
		Imgproc.resize(mat, mat, new Size(mat.cols()*scale,mat.rows()*scale));
		Core.flip(mat,mat,1);
       int dataSize =mat.cols()*mat.rows()*(int)mat.elemSize();
        byte[] data=new byte[dataSize];
        mat.get(0, 0,data);
        int type=mat.channels()==1?BufferedImage.TYPE_BYTE_GRAY:BufferedImage.TYPE_3BYTE_BGR;
        if(type==BufferedImage.TYPE_3BYTE_BGR){
            for(int i=0;i<dataSize;i+=3){
                byte blue=data[i+0];
                data[i+0]=data[i+2];
                data[i+2]=blue;
            }
        }
        BufferedImage image=new BufferedImage(mat.cols(),mat.rows(),type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return image;
    }
	
	public BufferedImage mat2BImg(Mat mat,double width,double height){
		//Mat mat=srcmat.clone();
		double scale=960.0/mat.cols();
		Imgproc.resize(mat, mat, new Size(width,height));
		Core.flip(mat,mat,1);
       int dataSize =mat.cols()*mat.rows()*(int)mat.elemSize();
        byte[] data=new byte[dataSize];
        mat.get(0, 0,data);
        int type=mat.channels()==1?BufferedImage.TYPE_BYTE_GRAY:BufferedImage.TYPE_3BYTE_BGR;
        if(type==BufferedImage.TYPE_3BYTE_BGR){
            for(int i=0;i<dataSize;i+=3){
                byte blue=data[i+0];
                data[i+0]=data[i+2];
                data[i+2]=blue;
            }
        }
        BufferedImage image=new BufferedImage(mat.cols(),mat.rows(),type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return image;
    }
	
	public static void main(String[] args) {
		FaceService fs=new FaceService();
	}
}
