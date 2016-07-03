package com.test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadFile extends javax.servlet.http.HttpServlet implements
javax.servlet.Servlet {
	static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 4096;
	String filePath;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String userId = request.getParameter("userid");
		String key = request.getParameter("key");
		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		try
		{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://localhost/pp_objectinfo?user=root&password=");
		conn.setAutoCommit(false);
		ps = conn.prepareStatement("select from objectmaster where MobNo = ? and Key=?");
		ps.setLong(1, Long.valueOf(userId));
		ps.setString(2, key);
		rs = ps.executeQuery();
		while(rs.next())
		{
			
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
			rs.close();
			conn.close();
			ps.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		filePath = "E:" + File.separator + "uploads" + File.separator + "abc.jpg";
				/*getServletContext().getRealPath("") + File.separator
				+ "abc.txt";*/
		File file = new File(filePath);
		int length = 0;
		ServletOutputStream outStream = response.getOutputStream();
		response.setContentType("text/html");
		response.setContentLength((int) file.length());
		String fileName = (new File(filePath)).getName();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");

		byte[] byteBuffer = new byte[BUFSIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
			outStream.write(byteBuffer, 0, length);
		}

		in.close();
		outStream.close();
	}
}