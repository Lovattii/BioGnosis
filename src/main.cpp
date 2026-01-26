#include <Arduino.h>
#include <ArduinoJson.h>
#include <PubSubClient.h>
#include <WiFiManager.h>
#include <LittleFS.h>
#include <FS.h>
#include <TimeLib.h>
#include <sys/time.h>

#ifdef ESP32
  #include <WiFi.h>
#else
  #include <ESP8266WiFi.h>
#endif

#include <WiFiClient.h>

#define LED_PIN 8

const char *url = "broker.emqx.io";
const int porta = 1883;
const char *user = "";
const char *pass = "";

const char *pub = "bioGnosis/sensores/dados";
const char *sub = "bioGnosis/sensores/listen";

const char * pathDados = "/dados.txt";
const char * pathSave = "/save.txt";

WiFiClient wifiClient;
PubSubClient client(wifiClient);
WiFiManager wifiManager;

#define TIME_SLEEP 60
#define FATOR_CONVERSAO 1000000ULL

void ledPiscaRapido() {
  digitalWrite(LED_PIN, HIGH);
  delay(200);
  digitalWrite(LED_PIN, LOW);
  delay(200);
}

void ledConectado() {
  digitalWrite(LED_PIN, HIGH);
}

void ledDesligado() {
  digitalWrite(LED_PIN, LOW);
}

void reconnect()
{
  while(!client.connected())
  {
    String id = "ESP";
    id += String(random(0xffff), HEX);

    client.connect(id.c_str(), user, pass);
    client.subscribe(sub);
    delay(1000);

    Serial.println("\ntentando konectar...");
  }
}

void deleteFile(const char *path)
{
  String p = String(path);
  if (LittleFS.remove(path))
    Serial.println("Apagado com sucesso... " + p);
  else
    Serial.println("Não foi possível apagar... " + p);
}

String readFile(const char *path)
{
  File file = LittleFS.open(path, "r");
  if (!file) {
    Serial.println("Arquivo não aberto");
    return "";
  }
  String buffer = file.readString();
  file.close();
  return buffer;
} 

String readFileJson(const char *path)
{
  File file = LittleFS.open(path, "r");
  if (!file || file.isDirectory()) {
    Serial.print("Erro ao tentar abrir");
    return "";
  }

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

void appendFileJson(const char *path, DynamicJsonDocument json)
{
  File file = LittleFS.open(path, "a");
  if (!file) {
    Serial.println("Arquivo não aberto");
    return;
  }
  serializeJson(json, file);
  file.println("");
  file.close();
}

void appendFile(const char *path, const char *msg)
{
  File file = LittleFS.open(path, "a");
  if (!file) {
    Serial.println("Arquivo não aberto");
    return;
  }
  file.print(msg);
  file.close();
}

void writeFile (const char *path, const char *msg)
{
  File file = LittleFS.open(path, "w");
  if (!file || file.isDirectory()) {
    Serial.println("Arquivo não aberto");
    return;
  }
  Serial.println("Escrito com sucesso... " + String(path));
  file.print(msg);
  file.close();
}

void callback(char * topic, byte * payload, int lenght)
{
  Serial.println("TOPICO: ");
  Serial.println(topic);
  Serial.println();

  String msg = "";
  for (int i = 0; i < lenght; i++)
    msg += (char)payload[i];

  if (payload) {
    String ultimo_envio = readFile(pathSave);
    Serial.println("Callback = " + msg);
    Serial.println("Ultimo envio = " + ultimo_envio);

    if (msg != ultimo_envio) {
      String dados = readFileJson(pathDados);
      unsigned int length = dados.length();
      Serial.println("Dados = " + dados);
      
      if (dados != "") {
        client.publish(pub,(uint8_t*) dados.c_str(), length, true);
        writeFile(pathSave, msg.c_str());
        deleteFile(pathDados);
      }
    }
  }
}

void mataESP(int time)
{
  WiFi.disconnect(true);
  WiFi.mode(WIFI_OFF);
  esp_sleep_enable_timer_wakeup(time * FATOR_CONVERSAO);
  Serial.println("  ... Dormindo");
  Serial.flush();
  esp_deep_sleep_start();
}

void setupLittleFS()
{
  if (!LittleFS.begin(true)) {
    Serial.println("Falha ao montar Little FS");
    return;
  }
  Serial.println("Deu Certo FS");
}

bool setupManager()
{
  wifiManager.setConfigPortalTimeout(180);
  wifiManager.setConnectTimeout(20);
  Serial.println("Conectando ao wifi...");

  bool res = wifiManager.autoConnect("bioGnosis", "");
  if (!res) {
    Serial.println("Erro ao tentar conectar");
    ledDesligado();
    return false;
  }

  Serial.println("Conectado com sucesso");
  ledConectado();
  return true;
}

int count = 0;
float temperatura = 30.0;
int pct_umidade = 3000;
int pct_luz = 2000;
bool primeira_vez = true;
DynamicJsonDocument json(128);

void setup() {
  Serial.begin(115200);
  delay(1000);
  Serial.println("Acordando....   ");

  pinMode(LED_PIN, OUTPUT);
  ledDesligado();

  setupLittleFS();

  if (!setupManager()) {
    Serial.println("DEU MERDA WIFI");
    ESP.restart();
  }

  while(WiFi.status() != WL_CONNECTED) {
    ledPiscaRapido();
    Serial.println("Conectando . . . . .");
  }

  configTime(-3 * 3600, 0, "pool.ntp.org", "time.nist.gov");
  while (time(nullptr) < 1000000000) {
    delay(500);
    Serial.print(".");
  }

  client.setBufferSize(4096);
  client.setServer(url, porta);
  client.setCallback(callback);

  if (primeira_vez) {
    writeFile(pathSave, "0");
    writeFile(pathDados, "");
    primeira_vez = false;
  }
}

unsigned long tempo = 0;
#define TIME_MEDICAO 20000

void loop() {
  while (!client.connected()) {
    reconnect();
    delay(1000);
  }

  if(WiFi.status() == WL_CONNECTED) {
    ledConectado();
  } else {
    ledDesligado();  
  }

  if(millis() - tempo > TIME_MEDICAO) {
    tempo = millis();
    temperatura += 5;
    pct_umidade += 100;
    pct_luz += 200;

    json["dataMedicao"] = (unsigned long long)time(nullptr) * 1000ULL;
    json["id_plant"] = 0;
    json["luminosidade"] = pct_luz;
    json["umidade"] = pct_umidade;
    json["temperatura"] = temperatura;
  
    Serial.println("enviando dados ao save..");
    appendFileJson(pathDados, json);
  }
  
  client.loop();
}