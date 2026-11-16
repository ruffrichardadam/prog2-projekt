package hu.pte.mik.ui;

import hu.pte.mik.domain.HeavyDutyCart;
import hu.pte.mik.domain.ShoppingCart;
import hu.pte.mik.domain.StandardCart;
import hu.pte.mik.domain.Wheel;
import hu.pte.mik.exception.UnsafeCartException;
import hu.pte.mik.persistence.CartRepository;
import hu.pte.mik.service.CartService;
import hu.pte.mik.service.InspectionStrategy;
import hu.pte.mik.service.PermissiveInspectionStrategy;
import hu.pte.mik.service.StrictInspectionStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final Scanner scanner;
    private InspectionStrategy currentStrategy;
    private boolean strictMode = true;

    public ConsoleMenu(CartService cartService, CartRepository cartRepository) {
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.scanner = new Scanner(System.in);
        this.currentStrategy = new StrictInspectionStrategy();
    }

    public void start() {
        loadOnStartup();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addNewCart();
                case "2" -> listAllCarts();
                case "3" -> inspectAllCarts();
                case "4" -> listCartsSortedBySafety();
                case "5" -> listCartsNeedingMaintenance();
                case "6" -> toggleStrategy();
                case "7" -> saveCarts();
                case "0" -> {
                    saveCarts();
                    System.out.println("Kilépés a rendszerből. Viszlát!");
                    running = false;
                }
                default -> System.out.println("Érvénytelen opció! Kérlek 0 és 7 közötti számot adj meg.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n==========================================");
        System.out.println("  BEVÁSÁRLÓKOCSI-KERÉK TESZTELŐ RENDSZER  ");
        System.out.println("==========================================");
        System.out.println("Aktuális stratégia: " + (strictMode ? "SZIGORÚ (Strict)" : "MEGENGEDŐ (Permissive)"));
        System.out.println("------------------------------------------");
        System.out.println("1. Új bevásárlókocsi felvétele");
        System.out.println("2. Összes kocsi listázása");
        System.out.println("3. Kocsik állapotának ellenőrzése (Teszt)");
        System.out.println("4. Kocsik listázása biztonsági index szerint rendezve");
        System.out.println("5. Szervizre szoruló kocsik listázása");
        System.out.println("6. Ellenőrzési stratégia váltása");
        System.out.println("7. Adatok mentése fájlba");
        System.out.println("0. Mentés és kilépés");
        System.out.print("Válassz egy menüpontot: ");
    }

    private void addNewCart() {
        System.out.print("Kocsi típusa (1 - Standard, 2 - HeavyDuty): ");
        String type = scanner.nextLine().trim();

        System.out.print("Kocsi azonosítója (pl. C-01): ");
        String id = scanner.nextLine().trim();

        List<Wheel> wheels = new ArrayList<>();
        System.out.println("Add meg a 4 kerék adatait!");

        try {
            for (int i = 1; i <= 4; i++) {
                System.out.print(i + ". kerék kopási szintje (0-100%): ");
                double wear = Double.parseDouble(scanner.nextLine().trim());

                System.out.print(i + ". kerék kanyarodási pontossága (0-100%): ");
                double accuracy = Double.parseDouble(scanner.nextLine().trim());

                wheels.add(new Wheel(wear, accuracy));
            }

            ShoppingCart cart = type.equals("2")
                    ? new HeavyDutyCart(id, wheels)
                    : new StandardCart(id, wheels);

            cartService.addCart(cart);
            System.out.println("Kocsi sikeresen hozzáadva: " + id);

        } catch (NumberFormatException e) {
            System.out.println("Hiba: Érvénytelen számformátum! A kocsi felvétele megszakítva.");
        } catch (IllegalArgumentException e) {
            System.out.println("Hiba az adatokban: " + e.getMessage());
        }
    }

    private void listAllCarts() {
        List<ShoppingCart> carts = cartService.getAllCarts();
        if (carts.isEmpty()) {
            System.out.println("Nincs rögzített bevásárlókocsi a rendszerben.");
            return;
        }
        System.out.println("\n--- Rögzített kocsik ---");
        for (ShoppingCart cart : carts) {
            System.out.println(cart);
        }
    }

    private void inspectAllCarts() {
        List<ShoppingCart> carts = cartService.getAllCarts();
        if (carts.isEmpty()) {
            System.out.println("Nincs ellenőrizhető kocsi.");
            return;
        }
        System.out.println("\n--- Kocsik ellenőrzése (" + (strictMode ? "Szigorú" : "Megengedő") + " mód) ---");
        for (ShoppingCart cart : carts) {
            try {
                cartService.inspectAndValidate(cart, currentStrategy);
                System.out.printf("[OK] %s - Használatra alkalmas (Biztonsági index: %.1f%%)%n",
                        cart.getId(), cart.calculateSafetyIndex());
            } catch (UnsafeCartException e) {
                System.out.println("[HIBÁS] " + e.getMessage());
            }
        }
    }

    private void listCartsSortedBySafety() {
        List<ShoppingCart> sorted = cartService.getCartsSortedBySafety();
        if (sorted.isEmpty()) {
            System.out.println("Nincs megjeleníthető kocsi.");
            return;
        }
        System.out.println("\n--- Kocsik biztonsági index szerint rendezve (legjobb elöl) ---");
        for (ShoppingCart cart : sorted) {
            System.out.println(cart);
        }
    }

    private void listCartsNeedingMaintenance() {
        List<ShoppingCart> maintenanceList = cartService.getCartsNeedingMaintenance();
        if (maintenanceList.isEmpty()) {
            System.out.println("Jelenleg egyetlen kocsi sem szorul szervizre!");
            return;
        }
        System.out.println("\n--- Szervizre szoruló kocsik (Biztonsági index < 60%) ---");
        for (ShoppingCart cart : maintenanceList) {
            System.out.println(cart);
        }
    }

    private void toggleStrategy() {
        strictMode = !strictMode;
        if (strictMode) {
            currentStrategy = new StrictInspectionStrategy();
            System.out.println("Stratégia átállítva: SZIGORÚ ellenőrzés.");
        } else {
            currentStrategy = new PermissiveInspectionStrategy();
            System.out.println("Stratégia átállítva: MEGENGEDŐ ellenőrzés.");
        }
    }

    private void saveCarts() {
        try {
            cartRepository.saveAll(cartService.getAllCarts());
            System.out.println("Adatok sikeresen elmentve a fájlba.");
        } catch (IOException e) {
            System.out.println("Hiba a mentés során: " + e.getMessage());
        }
    }

    private void loadOnStartup() {
        try {
            List<ShoppingCart> loaded = cartRepository.loadAll();
            for (ShoppingCart cart : loaded) {
                cartService.addCart(cart);
            }
            if (!loaded.isEmpty()) {
                System.out.println(loaded.size() + " db kocsi sikeresen betöltve a korábbi mentésből.");
            }
        } catch (IOException e) {
            System.out.println("Nem sikerült betölteni a korábbi adatokat: " + e.getMessage());
        }
    }
}