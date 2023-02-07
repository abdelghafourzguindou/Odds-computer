# The odds computer

The project was build using [spring boot framework](https://spring.io/projects/spring-boot).  
The project uses maven and java 17

## Web
There is only one endpoint of type post allowing to obtain the odds.
The project needs a [millennium-falcon.json file](src/main/resources/millennium-falcon.json) containing all the configuration about the Millennium Falcon.  

<u>Example:</u>
```json
{
  "autonomy": 6,
  "departure": "Tatooine",
  "arrival": "Endor",
  "routes_db": "universe.db"
}
```

The [`routes_db`](src/main/resources/universe.db) property should redirect to a SQLite database file containing the routes from the same directory

### Run the project

To run the project locally, use the command below:
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=web 
```


## CLI
The project uses [Picocli](https://picocli.info/) library to generate a CLI with Java and spring boot native to create a native executable working without JVM thanks to [GraalVM](https://www.graalvm.org/)

### Installation

To launch the project you will need the jdk17 of graalvm with the native-image tool.
- You can use [SDKMAN](https://sdkman.io/install) to manage all your SDK and install the graalVM JDK.
- Then install [native image](https://www.graalvm.org/22.0/reference-manual/native-image/#install-native-image)

### Configuration

To launch the command, you will need two json file locating in the root of the project:
1. [millennium-falcon.json file](millennium-falcon.json) containing all the configuration about the Millennium Falcon.
2. [empire.json](empire.json) containing all the information about the Empire.

The project is working with a sqlite database, the configuration file is in the root of the project:
- [universe.db](universe.db) for the schema and default dataset. It is set from the [millennium-falcon.json](millennium-falcon.json) but can be change dynamically.

### Generate the executable file

To generate the executable run the commands below
```shell
mvn clean package -Pnative -DskipTests
```
You will then get the executable file [/target/give-me-the-odds](./target/give-me-the-odds)

### The CLI
To use the CLI, add the executable's directory to your PATH variable. (Use your config file .zshrc, .bashrc, etc.)  
For a quick test you can add it to your current session with the following commands:
```shell
export PATH="YOUR/PATH/TO/THE_PROJECT/target:$PATH"
```
You can then use the command `give-me-the-odds`

```shell
~/Desktop/demo: give-me-the-odds millennium-falcon.json empire.json
```
