package netty.chatroom;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ChatMessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int sendDateLen = in.readInt();
		int senderIdLen = in.readInt();
		int sendMessageLen = in.readInt();
		
		ByteBuf dateBuf = in.readBytes(sendDateLen);
		byte[] dateBytes = new byte[sendDateLen];
		dateBuf.getBytes(dateBuf.readerIndex(), dateBytes);
		
		ByteBuf senderIdBuf = in.readBytes(senderIdLen);
		byte[] idBytes = new byte[senderIdLen];
		senderIdBuf.getBytes(senderIdBuf.readerIndex(), idBytes);
		
		ByteBuf msgBuf = in.readBytes(sendMessageLen);
		byte[] msgBytes = new byte[sendMessageLen];
		msgBuf.getBytes(msgBuf.readerIndex(), msgBytes);
		
		out.add(new ChatMessage(new String(dateBytes), new String(msgBytes), new String(idBytes)));
	}
	
}
