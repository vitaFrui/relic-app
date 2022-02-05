# relic-app
A simple application capable of parsing files and text input for the top one-hundred most common three-word sequences. These sequences are printed in descending order of frequency.

## Table of Contents
* [Setup and Installation](#setup-and-installation)
  + [Dependencies](#dependencies)
  + [Project](#project)
    - [(Optional) Use the Release Jar](#use-the-release-jar)
    - [Clone](#clone)
    - [Build](#build)
    - [Running Unit Tests](#running-unit-tests)
* [Usage](#usage)
  + [Supplying Input via Program Arguments](#supplying-input-via-program-arguments)
    - [Disable Aggregate Processing](#disable-aggregate-processing)
  + [Supplying Input via StdIn](#supplying-input-via-stdin)
  + [Sample Output](#sample-output)
* [Future Goals](#future-goals)
* [Known Bugs](#known-bugs)


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
#### Use the Release Jar
In lieu of the other setup steps below, simply navigate to the [Relic App Release Version 1.0.0](https://github.com/vitaFrui/relic-app/releases/tag/1.0.0) and download the jarfile from the binaries provided. You can use the packaged jar in conjunction with the [Usage](https://github.com/vitaFrui/relic-app#usage) guide below.

If you opt to, though, you can retrieve the source code and package the project yourself.

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
$ java -jar relic-app-1.0.0.jar ../texts/moby-dick.txt
```
Multiple files can also be provided:
```
$ java -jar relic-app-1.0.0.jar ../texts/moby-dick.txt ../texts/brothers-karamazov.txt
```
Running the program with multiple files in the program arguments will process them ___in aggregate___. Any word sequences will be recorded and counted across _all_ processed files.

For example, if we run the above multi-file execution and `moby-dick.txt` has the phrase "one of the" occur thirty times while `brothers-karamazov.txt` has it occur twenty-six times - then the final result will have "one of the" recorded with fifty-six occurences. If this is enough to make one of the top one-hundred word sequences it will be reported as such.

#### Disable Aggregate Processing
A system property can be set to disable aggregate processing. By supplying `-DresetEach` as a JVM argument the application will process and report each argument separately - as if the application had been executed two separate times with each file provided as a single argument.
```
$ java -DresetEach -jar relic-app-1.0.0.jar ../texts/moby-dick.txt ../texts/brothers-karamazov.txt
```

### Supplying Input via StdIn
StdIn can be piped into the application directly __if__ no program arguments are provided. Any input provided via StdIn will be processed as a singular entity.
```
$ cat ../texts/*.txt | java -jar target/relic-app-1.0.0.jar
```

### Sample Output
Regardless of which input method you use, you can expect the output to look something like below:
```
$ cat ../texts/*.txt | java -jar target/relic-app-1.0.0-SNAPSHOT.jar
Parsing StdIn

Here are the most common word sequences for StdIn

Sequence                            | Count
-------------------------------------------
out of the                          | 95
the sperm whale                     | 86
one of the                          | 81
a sort of                           | 80
of the whale                        | 78
the old man                         | 74
the white whale                     | 72
it was a                            | 60
that he was                         | 59
of the sea                          | 58
he did not                          | 57
part of the                         | 57
there was a                         | 57
as soon as                          | 52
at the time                         | 52
i don't know                        | 50
that he had                         | 47
do you know                         | 46
and in the                          | 45
in the world                        | 45
for a moment                        | 44
of the sperm                        | 43
he had been                         | 43
that it was                         | 43
there is no                         | 41
as it were                          | 41
it is a                             | 40
i want to                           | 39
he was a                            | 37
at the same                         | 37
in spite of                         | 34
he could not                        | 34
it was not                          | 34
so as to                            | 33
what do you                         | 33
up to the                           | 32
by no means                         | 32
the same time                       | 32
as though he                        | 32
to go to                            | 32
for the time                        | 32
it was the                          | 32
in the sea                          | 32
it would be                         | 31
at that moment                      | 31
a long time                         | 31
i am not                            | 31
on the contrary                     | 31
it will be                          | 31
there was no                        | 31
must have been                      | 31
all the time                        | 30
the first time                      | 29
in order to                         | 29
the bottom of                       | 29
all at once                         | 29
of the boat                         | 29
in the air                          | 29
in the end                          | 29
father pa ssy                       | 29
the end of                          | 28
it was that                         | 28
of the world                        | 28
not at all                          | 28
in the same                         | 28
of the ship                         | 28
to be the                           | 28
the sperm whale's                   | 28
and at the                          | 27
to the deck                         | 27
it is not                           | 27
he seemed to                        | 27
i know that                         | 27
to be a                             | 27
it must be                          | 27
more and more                       | 26
would have been                     | 26
and all the                         | 26
on the other                        | 26
the sea and                         | 26
he had not                          | 26
i do not                            | 26
of his own                          | 26
with the same                       | 26
one of those                        | 26
that in the                         | 26
one of them                         | 25
out of sight                        | 25
as well as                          | 25
the right whale                     | 25
you are a                           | 25
for the first                       | 25
down to the                         | 25
in his own                          | 25
him in the                          | 24
into the sea                        | 24
end of the                          | 24
there is a                          | 24
in the morning                      | 23
i have a                            | 23
===========================================
```

## Future Goals
What would I do if given more time?

* __Make the application more dynamic.__ I built out most of the application to be able to accept parameters to change the sequence-size and result-count. However, I did not provide a simple way for these parameters to be set when executing the application.
* __Make use of parallelization.__ In order to improve performance parallelization would've been a great feature to implement. Especially for larger inputs.
* __Give further scrutiny to the handling of unicode characters.__ While I think my approach is fairly robust for the time and sample set - I am positive I did not account for all possible scenarios.

## Known Bugs
There are no definitively known bugs, but I am fairly certain more scrutiny can be afforded to the handling of unicode characters as well as verifying word counts over more robust test sets.
