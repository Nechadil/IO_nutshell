package netty.chatroom;

import java.awt.TextArea;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatroomClient {
	private ClientChannelHandler handler;
	
	public void initialize(TextArea area) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			handler = new ClientChannelHandler(area);
			ChannelFuture future = new Bootstrap().group(group)
												  .channel(NioSocketChannel.class)
												  .handler(new ChannelInitializer<SocketChannel>() {
														@Override
														protected void initChannel(SocketChannel ch) throws Exception {
															ch.pipeline().addLast(new ChatMessageEncoder())
																		 .addLast(new ChatMessageDecoder())
															     		 .addLast(handler);
														}
														  
													})
												  .connect("localhost", 8888)
												  .sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public void sendMessage(ChatMessage message) {
		handler.sendMessage(message);
	}
}

class ClientChannelHandler extends ChannelInboundHandlerAdapter {
	private ChannelHandlerContext ctx;
	private TextArea area;
	
	public ClientChannelHandler(TextArea area) {
		this.area = area;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ChatMessage chatMessage = (ChatMessage)msg;
		area.setText(area.getText() + "\n" + chatMessage);
		
	}
	
	public void sendMessage(ChatMessage message) {
		ctx.writeAndFlush(message);
	}
}