# BioGnosis
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

#### Sistema

Desenvolvido através do Arduino IDE, o circuito possui um sistema próprio que é capaz de interpretar informações obtidas por meio dos sensores de seus respectivos parâmetros. Em questão, é necessário entender os seguintes componentes: o sensor de umidade do solo mede a resistência da terra e retorna um valor em volts. O sensor de luminosidade, do tipo LDR, varia sua resistência de acordo com a incidência de luz, permitindo a leitura do nível de iluminação do ambiente. Por fim, o sensor de temperatura, do tipo DHT11, realiza a medição da temperatura ambiente e envia essas informações ao microcontrolador.

Utilizando a biblioteca WiFiManager, o ESP é configurado como um access point temporário, tornando-se visível na lista de redes disponíveis. Após a conexão, o usuário insere os dados de sua rede, permitindo o acesso do sistema à internet. Em seguida, os dados coletados pelos sensores são enviados via protocolo MQTT ao EMQX, um broker público previamente configurado no ESP.


#### Cálculo de estabilidade da planta

A fim de tornar a interação do usuário com o plantio numa atividade intuitiva e agradável, estipula-se um cálculo montado com os dados obtidos dos sensores

![CalculoEstabilidade](https://github.com/Lovattii/BioGnosis/blob/main/images/calculo.png)


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
      <img src="IMAGEM2" width="120"><br>
      <strong>Bernardo Francisco</strong><br>
      Developer<br>
      <a href="https://github.com/usuario2">
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
