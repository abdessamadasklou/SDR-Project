package temperature.demo.MQTT;

import java.rmi.Naming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import temperature.demo.model.Mesure;

import org.springframework.integration.mqtt.core.MqttPahoClientFactory;


import temperature.demo.remote.TemperatureService;
import temperature.demo.model.Mesure;

@Configuration
public class MqttSubscriberConfig {

    @Autowired
    private MqttPahoClientFactory clientFactory;

    // Define the MQTT topic(s) to subscribe to
    private static final String[] TOPICS = { "esp32/data" };
    private static final int[] QOS = { 1 }; // Quality of Service (0, 1, or 2)

    // Create a channel adapter to listen to MQTT messages
    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = 
            new MqttPahoMessageDrivenChannelAdapter(
                "spring-subscriber", // Unique client ID
                clientFactory,
                TOPICS // Topics to subscribe to
            );
        adapter.setQos(QOS); // Set QoS for each topic
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(mqttInputChannel()); // Route messages to a channel
        return adapter;
    }

    // Create a message channel to handle incoming messages
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    // Process messages from the channel
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler messageHandler() {
        return message -> {
            // This is the simple verification
            System.out.println("✅ RECEIVED MQTT MESSAGE!");
            System.out.println("Topic: " + message.getHeaders().get("mqtt_receivedTopic"));
            System.out.println("Payload: " + message.getPayload());
            System.out.println("---");
            
            // Convert payload to string
            try {
                TemperatureService tempService = (TemperatureService) Naming.lookup("rmi://localhost:1100/temp");
                String payloadStr = message.getPayload().toString();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(payloadStr);

                double temperature = rootNode.get("Temperature").asDouble();
                double humidity = rootNode.get("Humidite").asDouble();

                System.out.println("Temperature: " + temperature);
                System.out.println("Humidity: " + humidity);
                
                Mesure mesure = new Mesure(temperature, humidity, "Sensor 1");
                tempService.envoyerMesure(mesure);

                System.out.println("Mesure envoyée !");
                Thread.sleep(2000);

            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
                e.printStackTrace();
                //break; // Optional: break loop on error
            }
        };
    }
}