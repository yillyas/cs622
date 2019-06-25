package view;

public class BookingView {
	private int bookingID;
	private int vehicleID;
	private String make;
	private String model;
	private int rent;
	private String bkngStartDate;
	private String bkngEndDate;
	/**
	 * @param vehicleID
	 * @param make
	 * @param model
	 * @param rent
	 * @param bkngStartDate
	 * @param bkngEndDate
	 */
	public BookingView(int bookingID,int vehicleID, String make, String model, int rent, String bkngStartDate, String bkngEndDate) {
		super();
		this.bookingID = bookingID;
		this.vehicleID = vehicleID;
		this.make = make;
		this.model = model;
		this.rent = rent;
		this.bkngStartDate = bkngStartDate;
		this.bkngEndDate = bkngEndDate;
	}
	
	public int getVehicleID() {
		return vehicleID;
	}
	public String getMake() {
		return make;
	}
	public String getModel() {
		return model;
	}
	public int getRent() {
		return rent;
	}
	public String getBkngStartDate() {
		return bkngStartDate;
	}
	public String getBkngEndDate() {
		return bkngEndDate;
	}

	public int getBookingID() {
		return bookingID;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}

	public void setBkngStartDate(String bkngStartDate) {
		this.bkngStartDate = bkngStartDate;
	}

	public void setBkngEndDate(String bkngEndDate) {
		this.bkngEndDate = bkngEndDate;
	}
	
}
