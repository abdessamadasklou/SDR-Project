package temperature.demo.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import temperature.demo.model.Mesure;

@Service
public class RmiServiceClient {

    public List<Mesure> getDernieresMesures() {
        return Arrays.asList(
            new Mesure(33.5, 55.0),
            new Mesure(31.2, 45.0)
        );
    }

    public List<Mesure> getAlertes() {
        return getDernieresMesures().stream()
            .filter(m -> m.getTemperature() < 10 || m.getTemperature() > 30)
            .toList();
    }
}
