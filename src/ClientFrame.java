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
import java.io.Writer;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Rain 
 * @classname ClientFrame.java
 * @project NetChat
 * @package 
* @describe 网络聊天室--客户端 
* @version 1.0
* @date 2018年1月21日 下午11:02:57
*/
//客户端聊天窗口类
public class ClientFrame  extends JFrame  implements ActionListener,HandleMsgCallBack
{
	
	//界面元素定义
	//1.顶部
	JLabel       lblServerIP  = new JLabel("服务器IP:");
	JTextField  tfServerIP   = new JTextField("127.0.0.1",10);
	JLabel       lblServerPort  = new JLabel("服务器端口:");
	JTextField  tfServerPort = new JTextField("18888",5);
	JButton      btnConnect  = new JButton("连接服务器");
	
	JLabel       lblNickName  = new JLabel("昵称:");
	JTextField  tfNickName = new JTextField(10);
	JButton      btnEnter  = new JButton("进入聊天室");
	
	
	Socket socket=null;//当前连接的Socket对象
	//2.中间聊天记录
	List        lstLog   = new List();
	
	//3.底部的聊天发送
	JTextField  tfMsg = new JTextField(30);
	JButton      btnSend = new JButton("发送");
	
	//4.右侧在线用户列表
	JLabel     lblUsers  = new JLabel("在线用户列表:");
	List       lstUser   = new List();
	
	public ClientFrame() {
		setSize(800,600);
		setTitle("我的聊天客户端V0.1");
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
					 pTop.add(lblServerIP);
					 pTop.add(tfServerIP);
					 pTop.add(lblServerPort);
					 pTop.add(tfServerPort);
					 pTop.add(btnConnect);
					 
					 pTop.add(lblNickName);
					 pTop.add(tfNickName);
					 pTop.add(btnEnter);
					 
		this.add(pTop, BorderLayout.NORTH); //加入到界面的顶部
		
		//2.中间聊天记录
		this.add(lstLog, BorderLayout.CENTER); 
		
		//3.底部发送信息
		JPanel   pBottom  = new JPanel();
					pBottom.setBackground(Color.gray);
					pBottom.add(tfMsg);
					pBottom.add(btnSend);
		  this.add(pBottom, BorderLayout.SOUTH); //加入到界面的底部
		
		 //4.右边在线人员列表
		  JPanel   pRight  = new JPanel();
		  pRight.setBackground(Color.pink);
		  pRight.setLayout(new BorderLayout());
		  
		  pRight.add(lblUsers,BorderLayout.NORTH);
		  pRight.add(lstUser);
		  this.add(pRight, BorderLayout.EAST); //加入到界面的右边

		  
		//设置事件的监听
		btnConnect.addActionListener(this);
		btnSend.addActionListener(this);
	 
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		 
		
		
		
		if(e.getSource()==btnConnect){
		   System.out.println("连接服务器!");
		   //创建一个Socket对象,绑定服务器的ip 和端口;
		   String serverip=tfServerIP.getText();
		   int serverport=Integer.parseInt(tfServerPort.getText());
		   try {
			    socket=new Socket(serverip,serverport);
			   lstLog.add("连接服务器成功...");
			   //连接成功后,设置连接按钮为不可用;
			   btnConnect.setEnabled(false);
			   
			   //启动接收信息的线程;
			   new ReceiveThread(socket, ClientFrame.this).start();
			   new SendThread(socket, "发送信息").start();
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
		  
			return;
		}
		
		if(e.getSource()==btnSend)
		{
			System.out.println("发送信息。。");
			//1.获取用户输入的内容
			String msg=tfMsg.getText();
			if(msg=="")return ;
			new SendThread(socket,msg).start();
			
			
			
		}
		
	}
	//定义接收网络的线程ReceiveThread(作为模块使用)
	
	
	
	//创建一个客户端发送信息线程SendThread（新建的类作为模块使用）
	
	
	public static void main(String[] args) {
		 new ClientFrame().setVisible(true);
//		 try {
//			 
//			Socket socket=new Socket("127.0.0.1",18888);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		 
	}


	@Override
	public void handleMsg(String msg) {
		// TODO Auto-generated method stub
		//1.显示接收的消息
				lstLog.add("【接收到:】"+msg);
		
	}

}
