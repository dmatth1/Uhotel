<%@ page language="java" import="cs5530.*,java.util.ArrayList" %>
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
String login = request.getParameter("login");
String password = request.getParameter("password");
String sessionLogin = session.getAttribute("login") == null ? null : session.getAttribute("login").toString();
String result = "";

String userMenu = "Thank you for logging in " + (login == null ? sessionLogin : login) + ".";
userMenu +="</br><a href='reserve.jsp'>Reserve and Checkout</a>";	    
userMenu += "<br><a href='browse.jsp'>Browse TH's</a>";
userMenu += "<br><a href='trusted.jsp'>Trusted Users</a>";

if((login == null || password == null) && sessionLogin == null) {
	result = "No login or password specified.";
}

else {
if(login != null && password != null) {
     Connector2 connector = new Connector2();
	   boolean success = User.login(login, password, connector.con);

	if(success) {
		     session.setAttribute("login", login);
		    session.setAttribute("reservations", new ArrayList<Reservation>());
		    
		    result = userMenu;
	    }

	else	{
		result = "Unable to log in with the specified login and password. Did you register?";
	  }
	  
 connector.closeStatement();
 connector.closeConnection();

}
	  
else {
     result = userMenu;
	       
}

}
%>

  
<p><%=result%></p>


<BR><a href="/~5530u39/"> Home </a></p>

</body>
