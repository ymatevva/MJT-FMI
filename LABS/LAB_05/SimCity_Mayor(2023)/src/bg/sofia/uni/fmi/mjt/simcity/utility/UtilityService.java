package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {

    private final Map<UtilityType, Double> taxRates;

    public UtilityService(Map<UtilityType, Double> taxRates) {
         this.taxRates = taxRates;
    }


    /**
     * Retrieves the costs of a specific utility for a given billable building.
     *
     * @param utilityType The utility type used for the costs' calculation.
     * @param billable    The billable building for which the utility costs will be calculated.
     * @return The cost of the specified utility for the billable building.
     * @throws IllegalArgumentException if the utility or billable is null.
     */

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null) {
            throw new IllegalArgumentException("The utility type is null.");
        }

        if (billable == null) {
            throw new IllegalArgumentException("The building is null.");
        }

        switch(utilityType) {
            case WATER -> {
                return getWaterCosts(billable);
            }
            case NATURAL_GAS -> {
                return getNaturalGasCosts(billable);
            }
            case ELECTRICITY -> {
                return getElectricityCosts(billable);
            }
            default -> {
                return 0.0;
            }
        }
    }

    private <T extends Billable> double getWaterCosts(T billable) {
        return taxRates.get(UtilityType.WATER) * billable.getWaterConsumption();
    }

    private  <T extends Billable> double getElectricityCosts(T billable) {
        return taxRates.get(UtilityType.ELECTRICITY) * billable.getElectricityConsumption();
    }

    private  <T extends Billable> double getNaturalGasCosts(T billable) {
        return taxRates.get(UtilityType.NATURAL_GAS) * billable.getNaturalGasConsumption();
    }

    /**
     * Calculates the total utility costs for a given billable building.
     *
     * @param billable The billable building for which total utility costs are calculated.
     * @return The total cost of all utilities for the billable building.
     * @throws IllegalArgumentException if the billable is null.
     */

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException("the building is null.");
        }

        return getWaterCosts(billable) +
           getElectricityCosts(billable) +
            getNaturalGasCosts(billable);
    }

    /**
     * Computes the absolute difference in utility costs between two billable buildings for each utility type.
     *
     * @param firstBillable  The first billable building used for the cost comparison.
     * @param secondBillable The second billable building used for the cost comparison.
     * @return An unmodifiable map containing the absolute difference in costs between the buildings for each
     * utility.
     * @throws IllegalArgumentException if any billable is null.
     */
    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null) {
            throw new IllegalArgumentException("The first building is null.");
        }

        if (secondBillable == null) {
            throw new IllegalArgumentException("The second building is null.");
        }

        Map<UtilityType,Double> costsDifferences = new HashMap<>();

        double absDiffWater = Math.abs(firstBillable.getWaterConsumption() - secondBillable.getWaterConsumption());
        double absDiffElectricity = Math.abs(firstBillable.getElectricityConsumption() - secondBillable.getElectricityConsumption());
        double absDiffGas = Math.abs(firstBillable.getNaturalGasConsumption() - secondBillable.getNaturalGasConsumption());

        costsDifferences.put(UtilityType.WATER, absDiffWater);
        costsDifferences.put(UtilityType.ELECTRICITY, absDiffElectricity);
        costsDifferences.put(UtilityType.NATURAL_GAS, absDiffGas);

        return costsDifferences;
    }
}
