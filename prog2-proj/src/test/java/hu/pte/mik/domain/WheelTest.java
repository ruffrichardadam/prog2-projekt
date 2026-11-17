package hu.pte.mik.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WheelTest {

    @Test
    public void testWheelValidCreation() {
        // Teszteljük, hogy helyes adatokkal hibátlanul létrejön a kerék
        Wheel wheel = new Wheel(10.0, 90.0);
        assertEquals(10.0, wheel.getWearLevel(), 0.001);
        assertEquals(90.0, wheel.getSteeringAccuracy(), 0.001);
    }

    @Test
    public void testWheelInvalidWearThrowsException() {
        // Teszteljük, hogy érvénytelen (100% feletti) kopás esetén kivételt dob a validáció
        assertThrows(IllegalArgumentException.class, () -> {
            new Wheel(150.0, 80.0);
        });
    }
}