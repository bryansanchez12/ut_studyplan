package nl.utwente.di.dao;

import nl.utwente.di.model.Course;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;

public class test {
    public static Connection conn;
    // Mauricio' credentials
    private static String user = "dab_di19202b_115";
    private static String password = "FxHz2vsoUuy1JM3/";

    public static void main (String[] args) {
        try {
            Class.forName("org.postgresql.Driver");

            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);
                System.out.println("Courses:");
                System.out.println();
                assignment5();
                List<Course> mandatoryCourses = new ArrayList<Course>();
                mandatoryCourses.addAll((CourseDao.instance.getMandatoryCourses("DST").values()));
                for (Course c : mandatoryCourses){
                    System.out.println("Course ID: " + c.getCourse_code());
                }
                conn.close();
            } catch(SQLException e) {
                System.err.println("Oops: " + e.getMessage() );
                System.err.println("SQLState: " + e.getSQLState() );
            }
        }
        catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not loaded");
        }

        String hashed1 = BCrypt.hashpw("123456789m", BCrypt.gensalt());
        String hashed2 = BCrypt.hashpw(password, BCrypt.gensalt());
        String hashed3 = BCrypt.hashpw("@BryanSanchez12", BCrypt.gensalt(12));
        String hashed4 = BCrypt.hashpw("123456789j", BCrypt.gensalt(12));
        String words[] = hashed4.split("\\$");
        System.out.println("Hashed (1): " + hashed1);
        System.out.println("Hashed (2): " + hashed2);
        System.out.println("Hashed (3): " + hashed3);
        System.out.println("Hashed (4): " + hashed4);
        System.out.println("split1: " + words[3]);


        if (BCrypt.checkpw(password, hashed1))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
    }

    public static void assignment5() {
        try {
            Statement st = conn.createStatement();
            String course_type = "core";
            ResultSet rs = st.executeQuery(     // TODO: change to the appropriate column names that appear on the schema
                    "SELECT c.course_code, c.course_name, c.credits, c.block, c.program "+
                            "FROM studyplan.courses c, studyplan.course_type ct " +
                            "WHERE c.course_type = ct.course_id "+
                            "AND ct.course_type LIKE '%" + course_type + "%' ");
            int i=1;

            while (rs.next()) {
                System.out.println(i+" : [ "+ rs.getString(1) + " ] "+ rs.getString(2)
                        + rs.getString(3) + rs.getString(4) + rs.getString(5));
                i++;
            }
            rs.close();
            st.close();
        } catch(SQLException e) {
            System.err.println("Oops: " + e.getMessage() );
            System.err.println("SQLState: " + e.getSQLState() );
        }
    }


    /*public static Map<String, Course> allCoreCourses() throws SQLException {
        Map<String, Course> courses = new HashMap<>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(     // TODO change to the appropriate column names
                "SELECT c.course_code, c.course_name, c.ec, ce.year, ce.block "+
                        "FROM studyplan.courses c, studyplan.course_execution ce " +
                        "WHERE c.course_type LIKE 'core' ");
        Course temp = new Course();
        while (rs.next()) {
            temp = new Course(rs.getString(1), rs.getString(2),
                    Integer.parseInt(rs.getString(3)), rs.getString(4), rs.getString(5));
            courses.put(temp.getCourse_code(), temp);
        }
        rs.close();
        st.close();
        return courses;
    }*/


    public static boolean hasDirector(int mid) throws SQLException {
        boolean result;
        PreparedStatement st = conn.prepareStatement("SELECT pid FROM directs WHERE mid=?;");
        st.setInt(1, mid);
        ResultSet rs = st.executeQuery();
        result = rs.next();
        rs.close();
        st.close();
        return result;
    }


}


