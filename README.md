# Vaadin 8 + Spring Boot + Auth0

This is just simple test project I made to test how to use these 3 together. API is little
complex as Auth0 does not provide nice clean API for single page server side applications.

If you want to play around with this project, you need to:

1. Register yourself as developer to Auth0
2. Create new Client under your account
3. Copy your **domain**, **client ID** and **client secret** from Auth0
4. You also need to get **management token** key **if** you want to manage your user accounts
5. Create file ```auth0.properties``` to ```src/main/resources```, you can use ```auth0-default.properties``` as your template
6. Compile project with ```mvn clean install```
7. Start the Spring Boot app with ```java -jar target/Auth0Demo.jar```
8. Go to ```http://localhost:8081``` with your browser

There is no online demo of this application, as Auth0 API is free only for 20 days.

Notice that you need to add your URLs to login and logout URL allowed lists on auth0.com.

More info:

1. Auth0 Java APIs really want to have access to HttpServletRequest, and with Vaadin this is challenging when eg. Push (WebSocket is used). This is the reason why separate LoginUI has been made (without @Push).
