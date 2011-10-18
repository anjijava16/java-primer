<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.page import="com.xcap.web.Token,java.util.*" />
<!--  </%@ page import="java.io.*" %> -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		tokenTest(request,out);
	%>

	<%!	
		void tokenTest(HttpServletRequest request, JspWriter out) throws Exception  {
		    String temp = request.getParameter("token");
			String token = "F8sUmrH997S5Vf0dovlHs0Drw1fp72LBQ3KUEcG5iho*";
		    if(temp != null && temp.length() > 1){
			    temp = temp.substring(1);
			    out.print("token is:" + temp + "<br/>");
		    	token = temp;		    	
		    }
			Map<Token.TokenInfo,String> result = Token.parseTokenXML(token);
			out.print("token:" + result);
		}
	
		interface face{
			
		}
		class test{
			
		}
	%>
</body>
</html>