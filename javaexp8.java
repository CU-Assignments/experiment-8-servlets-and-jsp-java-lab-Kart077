EASY:

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Simulated user credentials for demonstration
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            out.println("<html><body>");
            out.println("<h2>Welcome, " + username + "!</h2>");
            out.println("</body></html>");
        } else {
            out.println("<html><body>");
            out.println("<h2>Login Failed: Invalid username or password</h2>");
            out.println("<a href=\"login.html\">Try again</a>");
            out.println("</body></html>");
        }
    }
}

MEDIUM: 

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String JDBC_USER = "your_username";
    private static final String JDBC_PASS = "your_password";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Employee Directory</h2>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                
                String query;
                PreparedStatement stmt;
                
                if (idParam != null && !idParam.trim().isEmpty()) {
                    query = "SELECT * FROM employees WHERE id = ?";
                    stmt = conn.prepareStatement(query);
                    stmt.setInt(1, Integer.parseInt(idParam));
                } else {
                    query = "SELECT * FROM employees";
                    stmt = conn.prepareStatement(query);
                }

                ResultSet rs = stmt.executeQuery();

                out.println("<table border='1'>");
                out.println("<tr><th>ID</th><th>Name</th><th>Department</th><th>Email</th></tr>");
                boolean found = false;

                while (rs.next()) {
                    found = true;
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("id") + "</td>");
                    out.println("<td>" + rs.getString("name") + "</td>");
                    out.println("<td>" + rs.getString("department") + "</td>");
                    out.println("<td>" + rs.getString("email") + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
                if (!found) {
                    out.println("<p>No employee found with the given ID.</p>");
                }

            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }

        out.println("<br><a href='employeeSearch.html'>Back to Search</a>");
        out.println("</body></html>");
    }
}

HARD:

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/SubmitAttendance")
public class SubmitAttendance extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String JDBC_USER = "your_username";
    private static final String JDBC_PASS = "your_password";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String studentName = request.getParameter("studentName");
        String rollNo = request.getParameter("rollNo");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                String sql = "INSERT INTO attendance (student_name, roll_no, date, status) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, studentName);
                stmt.setString(2, rollNo);
                stmt.setDate(3, Date.valueOf(date));
                stmt.setString(4, status);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    out.println("<h2>Attendance submitted successfully!</h2>");
                } else {
                    out.println("<h2>Failed to submit attendance.</h2>");
                }
            }

        } catch (Exception e) {
            out.println("<h3>Error:</h3>");
            e.printStackTrace(out);
        }

        out.println("<br><a href='attendance.jsp'>Back to Form</a>");
    }
}
