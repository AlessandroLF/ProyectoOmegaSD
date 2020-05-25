/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author sandro
 */
public class ColasServlet extends HttpServlet {
    
    private ArrayList<String> followers;
    private ArrayList<String> following;
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject;
    private String uname;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        Cookie ck[] = request.getCookies();
        for(Cookie c : ck ){
            if(c.getName().equals("Chirupero")){
                uname = c.getValue();
                break;
            }
        }
        if (uname == null)
            response.sendRedirect("index.jsp?nolog=1");
        if(request.getParameter("function") == null)
            response.sendRedirect("main.jsp?nofunc=1");
        
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection;
        MessageProducer messageProducer;
        TextMessage message;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false /*Transacter*/, Session.AUTO_ACKNOWLEDGE);
            switch(request.getParameter("function")){
                case "send":{
                    BdConnection bd = new BdConnection();
                    followers = bd.getFollowers(uname);
                    bd.closeCon();
                    for(String follower: followers){
                        subject = uname + follower;
                        Destination destination = session.createQueue(subject);
                        messageProducer = session.createProducer(destination);
                        message = session.createTextMessage();
                        message.setText(request.getParameter("message"));
                        messageProducer.send(message);
                        messageProducer.close();
                        session.close();
                        connection.close();
                        out.print("Sent to " + follower);
                    }
                    break;
                }
                case "get":{
                    subject = request.getParameter("target") + uname;
                    Destination destination = session.createQueue(subject);
                    MessageConsumer messageConsumer = session.createConsumer(destination);
                    TextMessage chirup = (TextMessage) messageConsumer.receive();
                    if (chirup != null) {
                        out.print(chirup.getText());
                    }
                    messageConsumer.close();
                    session.close();
                    connection.close();
                    break;
                }
                default:response.sendRedirect("main.jsp?nofunc=1");
            }
        } catch (JMSException ex) {
            response.sendRedirect("main.jsp?nojms=1");
        }
    }
    
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
