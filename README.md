# project-manager-app

COMP 354 Project Management Application

## Installation

You need:

* Java SE 8

* Eclipse Luna

* Window builder ( eclipse plugin )

Then you can clone this repo and import the project into Eclipse.

For information about window builder see the pinned document **window-builder-install.pdf** on the development channel.

## Running the app

Run the **main** function inside the class [App](blob/master/src/ateamcomp354/projectmanagerapp/App.java)

## Development guidelines

### User interface

Refer to the pinned document **using-window-builder.pdf** on the development channel.

### "Business" logic

Keep business logic out of the UI code. For example, when deleting a project don't put the SQL code into the delete button's
click event listener. Instead make the delete button's click event listener delegate the work to an interface class.

For an idea of what an interface class should look like refer to
[ProjectService](blob/master/src/ateamcomp354/projectmanagerapp/services/ProjectService.java).
For an idea on how to obtain an instance of that interface refer to
[App.getApplicationContext](blob/master/src/ateamcomp354/projectmanagerapp/App.java#L51).

### Database interaction

Use generated code by JOOQ to interact with the database. Refer to the class
[JooqExamples](blob/df2be3121c516cd47410e8d66e5cde3a99d676a7/src/ateamcomp354/projectmanagerapp/JooqExamples.java)
for usage examples. To learn more about using JOOQ refer to its (manual)[http://www.jooq.org/doc/3.6/manual/].

### Database schema

Each database table should be written as a **create table statement** in its own sql file. Refer to
[ateamcomp354.projectmanagerapp.dataAccess.ddl package](tree/master/src/ateamcomp354/projectmanagerapp/dataAccess/ddl)
for examples.

Then each **create table statement** file should be used in
[DatabaseManager.createTables](blob/master/src/ateamcomp354/projectmanagerapp/dataAccess/DatabaseManager.java#L60)
function.

When you modify the database schema make sure you also re-generate the java code. The next section explains the process.

## Generating java code

With a database schema we can use JOOQ to generate java code to facilitate interacting with the database. See [Database interaction](#database-interaction)
about using the generated code.

Run the **main** function inside the class
[GenerationTool](blob/master/src/ateamcomp354/projectmanagerapp/jooq/GenerationTool.java)
to generate java code.

You can find all the generated code in the [org.jooq.ateamcomp354.projectmanagerapp package](tree/master/src/org/jooq/ateamcomp354/projectmanagerapp)

### Modifying the generation procedure

Refer to the file [jooq-code.xml](blob/master/jooq-code.xml) and JOOQ's manual for [code generation](http://www.jooq.org/doc/3.6/manual/code-generation/codegen-advanced/)

### Mapping int columns to booleans

SQLite does not support a boolean column type. Instead an INT with a value of 0 or 1 can be used. However, in the generated
java code we would want a boolean primitive.

You can use our custom type
[C_Boolean](blob/master/jooq-code.xml#L30)
in the jooq-code.xml file to convert desired int columns in the database to
Boolean objects. You can find the code for the converter in the class
[C_BooleanConverter](blob/master/src/ateamcomp354/projectmanagerapp/jooq/converters/C_BooleanConverter.java)

### Mapping int columns to enums

SQLite does not support a enum column type. Instead an INT column can be used and it would contain contain valid ordinals of an enum.
However, in the generated java code we would want to use an enum object.

You can create enum custom types and converters for JOOQ. Refer to the
[Status](blob/master/jooq-code.xml#L24)
custom type and class
[StatusConverter](blob/master/src/ateamcomp354/projectmanagerapp/jooq/converters/StatusConverter.java)
for examples on how to create custom enum converters.

## FAQ

**I run the app but get a database exception**

If you just pulled changes into your local repo chances are your database file is out of date. The database.db file that
the app uses is not tracked by git. The database is also not updated across commits.

Delete the **database.db** file and restart the app. The app creates a new database.db file on startup if none exists.

