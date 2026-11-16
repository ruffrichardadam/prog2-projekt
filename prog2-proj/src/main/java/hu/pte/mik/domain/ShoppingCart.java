package hu.pte.mik.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class ShoppingCart implements Comparable<ShoppingCart> {
    private final String id;
    private final List<Wheel> wheels;

    public ShoppingCart(String id, List<Wheel> wheels) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("A kocsi azonosítója nem lehet üres!");
        }
        if (wheels == null || wheels.size() != 4) {
            throw new IllegalArgumentException("Egy bevásárlókocsinak pontosan 4 kerékkel kell rendelkeznie!");
        }
        this.id = id;
        this.wheels = new ArrayList<>(wheels);
    }

    public String getId() {
        return id;
    }

    public List<Wheel> getWheels() {
        return new ArrayList<>(wheels); // Defenzív másolat a tokba zárás (enkapszuláció) védelméért
    }

    // Absztrakt metódus: a gyerekosztályok kötelezően megvalósítják a saját számítási logikájukat
    public abstract double calculateSafetyIndex();

    // Szervizre szorul a kocsi, ha a biztonsági indexe 60% alá esik
    public boolean needsMaintenance() {
        return calculateSafetyIndex() < 60.0;
    }

    // A Comparable interfész megvalósítása: biztonsági index alapján csökkenő sorrendbe rendezünk
    @Override
    public int compareTo(ShoppingCart other) {
        return Double.compare(other.calculateSafetyIndex(), this.calculateSafetyIndex());
    }

    @Override
    public String toString() {
        return String.format("%s [ID=%s, SafetyIndex=%.1f%%, Status=%s]",
                getClass().getSimpleName(),
                id,
                calculateSafetyIndex(),
                needsMaintenance() ? "SZERVIZRE VÁR" : "HASZNÁLHATÓ");
    }
}