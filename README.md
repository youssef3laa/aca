# Appwork

[![N|Solid](https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg)](https://nodesource.com/products/nsolid)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

Appwork is a project for handling correspondence to Administrative Control Authority (ACA). the aim of this project is to be build as a cloud based product. To acheive this we need to take into considaration the code quality not just in back end but also into front end. 
# Backend Stack:
  - Spring boot
  - Spring JPA
  - lombok (automatically plugs getter and setters, constructors, and Automate your logging variables)
  - jackson (Java JSON library)
  - java-ews (outlook integration and sending emails)

# Java Modules
- appwork-common module (for database interaction and shared features)
- appwork-dev (communcication with appworks and content server)
- web-app module (for handling http request and the security layer will be implemented on the top of this module )

Each module may have their own exception and handle it on their own ways

# ResponseBuilder class
it class for handling response for client side usage example: 
```sh  
AppResponse.ResponseBuilder<Account> respBuilder = AppResponse.builder();
try {
    CordysManagement.User user= cordysManagement.create(account.getUsername(), account.getPassword(), account.getOrganization());
    account.setSAMLart(user.getSAMLart());
    respBuilder.data(account);
} catch (JsonProcessingException e) {
    respBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
} catch (AppworkException e) {
    respBuilder.status(e.getCode());
}
return respBuilder.build().getResponseEntity();
    
```
# ResponseCode enum code class 
hold all the error codes and translation keys that will be sent to the frontend. so frontend will display error messages based on the key retreived from backend  

# AppworkException class 
we extend the java exception to send error code in the exception found example: 
```sh
throw new AppworkException("INVALID_CREDENTIALS", ResponseCode.INVALID_AUTH);
```

# Backend Naming Convention!
| Type | Style | Example |
| :---: | :---: | :---: |
| Class | Upper Camel Case (PascalCase) | MyClass |
| Class | Class name should be noun  |  |    
| Class | Try to make only one public method Everything else private  |  |  
| Method | Lower Camel Case  | findMyPhone() | 
| Method | Method name should be verb  | | 
| Method |Avoid using many parameters| saveUser(User user)
| Variable | Lower Camel Case  |  |
| Variable | One-character names should be avoided except for temporary variables | x,y,z |
| Packages | all lowercase | service |
| Packages | should be one of the top-level domain names |  com, edu, gov, mil, net, org, ex: com.sun.eng |
| Constants | all uppercase  | POSITIVE_INFINITY, FIRSTNAME |
| Constants | Only constant can have “_” in their name  |  |
| exceptions | should be a noun and should describe exactly what happened.  | ArgumentCannotBeNull, ProductIsOutOfStock |
| exceptions | Avoid to implement exception class for every unwanted behaviour .  | |
| File name | {important-parameter}-{date}-{version}-{Sequence}  |  1915-201031-v1-001.txt |
| boolean | For any boolean checking use is{Somthing}  | isSaved | 

   - To find something from database use `find` instead of get
   - To remove something from database use `remove` instead of delete 
   - To save something in database use `save` instead of create or insert or put
   - To write something into file use `write` instead of save or update 


# Java tips and tricks
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
 
Functional Interface 
interface with a SAM(Single Abstract Method).  used to make the implementation of this function on runtime using lambda expression example of this techniques are 
   - Consumer
   - Runnable
   - Supplier
# Backend Comments: 
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
# Frontend Stack:
  - vue cli
  - sass
  - vue i18n (internationalization plugin for Vue.js)
  - vue-axios (promise-based HTTP client)
  - vuetify (Material Design Framework)
  - vee-validate (for handling front end validation)
# Frontend Naming Convention!
   - Component should always be multi-word, “UserCard”
   - Filenames kebab-case “user-card.vue”
   - Components that are only used once per page should begin with the prefix “The”  “TheNavbar.vue” or “TheFooter.vue”.
   - Child components should include their parent name example if you would like a “Photo” component used in the “UserCard” you will name it “UserCardPhoto”.
# Frontend Folder structure
In this project we follow module based structure; each module contains (components, lib, services, routers, views and locales)
   - Components: are the components used in these module
   - lib: library used for this module
   - routers: route used in the module
   - views: is the entry point for the page 
   - locales: hold the translation used in the module
   - and finally the core module is the main module that hold common features used on the application
  

