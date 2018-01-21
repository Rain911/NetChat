/**
 * @author Rain 
 * @classname ServerFrame.java
 * @project NetChat
 * @package 
 * @describe 网络聊天室--服务端
 * @version 1.0
 * @date 2018年1月21日 下午11:06:51
*/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//服务器端聊天窗口类
public class ServerFrame  extends JFrame  implements ActionListener,HandleMsgCallBack
{
	
	//界面元素定义
	//1.顶部
    JLabel       lblServerPort  = new JLabel("服务器监听端口:");
	JTextField  tfServerPort = new JTextField("18888",5);
	JButton      btnStart = new JButton("启动服务器");
	
 
	//2.中间聊天记录
	List        lstLog   = new List();
	
		
	
	
	//3.右侧在线用户列表
	JLabel     lblUsers  = new JLabel("在线用户列表:");
	List        lstUser   = new List();
	
	
	//4.记录已经与服务器建立了连接的动态数组...
	ArrayList<Socket> lstSockets=new ArrayList<Socket>();
	
	
	
	public ServerFrame() {
		setSize(800,600);
		setTitle("我的聊天服务端V0.1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initFrame();
	}
	
	
	//初始化窗口
	private void initFrame()
	{
		 setLayout( new BorderLayout());  //设置整个界面采用边框布局
		 
		//1.顶部界面
		JPanel   pTop  = new JPanel();
		             pTop.setBackground(Color.cyan);
					 pTop.add(lblServerPort);
					 pTop.add(tfServerPort);
					 pTop.add(btnStart);
					 
									 
		this.add(pTop, BorderLayout.NORTH); //加入到界面的顶部
		
		//2.中间聊天记录
		this.add(lstLog, BorderLayout.CENTER); 
		
				
		 //3.右边在线人员列表
		  JPanel   pRight  = new JPanel();
		  pRight.setBackground(Color.pink);
		  pRight.setLayout(new BorderLayout());
		  
		  pRight.add(lblUsers,BorderLayout.NORTH);
		  pRight.add(lstUser);
		  this.add(pRight, BorderLayout.EAST); //加入到界面的右边

		  
		//设置事件的监听
		  btnStart.addActionListener(this);
		 
	 
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		 
		if(e.getSource()==btnStart){
		   System.out.println("启动服务器!");
		   //启动服务，不断地监听来自客户端的监听;
		   //启动独立的线程的对象
		   //直接继承一个Thread对象,---->匿名类;
		   new Thread()
		   {
			   public void run() {
				   try {
					   
					   int port =Integer.parseInt(tfServerPort.getText());
					   //
					   ServerSocket server=new ServerSocket(port);
					   
					   while(true)
					   {
						   lstLog.add("等待连接...");
						   Socket socket=server.accept();
						   
						   
						   lstLog.add("有连接进来，来自:"+socket.getInetAddress());
						   
						   
						   //将当前的连接的对象保存在动态的数组里
						   lstSockets.add(socket);
						   
						   //发送欢迎词给当前连接
						   new SendThread(socket, "欢迎进入聊天室!").start();
						   //启动该链接的接收线程;
						   new ReceiveThread(socket,ServerFrame.this).start();
						   
						   
						   
						   
						   
						   
						   
					   }
					
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			   };
		   }.start();
		   //启动成功
		   btnStart.setEnabled(false);
		   
			return;
		}
		
		 
		
	}
	//编写的一个广播信息的方法准备广播发送到各个客户端
	public void doBroadCastMsg(String msg)
	{
		
		//遍历当前所有的连接，调用SendThread进行发送
		for (Socket socket : lstSockets) {
			
			new SendThread(socket, msg).start();
		}
		
	}//end doBroadCastMsg
	
	
	
	
	public static void main(String[] args) {
		 new ServerFrame().setVisible(true);
	}


	//重写接口中的方法
	@Override
	public void handleMsg(String msg) {
		// TODO Auto-generated method stub
		//1.显示接收的消息
		lstLog.add("【接收到:】"+msg);
		
		//2.广播这个消息
		doBroadCastMsg(msg);
	}

}
