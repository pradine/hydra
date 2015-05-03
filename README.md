# Hydra

The aim of this project is to take a WS-BPEL process description, along with any required WSDLs and XSDs, and convert it into an application that can be deployed an run, just like any other application. Hydra is not an engine that stores and manages a set of BPEL process descriptions. Instead it is a combination of an application, and a library, that together allow you to take those process descriptions and transform them into separate stand-alone applications. With these applications you can deploy them to your favourite application server, or even deploy them to the cloud.

## Background

The idea for this project originated from work that was done as a part of an MSc. project to transform WS-BPEL process descriptions into Communicating Sequential Process (CSP) descriptions for analysis. This work is documented in this [dissertation](https://drive.google.com/open?id=0B6e1QBk_n2I4QTZ3d0ZaVUFFVGs&authuser=0). It quickly became apparent that in addition to generating CSP it should also be possible to generate Java code, and produce an application that
can be executed. And here we are today!

## Prerequisites

In order to build or use Hydra the following prerequisites must be fulfilled:

* Java 1.7 or later

## Building and running tests

This project is built using maven.

## Contributing

If you would like to contribute, information on the overall design can be found in the [High Level Overview](docs/high-level-overview.md).
