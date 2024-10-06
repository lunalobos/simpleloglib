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
    "threads": 10,
    "batchSize": 100,
    "appenders": [
        {
            "name": "DatabaseAppender",
            "type": "jdbc",
            "connectURL": "jdbc:sqlite:sample.db",
            "tableName": "application_log",
            "columns": [
                {
                    "name": "event_id"
                },
                {
                    "name": "event_date"
                },
                {
                    "name": "level"
                },
                {
                    "name": "message"
                },
                {
                    "name": "logger"
                },
                {
                    "name": "throwable"
                }
            ],
            "filter": {
                "level": "DEBUG"
            }
        },
        {
            "name": "ConsoleAppender",
            "type": "console",
            "filter": {
                "level": "INFO"
            }
        },
        {
            "name": "FileAppender",
            "type": "file",
            "filePath": "error.log",
            "filter": {
                "level": "ERROR"
            }
        }
    ],
    "layout": "[%level] %date - %logger - %thread : %msg %throwable"
}

```

### Appenders
There are three tested types of appenders: ConsoleApppender, JDBCAppender and FileAppender. In the other hand
HttpAppender it is still experimental.

You can add an HttpAppender with this configuration inside the appenders array:

```json

{
    "name": "HttpAppender",
    "type": "http",
    "connectURL": "<insert your api URL>",
    "authorization": "<insert yout authorization string if any or set this to null>",
    "filter": {
        "level": "TRACE"
    },
    "layout": "[%level] %date - %logger - %thread : %msg %throwable"
}

```
