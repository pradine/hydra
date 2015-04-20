# High Level Overview

Hydra consists of two main components: a transformer that generates the WS-BPEL application, and a collection of libraries that are used by the generated application. One of the aims of this project is to stick as much as possible to standardised APIs, where possible. A second aim of this project is to maximise code reuse through appropriate use of open-source software, once again where possible.

## Transformer

The transformer is an application in it's own right that takes the WS-BPEL process description, along with all associated WSDLs and XSDs, and transforms them into an application over a number of phases. The first phase is *validation*. This consists of both XML Schema validation of the WS_BPEL process description itself, and also the static-analysis required by the [WS-BPEL 2.0 specification](http://docs.oasis-open.org/wsbpel/2.0/OS/wsbpel-v2.0-OS.html). The validation phase is an optional one that can be skipped, if necessary.

The second phase is the *transformation* phase where the code for the application is generated. Most of the required code will already be provided by the libraries, documented below, so that no actual code generation is necessary in this phases, except in certain scenarios e.g. implementing message correlations. So this phase consists mainly of configuring existing classes and linking them together based on the original WS-BPEL process description.

The final phase is the *packaging* phase. In this phase we gather all of the artifacts that were produced in the previous phase and package them as an application suitable for deployment. By default, this packaging step will produce a Web Application Archive (WAR) to contain the WS-BPEL application. 

## Libraries

Hydra provides libraries to support the generated WS-BPEL application. There is the main library that provides services to the application such as:

### Scheduler

This is the component responsible for managing process flow of the process instances. For further details on how the Scheduler works with the other components to achieve this see, [the process flow](process_flow.md).

### Activities

This component provides primitives that implement the WS-BPEL basic and structured activities. These primitives are based on the CSP descriptions found here, `TODO`

### Persistence

This component is responsible for persisting the state associated with the WS-BPEL process instance. All persistence tasks are delegated to the Java Persistence API (JPA).

### Alarm Manager

This component provides support for persistent timers needed to support the business process. This component is closely linked to the Persistence component.

### XML Processing

The WS-BPEL 2.0 specification only requires support for XPath 1.0 and XSLT 1.0, however, it does allow for additional XML processing standards such as XPath 2.0, XSLT 2.0, etc., to be supported as well, and this is something that we will aim to do in this project. However, due to a lack of suitable XML schema-aware open-source XSLT 2.0 processors, all XML processing is delegated to an underlying processor via an adaptor. This is to allow alternative processors to be plugged in more easily should the situation change in future. By default, this project uses an adaptor for the [Saxon HE processor](http://www.saxonica.com/welcome/welcome.xml).

There are also the binding libraries that provide web services support:

### SOAP Bindings

Most of the SOAP binding support is provided via the Java API for XML Web Services (JAX-WS). There are some limited features that are not yet supported by JAX-WS, e.g. asynchronous provider support, that would require the use of some proprietary extensions.
