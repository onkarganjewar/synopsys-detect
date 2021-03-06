<#-- kk edited 1.8.20 -->
# Introduction

${solution_name} analyzes your software project to identify open source component dependencies.

${solution_name} consolidates the functionality of ${blackduck_product_name}(tm) and Coverity(tm) on Polaris(tm) to support
Software Composition Analysis (SCA: open source software detection) and Static Application Security Testing (SAST: static code analysis).
${solution_name} makes it easier to set up and scan code bases for a variety of languages and package managers.
${solution_name} leverages multi-factor discovery techniques to scan software projects and directories.
${solution_name} runs on Windows, Linux, and MacOS. It is available through GitHub, under a permissive
Apache license and does not require pre-installation or configuration.
For more information, refer to [Requirements](10-requirements.md).

${solution_name} can be used either for ${blackduck_product_name} SCA (open source software detection), or for Coverity on Polaris SAST (static code) analysis, or both simultaneously.
Although ${solution_name} can support large projects, care should be taken to ensure that scans are performed to optimize platform performance and produce manageable and meaningful results.
For applications containing multiple sub-projects, it may be advantageous to scan sub-projects separately and combine results as a project of projects, or other techniques.

## A typical ${solution_name} run

While there are many variations on a ${solution_name} run, a typical ${solution_name} run performs the steps described
as follows. In this example, the user has provided ${blackduck_product_name} connection details through property settings
to ${solution_name}, signalling that results (project dependency details)
are to be uploaded to ${blackduck_product_name}.

In a typical run, ${solution_name}:

1. Uses the project's package manager to derive the hierarchy of dependencies known to that package manager.
For example, on a Maven project, ${solution_name} executes an "mvn dependency:tree" command,
and derives dependency information from the output.
2. Runs the ${blackduck_signature_scanner_name} on the project. This may identify additional dependencies
not known to the package manager (for example, a .jar file copied into the project directory).
3. Uploads both sets of results (dependency details) to ${blackduck_product_name} creating the project/version
if it does not already exist. ${blackduck_product_name} uses the uploaded dependency information
to build the Bill Of Materials (BOM) for the project/version.

## ${solution_name} processing in more detail

${solution_name} processing is divided into three phases:

* Initialization
* Run
* Cleanup

### Initialization phase

During the initialization phase, ${solution_name} performs verification checks on the user-provided configration, checks to see if it
can connect to any external systems needed for the run, and creates any required directories.

### Run phase

During the run phase, ${solution_name} processes an ordered list of [tools](components/tools.md), invoking all that apply.
Tool applicability depends on how ${solution_name} is configured; in other words, the property values you set.
The detector tool runs by default. The ${blackduck_signature_scanner_name} tool runs by default when ${blackduck_product_name} connection
details are provided.

The detector tool invokes all applicable [detectors](components/detectors.md).
Detector applicability depends on what ${solution_name} finds in your project. For example, if ${solution_name}
finds a pom.xml file, it runs the Maven detector. If it finds Gradle files, it runs the Gradle detector.

In the typical run previously described, two tools are applied: the detector tool which ran the Maven detector,
and the ${blackduck_signature_scanner_name} tool which ran the ${blackduck_signature_scanner_name}.

At the end of the run phase, ${solution_name} uploads results to the appropriate external
system(s) (${blackduck_product_name} and/or ${polaris_product_name}), and optionally perform post actions, such as generating
a risk report or checking for policy violations.

In the typical run previously described, ${solution_name} uploads ${blackduck_product_name} results from
the Maven detector and the ${blackduck_signature_scanner_name}.

### Cleanup phase

During the cleanup phase, ${solution_name} removes temporary files and directories before exiting.

## Controlling ${solution_name} processing

For more information on controlling the tools and detectors executed by ${solution_name}, refer to
[Including/excluding tools/detectors](30-running.md#including-and-excluding-tools-and-detectors).
