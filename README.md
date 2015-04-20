# Hydra

A bespoke application generator for WS-BPEL 2.0 process descriptions.

The aim of this project is to take a WS-BPEL process description, along with any required WSDLs and XSDs, and convert it into an application that can be deployed and run as needed. Note that Hydra is not a BPEL process engine that stores and executes the process descriptions presented to it. Instead it is a system that takes those descriptions and transforms them into a standalone application that implements your business process. This application can then be deployed and managed like any other application that you might have. For example, you can deploy the application to your favourite application server, or even deploy the application to the cloud.

Further information can be found in the [High Level Overview](docs/high-level-overview.md).

## Background

The idea for this project originated from work that was done as a part of an MSc. project to transform WS-BPEL process descriptions into Communicating Sequential Process (CSP) descriptions for analysis. This work is documented in this [dissertation](https://drive.google.com/open?id=0B6e1QBk_n2I4QTZ3d0ZaVUFFVGs&authuser=0). The CSP descriptions used in this project have been refined since the original MSc. project and are now documented here, `TODO`

## Building and running tests

This project is built using maven.

## Contributing
