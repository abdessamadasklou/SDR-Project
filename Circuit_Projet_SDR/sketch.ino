#include <WiFi.h>
#include <PubSubClient.h>
#include "DHTesp.h"

const char* ssid = "Wokwi-GUEST";        // Wi-Fi SSID
const char* password = "";               // Wi-Fi Password (Wokwi-GUEST has none)
const char* mqttServer = "broker.hivemq.com"; // Public MQTT broker
const int mqttPort = 1883;               // Non-secure MQTT port

WiFiClient wifiClient;                   // Non-secure WiFi client
PubSubClient mqttClient(wifiClient);

const char* topic = "esp32/chat";        // Shared topic for both ESPs
const char* topic2 = "esp32/data";       
String clientId;                         // Unique identifier for each ESP

const int DHT_PIN = 22;
DHTesp dhtSensor;
String t;
String h;

void callback(char* topic, byte* payload, unsigned int length) {
  String message = "";
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }

  // Ignore messages published by this ESP
  if (message.startsWith(clientId)) return;

  // Display received message
  Serial.println("Received: " + message);
  Serial.println("---");
  digitalWrite(16, HIGH);
  delay(2800);
  digitalWrite(16, LOW);
}

void setup() {
  Serial.begin(115200);
  pinMode(14, OUTPUT);
  pinMode(16, OUTPUT);

  dhtSensor.setup(DHT_PIN, DHTesp::DHT22);
  clientId = "ESP32-#1 :";
  Serial.println("Client ID: " + clientId);

  // Connect to Wi-Fi
  Serial.println("Connecting to WiFi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println("\nConnected to WiFi");

  // Configure MQTT client
  mqttClient.setServer(mqttServer, mqttPort);
  mqttClient.setKeepAlive(600);
  mqttClient.setCallback(callback);
  // Connect to the MQTT broker
  while (!mqttClient.connected()) {
    Serial.print("Connecting to MQTT...");
    if (mqttClient.connect(clientId.c_str())) {
      Serial.println("Connected to MQTT broker");
      delay(5000);
    } else {
      Serial.print("Failed, rc=");
      Serial.println(mqttClient.state());
      delay(2000);
    }
  }

  // Subscribe to the shared topic
  mqttClient.subscribe(topic);
  Serial.println("Subscribed to topic: " + String(topic));

  // Publish a connection message
  mqttClient.publish(topic, (clientId + " connected!").c_str());
  Serial.println("---");
} 

void loop() {
  ensureWiFiConnected();
  ensureMQTTConnected();
  mqttClient.loop();
  sendTempHum();
  //mqttClient.connect(clientId.c_str());
  // Check for user input from Serial Monitor
  if (Serial.available() > 0) {
    String message = Serial.readStringUntil('\n');
    message.trim(); // Remove any trailing newline or spaces

    // Publish the message with the client ID prefix
    String fullMessage = clientId + ": " + message;
    mqttClient.publish(topic, fullMessage.c_str());
    Serial.println("Published: " + fullMessage);
    Serial.println("---");
    digitalWrite(14, HIGH);
    delay(2800);
    digitalWrite(14, LOW);
  }
}
void sendTempHum(){
  TempAndHumidity  data = dhtSensor.getTempAndHumidity();
  t=String(data.temperature, 2);
  h=String(data.humidity, 1);
  String fullMessage = "{\"Temperature\":"+ t +",\"Humidite\":"+h+"}" ;
  mqttClient.publish(topic2, fullMessage.c_str());
  delay(2200);
  // Serial.println("Temp: " + String(data.temperature, 2) + "°C");
  // Serial.println("Humidity: " + String(data.humidity, 1) + "%");
}
void ensureWiFiConnected() 
{
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("Reconnecting to WiFi...");
    while (WiFi.status() != WL_CONNECTED) {
      WiFi.begin(ssid, password);
      delay(1000);
      Serial.print(".");
    }
    Serial.println("\nReconnected to WiFi");
  }
}

void ensureMQTTConnected()
{
  if (!mqttClient.connected()) {
    Serial.println("Disconnected from MQTT broker. Reconnecting...");
    while (!mqttClient.connected()) {
      if (mqttClient.connect(clientId.c_str())) {
        Serial.println("Reconnected to MQTT broker");
        mqttClient.subscribe(topic); // Resubscribe after reconnecting
      } else {
        Serial.print("Reconnection failed, rc=");
        Serial.println(mqttClient.state());
        delay(2000); // Wait before retrying
      }
    }
  }
}