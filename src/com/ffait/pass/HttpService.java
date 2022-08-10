package com.ffait.pass;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpService {
	//url：请求路经
	//filePath：需要上传的文件路径
	public static String fileUpload(String url,String filePath){
		 String result=null;
		 CloseableHttpClient httpclient = HttpClients.createDefault(); 
       try { 
           HttpPost httppost = new HttpPost(url); 
           RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
           httppost.setConfig(requestConfig);
           FileBody bin = new FileBody(new File(filePath)); 
           //StringBody comment = new StringBody("This is comment", ContentType.TEXT_PLAIN); 
           HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("fileName", bin).build(); 
           httppost.setEntity(reqEntity); 
           CloseableHttpResponse response = httpclient.execute(httppost); 
           try { 
               HttpEntity resEntity = response.getEntity(); 
               if (resEntity != null) { 
                   String responseEntityStr = EntityUtils.toString(response.getEntity());
                   result=responseEntityStr;
                   if("null".equals(result)){
	                	  return null;
	                  }
                   
				   //System.out.println("Response content length: " + resEntity.getContentLength()); 
               } 
               EntityUtils.consume(resEntity); 
           } finally { 
               response.close(); 
           } 
       } catch (ClientProtocolException e) { 
           e.printStackTrace(); 
       } catch (IOException e) { 
           e.printStackTrace(); 
       } finally { 
           try { 
               httpclient.close(); 
           } catch (IOException e) { 
               e.printStackTrace(); 
           } 
       } 
       return result;
	}
	public static String faceFea(String url,BufferedImage bufferedImage){
		String result=null;
		 CloseableHttpClient httpclient = HttpClients.createDefault(); 
     try { 
         HttpPost httppost = new HttpPost(url); 
         RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
         httppost.setConfig(requestConfig);
         File outputfile = new File("image.jpg");
         ImageIO.write(bufferedImage, "jpg", outputfile);
         FileBody bin = new FileBody(outputfile); 
         HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("fileName", bin).build(); 
         httppost.setEntity(reqEntity); 
         //System.out.println("executing request " + httppost.getRequestLine()); 
         CloseableHttpResponse response = httpclient.execute(httppost); 
         try { 
             //System.out.println(response.getStatusLine()); 
             HttpEntity resEntity = response.getEntity(); 
             if (resEntity != null) { 
                 String responseEntityStr = EntityUtils.toString(response.getEntity());
                 result= responseEntityStr;
                 if("null".equals(result)){
               	  return null;
                 }
                 //System.out.println(responseEntityStr);
                 //System.out.println("Response content length: " + resEntity.getContentLength()); 
             }
             EntityUtils.consume(resEntity); 
         } finally { 
             response.close(); 
         } 
     } catch (ClientProtocolException e) { 
         e.printStackTrace(); 
     } catch (IOException e) { 
         e.printStackTrace(); 
     } finally { 
         try { 
             httpclient.close(); 
         } catch (IOException e) { 
             e.printStackTrace(); 
         } 
     } 
     return result;
	}
	
	//注册服务
	public static String faceRegister(String url,BufferedImage bufferedImage,String idNum,String name){
		String result=null;
		 CloseableHttpClient httpclient = HttpClients.createDefault(); 
     try { 
         HttpPost httppost = new HttpPost(url); 
         RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
         httppost.setConfig(requestConfig);
         File outputfile = new File("image.jpg");
         ImageIO.write(bufferedImage, "jpg", outputfile);
         FileBody bin = new FileBody(outputfile); 
         StringBody username = new StringBody(idNum, Charset.forName("UTF-8")); 
         StringBody name1 = new StringBody(name, Charset.forName("UTF-8")); 
         
         MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//         multipartEntityBuilder.setCharset(Charset.forName("utf-8"));
         multipartEntityBuilder.setCharset(Charset.forName("utf-8"));
         multipartEntityBuilder.addPart("fileName", bin);
         multipartEntityBuilder.addPart("idNum",username);
         multipartEntityBuilder.addPart("name",name1);
         
         HttpEntity reqEntity = multipartEntityBuilder.build(); 
         httppost.setEntity(reqEntity); 
         //System.out.println("executing request " + httppost.getRequestLine()); 
         CloseableHttpResponse response = httpclient.execute(httppost); 
         try { 
             //System.out.println(response.getStatusLine()); 
             HttpEntity resEntity = response.getEntity(); 
             if (resEntity != null) { 
                 String responseEntityStr = EntityUtils.toString(response.getEntity());
                 result= responseEntityStr;
                 if("null".equals(result)){
               	  return null;
                 }
                 //System.out.println(responseEntityStr);
                 //System.out.println("Response content length: " + resEntity.getContentLength()); 
             }
             EntityUtils.consume(resEntity); 
         } finally { 
             response.close(); 
         } 
     } catch (ClientProtocolException e) { 
         e.printStackTrace(); 
     } catch (IOException e) { 
         e.printStackTrace(); 
     } finally { 
         try { 
             httpclient.close(); 
         } catch (IOException e) { 
             e.printStackTrace(); 
         } 
     } 
     return result;
	}
	
	
	public static String getReport(String url){
		//url格式问题
		url= url.replace("\"", "%22");
		 String result=null;
		 CloseableHttpClient httpclient = HttpClients.createDefault(); 
    try { 
        HttpGet httpGet = new HttpGet(url); 

//        httppost.setEntity(new StringEntity(jsonParam.toString(), Charset.forName("UTF-8")));
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
        httpGet.setConfig(requestConfig);
        
        CloseableHttpResponse response = httpclient.execute(httpGet); 
        //解析返回数据
        try { 
            HttpEntity resEntity = response.getEntity(); 
            if (resEntity != null) { 
                String responseEntityStr = EntityUtils.toString(response.getEntity());
                result=responseEntityStr;
                if("null".equals(result)){
	                	  return null;
	                  }
                
				   //System.out.println("Response content length: " + resEntity.getContentLength()); 
            } 
            EntityUtils.consume(resEntity); 
        } finally { 
            response.close(); 
        } 
    } catch (ClientProtocolException e) { 
        e.printStackTrace(); 
    } catch (IOException e) { 
        e.printStackTrace(); 
    } finally { 
        try { 
            httpclient.close(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
    return result;
	}	
	
	
	
}
