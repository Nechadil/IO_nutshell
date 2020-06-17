package nio.singleThreadModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
	public static void connect() throws IOException {
		SocketChannel channel = SocketChannel.open();
		channel.connect(new InetSocketAddress("127.0.0.1", 8888));
		ByteBuffer byteBuffer = ByteBuffer.wrap("Hi".getBytes());
		channel.write(byteBuffer);
		channel.close();
	}
	
	public static void main(String[] args) {
		try {
			NIOClient.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
