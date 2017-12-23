package nikita;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class KKMultiServer extends JFrame {
	
	private JTextField tfPort = null;
	private JTextField tfCount = null;
	private JTextArea tvInfo = null;
	private JLabel ans = null;
	
	public KKMultiServer() {
		super("BigServer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		
		JLabel labelPort = new JLabel("Введите порт: ");
		tfPort = new JTextField(20);
		
		JLabel labelCount = new JLabel("Введите число машин: ");
		tfCount = new JTextField(20);
		JLabel labelInfo = new JLabel("Введите числа для умножения");
		tvInfo = new JTextArea(20,20);
		//tvInfo.setSize(200, 100);
		ans = new JLabel("Поле для ответа...");
		
		JButton button = new JButton("Начать прослушивание порта");
		button.addActionListener(new ButtonPress());
	
		add(labelPort);
		add(tfPort);
		add(labelCount);
		add(tfCount);
		add(button);
		add(labelInfo);
		add(tvInfo);
		add(ans);
		setSize(300, 600);
		setVisible(true);
	}
	
	class ButtonPress implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String text = tvInfo.getText();
			String[] masText = text.split("\n");
			int port = Integer.parseInt(tfPort.getText().trim());
			int mach = Integer.parseInt(tfCount.getText().trim());
			new MyJob(port, mach, masText).start();
		}
	}
	
	class MyJob extends Thread
	{
		private int port = 0;
		private int countOfMachine;
		private String[] masOfCount;
		public MyJob(int p,int mach,String[] mas) {
			this.port = p;
			this.countOfMachine = mach;
			this.masOfCount = mas.clone();
		}
		@Override
		public void run() {
			ArrayList<String> mylist = new ArrayList<String>();
			for (int i=0;i<masOfCount.length;i++)
			{
				mylist.add(masOfCount[i]);
			}
			
			System.out.println("List of files determine");
			MyData data = null;
			try {
				data = new MyData(mylist, this.countOfMachine);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			ServerSocket serverSocket = null;
			boolean listening = true;

			try {
				serverSocket = new ServerSocket(port);
				System.out.println("Listen port:  "  + port);
			} catch (IOException e) {
				System.err.println("Could not listen on port: " + port);
				System.exit(-1);
			}

			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					tvInfo.setText(tvInfo.getText() + "\nСлушаю порт...\n");
				}
			});

			while (listening)
				try {
					new KKMultiServerThread(serverSocket.accept(),data).start();
				} catch (IOException e) {
					e.printStackTrace();
				}

			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	class MyData 
	{
		private ArrayList<String> list = new ArrayList<String>();
		private int countOfMachine;
		public int countForCLear;

		public MyData(ArrayList<String> list1,int countOfPC) throws IOException {
			this.list = (ArrayList<String>) list1.clone();
			this.countOfMachine = countOfPC;
			this.countForCLear = 0;
			}
		
		public String[] readCurrentFile() throws IOException
		{
			String[] strMas = new String[2];
			if (this.list.size()>=2)
			{
				strMas[0] = this.list.get(0);
				this.list.remove(0);
				
				strMas[1] = this.list.get(0);
				this.list.remove(0);
				return strMas;
			}
			else 
			{
				return null;
			}
		}
		
		public void writeCurrentFile(String newNumber) throws IOException
		{
			this.list.add(0,newNumber);
		}

		
		public int getCountOfMachine()
		{
			return this.countOfMachine;
		}

		public String getAnswer()
		{
			return this.list.get(0);
		}
		
	}
	
	
	class KKMultiServerThread extends Thread {
	    private Socket socket = null;
	    private MyData data = null;
	    
	    public KKMultiServerThread(Socket socket,MyData dat) {
	    super("KKMultiServerThread");
	    this.socket = socket;
	    this.data = dat;
	    System.out.println("Thread created");
	    }
	 
	    public void run() {
	 
	    	OutputStream os=null;
	    	try {
				os = this.socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			InputStream is=null;
			try {
				is = this.socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			boolean flag = true;
			System.out.println("Reader и Writer created");

			while(flag)
			{	
				try 
				{
					if (flag)
					{
						String[] buffer;

						synchronized (data) {
							buffer = data.readCurrentFile();
						}
						if (buffer!=null)
						{
							os.write(buffer[0].length());
							os.write(buffer[0].getBytes());
							
							os.write(buffer[1].length());
							os.write(buffer[1].getBytes());
						}
						else 
						{
							flag = false;
						}
						System.out.println("seding data...");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (flag)
				{
					String b = "";
					int i = 0; 
					try {		
					i=is.read();
					byte[] buf = new byte[i];
					is.read(buf);
					
					synchronized (data) {
						if (i!=0)
						data.writeCurrentFile(new String(buf));
						System.out.println("receiving data...");
					}
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
			}
				
			synchronized (data) {
				System.out.println(data.getAnswer());
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ans.setText("Ответ: " + data.getAnswer());
						
					}
				});
			}
			
			try {
				os.write(0);
				if (is!=null) is.close();
				if (os!=null) os.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Work performed!");
	    }
	    
	}
	
	
	public static void main(String[] args) throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new KKMultiServer();	
			}
		});
	}
}

