<%@ page language="java"
	import="javax.naming.*,javax.jms.*,com.slieer.ejbpro.web.MDBean"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		try {
			InitialContext ctx = new InitialContext();
			MDBean.callMDB(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		out.print("OK");
	%>
</body>
</html>