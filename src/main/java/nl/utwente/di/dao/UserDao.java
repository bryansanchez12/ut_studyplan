package nl.utwente.di.dao;

import nl.utwente.di.model.Mentor;
import nl.utwente.di.model.Student;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A DAO for Students
 */
public enum UserDao {

    // -- Instance variables -----------------------------------------

    instance;
    public static Connection conn;
    // Mauricio' credentials for the database
    private static String user = "dab_di19202b_115";
    private static String password = "FxHz2vsoUuy1JM3/";

    private Map<String, Student> users_ContentProvider = new HashMap<String, Student>();

    // -- Constructors -----------------------------------------------

    /**
     * Connects with the database
     */
    private UserDao() {



    }

    // -- Queries ----------------------------------------------------

    /**
     * Sets up the connection with the Database
     */
    public void setUp(){
        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);


                conn.close();
            } catch(SQLException e) {
                System.err.println("Oops: " + e.getMessage() );
                System.err.println("SQLState: " + e.getSQLState() );
            }
        }
        catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not loaded");
        }
    }

    public Map<String, Student> getUsers_Model() {
        return users_ContentProvider;
    }

    /**
     * Compare if a password given by a user matched with the one stored in the database
     * @param email the student or employee email
     * @param unHashedPassword the given password by the user
     * @return a true if the password matched with the one provided, false otherwise
     */
    public boolean passwordMatches(String email, String unHashedPassword) {
        boolean result = false;
        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);
                String query = "SELECT u.password " +
                        "FROM studyplan.user u " +
                        "WHERE u.email LIKE ?;";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    String temp = rs.getString("password");
                    String hashedPassword = "$2a$12$" + temp;
                    result = BCrypt.checkpw(unHashedPassword, hashedPassword);
                }
                rs.close();
                ps.close();

                conn.close();
            } catch(SQLException e) {
                System.err.println("Oops: " + e.getMessage() );
                System.err.println("SQLState: " + e.getSQLState() );
            }
        }
        catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not loaded");
        }
        System.out.println("boolean: " + result);
        return result;
    }

    /**
     * Compare if the given email belongs to a student
     * @param email the student or employee email
     * @return a true if the email provided belongs to a student, false otherwise
     */
    public boolean isStudent(String email) {
        boolean result = false;
        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);
                String query = "SELECT u.user_type " +
                        "FROM studyplan.user u " +
                        "WHERE u.email LIKE ?;";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    String temp = rs.getString("user_type");
                    result  = temp.equalsIgnoreCase("student");
                }
                rs.close();
                ps.close();

                conn.close();
            } catch(SQLException e) {
                System.err.println("Oops: " + e.getMessage() );
                System.err.println("SQLState: " + e.getSQLState() );
            }
        }
        catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not loaded");
        }
        System.out.println("boolean: " + result);
        return result;
    }

    /**
     * Create a new user in the database
     * @param st_number the student number
     * @param firstName the first name of this student
     * @param surname the Surname of this student
     * @param email the student or employee email
     * @param hashedPassword the password that were hashed in order to be stored in the DB
     * @return a true if the student was created successfully, false otherwise
     */
    public boolean newUser(String st_number, String firstName, String surname, String email, String hashedPassword) {
        boolean result = false;
        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);
                String query = "INSERT INTO studyplan.user\n" +
                        "VALUES (?, ?, ?, ?, ?, 'student');";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, st_number);
                ps.setString(2, firstName);
                ps.setString(3, surname);
                ps.setString(4, email);
                ps.setString(5, hashedPassword);
                ResultSet rs = ps.executeQuery();
                rs.close();
                ps.close();
                conn.close();
            } catch(SQLException e) {

                if(e.getSQLState().equalsIgnoreCase("02000")){
                    result = true;
                } else {
                    System.err.println("Oops: " + e.getMessage() );
                    System.err.println("SQLState: " + e.getSQLState() );
                }
            }
        }
        catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not loaded");
        }
        return result;
    }

    /**
     * Retrieve a student depending the student number
     * @param id the student number
     * @return a Student object with all the attributes
     */
    public Student getStudentByID(String id) {
        Student student = new Student();
        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);
                String query = "SELECT * " +
                        "FROM studyplan.user u " +
                        "WHERE u.user_id LIKE ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    student = new Student(rs.getString(1), rs.getString(2),
                            rs.getString(3), rs.getString(4), rs.getString(5));
                }
                rs.close();
                ps.close();

                conn.close();
            } catch(SQLException e) {
                if(e.getSQLState().equalsIgnoreCase("02000")){
                    System.out.println("Action completed successfully");
                } else {
                    System.err.println("Oops: " + e.getMessage() );
                    System.err.println("SQLState: " + e.getSQLState() );
                }
            }
        }
        catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not loaded");
        }
        return student;
    }

    /**
     * Retrieve a student depending the student email
     * @param email the email address of a student
     * @return a Student object with all the attributes
     */
    public Student getStudentByEmail(String email) {
        Student student = new Student();
        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);

                String query = "SELECT * " +
                        "FROM studyplan.user u " +
                        "WHERE u.email LIKE ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    student = new Student(rs.getString(1), rs.getString(2),
                            rs.getString(3), rs.getString(4), rs.getString(5));
                }
                rs.close();
                ps.close();
                conn.close();
            } catch(SQLException e) {
                System.err.println("Oops: " + e.getMessage() );
                System.err.println("SQLState: " + e.getSQLState() );
            }
        }
        catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not loaded");
        }
        return student;
    }

    /**
     * Retrieve a student depending the student number
     * @param id the student number
     * @return a Student object with all the attributes
     */
    public Mentor getMentorByID(String id) {
        Mentor mentor = new Mentor();
        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);

                String query = "SELECT * " +
                        "FROM studyplan.user u " +
                        "WHERE u.user_id LIKE ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    mentor = new Mentor(rs.getString(1), rs.getString(2),
                            rs.getString(3), rs.getString(4), rs.getString(5));
                }
                rs.close();
                ps.close();

                conn.close();
            } catch(SQLException e) {
                System.err.println("Oops: " + e.getMessage() );
                System.err.println("SQLState: " + e.getSQLState() );
            }
        }
        catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not loaded");
        }
        return mentor;
    }
}
