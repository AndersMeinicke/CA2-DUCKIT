
*This project is meant as start code for projects and exercises given in Flow-1+2 (+3 using the security-branch) at http://cphbusiness.dk in the Study Program "AP degree in Computer Science"*

*Projects which are expected to use this start-code are projects that require all, or most of the following technologies:*
 - *JPA and REST*
- *Testing, including database test*
- *Testing, including tests of REST-API's*
- *CI and CONTINUOUS DELIVERY*

## Flow 2 week 1

### Preconditions
*In order to use this code, you should have a local developer setup + a "matching" droplet on Digital Ocean as described in the 3. semester guidelines* 

### Getting Started

This document explains how to use this code (build, test and deploy), locally with maven, and remotely with maven controlled by Github actions
 - [How to use](https://docs.google.com/document/d/1rymrRWF3VVR7ujo3k3sSGD_27q73meGeiMYtmUtYt6c/edit?usp=sharing)

### JPA snippets

### Setup in Intellij
- open view->too windows->persistence
- open the Database tab and create a new data source (remember to point to a database event though this is already written in the persistence unit. This is necessary in order to use the JPQL console)
- in the persistence window right click the pu or an entity and choose "console"
- write a jpql query in the console and execute it.
### In netbeans it is much simpler
- just right click the pu and choose: "Run JPQL query"

### Create model in workbench (cannot be done from Intellij - No model designer yet)
- file-> new model
- dobbelclick the mydb icon and change to relevant database (create one first if needed)
- click the Add Diagram icon
- click the table icon in the left side panel and click in the squared area to insert new table
- dobbelclick the new table and change name and add columns (remember to add a check mark in 'ai' for the primary key)
- do the process again to add a second table
- now in the panel choose the 'non identifying relationship' on to many
- click first on the child table (the one that should hold the foreign key) and then on the parent. A new relationship was now added.
- When done with designing - goto top menu: Database->forward engineer.
  - Check that all settings looks right and click continue
  - click continue again (no changes needed here)
  - Make sure the 'Export mysql table objects' is checked and Show filter to make sure that all your tables are in the 'objects to process' window -> click continue
  - Verify that the generated script looks right -> click continue
  - click close and open the database to see the new tables, that was just created.

### create entities from database in Intellij (Persistence mappings)
- From inside the Persistence window:
- Right-click a persistence unit, point to Generate Persistence Mapping and select By Database Schema.
- Select the 
  - data source 
  - package
  - tick tables to include
  - open tables to see columns and add the ones with mapped type: Collection<SomeEntity> and SomeEntity
  - click OK.

### In netbeans it is much easier
- Right click project name -> new -> persistence -> Entity classes From Database -> choose database connection from list -> add the tables you need -> Finish



