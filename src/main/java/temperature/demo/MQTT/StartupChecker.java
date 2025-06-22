package temperature.demo.MQTT;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.stereotype.Component;

@Component
public class StartupChecker implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(StartupChecker.class);
    
    @Autowired
    private MqttPahoClientFactory clientFactory;

    @Override
    public void afterPropertiesSet() {
        MqttConnectOptions options = clientFactory.getConnectionOptions();
        String brokerUrl = options.getServerURIs()[0];
        
        try {
            log.info("⌛ Testing MQTT connection to: {}", brokerUrl);
            
            MqttAsyncClient testClient = new MqttAsyncClient(
                brokerUrl, 
                "connection-tester-" + System.currentTimeMillis()
            );
            
            IMqttToken connectToken = testClient.connect(options);
            connectToken.waitForCompletion(3000); // 3 second timeout
            
            if (connectToken.isComplete()) {
                log.info("✅ Successfully connected to MQTT broker!");
                testClient.disconnect();
            }
        } catch (MqttException e) {
            log.error("❌ Connection failed to {}: {}", brokerUrl, e.getMessage());
            log.error("Possible causes: Broker not running, wrong URL, or firewall issues");
        }
    }
}