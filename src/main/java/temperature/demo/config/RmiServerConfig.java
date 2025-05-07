// package temperature.demo.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.remoting.rmi.RmiServiceExporter;
// import temperature.demo.service.RmiServiceClient;
// import temperature.demo.remote.TemperatureService;

// @Configuration
// public class RmiServerConfig {

//     @Bean
//     public RmiServiceExporter rmiServiceExporter(RmiServiceClient rmiServiceClient) {
//         RmiServiceExporter exporter = new RmiServiceExporter();
//         exporter.setServiceName("temp");
//         exporter.setServiceInterface(TemperatureService.class);
//         exporter.setService(rmiServiceClient);
//         exporter.setRegistryPort(1100);
//         return exporter;
//     }
// }
