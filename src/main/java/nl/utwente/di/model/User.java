package nl.utwente.di.model;

/**
 * Abstract class for keeping a user in the StudyPlan application.
 */
public abstract class User {

    // -- Instance variables -----------------------------------------


    private String first_name;
    private String surname;
    private String email;
    private String password;


    // -- Constructors -----------------------------------------------


    /**
     * Creates a user with its basic attributes
     * @requires first_name and surname to be non-null
     * @param first_name of the user
     * @param surname of the user
     * @param email with which the user logs in
     * @param password with which the user logs in
     */
    public User(String first_name, String surname,
                String email,String password){
        this.first_name = first_name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }


    /**
     * Creates a user with empty attributes
     */
    public User(){}


    // -- Queries ----------------------------------------------------


    /**
     * Returns current fist name that this user has
     * @return the first name of the user
     */
    public String getFirst_name() { return first_name; }

    /**
     * Returns current surname that this user has
     * @return the surname of the user
     */
    public String getSurname() { return surname; }

    /**
     * Returns current email address that this user has
     * @return the email of the user
     */
    public String getEmail() { return email; }

    /**
     * Returns current password that this user has
     * @return the password of the user
     */
    public String getPassword() { return password; }

}
