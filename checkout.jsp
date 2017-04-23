<%@ page language="java" import="java.util.ArrayList, cs5530.*" %>
<html>
<head>
<script LANGUAGE="javascript">

function check_all_fields(form_obj){
	alert(form_obj.searchAttribute.value+"='"+form_obj.attributeValue.value+"'");
	if( form_obj.attributeValue.value == ""){
		alert("Search field should be nonempty");
		return false;
	}
	return true;
}

</script> 
</head>
<body>

<%
String login = session.getAttribute("login").toString();
ArrayList<Reservation> reservations = (ArrayList<Reservation>) session.getAttribute("reservations");
String result = "";
Connector2 connector = new Connector2();

for(Reservation r: reservations) {
		boolean added = TH.bookReservation(login, r, connector.con);
		result += "<br>House ID:" + r.house_id + "    StartDate:" + r.startDate + "    EndDate:" + r.endDate + "    " + (added ? "added!" : "Not added!");
		
}

session.setAttribute("reservations", new ArrayList<Reservation>());

connector.closeStatement();
connector.closeConnection();

%>

<p><%=result%></p>
<br><a href="/~5530u39/login.jsp"> User page </a></p>
<BR><a href="/~5530u39/"> Home </a></p>

</body>
