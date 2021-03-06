package netty.chatroom;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Random;

public class ClientFrame extends Frame{
	private static final long serialVersionUID = 6497662201587286582L;
	
	TextArea area = new TextArea();
	TextField field = new TextField();
	
	public ClientFrame() {
		this.setSize(600,400);
		this.setLocation(100, 20);
		area.setEditable(false);
		this.add(area, BorderLayout.CENTER);
		this.add(field, BorderLayout.SOUTH);
		
		ChatroomClient client = new ChatroomClient();
		new Thread(() -> {client.initialize(area);}).start();
		
		ChatMessage msg = new ChatMessage("", "", "Guest" + new Random().nextInt(1000));
		
		field.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				msg.setSendDate(LocalTime.now().toString());
				msg.setMessage(field.getText());
				client.sendMessage(msg);
				field.setText("");
			}
		});
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new ClientFrame();
	}
}
