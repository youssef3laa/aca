# Appwork

[![N|Solid](https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg)](https://nodesource.com/products/nsolid)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

Appwork is a project for handling correspondence to Administrative Control Authority (ACA). the aim of this project is to be build as a cloud based product. To acheive this we need to take into considaration the code quality not just in back end but also into front end. 
Stack used for Back End:
  - Spring boot
  - Spring JPA
  - lombok (automatically plugs getter and setters, constructors, and Automate your logging variables)
  - jackson (Java JSON library)
  - java-ews (outlook integration and sending emails)

Stack used for Front End:
  - vue cli
  - sass
  - vue i18n (internationalization plugin for Vue.js)
  - vue-axios (promise-based HTTP client)
  - vuetify (Material Design Framework)

The java project is consist of two independent modules (appwork-common module and web-app module )
  - appwork-common module (this module is reponsible for database communication and handling any shared features between other modules )
  - web-app module this module handle any http request 

Java tips and tricks
   - Minimize object creation (by understanding when object is created and re-use it)
   - Avoid using old school for loop use 
```sh  
    foreach list.forEach(s->System.out.print(s))
```
   - Always use Optional to minimize null pointer exceptions 
```sh   
    Optional.ofNullable(systemUtil.getExt(contentType)).orElse("png" ) 
```
   - Try to reduce using of if-else by using ifPresentOrElse in spring boot 
```sh      
    String[] result = {null};
    Optionals.ifPresentOrElse(optionalId, id -> String.join("-", id, result[0] =  generateId(type)),
        () -> result[0] =  generateId(type));
```
   - Alawys use enum when you know all possible values at compile time.
   - String builder is used when you have multiple write operation on a string
   - Use String.join(delimiter, string1, string2 ) or String.format("%s - %s", notificationId, date); to concat multiple string 
   - If you need to work with unknown objects try using Generics instead of Object
   - Always use final after try and catch 
   - Do not copy and paste code (DRY — Don’t repeat yourself)
   - Avoid hardcoding use .properties.
# Naming Convention!

| Type | Style | Example |
| :---: | :---: | :---: |
| Class | Upper Camel Case (PascalCase) | MyClass |
| Class | Try to make only one public method Everything else private  |  |    
| Class | 2–3 words for class names  |  | 
| Method | Lower Camel Case  | findMyPhone() | 
| Method |Avoid using many parameters| saveUser(String name, Address userAddress)
| Method | Use verbs for method name  |  | 
| Method | 1–2 words for method names  |  | 
| Variable | Lower Camel Case  |  |
| Constants | all uppercase  | FIRSTNAME |
| Constants | 3–4 words for global variable names  |  |
| Constants | Only constant can have “_” in their name  |  |
| File name | {important-parameter}-{date}-{version}-{Sequence}  |  1915-201031-v1-001.txt |
| boolean | For any boolean checking use is{Somthing}  | isSaved | 

   - To find something from database use `find` instead of get
   - To remove something from database use `remove` instead of delete 
   - To save something in database use `save` instead of create or insert or put
   - To write something into file use `write` instead of save or update 

Comments: 
use java Docs for comments by the way Is not good practice to write many comments;
```sh   
/**
* Returns an Image object that can then be painted on the screen. 
* The url argument must specify an absolute <a href="#{@link}">{@link URL}</a>. The name
* argument is a specifier that is relative to the url argument. 
* <p>
* This method always returns immediately, whether or not the 
* image exists. When this applet attempts to draw the image on
* the screen, the data will be loaded. The graphics primitives 
* that draw the image will incrementally paint on the screen. 
*
* @param  url  an absolute URL giving the base location of the image
* @param  name the location of the image, relative to the url argument
* @return      the image at the specified URL
* @see         Image
*/
public Image getImage(URL url, String name) {
    try {
        return getImage(new URL(url, name));
    } catch (MalformedURLException e) {
        return null;
    }
}

```
You can also:
  - Import and save files from GitHub, Dropbox, Google Drive and One Drive
  - Drag and drop markdown and HTML files into Dillinger
  - Export documents as Markdown, HTML and PDF

Markdown is a lightweight markup language based on the formatting conventions that people naturally use in email.  As [John Gruber] writes on the [Markdown site][df1]

> The overriding design goal for Markdown's
> formatting syntax is to make it as readable
> as possible. The idea is that a
> Markdown-formatted document should be
> publishable as-is, as plain text, without
> looking like it's been marked up with tags
> or formatting instructions.

This text you see here is *actually* written in Markdown! To get a feel for Markdown's syntax, type some text into the left window and watch the results in the right.

### Tech

Dillinger uses a number of open source projects to work properly:

* [AngularJS] - HTML enhanced for web apps!
* [Ace Editor] - awesome web-based text editor
* [markdown-it] - Markdown parser done right. Fast and easy to extend.
* [Twitter Bootstrap] - great UI boilerplate for modern web apps
* [node.js] - evented I/O for the backend
* [Express] - fast node.js network app framework [@tjholowaychuk]
* [Gulp] - the streaming build system
* [Breakdance](https://breakdance.github.io/breakdance/) - HTML to Markdown converter
* [jQuery] - duh

And of course Dillinger itself is open source with a [public repository][dill]
 on GitHub.

### Installation

Dillinger requires [Node.js](https://nodejs.org/) v4+ to run.

Install the dependencies and devDependencies and start the server.

```sh
$ cd dillinger
$ npm install -d
$ node app
```

For production environments...

```sh
$ npm install --production
$ NODE_ENV=production node app
```

### Plugins

Dillinger is currently extended with the following plugins. Instructions on how to use them in your own application are linked below.

| Plugin | README |
| ------ | ------ |
| Dropbox | [plugins/dropbox/README.md][PlDb] |
| GitHub | [plugins/github/README.md][PlGh] |
| Google Drive | [plugins/googledrive/README.md][PlGd] |
| OneDrive | [plugins/onedrive/README.md][PlOd] |
| Medium | [plugins/medium/README.md][PlMe] |
| Google Analytics | [plugins/googleanalytics/README.md][PlGa] |


### Development

Want to contribute? Great!

Dillinger uses Gulp + Webpack for fast developing.
Make a change in your file and instantaneously see your updates!

Open your favorite Terminal and run these commands.

First Tab:
```sh
$ node app
```

Second Tab:
```sh
$ gulp watch
```

(optional) Third:
```sh
$ karma test
```
#### Building for source
For production release:
```sh
$ gulp build --prod
```
Generating pre-built zip archives for distribution:
```sh
$ gulp build dist --prod
```
### Docker
Dillinger is very easy to install and deploy in a Docker container.

By default, the Docker will expose port 8080, so change this within the Dockerfile if necessary. When ready, simply use the Dockerfile to build the image.

```sh
cd dillinger
docker build -t joemccann/dillinger:${package.json.version} .
```
This will create the dillinger image and pull in the necessary dependencies. Be sure to swap out `${package.json.version}` with the actual version of Dillinger.

Once done, run the Docker image and map the port to whatever you wish on your host. In this example, we simply map port 8000 of the host to port 8080 of the Docker (or whatever port was exposed in the Dockerfile):

```sh
docker run -d -p 8000:8080 --restart="always" <youruser>/dillinger:${package.json.version}
```

Verify the deployment by navigating to your server address in your preferred browser.

```sh
127.0.0.1:8000
```

#### Kubernetes + Google Cloud

See [KUBERNETES.md](https://github.com/joemccann/dillinger/blob/master/KUBERNETES.md)


### Todos

 - Write MORE Tests
 - Add Night Mode

License
----

MIT


**Free Software, Hell Yeah!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>

