package nl.utwente.di.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class model for keeping a Mentor in the StudyPlan application.
 */
@XmlRootElement
public class Mentor extends User{

    // -- Instance variables -----------------------------------------


    /**
     * The employee number at the University
     */
    private String employee_no;


    // -- Constructors -----------------------------------------------


    /**
     * Creates a Mentor user with its basic attributes
     * @requires first_name and surname to be non-null
     * @param first_name of the user
     * @param surname of the user
     * @param email with which the user logs in
     * @param password with which the user logs in
     */
    public Mentor (String employee_no, String first_name,
                    String surname, String email, String password){
        super(first_name,surname,email,password);
        this.employee_no = employee_no;
    }

    /**
     * Creates a Mentor user with empty attributes
     */
    public Mentor(){}


    // -- Commands ---------------------------------------------------


    /**
     * Sets a Mentor number
     * @requires employee_no to be non-null
     * @ensures employee_no to be set to this Mentor
     * @param employee_no the employee number of this user
     */
    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }


    // -- Queries ----------------------------------------------------


    /**
     * Returns current employee number that this user has
     * @return the employee number of the user
     */
    public String getEmployee_no() {
        return employee_no;
    }
}
