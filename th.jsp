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

     //Rate feedback
     String feedback_rating= request.getParameter("feedback_rating");
     String fid = request.getParameter("fid");
     if(feedback_rating != null && !feedback_rating.isEmpty() && fid != null && !fid.isEmpty()) {
     	result += "<b>" + TH.rateFeedback(sessionLogin, fid, feedback_rating, connector.con) + "</b><br>";
     }


     //Favorite
     String favorite = request.getParameter("favorite");
     if(favorite != null && favorite.equals("yes")) {
     	try {
     	String sql = "insert into favorites (hid, login) values (?,?)";
	PreparedStatement psmt = connector.con.prepareStatement(sql);
	psmt.setInt(1, Integer.parseInt(hid));
	psmt.setString(2, sessionLogin);
	psmt.executeUpdate();

	result += "Favorited!";
     	} catch(Exception e) {
	  result += "Unable to favorite";
	  result += e.toString();
	}
     }
     else {
     	  String favoriteHTML = "<a href='th.jsp?hid=" + hid + "&favorite=yes'>Favorite this place</a>";
     	  String sql = "SELECT * FROM favorites WHERE hid = '" + hid + "' AND login = '" + sessionLogin + "'";
    	  try {
     	      ResultSet rs = connector.con.createStatement().executeQuery(sql);
	      if(rs.next()) {
	      	result += "You have favorited this place!";
	 	}
		else {
		result += favoriteHTML;
		}
		
	} catch(Exception e) {
	  result += favoriteHTML;
	}	 
     }

     //TH stats
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

	//Record user feedback
	String feedback_text = request.getParameter("feedback_text");
	String feedback_val = request.getParameter("feedback_val");
	
	if(feedback_text != null && !feedback_text.isEmpty() && feedback_val != null && !feedback_val.isEmpty()) {
		try {
     		    sql = "insert into feedback (hid, login,score,text) values (?,?,?,?)";
		    PreparedStatement psmt = connector.con.prepareStatement(sql);
		    psmt.setInt(1, Integer.parseInt(hid));
		    psmt.setString(2, sessionLogin);
		    psmt.setInt(3, Integer.parseInt(feedback_val));
		    psmt.setString(4, feedback_text);
		    psmt.executeUpdate();

		    result += "<br><b>You have left feedback for this place. Thank you!</b>";
     		} catch(Exception e) {
	  	  result += "<br>Unable to leave feedback";
	 	  result += e.toString();
		}		
	}
	else {
	     sql = "Select * from feedback where hid = '" + hid + "' and login = '" + sessionLogin + "'";
	     rs = connector.con.createStatement().executeQuery(sql);	
	     if(rs.next()) {
	     	result += "<br><b>You have left feedback for this place.</b>"; 
		}
		else {
		     result += "<br><form method=get action='th.jsp'><input type=hidden name='hid' value='" + hid + "'>Leave Feedback: <input type=text name='feedback_text' required>Score: <input type=number name='feedback_val' min=0 max=10 required><input type=submit></form>";
	   	}
	}


	//Top n most useful feedbacks
	String n_useful = request.getParameter("n_useful");
	if(n_useful == null || n_useful.isEmpty()) n_useful = "10";
	result += "<form name='most_useful' method=get action='th.jsp'><input type=hidden name='hid' value='" + hid + "'>";
	result += "Top <input type=number name='n_useful' min=0 value=" + n_useful + "> Most Useful Feedbacks <input type=submit></form><br>";
	
	//Not all feedback have ratings
	if(request.getParameter("n_useful") == null || request.getParameter("n_useful").isEmpty()) {
		result += TH.usefullnessFeedback(false, sessionLogin, hid, n_useful, connector.con);
	}
	else {
	     result += TH.usefullnessFeedback(true, sessionLogin, hid, n_useful, connector.con);

	     }
     } catch(Exception e) {
       result = "Invalid hid specified.";
     }


     connector.closeStatement();
     connector.closeConnection();

}
%>

  
<p><%=result%></p>

<br><a href="/~5530u39/login.jsp"> User Page </a>
<BR><a href="/~5530u39/"> Home </a>

</body>
