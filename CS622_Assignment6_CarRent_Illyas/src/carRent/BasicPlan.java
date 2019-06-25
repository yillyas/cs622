package carRent;

public class BasicPlan extends InsurancePlan {
	private int deductable = 500;
	private int costPerTrip = 25;
	private int policyNumber;
	public BasicPlan () {
		super();
		InsurancePlan.planID ++;
		this.setPolicyNumber(InsurancePlan.planID);
	}
	@Override
	public int getPolicyNumber() {
		return policyNumber;
	}
	public int getDeductable() {
		return deductable;
	}

	public void setDeductable(int deductable) {
		this.deductable = deductable;
	}

	public int getCostPerTrip() {
		return costPerTrip;
	}
	
	public void setPolicyNumber(int policyNumber) {
		this.policyNumber = policyNumber;
	}

	public void setCostPerTrip(int costPerTrip) {
		this.costPerTrip = costPerTrip;
	}

	@Override
	public String toString() {
		return "BasicPlan [deductable=" + deductable + ", costPerTrip=" + costPerTrip + ", policyNumber=" + policyNumber
				+ "]";
	}


}
