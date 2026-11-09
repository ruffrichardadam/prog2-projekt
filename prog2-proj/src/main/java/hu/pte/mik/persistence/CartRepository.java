package hu.pte.mik.persistence;

import hu.pte.mik.domain.HeavyDutyCart;
import hu.pte.mik.domain.ShoppingCart;
import hu.pte.mik.domain.StandardCart;
import hu.pte.mik.domain.Wheel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private final String filePath;

    public CartRepository(String filePath) {
        this.filePath = filePath;
    }

    // 1. Kocsik mentése fájlba (try-with-resources szerkezettel)
    public void saveAll(List<ShoppingCart> carts) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ShoppingCart cart : carts) {
                // Megkülönböztetjük a kocsi típusát: STANDARD vagy HEAVY
                String type = (cart instanceof HeavyDutyCart) ? "HEAVY" : "STANDARD";

                // Sor formátuma: TÍPUS;ID;KEREKEK_KOPÁSAI_VESSZŐVEL_SEPARÁLVA
                StringBuilder wheelsData = new StringBuilder();
                List<Wheel> wheels = cart.getWheels();
                for (int i = 0; i < wheels.size(); i++) {
                    wheelsData.append(wheels.get(i).getWearPercentage());
                    if (i < wheels.size() - 1) {
                        wheelsData.append(",");
                    }
                }

                writer.write(String.format("%s;%s;%s", type, cart.getId(), wheelsData.toString()));
                writer.newLine();
            }
        }
    }

    // 2. Kocsik betöltése fájlból (try-with-resources szerkezettel)
    public List<ShoppingCart> loadAll() throws IOException {
        List<ShoppingCart> loadedCarts = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return loadedCarts; // Ha még nem létezik a fájl, üres listát adunk vissza
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length < 3) continue;

                String type = parts[0];
                String id = parts[1];
                String[] wearStrings = parts[2].split(",");

                List<Wheel> wheels = new ArrayList<>();
                for (String wearStr : wearStrings) {
                    double wear = Double.parseDouble(wearStr);
                    wheels.add(new Wheel(wear));
                }

                ShoppingCart cart;
                if ("HEAVY".equalsIgnoreCase(type)) {
                    cart = new HeavyDutyCart(id, wheels);
                } else {
                    cart = new StandardCart(id, wheels);
                }

                loadedCarts.add(cart);
            }
        }

        return loadedCarts;
    }
}