package com.ffait.pass;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.ffait.util.ByteUtils;
import com.ffait.util.DownloadFromUrl;
import com.ffait.util.JsonToObject;

import com.ffait.util.ParameterOperate;
import com.ffait.util.ShowUtils;
import com.google.gson.Gson;

import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.text.SimpleDateFormat;

public class MainFrame {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/*
	 * 
	 * textName textInfo textReport lblCrame btnPrint btnExit lblPhoto
	 */

	static FaceService fs = new FaceService();
	static JFrame frame;
	static JLabel textName;
	static JTextArea textInfo;
	static JTextArea textReport;
	static JLabel lblCrame;
	// static JButton btnPrint;
	// static JButton btnExit;
	static JLabel lblPhoto;
	static JLabel message;
	static JLabel lblbackPhoto;
	static int flag = 0;
	// 当前工作工作状态 true :人脸识别 false:报告分析展示
	static boolean state = true;

	static String backgroundPath = "C:\\parameter\\okBack.jpeg";
	static String facePath = "C:\\parameter\\faceImage.jpg";
	static VideoCapture camera;
	// 串口对象
	private static SerialPort mSerialport;

	public MainFrame() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("闸机控制");
		try {
			frame.setIconImage(ImageIO.read(new File("C:\\parameter\\nicola.jpg")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setBounds(0, 0, 1280, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		lblPhoto = new JLabel("");
		lblPhoto.setBounds(1120, 40, 141, 178);
		frame.add(lblPhoto);

		lblbackPhoto = new JLabel("");
		lblbackPhoto.setBounds(0, 0, 1024, 744);
		BufferedImage backPhoto = null;

		// 设置透明
		lblbackPhoto.setOpaque(false);
		frame.add(lblbackPhoto);
		// 人员名称
		textName = new JLabel("",JLabel.CENTER);
		textName.setBounds(800, 40, 320, 178);
		textName.setFont(new Font("黑体", Font.PLAIN, 40));
		textName.setBackground(new Color(238, 238, 238));
		frame.add(textName);

		// 考核信息
		textInfo = new JTextArea();
		textInfo.setEditable(false);
		textInfo.setOpaque(false);
		textInfo.setBounds(800, 230, 470, 200);
		textInfo.setFont(new Font("黑体", Font.PLAIN, 25));
		textInfo.setLineWrap(true); // 激活自动换行功能
		textInfo.setWrapStyleWord(true); // 激活断行不断字功能
		textInfo.setBackground(new Color(238, 238, 238));
		frame.add(textInfo);
		textInfo.setColumns(10);
		// 报告分析
		textReport = new JTextArea();
		textReport.setOpaque(false);
		textReport.setEditable(false);
		textReport.setBounds(800, 430, 470, 300);

		textReport.setFont(new Font("黑体", Font.PLAIN, 25));
		textReport.setBackground(new Color(238, 238, 238));
		frame.add(textReport);
		textReport.setColumns(10);

		// 提示信息栏
		message = new JLabel("培训结果查询", JLabel.CENTER);
		message.setFont(new Font("黑体", Font.PLAIN, 30));
		message.setForeground(Color.RED);
		message.setBounds(20, 10, 800, 100);
		frame.add(message);

		lblCrame = new JLabel("");
		lblCrame.setBounds(16, 124, 768, 576);
		frame.add(lblCrame);

		int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - frame.getWidth()) / 2;
		int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - frame.getHeight()) / 2;
		frame.setLocation(x, 0);
		// 设置部分组件可见性
		setVisible(false);
	}

	private static void openSerialPort(String comname) {
		int baudrate = 115200;
		// 检查串口名称是否获取正确
		if (comname == null || comname.equals("")) {
			System.out.println("没有搜索到有效串口！");
		} else {
			try {
				mSerialport = SerialPortManager.openPort(comname, baudrate);
				if (mSerialport != null) {
					System.out.println(comname + "串口已打开");
					// mSerialPortOperate.setText("关闭串口");
				}
			} catch (PortInUseException e) {
				System.out.println("串口已被占用！");
			}
		}
	}

	private static boolean sendData() {
		// 待发送数据
		if (mSerialport == null) {
			ShowUtils.warningMessage("请先打开串口！");
			return false;
		}
		SerialPortManager.sendToPort(mSerialport, ByteUtils.hexStr2Byte(ParameterOperate.extract("command")));
		return true;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		long pretime = System.currentTimeMillis();
		camera = new VideoCapture(0);
		if (!camera.isOpened()) {
			System.out.println("Camera Error");
		} else {
			Mat frame = new Mat();
			while (flag == 0) {
				camera.read(frame);
				BufferedImage bi = fs.mat2BI(frame);
				long currenttime = System.currentTimeMillis();
				long timeGap=Long.parseLong(ParameterOperate.extract("timeGap"));
				if (currenttime - pretime > timeGap && state) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// long a=System.currentTimeMillis();
							if (true) {
								message.setText("人脸识别中，正在为您输出培训结果！");
								String s = fs.judgeMember(bi);
								if(null==s) {
									message.setText("请检查服务器是否开启");
								}else if("null".equals(s)) {
									message.setText("没有检测到人脸");
								}else if("noFace".equals(s)) {
									message.setText("没有检测到人脸");
								}else if ("noUser".equals(s)) {
									message.setText("检测不成功，请将人脸置于屏幕中央！");
								}else {// 人脸验证通过
									// 不在新增线程进行人脸识别，点击退出后再开启人脸识别
									state = false;
									int f1 = s.indexOf('_');
									int f2 = s.indexOf('_', f1 + 1);
									int f3 = s.indexOf('_', f2 + 1);
									int f4 = s.indexOf('_', f3 + 1);
									int f5 = s.indexOf('_', f4 + 1);
									String userID = s.substring(0, f1);
									String userCode = s.substring(f1 + 1, f2);
									String userName = s.substring(f2 + 1, f3);
									String roleId = s.substring(f3 + 1, f4);
									String photoUrl = s.substring(f4 + 1, f5);
									String projects = s.substring(f5 + 1);

									// 展示用的数据
									int avg = 0;
									String type = "";
									String projectNames = ParameterOperate.extract("projectNames");
									String projectIds = ParameterOperate.extract("projectIds");
									projectNames = projectNames.replace("\"", "");
									// 成绩展示用
									String[] pros = projectNames.split("_");
									// 构建tInfo
									projectNames = projectNames.replace('_', ',');

									String res = "";
									// 获取考核相关数据
									res = fs.reportAnalysis(userID, projectIds);

									BufferedImage photo = DownloadFromUrl.downloadBufferedImageFromUrl(
											ParameterOperate.extract("mainService") + photoUrl, "jpg");

									// 获取项目列表
									ArrayList<ExamResult> results = new ArrayList<ExamResult>();
									ExamResult result = null;
									try {
										results = (ArrayList<ExamResult>) JsonToObject.getExamresult(res);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									Date d = new Date();
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy年 MM 月 dd 日");
									String date = sdf.format(d);

									String tName = userName;
									String tInfo = "    " + date + "参加" + projectNames + "培训考核，经考核合格，具备相应的专业知识和技能!";

									StringBuilder sBuilder = new StringBuilder();
									if (results != null && results.size() > 0) {
										for (int i = 0; i < results.size(); i++) {

											sBuilder.append(pros[i] + ":\t");
											sBuilder.append(results.get(i).getPoint() + "\n");
											avg += results.get(i).getPoint();
										}
										avg /= results.size();
										if (avg >= 80) {
											type = "优秀";
										} else if (avg >= 60) {
											type = "及格";
										} else if (avg < 60) {
											tInfo = date + "参加" + projectNames + "培训考核，考核未通过!";
											type = "不及格";
										}

									}

									sBuilder.append("\n总评成绩：" + avg + "分，" + type);
									String tReport = sBuilder.toString();

									textName.setText(tName);
									textInfo.setText(tInfo);
									textReport.setText(tReport);

									// 将需要打印的照片写入文件
									try {
										File outputfile = new File(facePath);
										ImageIO.write(photo, "jpg", outputfile);
									} catch (IOException e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}

									// 转换部分组件可见性
									setVisible(true);

									lblPhoto.setIcon(
											new ImageIcon(photo.getScaledInstance(141, 178, Image.SCALE_DEFAULT)));

									if ("2".equals(roleId)) {
										message.setText("您是管理员，开启闸机！");
										message.setVisible(true);
										try {
											Thread.sleep(8000);
											state = true;
											setVisible(false);
											clearInfo();
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										openSerialPort(ParameterOperate.extract("commName"));
										if (sendData()) {
											SerialPortManager.closePort(mSerialport);
										}
									} else {
										if (avg < Integer.parseInt(ParameterOperate.extract("passLine"))) {
											message.setText("你没有达到考核要求，请继续学习！");
											message.setVisible(true);
											try {
												Thread.sleep(8000);
												state = true;
												setVisible(false);
												clearInfo();
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										} else {
											message.setText("达到考核要求，闸机开启！");
											message.setVisible(true);
											try {
												Thread.sleep(8000);
												state = true;
												setVisible(false);
												clearInfo();
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
											openSerialPort(ParameterOperate.extract("commName"));
											if (sendData()) {
												SerialPortManager.closePort(mSerialport);
												//System.exit(0);
											}

										}
									}

								}
								 

							}
						}

					}).start();
					pretime = currenttime;
				}
				lblCrame.setIcon(new ImageIcon(bi));
			}
		}
	}

	public static void setVisible(boolean state) {
		textName.setVisible(state);
		textInfo.setVisible(state);
		textReport.setVisible(state);
		lblPhoto.setVisible(state);
		lblbackPhoto.setVisible(state);

		//lblCrame.setVisible(!state);
		message.setVisible(!state);
	}

	public static void clearInfo() {

		textName.setText("");
		textInfo.setText("");
		textReport.setText("");
		lblPhoto.setIcon(null);
	}

}
