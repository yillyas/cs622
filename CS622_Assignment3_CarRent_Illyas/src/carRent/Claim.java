package carRent;

import java.util.HashMap;

public class Claim <A extends Account, B extends Booking>{
	private int claimID;
	private boolean resloved = false;
	private B booking;
	private A account;
	private double claimedAmount;
	
	public Claim (A a, B b) {
		this.claimID = claimID ++;
		this.account = a;
		this.booking = b;
		//this.claimedAmount = c;
	}
	
	public void resolveClaim() {
		this.resloved = true;
	}
	
	/*
	public <K,V> void claimType (K a, V b ) {
		
	}
	*/
	
	public void updateBalance(double claimedAmount) {
		A account = this.account;
		this.claimedAmount = claimedAmount;  // update the claim information with claim amount
		//double curentBalance; // = account.getBalance();
		//System.out.println("Current balance: " + curentBalance);
		if (account instanceof Owner) { // If the claim is filed by Owner
			//double adjustedOwnerbalance = curentBalance + claimedAmount; // add claimed amount to owner
			//double adjustedRenterbalance = curentBalance - claimedAmount; // deduct from the renter (Renter will have to pay the additional charges)
			//currentBalance = 
			Renter renter = booking.getRenter(); // Get Renter account information for booking details
			Owner owner = booking.getOwner();    // Get Owner info
			HashMap<String,Double> tripRent = this.booking.calculateCost();
			System.out.println("Current Owner balance: " + owner.getBalance());
			double adjustedOwnerbalance = owner.getBalance() + claimedAmount; // add claimed amount to owner current balance
			double adjustedRenterbalance = renter.getBalance() + claimedAmount; // add claimed amount to renter current balance
			double ownerTripRent = tripRent.get("ownerTripBalance") + claimedAmount;
			double renterTripRent = tripRent.get("renterTripBalance") + claimedAmount;
			owner.setRentalChangers(this.booking.getBookingID(), ownerTripRent); // update the amount of rent for Owner for this particular trip
			owner.setBalance(adjustedOwnerbalance); // update Owner total balance
			renter.setRentalChangers(this.booking.getBookingID(), renterTripRent); // update the amount of rent for Renter for this particular trip
			renter.setBalance(adjustedRenterbalance); // update Renter balance
			
			this.resolveClaim(); // change the claim state to resolved
			System.out.println("New Owner Rent for this booking: " + ownerTripRent);
			System.out.println("Adjusted Owner balance: " + adjustedOwnerbalance);
			
			System.out.println("New Renter Rent for this booking: " + renterTripRent);
			System.out.println("Adjusted Renter balance: " + adjustedRenterbalance);
			
		}
		else if (account instanceof Renter) { // If the claim is filed by Renter
			//double adjustedOwnerbalance = curentBalance - claimedAmount; // deduct claimed amount from Owner
			//double adjustedRenterbalance = curentBalance + claimedAmount; // Add claimed amount to Renter
			//double adjustedbalance = curentBalance - claimedAmount; // add claimed amount
			Renter renter = booking.getRenter(); // Get Renter account information for booking details
			Owner owner = booking.getOwner();    // Get Owner info
			HashMap<String,Double> tripRent = this.booking.calculateCost();
			System.out.println("Current Renter balance: " + renter.getBalance());
			double adjustedOwnerbalance = owner.getBalance() - claimedAmount; // subtract claimed amount to owner current balance
			double adjustedRenterbalance = renter.getBalance() - claimedAmount; // subtract claimed amount to renter current balance
			double ownerTripRent = tripRent.get("ownerTripBalance") - claimedAmount;
			double renterTripRent = tripRent.get("renterTripBalance") + claimedAmount;
			owner.setRentalChangers(this.booking.getBookingID(), ownerTripRent); // update the amount of rent for Owner for this particular trip
			owner.setBalance(adjustedOwnerbalance); // update Owner balance
			renter.setRentalChangers(this.booking.getBookingID(), renterTripRent); // update the amount of rent for Renter for this particular trip
			renter.setBalance(adjustedRenterbalance); // update Renter balance
			this.resolveClaim(); // change the claim state to resolved
			//System.out.println("New Owner balance: " + adjustedOwnerbalance);
			//System.out.println("New Renter balance: " + adjustedRenterbalance);
			
			this.resolveClaim(); // change the claim state to resolved
			
			System.out.println("New Renter Rent for this booking: " + renterTripRent);
			System.out.println("Adjusted Renter balance: " + adjustedRenterbalance);
			
			System.out.println("New Owner Rent for this booking: " + ownerTripRent);
			System.out.println("Adjusted Owner balance: " + adjustedOwnerbalance);
			
		}	
	}
}



