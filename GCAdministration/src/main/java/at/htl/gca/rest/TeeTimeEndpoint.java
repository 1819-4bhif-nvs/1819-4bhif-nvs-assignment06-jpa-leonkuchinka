package at.htl.gca.rest;

import at.htl.gca.model.TeeTime;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@Path("teetime")
public class TeeTimeEndpoint {

    @PersistenceContext
    EntityManager em;

    @Path("findall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TeeTime> getAll(){
        return em.createNamedQuery("TeeTime.findAll", TeeTime.class).getResultList();
    }

    @Path("find/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TeeTime getAll(@PathParam("id") long id){
        return em.find(TeeTime.class, id);
    }



}
