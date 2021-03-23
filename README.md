# Introduction

The Virtual Imaging Platform is a web portal for pipeline execution on
distributed systems. An instance of this portal deployed on the
European Grid Infrastructure is accessible at
[https://vip.creatis.insa-lyon.fr](https://vip.creatis.insa-lyon.fr).

# Development guidelines

0. Your Git username and email should be set to your First-Last name and actual email:
```
git config --global user.name "Jeanne Tartempion"                  # not "tartempion" or "jtartempion" or "root"
git config --global user.email "jeanne.tartempion@university.fr"   # not "root@localhost"
```
1. Developments are made in forks from the base repository. Developments lead to pull-requests that are merged by the owner(s) of the base repository.
2. Starting from release 1.15, we adopt the branching model described [here](http://nvie.com/posts/a-successful-git-branching-model). 
3. When working on a new feature: 
* In your fork, create a new feature branch from the development branch:
```
git checkout -b new_feature develop
```
You may name your branch anything except master, develop, release-*, or hotfix-*
3. When feature development is finished:
* Push your commits to Github (your fork).
* Make a pull request to merge feature branch (in your fork) to development branch (in the base repository).

# Production installation instructions

To be done. Until this section is written, install at your own risks or
contact us at
[vip-support@creatis.insa-lyon.fr](vip-support@creatis.insa-lyon.fr).

# Local test installation instructions

It is possible to launch a local vip instance in a tomcat on a linux machine.
This is only useful for testing purposes as most of the features are simulated and many are still to be implemented and do not work yet.
Here are the instructions to configure a local vip instance :
- create a empty folder that will contain all the configuration files and simulated data. `/path/to/vip/local/folder` will be its path used in the next instructions
- unzip the `local-config.zip` archive available in `vip-local/src/test/resources` in `/path/to/vip/local/folder`. This should contain 3 `.conf` files
- create a `$HOME/.vip` directory and a `$HOME/.vip/local-config-folder.properties` file
- put `vipConfigFolder = /path/to/vip/local/folder` in the `$HOME/.vip/local-config-folder.properties` file
- comment the `@Disabled` line in the `VipLocalConfigurationIT.java` file (in `vip-local` in `src/test/java` hierarchy)
- use the `mvn clean verify` command at the root of the project (everything should be ok)
- uncomment the `@Disabled` line in the `VipLocalConfigurationIT`

Once this is done, you should have all a `vip-portal-[...]-local.war` file in `vip-portal/target`, and many files and folders in `/path/to/vip/local/folder`.
Now, it is possible to launch a local tomcat server running vip on this configuration.

1. Put the `vip-portal-[...]-local.war` file in the `$TOMCAT_HOME/webapps` directory
2. Add the folowing lines in `$TOMCAT_HOME/conf/context.xml` (database jndi configuration)
```
<Resource name="jdbc/vip" auth="Container"  type="javax.sql.DataSource" 
       username="sa"     
       password="" 
       driverClassName="org.h2.Driver" 
       description="VIP local h2 Connection" 
       url="jdbc:h2:/path/to/vip/local/folder/vip" 
       maxActive="100" 
       maxIdle="50" />
```
3. Create or adapt the `$TOMCAT_HOME/bin/setenv.sh` file with these lines :

```
export CATALINA_OPTS="$CATALINA_OPTS -Dspring.profiles.active=local,config-file,jndi-db"
export CATALINA_OPTS="$CATALINA_OPTS -DvipConfigFolder=/path/to/vip/local/folder"
```

4. That's it, start tomcat. Access vip on `localhost:8080/vip-portal-[...]-local` (adapt with the war name and your tomcat host/port configuration)

### Local instance notes

- The default admin email/password is `admin@vip-local-test.local`/`localAdminPassword`.
- all is not working perfectly yet, expect to see some error messages. The home page, files transfers and executions shoud work.
- vip do not send email but logs them
- at the moment, logging is done in the `$HOME/.vip/vip.log` file


# Procedure to install all the VIP infrastructure

## Introduction

This procedure will explain how to install the whole vip infrastructure on 2 machines.
The first machine (called `vip-machine` is composed of the VIP-portal and of the services it needs to run (grida, sma).
The second machine (called `moteur-machine` is composed of moteur-server, its plugins and few others necessary plugins.

The installed infrastucture is meant to run only local jobs (on the moteur machine) and to use only local files.
To use dirac, additional changes not documented here (yet) are necessary.

The procedure is described for centos machines but should be adapted to any major linux distribution without much effort.

## Prerequisites

As the whole infrastucture is installed on 2 machines, some things must be done to allow the services to communicate and share things.

1. Install `apache` (`httpd`) on both machines through the package manager

2. Create the `/var/www/html/workflows` on both machines

3. A NFS share must be established on the `/var/www/html/workflows` path on the 2 machines.
It is advised to use the `moteur-machine` as the NFS server and the `vip-machine` as a NFS client.

4. The `9092` port must be opened on the `moteur-machine` and accessable from the `vip-machine`

5. A SMTP server must be accessible from the `vip-machine`

## `vip-machine` installation

TODO

## `moteur-machine` installation

0. Every folder/file created on the `moteur-machine` must belong to the apache user.
       It is advised to do the whole machine installation as root and do `chown` commands at the end.
       The `chown` commands that are necessary are documented at the end

1. If it isn't already done, `apache` (`httpd`) must be installed through the system package manager

2. Stop the apache server (`service httpd stop`)

3. Install Java JDK 8 through the system package manager (`yum install java-1.8.0-openjdk-devel`)

4. `mkdir -p /var/www/cgi-bin/m2Server-gasw3; export MOTEUR_HOME=/var/www/cgi-bin/m2Server-gasw3`

5. Install the moteur server.

It is a small c++ server already compiled and available for CentOS 7.
For other any other system, please adapt the compilation instuctions from https://github.com/virtual-imaging-platform/moteur_server to your needs and use the produced executable.
```
cd $MOTEUR_HOME
wget https://github.com/virtual-imaging-platform/moteur_server/releases/download/v1.1/moteur_server-v1.1-centos7.tar.gz
tar xzf moteur_server-*-centos7.tar.gz
rm -rf moteur_server-*-centos7*
```

6. Install moteur server scripts
 
```
export MOTEUR_SERVER_RAW_FILES=https://github.com/virtual-imaging-platform/moteur_server/raw/v1.1
wget -q ${MOTEUR_SERVER_RAW_FILES}/env.sh -O ${MOTEUR_HOME}/env.sh
wget -q ${MOTEUR_SERVER_RAW_FILES}/killWorkflow.sh -O ${MOTEUR_HOME}/killWorkflow.sh
wget -q ${MOTEUR_SERVER_RAW_FILES}/submitWorkflow.sh -O ${MOTEUR_HOME}/submitWorkflow.sh
chmod +x ${MOTEUR_HOME}/killWorkflow.sh ${MOTEUR_HOME}/submitWorkflow.sh
```

7. Install h2 server

```
mkdir /var/www/prod
export H2_ZIP=h2-2012-05-23.zip
wget -q https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/h2database/${H2_ZIP} -O /var/www/prod/${H2_ZIP}
cd /var/www/prod && unzip -q /var/www/prod/${H2_ZIP}
rm -f /var/www/prod/${H2_ZIP}
unset H2_ZIP
chown -R apache:apache /var/www/prod/h2
```

8. Start the h2 server

The h2 service must be started with the apache user with the command `java -cp /var/www/prod/h2/bin/h2-1.3.167.jar org.h2.tools.Server -tcpAllowOthers`.
It is advised to configure it as a system service and make it start automatically on machine boot.

9. Install grida

```
mkdir /var/www/prod/grida
wget -q  https://github.com/axlbonnet/GRIDA/releases/download/2.1.0-alpha/grida-server-2.1.0-alpha.jar -O /var/www/prod/grida/grida-server-2.0.1.jar
mkdir /usr/share/httpd/.dirac
mkdir /usr/share/httpd/.cache
chown apache:apache /var/www/prod/grida /usr/share/httpd/.dirac /usr/share/httpd/.cache 
```

10. Configure and start grida

Copy https://github.com/virtual-imaging-platform/GRIDA#server-configuration in `/var/www/prod/grida/grida-server.conf` and change `commands.type` to `local`.

Grida must be started with the apache user with the command `java -jar grida-server-2.0.1.jar` in the `/var/www/prod/grida` folder.
It is advised to configure it as a system service and make it start automatically on machine boot.

11. Install moteur2 jars

```
cd $MOTEUR_HOME
wget https://github.com/virtual-imaging-platform/Complementary-tools/raw/develop/moteur/moteur2.jar
mkdir worflow-agent-0.2
cd worflow-agent-0.2
wget https://github.com/virtual-imaging-platform/Complementary-tools/raw/develop/moteur/worflow-agent-0.2/workflow-agent-0.2.jar
cd ..
mkdir libs plugins
cd libs
wget https://github.com/virtual-imaging-platform/GASW/releases/download/v3.6.0/gasw-3.6.0.jar
wget https://github.com/virtual-imaging-platform/GRIDA/releases/download/2.0.1/grida-client-2.0.1.jar
wget https://github.com/virtual-imaging-platform/GRIDA/releases/download/2.0.1/grida-common-2.0.1.jar
wget https://github.com/virtual-imaging-platform/Moteur2-Grida/releases/download/1.1/moteur2-grida-1.1.jar
cd ../plugins
wget https://github.com/virtual-imaging-platform/GASW-Dirac-Plugin/releases/download/V3.5.0/gasw-dirac-plugin-3.5.0-jar-with-dependencies.jar
wget https://github.com/virtual-imaging-platform/GASW-Healing-Plugin/releases/download/v3.3.1/gasw-healing-plugin-3.3.1-jar-with-dependencies.jar
wget https://github.com/virtual-imaging-platform/Moteur2-WorkflowsDB-Plugin/releases/download/v1.5/moteur2-workflowsdb-plugin-1.5-jar-with-dependencies.jar
wget https://github.com/virtual-imaging-platform/GASW-Stats-Plugin/releases/download/v3.2.0/gasw-stats-plugin-3.2.0-jar-with-dependencies.jar
wget https://github.com/virtual-imaging-platform/GASW-H2-Plugin/releases/download/3.0/gasw-h2-plugin-3.0-jar-with-dependencies.jar
wget https://github.com/virtual-imaging-platform/GASW-Local-Plugin/releases/download/3.0/gasw-local-plugin-3.0-jar-with-dependencies.jar
cd ..
mkdir conf
```

12. Add configuration

TODO

13. Change rights

TODO
```
chown -R apache:apache $MOTEUR_HOME /var/www/prod
```

14. Start apache









