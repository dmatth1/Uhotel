package cs5530;

public class Reservation{
    public int house_id;
    public String startDate, endDate;
    public Reservation(int h_id, String sD, String eD){
	this.house_id = h_id;
	this.startDate = sD;
	this.endDate = eD;
    }

}