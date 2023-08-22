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



@WebServlet(urlPatterns = "/pages/item")
public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");
            PreparedStatement pstm1 = connection.prepareStatement("select * from item");
            ResultSet rst = pstm1.executeQuery();

            resp.addHeader("Content-Type","application/json");

            JsonArrayBuilder allItems = Json.createArrayBuilder();
            while (rst.next()){
                String code = rst.getString(1);
                String description= rst.getString(2);
                String qty = rst.getString(3);
                String unitPrice = rst.getString(4);

                JsonObjectBuilder itemObject =Json.createObjectBuilder();
                itemObject.add("code",code);
                itemObject.add("description",description);
                itemObject.add("qty",qty);
                itemObject.add("unitPrice",unitPrice);
                allItems.add(itemObject.build());
            }
            resp.setContentType("application/json");
            resp.getWriter().print(allItems.build());
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("code");
        String itemName = req.getParameter("description");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");
        String option = req.getParameter("option");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");
            switch (option) {
                case "add":
                    PreparedStatement pstm = connection.prepareStatement("insert into item values(?,?,?,?)");
                    pstm.setObject(1, code);
                    pstm.setObject(2, itemName);
                    pstm.setObject(3, qty);
                    pstm.setObject(4, unitPrice);
                    resp.addHeader("Content-Type","application/json");

                    if (pstm.executeUpdate() > 0) {

                        JsonObjectBuilder itemObject =Json.createObjectBuilder();

                        itemObject.add("state","ok");
                        itemObject.add("message","Successfully Added..!");
                        itemObject.add("data","");
                        resp.getWriter().print(itemObject.build());
                    }
                    break;
                case "delete":
                    PreparedStatement pstm2 = connection.prepareStatement("delete from item where code=?");
                    pstm2.setObject(1, code);
                    resp.addHeader("Content-Type","application/json");

                    if (pstm2.executeUpdate() > 0) {


                        JsonObjectBuilder deleteObject =Json.createObjectBuilder();

                        deleteObject.add("state","ok");
                        deleteObject.add("message","Item delete");
                        deleteObject.add("data","");
                        resp.getWriter().print(deleteObject.build());
                    }
                    break;
                case "update":
                    PreparedStatement pstm3 = connection.prepareStatement("update item set description=?,qtyOnHand=?,unitPrice=? where code=?");
                    pstm3.setObject(4, code);
                    pstm3.setObject(1, itemName);
                    pstm3.setObject(2, qty);
                    pstm3.setObject(3, unitPrice);

                    resp.addHeader("Content-Type","application/json");
                    if (pstm3.executeUpdate() > 0) {

                        JsonObjectBuilder updateObject =Json.createObjectBuilder();

                        updateObject.add("state","ok");
                        updateObject.add("message","Update Item..!");
                        updateObject.add("data","");
                        resp.getWriter().print(updateObject.build());
                    }
                    break;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder itemObject =Json.createObjectBuilder();

            itemObject.add("state","error");
            itemObject.add("message","Added Unsuccessful..!");
            itemObject.add("data","");
            resp.getWriter().print(itemObject.build());
            resp.setStatus(400);
            throw new RuntimeException(e);
        }
    }
}
