package at.htl.gca.rest;

import at.htl.gca.model.Team;
import at.htl.gca.model.TeamPlayer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("team")
@Stateless
public class TeamEndpoint {

    @PersistenceContext
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findall(){
        List<Team> list = em.createNamedQuery("Team.findall", Team.class).getResultList();
        if(list == null || list.isEmpty()){
            return Response.noContent().build();
        }
        return Response.ok(list).build();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") long id){
        Team t = em.find(Team.class, id);
        if(t == null)
            return Response.noContent().build();
        return Response.ok(t).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Team t){
        em.persist(t);
        em.flush();
        return Response.created(URI.create("http://localhost:8085/gca/api/team/" + t.getId())).build();
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id,  Team t){
        Team team = em.find(Team.class, id);
        team.setTeamName(t.getTeamName());
        em.merge(team);
        return Response.ok().build();
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") long id){
        try{
            Team t = em.find(Team.class, id);
            if(t != null){
                List<TeamPlayer> list = new ArrayList<>(t.getTeamMembers());
                for(TeamPlayer p:list){
                    t.removeMember(p);
                }
                em.remove(t);
            }
        }catch (Exception e){
            return Response.status(404).build();
        }
        return Response.ok().build();
    }

}
