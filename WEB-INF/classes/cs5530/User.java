package cs5530;

import java.sql.*;
//import javax.servlet.http.*;

public class User{
    //Connection con;
    public User(/*Connection con*/) {
	    //this.con = con;
	}

    public static boolean login(String login, String password, Connection con) throws Exception {
	String sql = "SELECT * FROM users WHERE login='" 
	    + login + "' && password='" + password+ "'";
	
	try {
	    ResultSet rs = con.createStatement().executeQuery(sql);
	    
	    
	    if(rs.next()){
		return true;
	    }
	    
	} catch (SQLException e) {
	    System.out.println("FAILED");
	    e.printStackTrace();
	}
	
	return false;
    }


    public static String register(String login, String firstname, String lastname, String password, String telnumber, Connection con) throws Exception{
	    /*		String query;
		String resultstr="";
		ResultSet results; 
		query="Select * from orders where "+attrName+"='"+attrValue+"'";
		try{
			results = stmt.executeQuery(query);
        } catch(Exception e) {
			System.err.println("Unable to execute query:"+query+"\n");
	                System.err.println(e.getMessage());
			throw(e);
		}
		System.out.println("Order:getOrders query="+query+"\n");
		while (results.next()){
			resultstr += "<b>"+results.getString("login")+"</b> purchased "+results.getInt("quantity") +
							" copies of &nbsp'<i>"+results.getString("title")+"'</i><BR>\n";	
		}
		return resultstr;
	    */

	    String query = " insert into users (login, firstname, lastname, password, telnumber)"
		+ " values (?, ?, ?, ?, ?)";
	    
	    PreparedStatement preparedStmt = con.prepareStatement(query);

	    preparedStmt.setString(1, login);
	    preparedStmt.setString(2, firstname);
	    preparedStmt.setString(3, lastname);
	    preparedStmt.setString(4, password);
	    preparedStmt.setInt(5, Integer.parseInt(telnumber));
	    
	    try{
		preparedStmt.executeUpdate();
	    }
	    
	    catch(Exception e){
		return e.toString();
	        //return "Unable to register at this time.";
	    }
	    

	    return "Successfully registered as " + login + ". Thank you " + firstname + ".";
	    
	}
}
