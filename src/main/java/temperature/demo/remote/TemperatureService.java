package temperature.demo.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import temperature.demo.model.Mesure;

public interface TemperatureService extends Remote {
    List<Mesure> getDernieresMesures() throws RemoteException;
    List<Mesure> getAlertes() throws RemoteException;
    void envoyerMesure(Mesure m) throws RemoteException;
}
