package nl.utwente.di.model;

import nl.utwente.di.dao.CourseDao;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Class model for keeping a form containing a StudyPlan
 * submitted by a student in the application.
 */
@XmlRootElement
public class Form {

    // -- Instance variables -----------------------------------------


    private String f_id;
    private String s_number;
    private String submission_date;
    private int EC;
    private boolean firstTime;
    private String explanation;
    private Map<String, Course> mandatoryCourses
            = new HashMap<String, Course>();
    private Map<String, Course> advanceCourses
            = new HashMap<String, Course>();
    private Map<String, Course> electiveCourses
            = new HashMap<String, Course>();
    private boolean internship;
    private String otherCourses;
    private String otherCoursesExplanation;
    private String comment;
    private String state;
    private String program;


    // -- Constructors -----------------------------------------------


    /**
     * Creates a form that a students has filled in
     * @requires first_name and surname to be non-null
     * @param f_id the id of this form
     * @param submission_date of the user
     */
    public Form (String f_id, String s_number, String submission_date,
                 int EC, boolean firstTime, String explanation,
                 Map<String, Course> advanceCourses,
                 Map<String, Course> electiveCourses,
                 boolean internship, String otherCourses,
                 String otherCoursesExplanation, String comment,
                 String state, String program){
        this.f_id = f_id;
        this.s_number = s_number;
        this.submission_date = submission_date;
        this.firstTime = firstTime;
        this.explanation = explanation;
        this.EC = EC;
        // put the selected courses
        this.advanceCourses = advanceCourses;
        this.electiveCourses= electiveCourses;
        this.internship = internship;
        this.otherCourses = otherCourses;
        this.otherCoursesExplanation = otherCoursesExplanation;
        this.comment = comment;
        this.state = state;
        this.program = program;
        this.mandatoryCourses = CourseDao.instance.getMandatoryCourses(program);
    }

    public Form (String f_id, String s_number, String submission_date,
                 int EC, boolean firstTime,
                 Map<String, Course> advanceCourses,
                 Map<String, Course> electiveCourses,
                 boolean internship, String program){
        this.f_id = f_id;
        this.s_number = s_number;
        this.submission_date = submission_date;
        this.firstTime = firstTime;
        this.EC = EC;
        this.state = "Revision";
        // put the selected courses
        this.advanceCourses = advanceCourses;
        this.electiveCourses= electiveCourses;
        this.internship = internship;
        this.program = program;
        this.mandatoryCourses = CourseDao.instance.getMandatoryCourses(program);
    }

    public Form(){}


    // -- Queries ----------------------------------------------------


    /**
     * Returns the id of this form
     * @return the f_id
     */
    public String getF_id() {
        return f_id;
    }

    /**
     * Returns the student number of the person who has filled in it
     * @return the submission date
     */
    public String getS_number() {
        return s_number;
    }

    /**
     * Returns the date when this form has been filled in
     * @return the submission date
     */
    public String getSubmission_date() {
        return submission_date;
    }

    /**
     * Return the total number of EC
     * @return the total of EC
     */
    public int getEC(){return EC;}

    /**
     * Return true if the forms was made by the first time
     * @return true or false,
     */

    public boolean isFirstTime() { return firstTime; }

    /**
     * Return true if the student wants a internship
     * @return true or false depending oh his election
     */
    public boolean hasInternship() { return internship; }

    /**
     * Return the mandatory courses of this form
     * @return a HashMap with the courses
     */
    public Map<String, Course> getMandatoryCourses() {
        return mandatoryCourses;
    }

    /**
     * Return the mandatory courses of this form
     * @return a HashMap with the courses
     */
    public Map<String, Course> getAdvanceCourses() {
        return advanceCourses;
    }

    /**
     * Return the Elective courses of this form
     * @return a HashMap with the courses
     */
    public Map<String, Course> getElectiveCourses() {
        return electiveCourses;
    }

    /**
     * Return all the descriptions if the form as a string
     * @return
     */
    public String getExplanation() {
        return explanation;
    }

    public String getOtherCoursesExplanation() {
        return otherCoursesExplanation;
    }

    public String getOtherCourses() {
        return otherCourses;
    }

    public String getComment(){return comment;}

    /**
     * Return the state of the form
     * @return Approved, Rejected, Revision
     */
    public String getState() {
        return this.state;
    }

    /**
     * Return the name of the master program
     * @return DST, IST, ST
     */
    public String getProgram() {
        return program;
    }

    // -- Commands ---------------------------------------------------


    /** Sets the student number
     * @requires s_number to be non-null
     * @param s_number the student number
     */
    public void setS_number(String s_number) {
        this.s_number = s_number;
    }

    /**
     * Sets the student credits
     */
    public void setEC(int EC){this.EC = EC;}

    /**
     * Set if the student wants an internship
     */
    public void setInternship(boolean internship) {
        this.internship = internship;
    }

    /**
     * Set true or false if this is the first time of the form
     */
    public void setFirstTime(boolean firstTime){
        this.firstTime = firstTime;
    }
    /**
     * set all the descriptions of the form
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setOtherCourses(String otherCourses) {
        this.otherCourses = otherCourses;
    }

    public void setOtherCoursesExplanation(String otherCoursesExplanation) {
        this.otherCoursesExplanation = otherCoursesExplanation;
    }
    public void setAdvanceCourses(HashMap<String,Course> AdCourses){
        this.advanceCourses = AdCourses;
    }

    public void setElectiveCourses(HashMap<String, Course> electiveCourses) {
        this.electiveCourses = electiveCourses;
    }

    public void setComment(String comment){this.comment = comment;}


}
