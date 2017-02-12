package nikita;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/*public class KKMultiServerThread extends Thread {
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
		System.out.println("Reader è Writer created");

			while(flag)
			{	
				try {
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
							else {
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
			
				synchronized (data) {
					data.countForCLear++;
					if (data.countForCLear==data.getCountOfMachine())
					
						System.out.println(data.getAnswer());
					
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
    
    
}*/