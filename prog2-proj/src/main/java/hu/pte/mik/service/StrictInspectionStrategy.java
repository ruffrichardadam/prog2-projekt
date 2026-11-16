package hu.pte.mik.service;

import hu.pte.mik.domain.ShoppingCart;

public class StrictInspectionStrategy implements InspectionStrategy {

    @Override
    public boolean isPass(ShoppingCart cart) {
        // Szigorú teszt: a biztonsági indexnek legalább 75.0%-nak kell lennie
        return cart.calculateSafetyIndex() >= 75.0;
    }
}