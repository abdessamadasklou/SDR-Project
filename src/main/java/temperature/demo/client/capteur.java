package temperature.demo.client;

import java.rmi.Naming;
import java.util.Scanner;
import temperature.demo.remote.TemperatureService;
import temperature.demo.model.Mesure;

public class capteur {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Only ONE scanner for System.in

        System.out.print("Entrez le nom du capteur : ");
        String capteurName = scanner.nextLine();

        while (true) {
            try {
                TemperatureService tempService = (TemperatureService) Naming.lookup("rmi://localhost:1100/temp");

                System.out.print("Entrez la température : ");
                double temperature = scanner.nextDouble();

                System.out.print("Entrez l'humidité : ");
                double humidity = scanner.nextDouble();

                Mesure mesure = new Mesure(temperature, humidity, capteurName);
                tempService.envoyerMesure(mesure);

                System.out.println("Mesure envoyée !");
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
                e.printStackTrace();
                break; // Optional: break loop on error
            }
        }

        scanner.close(); // Close at the very end
    }
}
