package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Server {
	
	public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public static void main(String[] args) {
		EventLoopGroup recieveGroup = new NioEventLoopGroup(1);
		EventLoopGroup handleGroup = new NioEventLoopGroup(2);
		try {
			ChannelFuture future = new ServerBootstrap().channel(NioServerSocketChannel.class)
															 .group(recieveGroup, handleGroup)
															 .childHandler(new ChannelInitializer<SocketChannel>() {
																@Override
																protected void initChannel(SocketChannel ch)
																		throws Exception {
																	ch.pipeline().addLast(new ServerChildHandler());
																}
																 
															})
															 .bind(8888)
															 .sync();
			System.out.println("Server started");
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			recieveGroup.shutdownGracefully();
			handleGroup.shutdownGracefully();
		}
	}
}

class ServerChildHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Server.clients.add(ctx.channel());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf)msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.getBytes(buf.readerIndex(), bytes);
		System.out.println(new String(bytes));
		Server.clients.writeAndFlush(msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
