package cs5530;

import cs5530.*;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
public class TH {
    public TH() {

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
}