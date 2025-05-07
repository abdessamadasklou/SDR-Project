package temperature.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import temperature.demo.service.RmiServiceClient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Component
public class RmiServerInitializer {

    private final RmiServiceClient remoteObj;

    public RmiServerInitializer(RmiServiceClient remoteObj) {
        this.remoteObj = remoteObj;
    }

    @PostConstruct
    public void startRmiServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(1100);
            System.out.println("RMI registry started on port 1100.");

            registry.rebind("temp", remoteObj);
            System.out.println("Remote object bound to registry with name 'temp'.");
        } catch (Exception e) {
            System.err.println("Exception starting RMI server: " + e.toString());
            e.printStackTrace();
        }
    }
}
