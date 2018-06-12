
package com.chat.component;

import java.util.HashMap;
import java.util.Map;

public class DataStorage {
	private static int chatRoomID = 2468;
	private static Map<Integer, ChatRoom> map = new HashMap<>();

	public static synchronized int addChatRoom(String chatRoomName, String creator) {
		map.put(Integer.valueOf(chatRoomID), new ChatRoom(chatRoomName, creator, chatRoomID));
		return chatRoomID++;
	}

	public static ChatRoom getChatRoom(int chatRoomID) {
		return (ChatRoom) map.get(Integer.valueOf(chatRoomID));
	}
}
