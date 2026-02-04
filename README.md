<h1 align="center">BioGnosis ☘️</h1>
<p align="center">
  <img src="https://img.shields.io/github/last-commit/Lovattii/BioGnosis" />
  <img src="https://img.shields.io/badge/Made%20with-Java-ED8B00?logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Made%20with-C%2B%2B-00599C?logo=c%2B%2B&logoColor=white" />
  <img src="https://img.shields.io/badge/Made%20with-Android%20Studio-3DDC84?logo=android-studio&logoColor=white" />
   <img src="https://img.shields.io/badge/Made%20with-XML-00599C?logo=xml&logoColor=white" />
  <img src="https://img.shields.io/badge/Made%20with-SQL-4479A1?logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Made%20with-Kotlin-7F52FF?logo=kotlin&logoColor=white" />

</p> <br>
<p align= "center">
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" width="50" />
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/cplusplus/cplusplus-original.svg" width="50" />
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/xml/xml-original.svg" width="50"/>
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/mysql/mysql-original.svg" width="50"/>
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/kotlin/kotlin-original.svg" width="50"/>
<img src="https://uxwing.com/wp-content/themes/uxwing/download/brands-and-social-media/android-studio-icon.png" width="50"/>
</p>


## Introdução ao Projeto
Repositório voltado para o projeto BioGnosis desenvolvido durante a disciplina de Projeto Integrado de Computação, ofertada pelo professor Jadir.
Articulado com o intuito de auxiliar no processo de cuidado e plantio, BioGnosis propõe a criação de um sistema integrado de sensores de temperatura, umidade e luminosidade
que possam monitorar as condições ideais de uma planta segundo um banco de dados definido em tempo de execução. Para maiores distâncias, há um aplicativo conectado
aos sensores que capta as informações e repassa ao usuário, facilitando seu trabalho à longo alcance, não limitando o sistema a um ambiente pessoal.

## Componentes

Para funcionamento do trabalho, utilizamos os seguintes componentes:

### Hardware

— ESP32-C3 Super Mini <br>
— Sensor de umidade e temperatura do ar (DHT11) <br>
— Sensor de umidade do solo (Módulo sensor de umidade do solo) <br>
— Sensor de luminosidade (LDR) <br>
— Case de Baterias AA <br>
— 3 Pilhas AA alcalinas. <br>


### Software
# 🌿 BioGnosis

> **Gamificação e IoT aplicados ao monitoramento inteligente de plantas.**

O **BioGnosis** é um aplicativo Android nativo que une a precisão da "Internet das Coisas" (IoT) com o engajamento da gamificação. O objetivo é democratizar o cultivo de plantas, traduzindo dados técnicos complexos (como níveis exatos de umidade e luminosidade) em uma interface visual, intuitiva e lúdica, acessível para usuários sem conhecimento técnico em botânica.


## Visão Geral

O aplicativo atua como a interface de controle para um sistema de monitoramento baseado em microcontroladores (**ESP32/ESP8266**). Através de uma conexão MQTT, o BioGnosis recebe dados dos sensores em tempo real e os interpreta visualmente.

A experiência do usuário é centrada na "saúde" da planta, representada por barras de vida e status gamificados. O usuário não apenas monitora, mas "cuida" da planta como em um jogo, desbloqueando conquistas e garantindo a sobrevivência do seu cultivo digital e físico.


## Funcionalidades Principais

### 1. 🎮 Front-End & UX (Gamificação)
O aplicativo é dividido em três módulos principais de navegação, acessíveis via *MeowBottomNavigation*:

* **🏠 Home (Dashboard):**
    * **Progressão Temporal (Dias vs. Lv):** O aplicativo inova ao substituir o tradicional "Nível" (Lv) de jogos pela contagem de **"Dias de Vida"**. O progresso do usuário é medido pela longevidade da planta, transformando o tempo de dedicação no principal indicador de sucesso.      
    * **Feedback Visual:** As barras variam de 0 a 100 com base no cálculo de estabilidade (setpoint ideal) de cada espécie, alterando cores e tamanhos para indicar urgência.
    * **Gestão de Plantas:** Menu *Bottom Sheet* para cadastro rápido. 
      * **Inventário Dinâmico:** Um menu deslizante que oferece acesso rápido a **todas as plantas cadastradas**. O usuário pode visualizar sua coleção completa e alternar qual planta está sendo monitorada na Home instantaneamente.
      * **Cadastro e Expansão:** Permite adicionar novas plantas ao sistema, selecionando nome e **espécie pré-definida** (incluindo Alface, Tomate, Couve-flor e Cebolinha), que já carregam os parâmetros ideais de cultivo.

* **📊 Relatório (Analytics):**
    * **Dados Brutos:** Visualização numérica das variáveis para usuários avançados.
    * **Histórico de Sobrevivência:** Contadores de "Dias de Vida", "Nº de Irrigações" e "Quase Mortes" (vida < 30%).
    * **Gráficos:** Renderização visual do histórico de dados via *MPAndroidChart*.

* **🏆 Conquistas (Engajamento):**
    * Sistema de troféus baseado em marcos (ex: "Sobreviver 1 ano", "Manter vida > 70% por 1000 medições").
    * Curiosidades educativas sobre a espécie cultivada e métodos de plantio.

### 2. ⚙️ Back-End & Arquitetura
O sistema foi desenvolvido com foco em performance e assincronicidade para garantir que a UI nunca trave enquanto aguarda respostas dos sensores.

* **Comunicação MQTT Assíncrona:**
    * Utiliza o protocolo MQTT (via *Eclipse Paho Client*) para comunicação bidirecional com o ESP32.
    * **Workers & Threads:** Implementação robusta utilizando `WorkManager` e Workers dedicados (como `MqttWorker` e `MqttResquisicaoWorker`) para processar conexões e requisições em segundo plano.
    * **Sincronização:** Uso de `CountDownLatch` para gerenciar o fluxo de *Publish/Subscribe*, garantindo que o aplicativo aguarde a resposta do sensor de forma controlada antes de atualizar a UI.

* **Persistência de Dados:**
    * Utilização de **Room Database** (camada de abstração sobre SQLite) para armazenamento local seguro das plantas cadastradas e seus históricos.

* **Serialização:**
    * Manipulação de dados JSON via **Google Gson** para troca de mensagens estruturadas com o dispositivo IoT.


## Design e Assets

O projeto visual do BioGnosis rompe com a rigidez das interfaces tradicionais, adotando um conceito de **Design Orgânico e Minimalista**.

* **Formas Orgânicas:** A interface prioriza bordas arredondadas e linhas suaves, criando uma estética fluida que reflete a naturalidade das plantas. O uso de curvas (como as Curvas de Bézier na barra de navegação) elimina a agressividade dos ângulos retos, proporcionando uma navegação mais amigável e moderna.
* **Minimalismo Funcional:** A paleta de cores e a disposição dos elementos foram pensadas para reduzir o ruído visual. O foco é mantido estritamente nas informações vitais (saúde da planta), garantindo que a tecnologia atue como um suporte invisível e elegante, sem sobrecarregar cognitivamente o usuário.
  

## Tecnologias Utilizadas

* **Linguagem Principal:** Java (Android Nativo)
* **Linguagens Auxiliares:** Kotlin (Integrações de UI), SQL (Banco de Dados)
* **IDE:** Android Studio
* **Design & UI:** XML, Material Design


### Bibliotecas e Dependências

| Biblioteca | Função |
| :--- | :--- |
| **MeowBottomNavigation** | Navegação com curvas de Bézier e design minimalista. |
| **MPAndroidChart** | Criação e renderização dos gráficos de relatório. |
| **Facebook Shimmer** | Feedback de carregamento (loading skeleton) elegante. |
| **Eclipse Paho MQTT** | Cliente MQTT para conexão IoT. |
| **Google Gson** | Serialização e desserialização de objetos JSON. |
| **Room Database** | Persistência de dados local. |
| **WorkManager** | Gerenciamento de tarefas em segundo plano (threads). |


## 💡 Sobre o Projeto

O BioGnosis é um projeto autoral, concebido e implementado do zero. Ele resolve o problema da "caixa preta" no monitoramento de plantas, onde sensores apenas entregam números. Aqui, os números viram cores, barras de vida e conquistas, aproximando a tecnologia da natureza de forma amigável.

### Sistema

Desenvolvido através do Arduino IDE, o circuito possui um sistema próprio que é capaz de interpretar informações obtidas por meio dos sensores de seus respectivos parâmetros. Em questão, é necessário entender os seguintes componentes: o sensor de umidade do solo mede a resistência da terra e retorna um valor inteiro de 12 bits. O sensor de luminosidade, do tipo LDR, varia sua resistência de acordo com a incidência de luz, permitindo a leitura do nível de iluminação do ambiente. Por fim, o sensor de temperatura, do tipo DHT11, realiza a medição da temperatura ambiente e envia essas informações ao microcontrolador.

Utilizando a biblioteca WiFiManager, o ESP é configurado como um access point temporário, tornando-se visível na lista de redes disponíveis. Após a conexão, o usuário insere os dados de sua rede, permitindo o acesso do sistema à internet. Em seguida, os dados coletados pelos sensores são enviados via protocolo MQTT ao EMQX, um broker público previamente configurado no ESP.


#### Cálculo de estabilidade da planta

O BioGnosis utiliza um algoritmo de pontuação ponderada para calcular a estabilidade da planta, ou seja, o quão próximo o ambiente atual está das condições ideais de cultivo definidas para cada espécie.

Cada planta cadastrada possui parâmetros ideais e tolerâncias para: temperatura, luminosidade e umidade do solo. Além dos valores ideais e tolerâncias, cada parâmetro recebe um peso que define sua importância relativa no cálculo final.

O cálculo da estabilidade segue os seguintes passos:

— Pontuação individual por parâmetro  
— Para cada sensor (temperatura, luminosidade e umidade), é calculado um score normalizado entre 0 e 1:
    

$$\large e^{-\frac{|\text{diff} - \text{tolerância}|}{\text{tolerância}}}$$



Considere diff = valor ideal - valor medido.

Isso garante que quanto mais distante do ideal, menor será a pontuação.
 
Cada score é multiplicado pelo peso definido para o parâmetro. Em seguida, calcula-se a média ponderada:

$$\text{estabilidade} = \frac{(\text{score}_{\text{temp}} \cdot \text{peso}_{\text{temp}}) + (\text{score}_{\text{lum}} \cdot \text{peso}_{\text{lum}}) + (\text{score}_{\text{hum}} \cdot \text{peso}_{\text{hum}})}{\text{peso}_{\text{temp}} + \text{peso}_{\text{lum}} + \text{peso}_{\text{hum}}} \cdot 100$$

O resultado final é um valor percentual entre 0% e 100%, representando a "vida" ou estabilidade da planta.


## Colaboradores


<table>
  <tr>
    <td align="center">
      <br>
      <img src="https://avatars.githubusercontent.com/u/217851668?v=4" width="120"><br>
      <strong>Beatriz Videira</strong><br>
      Scrum Master<br>
      <a href="https://github.com/bvfreitas">
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" width="20">
        GitHub
      </a>
    </td>
    <td align="center">
        <br>
      <img src="https://avatars.githubusercontent.com/u/219158917?v=4" width="120"><br>
      <strong>Bernardo Francisco</strong><br>
      Developer<br>
      <a href="https://github.com/BartSOS">
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" width="20">
        GitHub
      </a>
    </td>
    <td align="center">
      <br>
      <img src="https://avatars.githubusercontent.com/u/237894667?v=4" width="120"><br>
      <strong>Guilherme Lovatti</strong><br>
      Developer<br>
      <a href="https://github.com/Lovattii">
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" width="20">
        GitHub
      </a>
    </td>
  </tr>
</table>
