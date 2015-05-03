# Hydra

[![Build Status](https://travis-ci.org/pradine/hydra.svg?branch=master)](https://travis-ci.org/pradine/hydra)

The aim of this project is to take a WS-BPEL 2.0 process description, along with any required WSDLs and XSDs, and convert it into a bespoke process engine application, that implements support only for that single process. From this description it should be clear that Hydra is not a generic process engine that stores and manages many different BPEL processes, like many other process engines available today. Instead, it is a combination of a transformer application, and a library, that together allow you to take a process description and transform it into an application that you can manage and deploy, just like any other application you may have. With this applications you can, for example, deploy and run it on your favourite application server, or even deploy it to the cloud.

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
