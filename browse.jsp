<%@ page language="java" import="cs5530.*,java.util.ArrayList" %>
<html>
<head>
<script LANGUAGE="javascript">
</script> 
</head>
<body>

<%
/*String login = request.getParameter("");
String password = request.getParameter("password");
*/

String sessionLogin = session.getAttribute("login").toString();
String result = "";

//Build browseForm
String browseForm = "<form name='browse' method=get action='browse.jsp'>";
browseForm += "Price, low: <input type=number name='low_price' min=0>";
browseForm += "<br>Price, high: <input type=number name='high_price'>";
browseForm += "<br>State: <input type=text name='state'>";
browseForm += "<br>City: <input type=text name='city'>";
browseForm += "<br>Keywords: <input type=text name='keywords'>";
browseForm += "<br>Category: <input type=text name='category'>";
browseForm += "<br>Sort By: <input type=radio name='sort' value='price' checked>Price<input type=radio name='sort' value='avg'>Average Feedback<input type=radio name='sort' value='avgtrusted'>Average Trusted Feedback";
browseForm += "<br><input type=submit>";
browseForm += "</form>";

//Get parameters
int low_price = 0;
int high_price = Integer.MAX_VALUE;
String state = null, city = null, text_search = null, category = null, sort = null;

if(request.getParameter("low_price") != null && !request.getParameter("low_price").isEmpty()) low_price = Integer.parseInt(request.getParameter("low_price"));
if(request.getParameter("high_price") != null && !request.getParameter("high_price").isEmpty()) high_price = Integer.parseInt(request.getParameter("high_price"));

if(request.getParameter("city") != null && !request.getParameter("city").isEmpty()) city = request.getParameter("city");
else if(request.getParameter("state") != null && !request.getParameter("state").isEmpty()) state = request.getParameter("state");

if(request.getParameter("category") != null && !request.getParameter("category").isEmpty()) category = request.getParameter("category");

if(request.getParameter("sort") == null || request.getParameter("sort").isEmpty()) sort = "price";
else sort = request.getParameter("sort");


if(sessionLogin == null) {
	result = "Not logged in!";
}

else {
     result += browseForm;
     
     Connector2 connector = new Connector2();
     String results = "";
     if(request.getParameter("keywords") != null && !request.getParameter("keywords").isEmpty()) {
     	results = Browse.withTextSearch(state, city, category, low_price, high_price, request.getParameter("keywords"), sort, connector.con);
     }
     else {
     	results = Browse.withoutTextSearch(state, city, category, low_price, high_price, sort, connector.con);
     }

     result +=  "<br>" + results;
	
	  
connector.closeStatement();
connector.closeConnection();

}
%>

  
<p><%=result%></p>

<br><a href="/~5530u39/login.jsp"> User Page </a></p>
<BR><a href="/~5530u39/"> Home </a></p>

</body>
