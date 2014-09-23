PixelServer
===========

Implementation of a HTTP component which just serves a 1x1 transparent pixel menat to assist with tracking user movements on websites.
It cookies the users and logs the visit also accepts various parameters which can be sent by the website implementing the pixels.

This project started based on the [series of posts on my blog regarding online privacy](http://liviutudor.com/2014/09/11/tracking-users-online-part-1/). Its intent is to showcase what is happening behind the courtains when dealing with tracking users online.
[My blog](http://liviutudor.com) will contain a new blog post for each release of this project. For each such release I will be going through the changes operated and explain why they are necessary and what impact they have on the overall solution and why they were needed.


Building
--------

This is a maven-ized project, so all you need is Apache Maven (and of course Java!). The project is set to use JDK 8 -- however, there is nothing currently in the code depending on Java 8, though I expect this to change pretty soon as more changes are added to the code, as such please stick to using JDK 8 unless absolutely necessary.

To package the project simply use

```
mvn clean package install
```

(which will also install it in your local repo).

The project uses [Checkstyle](http://maven.apache.org/plugins/maven-checkstyle-plugin/) and [FindBugs](http://mojo.codehaus.org/findbugs-maven-plugin/) to check for coding conventions and prevent common bugs making their ways in. If you only want to run the code analysis tools, these have been bound to the `validate` maven phase so simply use

```
mvn validate
```

and you will get an error report in the console.


Version History
---------------

* `1.0.0` -- this is the first official cut of the project and has just the basic code to deliver a 1x1 transparent (GIF) pixel to the browser. More details in my blog post [here](http://liviutudor.com/2014/09/22/tracking-users-online-part-2/).
* `1.0.1` -- this simply adds headers to prevent caching and introduces code coverage tools such as CheckStyle and FindBugs. More details in my blog post [here]()
