package netty.chatroom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ChatMessageEncoder extends MessageToByteEncoder<ChatMessage>{

	@Override
	protected void encode(ChannelHandlerContext ctx, ChatMessage msg, ByteBuf out) throws Exception {
		out.writeInt(msg.getSendDate().getBytes().length);
		out.writeInt(msg.getSenderId().getBytes().length);
		out.writeInt(msg.getMessage().getBytes().length);
		out.writeBytes(msg.getSendDate().getBytes());
		out.writeBytes(msg.getSenderId().getBytes());
		out.writeBytes(msg.getMessage().getBytes());
	}

}
