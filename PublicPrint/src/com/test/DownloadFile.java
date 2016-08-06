package com.test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class DownloadFile extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 4096;
	String filePath;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userId = request.getParameter("userid");
		String key = request.getParameter("key");
		key = FileUploadHandler.encrypt(key);
		PreparedStatement ps = null;
		Connection conn = null;
		long collectionTime = -1;
		ResultSet rs = null;
		List path = new LinkedList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost/pp_objectinfo?user=root&password=");
			conn.setAutoCommit(false);
			ps = conn.prepareStatement("select File, CollectionTime from objectmaster where MobNo = ? and `Key`=?");
			ps.setLong(1, Long.valueOf(userId));
			ps.setString(2, key);
			rs = ps.executeQuery();
			while (rs.next()) {
				path.add(rs.getString(1));
				collectionTime = rs.getLong(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				conn.close();
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(collectionTime != -1)
		{
			HttpSession session = request.getSession();
			session.setAttribute("path-meta", userId + File.separator + collectionTime);
			session.setMaxInactiveInterval(30*60);
		}
		request.setAttribute("filepath", path);
		request.getRequestDispatcher("lists.jsp").forward(request, response);
//		response.sendRedirect(request.getContextPath() + "/lists.jsp");
	}
}