package hu.pte.mik.domain;

import java.util.List;

public class HeavyDutyCart extends ShoppingCart {

    public HeavyDutyCart(String id, List<Wheel> wheels) {
        super(id, wheels);
    }

    @Override
    public double calculateSafetyIndex() {
        double minAccuracy = 100.0;
        double maxWear = 0.0;

        // A nagy teherbírású kocsinál a LEGROSSZABB kerék állapota a szűk keresztmetszet
        for (Wheel wheel : getWheels()) {
            if (wheel.getSteeringAccuracy() < minAccuracy) {
                minAccuracy = wheel.getSteeringAccuracy();
            }
            if (wheel.getWearLevel() > maxWear) {
                maxWear = wheel.getWearLevel();
            }
        }

        // A legrosszabb kerék kanyarodása 70%-os súllyal, a legrosszabb kopása 30%-os súllyal számít
        double index = (minAccuracy * 0.7) + ((100.0 - maxWear) * 0.3);
        return Math.max(0.0, Math.min(100.0, index));
    }
}