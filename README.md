# Shopping Cart Domain Model (Prog2 Projekt)

Egy objektumorientált Java-alkalmazás, amely egy bevásárlókocsi-kezelő rendszert valósít meg konzolos menüin keresztül, robusztus kivételkezeléssel és fájlkezeléssel.

##  Főbb Jellemzők és Architektúra

- **Domain Model:** Különböző típusú bevásárlókocsik (`StandardCart`, `HeavyDutyCart`) és kerekek (`Wheel`) kezelése egységes logika mentén.
- **Stratégia & Validáció:** Egyedi kivételkezelés (`IllegalArgumentException`, `NumberFormatException`) az adatok helyességének biztosítására (pl. kerék kopás és pontosság 0-100% között).
- **Adatkezelés (Persistence):** Kocsik mentése és betöltése külső fájlba (`carts.txt`).
- **Interaktív Konzolos Menü (`ConsoleMenu`):** Felhasználóbarát, Top-Down tervezésű menürendszer `Scanner` alapú beolvasással és `try-catch` védőhálóval.

##  Csomagstruktúra (`hu.pte.mik`)

- `domain`: A fő üzleti modellek (`ShoppingCart`, `StandardCart`, `HeavyDutyCart`, `Wheel`).
- `exception`: Egyedi kivételek a hibás adatok kezelésére.
- `persistence`: Adattárolásért és fájlkezelésért felelős osztályok (`CartRepository`).
- `service`: Az üzleti logikát összefogó réteg (`CartService`).
- `ui`: A konzolos felület és a fő menü (`ConsoleMenu`).

## Futtatás

Nyisd meg a projektet egy tetszőleges Java IDE-ben (pl. IntelliJ IDEA), navigálj a `Main` osztályhoz, majd futtasd a `main` metódust.