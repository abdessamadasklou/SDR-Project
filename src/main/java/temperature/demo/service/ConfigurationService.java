package temperature.demo.service;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    private double minTemperatureThreshold = 10.0;
    private double maxTemperatureThreshold = 30.0;
    private String alertEmail = "dds3157f@gmail.com";

    public double getMinTemperatureThreshold() {
        return minTemperatureThreshold;
    }

    public void setMinTemperatureThreshold(double minTemperatureThreshold) {
        this.minTemperatureThreshold = minTemperatureThreshold;
    }

    public double getMaxTemperatureThreshold() {
        return maxTemperatureThreshold;
    }

    public void setMaxTemperatureThreshold(double maxTemperatureThreshold) {
        this.maxTemperatureThreshold = maxTemperatureThreshold;
    }

    public String getAlertEmail() {
        return alertEmail;
    }

    public void setAlertEmail(String alertEmail) {
        this.alertEmail = alertEmail;
    }
}
