# relic-app
A simple application capable of parsing files and text input for the top one-hundred most common three-word sequences. These sequences are printed in descending order of frequency.

## Table of Contents

## Setup and Installation
Follow these steps to set up the application development environment, package it into a distributable jar, and execute it.

### Dependencies
The following dependencies must be installed on your system:
* Java JDK 8 or higher must be installed, see [here](https://www.oracle.com/java/technologies/downloads/)
* Maven 3.5.0 or higher must be installed, see [here](https://maven.apache.org/install.html)

Verify the dependencies are installed and available on your Path.
```
$ java --version
java version "17.0.2" 2022-01-18 LTS
...
$ mvn -v
Apache Maven 3.8.4
...
```
### Project
#### Clone
Clone the project from the Github repository.
```
$ git clone https://github.com/vitaFrui/relic-app.git
```

#### Build
Build the application using Maven. This will package the project and its dependencies into a distributable jar as well as execute all automated unit tests.
```
mvn package
```

#### Running Unit Tests
Automated JUnit tests can be run easily by using Maven: `mvn test`.

## Usage
Once the application is packaged into a distributable jar you can execute it against input. There are a variety of ways to provide input to the application.

### Supplying Input via Program Arguments
File paths can be provided to the application via program arguments:
```
$ java -jar relic-app-1.0.0-SNAPSHOT.jar ../texts/moby-dick.txt
```
Multiple files can also be provided:
```
$ java -jar relic-app-1.0.0-SNAPSHOT.jar ../texts/moby-dick.txt ../texts/brothers-karamazov.txt
```
Running the program with multiple files in the program arguments will process them _in aggregate_. Any word sequences will be recorded and counted across _all_ processed files.

For example, if `moby-dick.txt` has the phrase "one of the" occur thirty times and `brothers-karamazov.txt` has it occur twenty-six times - then the final result will have "one of the" recorded with fifty-six occurences. If this is enough to make one of the top one-hundred word sequences it will be reported as such.

A system property can be set to disable aggregate processing. By supplying `-DresetEach` as a JVM argument the application will process and report each argument separately - as if the application had been executed two separate times with each file provided as a single argument.
```
$ java -DresetEach -jar relic-app-1.0.0-SNAPSHOT.jar ../texts/moby-dick.txt ../texts/brothers-karamazov.txt
```

### Supplying Input via StdIn
StdIn can be piped into the application directly __if__ no program arguments are provided. Any input provided via StdIn will be processed as a singular entity.
```
$ cat ../texts/*.txt | java -jar target/relic-app-1.0.0-SNAPSHOT.jar
```

## Future Goals
What would I do if given more time?

* __Make the application more dynamic.__ I built out most of the application to be able to accept parameters to change the sequence-size and result-count. However, I did not provide a simple way for these parameters to be set when executing the application.
* __Make use of parallelization.__ In order to improve performance parallelization would've been a great feature to implement. Especially for larger inputs.
* __Give further scrutiny to the handling of unicode characters.__ While I think my approach is fairly robust for the time and sample set - I am positive I did not account for all possible scenarios.

## Known Bugs
There are no definitively known bugs, but I am fairly certain more scrutiny can be afforded to the handling of unicode characters as well as verifying word counts over more robust test sets.
