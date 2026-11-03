package hu.pte.mik.service;

import hu.pte.mik.domain.ShoppingCart;

public class PermissiveInspectionStrategy implements InspectionStrategy {

    @Override
    public boolean isPass(ShoppingCart cart) {
        // Megengedő teszt: elég, ha nem szorul azonnali szervizre (index >= 60.0%)
        return !cart.needsMaintenance();
    }
}