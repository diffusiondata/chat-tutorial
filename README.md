# Chat Tutorial - Chapter 2

This is the final source for chapter 2 of our [chat tutorial](https://www.pushtechnology.com/blog/creating-a-chat-application-part-1).

It includes the improvements at the end of the tutorial, and also adds some CSS to make the final result look nicer.

## Requirements

* **Node.js** - To install Angular and the Diffusion client library.
* **Diffusion** - You will need to set up a Diffusion server instance.
* **Java** - The control client is based on Java.
* **Maven** - Maven is responsible for the Java depencies of the control client.

## Building

Run these commands in a console of your choice.

### Build Web

Enter the `.\web\` directory and run `npm install` to fetch the packages.

``` BASH
npm install
```

In the same path, build the project with the `npm` script defined in `package.json` .

``` BASH
npm run build
```

### Build Control

Enter the `.\control\` directory and run `mvn clean install` to build the control client.

``` BASH
mvn clean install
```

## Running

### Run Diffusion

Start Diffusion Server by navigating to the `bin` folder inside your installation and run the script for your operating system. More information can be found in the [Starting the Diffusion server](https://docs.pushtechnology.com/docs/6.2.0/manual/html/administratorguide/server/starting_basic.html) page of the documentation.

WINDOWS: `.\diffusion.bat`

LINUX|UNIX: `./diffusion.sh`

### Run Web

Navigate to where you cloned this project, enter the `.\web\` path and use the `start` script for npm in `package.json` to run the project.

``` BASH
npm run start
```

### Run Control

Execute the `control-1.0-SNAPSHOT.jar` file in `./control/target/`.

``` BASH
java -jar ./control/target/control-1.0-SNAPSHOT.jar
```

### Open in Browser

Using the default configuration, open [http://localhost:4200](http://localhost:4200) in a browser of your choice.
