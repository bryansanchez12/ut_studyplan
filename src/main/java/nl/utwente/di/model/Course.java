package nl.utwente.di.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class model for keeping a course in the StudyPlan application.
 */
@XmlRootElement
public class  Course extends User{

    // -- Instance variables -----------------------------------------


    private String course_code;
    private String course_name;
    private int ec_number;
    private String block;
    private String program;


    // -- Constructors -----------------------------------------------


    /**
     * Creates a Course with its basic attributes
     * @requires course_name to be non-null
     * @param course_code that identifies the course
     * @param course_name the name of the course
     * @param block in which this course is taught
     * @param ec_number the amount of ECs
     */
    public Course (String course_code, String course_name, int ec_number,
                   String block, String program){
        this.course_code = course_code;
        this.program = program;
        this.course_name = course_name;
        this.ec_number = ec_number;
        this.block = block;
    }

    /**
     * Creates a Course with empty attributes
     */
    public Course(){}


    // -- Queries ----------------------------------------------------


    /**
     * Returns current code which identifies this course
     * @return the course code
     */
    public String getCourse_code() {
        return course_code;
    }

    /**
     * Returns current name of this course
     * @return the course name
     */
    public String getCourse_name() {
        return course_name;
    }


    /**
     * Returns number of ECs that this course has
     * @return the ec_number
     */
    public int getEc_number() {
        return ec_number;
    }

    /**
     * Returns the program name where this course belongs to
     * @return the program
     */
    public String getProgram() {
        return program;
    }
}
