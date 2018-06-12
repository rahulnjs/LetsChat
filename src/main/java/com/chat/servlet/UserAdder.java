package com.chat.servlet;

import com.chat.component.Worker;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAdder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("u-name");
		String chatRoomID = request.getParameter("cid");
		Worker worker = new Worker();
		response.getWriter().write(worker.addUser(Integer.parseInt(chatRoomID), userName));
	}
}
