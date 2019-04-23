package at.htl.gca.rest;

import at.htl.gca.model.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("golfer")
@Stateless
public class GolferEndpoint {

    @PersistenceContext
    EntityManager em;

    @Path("teamplayers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllTeamPlayer(){
        List<TeamPlayer> list = em.createNamedQuery("TeamPlayer.findall", TeamPlayer.class).getResultList();
        return Response.ok(list).build();
    }

    @Path("hobbyplayers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllHobbyPlayer(){
        List<HobbyPlayer> list = em.createNamedQuery("HobbyPlayer.findall", HobbyPlayer.class).getResultList();
        return Response.ok(list).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response findAll(){
        List<Golfer> list = em.createNamedQuery("Golfer.findall", Golfer.class).getResultList();
        GenericEntity entity = new GenericEntity<List<Golfer>>(list){};
        return Response.ok(entity).build();
    }

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response find(@PathParam("id") long id){
        Golfer g = em.find(Golfer.class, id);
        if(g != null){
            return Response.ok(g).build();
        }
        else
        {
            return Response.noContent().build();
        }
    }

    @Path("{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id){
        try{
            Golfer g = em.find(Golfer.class, id);
            if(g != null){
                List<TeeTime> teetimes = new ArrayList<>(g.getTeeTimes());
                for (TeeTime t:teetimes) {
                    t.removePlayer(g);
                }
                em.remove(g);
            }
        }
        catch (Exception e){
            return Response.status(404).build();
        }
        return Response.ok().build();
    }

    /*
    * path: http://localhost:8085/gca/api/golfer/teamplayer
    * body:
    * {
        "name": "Max Mustermann",
        "hcp": -45,
        "age": 49,
        "joined": 2019,
        "regularPlayer": false,
        "joined": 2015,
        "regularPlayer": true,
        "team": {
            "id": 2,
            "teamName": "Mens Team"
          }
      }
    *
    * */
    @Path("teamplayer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(TeamPlayer p){
        em.persist(p);
        em.flush();
        return Response.created(URI.create("http://localhost:8085/gca/api/golfer/" + p.getId())).build();
    }

    /*
    * path: http://localhost:8085/gca/api/golfer/hobbyplayer
    * body:
    * {
        "name": "Max Mustermann",
        "hcp": -45,
        "age": 49,
        "premiumMember": true
      }
    *
    * */
    @Path("hobbyplayer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(HobbyPlayer p){
        em.persist(p);
        em.flush();
        return Response.created(URI.create("http://localhost:8085/gca/api/golfer/" + p.getId())).build();
    }

    /*
    * path: http://localhost:8085/gca/api/golfer/teamplayer/1
    * body:
    * {
        "name": "Leon Kuchinka",
        "hcp": -1.7,
        "age": 18,
        "joined": 2015,
        "regularPlayer": true,
        "team": {
            "id": 1,
            "teamName": "Youth Team"
          }
      }
    *
    * */
    @Path("teamplayer/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, TeamPlayer p){
        TeamPlayer old = em.find(TeamPlayer.class, id);
        if(old == null)
            return Response.status(404).build();
        p.setId(old.getId());
        em.merge(p);
        return Response.ok().build();
    }


}
