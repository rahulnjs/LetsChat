package com.chat.servlet;

import com.chat.component.Worker;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatusFetcher extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
		Worker wr = new Worker();
		String respon = wr.getUserStatus(Integer.parseInt(chatID), user, request.getParameter("time"));
		//System.out.println(respon);
		response.getWriter().write(respon);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
