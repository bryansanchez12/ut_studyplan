package nl.utwente.di.resources;

import nl.utwente.di.dao.CourseDao;
import nl.utwente.di.dao.FormDao;
import nl.utwente.di.dao.UserDao;
import nl.utwente.di.model.Course;
import nl.utwente.di.model.Form;
import nl.utwente.di.model.Student;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Will map the resources for the form
 */
@Path("/form")
public class StudentPageResource {

    //Allows to insert contextual objects into Form

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    Student student;


    /**
     * Retrieves a Student depending on its student number
     * @return a student object
     */
    @GET
    @Path("/student")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudent() {
        Map<String,Student> students = UserDao.instance.getUsers_Model();
        Set <String> keys = students.keySet();
        Iterator<String> keyIter = keys.iterator();
        if(keyIter.hasNext()){
            String course_id = keyIter.next();
            student = students.get(course_id);
            CourseDao.instance.setUp();
        }
        if(student==null)
            throw new RuntimeException("Get: Student with " +  " not found");
        return student;
    }

    /**
     * Retrieves a List of the Mandatory Courses
     * @return a List of Courses
     */
    @GET
    @Path("/mandatory_courses/{program_type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getMandatoryCourses(@PathParam("program_type") String program_type){
        List<Course> mandatoryCourses = new ArrayList<Course>();
        mandatoryCourses.addAll((CourseDao.instance.getMandatoryCourses(program_type).values()));
        return mandatoryCourses;
    }
    /**
     * Retrieves a List of the Ethics Courses
     * @return a List of Courses
     */
    @GET
    @Path("/ethics_courses/{program_type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getEthicsCourses(@PathParam("program_type") String program_type){
        List<Course> ethicsCourses = new ArrayList<Course>();
        ethicsCourses.addAll((CourseDao.instance.getEthicsCourses(program_type).values()));
        return ethicsCourses;
    }

    /**
     * retrieves a List of Graduation Courses
     * @return a List of Courses
     */
    @GET
    @Path("/graduation_courses/{program_type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getGraduationCourses(@PathParam("program_type") String program_type){
        List<Course> graduationCourses = new ArrayList<Course>();
        graduationCourses.addAll((CourseDao.instance.getGraduationCourses(program_type).values()));
        return graduationCourses;
    }

    /**
     * Retrieves a List of the Advanced Courses
     * @return a List of Courses
     */
    @GET
    @Path("/advanced_courses/{program_type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getAdvancedCourses(@PathParam("program_type") String program_type){
        List<Course> advancedCourses = new ArrayList<Course>();
        advancedCourses.addAll((CourseDao.instance.getAdvancedCourses(program_type).values()));
        return advancedCourses;
    }

    /**
     * retrieves a List of Elective Courses
     * @return a List of Courses
     */
    @GET
    @Path("/elective_courses/{program_type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getElectiveCourses(@PathParam("program_type") String program_type){
        List<Course> electiveCourses = new ArrayList<Course>();
        electiveCourses.addAll((CourseDao.instance.getElectiveCourses(program_type).values()));
        return electiveCourses;
    }

    /**
     * retrieves a List of Elective Courses
     * @return a List of Courses
     */
    @GET
    @Path("/submitted_forms/{student_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Form> getSubmittedForms(@PathParam("student_id") String student_id){
        List<Form> forms = new ArrayList<Form>();
        forms.addAll((FormDao.instance.getStudentForms(student_id).values()));
        return forms;
    }

    /**
     * Deletes the session, a successfully log out
     * @return a List of Courses
     */
    @GET
    @Path("/logout/{student_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void logOut(@PathParam("student_id") String student_id,
                       @Context HttpServletResponse servletResponse) throws IOException {
        //TODO function that deletes the session token of this student
        System.out.println(">> Student with id: " + student_id + " was logged out");
        servletResponse.sendRedirect("/studyplan");
    }

    @POST
    @Path("/submitted")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void newForm(@FormParam("inputStudent") String s_id,
                        @FormParam("inputEmail") String email, @FormParam("totalEC") int EC,
                        @FormParam("boolean")boolean firstTime, @FormParam("internship") boolean internship,
                        @FormParam("explanation")String explanation, @FormParam("other_courses") String o_Courses,
                        @FormParam("oCoursesExplanation") String OCoursesExplanation,
                        @FormParam("form_type") String program,
                        @FormParam("adCourses")String adCourses,@FormParam("elCourses") String elCourses ,
                        @Context HttpServletResponse servletResponse) throws IOException{
        System.out.println("\n******************************************\n");
        // Date object
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calObj = Calendar.getInstance();
        String date = df.format(calObj.getTime());


        //ID of the form
        String[] list = date.split("/");
        String f_id = list[0] + list[1] + list[2] + s_id;
        String newDate = list[2] +"-"+list[1]+ "-" + list[0] ;

        String[] adCoursesList = adCourses.split(",");
        String[] elCoursesList = elCourses.split(",");

        // Create a hashmap and add the advanced courses
        Map<String,Course> advanced_courses = new HashMap<>();
        for (String n : adCoursesList){
            advanced_courses.put(n,CourseDao.instance.getAdvancedCourses(program).get(n));
        }

        // Create a hashmap and add the elective courses
        Map<String,Course> elective_courses= new HashMap<>();
        for (String n : elCoursesList){
            elective_courses.put(n,CourseDao.instance.getElectiveCourses(program).get(n));
        }

        /**         TEST*/
         System.out.println("Form id: " + f_id);
         System.out.println("\nStudent id: " + s_id);
         System.out.println("Date: " + newDate);
         System.out.println("First Time: " + firstTime);
         System.out.println("Explanation: \n>>" + explanation+"<<\n");
         System.out.println("Advanced Courses:\n" + adCourses +"\n");
         System.out.println("Electives Courses:\n" +elCourses+"\n");
         System.out.println("Internship: " + internship);
         System.out.println("Other Courses:\n>>" + o_Courses+"<<\n");
         System.out.println("Other Courses Explanation:\n>>"+ OCoursesExplanation + "<<\n");
         System.out.println("Total EC: " + EC);

        Form form = new Form(f_id, s_id,newDate, EC, firstTime,advanced_courses,elective_courses,internship,program);

        if(explanation != null){form.setExplanation(explanation);}
        if(o_Courses != null){form.setOtherCourses(o_Courses);}
        if(OCoursesExplanation != null){form.setOtherCoursesExplanation(OCoursesExplanation);}


        FormDao.instance.getFormsQueue().put(f_id, form);
        FormDao.instance.getFormsArchive().put(f_id, form);
        FormDao.instance.createNewForm(form);
        //servletResponse.sendRedirect("../create_form.html");

        System.out.println("\n******************************************\n");
        System.out.println("Form with id: " + f_id + " was created successfully");
    }

}
