package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import entities.User;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import facades.UserFacade;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class UserResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final UserFacade FACADE =  UserFacade.getUserFacade(EMF);
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("addperson")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(String content) {
        UserDTO userDTO = GSON.fromJson(content, UserDTO.class);
        UserDTO newUserDTO = FACADE.createUser(userDTO);
        return Response.ok().entity(GSON.toJson(newUserDTO)).build();
    }
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllUsers() {
        return Response.ok().entity(GSON.toJson(FACADE.getAllUsers())).build();
    }

    @GET
    @Path("user/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserByID(@PathParam("id") long id) {
        UserDTO userId = FACADE.getUserByID(id);
        return Response.ok().entity(GSON.toJson(userId)).build();
    }

    @DELETE
    @Path("user/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteUser(@PathParam("id") long id) {
        UserDTO userDeleted = FACADE.deleteUser(id);
        return Response.ok().entity(GSON.toJson(userDeleted)).build();
    }

}