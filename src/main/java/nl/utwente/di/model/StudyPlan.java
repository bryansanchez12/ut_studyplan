package nl.utwente.di.model;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

/**
 * Class model for keeping a StudyPlan in the application.
 */
@XmlRootElement
public class StudyPlan {

    // -- Instance variables -----------------------------------------


    private String f_id;
    private String s_number;
    private String s_email;
    private HashMap<String, Integer> course_codes_ecs;
    private boolean internship;
    private boolean approved;


    // -- Constructors -----------------------------------------------


    /**
     * Creates a StudyPlan depending on the form
     * @requires course_name to be non-null
     */
    public StudyPlan (String f_id, String s_number, String s_email,
                      HashMap<String, Integer> course_codes_ecs,
                      boolean internship){
        this.f_id = f_id;
        this.s_number = s_number;
        this.s_email = s_email;
        this.course_codes_ecs = course_codes_ecs;
        this.internship = internship;
        this.approved = false;
    }


    // -- Commands ---------------------------------------------------


    /**
     * Sets true or false to this StudyPlan
     * @ensures approved to be set to this plan
     * @param approved true or false depending on this approval
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }


    // -- Queries ----------------------------------------------------


    /**
     * Returns the id of the form in which this plan is based on
     * @return the f_id of the form
     */
    public String getF_id() {
        return f_id;
    }

    /**
     * Returns student number to which this plan belongs
     * @return the student number
     */
    public String getS_number() {
        return s_number;
    }

    /**
     * Returns true if this study plan is approved, or false otherwise
     * @return true or false depending on this approval
     */
    public boolean isApproved() {
        return approved;
    }



}
