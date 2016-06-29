package polyu.haleyfu;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonUI;








import org.json.JSONException;

import polyu.haleyfu.ShellUtils.CommandResult;
import polyu.performance.performanceFacade;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main extends JFrame implements ActionListener {

	performanceFacade getPerform=new performanceFacade();
	public static String currentPath = System.getProperty("user.dir");
	private PropUtil PropUtil;
	Map<String,JButton> buttons=new LinkedHashMap<String,JButton>();
	Map<String,String> customButtons=new LinkedHashMap<String,String>();
	public final static String CUSTOMFLAG="_custom";
	
	FileUtil Util=new FileUtil();
	
	//默认应用信息
	public static String app = "com.android.browser";
	public static String activity = "com.android.browser.BrowserActivity";

	public static boolean conect = false;//adb是否有连接

	//定义顶部按钮信息
	private final String PROP = "getprop";
	private final String SCREEN = "takescreen";
	private final String CLEAR = "clear";
	private final String INSTALL = "install";
	private final String RECORD = "record";
	private final String UNINSTALL = "uninstall";
	private final String PUSHLOCAL = "pushlocal";
	private final String MTKSTART = "mtkstart";
	private final String GETLOG = "getlog";
	
	private final String PROXY = "proxy";
	private final String RUNMONKEY = "runmonkey";
	private final String GETMONKEYLOG = "getmonkeylog";
	private final String INWEBWIFI = "inwebwifi";
	
		
	private final String GETMEMORY = "getmemory";
	private final String MEMORYTEST = "memorytest";
	private final String STARTTEST = "starttest";
	private final String FLOWTEST = "flowtest";
	private final String SCREENTEST = "screentest";
	
	private final String ADDBUTTON = "addbutton";
	
	//定义底部按钮信息
	private final String KILLADB = "killadb";
	private final String CLEARTEXT = "cleartext";
	
	// 长宽
	private final Dimension TopItemDimension = new Dimension(10, 30);
	private final Dimension bottomItemDimension = new Dimension(30, 35);

	JPanel mAdminPanel;
	
	JPanel commomUsed;
	JPanel apkOpertion;
	JPanel commomOpertion;
	JPanel performanceOpertion;
	
	JPanel bottomPanel;
	static JTextArea txtIng;

	public Main() throws FileNotFoundException {
		super("adbShortcut@App_distribute"); // title
		loadConfig();
	}

	//获取配置文件
	public void loadConfig() {
		try {
			PropUtil = new PropUtil("config/app.properties");
			app = PropUtil.get("app");
			activity = PropUtil.get("activity");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//本地调试直接用默认值
		}
	}

	// 顶部按钮
	public void buttonForTop(JPanel j,String buttonName, String buttonFlag) {
		JButton button = new JButton(buttonName);
		button.addActionListener(this);
		button.setActionCommand(buttonFlag);
		button.setPreferredSize(TopItemDimension);
		//button.setSize(120,120);
		j.add(button);
		buttons.put( buttonFlag, button);
	}
	

	//底部按钮
	public void buttonForBottom(JPanel j,String buttonName, String buttonFlag) {
		JButton button = new JButton(buttonName);
		button.addActionListener(this);
		button.setActionCommand(buttonFlag);
		button.setPreferredSize(bottomItemDimension);
		j.add(button);
	}

	
	public void setjPanel(JPanel j,String title,LayoutManager mgr){
		 j.setBorder(new TitledBorder(title));
		 j.setLayout(mgr);
	}
	

	public void run() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	    	
		setjPanel(commomUsed=new JPanel(),"常用按钮",new GridLayout(2, 2));
		setjPanel(apkOpertion=new JPanel(),"应用相关",new GridLayout(2, 2));
		setjPanel(commomOpertion=new JPanel(),"自定义操作",new BoxLayout(commomOpertion, BoxLayout.X_AXIS));
		//setjPanel(performanceOpertion=new JPanel(),"性能相关",new BoxLayout(performanceOpertion, BoxLayout.X_AXIS));
    
		mAdminPanel = new JPanel(new FlowLayout());
		mAdminPanel.setLayout(new GridLayout(3, 1)); 
		//mAdminPanel.add(performanceOpertion,0);
		mAdminPanel.add(commomOpertion,0);
		mAdminPanel.add(apkOpertion,0);
		mAdminPanel.add(commomUsed,0);	

		bottomPanel = new JPanel(new FlowLayout());
		bottomPanel.setLayout(new GridLayout(1, 2)); // 底部按钮

		txtIng = new JTextArea();
		drag();// 定义可以拉进去安装方法
        //顶层按钮
		buttonForTop(commomUsed,"获取手机信息", PROP);
		buttonForTop(commomUsed,"获取当前截图", SCREEN);
		buttonForTop(commomUsed,"录制手机屏幕", RECORD);
		buttonForTop(commomUsed,"打开 MTK LOG", MTKSTART);
		buttonForTop(commomUsed,"获取最新MTK LOG", GETLOG);
		
		buttonForTop(apkOpertion,"清数据启动", CLEAR);
		buttonForTop(apkOpertion,"安装应用", INSTALL);
		buttonForTop(apkOpertion,"删除应用", UNINSTALL);
		buttonForTop(apkOpertion,"调用本地PUSH", PUSHLOCAL);
		
		buttonForTop(commomUsed,"设置代理", PROXY);
		buttonForTop(commomUsed,"跑monkey", RUNMONKEY);
		buttonForTop(commomUsed,"检查monkey log", GETMONKEYLOG);
		buttonForTop(commomUsed,"连接inwebWifi", INWEBWIFI);

		buttonForTop(apkOpertion,"内存测试", MEMORYTEST);
		buttonForTop(apkOpertion,"获取内存", GETMEMORY);
		buttonForTop(apkOpertion,"启动时间", STARTTEST);
		buttonForTop(apkOpertion,"当前耗流", FLOWTEST);
		buttonForTop(apkOpertion,"熄屏检查", SCREENTEST);
		
		buttonForTop(commomOpertion,"自定义添加", ADDBUTTON);

		// 底层按钮
		buttonForBottom(bottomPanel,"清屏", CLEARTEXT);
		buttonForBottom(bottomPanel,"kill/restart adb", KILLADB);

		JScrollPane sp = new JScrollPane(txtIng);
		this.setLayout(new BorderLayout());
		this.add(mAdminPanel, BorderLayout.NORTH);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.add(sp, BorderLayout.CENTER);
		setSize(600, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	//操作处理
	public void actionPerformed(ActionEvent evt) {
		//底部按钮不需要连接adb就可以点击
		if (evt.getActionCommand().equals(CLEARTEXT)) {
			txtIng.setText("");
		} else if (evt.getActionCommand().equals(KILLADB)) {
			exec("adb kill-server");
			exec("adb start-server");
			fill("如果adb还没连上请重新插拔usb");
			newline();
		}
        //操作和连接为null，不执行操作
		if (evt == null || conect == false) {
			return;
		}
		
		//执行对应的操作
		if (evt.getActionCommand().equals(SCREEN)) {
			String name = getTime();
			exec("adb shell /system/bin/screencap -p /sdcard/" + name + ".png");
			sleep(300);
			exec("adb pull /sdcard/" + name + ".png " + currentPath + "");
			fill("图片拿出来了，在当前目录看看拉  " + name + ".png");
			newline();

		} else if (evt.getActionCommand().equals(CLEAR)) {
			exec("adb shell pm clear " + app + "");
			exec("adb shell am start -W " + app + "/" + activity + "");
			fill("重启浏览器完成");
			newline();

		} else if (evt.getActionCommand().equals(PROP)) {
			String imei = getProp("ril.gsm.imei");
			String flyme = getProp("ro.build.display.id");
			String flyme1 = getProp("ro.build.inside.id");
			String device = getProp("ro.meizu.product.model");
			String device1 = getProp("ro.product.model");
			String appVersion = getAppVersion();
			
			fill("--------------获取手机信息---------------");		
			if (imei.length() > 4 && imei != null && !imei.contains("adb")) {
				fill("Imei:  " + imei);
			} else {
				fill("Imei:  adb无法获取");
			}						
			fill("固件名:  " + flyme);
			fill("固件号:  " + flyme1);
			fill("对内型号:  " + device);
			fill("对外型号:  " + device1);
			fill("应用版本：  " + appVersion);
			fill("PC--IP： "+getIp());
			newline();

		} else if (evt.getActionCommand().equals(RECORD)) {
			String name = getTime();
			String str;
			str = JOptionPane.showInputDialog("请输入录制秒数:").trim();
			System.out.println(str);
			try {
				Integer.valueOf(str);
				JOptionPane.showMessageDialog(null,
						"已经开始录制了，看什么看，还不赶紧操作  录制时间：" + str + "s");
				exec("adb shell screenrecord  --time-limit " + str
						+ " /sdcard/" + name + ".mp4");
				exec("adb pull /sdcard/" + name + ".mp4 " + currentPath + "");
				fill("视频拿出来了，在当前目录看看拉  " + name + ".mp4");
				newline();
			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, "已经没办法帮你录制了");
			}

		} else if (evt.getActionCommand().equals(INSTALL)) {
			JOptionPane.showMessageDialog(null, "直接把应用拖进来就可以了亲，给好评哦");

		} else if (evt.getActionCommand().equals(UNINSTALL)) {
			// showP();
			String commands = "pm uninstall -k " + app + "";
			ShellUtils.execCommand(commands, false);
			fill("当前版本：  " + getAppVersion());
			// dialog.setVisible(false);
			newline();

		} else if (evt.getActionCommand().equals(PUSHLOCAL)) {
			String appVersion = getAppVersion();
			String url;
			String commands;
			if (app.contains("browser")) {
				url = JOptionPane.showInputDialog("请输入推送的url:");
				if (pushCheck(appVersion.trim())) {//4.2以上版本
					commands = "am broadcast -a com.meizu.flyme.push.intent.MESSAGE -p \""
							+ app
							+ "\" --es message \"{'tp':1,'fb': {'c': 'test','u': '"
							+ url + "','t': 'test'}}\"";
				} else {
					commands = "am broadcast -a com.meizu.flyme.push.intent.MESSAGE -p \""
							+ app
							+ "\" --es message \"{'notice':{'content':'content','id':174,'layout':'ACTIVITY_DETAIL','subject':'subject','url':'"
							+ url + "'}}\"";
				}
				ShellUtils.execCommand(commands, false);
			} else {
				String json = JOptionPane.showInputDialog("请输入推送的json:");
				String commandOtherApp = "am broadcast -a com.meizu.flyme.push.intent.MESSAGE -p \""
						+ app + "\" --es message \"" + json + "\"";
				ShellUtils.execCommand(commandOtherApp, false);
			}

			exec("adb shell input swipe 500 0 500 1600");
			/*
			 * JOptionPane.showMessageDialog(null, "看下有没有，没有请去平台推送吧");
			 * 
			 * 
			 * exec("adb shell pm clear com.meizu.cloud"); String
			 * imei=exec("adb shell getprop ril.gsm.imei");
			 * if(imei.length()>4&&imei!=null&&!imei.contains("adb")){ String[]
			 * v=imei.split(","); if(v.length>1){ imei=v[0]; } Map<String,
			 * String> map =
			 * HttpRequest.get("http://172.17.49.37/deviceStatus?uid="+imei+"");
			 * fill("检查测试环境IMEI号是否在线： "+imei);
			 * fill("返回结果： "+map.get("Response")); }
			 * 
			 * newline();
			 */
		} else if (evt.getActionCommand().equals(MTKSTART)) {
			exec("adb shell am start -n com.mediatek.mtklogger/.MainActivity");
			sleep(200);
			CommandResult result = ShellUtils.execCommand(
					"dumpsys activity top | grep ACTIVITY", false);
			String respon = result.successMsg;
			if (respon.contains("mtklogger")) {
				fill("启动 MTL LOG 完毕");
			} else {
				fill("无法启动，请手动启动 ----拨号盘输入*#*#3646633#*#*");
			}
			newline();

		} else if (evt.getActionCommand().equals(GETLOG)) {
			// String[] commands={"rm -rf /sdcard/mtklog/mobilelog/"};
			String[] commands = { "cd /sdcard/mtklog/mobilelog/", "ls -l" };
			CommandResult result = ShellUtils.execCommand(commands, false);
			System.out.println(result.successMsg);
			System.out.println(result.successMsg.split("\n").length);
			fill("-----Log 包------");
			List<String> data = new LinkedList<String>();
			for (String a : result.successMsg.split("\n")) {
				if (a.contains("drw")) {
					fill(a);
					data.add(a);
				}
			}
			if (data.size() > 0) {
				String last = data.get(data.size() - 1);
				int APLog = last.indexOf("APLog");
				if (APLog > 0) {
					String lastLog = last.substring(APLog, last.length());
					fill("拉最新的到电脑：  " + lastLog);
					System.out.println(currentPath);
					
					exec("adb pull /sdcard/mtklog/mobilelog/" + lastLog + " "
							+ currentPath + "\\" + lastLog + "");

				} else {
					fill("没找到APLog 先不抓了  ");
				}
			} else {
				fill("没找到log /sdcard/mtklog/mobilelog/");
			}

			newline();

		} else if (evt.getActionCommand().equals(PROXY)) {
			exec("adb push " + currentPath + "\\resource\\adb.jar /sdcard/");
			System.out.println("adb push " + currentPath
					+ "\\src\\resource\\adb.jar /sdcard/");
			exec("adb shell uiautomator runtest /sdcard/adb.jar --nohup -c testcase.adbSupport#proxySet");
			fill("设置完了");
			newline();
			
		} else if (evt.getActionCommand().equals(STARTTEST)) {
			JOptionPane.showMessageDialog(null, "启动时间测试开始，冷热启动各5次\n 需要20S，确定开始");
			int runTime=5;
			 fill("------启动时间测试开始，冷热启动各5次--------");
			 fill("------冷启动--------");
			int start1=0;
			int startTime=0;
			for (int i = 0; i < runTime; i++) {			
				this.sleep(1000);
				ShellUtils.execCommand("am force-stop "+app, false);
				this.sleep(2000);
				startTime =getPerform.getStartTime(app, activity);
				start1+=startTime;
				fill(startTime+"");
			}
			fill("avg: "+start1/runTime);
			 start1=0;
			 startTime=0;
			fill("-----热启动--------");
			//热启动时间
			for (int i = 0; i < runTime; i++) {
				this.sleep(1000);
				//ShellUtil.clearCache(app);				
				ShellUtils.execCommand("input keyevent 3", false);
				this.sleep(2000);
				startTime =getPerform.getStartTime(app, activity);
				start1+=startTime;
				fill(startTime+"");
			}
			fill("avg: "+start1/runTime);
			newline();
			
		} else if (evt.getActionCommand().equals(FLOWTEST)) {
			fill("-----获取耗流  单位：KB-----");
			if(!getCurrentActivity().contains(app))
			exec("adb shell am start -n " + app + "/" + activity + "");
			Long[] data=getPerform.getFlow(app);
			long beforeFlowSnd=data[2]/1024;
			long beforeFlowRev=data[1]/1024;			
			fill("发送: "+beforeFlowSnd+"        相差   "+(beforeFlowSnd-this.beforeFlowSnd));
			fill("接收: "+beforeFlowRev+"        相差   "+(beforeFlowRev-this.beforeFlowRev));
			fill("总: "+(beforeFlowSnd+beforeFlowRev)+"        相差   "+(beforeFlowSnd+beforeFlowRev-this.beforeFlowSnd-this.beforeFlowRev));
			newline();
			this.beforeFlowRev=beforeFlowRev;
			this.beforeFlowSnd=beforeFlowSnd;
			
		} else if (evt.getActionCommand().equals(GETMEMORY)) {
			fill("-----获取内存信息-----");
			if(!getCurrentActivity().contains(app))
			exec("adb shell am start -n " + app + "/" + activity + "");
			String[] data=getPerform.getMemory(app);
			fill("Total: "+data[0]);
			fill("Dalvik_Heap: "+data[1]);
			fill("Native_Heap: "+data[2]);
			fill("Views: "+data[3]);
			fill("Activites: "+data[4]);
			newline();
			
		}else if (evt.getActionCommand().equals(MEMORYTEST)) {
			JOptionPane.showMessageDialog(null, "需要20S\n 【验证1】：开启应用，滑动listview，退出到桌面，查看Views和Activites是否为0\n 【验证2】：杀掉应用，查看应用是否被杀死\n确定开始");
			int runTime=5;
			fill("-----内存测试-----");
			fill("【验证1】：开启应用，滑动listview，退出到桌面，查看Views和Activites是否为0");
			ShellUtils.execCommand("am force-stop "+app, false);
			exec("adb shell am start -n " + app + "/" + activity + "");
			for (int i = 0; i < runTime; i++) {
				this.sleep(500);
				exec("adb shell input swipe 500 1000 500 0");
			}
			for (int i = 0; i < runTime; i++) {
				exec("adb shell input keyevent 4");
			}
			this.sleep(5000);			
			try {
				String[] data=getPerform.getMemory(app);
				fill("  Total: "+data[0]+"  Dalvik_Heap: "+data[1]+"  Native_Heap: "+data[2]+"  Views: "+data[3]+"  Activites: "+data[4]);
				if(Integer.valueOf(data[3])+Integer.valueOf(data[4])==0){
					 fill("【passed】 Views和Activites为0");
				}else{
					fill("【failed】 Views或Activites不为0");
				}
			} catch (Exception e) {
				fill("【failed】  Views或Activites不为0");
			}
			
			fill("【验证2】：杀掉应用，查看应用是否被杀死");
			ShellUtils.execCommand("am force-stop "+app, false);
			this.sleep(1000);
			String result = exec("adb shell dumpsys meminfo " + app);
			if(result.contains("No process found for:")){
				 fill("【passed】 应用被杀死");
			}else{
				fill("【failed】 应用没被杀死");
			}
			newline();
		}else if (evt.getActionCommand().equals(SCREENTEST)) {
			String checkData="Disabling non-boot CPUs";
			JOptionPane.showMessageDialog(null, "熄屏检查只是去获取你的最新mtklog \n查看是否存在 “"+checkData+"”\n 确定开始");
			fill("-----熄屏检查开始------");
			String[] commands = { "cd /sdcard/mtklog/mobilelog/", "ls -l" };
			CommandResult result = ShellUtils.execCommand(commands, false);
			List<String> data = new LinkedList<String>();
			for (String a : result.successMsg.split("\n")) {
				if (a.contains("drw")) {
					data.add(a);
				}
			}
			if (data.size() > 0) {
				String last = data.get(data.size() - 1);
				int APLog = last.indexOf("APLog");
				if (APLog > 0) {
					String lastLog = last.substring(APLog, last.length());
					fill("拉最新的到电脑：  " + lastLog);					
					exec("adb pull /sdcard/mtklog/mobilelog/" + lastLog + " "
							+ currentPath + "\\" + lastLog + "");					
					boolean getData=FileUtil.readTxtFile(currentPath + "\\" + lastLog +"\\kernel_log",checkData);	
					fill("检查log  "+currentPath + "\\" + lastLog +"\\kernel_log");
					if(getData){
						fill("【passed】 log中找到 "+checkData+"");
					}else{
						fill("【faileded】 log中找不到 "+checkData+"");
					}
				} else {
					fill("找不到最新的 MTK-APLog  ");
				}
			} else {
				fill("没找到log /sdcard/mtklog/mobilelog/");
			}
			newline();			
		}else if (evt.getActionCommand().equals(RUNMONKEY)) {
	        fill("未实现");
			newline();
			
		}else if (evt.getActionCommand().equals(GETMONKEYLOG)) {
			 fill("未实现");
			 newline();
			
		}else if (evt.getActionCommand().equals(INWEBWIFI)) {
			 fill("未实现");
			 newline();
			
		}else if (evt.getActionCommand().equals(ADDBUTTON)) {	 
			 if (dialog == null) {
		            dialog = new DialogShow(this, "输入对话框");
		        }
		        dialog.setVisible(true);
			 newline();			
		}else if (evt.getActionCommand().endsWith(CUSTOMFLAG)) {//所有自定义按钮
			fill("--------------开始执行自定义命令---------------");
			String buttonName=evt.getActionCommand().replace(CUSTOMFLAG, "");
			System.out.println(buttonName);
			String command=customButtons.get(buttonName.replace(CUSTOMFLAG, "")).trim();
			System.out.println(command);
			if(buttonName.contains("@window")){
				fill(exec(command));
			}else{
				CommandResult result = ShellUtils.execCommand(command, false);
				fill(result.successMsg);
			}
			newline();			
		}

	} 
	
	 DialogShow dialog;
	
	public void removeButton(String buttonName, JPanel j) {
		j.remove(buttons.get(buttonName));
		j.repaint();
		j.updateUI();
	}

	public void addButton(String buttonName, String command, JPanel j) {
		JButton button = new JButton(buttonName);
		button.addActionListener(this);
		button.setActionCommand(buttonName+CUSTOMFLAG);
		j.add(button);
		buttons.put(buttonName,button);
		customButtons.put(buttonName,command);
		j.repaint();
		j.updateUI();
	}

	static long beforeFlowRev=0;
	static long beforeFlowSnd=0;
	
	 public boolean isWifiConnected()
	  {
	    String result = getProp("dhcp.wlan0.result");
	    System.out.println(result);
	    return ((result != null) && ("ok".equals(result.trim())));
	  }

	public String getTime() {
		return String.valueOf(System.currentTimeMillis());
	}
	
	public String getCurrentActivity(){
		CommandResult result = ShellUtils.execCommand("dumpsys activity top | grep ACTIVITY", false);
		return result.successMsg;
	}

	public String getAppVersion() {
		String check="";
		try {
			String[] commands = { "dumpsys package " + Main.app + "" };
			CommandResult result = ShellUtils.execCommand(commands, false);
			int a = result.successMsg.indexOf("versionName=");
			check=result.successMsg.substring(a + 12, a + 23).trim();
		} catch (Exception e) {
			// TODO: handle exception
			check="无法找到应用版本信息";
		}		
		return check;
	}
	
	public String getIp(){
		InetAddress ia=null;
		String localip="localhost";
		try {
			ia=ia.getLocalHost();			
			//String localname=ia.getHostName();
			localip=ia.getHostAddress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localip;
	}

	public void drag()// 定义的拖拽方法
	{
		// panel表示要接受拖拽的控件
		new DropTarget(txtIng, DnDConstants.ACTION_COPY_OR_MOVE,
				new DropTargetAdapter() {
					@Override
					public void drop(DropTargetDropEvent dtde)// 重写适配器的drop方法
					{
						try {
							if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))// 如果拖入的文件格式受支持
							{
								dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);// 接收拖拽来的数据
								List<File> list = (List<File>) (dtde
										.getTransferable()
										.getTransferData(DataFlavor.javaFileListFlavor));
								String temp = "";
								for (File file : list)
									temp += file.getAbsolutePath() + ";\n";
								if (temp.contains(".apk")) {
									JOptionPane.showMessageDialog(null,
											"\n将会安装  " + temp + "  请稍等 【20S】");

									dtde.dropComplete(true);// 指示拖拽操作已完成

									txtIng.setCaretPosition(txtIng.getText()
											.length());
									// System.out.println("adb install -r "+temp.replace(";",
									// "")+"");
									exec("adb install -r "
											+ temp.replace(";", "") + "");
									fill("已安装完毕");
									fill("【当前版本】：  " + getAppVersion());
								} else if (temp.contains(".jar")) {
									JOptionPane.showMessageDialog(null,
											"\n运行jar  " + temp + "  请稍等");
									exec("adb push " + temp.replace(";", "")
											+ " /sdcard/");
									exec("");
									dtde.dropComplete(true);// 指示拖拽操作已完成

								} else {
									JOptionPane.showMessageDialog(null,
											"\n无操作指令for  " + temp);
									dtde.dropComplete(true);// 指示拖拽操作已完成
								}
							} else {
								dtde.rejectDrop();// 否则拒绝拖拽来的数据
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	public String getProp(String item) {
		return exec("adb shell getprop " + item + "");
	}

	// 4.2以下的返回true
	public boolean pushCheck(String appVerison) {
		boolean check = false;
		try {
			appVerison=appVerison.substring(0, 3);
			if(Double.valueOf(appVerison)>=4.2){
				check=true;
			}			
		} catch (Exception e) {
			// 报错默认是false
		}		
		return check;
	}

	// 检查adb
	public static String checkadb() throws IOException {
		String check = "";
		BufferedReader br = null;
		Process p = Runtime.getRuntime().exec("cmd /c adb devices");
		br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}

		if (!sb.toString().trim().equals("List of devices attached")
				&& !sb.toString().trim().contains("offline")
				&& !sb.toString().trim().contains("adb server is out of date")) {
			conect = true;
			check = "【adb已连接，为所欲为吧】  \n" + sb.toString().trim();
		} else {
			conect = false;
			check = "【无法连接adb,什么都不能操作！悲剧】\n" + sb.toString().trim();
		}
		return check;
	}

	public static int getDevices(String check) {
		ArrayList<String> devices = new ArrayList<String>();
		for (String aa : check.split("\n")) {
			if (aa.length() == 19 && aa.contains("device")) {
				devices.add(aa.substring(0, 12));
			}
		}
		return devices.size();
	}

	public static String exec(String command) {
		Process p = null;
		String result = "";
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream(), "GBK"));	
			String s="";
			 while ((s=br.readLine() )!= null) {
				 result +=s+"\n";
             }			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (p != null) {
				p.destroy();
			}
		}
		return result;
	}

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String check = "";

	public static void main(String[] args) throws InterruptedException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException,
			FileNotFoundException, JSONException {

		Main a = new Main();
		a.run();
		
		String r=FileUtil.readTxtFile(FileUtil.jsonPath);
		System.out.println(r);
		Object[] datas=FileUtil.getJsonArrayValues(r,"buttons");
		int num=datas.length;
		for (int i = 0; i < num; i++) {
			a.addButton(FileUtil.getValue(datas[i].toString(), "name"),FileUtil.getValue(datas[i].toString(), "command"),a.commomOpertion);
		}
		
		fill("Design by 应用分发测试部");
		fill("运行包名：   " + app);
		fill("activity：  " + activity);
		fill("------------开始运行----------");
		try {
			deviceCheck();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * } });
		 */
	}
    //换行
	public void newline() {
		txtIng.setCaretPosition(txtIng.getText().length());
		txtIng.paintImmediately(txtIng.getBounds());
	}
    //显示值
	public static void fill(String value) {
		txtIng.append("\n" + value + "");
	}

	public static void deviceCheck() throws InterruptedException {
		Thread.sleep(1000);
		String checkAgain = "";
		// 检查adb
		while (true) {
			try {
				check = checkadb();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!checkAgain.equals(check)) {
				txtIng.append("\n 检查ADB过程中.....\n");
				txtIng.append(check);
				txtIng.setCaretPosition(txtIng.getText().length());
				txtIng.paintImmediately(txtIng.getBounds());
				checkAgain = check;
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
