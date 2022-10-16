package nl.utwente.di.dao;

import nl.utwente.di.model.Course;

import java.sql.*;
import java.util.*;

/**
 * Class DAO for connecting to the database schema with the StudyPlan application.
 */
public enum CourseDao {

    // -- Instance variables -----------------------------------------

    instance;
    public static Connection conn;
    // Mauricio's credentials for the database
    private static String user = "dab_di19202b_115";
    private static String password = "FxHz2vsoUuy1JM3/";
    // HashMaps depending on the course type
    private Map<String, Course> mandatoryCourses ;
    private Map<String, Course> ethicsCourses;
    private Map<String, Course> graduationCourses;
    private Map<String, Course> advancedCourses;
    private Map<String, Course> electiveCourses;

    // -- Constructors -----------------------------------------------

    /**
     * Connects with the database, creates the courses as root elements, and then assigns them with the hash maps
     */
    private CourseDao(){
        // HashMaps depending on the course type
        mandatoryCourses = new HashMap<>();
        ethicsCourses = new HashMap<>();
        graduationCourses = new HashMap<>();
        advancedCourses = new HashMap<>();
        electiveCourses = new HashMap<>();
        //Sets up the connection with the DB
        setUp();
    }

    // -- Commands ---------------------------------------------------

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
                // TODO: change to the appropriate column names that appear on the schema
                mandatoryCourses = fetchAllCourses("%core%");
                ethicsCourses = fetchAllCourses("%Ethics%");
                graduationCourses = fetchAllCourses("%graduation%");
                advancedCourses = fetchAllCourses("%Advanced%");
                electiveCourses = fetchAllCourses("%suggested electives%");

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

    // -- Queries ----------------------------------------------------

    /**
     * Retrieves the courses that could be addressed with their unique code
     * @param program the name
     * @return a hashmap with the courses depending on the type
     */
    public Map<String, Course> getMandatoryCourses(String program){
        return getCoursesDependingOn(program, mandatoryCourses);
    }
    public Map<String, Course> getEthicsCourses(String program){
        return getCoursesDependingOn(program,ethicsCourses);
    }
    public Map<String, Course> getGraduationCourses(String program){
        return getCoursesDependingOn(program,graduationCourses);
    }
    public Map<String, Course> getAdvancedCourses(String program){
        return getCoursesDependingOn(program,advancedCourses);
    }
    public Map<String, Course> getElectiveCourses(String program){
        return getCoursesDependingOn(program,electiveCourses);
    }

    /**
     * Retrieves the courses depending on the program where it belongs
     * @param program the name of the program
     * @param map the hashmap containing all the courses
     * @return a hash map with desired courses
     */
    public Map<String, Course> getCoursesDependingOn(String program, Map<String, Course> map){
        Map<String, Course> result = new HashMap<>();
        Set <String> keys = map.keySet();
        Iterator<String> keyIter = keys.iterator();
        while (keyIter.hasNext()) {
            String course_id = keyIter.next();
            Course course = map.get(course_id);

            if (course.getProgram().equalsIgnoreCase(program)){
                result.put(course_id, course);
            }
        }
        return result;
    }

    /**
     * Fetch all courses in the database that match with an specific course type
     * @param course_type that could be Core c, Ethics e, Graduation g, Advanced a and Electives se
     * @return a hashmap containing all the courses with their code as the key
     * @throws SQLException
     */
    public Map<String, Course> fetchAllCourses(String course_type) throws SQLException {
        Map<String, Course> courses = new HashMap<>();

        String query = "SELECT c.course_code, c.course_name, c.credits, c.block, c.program " +
                "FROM studyplan.courses c, studyplan.course_type ct " +
                "WHERE c.course_type = ct.course_id "+
                "AND ct.course_type LIKE ?;";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, course_type);
        ResultSet rs = ps.executeQuery();

        Course temp = new Course();
        while (rs.next()) {
            temp = new Course(rs.getString("course_code"), rs.getString("course_name"),
                    Integer.parseInt(rs.getString("credits")), rs.getString("block")
                            , rs.getString("program"));
            courses.put(temp.getCourse_code(), temp);
        }
        rs.close();
        ps.close();
        return courses;
    }



}
