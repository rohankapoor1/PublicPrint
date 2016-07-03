<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div>
		<h3>Choose File to Upload in Server</h3>
		<form action="upload" method="post" enctype="multipart/form-data">
		<input type="text" name="userid" value="1234"/> <br/>
		<input type="file" name="filename" multiple/> <input type="submit" value="upload" />
		</form>
	</div>
	<br>
	<form action="DownloadFile" method="get" >
		<input type="text" name="userid" value="1234"/> <br/>
		<input type="password" name="key"/>
		<input type="submit" value="Download File" />
	</form>
</body>
</html>