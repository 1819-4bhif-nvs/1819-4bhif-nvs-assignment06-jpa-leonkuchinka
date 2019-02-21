## Wildfly 15

Gleiche Vorgehensweise wie Wildfly 14 (siehe Folien Download Ordner)

## Payara 5

#### Server konfigurieren

- payara5/bin/asadmin ausführen
- start-domain eingeben
- im Browser auf localhost:4848
- Neuen JDBC Connection Pool erstellen
    - Resources -> JDBC -> JDBC Connection Pool
    - Neuen Pool erstellen
    - Pool Name definieren: z.B.: derby-connector
    - Resource Type: javax.sql.DataSource
    - Database Driver Vendor: Derby
    - -> Next
    - Additional Properties setzen:
        - password -> app
        - user -> app
        - serverName -> localhost
        - portNumber -> 1527
        - databaseName -> db
- Neue JDBC Resource erstellen
    - Resources -> JDBC -> JDBC Resources
    - Neue Resource erstellen
    - JNDI Name: DbDS
    - Pool Name: <vorher erstellter Pool>
    - -> Ok


 #### Projekt deployen

- persistence.xml anpassen: <jta-data-source> auf DbDS setzen
- Edit Configurations -> + -> GlassFish Server
- Payara5 Pfad auswählen (../opt/payara5)
- Server Domain setzten: domain1
- -> fix für Deployment
- -> Ok

-> Fertig

## Glassfish

#### Server konfigurieren

- glassfish5/bin/asadmin ausführen
- start-domain eingeben
- im Browser auf localhost:4848
- DerbyPool bearbeiten
    - Resources -> JDBC -> JDBC Connection Pool -> DerbyPool
    - Additional Properties verändern:
        - password -> app
        - user -> app
        - serverName -> localhost
        - portNumber -> 1527
        - databaseName -> db
- Neue JDBC Resource erstellen
    - Resources -> JDBC -> JDBC Resources
    - Neue Resource erstellen
    - JNDI Name: DbDS
    - Pool Name: DerbyPool
    - -> Ok


 #### Projekt deployen

- persistence.xml anpassen: <jta-data-source> auf DbDS setzen
- Edit Configurations -> + -> GlassFish Server
- GlassFish Pfad auswählen (../opt/glassfish5)
- Server Domain setzten: domain1
- -> fix für Deployment
- -> Ok

-> Fertig


## TomEE

#### Server konfigurieren

- im Ordner bin ein neues File erstellen: setenv.sh
- JAVA_HOME in diesem File setzen:
    ```
    JAVA_HOME=/usr/lib/jvm/java-8-oracle
    ```
- bin/startup.sh
- User setzen: conf/tomcat-users.xml:
    ```
    <role rolename="manager-gui"/>
    <user username="admin" password="passme" roles="tomee-admin, admin-gui, manager-gui"/>
    ```
- derbyclient.jar in lib Ordner kopieren
- Datasource in conf/tomee.xml setzen:
    ```
    <Resource id="jdbc/dbDS" type="DataSource">
            #dbDS Datasource

            JdbcDriver = org.apache.derby.jdbc.ClientDriver
            JdbcUrl = jdbc:derby://localhost:1527/db;create=true
            Password = app
            UserName = app
        </Resource>
    ```
 #### Projekt deployen
- Wichtig: Kein Hibernate verwenden!
- persistence.xml:
    ```
    <jta-data-source>jdbc/dbDS</jta-data-source>
    <properties>
            <property name="javax.persistence.schema-generation.database.action" value="drop-and-create"/>
        </properties>
    ```
- Edit Configuration -> TomEE Server Local
- Pfad auswählen
- Fix Deployment

Fertig!


## Liberty

#### Server konfigurieren

- /bin/server create -> Erstellt Server
- usr/servers/defaultServer/server.env -> JAVA_HOME setzen
    ```
    JAVA_HOME=/usr/lib/jvm/java-8-oracle
    ```
- /bin/installUtility install adminCenter-1.0
- usr/servers/defaulServer/server.xml auf folgendes ändern (Passwort ist "passme" encoded):
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <server description="new server">
    <featureManager>
        <feature>adminCenter-1.0</feature>
        <feature>javaee-8.0</feature>
        <feature>localConnector-1.0</feature>
    </featureManager>
    <applicationMonitor updateTrigger="mbean" />
    <remoteFileAccess>
        <writeDir>${server.config.dir}</writeDir>
    </remoteFileAccess>
    <basicRegistry id="basic" realm="BasicRealm">
        <user name="admin" password="{xor}Lz4sLDI6" />
    </basicRegistry>
    <administrator-role>
        <user>admin</user>
    </administrator-role>
    <httpEndpoint id="defaultHttpEndpoint" httpPort="9080" httpsPort="9443" />
    <applicationManager autoExpand="true" />
    </server>
    ```
- https://localhost:9443/adminCenter/
- Server Config
- Add Child -> JDBC Driver -> id: derbydb-connector
    - Add Child -> Shared Library -> Name: DerbyLib
    - Add Child -> Fileset
        - Id: DerbyFileset
        - Base directory: ${shared.resource.dir}/DerbyLibs
        - Includes pattern: derbyclient.jar
- Add Child -> Data Source
    - Id: DbDS
    - JNDI name: jdbc/DbDS
    - JDBC driver reference: derbydb-connector
    - Add Child -> Derby Netowrk Client Properties
        - Create database: create
        - Database name: db
        - Server name: localhost
        - Port number: 1527
        - Password: app
        - User: app
- usr/servers/defaultServer/server.xml sollte jetzt so ausschauen:
    ```
    ...
    <jdbcDriver id="derbydb-connector">
    <library name="DerbyLib">
      <fileset id="DerbyFileset" includes="derbyclient.jar" dir="${shared.resource.dir}/DerbyLibs" />
    </library>
    </jdbcDriver>
    <dataSource id="DbDS" jndiName="jdbc/DbDS" jdbcDriverRef="derbydb-connector">
        <properties.derby.client createDatabase="create" databaseName="db" serverName="localhost" portNumber="1527" password="app" user="app" />
    </dataSource>
    ...
    ```
 #### Projekt deployen
- persistence.xml:
    ```
    <jta-data-source>jdbc/dbDS</jta-data-source>
    <properties>
            <property name="javax.persistence.schema-generation.database.action" value="drop-and-create"/>
        </properties>
    ```
- Edit Configuration -> WebSphere Server
- Pfad auswählen
- Fix Deployment

Fertig!