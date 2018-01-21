/**
 * @author Rain 
 * @classname ServerFrame.java
 * @project NetChat
 * @package 
 * @describe ����������--�����
 * @version 1.0
 * @date 2018��1��21�� ����11:06:51
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

//�����������촰����
public class ServerFrame  extends JFrame  implements ActionListener,HandleMsgCallBack
{
	
	//����Ԫ�ض���
	//1.����
    JLabel       lblServerPort  = new JLabel("�����������˿�:");
	JTextField  tfServerPort = new JTextField("18888",5);
	JButton      btnStart = new JButton("����������");
	
 
	//2.�м������¼
	List        lstLog   = new List();
	
		
	
	
	//3.�Ҳ������û��б�
	JLabel     lblUsers  = new JLabel("�����û��б�:");
	List        lstUser   = new List();
	
	
	//4.��¼�Ѿ�����������������ӵĶ�̬����...
	ArrayList<Socket> lstSockets=new ArrayList<Socket>();
	
	
	
	public ServerFrame() {
		setSize(800,600);
		setTitle("�ҵ���������V0.1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initFrame();
	}
	
	
	//��ʼ������
	private void initFrame()
	{
		 setLayout( new BorderLayout());  //��������������ñ߿򲼾�
		 
		//1.��������
		JPanel   pTop  = new JPanel();
		             pTop.setBackground(Color.cyan);
					 pTop.add(lblServerPort);
					 pTop.add(tfServerPort);
					 pTop.add(btnStart);
					 
									 
		this.add(pTop, BorderLayout.NORTH); //���뵽����Ķ���
		
		//2.�м������¼
		this.add(lstLog, BorderLayout.CENTER); 
		
				
		 //3.�ұ�������Ա�б�
		  JPanel   pRight  = new JPanel();
		  pRight.setBackground(Color.pink);
		  pRight.setLayout(new BorderLayout());
		  
		  pRight.add(lblUsers,BorderLayout.NORTH);
		  pRight.add(lstUser);
		  this.add(pRight, BorderLayout.EAST); //���뵽������ұ�

		  
		//�����¼��ļ���
		  btnStart.addActionListener(this);
		 
	 
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		 
		if(e.getSource()==btnStart){
		   System.out.println("����������!");
		   //�������񣬲��ϵؼ������Կͻ��˵ļ���;
		   //�����������̵߳Ķ���
		   //ֱ�Ӽ̳�һ��Thread����,---->������;
		   new Thread()
		   {
			   public void run() {
				   try {
					   
					   int port =Integer.parseInt(tfServerPort.getText());
					   //
					   ServerSocket server=new ServerSocket(port);
					   
					   while(true)
					   {
						   lstLog.add("�ȴ�����...");
						   Socket socket=server.accept();
						   
						   
						   lstLog.add("�����ӽ���������:"+socket.getInetAddress());
						   
						   
						   //����ǰ�����ӵĶ��󱣴��ڶ�̬��������
						   lstSockets.add(socket);
						   
						   //���ͻ�ӭ�ʸ���ǰ����
						   new SendThread(socket, "��ӭ����������!").start();
						   //���������ӵĽ����߳�;
						   new ReceiveThread(socket,ServerFrame.this).start();
						   
						   
						   
						   
						   
						   
						   
					   }
					
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			   };
		   }.start();
		   //�����ɹ�
		   btnStart.setEnabled(false);
		   
			return;
		}
		
		 
		
	}
	//��д��һ���㲥��Ϣ�ķ���׼���㲥���͵������ͻ���
	public void doBroadCastMsg(String msg)
	{
		
		//������ǰ���е����ӣ�����SendThread���з���
		for (Socket socket : lstSockets) {
			
			new SendThread(socket, msg).start();
		}
		
	}//end doBroadCastMsg
	
	
	
	
	public static void main(String[] args) {
		 new ServerFrame().setVisible(true);
	}


	//��д�ӿ��еķ���
	@Override
	public void handleMsg(String msg) {
		// TODO Auto-generated method stub
		//1.��ʾ���յ���Ϣ
		lstLog.add("�����յ�:��"+msg);
		
		//2.�㲥�����Ϣ
		doBroadCastMsg(msg);
	}

}
