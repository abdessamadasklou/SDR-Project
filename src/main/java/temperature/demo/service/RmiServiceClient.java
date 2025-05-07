package temperature.demo.service;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import temperature.demo.model.Mesure;
import temperature.demo.remote.TemperatureService;

public class RmiServiceClient extends UnicastRemoteObject implements TemperatureService {
    private List<Mesure> receivedMesures = new ArrayList<>();
    public RmiServiceClient() throws RemoteException {
        super();
    }

    @Override
public void envoyerMesure(Mesure m) throws RemoteException {
    receivedMesures.add(m);
    // this.getDernieresMesures() ;
    System.out.println("Nouvelle mesure reçue : " + m.getTemperature() + "°C / " + m.getHumidite() + "%");
}
    public List<Mesure> getDernieresMesures() throws RemoteException {
        return new ArrayList<>(receivedMesures);
    }

    public List<Mesure> getAlertes() throws RemoteException {
        return getDernieresMesures().stream()
            .filter(m -> m.getTemperature() < 10 || m.getTemperature() > 30)
            .toList();
    }
}
