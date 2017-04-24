package cs5530;

import cs5530.*;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
public class TH {
    public TH() {

    }

    public static String rateFeedback(String login, String fid, String feedback_rating, Connection con) throws SQLException {
	String ret = "";
	String query = " insert into rates (login, fid, rating)"
	    + " values (?, ?, ?)";

	PreparedStatement preparedStmt = con.prepareStatement(query);
	preparedStmt.setString(1, login);
	preparedStmt.setInt(2, Integer.parseInt(fid));
	preparedStmt.setInt(3, Integer.parseInt(feedback_rating));

	try{
	    preparedStmt.executeUpdate();
	    ret = "Successfully rated feedback!";
	}

	catch(Exception e){
	    e.printStackTrace();
	    ret = "cannot execute the query" + e.toString();
	}


	return ret;
    }

    public static boolean bookReservation(String login, Reservation r, Connection con) {

	try {
	String hid = Integer.toString(r.house_id), startdate = r.startDate, enddate = r.endDate;
    if (reservationExists(con, hid, startdate, enddate)) {
	//System.out.println("Could not add " + i/3 + " reservation");
	return false;
    }
    else {
	ResultSet results = con.createStatement().executeQuery("select price_per from th where hid = " + hid + "");
	if(!results.next()) {
	    return false;
	}

	double cost = results.getDouble(1);

	String sql = "insert into reserve (login, hid, pid, startdate, enddate, cost) "
	    + "values (?,?,?,?,?,?)";
	PreparedStatement pstmt = con.prepareStatement(sql);
	pstmt.setString(1, login);
	pstmt.setInt(2, Integer.parseInt(hid));
	pstmt.setInt(3, -1);

	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
	java.util.Date startDate = formatter.parse(startdate);
	java.util.Date endDate = formatter.parse(enddate);
	pstmt.setDate(4, new java.sql.Date(startDate.getTime()));
	pstmt.setDate(5, new java.sql.Date(endDate.getTime()));
	pstmt.setDouble(6, cost);

	pstmt.executeUpdate();
    }

    return true;
	} catch(Exception e) {
	    return false;
	}
    }
    
    private static boolean reservationExists(Connection con, String hid, String startdate, String enddate)throws SQLException, ParseException {
	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
	java.util.Date startDate = formatter.parse(startdate);
	java.util.Date endDate = formatter.parse(enddate);
	PreparedStatement pstmt = con.prepareStatement("SELECT count(*) "
						       + "FROM reserve "
						       + "WHERE hid = ? "
						       + "AND (? BETWEEN startdate AND enddate "
						       + "OR ? BETWEEN startdate AND enddate)");

	pstmt.setInt(1, Integer.parseInt(hid));
	pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
	pstmt.setDate(3, new java.sql.Date(endDate.getTime()));

	ResultSet results = pstmt.executeQuery();
	if(results.next() && results.getInt(1) != 0) {
	    return true;
	}

	return false;
    }

    public static String usefullnessFeedback(boolean mustBeRated, String login, String hid, String n_useful, Connection con) throws SQLException{
	String ret = "";
	String query = "";
	if(mustBeRated) {
	    query = "select feedback.fid, feedback.login, feedback.score, feedback.text, feedback.date " +
	    "from feedback,rates " +
	    "where hid = ? and feedback.fid = rates.fid " +
	    "group by feedback.fid " +
	    "order by avg(rates.rating) desc " +
	    "limit ?";
	}
	else {
	    query = "select feedback.fid, feedback.login, feedback.score, feedback.text, feedback.date " +
	    "from feedback " +
	    "where hid = ? " +
	    "limit ?";
	}

	PreparedStatement preparedStmt = con.prepareStatement(query);
	preparedStmt.setInt(1, Integer.parseInt(hid));
	preparedStmt.setInt(2, Integer.parseInt(n_useful));

	try{
	    ResultSet results = preparedStmt.executeQuery();
	    ret += "<table>";
	    while(results.next()) {
		ret += "<tr>";

		ret += "<td>Username: " + results.getString(2) +
				   "</td><td>Score: " + results.getInt(3) + "</td><td>Text: " + results.getString(4)
				   + "</td><td>Date: " + results.getDate(5) + "</td>";

		//If logged in user left feedback, show no rate
		if(!results.getString(2).equals(login)) {
		    preparedStmt = con.prepareStatement("select * from rates where fid = ? and login = ?");
		    preparedStmt.setInt(1, results.getInt(1));
		    preparedStmt.setString(2, login);
		    ResultSet rs = preparedStmt.executeQuery();
		    
		    //If already left feedback rating, show rating left
		    if(rs.next()) {
			ret += "<td>Rating: " + rs.getString(3) + "</td>";
		    }
		    else {
			ret += "<td><form method=get action='th.jsp'>Rating: <input type=number name='feedback_rating' min=0 max=2 required><input type=hidden name='hid' value='" + hid + "'><input type=hidden name='fid' value='" + results.getString(1) + "'><input type=submit></form></td>";
		
		    }

		}
		ret += "</tr>";
	    }
	    ret += "</table";
	}

	catch(Exception e){
	    e.printStackTrace();
	    ret = "cannot execute the query" + e.toString();
	}


	return ret;
    }
}