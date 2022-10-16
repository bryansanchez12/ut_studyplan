package nl.utwente.di.dao;

import nl.utwente.di.model.Course;
import nl.utwente.di.model.Form;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.google.gson.*;

/**
 * A DAO for StudyPlan, Courses and Form
 */

public enum FormDao {

    // -- Instance variables -----------------------------------------

    instance;
    public static Connection conn;
    // Mauricio' credentials for the database
    private static String user = "dab_di19202b_115";
    private static String password = "FxHz2vsoUuy1JM3/";
    // Maps containing specific forms
    private Map<String, Form> FormsProvider;
    private Map<String, Form> FormsQueue;
    private Map<String, Form> FormsApproved;

    // -- Constructors -----------------------------------------------

    /**
     * Connects with the database, creates all list of forms, and then assigns them with the hash maps
     */
    private FormDao(){
        FormsProvider = new HashMap<>();
        FormsQueue = new HashMap<>();
        FormsApproved = new HashMap<>();
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
                fetchAllForms();

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

    public Map<String, Form> getFormsArchive(){
        return FormsProvider;
    }

    public Map<String, Form> getFormsQueue(){
        setUp();
        return getFormsDependingOn("Revision", FormsProvider);
    }
    public Map<String, Form> getFormsApproved(){
        setUp();
        return getFormsDependingOn("Approved", FormsProvider);
    }
    public Map<String, Form> getFormsRejected(){
        setUp();
        return getFormsDependingOn("Rejected", FormsProvider);
    }
    public Map<String, Form> getStudentForms(String student_id){
        setUp();
        return getFormsDependingOnID(student_id, FormsProvider);
    }

    /**
     * Retrieves the forms depending on the state which can be Approved, Rejected, Revision
     * @param state the name of the form
     * @param map the hashmap containing all the forms
     * @return a hash map with desired forms
     */
    public Map<String, Form> getFormsDependingOn(String state, Map<String, Form> map){
        Map<String, Form> result = new HashMap<>();
        Set<String> keys = map.keySet();
        Iterator<String> keyIter = keys.iterator();
        while (keyIter.hasNext()) {
            String form_id = keyIter.next();
            Form form = map.get(form_id);
            if (form.getState().equalsIgnoreCase(state)){
                result.put(form_id, form);
            }
        }
        return result;
    }

    /**
     * Fetch all forms in the database
     * @return a hashmap containing all the forms with their id as the key
     * @throws SQLException
     */
    public void fetchAllForms() throws SQLException {
        String query = "SELECT * FROM studyplan.form ";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        Form temp = new Form();
        while (rs.next()) {
            temp = new Form(rs.getString("form_id"), rs.getString("s_number"),
                    rs.getString("submission_date"), Integer.parseInt(rs.getString("credits")),
                    Boolean.parseBoolean(rs.getString("credits")), rs.getString("first_time"),
                    getCourses(rs.getString("advanced_courses")),
                    getCourses(rs.getString("elective_courses")),
                    Boolean.parseBoolean(rs.getString("internship")),
                    rs.getString("other_courses"), rs.getString("courses_explanation"),
                    rs.getString("comment"),
                    rs.getString("state"), rs.getString("program"));
            FormsProvider.put(temp.getF_id(), temp);
        }
        rs.close();
        ps.close();
    }

    /**change the Status of a form
     * @throws SQLException
     */
    public void changeState(String form_id, String state, String comment) throws SQLException{
        try{
            Class.forName("org.postgresql.Driver");
            try{
                conn = DriverManager.getConnection("jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);

                String query = "UPDATE studyplan.form SET state = ?, comment = ? " +
                        "WHERE form_id = ? ";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, state);
                ps.setString(2, comment);
                ps.setString(3, form_id);
                ResultSet rs = ps.executeQuery();

                rs.close();
                ps.close();

                conn.close();
            }catch (SQLException e){
                System.err.println("Oops: " + e.getMessage() );
                System.err.println("SQLState: " + e.getSQLState() );
            }


        }catch(ClassNotFoundException e){
            System.err.println("JBC driver not loaded");
        }
    }

    /**
     * Converts a json object to a HashMap
     * @param jsonObject a String with courses
     * @return the HashMap with course_id as the key
     */
    public Map<String, Course> getCourses(String jsonObject){
        Map<String, Course> courses = new Gson().fromJson(jsonObject, HashMap.class);
        return courses;
    }

    /**
     * Converts a json object to a HashMap
     * @param courses the HashMap with course_id as the key
     * @return jsonObject a String with courses
     */
    public String getJsonObject(Map<String, Course> courses){
        String result = "";
        GsonBuilder gSonMapBuilder = new GsonBuilder();
        Gson gSonObject = gSonMapBuilder.create();
        result = gSonObject.toJson(courses);
        return result;
    }

    public void createNewForm(Form form){
        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://bronto.ewi.utwente.nl:5432/"+user,
                        user, password);
                fetchAllForms();

                String query = "INSERT INTO studyplan.form " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, form.getF_id());
                ps.setString(2, form.getS_number());
                Date date = Date.valueOf(form.getSubmission_date());
                ps.setDate(3, date);
                ps.setInt(4, form.getEC());
                ps.setBoolean(5, form.isFirstTime());
                ps.setString(6, form.getExplanation());
                ps.setString(7, FormDao.instance.getJsonObject(form.getMandatoryCourses()));
                ps.setString(8, FormDao.instance.getJsonObject(form.getAdvanceCourses()));
                ps.setString(9, FormDao.instance.getJsonObject(form.getElectiveCourses()));
                ps.setBoolean(10, form.hasInternship());
                ps.setString(11, form.getOtherCourses());
                ps.setString(12, form.getOtherCourses());
                ps.setString(13, form.getComment());
                ps.setString(14, form.getState());
                ResultSet rs = ps.executeQuery();

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
    }

    /**
     * Retrieves the forms depending on the student number
     * @param student_id the student number
     * @param map the hashmap containing all the forms that belongs to that student
     * @return a hash map with desired forms
     */
    public Map<String, Form> getFormsDependingOnID(String student_id,  Map<String, Form> map){
        Map<String, Form> result = new HashMap<>();
        Set<String> keys = map.keySet();
        Iterator<String> keyIter = keys.iterator();
        while (keyIter.hasNext()) {
            String form_id = keyIter.next();
            Form form = map.get(form_id);
            if (form.getS_number().equalsIgnoreCase(student_id)){
                result.put(form_id, form);
            }
        }
        return result;
    }


    public boolean isApprovedForm(String f_id) {
        if (FormsProvider.containsKey(f_id)) {
            Form tempForm;
            tempForm = FormsProvider.get(f_id);
            FormsApproved.put(tempForm.getF_id(), tempForm);
            return FormsProvider.remove(f_id, tempForm);
        } else {
            return false;
        }
    }


}


