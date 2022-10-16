package nl.utwente.di.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class model for keeping a student in the StudyPlan application.
 */
@XmlRootElement
public class Student extends User{

    // -- Instance variables -----------------------------------------


    /**
     * The student number at the University
     */
    private String s_id;


    // -- Constructors -----------------------------------------------


    /**
     * Creates a student user with its basic attributes
     * @requires first_name and surname to be non-null
     * @param first_name of the user
     * @param surname of the user
     * @param email with which the user logs in
     * @param password with which the user logs in
     */
    public Student (String s_id, String first_name,
                    String surname, String email, String password){
        super(first_name,surname,email,password);
        this.s_id = s_id;
    }

    /**
     * Creates a student user with empty attributes
     */
    public Student(){}


    // -- Commands ---------------------------------------------------


    /**
     * Sets a student number
     * @requires s_id to be non-null
     * @ensures s_id to be set to this student
     * @param s_id the student number of this user
     */
    public void setS_id(String s_id) {
        this.s_id = s_id;
    }


    // -- Queries ----------------------------------------------------


    /**
     * Returns current student number that this user has
     * @return the student number of the user
     */
    public String getS_id() {
        return s_id;
    }

}
