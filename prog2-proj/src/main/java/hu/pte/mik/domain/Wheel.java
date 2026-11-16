package hu.pte.mik.domain;

public class Wheel {
    private double wearLevel; // Kopási szint: 0.0 (új) - 100.0 (tönkrement)
    private double steeringAccuracy; // Kanyarodási pontosság: 0.0% - 100.0%

    public Wheel(double wearLevel, double steeringAccuracy) {
        setWearLevel(wearLevel);
        setSteeringAccuracy(steeringAccuracy);
    }

    public double getWearLevel() {
        return wearLevel;
    }

    public void setWearLevel(double wearLevel) {
        if (wearLevel < 0 || wearLevel > 100) {
            throw new IllegalArgumentException("A kopás mértéke 0 és 100% között kell legyen!");
        }
        this.wearLevel = wearLevel;
    }

    public double getSteeringAccuracy() {
        return steeringAccuracy;
    }

    public void setSteeringAccuracy(double steeringAccuracy) {
        if (steeringAccuracy < 0 || steeringAccuracy > 100) {
            throw new IllegalArgumentException("A pontosság 0 és 100% között kell legyen!");
        }
        this.steeringAccuracy = steeringAccuracy;
    }

    public boolean isUsable() {
        return wearLevel < 70.0 && steeringAccuracy > 50.0;
    }

    @Override
    public String toString() {
        return "Wheel{" +
                "wearLevel=" + wearLevel +
                "%, steeringAccuracy=" + steeringAccuracy +
                "%}";
    }
}