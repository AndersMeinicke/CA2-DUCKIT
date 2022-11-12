for the code to work you have to do the following:

1. make sure the JDK is set to version 11.

2. go to edit configurations and add tomcat local server.

3. in the pom.xlm file change the name of the artifactId and the name of the project.

4. in the pom.xml file edit the database name to the name of your database.

5. add your remote secrets to your github repository(REMOTE_USER and REMOTE_PW).

6. check that it uses the correct branch main/master in the mavenworkflow.yml file.

7. Add a mysql datasource with the database you are going to use.

8. make sure it uses the correct DB and has the right username and password in persistence.xml. 

9. commit, push and go to GitHub actions.
