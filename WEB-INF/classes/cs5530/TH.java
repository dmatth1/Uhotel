package cs5530;

import cs5530.*;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class TH {
    public TH() {

    }

    public static String addTH(String login, ArrayList<String> items, Connection con) throws NumberFormatException, SQLException{
	
	String ret = "";
	boolean rs = false;
	String query = "insert into th (login, category, name, URL, telnumber,"
	    + "yearbuilt, numrooms, address, city, state, zip, price_per)"
	    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	PreparedStatement preparedStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	preparedStmt.setString (1, login);
	String s = "";

	for (int i = 0; i < items.size() -1; i++){
	    s = items.get(i);
	    
	    boolean is_int = false;
	    if (s.contains(":")){ 
		if(s.split(":")[1] == "int") {
		    is_int = true;
		    preparedStmt.setInt(i+2, Integer.parseInt(s.split(":")[0]));
		}
	    }
	    if(!is_int)
		preparedStmt.setString (i+2, s);
	}
	int hid = 1;

	try{
	    rs = preparedStmt.execute();
	    ResultSet rs1 = preparedStmt.getGeneratedKeys();
	    if (rs1.next()) hid = rs1.getInt(1);
	}
	
	catch(Exception e){
	    e.printStackTrace();
	    ret = "cannot execute the query" + e.toString();
	}

	String keywords_str = items.get(items.size() - 1);
	for(String keyword: keywords_str.split(",")) {
	    String sql = "insert into keywords (word, hid) values ('" + keyword + "', '" + hid + "')";
	    con.createStatement().executeUpdate(sql);
	}
	ret += "<br><b>Added new TH!</b><br>";
	

	return ret;
	
    }

    public static String mostPopularTHs(Statement stmt, int limit) throws SQLException{
	String output = "";
	ArrayList<String> categories = new ArrayList<String>();
	try {
	    categories = getCategories(stmt);
	}
	catch(Exception e) {
	    output = "With categories" + e.toString();
	}
	ResultSet results = null;

	output += "<h3>Most Popular TH's</h3>";
	try{
      	for(String category: categories){
	    output += "<br><b>Category: " + category + "</b><br>";
	    output += "<table>";
	    output += "<tr><th>HID</th><th>Category</th><th>Visits</th></tr>";
	
	    String sql = "select t.hid, category, count(t.hid) as visitCount "
		+ "from th t, visit v "
		+ "where (t.hid = v.hid) and (category = " +  "'" +  category + "'" + " )"
		+ " group by hid  "
		+ " order by visitCount desc limit " + limit + ";";
	    results = stmt.executeQuery(sql);
	    while (results.next())
		output += "<tr><td>" + results.getString("hid")+ "</td><td>" + results.getString("category")
		    + "</td><td>" + results.getString("visitCount") +"</td></tr>";

	    output += "</table>";
	    results.close();
	}
	}
	catch(Exception e) {
	    output += "With results " + e.toString();
	}

	return output;
    }

    public static String mostExpensiveTHs(Statement stmt, Integer limit) throws SQLException{
	
	String output = "";
	ArrayList<String> categories = getCategories(stmt);
	ResultSet results = null;
	
	output += "<h3>Most Expensive TH's</h3>";
	for(String category: categories){
	    output += "<br><b>Category: " + category + "</b><br>";
	    output += "<table>";
	    output += "<tr><th>HID</th><th>Category</th><th>Visit Count</th><th>Average Cost</th></tr>";
	
	    String sql = "select t.hid, category, count(t.hid) as visitCount, avg(cost) as avgCost "
		+ "from th t, visit v "
		+ "where (t.hid = v.hid) and (category = " +  "'" +  category + "'" + " )"
		+ " group by hid  "
		+ " order by avgCost desc limit " + limit + ";";
	    results = stmt.executeQuery(sql);
	    while (results.next())
		output += "<tr><td>" + results.getString("hid")+ "</td><td>" + results.getString("category")
		    + "</td><td>" + results.getString("visitCount") + "</td><td>" + results.getString("avgCost") + "</td></tr>";

	    results.close();
	    output += "</table>";
	}

	return output;


    }

    public static String mostHighlyRatedTHs(Statement stmt, Integer limit) throws SQLException{

	String output = "";
	ArrayList<String> categories = getCategories(stmt);
	ResultSet results = null;
	
	output += "<h3>Highest Rated TH's</h3>";
	for(String category: categories){
	    output += "<br><b>Category: " + category + "</b><br>";
	    output += "<table>";
	    output += "<tr><th>HID</th><th>Category</th><th>Average Score</th></tr>";

	    String sql = "select f.hid, category, avg(score) as avgScore "
		+ "from th t, feedback f "
		+ "where (t.hid = f.hid) and (category = " +  "'" +  category + "'" + " )"
		+ " group by f.hid  "
		+ " order by avgScore desc limit " + limit + ";";
	    results = stmt.executeQuery(sql);
	    while (results.next())
		output += "<tr><td>" + results.getString("hid")+ "</td><td>" + results.getString("category")
		    + "</td><td>" + results.getString("avgScore") + "</td></tr>";

	    output += "</table>";
	    results.close();
	    
	}

	return output;


    }

    public static ArrayList<String> getCategories(Statement stmt) throws SQLException{
	ArrayList<String> categories = new ArrayList<String>();

	String sql = "select distinct category from th";

	ResultSet results = stmt.executeQuery(sql);

	while(results.next())
	    categories.add(results.getString(1));
	return categories;

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