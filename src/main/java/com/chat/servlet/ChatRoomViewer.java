package com.chat.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChatRoomViewer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() {
		//System.out.println(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String chatID = "";
		Cookie[] cs = request.getCookies();
		String user = "";
		Cookie[] arrayOfCookie1;
		int j = (arrayOfCookie1 = cs).length;
		for (int i = 0; i < j; i++) {
			Cookie c = arrayOfCookie1[i];
			if (c.getName().equals("user")) {
				user = c.getValue();
			}
			if (c.getName().equals("cid")) {
				chatID = c.getValue();
			}
		}
		request.setAttribute("cid", chatID);
		request.setAttribute("user", user);
		request.getRequestDispatcher("chat-room.jsp").forward(request, response);
	}
}
