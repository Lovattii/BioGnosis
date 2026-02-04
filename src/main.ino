#include <ArduinoJson.h>
#include <PubSubClient.h>
#include <LittleFS.h>
#include <FS.h>
#include <WiFi.h>
#include <WiFiClient.h>
#include "time.h"
#include "DHTesp.h"
#include <WebServer.h>

const char *url = "broker.emqx.io";
const int porta = 1883;
const char *user = "";
const char *pass = "";

const char *pub = "bioGnosis/sensores/dados";
const char *sub = "bioGnosis/sensores/listen";

const char * pathDados = "/dados.txt";
const char * pathSave = "/save.txt";
const char * pathWiFi = "/wifi.json";

WiFiClient wifiClient;
PubSubClient client(wifiClient);
WebServer server(80);

bool loadWiFiConfig(String &ssid, String &password) {
  File file = LittleFS.open(pathWiFi, "r");
  if (!file) return false;
  DynamicJsonDocument doc(256);
  if (deserializeJson(doc, file)) return false;
  ssid = doc["ssid"].as<String>();
  password = doc["password"].as<String>();
  file.close();
  return true;
}

void saveWiFiConfig(const char* ssid, const char* password) {
  DynamicJsonDocument doc(256);
  doc["ssid"] = ssid;
  doc["password"] = password;
  File file = LittleFS.open(pathWiFi, "w");
  serializeJson(doc, file);
  file.close();
}

bool connectWiFi() {
  String ssid, password;
  if (!loadWiFiConfig(ssid, password)) return false;
  WiFi.begin(ssid.c_str(), password.c_str());
  unsigned long start = millis();
  while (WiFi.status() != WL_CONNECTED && millis() - start < 10000) {
    delay(500);
  }
  return WiFi.status() == WL_CONNECTED;
}

void handleRoot() {
  server.send(200, "text/html",
    "<form action='/save' method='POST'>"
    "SSID:<input name='ssid'><br>"
    "Senha:<input name='password'><br>"
    "<input type='submit'></form>");
}

void handleSave() {
  String ssid = server.arg("ssid");
  String password = server.arg("password");
  saveWiFiConfig(ssid.c_str(), password.c_str());
  server.send(200, "text/plain", "Credenciais salvas! Reinicie o ESP.");
}

void startAP() {
  WiFi.softAP("ESP_Config");
  server.on("/", handleRoot);
  server.on("/save", HTTP_POST, handleSave);
  server.begin();
}

void reconnect() {
  while(!client.connected()) {
    String id = "ESP";
    id += String(random(0xffff), HEX);
    client.connect(id.c_str(), user, pass);
    client.subscribe(sub);
    delay(1000);
  }
}

void deleteFile(const char *path) {
  LittleFS.remove(path);
}

String readFile(const char *path) {
  File file = LittleFS.open(path, "r");
  if (!file) return "";
  String buffer = file.readString();
  file.close();
  return buffer;
} 

String readFileJson(const char *path) {
  File file = LittleFS.open(path, "r");
  if (!file || file.isDirectory()) return "";
  String payload = "{\"dados\":[";
  bool first_line = true;
  while(file.available()) {
    String linha = file.readStringUntil('\n');
    if (linha.length() < 2) continue;
    linha.trim();
    if (first_line) first_line = false;
    else payload += ",";
    payload += linha;
  }
  payload += "]}";
  file.close();
  return payload;
}

void appendFileJson(const char *path, DynamicJsonDocument json) {
  File file = LittleFS.open(path, "a");
  if (!file) return;
  serializeJson(json, file);
  file.println("");
  file.close();
}

void writeFile (const char *path, const char *msg) {
  File file = LittleFS.open(path, "w");
  if (!file || file.isDirectory()) return;
  file.print(msg);
  file.close();
}

void callback(char * topic, byte * payload, int lenght) {
  String msg = "";
  for (int i = 0; i < lenght; i++)
    msg += (char)payload[i];
  if (payload) {
    String ultimo_envio = readFile(pathSave);
    if (msg != ultimo_envio) {
      String dados = readFileJson(pathDados);
      unsigned int length = dados.length();
      if (dados != "") {
        client.publish(pub,(uint8_t*) dados.c_str(), length, true);
        writeFile(pathSave, msg.c_str());
        deleteFile(pathDados);
      }
    }
  }
}

void setupLittleFS() {
  LittleFS.begin(true);
}

#define pinoLDR 2
#define pinoUmidade 0
#define TIME_MEDICAO 2000
#define DHT_PIN 1

short int umidade = 0;
short int luminosidade = 0;
time_t now;
DynamicJsonDocument json(128);
unsigned long tempo = 0;
DHTesp dht;
const char* ntpServer = "pool.ntp.org";

void setup() {
  Serial.begin(115200);
  dht.setup(DHT_PIN, DHTesp::DHT11);
  setupLittleFS();
  if (connectWiFi()) {
    configTime(3 * 3600, 0, ntpServer);
    client.setBufferSize(4096);
    client.setServer(url, porta);
    client.setCallback(callback);
    writeFile(pathSave, "0");
    writeFile(pathDados, "");
  } else {
    startAP();
  }
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    while (!client.connected()) {
      reconnect();
      delay(1000);
    }
    if(millis() - tempo > TIME_MEDICAO) {
      tempo = millis();
      TempAndHumidity data = dht.getTempAndHumidity();
      float temperatura = data.temperature;
      umidade = analogRead(pinoUmidade);
      luminosidade = analogRead(pinoLDR);
      time(&now);
      now -= 10800;
      json["dataMedicao"] = (unsigned long long)time(nullptr) * 1000ULL;
      json["id_plant"] = 0;
      json["luminosidade"] = luminosidade;
      json["umidade"] = umidade;
      json["temperatura"] = temperatura;
      appendFileJson(pathDados, json);
    }
    client.loop();
  } else {
    server.handleClient();
  }
}
