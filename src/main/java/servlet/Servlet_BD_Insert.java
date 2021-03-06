package servlet;

import jpa.business.*;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Locale;


/**
 * Servlet implementation class DB
 */

@WebServlet(name="Servlet_BD_Insert",
        urlPatterns={"/Servlet_BD_Insert"})
public class Servlet_BD_Insert extends HttpServlet {
    private static final long serialVersionUID = 1L;



    //connexion à la base de données
    Connection connection = null;
    // Execute SQL query
    Statement statement = null;
    // results
    ResultSet result = null;


    final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC";
    final String USER = "root";
    final String PASS = "";
    private Date sqlDate= null ;
    final String formatDate = "yyyy-MM-dd";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doGet(req, resp);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<HTML>\n<BODY>\n" +
                "<H1>Recapitulatif des informations</H1>\n" +
                "<UL>\n" +
                " <LI>LastName: "
                + request.getParameter("lastname") + "\n" +
                " <LI>FirstName: "
                + request.getParameter("firstname") + "\n" +
                " <LI>Email: "
                + request.getParameter("email") + "\n" +
                "</UL>\n" +
                "</BODY></HTML>");
        System.out.println(request.getParameter("deadline"));
        String title = "Database Results";

        out.println("<html>\n" +
                "<head><title>" + title + " </title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + " </h1>\n"
        + "<h3>Voir l'insertion: http://localhost:8080/Servlet_BD_Display</h3>");

        KanbanBoard kanban = new KanbanBoard();
        kanban.setName(request.getParameter("kanbanBoardName"));
        //System.out.println("Le kanban est : "+kanban);
        addKanbanBoard(kanban);


        addSection(new Section(request.getParameter("sectionLabel")));


         /* final String formatDate = "yyyy-MM-dd";
        String date = request.getParameter("deadline");

        SimpleDateFormat format = new SimpleDateFormat(formatDate);

        try {
            java.util.Date dat = format.parse(date);
            java.sql.Date sqlDate = new java.sql.Date(dat.getTime());
            System.out.println(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        dateParser(request.getParameter("deadline"));
        System.out.println("La date est :" + sqlDate);



        addCard( new Card(request.getParameter("cardLabel"),dateParser(request.getParameter("deadline")),
                dateParser(request.getParameter("timeToDo")),"url","note","location"));

        addCardUser( new CardUser(dateParser(request.getParameter("attributionDate")),
                dateParser(request.getParameter("withdrawalDate")),dateParser(request.getParameter("beginDate")),
                dateParser(request.getParameter("endDate"))));


        addUser(new User(request.getParameter("name"),request.getParameter("email")));

        addTag(new Tag(request.getParameter("level"),request.getParameter("AvailabilityLevel"),
                request.getParameter("Taglabel"),request.getIntHeader("Boards")));

    }


    public void addUser(User user) {

        try {
            loadDataBase();
            String sql2 = "INSERT INTO user (name,enabled, email) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql2);


            //preparedStatement.setLong(1, user.getIdUser());
            preparedStatement.setString(1, user.getName());
            preparedStatement.setBoolean(2, true);
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addKanbanBoard(KanbanBoard kanbanBoard) {

        try {
            loadDataBase();
            String sql2 = "INSERT INTO kanbanBoard (name) VALUES (?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql2);

            //preparedStatement.setLong(1, kanbanBoard.getId());
            preparedStatement.setString(1, kanbanBoard.getName());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSection(Section section) {

        try {
            loadDataBase();
            String sql2 = "INSERT INTO section (label) VALUES ( ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql2);

            //preparedStatement.setLong(1, section.getId());
            preparedStatement.setString(1, section.getLabel());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCard(Card card) {
        try {
            loadDataBase();
            String sql2 = "INSERT INTO card (label, deadline, timeToDo,url,note,location,enabled) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql2);

            preparedStatement.setString(1, card.getLabel());
            preparedStatement.setDate(2,  new java.sql.Date(card.getDeadline().getTime()));
            preparedStatement.setDate(3,  new java.sql.Date(card.getTimeToDo().getTime()));
            preparedStatement.setString(4, card.getUrl());
            preparedStatement.setString(5, card.getNote());
            preparedStatement.setString(6, card.getLocation());
            preparedStatement.setBoolean(7, true);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCardUser(CardUser cardUser) {
        try {
            loadDataBase();
            String sql2 = "INSERT INTO cardUser (attributionDate, withdrawalDate, beginDate,endDate) VALUES (?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql2);

            preparedStatement.setDate(1, new java.sql.Date(cardUser.getAttributionDate().getTime()));
            preparedStatement.setDate(2,  new java.sql.Date(cardUser.getWithdrawalDate().getTime()));
            preparedStatement.setDate(3,  new java.sql.Date(cardUser.getBeginDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(cardUser.getEndDate().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTag(Tag tag) {
        try {
            loadDataBase();
            String sql2 = "INSERT INTO tag (level, AvailabilityLevel, label,Boards) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql2);

            preparedStatement.setString(1, tag.getLevel());
            preparedStatement.setString(2, tag.getAvailabilityLevel());
            preparedStatement.setString(3, tag.getLabel());
            preparedStatement.setInt(4, tag.getBoards());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void loadDataBase() {
        //chargement du driver
        try {
            Class.forName(JDBC_DRIVER);

        } catch (ClassNotFoundException e) {
        }
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public Date dateParser(String sql){

        //String date = request.getParameter("s");

        SimpleDateFormat format = new SimpleDateFormat(formatDate);

        try {
            java.util.Date dat = format.parse(sql);
            sqlDate = new java.sql.Date(dat.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sqlDate;
    }
}


