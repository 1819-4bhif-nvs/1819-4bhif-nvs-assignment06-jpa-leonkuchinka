package at.htl.gca.rest;

import at.htl.gca.model.Golfer;
import at.htl.gca.model.Team;
import at.htl.gca.model.TeeTime;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Path("teetime")
@Stateless
public class TeeTimeEndpoint {

    @PersistenceContext
    EntityManager em;

    @Path("findall")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAll(){
        List<TeeTime> list = em.createNamedQuery("TeeTime.findAll", TeeTime.class).getResultList();
        GenericEntity entity = new GenericEntity<List<TeeTime>>(list){};
        if(list != null && !list.isEmpty())
            return Response.ok(entity).build();
        else
            return Response.noContent().build();
    }

    @Path("find/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response get(@PathParam("id") long id){
        TeeTime res = em.createQuery("select t from TeeTime t join fetch t.players where t.id = ?1", TeeTime.class).setParameter(1, id).getSingleResult();
        if(res != null)
            return Response.ok(res).build();
        else
            return Response.noContent().build();
    }

    @Path("delete/{id}")
    @DELETE
    public Response delete(@PathParam("id") long id){
        try{
            TeeTime t = em.find(TeeTime.class, id);
            if(t != null){
                List<Golfer> list = new ArrayList<>(t.getPlayers());
                for (Golfer g:list) {
                    g.removeTeeTime(t);
                }
                em.remove(t);
            }
        }catch (Exception e){
            return Response.status(404).build();
        }
        return Response.ok().build();
    }



}
