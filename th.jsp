<%@ page language="java" import="cs5530.*,java.sql.*, java.util.ArrayList" %>
<html>
<head>
<script LANGUAGE="javascript">
</script> 
</head>
<body>

<%

String sessionLogin = session.getAttribute("login").toString();
String result = "";


if(request.getParameter("hid") == null) {
	result = "Please specify a TH ID!";
}

else {
     String hid = request.getParameter("hid");
     Connector2 connector = new Connector2();
     String sql = "SELECT * FROM th WHERE hid = '" + hid + "'";

     try{
	ResultSet rs = connector.con.createStatement().executeQuery(sql);
	while(rs.next()) {
		result += "<br>HID: " + rs.getString("hid");
		result += "<br>User: " + rs.getString("login");
		result += "<br>Category: " + rs.getString("category");
		result += "<br>Name: " + rs.getString("name");
		result += "<br>URL: " + rs.getString("URL");
		result += "<br>Telephone Number: " + rs.getString("telnumber");
		result += "<br>Year Built: " + rs.getString("yearbuilt");
		result += "<br>Number of Rooms: " + rs.getString("numrooms");
		result += "<br>Address: " + rs.getString("address");
		result += "<br>City: " + rs.getString("city");
		result += "<br>State: " + rs.getString("state");
		result += "<br>Zip: " + rs.getString("zip");
		result += "<br>Price per person: " + rs.getString("price_per");
	}

	//Top n most useful feedbacks
	String n_useful = request.getParameter("n_useful");
	if(n_useful == null || n_useful.isEmpty()) n_useful = "10";
	result += "<form name='most_useful' method=get action='th.jsp'><input type=hidden name='hid' value='" + hid + "'>";
	result += "Top <input type=number name='n_useful' min=0 value=" + n_useful + "> Most Useful Feedbacks <input type=submit></form><br>";
	
	result += TH.usefullnessFeedback(sessionLogin, hid, n_useful, connector.con);

     } catch(Exception e) {
       result = "Invalid hid specified.";
     }


     connector.closeStatement();
     connector.closeConnection();

}
%>

  
<p><%=result%></p>

<br><a href="/~5530u39/login.jsp"> User Page </a></p>
<BR><a href="/~5530u39/"> Home </a></p>

</body>
