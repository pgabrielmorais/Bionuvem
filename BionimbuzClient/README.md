# BionimbuzClient
#### Universidade de Brasília
#### Departamento de Ciência da Computação
#### Projeto de Graduação em Engenharia de Computação
###### Aluno: Vinícius de Almeida Ramos
###### Orientador(a): Professora Aleteia Araújo

#### Introdução

Software em desenvolvimento que servirá como interface cliente dos serviços providos pelo núcleo de processamento do ambiente de execução de *workflows* em Bioinformática BioNimbuZ.

#### Tecnologias

A interface será desenvolvida utilizando tecnologias web para prover acesso ao BioNimbuZ, sendo elas:
* **Linguagem**: Java
* **Framework MVC**: JSF 2.0
* **Biblioteca de Componentes gráficos**: Primefaces 5.0
* **Servidor de Aplicações**: JBoss 10 Wildfly
* **Interface Web**: HTML e CSS
* **Camada de Comunicação**: WebServices utilizando o framework Resteasy
* **Controle de Configurações**: Apache Maven
* **Framework JSON**: Jackson
 
#### Tecnologias à serem integradas
* **Spring Security**: para gerenciar a segurança da aplicação
* **PrettyFaces**: Framework para reescrever as URLs da aplicação

#### Utilização (em construção)
* Este projeto deve ser copiado para a pasta /pasta_home/nome_usuario/BioNimbuzClient

#### Instalação
Para compilar, realizar o build e executar o ambiente do BioNimbuZClient, seguir o passo-a-passo descrito abaixo.

##### 1. Wildfly
Servidor de aplicação web, utilizado no projeto.
----------------------------------------------------------------------------------------------------------------------
1.1 Realizar o download do wildfly: http://download.jboss.org/wildfly/10.0.0.Final/wildfly-10.0.0.Final.zip

1.2 Descompacte na pasta /pasta_home/nome_usuario/wildfly

1.3 Configure o arquivo /pasta_home/nome_usuario/wildfly/standalone/configuration/standalone-full-ha.xml

1.4 Altere as linhas, para a utilização da porta 8888 no wildfly e para as tranferências de arquivos maiores: 

socket-binding name="http" port="${jboss.http.port:8080}" para 

socket-binding name="http" port="${jboss.http.port:8888}"

http-listener name="default" socket-binding="http" redirect-socket="https" para

http-listener name="default" socket-binding="http" redirect-socket="https" max-post-size="0"

1.5 Salve e feche o arquivo

1.6 Configure o arquivo /pasta_home/nome_usuario/wildfly/bin/standalone.conf

1.7 Altere as linhas, para as tranferências de arquivos maiores:

JAVA_OPTS="-Xms64m -Xmx512m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true" para 

JAVA_OPTS="-Xms1024m -Xmx2048m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true"

1.8 Salve e feche o arquivo

1.9 Inicie o wildfly com o comando sudo /pasta_home/nome_usuario/wildfly/bin/./standalone.sh

1.10 Pronto! O servidor wildfly está executando, mais detalhes na documentação:

 https://docs.jboss.org/author/display/WFLY10/Documentation
 
----------------------------------------------------------------------------------------------------------------------
##### Executando o BioNimbuZClient

Para iniciar a execução o servidor wildfly deve ser iniciado.

-------------------------------------------------------------------------------------------------------------
1 - Alterar conf/node.yaml com as configurações de ip do servidor , ip e os caminhos das pastas 
    necessárias para a execução, etc..
