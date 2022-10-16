package nl.utwente.di.resources;

import nl.utwente.di.dao.FormDao;
import nl.utwente.di.dao.UserDao;
import nl.utwente.di.model.Form;
import nl.utwente.di.model.Mentor;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Resources for the Mentor
 */
@Path("/mentor")
public class MentorService {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    String id;

    /**
     * Retrieves a Mentor depending on its worker number
     * @return a student object
     */
    @GET
    @Path("/mentorId")
    @Produces(MediaType.APPLICATION_JSON)
    public Mentor getMentorHTML() {
        Mentor mentor = UserDao.instance.getMentorByID(id);
        if(mentor==null)
            throw new RuntimeException("Get: Mentor with " + id +  " not found");
        return mentor;
    }

    /**
     * Retrieves a list with the form which are in the queue
     * @return a list of forms
     */
    @GET
    @Path("/submitted_forms")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Form> getFormsQueueBrowser() {
        List<Form> forms = new ArrayList<>();
        forms.addAll(FormDao.instance.getFormsQueue().values());
        return forms;
    }


    @PUT
    @Path("/form/{form_id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void newComment(@PathParam("form_id") String form_id,
                             @FormParam("comment") String comment,
                             @FormParam("state") String state,
                             @Context HttpServletResponse servletResponse) throws SQLException {
        System.out.println(form_id + " >> " + comment + " >>>>  " + state);
        FormDao.instance.changeState(form_id,state,comment);
    }

    /**
     * Retrieves a list with the approved form
     * @return a list of forms
     */
    @GET
    @Path("/approved_forms")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Form> getApprovedFormsBrowser() {
        List<Form> forms = new ArrayList<>();
        forms.addAll(FormDao.instance.getFormsApproved().values());
        return forms;
    }

    /**
     * Retrieves a list with the form which are archived
     * @return a list of forms
     */
    @GET
    @Path("/archive")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Form> getArchiveFormsBrowser() {
        List<Form> forms = new ArrayList<>();
        forms.addAll(FormDao.instance.getFormsRejected().values());
        return forms;
    }

    @GET
    @Path("/display/{form_id}")
    @Produces( MediaType.APPLICATION_JSON)
    public Form getFormDetails(@PathParam("form_id") String form_id) {

        Form form =  FormDao.instance.getFormsArchive().get(form_id);
        System.out.println(form.getF_id());
        /**if (form == null){
            throw new RuntimeException("Get: Form with id = " + form_id + " not found");}*/
        return form;
    }


    @POST
    @Path("/approval/{form_id}")
    public String approveForm(@PathParam("form_id") String form_id) {

        // simulate approval - by removing the form from the queue
        Form result = FormDao.instance.getFormsQueue().remove(form_id);

        if (result == null)
            return "Approval: Form with id = " + form_id + " not found";

        FormDao.instance.getFormsApproved().put(form_id, result);
        return "Approval: Successfully approved form with " + form_id + " + form_id";

    }

    @POST
    @Path("/reject/{form_id}")
    public String rejectForm(@PathParam("form_id") String form_id) {

        // simulate reject - by removing the form from the queue
        // TODO implement - insert this form into the rejected forms list
        Form result = FormDao.instance.getFormsQueue().remove(form_id);

        if (result == null)
            return "Reject: Form with id = " + form_id + " not found";

        return "Reject: Successfully rejected form with " + form_id + " + form_id";

    }


}
