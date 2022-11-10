In order for the startcode to work you have to do the following steps:

1. Change the JDK version to 11.
2. Add a configuration - tomcat local version 9. 
3. Head to your pom.xml file and change the name of the project and its artifact id.
4. While you're in the pom.xml file you should also change the name of the database to the one you will be using.
5. Make sure to add your remote secrets to your GitHub project: REMOTE_PW and REMOTE_USER, otherwise you can't deploy the project.
6. In the mavenworkflow.yml file check that it uses the correct branch main/master.
7. Add a mysql datasource with the database you are going to use.
8. Head to persistence.xml and make sure it uses the correct DB and has the right username and password. 
9. You can now commit and push and head to GitHub actions, and hopefully it runs with no problems if you followed all the steps above.