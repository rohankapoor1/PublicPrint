package com.test;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class FileUploadHandler extends HttpServlet {
	private final String UPLOAD_DIRECTORY = "E:" + File.separator + "uploads";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// process only if its multipart content
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				boolean allSet = true;
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				String userId = null;
				String key = getAccessKey();
				String encKey = encrypt(key);
				PreparedStatement ps = null;
				Connection conn = null;
				long collectionTime = System.currentTimeMillis();
				try
				{
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					conn = DriverManager.getConnection("jdbc:mysql://localhost/pp_objectinfo?user=root&password=");
					conn.setAutoCommit(false);
					ps = conn.prepareStatement("insert into objectmaster (MobNo,`Key`,`File`,CollectionTime) values (?,?,?,?)");
					for (FileItem item : multiparts) {
						if(item.isFormField())
						{
							userId = item.getString();
						}
						if (!item.isFormField()) {
							String fileName = new File(item.getName()).getName();
							ps.setLong(1, Long.valueOf(userId));
							ps.setString(2, encKey);
							ps.setString(3, fileName);
							ps.setLong(4, collectionTime);
							ps.addBatch();
						}
					}
					ps.executeBatch();
					conn.commit();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					allSet = false;
				}
				finally
				{
					conn.close();
					ps.close();
				}
				if(allSet)
				{
					for (FileItem item : multiparts) {
						if (!item.isFormField())
						{
							String fileName = new File(item.getName()).getName();
							item.write(new File(UPLOAD_DIRECTORY + File.separator + fileName));
						}
					}
				}

				// File uploaded successfully
				request.setAttribute("message", "File Uploaded Successfully with key: " + key);
			} catch (Exception ex) {
				request.setAttribute("message", "File Upload Failed due to " + ex);
			}

		} else {
			request.setAttribute("message", "Sorry this Servlet only handles file upload request");
		}

		request.getRequestDispatcher("/result.jsp").forward(request, response);

	}

	public static String encrypt(String key) {
		try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(key.getBytes());
            //Get the hash's bytes 
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            return sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
		return null;
	}

	private String getAccessKey()
	{
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}

}
