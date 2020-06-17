package nio.singleThreadModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
	public static void startServer() throws IOException {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8888));
		serverSocketChannel.configureBlocking(false);
		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		System.out.println("Server started");
		
		while(true) {
			selector.select();
			
			System.out.println("Selector is awaken");
			
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = keys.iterator();
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				keyIterator.remove();
				handle(key);
			}
		}
	}

	private static void handle(SelectionKey key) throws IOException {
		if(key.isAcceptable()) {
			ServerSocketChannel channel = (ServerSocketChannel) key.channel();
			SocketChannel socketChannel = channel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(key.selector(), SelectionKey.OP_READ);
		} else if(key.isReadable()) {
			SocketChannel channel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(512);
			buffer.clear();
			int len = channel.read(buffer);
			if(len == -1) {
				channel.close();
				System.out.println("End of request. Channel is closed");
				return;
			}
			System.out.println(new String(buffer.array(), 0, len));
		}
	}
	
	public static void main(String[] args) {
		try {
			NIOServer.startServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
