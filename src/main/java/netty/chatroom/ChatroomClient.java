package netty.chatroom;

import java.awt.TextArea;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
															ch.pipeline().addLast(handler);
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
	
	public void sendMessage(String message) {
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
		ByteBuf buf = (ByteBuf)msg;
		byte[] destination = new byte[buf.readableBytes()];
		buf.getBytes(buf.readerIndex(), destination);
		String message = new String(destination);
		area.setText(area.getText() + "\n" + message);
		
	}
	
	public void sendMessage(String message) {
		ByteBuf buf = Unpooled.copiedBuffer(message.getBytes());
		ctx.writeAndFlush(buf);
	}
}