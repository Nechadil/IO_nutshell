package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	public static void main(String[] args) {
		// Initiate a thread pool
		EventLoopGroup group = new NioEventLoopGroup();
		
		Bootstrap b = new Bootstrap();
		
		try {
			ChannelFuture future = b.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								ByteBuf buf = (ByteBuf)msg;
								byte[] bytes = new byte[buf.readableBytes()];
								buf.getBytes(buf.readerIndex(), bytes);
								System.out.println(new String(bytes));
								Server.clients.writeAndFlush(msg);
							}
							
							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								System.out.println("Channel activated");
								ByteBuf buf = Unpooled.copiedBuffer("Client: hello".getBytes());
								ctx.writeAndFlush(buf);
							}
						});
					}
				})
				.connect("localhost", 8888)
				.sync();
			
			future.addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					System.out.println("Operation Completion detected");
					
				}
			});
			
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
