# Chat Tutorial - Chapter 1

This is the optimized source from our Chat Tutorial that can be found in [here](https://www.pushtechnology.com/blog/creating-a-chat-application-part-1).

## Requirements

* **Node.js** - To install Angular and the Diffusion client library.
* **Diffusion** - You will need to set up a Diffusion server instance.

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

Using the default configuration, open [http://localhost:4200](http://localhost:4200) in a browser of your choice.
