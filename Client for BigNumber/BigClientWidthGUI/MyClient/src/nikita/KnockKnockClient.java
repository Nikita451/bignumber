package nikita;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class KnockKnockClient extends JFrame{

	JTextField tfForSocket;
	JTextField tfForPort;
	JTextArea ta;
	
	public KnockKnockClient() {
		super("BigNumber");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JLabel labelForSocket = new JLabel("Введите имя хоста");
		tfForSocket = new JTextField(20);
		JLabel labelForPort = new JLabel("Введите номер порта");
		tfForPort = new JTextField(4);
		JButton button = new JButton("Подключиться");
		button.addActionListener(new ButtonAction());
		String text= "Тут будут появляться результаты работы\n";
		ta = new JTextArea(text);
		add(labelForSocket);
		add(tfForSocket);
		add(labelForPort);
		add(tfForPort);
		add(button);
		add(ta);
		setSize(300, 200);
		setVisible(true);
	}
	
	class ButtonAction implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e1) {
			String myserver ="";
			myserver = tfForSocket.getText().trim(); // notebook_asus
	    	int port = Integer.parseInt(tfForPort.getText().trim());
	      	new MainJob(myserver, port).start();			
		}
			
	}
	
	class MainJob extends Thread
	{
		String myserver ="";
		int port = 0;
		public MainJob(String s,int p) {
			this.myserver = s;
			this.port = p;
		}
		@Override
		public void run() {
			
			Socket kkSocket = null;
		       
	        InputStream is = null;
	        OutputStream os = null;
	        
	        SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ta.setText(ta.getText() + "Соединение...\n");
				}
			});
	        
	        
	        try {
	            kkSocket = new Socket(myserver, port);
				
	            is=kkSocket.getInputStream();
	            os=kkSocket.getOutputStream();
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host: " + myserver);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to: " + myserver);
	            System.exit(1);
	        }
	        
	        	SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					ta.setText(ta.getText() + "Соединение установлено!\n");
				}
			});
	
	        
	        BufferedInputStream bis = new BufferedInputStream(is);
	        
	      	while (true)
	      	{
	    	  	int len = 0;
				try {
					len = bis.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	  	if (len==0) break;
	    	  	byte[] buffer = new byte[len];
	    	  	try {
					bis.read(buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	  	String n1 = new String(buffer);
	    	  
	    	  	try {
					len = bis.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	  	buffer = new byte[len];
	    	  	try {
					bis.read(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	  	String n2 = new String(buffer);
	    	  
	    	  	final BigNumber number1 = new BigNumber(n1);
	    	  	final BigNumber number2 = new BigNumber(n2);
	    	  
		        SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
				    	  ta.setText(ta.getText() + "Получили число "+ number1 +"\n");
				    	  ta.setText(ta.getText() + "Получили число"+ number2 +"\n");
					}
				});
	    	  
	    	  
		      	final BigNumber numberRes = number1.multiply(number2);
	    	  
	    	  	try {
					os.write(numberRes.toString().length());
					os.write(numberRes.toString().getBytes());
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ta.setText(ta.getText() + "Отправили число"+ numberRes +"\n");
					}
				});
	      	}
		
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ta.setText(ta.getText() + "Работа завершена!...\n");
				}
			});

	        try {
				bis.close();
				is.close();
		        os.close();
		        kkSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
    public static void main(String[] args) throws Exception {
    	
    	SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new KnockKnockClient();
			}
		});
    	
    }
    
}

