<%@ page language="java" import="java.util.ArrayList, java.sql.*, cs5530.*" %>
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

	%>
	 Add TH:
	 <form name="addth" method=get  action="addth.jsp">
	       Category: <input type=text name="category" required></br>
	       		 Name: <input type=text name="name" required></br>
			       Url: <input type=text name="url" required></br>
			       	    Telephone Number: <input type=number min=1 max=9999999999 name="telnumber" required></br>
				    	      Year Built: <input type=number min=1 max=9999999999 name="yearbuilt" required></br>
					      	   Number Of Rooms: <input type=number min=1 max=9999999999 name="numrooms" required></br>
						   	  Address: <input type=text name="address" required></br>
							  	   City: <input type=text name="city" required></br>
								   	 State: <input type=text name="state" required></br>
									 	Zip Code: <input type=number min=1 max=9999999999 name="zipcode" required></br>
										    Price Per Person: <input type=number min=1 max=9999999999 name="priceper" required></br>
										    	  Keywords: <input type=text name="keywords" required></br>
											  	    <input type=submit>
												    </form>

     <%//String login = request.getParameter("login");
     String category = request.getParameter("category");
     String name = request.getParameter("name");
     String url = request.getParameter("url");
     String telnumber = request.getParameter("telnumber");
     String year = request.getParameter("yearbuilt");
     String numrooms = request.getParameter("numrooms");
     String address = request.getParameter("address");
     String city = request.getParameter("city");
     String state = request.getParameter("state");
     String zip = request.getParameter("zipcode");
     String price = request.getParameter("priceper");
     String keywords = request.getParameter("keywords") + " (comma seperated): ";

     if(category != null && !category.isEmpty()) {
     ArrayList<String> al = new ArrayList<String>();
     al.add(category);
     al.add(name);
     al.add(url);
     al.add(telnumber);
     al.add(year);
     al.add(numrooms);
     al.add(address);
     al.add(city);
     al.add(state);
     al.add(zip);
     al.add(price);
     al.add(keywords);


     Connector2 connector = new Connector2();
     result = TH.addTH(login, al, connector.con);
      connector.closeStatement();
       connector.closeConnection();

     }
     
     
%>  

<p><%=result%></p>
<br><a href="/~5530u39/login.jsp"> User page </a></p>
<BR><a href="/~5530u39/"> Home </a></p>

</body>