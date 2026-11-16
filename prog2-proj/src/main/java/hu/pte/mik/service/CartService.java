package hu.pte.mik.service;

import hu.pte.mik.domain.ShoppingCart;
import hu.pte.mik.exception.UnsafeCartException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartService {
    private final List<ShoppingCart> carts = new ArrayList<>();

    // Új kocsi hozzáadása a rendszerhez
    public void addCart(ShoppingCart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("A hozzáadandó kocsi nem lehet null!");
        }
        carts.add(cart);
    }

    // Az összes kocsi lekérdezése (defenzív másolattal)
    public List<ShoppingCart> getAllCarts() {
        return new ArrayList<>(carts);
    }

    // Kocsi ellenőrzése a megadott stratégia alapján (ha megbukik, kivételt dob)
    public void inspectAndValidate(ShoppingCart cart, InspectionStrategy strategy) throws UnsafeCartException {
        if (!strategy.isPass(cart)) {
            throw new UnsafeCartException(
                    String.format("VESZÉLYES KOCSI! A(z) %s azonosítójú kocsi nem felelt meg a teszten! (Biztonsági index: %.1f%%)",
                            cart.getId(),
                            cart.calculateSafetyIndex())
            );
        }
    }

    // Kocsik rendezése a biztonsági indexük alapján (csökkenő sorrendben)
    public List<ShoppingCart> getCartsSortedBySafety() {
        List<ShoppingCart> sortedList = new ArrayList<>(carts);
        Collections.sort(sortedList); // A ShoppingCart Comparable felületét használja
        return sortedList;
    }

    // Csak a szervizre szoruló kocsik kigyűjtése
    public List<ShoppingCart> getCartsNeedingMaintenance() {
        List<ShoppingCart> maintenanceList = new ArrayList<>();
        for (ShoppingCart cart : carts) {
            if (cart.needsMaintenance()) {
                maintenanceList.add(cart);
            }
        }
        return maintenanceList;
    }
}