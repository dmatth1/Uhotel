package cs5530;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Browse {
    
    public Browse(){}
    
    public static String withoutTextSearch(String state, String city, String category, int pricelow, 
					 int pricehigh, String sortingOption, Connection con) throws SQLException{
	
	String sql;
	ResultSet results = null;
	PreparedStatement preparedStmt = con.prepareStatement("");
	
	sql = generateQueries(false, sortingOption, "");
	System.out.println(sql);
	preparedStmt = con.prepareStatement(sql);
	preparedStmt.setInt(1, pricelow);
	preparedStmt.setInt(2, pricehigh);
	preparedStmt.setString(3, state);
	preparedStmt.setString(4, state);
	preparedStmt.setString(5, city);
	preparedStmt.setString(6, city);
	preparedStmt.setString(7, category);
	preparedStmt.setString(8, category);
	results = preparedStmt.executeQuery();
	return printResults(results);
    }
    
    public static String withTextSearch(String state, String city, String category, int pricelow, 
				      int pricehigh, String text, String sortingOption, Connection con) throws SQLException{
	
	String sql;
	ResultSet results = null;
	PreparedStatement preparedStmt = con.prepareStatement("");
	String[] searchTerms = text.split(" ");
	String likeQuery = " and (name like ? or word like ?";
	for (int i = 1; i < searchTerms.length; i++)
	    likeQuery += " or name like ? or word like ?";
	likeQuery += ")";
	sql = generateQueries(true, sortingOption, likeQuery);
	System.out.println(sql);
	preparedStmt = con.prepareStatement(sql);
	preparedStmt.setInt(1, pricelow);
	preparedStmt.setInt(2, pricehigh);
	preparedStmt.setString(3, state);
	preparedStmt.setString(4, state);
	preparedStmt.setString(5, city);
	preparedStmt.setString(6, city);
	preparedStmt.setString(7, category);
	preparedStmt.setString(8, category);

	int i = 8;
	for (String s: searchTerms){
	    preparedStmt.setString(++i, "%" + s + "%");
	    preparedStmt.setString(++i, "%" + s + "%");
	}
	
	results = preparedStmt.executeQuery();
	return printResults(results);
    }
    
    public static String generateQueries(boolean textSearch, String sortingOption, String likeQuery){
	
	String sql = "";
	
	sql = "select distinct t.hid, login, category, name, city, state, price_per "
	    + ((sortingOption.equals("avg") | sortingOption.equals("avgtrusted")) ? " , A.avgScore " : "")
	    + "from th t" + (textSearch == true ? ", keywords k" : "")
	    + (sortingOption.equals("avg") ? ", (select avg(score) as avgScore, hid from feedback group by hid) as A " : "")
	    +  (sortingOption.equals("avgtrusted") ? ", (select f.hid, avg(f.score) as avgScore from trust tr, feedback f where tr.login2 = f.login group by f.hid) as A " : "")
	    + " where " + (textSearch == true ? "t.hid = k.hid and " : "")
	    +  ((sortingOption.equals("avg") | sortingOption.equals("avgtrusted")) ? " A.hid = t.hid and " : "")
	    + "(price_per > ? and price_per < ?) "
	    + "and (? is null or state = ?)"
	    + " and (? is null or city = ?) "
	    + "and (? is null or category = ?)"
	    + (textSearch == true ? likeQuery : "")
	    + (sortingOption.equals("price") ? " order by price_per " : "")
	    + ((sortingOption.equals("avg") | sortingOption.equals("avgtrusted")) ? " order by A.avgScore desc;" : "");
	
	//bad solution but an easy one
	if(!sql.substring(sql.length() - 1).equals(";"))
	    sql += ";";
	
	return sql;
	
    }
    
    public static String printResults(ResultSet results) throws SQLException{
	String result = "<table>";
	while(results.next()) {
	    result += "<tr>";
	    result += "<td>HID: " + results.getInt(1) + "</td><td>Username: " + results.getString(2) +
			       "</td><td>Category: " + results.getString(3) + "</td><td>Name: " + results.getString(4) + "</td><td>City: " + results.getString(5) +
		"</td><td>State: " + results.getString(6) + "</td><td>Price Per: " + results.getInt(7) + "</td><td><a href='th.jsp?hid=" + results.getInt(1) + "'>Overview</a></td>";
	    result += "</tr>";
	}

	result += "</table>";
	return result;
    }

}
