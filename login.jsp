<%@ page language="java" import="cs5530.*" %>
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
String result = "";

if(login == null || password == null) {
	result = "No login or password specified.";
}

else {

     Connector2 connector = new Connector2();
	   boolean success = User.login(login, password, connector.con);

	if(success) {
		result = "Thank you for loggin in " + login + ".";	    

	    }

	else	{
		result = "Unable to log in with the specified login and password. Did you register?";
	  }


 connector.closeStatement();
 connector.closeConnection();

}
%>

  
<p><%=result%></p>


<BR><a href="/~5530u39/"> Home </a></p>

</body>
