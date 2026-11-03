package hu.pte.mik.service;

import hu.pte.mik.domain.ShoppingCart;

public interface InspectionStrategy {
    boolean isPass(ShoppingCart cart);
}