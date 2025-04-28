


package temperature.demo.model;



public class Mesure {
    public double temperature;
    public double humidite;

    public Mesure() {}

    public Mesure(double temperature, double humidite) {
        this.temperature = temperature;
        this.humidite = humidite;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidite() {
        return humidite;
    }

    public void setHumidite(double humidite) {
        this.humidite = humidite;
    }
}

