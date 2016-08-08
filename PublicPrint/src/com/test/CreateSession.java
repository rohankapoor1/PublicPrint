package com.test;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreateSession extends HttpServlet {
	public String UPLOAD_DIRECTORY = "E:" + File.separator + "uploads";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String mobNo = request.getParameter("userid");
		session.setAttribute("mobNo", mobNo);
		session.setAttribute("createTime", System.currentTimeMillis());
		session.setMaxInactiveInterval(30*60);
		request.getRequestDispatcher("start.jsp").forward(request, response);
	}
}
