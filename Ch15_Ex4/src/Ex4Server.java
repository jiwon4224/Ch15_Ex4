import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Ex4Server extends JFrame{
	private JTextArea textArea = new JTextArea();
	private Vector<String> wordVector = new Vector<String>(); 
	public Ex4Server(){
		super("영어 단어 체크 서버");
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
		Container c = getContentPane(); 
		c.add(textArea);
		textArea.setEnabled(false); 
		this.loadWord("words.txt"); 
		new ServerThread().start();
		
		setSize(500, 500); 
		setVisible(true);
	}
	public void loadWord(String fileName){
		FileInputStream fileInputStream = null;
		BufferedReader in = null;
		String words;
		try{
			fileInputStream = new FileInputStream(fileName); 
			in = new BufferedReader(new InputStreamReader(fileInputStream));
			while((words = in.readLine()) != null){
				wordVector.add(words);
				System.out.println(words);
			}
			textArea.append(fileName + " 읽기 완료\n");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	class ServerThread extends Thread{
		@Override
		public void run(){
			ServerSocket listener = null;
			Socket socket = null;
			try{
				listener = new ServerSocket (9999); 
				while (true){
					socket = listener.accept();
					textArea.append("클라이언트 연결됨\n");
					textArea.append("-----------------------------\n");
					new ServiceThread(socket).start();
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			try{
				if(listener != null) listener.close(); 
				if(socket != null) listener.close(); 
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class ServiceThread extends Thread{
		private BufferedReader in = null;
		private BufferedWriter out = null;
		private Socket socket;
		public ServiceThread(Socket socket) {
			this.socket = socket;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			String inputMessage, outputMessage;
			while(true) {
				try {
					inputMessage = in.readLine();
					if(wordVector.contains(inputMessage)) {
						outputMessage = "YES";
					}
					else {
						outputMessage="NO";
					}
					textArea.append(inputMessage+" = "+outputMessage+"\n");
					out.write(outputMessage+"\n");
					out.flush();
				}catch(Exception e) {
					e.printStackTrace();
					textArea.append("-----------------------------\n");
					textArea.append("연결종료\n");
					return;
				}
			}
		}
	}
	public static void main(String[]args) {
		new Ex4Server();
	}
}