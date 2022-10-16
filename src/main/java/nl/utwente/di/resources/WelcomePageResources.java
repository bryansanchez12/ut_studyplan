package nl.utwente.di.resources;

import nl.utwente.di.dao.UserDao;
import nl.utwente.di.model.Student;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Resources for a login page
 */
@Path("/login/")
public class WelcomePageResources {

    /**
     * Retrieves true if the student email and password match with the ones in the databases
     * @return a boolean
     */
    @GET
    @Path("/credentials")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean getMandatoryCourses(){
        boolean result = false;
        // TODO function that searches for the password which belongs to the email address
        return result;
    }

    @POST
    @Path("/create_user")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public void createUser(@FormParam("inputStNumber") String st_number,
                         @FormParam("inputFsName") String firstName,
                           @FormParam("inputSurname") String surname,
                           @FormParam("inputEmail") String email,
                           @FormParam("inputPassword") String password,
                           @Context HttpServletResponse servletResponse) throws IOException {
        String temp = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String words[] = temp.split("\\$");
        password = words[3];
        if( UserDao.instance.newUser(st_number,firstName,surname,email,password) ){
            servletResponse.sendRedirect("/studyplan/confirm_user_creation.html");
        } else {
            servletResponse.sendRedirect("/studyplan");
        }

    }

    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public void validate(@FormParam("inputEmail") String email,
                         @FormParam("inputPassword") String password,
                         @Context HttpServletResponse servletResponse,
                         @Context HttpServletRequest servletRequest) throws IOException, ServletException {

        if (UserDao.instance.passwordMatches(email, password)){
            if(UserDao.instance.isStudent(email)){
                String s_id = UserDao.instance.getStudentByEmail(email).getS_id();
                Student st = UserDao.instance.getStudentByID(s_id);
                UserDao.instance.getUsers_Model().put(s_id, st);
                Cookie cookie = new Cookie("student_id", s_id);
                servletResponse.sendRedirect("/studyplan/student_page.html");
                servletResponse.addCookie(cookie);
            } else{
                String m_id = UserDao.instance.getMentorByID(email).getEmployee_no();
                Cookie cookie = new Cookie("student_id", m_id);
                servletResponse.sendRedirect("/studyplan/teacher.html");
                servletResponse.addCookie(cookie);
            }

        } else {
            servletResponse.sendRedirect("/studyplan");
        }
    }
}
