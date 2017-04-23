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
String result = "";


if(login == null) {
	 result = "Not logged in!";
}
else {
     String house_id = request.getParameter("house_id");
     String sDate = request.getParameter("start_date");
     String eDate = request.getParameter("end_date");
     ArrayList<Reservation> reservations = (ArrayList<Reservation>) session.getAttribute("reservations");     
     

     result = "Reserve:<form name='reserve' method=get action='reserve.jsp'>House ID: <input type='text' name='house_id' required><br>StartDate(MM-dd-yyyy): <input type='text' name='start_date'><br>EndDate(MM-dd-yyyy): <input type='text' name='end_date'><br><input type='submit'></form>";    
		 
     if(house_id == null || sDate == null || eDate == null) {
     		 result += "<br> Please fill out all fields";
		 } else {
		   Reservation newReservation = new Reservation(Integer.parseInt(house_id), sDate, eDate);
		   result += "<br>Reservation added to checkout!";
		   reservations.add(newReservation);
		 }
		 
	for(Reservation r: reservations) {
			result += "<br>House ID:" + r.house_id + "    StartDate:" + r.startDate + "    EndDate:" + r.endDate;
			}

	result += "<br><a href='checkout.jsp'>Checkout</a>";
}
%>

<p><%=result%></p>
<br><a href="/~5530u39/login.jsp"> User page </a></p>
<BR><a href="/~5530u39/"> Home </a></p>

</body>
