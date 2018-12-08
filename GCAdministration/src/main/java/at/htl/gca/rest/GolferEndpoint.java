package at.htl.gca.rest;

import at.htl.gca.model.Golfer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("golfer")
@Stateless //um sich nicht um Transaktionen kümmern zu müssen
public class GolferEndpoint {

    @PersistenceContext
    EntityManager em;

    @Path("findall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Golfer> findAll(){
        return em.createNamedQuery("Golfer.findAll", Golfer.class).getResultList();
    }

    @Path("find/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Golfer find(@PathParam("id") long id){
        return em.find(Golfer.class, id);
    }


    @Path("delete/{id}")
    @DELETE
    @Transactional
    public void delete(@PathParam("id") long id){
        Golfer g = em.find(Golfer.class, id);
        em.remove(g);
    }

    @Path("put/{id}")
    @PUT
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public void put(@PathParam("id") long id, Golfer golfer){
        Golfer g = em.find(Golfer.class, id);
        g.setAge(golfer.getAge());
        g.setHcp(golfer.getHcp());
        g.setName(golfer.getName());
        em.merge(g);
    }

    @Path("post")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Long post(Golfer golfer){
        em.persist(golfer);
        em.flush();
        return golfer.getId();
    }


}
