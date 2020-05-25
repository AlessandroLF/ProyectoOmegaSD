<%-- 
    Document   : index
    Created on : 24/05/2020, 12:48:38 PM
    Author     : sandro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form name="loginF" action="LoginServlet" method="POST">
            Usuario<input type="text" name="uname" value="" />
            Contraseña<input type="password" name="pwd" value="" />
            <input type="submit" value="LogIn" />
        </form>
        <form name="sinF" action="SignInServlet" method="POST">
            Usuario<input type="text" name="uname" value="" />
            Contraseña<input type="password" name="pwd" value="" />
            email<input type="text" name="email" value="" />
            <input type="submit" value="SignIn" />
        </form>
    </body>
</html>
