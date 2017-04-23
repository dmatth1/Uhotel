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
String searchAttribute = request.getParameter("login");
if( searchAttribute == null ){
%>

	Register:
	<form name="register" method=get  action="register.jsp">
		Login: <input type=text name="login" required></br>
		First Name: <input type=text name="firstname" required></br>
		Last Name: <input type=text name="lastname" required></br>
		Password: <input type=password name="password" required></br>
		Telephone Number: <input type=number min=1 max=9999999999 name="telnumber" required></br>
		<input type=submit>
	</form>

<%

} else {

	String login = request.getParameter("login");
	String firstname = request.getParameter("firstname");
	String lastname = request.getParameter("lastname");
	String password = request.getParameter("password");
	String telnumber = request.getParameter("telnumber");
	Connector2 connector = new Connector2();
	String result = User.register(login, firstname, lastname, password, telnumber, connector.con);
	
	
%>  
    <p><%=result%></p>

<%
 connector.closeStatement();
 connector.closeConnection();
}  // We are ending the braces for else here
%>

<BR><a href="/~5530u39/"> Home </a></p>

</body>
