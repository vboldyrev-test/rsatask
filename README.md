## Homework project

### Preliminary requirements
These instructions describe the installation process on a **CentOS 7** system.

The following software packages will be installed first.

  * JDK 8 (tested with OpenJDK and Oracle JDK)
  * Maven
  * Cassandra
  * git
  
#### Installation  
  
Enter the following commands in a terminal session.


1. Install the Maven and JDK packages.

       # yum install java-1.8.0-openjdk
       # yum install maven

2. Install Cassandra.
 
   Make sure that `/etc/yum.repos.d/datastax.repo` contains the following:

       [datastax]
       name = DataStax Repo for Apache Cassandra
       baseurl = http://rpm.datastax.com/community
       enabled = 1
       gpgcheck = 0
       
   Execute the command:
       
       # yum install dsc30

3. Make sure that git is installed.

       # yum install git

4. Configure and start Cassandra.
   
   Edit `/etc/cassandra/conf/cassandra.yaml` as follows.
   
   - Find the `authenticator` parameter and set it to `PasswordAuthenticator`.
   - Set `authorizer` to `CassandraAuthorizer`.
   - Save the changed file.
   
   Execute this command to start Cassandra:
       
       # service cassandra start
       
   Start the Cassandra shell, `cqlsh`.
   
       $ cqlsh -u cassandra -p cassandra
       
   Create a new user:
   
       cassandra@cqlsh> CREATE USER 'test' WITH PASSWORD 'test123' SUPERUSER;
       
   **Note:** If you'd like to use a different set of credentials, make sure to change the default values in `src/main/resources/application.properties` before building the project.    
   Use `Ctrl-D` to leave `cqlsh`.              
5. Clone this repo and change the current directory.

       $ git clone https://github.com/vboldyrev-test/rsatask
       $ cd rsatask
       
6. Build the project.
       
       $ mvn install
       
7. Run the application.

       $ java -jar /target/rsa-task-1.0-SNAPSHOT.jar              
       
   **Note:**  By default, Tomcat is configured to listen for HTTP requests on port 8080.
   
   You can use additional command line options `--cassandra.contactPoint` and `--cassandra.port` to override the defaults for accessing a remote Cassandra instance.
   
   If you want to be able to access the application from the outside, you need to specify a firewall rule with the following command:
 
       # firewall-cmd --zone=public --add-port 8080/tcp --permanent
           
8. Usage of the application.

   Point your browser to `http://localhost:8080`.
   
   **Note:** If you want to use the application from a remote machine, replace `localhost` with the appropriate hostname or IP address.  
   