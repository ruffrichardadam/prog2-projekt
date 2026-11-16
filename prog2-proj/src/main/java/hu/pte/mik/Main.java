package hu.pte.mik;

import hu.pte.mik.persistence.CartRepository;
import hu.pte.mik.service.CartService;
import hu.pte.mik.ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        CartRepository repository = new CartRepository("carts.txt");
        CartService service = new CartService();

        ConsoleMenu menu = new ConsoleMenu(service, repository);
        menu.start();
    }
}