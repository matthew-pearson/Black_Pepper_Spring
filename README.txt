To build ready to import into eclipse, run this from the command line:
	(on Windows) gradlew.bat build eclipse
	(on Linux) ./gradlew build eclipse
Then import the project into eclipse.

To run the web-app, you can skip the above and just run this from the command line:
	(on Windows) gradlew.bat bootRun
	(on Linux) ./gradlew bootRun

To GET the saved person, browse to http://localhost:8080,
but before you do this, you'll first need to POST a person. I used a Chrome plug-in, advanced-rest-client, to POST, but there are no doubt many other ways of achieving this.
Example POST parameters: forename=David;surname=Cameron;age=49
