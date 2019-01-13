package at.htl.gca.rest;

import at.htl.gca.model.Team;
import at.htl.gca.model.TeeTime;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Path("teetime")
public class TeeTimeEndpoint {

    @PersistenceContext
    EntityManager em;

    @Path("findall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        List<TeeTime> list = em.createNamedQuery("TeeTime.findAll", TeeTime.class).getResultList();
        if(list != null && !list.isEmpty())
            return Response.ok(list).build();
        else
            return Response.noContent().build();
    }

    @Path("find/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("id") long id){
        TeeTime res = em.find(TeeTime.class, id);
        if(res != null)
            return Response.ok(res).build();
        else
            return Response.noContent().build();
    }



}
