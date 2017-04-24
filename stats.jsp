<%@ page language="java" import="cs5530.*,java.util.ArrayList, java.sql.*" %>
<html>
<head>
<script LANGUAGE="javascript">

</script> 
</head>
<body>

<br><a href="/~5530u39/login.jsp"> User Page </a>
<BR><a href="/~5530u39/"> Home </a>

<%
String sessionLogin = session.getAttribute("login").toString();
String result = "";
Connector2 connector = new Connector2();

String m = request.getParameter("m");

//Default m
if(m == null || m.isEmpty()) {
     m = "5";
}
result += "<br><form action='stats.jsp' method=get>Choose M: <input type=number name='m' min=0 required value=" + m + "><input type=submit></form><br>";

//Show tables
try{
	result += "<div style='float:left;display:inline;margin-right:100'>" + TH.mostPopularTHs(connector.con.createStatement(), Integer.parseInt(m)) + "</div>";

	result += "<div style='float:left;display:inline'>" + TH.mostExpensiveTHs(connector.con.createStatement(), Integer.parseInt(m)) + "</div>";
	
	result += "<div style='float:left;display:inline'>" + TH.mostHighlyRatedTHs(connector.con.createStatement(), Integer.parseInt(m)) + "</div>";
} catch(Exception e) {
  	result += e.toString();
} 
 connector.closeStatement();
 connector.closeConnection();
%>

  
<p><%=result%></p>

</body>
