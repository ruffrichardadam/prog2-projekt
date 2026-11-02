package hu.pte.mik.domain;

import java.util.List;

public class StandardCart extends ShoppingCart {

    public StandardCart(String id, List<Wheel> wheels) {
        super(id, wheels);
    }

    @Override
    public double calculateSafetyIndex() {
        double totalWear = 0;
        double totalAccuracy = 0;

        for (Wheel wheel : getWheels()) {
            totalWear += wheel.getWearLevel();
            totalAccuracy += wheel.getSteeringAccuracy();
        }

        double avgWear = totalWear / 4.0;
        double avgAccuracy = totalAccuracy / 4.0;

        // A normál kocsinál az átlagos kanyarodási pontosság 60%-ban, a kopásmentesség (100 - kopás) 40%-ban számít
        double index = (avgAccuracy * 0.6) + ((100.0 - avgWear) * 0.4);
        return Math.max(0.0, Math.min(100.0, index));
    }
}