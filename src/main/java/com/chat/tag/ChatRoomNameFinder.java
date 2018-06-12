package com.chat.tag;

import com.chat.component.Worker;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ChatRoomNameFinder extends SimpleTagSupport {
	public void doTag() {
		String chatID = (String) getJspContext().findAttribute("cid");
		Worker wr = new Worker();
		getJspContext().setAttribute("chatRoomName", wr.getChatRoomName(Integer.parseInt(chatID)));
	}
}
