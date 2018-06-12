package com.chat.servlet;

import com.chat.component.Worker;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChatRoomCreator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("u-name");
		String chatRoomName = request.getParameter("chat-room-name");
		Worker worker = new Worker();
		int chatRoomID = worker.createChatRoom(userName, chatRoomName);
		//System.out.println("Chat Room {" + chatRoomName + "} created by " + userName + " with id {" + chatRoomID + "}");
		response.getWriter().write(String.valueOf(chatRoomID));
	}
}
