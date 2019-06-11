package carRent;

import java.io.Serializable;

public abstract class InsurancePlan implements Serializable {
	public static int planID = 0;
	public abstract int getCostPerTrip();
}
