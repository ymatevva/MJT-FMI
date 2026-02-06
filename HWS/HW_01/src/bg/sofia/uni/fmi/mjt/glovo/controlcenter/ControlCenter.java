package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.comparator.ComparatorCheapest;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.routegenerator.RouteGenerator;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.comparator.ComparatorFastest;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class ControlCenter implements ControlCenterApi {

    private final MapEntity[][] cityMap;
    private final Set<MapEntity> deliveryGuys;
    public static final int NO_CONSTRAINT_FLAG = -1;

    public ControlCenter(char[][] mapLayout) {

        this.deliveryGuys = new HashSet<>();
        this.cityMap = new MapEntity[mapLayout.length][];

        for (int i = 0; i < mapLayout.length; i++) {

            cityMap[i] = new MapEntity[mapLayout[i].length];

            for (int j = 0; j < mapLayout[i].length; j++) {

                Location currLocation = new Location(i, j);
                MapEntityType entityType = getMapEntityType(mapLayout, i, j);

                cityMap[i][j] = new MapEntity(currLocation, entityType);

                if (entityType.equals(MapEntityType.DELIVERY_GUY_CAR) ||
                    entityType.equals(MapEntityType.DELIVERY_GUY_BIKE)) {
                    deliveryGuys.add(cityMap[i][j]);
                }
            }
        }
    }

    private static MapEntityType getMapEntityType(char[][] mapLayout, int i, int j) {
        MapEntityType entityType = null;

        switch (mapLayout[i][j]) {
            case 'A' -> entityType = MapEntityType.DELIVERY_GUY_CAR;
            case 'R' -> entityType = MapEntityType.RESTAURANT;
            case 'C' -> entityType = MapEntityType.CLIENT;
            case '#' -> entityType = MapEntityType.WALL;
            case '.' -> entityType = MapEntityType.ROAD;
            case 'B' -> entityType = MapEntityType.DELIVERY_GUY_BIKE;
        }
        return entityType;
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation, double maxPrice,

                                               int maxTime, ShippingMethod shippingMethod) {

        RouteGenerator routeGenerator = new RouteGenerator(cityMap);

        if (shippingMethod.equals(ShippingMethod.CHEAPEST)) {
            TreeSet<DeliveryInfo> routesList = new TreeSet<>(new ComparatorCheapest());
            routesList.addAll(routeGenerator.gatherRoutes(restaurantLocation, clientLocation, deliveryGuys));
            return findCheapestRoute(routesList, maxTime);
        }

        if (shippingMethod.equals(ShippingMethod.FASTEST)) {
            TreeSet<DeliveryInfo> routesList = new TreeSet<>(new ComparatorFastest());
            routesList.addAll(routeGenerator.gatherRoutes(restaurantLocation, clientLocation, deliveryGuys));
            return findFastestRoute(routesList, maxTime);
        }

        return null;
    }

    private DeliveryInfo findFastestRoute(TreeSet<DeliveryInfo> routesList, double priceLimit) {
        if (priceLimit != NO_CONSTRAINT_FLAG) {
            for (var route : routesList) {
                if (route.price() < priceLimit) {
                    return new DeliveryInfo(route.deliveryGuyLocation(), route.price(), route.estimatedTime(),
                        route.deliveryType());
                }
            }
        } else {
            return new DeliveryInfo(routesList.first().deliveryGuyLocation(), routesList.first().price(),
                routesList.first().estimatedTime(),
                routesList.first().deliveryType());
        }
        return null;
    }

    private DeliveryInfo findCheapestRoute(TreeSet<DeliveryInfo> routesList, int timeLimit) {
        if (timeLimit != NO_CONSTRAINT_FLAG) {
            for (var route : routesList) {
                if (route.estimatedTime() < timeLimit) {
                    return new DeliveryInfo(route.deliveryGuyLocation(), route.price(), route.estimatedTime(),
                        route.deliveryType());
                }
            }
        } else {
            return new DeliveryInfo(routesList.first().deliveryGuyLocation(), routesList.first().price(),
                routesList.first().estimatedTime(),
                routesList.first().deliveryType());
        }
        return null;
    }

    @Override
    public MapEntity[][] getLayout() {
        return cityMap;
    }
}
