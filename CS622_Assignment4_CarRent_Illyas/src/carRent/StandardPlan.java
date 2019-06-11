package carRent;

public class StandardPlan extends InsurancePlan {
	private int deductable = 300;
	private int costPerTrip = 35;
	private int policyNumber;
	
	public StandardPlan () {
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
		return "StandardPlan [deductable=" + deductable + ", costPerTrip=" + costPerTrip + ", policyNumber="
				+ policyNumber + "]";
	}

}
