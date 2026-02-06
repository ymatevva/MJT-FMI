package bg.sofia.uni.fmi.mjt.glovo.controlcenter.routegenerator;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class RouteGenerator {

    private final MapEntity[][] cityMap;

    public RouteGenerator(MapEntity[][] cityMap) {
        this.cityMap = new MapEntity[cityMap.length][];
        for (int i = 0; i < cityMap.length; i++) {
            this.cityMap[i] = new MapEntity[cityMap[i].length];
            System.arraycopy(cityMap[i], 0, this.cityMap[i], 0, cityMap[i].length);
        }
    }

    public Set<DeliveryInfo> gatherRoutes(Location restaurantLocation, Location clientLocation,
                                          Set<MapEntity> deliveryGuys) {

        Set<DeliveryInfo> routes = new HashSet<>();

        for (var deliveryGuy : deliveryGuys) {

            int distanceToRestaurant = findShortestRoute(deliveryGuy.getLocation(), restaurantLocation);
            if (distanceToRestaurant == -1) {
                continue;
            }

            int distanceToClient = findShortestRoute(restaurantLocation, clientLocation);
            if (distanceToClient == -1) {
                continue;
            }

            DeliveryInfo currDelivery =
                getDeliveryInfo(deliveryGuy, distanceToRestaurant, distanceToClient);

            routes.add(currDelivery);
        }
        return routes;
    }

    private DeliveryInfo getDeliveryInfo(MapEntity deliveryGuy, int distanceToRestaurant, int distanceToClient) {
        int totalDistance = distanceToRestaurant + distanceToClient;

        DeliveryType routeDeliveryType =
            deliveryGuy.getType().equals(MapEntityType.DELIVERY_GUY_CAR) ? DeliveryType.CAR :
                DeliveryType.BIKE;

        double routePrice = totalDistance * routeDeliveryType.getPricePerKm();
        int routeTime = totalDistance * routeDeliveryType.getTimePerKm();

        DeliveryInfo currDelivery =
            new DeliveryInfo(deliveryGuy.getLocation(), routePrice, routeTime, routeDeliveryType);
        return currDelivery;
    }

    private Integer findShortestRoute(Location start, Location end) {
        boolean[][] visited = new boolean[cityMap.length][cityMap[0].length];

        Queue<Location> myQ = new LinkedList<>();
        int distance = 0;
        myQ.offer(start);
        visited[start.x()][start.y()] = true;

        while (!myQ.isEmpty()) {

            int currQSize = myQ.size();

            for (int i = 0; i < currQSize; i++) {
                Location currLocation = myQ.poll();

                if (currLocation.equals(end)) {
                    return distance;
                }

                for (var neighbour : getNeighbours(currLocation)) {
                    if (validateNeighbour(neighbour, visited)) {
                        myQ.offer(neighbour);
                        visited[neighbour.x()][neighbour.y()] = true;
                    }
                }
            }
            distance++;
        }
        return -1;
    }

    private boolean validateNeighbour(Location neighbour, boolean[][] visited) {
        int neighbourX = neighbour.x();
        int neighbourY = neighbour.y();

        return (neighbourX >= 0 && neighbourY >= 0 && neighbourX < cityMap.length && neighbourY < cityMap[0].length
            && !visited[neighbourX][neighbourY] &&
            !(cityMap[neighbourX][neighbourY].getType().equals(MapEntityType.WALL)));
    }

    private List<Location> getNeighbours(Location currLocation) {
        return List.of(new Location(currLocation.x() + 1, currLocation.y()),
            new Location(currLocation.x(), currLocation.y() + 1),
            new Location(currLocation.x() - 1, currLocation.y()),
            new Location(currLocation.x(), currLocation.y() - 1));
    }
}
