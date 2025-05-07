package temperature.demo.service;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.*;

public class tempServer {
    public static void main(String[] args) {
 try {// Créer l'instance de l'objet distant
 RmiServiceClient obj = new RmiServiceClient();
 // Créer le registre RMI sur le port par défaut (1099)
 Registry registry = LocateRegistry.createRegistry(1100);
 // Enregistrer l'objet dans le registre avec le nom "Time"
 
 registry.rebind("temp", obj);
 System.out.println("Serveur RMI démarré !");
 } catch (Exception e) {System.out.println("Erreur serveur : " + e);}
}
    
}
