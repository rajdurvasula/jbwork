package net.mysql.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Resource(name="jdbc/CredDS")
  private DataSource ds;

  public QueryServlet() {
    super();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Connection conn = null;
    PreparedStatement pstmt = null;
    String sql = "select * from credential";
    try {
      response.setContentType("text/html");
      StringBuffer resp = new StringBuffer();
      resp.append("<html>")
      .append("<head><title>MySQL JDBC Test </title></head>");
      conn = ds.getConnection();
      pstmt = conn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery(); 
      resp.append("<body>")
      .append("<table border=\"1\">")
      .append("<thead>")
      .append("<tr><td>Id</td><td>User name</td><td>Password</td><td>Role</td></tr>")
      .append("</thead>")
      .append("<tbody>"); 
      while (rs.next()) {
        resp.append("<tr>")
        .append("<td>").append(rs.getInt("id")).append("</td>")
        .append("<td>").append(rs.getString("uname")).append("</td>")
        .append("<td>").append(rs.getString("password")).append("</td>")
        .append("<td>").append(rs.getString("urole")).append("</td>")
        .append("</tr>");
      }
      resp.append("</tbody>")
      .append("</table>");
      resp.append("<p>");
      resp.append("<a href=\"/wildfly-TestMySQL/index.html\">Try again ..</a>");
      resp.append("</body>").append("</html>");
      response.getWriter().write(resp.toString());
    } catch (Exception e) {
        System.err.println("Error in JDBC operation: "+e.getMessage());
        e.printStackTrace();
    } finally {
      try {
        if (null != pstmt) {
          pstmt.close();
          pstmt = null;
        }
      } catch (Exception e1) {
        System.err.println("Error closing statement: "+e1.getMessage());
        e1.printStackTrace();
      }
      try {
        if (null != conn) {
          conn.close();
          conn = null;
        }
      } catch (Exception e1) {
        System.err.println("Error closing connection: "+e1.getMessage());
        e1.printStackTrace();
      }
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}
