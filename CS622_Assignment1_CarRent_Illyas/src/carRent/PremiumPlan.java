package carRent;

public class PremiumPlan extends InsurancePlan {
	private int deductable = 250;
	private int costPerTrip = 50;
	private int policyNumber;
	
	public PremiumPlan () {
		super();
		InsurancePlan.planID ++;
		this.setPolicyNumber(InsurancePlan.planID);
	}

	public int getPolicyNumber() {
		return policyNumber;
	}
	public int getDeductable() {
		return deductable;
	}

	public int getCostPerTrip() {
		return costPerTrip;
	}
	
	public void setDeductable(int deductable) {
		this.deductable = deductable;
	}

	public void setPolicyNumber(int policyNumber) {
		this.policyNumber = policyNumber;
	}

	public void setCostPerTrip(int costPerTrip) {
		this.costPerTrip = costPerTrip;
	}

	@Override
	public String toString() {
		return "PremiumPlan [deductable=" + deductable + ", costPerTrip=" + costPerTrip + ", policyNumber="
				+ policyNumber + "]";
	}

}
