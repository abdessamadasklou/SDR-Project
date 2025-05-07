package temperature.demo.client;

import java.rmi.Naming;
import java.util.Scanner;
import temperature.demo.remote.TemperatureService;
import temperature.demo.model.Mesure;

public class capteur {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            TemperatureService tempService = (TemperatureService) Naming.lookup("rmi://localhost:1100/temp");

            System.out.print("Entrez la température : ");
            double temperature = scanner.nextDouble();

            System.out.print("Entrez l'humidité : ");
            double humidity = scanner.nextDouble();

            Mesure mesure = new Mesure(temperature, humidity);
            tempService.envoyerMesure(mesure);

            System.out.println("Mesure envoyée !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
