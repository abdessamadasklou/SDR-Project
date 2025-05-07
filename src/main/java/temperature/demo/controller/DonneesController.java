package temperature.demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import temperature.demo.service.RmiServiceClient;
import temperature.demo.model.Mesure;
import java.util.stream.IntStream;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.rmi.RemoteException;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class DonneesController {

    @Autowired
    private RmiServiceClient rmiServiceClient;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @GetMapping("/donnees")
    public List<Map<String, Object>> getDonnees() throws RemoteException {
        List<Mesure> mesures = rmiServiceClient.getDernieresMesures();
        List<Map<String, Object>> donnees = new ArrayList<>();

        IntStream.range(0, mesures.size()).forEach(i -> {
            Mesure m = mesures.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("capteurId", i + 1);
            map.put("temperature", m.getTemperature());
            map.put("humidite", m.getHumidite());
            // No timestamp available in Mesure, so omit or add null
            donnees.add(map);
        });

        return donnees;
    }

    @GetMapping("/alertes")
    public List<Map<String, Object>> getAlertes() throws RemoteException {
        List<Mesure> alertesMesures = rmiServiceClient.getAlertes();
        List<Map<String, Object>> alertes = new ArrayList<>();

        IntStream.range(0, alertesMesures.size()).forEach(i -> {
            Mesure m = alertesMesures.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("capteurId", i + 1);
            map.put("temperature", m.getTemperature());
            map.put("humidite", m.getHumidite());
            alertes.add(map);
        });

        return alertes;
    }

    @GetMapping("/donnees/stream")
    public SseEmitter streamDonnees() throws RemoteException {
        SseEmitter emitter = new SseEmitter();

        final var future = scheduler.scheduleAtFixedRate(() -> {
            try {
                List<Mesure> mesures = rmiServiceClient.getDernieresMesures();
                List<Map<String, Object>> donnees = new ArrayList<>();
                IntStream.range(0, mesures.size()).forEach(i -> {
                    Mesure m = mesures.get(i);
                    Map<String, Object> map = new HashMap<>();
                    map.put("capteurId", i + 1);
                    map.put("temperature", m.getTemperature());
                    map.put("humidite", m.getHumidite());
                    donnees.add(map);
                });
                emitter.send(donnees);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 5, TimeUnit.SECONDS);

        emitter.onCompletion(() -> future.cancel(true));
        emitter.onTimeout(() -> {
            future.cancel(true);
            emitter.complete();
        });

        return emitter;
    }
}
