<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Download</title>
</head>
<body>
<h1>Download counter: <%=it.dsestili.cv.Utils.getDownloadCounter("cv")%></h1>
 <form method='GET' action='/cv/download'>
 	<input type="hidden" value="cv" name="param" />
    <input type="submit" value="Download curriculum">
 </form>
 <a href="/MyWebProject">Checksums</a>
</body>
</html>
