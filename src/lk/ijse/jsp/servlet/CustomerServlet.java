package lk.ijse.jsp.servlet;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;



@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from Customer");
            ResultSet rst = pstm.executeQuery();
            resp.addHeader("Content-Type","application/json");

            JsonArrayBuilder allCustomers = Json.createArrayBuilder();
            while (rst.next()){
                String id = rst.getString(1);
                String name= rst.getString(2);
                String address = rst.getString(3);
                String salary = rst.getString(4);

                JsonObjectBuilder customerObject =Json.createObjectBuilder();
                customerObject.add("id",id);
                customerObject.add("name",name);
                customerObject.add("address",address);
                customerObject.add("salary",salary);

               allCustomers.add(customerObject.build());
            }

            resp.setContentType("application/json");
            resp.getWriter().print(allCustomers.build());

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
        String cusSalary = req.getParameter("cusSalary");
        String option = req.getParameter("option");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");
            switch (option) {
                case "add":
                    PreparedStatement pstm = connection.prepareStatement("insert into Customer values(?,?,?,?)");
                    pstm.setObject(1, cusID);
                    pstm.setObject(2, cusName);
                    pstm.setObject(3, cusAddress);
                    pstm.setObject(4, cusSalary);

                    resp.addHeader("Content-Type","application/json");
                    if (pstm.executeUpdate() > 0) {
                       // resp.getWriter().println("Customer Added..!");

                        JsonObjectBuilder customerObject =Json.createObjectBuilder();

                        customerObject.add("state","ok");
                        customerObject.add("message","Successfully Added..!");
                        customerObject.add("data","");
                       resp.getWriter().print(customerObject.build());
                    }

                    break;
                case "delete":
                    PreparedStatement pstm2 = connection.prepareStatement("delete from Customer where id=?");
                    pstm2.setObject(1, cusID);
                    resp.addHeader("Content-Type","application/json");
                    if (pstm2.executeUpdate() > 0) {

                        JsonObjectBuilder deleteObject =Json.createObjectBuilder();

                        deleteObject.add("state","ok");
                        deleteObject.add("message","Delete Customer");
                        deleteObject.add("data","");
                        resp.getWriter().print(deleteObject.build());
                    }
                    break;
                case "update":
                    PreparedStatement pstm3 = connection.prepareStatement("update Customer set name=?,address=?,salary=? where id=?");
                    pstm3.setObject(4, cusID);
                    pstm3.setObject(1, cusName);
                    pstm3.setObject(2, cusAddress);
                    pstm3.setObject(3, cusSalary);


                    resp.addHeader("Content-Type","application/json");
                    if (pstm3.executeUpdate() > 0) {

                        JsonObjectBuilder updateObject =Json.createObjectBuilder();

                        updateObject.add("state","ok");
                        updateObject.add("message","Update Customer..!");
                        updateObject.add("data","");
                        resp.getWriter().print(updateObject.build());
                    }
                    break;
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {

            JsonObjectBuilder customerObject =Json.createObjectBuilder();

            customerObject.add("state","error");
            customerObject.add("message","Added Unsuccessfully..!");
            customerObject.add("data","");
            resp.getWriter().print(customerObject.build());
            resp.setStatus(400);
            throw new RuntimeException(e);
        }
    }
}
