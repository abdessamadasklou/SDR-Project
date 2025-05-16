package temperature.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "temperature.demo.repository")
@EntityScan(basePackages = "temperature.demo.model")
public class SurveillanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SurveillanceApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public temperature.demo.service.RmiServiceClient rmiServiceClient() throws java.rmi.RemoteException {
        return new temperature.demo.service.RmiServiceClient();
    }
}
