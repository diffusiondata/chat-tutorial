# Chat Tutorial - Chapter 2

This is the final source for chapter 2 of our [chat tutorial](https://www.pushtechnology.com/blog/creating-a-chat-application-part-1).

It includes the improvements at the end of the tutorial, and also adds some CSS to make the final result look nicer.

## Requirements

* **Node.js** - To install Angular and the Diffusion client library.
* **Diffusion** - You will need to set up a Diffusion server instance.
* **Java** - The control client is based on Java.
* **Maven** - Maven is responsible for the Java depencies that diffusion is using.

## Building

Run these commands in a console of your choice.

Run `npm install` to fetch the packages.

``` BASH
npm install
```

Build the project with the `npm` script defined in `package.json` .

``` BASH
npm run build
```

``` oook
INSTRUCTIONS FOR JAVA CONTROL CLIENT.
```

## Running

Start Diffusion Server by navigating to the `bin` folder inside your installation and run the script for your operating system. More information can be found in the [Starting the Diffusion server](https://docs.pushtechnology.com/docs/6.2.0/manual/html/administratorguide/server/starting_basic.html) page of the documentation.

``` BASH
WINDOWS: .\diffusion.bat
LINUX|UNIX: ./diffusion.sh
```

Navigate to where you cloned this project and use the `start` script for npm in `package.json` to run the project.

``` BASH
npm run start
```

``` oook
INSTRUCTIONS FOR JAVA CONTROL CLIENT.
```

Using the default configuration, open [http://localhost:4200](http://localhost:4200) in a browser of your choice.
