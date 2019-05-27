package carRentTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class dateTest {
	private static String BOOKINGDETAILS = "bookingdetail.txt";
	
	public static void appendToBookingDetail(String newBooking) {
		
		BufferedWriter bw = null;
		try {
		//	File file = new File("src/bookingdetail.txt");
		//	FileWriter fw = new FileWriter(file);
		//	bw = new BufferedWriter(fw);
		//	bw.write(newBooking);
			
			bw = new BufferedWriter(new FileWriter(BOOKINGDETAILS, true)); // (append flag)
			bw.write(newBooking);  // C3
			System.out.println(newBooking + "<--written to bookingdetail");  
			//bw.newLine(); // C4
			//bw.flush();
		} 
		catch (IOException e) {
			System.out.println("Could not append to bookingdetail.txt");
			e.printStackTrace();
		} 
		finally { // (close file)
			if (bw != null) 
				try {
					bw.close();
				} 
			catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}

	public static void main(String[] args) {
		
		/*
		String startDate = "01/02/2016";
		String passedDate = "29/02/2016";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate date1 = LocalDate.parse(startDate, formatter);
		LocalDate date2 = LocalDate.parse(passedDate, formatter);

		long elapsedDays = ChronoUnit.DAYS.between(date1, date2);
		System.out.println(elapsedDays); 
		*/
		
		String book = "this is a test. \n";
		
		appendToBookingDetail(book);

	}

}
