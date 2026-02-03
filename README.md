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

##### Biognosis

O BioGnosis é um aplicativo Android desenvolvido no Android Studio, utilizando Java e XML, com foco no monitoramento de plantas por meio de sensores que se comunicam via transmissão MQTT. O projeto aplica conceitos de gamificação para tornar a interpretação de dados como luminosidade e umidade mais acessível a usuários sem conhecimento técnico na área de plantio, por meio de uma interface minimalista e intuitiva.

O aplicativo conta com uma aba inicial que exibe a planta cadastrada, apresentando barras gamificadas de luminosidade, umidade e vida (baseada em um cálculo de ambiente ideal para plantio). Possui também uma aba para cadastro de novas plantas, permitindo definir nome e tipo da planta, uma aba responsável pelo registro de estatísticas de cada planta e uma tela dedicada às conquistas, que incentivam o engajamento contínuo do usuário.


## Funcionamento

#### Aplicativo

Desenvolvido de forma inovadora pelo grupo por meio do Android Studio, o aplicativo BioGnosis é um projeto totalmente autoral, concebido e implementado do zero, com o objetivo de apresentar informações visuais relevantes a partir das medições realizadas pelos sensores do ESP de forma assíncrona. <br>


##### Front-End

A interface gráfica do aplicativo possui três abas de navegação, a HOME, o RELATÓRIO e as CONQUISTAS. <br>

— A Home é responsável por conter as principais informações referentes a planta, tratando-se de: dias de vida da planta, luminosidade, temperatura, umidade e a "barra de vida". Cada um dos parâmetros variam entre um valor de 0 a 100 definido pelo cálculo de estabilidade da planta e são interpretados para o usuário por meio de cores e tamanhos diferentes de barra. Além disso, essa aba conta com dois botoões: o menu de plantas e a "lixeira", responsáveis por, respectivamente, adicionar novas plantas ou navegar através do banco de dados e apagar a planta atual. O menu de plantas abre uma janela temporária (bottom sheet), que permite inserir um novo objeto de acordo com seu nome e sua espécie de planta (pré-definida pelo aplicativo), ou trocar a planta da home por uma da escolha do usuário. <br>

— O Relatório possui alguns dados principais da planta como: as variáveis padrões da planta em valores numéricos, número de dias, número de vezes irrigadas, número de "quase mortes" (quantidade de vezes em que a vida da planta chegou a menos de 30%), além de conter os gráficos que permitem um gerenciamento técnico de sua planta. <br>

— A aba de conquistas trata-se de uma proposta de gamificar o aplicativo fornecendo interações lúdicas com o usuário através de troféus, por exemplo: a planta sobreviver por um ano e o usuário manteve a vida da planta acima de 70% durante 1000 medições. E, não obstante, informa ao usuário curiosidades interessantes sobre aquela espécie e seu método de plantio. <br>

##### Back-End

O aplicativo foi projetado principalmente na linguagem Java, contudo, existem outras linguagens presentes no sistema, como: Kotlin (para certas bibliotecas gráficas) e SQL (utilizado para criação e gerenciamento do banco de dados das plantas). A estrutura básica do UI (User Interface), é feita através da linguagem de marcação XML (Linguagem de Marcação Extensível).

Bibliotecas mais usadas:

Meowbottomnavigation - Cria uma barra de navegação pra interface gráfica utilizando o conceito da curva de Bezier (criando um estilo gráfico minimalista e agradável).
MPAndroidChart - Utilizada pra criação de gráficos e interpretação de informações.
FacebookShimmer - Cria uma animação de brilho utilizada como setup enquanto o sistema está carregando as informações.
MQTTEclipse - Responsável pela conexão MQTT com o ESP32 C3 Super Mini.
GoogleGson - Transforma uma string em um objeto json para manipulação de dados.

#### Sistema

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
