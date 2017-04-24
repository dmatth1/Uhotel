<%@ page language="java" import="cs5530.*,java.util.ArrayList, java.sql.*" %>
<html>
<head>
<script LANGUAGE="javascript">

</script> 
</head>
<body>

<%
String sessionLogin = session.getAttribute("login").toString();
String result = "";
Connector2 connector = new Connector2();

//Process form if submitted
String user = request.getParameter("user");
String trust = request.getParameter("trust");
if(user != null && !user.isEmpty() && trust != null && !trust.isEmpty()) {
	String query = " insert into trust (login1, login2, isTrusted)"
		       + " values (?, ?, ?)";

	PreparedStatement preparedStmt = connector.con.prepareStatement(query);
	preparedStmt.setString(1, sessionLogin);
	preparedStmt.setString(2, user);
	if(trust.equals("1")) {
		 preparedStmt.setInt(3, 1);
	}
	else preparedStmt.setInt(3, 0);
        
	try{
	    preparedStmt.executeUpdate();
	    result += "<b>You now trust " + user + "</b><br>";
	    																                        
	    } catch(Exception e) {
	    result += e.toString();
	    }
}

//Show form
result += "<br><form action='trusted.jsp' method=get>User: <input type=text name='user' required><br>Trust: <input type=radio name='trust' value=1 checked>Yes<input type=radio name='trust' value=0>No<input type=submit></form><br>";

//Show user trustings
try{
	String sql = "select * from trust where login1 = '" + sessionLogin + "'";
	ResultSet rs = connector.con.createStatement().executeQuery(sql);
	
	result += "<table>";
	result += "<tr><th>User</th><th>Trusted</th><th></tr>";
	while(rs.next()) {
		result += "<tr>";
		result += "<td>" + rs.getString(2) + "</td><td>" + (rs.getInt(3) == 1 ? "yes" : "no") + "</td>";
		result += "</tr>";
	}
	result += "</table>";

} catch(Exception e) {
  	result += e.toString();
} 
 connector.closeStatement();
 connector.closeConnection();
%>

  
<p><%=result%></p>

<br><a href="/~5530u39/login.jsp"> User Page </a>
<BR><a href="/~5530u39/"> Home </a>

</body>
