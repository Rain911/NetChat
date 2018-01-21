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
* @describe ����������--�ͻ��� 
* @version 1.0
* @date 2018��1��21�� ����11:02:57
*/
//�ͻ������촰����
public class ClientFrame  extends JFrame  implements ActionListener,HandleMsgCallBack
{
	
	//����Ԫ�ض���
	//1.����
	JLabel       lblServerIP  = new JLabel("������IP:");
	JTextField  tfServerIP   = new JTextField("127.0.0.1",10);
	JLabel       lblServerPort  = new JLabel("�������˿�:");
	JTextField  tfServerPort = new JTextField("18888",5);
	JButton      btnConnect  = new JButton("���ӷ�����");
	
	JLabel       lblNickName  = new JLabel("�ǳ�:");
	JTextField  tfNickName = new JTextField(10);
	JButton      btnEnter  = new JButton("����������");
	
	
	Socket socket=null;//��ǰ���ӵ�Socket����
	//2.�м������¼
	List        lstLog   = new List();
	
	//3.�ײ������췢��
	JTextField  tfMsg = new JTextField(30);
	JButton      btnSend = new JButton("����");
	
	//4.�Ҳ������û��б�
	JLabel     lblUsers  = new JLabel("�����û��б�:");
	List       lstUser   = new List();
	
	public ClientFrame() {
		setSize(800,600);
		setTitle("�ҵ�����ͻ���V0.1");
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
					 pTop.add(lblServerIP);
					 pTop.add(tfServerIP);
					 pTop.add(lblServerPort);
					 pTop.add(tfServerPort);
					 pTop.add(btnConnect);
					 
					 pTop.add(lblNickName);
					 pTop.add(tfNickName);
					 pTop.add(btnEnter);
					 
		this.add(pTop, BorderLayout.NORTH); //���뵽����Ķ���
		
		//2.�м������¼
		this.add(lstLog, BorderLayout.CENTER); 
		
		//3.�ײ�������Ϣ
		JPanel   pBottom  = new JPanel();
					pBottom.setBackground(Color.gray);
					pBottom.add(tfMsg);
					pBottom.add(btnSend);
		  this.add(pBottom, BorderLayout.SOUTH); //���뵽����ĵײ�
		
		 //4.�ұ�������Ա�б�
		  JPanel   pRight  = new JPanel();
		  pRight.setBackground(Color.pink);
		  pRight.setLayout(new BorderLayout());
		  
		  pRight.add(lblUsers,BorderLayout.NORTH);
		  pRight.add(lstUser);
		  this.add(pRight, BorderLayout.EAST); //���뵽������ұ�

		  
		//�����¼��ļ���
		btnConnect.addActionListener(this);
		btnSend.addActionListener(this);
	 
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		 
		
		
		
		if(e.getSource()==btnConnect){
		   System.out.println("���ӷ�����!");
		   //����һ��Socket����,�󶨷�������ip �Ͷ˿�;
		   String serverip=tfServerIP.getText();
		   int serverport=Integer.parseInt(tfServerPort.getText());
		   try {
			    socket=new Socket(serverip,serverport);
			   lstLog.add("���ӷ������ɹ�...");
			   //���ӳɹ���,�������Ӱ�ťΪ������;
			   btnConnect.setEnabled(false);
			   
			   //����������Ϣ���߳�;
			   new ReceiveThread(socket, ClientFrame.this).start();
			   new SendThread(socket, "������Ϣ").start();
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
		  
			return;
		}
		
		if(e.getSource()==btnSend)
		{
			System.out.println("������Ϣ����");
			//1.��ȡ�û����������
			String msg=tfMsg.getText();
			if(msg=="")return ;
			new SendThread(socket,msg).start();
			
			
			
		}
		
	}
	//�������������߳�ReceiveThread(��Ϊģ��ʹ��)
	
	
	
	//����һ���ͻ��˷�����Ϣ�߳�SendThread���½�������Ϊģ��ʹ�ã�
	
	
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
		//1.��ʾ���յ���Ϣ
				lstLog.add("�����յ�:��"+msg);
		
	}

}
