# SimpleLogLib

## Table of Contents
+ [About](#about)
+ [Getting Started](#getting_started)
+ [Usage](#usage)
+ [Contributing](../CONTRIBUTING.md)

## About <a name = "about"></a>
This project was created because I encountered the great complexity of logging libraries and interfaces that didn't fully meet my needs. I decided to create a library that's a bit simpler, but with some features broadly similar to existing ones. There areloggers, appenders, messages, levels but everything is much simpler, including the configuration. It is still under development.

Simplicity is sometimes needed:

- There are no custom levels—you don’t need that.
- No complicated XML configuration files; JSON is more intuitive.
- No unnecessary annotations that hide implementations—only Spring can do that.
- Java is a great language. It should be simple, and it can be simple with your help.

## Getting Started <a name = "getting_started"></a>
This project is under development, but many classes are already usable. You just need to clone the repository and install it with mvn install. Then, the LogManager class provides instances of Loggers according to the class in question. The configuration is simple, and for now, there are only two types of Appenders: console and relational database using JDBC.

### Prerequisites

You need java 21 and maven 3.8 or superior.

### Installing

```console

git clone git@github.com:lunalobos/simpleloglib.git

```

```console

cd <your cloned repo location>

mvn clean install

```

## Usage <a name = "usage"></a>

To use it just import simpleloglib.loggers.LogManager factory class and simpleloglib.Logger interface:

```java
import simpleloglib.loggers.LogManager;
import simpleloglib.Logger;

class MyClass {
    private static final Logger logger = LogManager.getLogger(MyClass.class);

    public void someMethod(){
        logger.info("hello");
    }
}


```

### Configuration
The configuration can currently be understood with an example. A file named simplelog.json should be placed in the resources folder. The Config class from the library will inspect the resources and apply the specified settings; if nothing is found, logging will be configured by default with a console appender. There are no validations yet, so if you provide an incorrect value, the behavior of the library's classes is undefined.

```json

{
    // Number of threads to be used in the thread pool for logging
    "threads": 10,

    // The batch size for batch insertions in general
    "batchSize": 100,

    // List of appenders, each appender defines where logs will be output
    "appenders": [
        {
            // Name of the appender, used for identification
            "name": "DatabaseAppender",

            // Type of the appender, in this case, jdbc for relational database logging
            "type": "jdbc",

            // The JDBC connection URL for the database
            // You must include a username and password in the URL if you're using a more 
            // advanced database. In this case, I use SQLite because I absolutely love 
            // keeping things simple.
            "connectURL": "jdbc:sqlite:sample.db",

            // Custom connection class name if needed (null by default)
            "connectionClassName": null,

            // The name of the table where logs will be stored
            "tableName": "application_log",

            // List of columns in the database table that will store log information
            "columns": [
                { "name": "event_id" },    // Unique ID for the log event
                { "name": "event_date" },  // Timestamp of the log event
                { "name": "level" },       // Log level (e.g., DEBUG, INFO, ERROR)
                { "name": "message" },     // The log message
                { "name": "logger" },      // Name of the logger instance
                { "name": "throwable" }    // Details of any thrown exception (if any)
            ],

            // Filter to specify the minimum logging level for this appender
            "filter": {
                "level": "DEBUG"  // Logs at DEBUG level or higher will be saved
            }
        },
        {
            // Name of the appender for console output
            "name": "ConsoleAppender",

            // Type of appender for console logging
            "type": "console",

            // No database connection required for console logging
            "connectURL": null,
            "connectionClassName": null,

            // No table needed for console logging
            "tableName": null,

            // No columns for console logging
            "columns": [],

            // Filter for the minimum logging level for this appender
            "filter": {
                "level": "INFO"  // Logs at INFO level or higher will be displayed in the console
            }
        }
    ],

    // The layout of the log messages, specifying how the log entries should be formatted
    "layout": "[%level] %date - %logger - %thread : %msg %throwable"
}


```
