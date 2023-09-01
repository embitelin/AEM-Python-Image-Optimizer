# AEM Image Optimizer using Python
## Introduction

Image compression requires less bandwidth when transmitted over the internet or downloaded from a webpage, reducing network congestion, and speeding up content delivery. By reducing the file size, more images can be stored in the given amount of disk or memory space.
Compression of the original image to fifty percent without losing the quality that must be applied to required images in the DAM repository with JPEG, and PNG extensions.
The Adobe Experience Manager with Python Integration provides a seamless approach to processing the Image and gives the output in the desired format with compression.

## Getting Started
### Project structure
The Project structure is built by using Maven archetypes with autoInstallPacakage profiles

The code is organized in the src folder. The project is organized in a basic plugin structure. The custom process used in the workflow is located under core/src/main/java/worflows. The Workflow in turn calls the PythonAEMService which is capable of running the python command installed from the Windows/Mac OS.

The Python script is located in the classpath, PythonAEMService loads the python script and executes which returns the output to the service

Based on run modes, the custom configuration is used to fetch the Instance URL, User Name, and Password from the OSGI configuration

![folderCreation](/images/technical_topology.png)

## Architecture
![architecture](/images/architecture.png)


## Process Flow
![process_flow](/images/process_flow.png)

This is a project template for AEM-based applications. It is intended as a best-practice set of examples as well as a potential starting point to develop your own functionality.
## Modules

The main parts of the template are:

* core: Java bundle containing all core functionality like OSGi services, listeners, or schedulers, as well as component-related Java code such as servlets or request filters.
* it.tests: Java-based integration tests
* ui.apps: contains the /apps (and /etc) parts of the project, i.e. JS&CSS clientlibs, components, and templates
* ui.content: contains sample content using the components from the ui.apps
* ui.config: contains runmode-specific OSGi configs for the project
* ui.frontend: an optional dedicated front-end build mechanism (Angular, React or general Webpack project)
* ui.tests: Selenium-based UI tests
* all: a single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies
* analyse: This module runs analysis on the project which provides additional validation for deploying into AEMaaCS

## Install, Build and Deployment

Make sure to install the following software

    *Java

    *Python

    *Maven

This Project used maven Archetype 39 to create the initial structure of the project. The Maven archetype command is listed below:

mvn -B archetype:generate \
 -D archetypeGroupId=com.adobe.aem \
 -D archetypeArtifactId=aem-project-archetype \
 -D archetypeVersion=39\
 -D aemVersion=cloud \
 -D appTitle="AEM Image Optimizer" \
 -D appId="pythonAEM" \
 -D groupId=" com.adobe.aem.python" \
 -D frontendModule=general \
 -D includeExamples=n \
 -D includeCommerce=y

To install Image and PIL libraries first PIP has to be installed

Steps to install PIP

      ** Once Python is installed, Open the command prompt and the curl command

       curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py

      ** Give the command, this installs PIP

       python get-pip.py

      ** Once the PIP is installed, install the Image and PIL libraries by providing the below commands

        pip install Image

        pip install PIL

For build and deployment

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

Or alternatively

    mvn clean install -PautoInstallSinglePackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

Or to deploy only a single content package, run in the sub-module directory (i.e `ui.apps`)

    mvn clean install -PautoInstallPackage
## Testing

•	In AEM Author, Digital Asset Management repo, select an image and start the DAM Update Asset Workflow
•	The payload path of the image is sent to Python for Compression
•	Python Libraries such as Image and PIL are imported, and the logic of compression to 50% is applied to the image without losing the quality.
•	Python script is located in the source file the Asset Workflow will pick the script from the classpath and execute Python script
•	The compressed image is again uploaded to DAM using Asset API post call with the image name as compressed at the end along with the extension. Example: embitel_compressed.jpg.
    Note: Please update the script to replace the original image with a compressed image
•	Now compressed images can be used on the images.


## Configuration
The custom configuration is used to fetch the Instance URL, User Name, and Password from the OSGI configuration.
 - config
 - config.author - As we run the workflow in author, the custom configuration values need to be updated here. This configuration can be updated with the author AEM instance, Username, and password for that instance
 - config.publish
 - config.prod
 - config.stage
## ClientLibs

The frontend module is made available using an [AEM ClientLib](https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/clientlibs.html). When executing the NPM build script, the app is built and the [`aem-clientlib-generator`](https://github.com/wcm-io-frontend/aem-clientlib-generator) package takes the resulting build output and transforms it into such a ClientLib.

A ClientLib will consist of the following files and directories:

- `css/`: CSS files which can be requested in the HTML
- `css.txt` (tells AEM the order and names of files in `css/` so they can be merged)
- `js/`: JavaScript files which can be requested in the HTML
- `js.txt` (tells AEM the order and names of files in `js/` so they can be merged
- `resources/`: Source maps, non-entry point code chunks (resulting from code splitting), static assets (e.g. icons), etc.

## Maven settings

The project comes with the auto-public repository configured. To set the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html
