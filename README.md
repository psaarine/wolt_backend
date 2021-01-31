# Wolt backend submission, Pyry Saarinen

## General information

Greetings! This is my submission to the wolt-backend
excercise given here:  https://github.com/psaarine/summer2021-internship  

It have done it using Java Spring Boot, Maven, Junit and Mockito.

Since you told me that you like unit testing I have built quite an extensive
unit testing for this one. Most important test cases are in the test
 folders "Basic testing"-class.


My Test cases can be inspected in the Tests-folder. If you simply want
to see how my code performs all you have to do is copy this repository
and build it using docker build -command. This will run all the unit tests.

After that you can try my application by running it on exposed port 8080.
My application automatically generates a dummy json file if there is 
no restaurants.json in the folder with my jar -file. If you want
to see my application with your own json file, all you have to do is replace
my restaurants.json with your own (remember to call it restaurants.json!)

## About unit testing

You mentioned in your job add that you love unit testing. Thats
why I have extensive unit testing in the Tests-folder. Most
important tests are done in Basic Testing class. It includes some
documentation, how ever here are some of the main points it tests:

    It asserts that program cuts off restaurants further than 1.5km
    It asserts that All sections have correct lenght
    it asserts that all sections find their correct restaurants
    it asserts online restaurants are prioritised over offline.

It will automatically run tests when you build it with Dockerfile
 how ever you can still see it again by running mvn test -command.

## About delivering

You asked that I would return my application as a zip file. How ever, you also
said that "easy testing will make you happy". So I took a liberty
of delivering it as a github-repo instead. This way I could prove
not only my ability to produce code but also demonstrate my ability
 with popular devOps tools such as Git and Docker.

On my computer I get it to run on port 8080 like this:

     sudo docker run -p 8080:8080 <built-image-name>

It will generate a restaurants.json file in the directory of the
jar file. It is built so that all of the restaurants are around point 
00.000 and 00.000. In order to watch the response, I use url of

    http://localhost:8080/discovery?lat=00.000&lon=00.000