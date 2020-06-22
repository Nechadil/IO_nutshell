package netty.chatroom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatroomServer {
    static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	public void run() {
		NioEventLoopGroup reciever = new NioEventLoopGroup(1);
		NioEventLoopGroup processor = new NioEventLoopGroup(2);
		
		ChannelFuture future;
		try {
			future = new ServerBootstrap().channel(NioServerSocketChannel.class)
										  .group(reciever, processor)
										  .childHandler(new ChannelInitializer<SocketChannel>() {
												@Override
												protected void initChannel(SocketChannel ch) throws Exception {
													ch.pipeline().addLast(new ServerHandler());
												}
										  	})
										  .bind(8888)
										  .sync();
			System.out.println("Chatroom server started");
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			reciever.shutdownGracefully();
			processor.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		ChatroomServer server = new ChatroomServer();
		server.run();
	}
	
}

class ServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ChatroomServer.clients.add(ctx.channel());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ChatroomServer.clients.writeAndFlush(msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ChatroomServer.clients.remove(ctx.channel());
		ctx.close();
		System.out.println("Client disconnected. Resources are released. Current channel number: " + ChatroomServer.clients.size());
	}
}
