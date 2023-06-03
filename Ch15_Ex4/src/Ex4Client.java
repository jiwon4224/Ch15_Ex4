import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Ex4Client extends JFrame{
	private JLabel label = new JLabel("단어 입력 >>");
	private JTextField textField = new JTextField(10);
	private JLabel resultLabel = new JLabel();
	private Socket socket = null;
	private BufferedReader in= null;
	private BufferedWriter out = null;
	
	public Ex4Client() {
		super("클라이언트");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(label);
		c.add(textField);
		c.add(resultLabel);
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField t = (JTextField)e.getSource();
				try {
					out.write(t.getText()+"\n");
					out.flush();
					resultLabel.setText(getName());
					t.setText("");
				}catch (Exception x) {
					x.printStackTrace();
				}
			}
		});
		setSize(400,100);
		setVisible(true);
		this.play();
	}
	public void play() {
		try {
		socket = new Socket("localhost",9999);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		while(true) {
			String inputMessage = in.readLine();
	
resultLabel.setText(inputMessage);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(socket !=null) socket.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Ex4Client();
	}
}