# filestats-app

Java application monitoring given (as parameter) directory and printing (to sys out) various statistics.

### Building

Build with maven, to build it with test run

``mvn package``

### Architecture

There are 3 main bounded contexts/application layers:

* com.hicx.filestats.io - for retrieving file contents from directory
* com.hicx.filestats.files - readers (parsers) of supported file types
* com.hicx.filestats.statistics - supported statistics processors

#### io

As possibly in the future there will be need to support other than local filesystem file storages (eg.: amazon's s3) io
package is an abstraction layer allowing future extension. 

#### files

TODO

#### statistics

TODO

#### main app package

All independent pieces are glued together in 'com.hicx.filestats' package - entry point is App class.

#### architecture remarks

I tried to keep dependency loosely coupled as possible - between modules there's only one - files -> io.
Each package has it's well defined public interfaces with implementation hidden on default or private scope.

I was thinking on using spring to not have care about dependency management but in the end boiler plate code doesn't seem
that big and I didn't add it

### Testing

I used TDD for statistics and files module.
Integration tests take a while - monitoring of new file take some time - area to improve.

### Summary

I didn't have time to properly manage:
* file reader should have some limitation of chunk size (per new line can be too much)
* encoding - for simplicity I'm using system default
* proper unit tests for io and main package
* fat jar maven configuration (is it needed?)
* I think I have a bug as I'm moving also not supported/processed files - should that be the case?