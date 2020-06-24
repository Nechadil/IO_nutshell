package netty.chatroom;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.netty.channel.embedded.EmbeddedChannel;

public class CodecTest {
	@Test
	public void testCodec() {
		EmbeddedChannel channel = new EmbeddedChannel(new ChatMessageEncoder(), new ChatMessageDecoder());
		ChatMessage sendMessage = new ChatMessage("20:10:10", "Hello World", "Guest 89757");
		channel.writeInbound(sendMessage);
		ChatMessage readMessage = channel.readInbound();
		assertEquals(sendMessage.getMessage(), readMessage.getMessage());
		assertEquals(sendMessage.getSendDate(), readMessage.getSendDate());
		assertEquals(sendMessage.getSenderId(), readMessage.getSenderId());
	}
}
