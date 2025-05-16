package temperature.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "mesures")
public class Mesure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double temperature;
    private double humidite;

    @Column(name = "capteur_name")
    private String capteurName;

    @Column(name = "insertion_time")
    private LocalDateTime insertionTime;

    public Mesure() {}

    public Mesure(double temperature, double humidite, String capteurName) {
        this.temperature = temperature;
        this.humidite = humidite;
        this.capteurName = capteurName;
        this.insertionTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCapteurName() {
        return capteurName;
    }

    public void setCapteurName(String capteurName) {
        this.capteurName = capteurName;
    }

    public LocalDateTime getInsertionTime() {
        return insertionTime;
    }

    public void setInsertionTime(LocalDateTime insertionTime) {
        this.insertionTime = insertionTime;
    }
}
