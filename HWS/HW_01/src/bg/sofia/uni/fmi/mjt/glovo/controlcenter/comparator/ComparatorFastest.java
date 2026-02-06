package bg.sofia.uni.fmi.mjt.glovo.controlcenter.comparator;

import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;

import java.util.Comparator;

public class ComparatorFastest implements Comparator<DeliveryInfo> {
    @Override
    public int compare(DeliveryInfo o1, DeliveryInfo o2) {
        int compRes = Integer.compare(o1.estimatedTime(), o2.estimatedTime());
        if (compRes != 0) {
            return compRes;
        }

        compRes = Double.compare(o1.price(), o2.price());
        if (compRes != 0) {
            return compRes;
        }

        return o1.deliveryType().compareTo(o2.deliveryType());
    }
}
