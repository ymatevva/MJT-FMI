package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {

    private int buildableArea;
    private Map<String, E> buildings;

    public Plot(int buildableArea) {
        this.buildableArea = buildableArea;
        this.buildings = new HashMap<>();
    }

    /**
     * Constructs a buildable on the plot.
     *
     * @param address   the address where the buildable should be constructed.
     * @param buildable the buildable that should be constructed on the given address.
     * @throws IllegalArgumentException        if the address is null or blank.
     * @throws IllegalArgumentException        if the buildable is null.
     * @throws BuildableAlreadyExistsException if the address is already occupied on the plot.
     * @throws InsufficientPlotAreaException   if the required area exceeds the remaining plot area.
     */
    @Override
    public void construct(String address, E buildable) {

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("The address is null or is blank.");
        }

        if (buildable == null) {
            throw new IllegalArgumentException("The building is null.");
        }

        if (buildings.containsKey(address)) {
            throw new BuildableAlreadyExistsException(
                String.format("The address %s is already occupied.", buildings.get(address)));
        }

        if (buildableArea < buildable.getArea()) {
            throw new InsufficientPlotAreaException("The area for the building to be built is not enough.");
        }

        buildings.put(address, buildable);
        buildableArea -= buildable.getArea();
    }


    /**
     * Constructs multiple buildables on the plot.
     * This method ensures that either all operations are successfully completed
     * or no changes are made to the plot's state.
     *
     * @param buildables a Map containing the addresses and corresponding buildable entities.
     * @throws IllegalArgumentException        if the map of buildables is null, empty.
     * @throws BuildableAlreadyExistsException if any of the addresses is already occupied on the plot.
     * @throws InsufficientPlotAreaException   if the combined area of the provided buildables exceeds
     *                                         the remaining plot area.
     */

    @Override
    public void constructAll(Map<String, E> buildables) {

        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("The buildables map is null or empty.");
        }

        if (!areAllAddressesEmpty(buildables)) {
            throw new BuildableAlreadyExistsException("The area is not available for construction.");
        }

        double areaNeeded = getTotalAreaOfBuildings(buildables);

        if (buildableArea < areaNeeded) {
            throw new InsufficientPlotAreaException(
                "The area available for construction of the buildings is not enough.");
        }

        for (Map.Entry<String, E> mappedBuilding : buildables.entrySet()) {
            buildings.put(mappedBuilding.getKey(), mappedBuilding.getValue());
        }

        // or we can use buildings.putAll(buildables)
        buildableArea -= areaNeeded;
    }

    private boolean areAllAddressesEmpty(Map<String, E> buildables) {

        for (var address : buildables.keySet()) {
            if (buildings.containsKey(address)) {
                return false;
            }
        }
        return true;
    }

    private double getTotalAreaOfBuildings(Map<String, E> buildables) {

        double totalArea = 0;

        for (Map.Entry<String, E> mappedBuilding : buildables.entrySet()) {
            totalArea += mappedBuilding.getValue().getArea();
        }

        return totalArea;
    }

    /**
     * Demolishes a buildable from the plot.
     *
     * @param address the address of the buildable which should be demolished.
     * @throws IllegalArgumentException   if the provided address is null or blank.
     * @throws BuildableNotFoundException if buildable with such address does not exist on the plot.
     */
    @Override
    public void demolish(String address) {

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("The address is null or is blank.");
        }

        if (!buildings.containsKey(address)) {
            throw new BuildableNotFoundException(
                String.format("The building with address %s is not constructed yet.", address));
        }

        buildableArea += buildings.get(address).getArea();
        buildings.remove(address);
    }

    @Override
    public void demolishAll() {
        buildableArea += getTotalAreaOfBuildings(buildings);
        buildings.clear();
    }

    /**
     * Retrieves all buildables present on the plot.
     *
     * @return An unmodifiable copy of the buildables present on the plot.
     */
    @Override
    public Map<String,E> getAllBuildables() {
        return Map.copyOf(buildings);
    }

    @Override
    public int getRemainingBuildableArea() {
        return buildableArea;
    }
}
