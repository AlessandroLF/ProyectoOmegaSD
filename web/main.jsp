<%-- 
    Document   : main
    Created on : 25/05/2020, 12:56:21 AM
    Author     : sandro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ChirrupER</title>
    </head>
    <body>
        <%
            Cookie[] cks = request.getCookies();
            boolean logedIn = false;
            for(Cookie c : cks){
                if(c.getName().equals("Chirupero")){
                    logedIn = true;
                    break;
                }
            }
            if(!logedIn)
                response.sendRedirect("index.jsp?bad=1");
        %>
        <form name="msgf" action="ColasServlet" method="POST"><br>
            Chirrup:<br>
            <input type="hidden" name="function" value="send" /><br>
            <textarea name="message" rows="11" cols="40">
            </textarea><br>
            <input type="submit" value="Send" name="sbut" />
        </form>
        <form name="getf" action="ColasServlet" method="POST">
            <input type="hidden" name="function" value="get" /><br>
            <input type="text" name="target" value="" /><br>
            <input type="submit" value="Fetch" name="fbut" />
        </form>
    </body>
</html>
