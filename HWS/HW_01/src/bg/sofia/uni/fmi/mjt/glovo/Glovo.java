package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidLocationException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidPriceConstraint;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidTimeConstraint;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

import static bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter.NO_CONSTRAINT_FLAG;

public class Glovo implements GlovoApi {

    private final ControlCenter controlCenter;

    public Glovo(char[][] mapLayout) {
        controlCenter = new ControlCenter(mapLayout);
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {

        return getDelivery(client, restaurant, foodItem, NO_CONSTRAINT_FLAG, NO_CONSTRAINT_FLAG,
            ShippingMethod.CHEAPEST);
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {

        return getDelivery(client, restaurant, foodItem, NO_CONSTRAINT_FLAG, NO_CONSTRAINT_FLAG,
            ShippingMethod.FASTEST);
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant, String foodItem,
                                                 double maxPrice) throws NoAvailableDeliveryGuyException {
        validatePrice(maxPrice);
        return getDelivery(client, restaurant, foodItem, maxPrice, NO_CONSTRAINT_FLAG,
            ShippingMethod.FASTEST);
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant, String foodItem,
                                                       int maxTime) throws NoAvailableDeliveryGuyException {
        validateTime(maxTime);
        return getDelivery(client, restaurant, foodItem, NO_CONSTRAINT_FLAG, maxTime,
            ShippingMethod.CHEAPEST);
    }

    private Delivery getDelivery(MapEntity client, MapEntity restaurant, String foodItem,
                                 double maxPrice, int maxTime, ShippingMethod shippingMethod)
        throws NoAvailableDeliveryGuyException {

        validateArguments(client, restaurant, foodItem, shippingMethod);
        validateLocations(client, restaurant);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.getLocation(),
            client.getLocation(), maxPrice, maxTime,
            shippingMethod);

        checkAvailableDeliveryPersonnel(deliveryInfo);

        return new Delivery(client.getLocation(), restaurant.getLocation(),
            deliveryInfo.deliveryGuyLocation(), foodItem, deliveryInfo.price(), deliveryInfo.estimatedTime());
    }

    private void validateArguments(MapEntity client, MapEntity restaurant, String foodItem,
                                   ShippingMethod shippingMethod) {
        if (client == null) {
            throw new IllegalArgumentException("The client argument is null.");
        }

        if (restaurant == null) {
            throw new IllegalArgumentException("The restaurant argument is null.");
        }

        if (foodItem == null) {
            throw new IllegalArgumentException("The food item is null.");
        }

        if (shippingMethod == null) {
            throw new IllegalArgumentException("The shipping method is null.");
        }
    }

    private void validateLocations(MapEntity client, MapEntity restaurant) {

        validateBorders(client);
        validateBorders(restaurant);

        Location clientLocation = client.getLocation();
        Location restaurantLocation = restaurant.getLocation();

        MapEntityType clientLocationOnMapType =
            controlCenter.getLayout()[clientLocation.x()][clientLocation.y()].getType();

        MapEntityType restaurantLocationOnMapType =
            controlCenter.getLayout()[restaurantLocation.x()][restaurantLocation.y()].getType();

        if (clientLocationOnMapType != MapEntityType.CLIENT) {
            throw new InvalidOrderException("The location of the client does not match a client location on the map.");
        }

        if (restaurantLocationOnMapType !=
            MapEntityType.RESTAURANT) {
            throw new InvalidOrderException("The location of the restaurant does not match a restaurant " +
                "location on the map.");
        }
    }

    private void validateBorders(MapEntity mapEntity) {
        if (mapEntity.getLocation().x() < 0 || mapEntity.getLocation().x() >= controlCenter.getLayout().length
            || mapEntity.getLocation().y() < 0 || mapEntity.getLocation().y() >= controlCenter.getLayout()[0].length) {
            throw new InvalidLocationException("The location is out of the map's borders.");
        }
    }

    private void validatePrice(double maxPrice) {
        if (maxPrice <= 0) {
            throw new InvalidPriceConstraint("The maximum price should be a positive number.");
        }
    }

    private void validateTime(double maxTime) {
        if (maxTime <= 0) {
            throw new InvalidTimeConstraint("The maximum time should be a positive number.");
        }
    }

    private void checkAvailableDeliveryPersonnel(DeliveryInfo deliveryInfo) throws NoAvailableDeliveryGuyException {
        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("There are no available delivery personnel to this location.");
        }
    }
}
