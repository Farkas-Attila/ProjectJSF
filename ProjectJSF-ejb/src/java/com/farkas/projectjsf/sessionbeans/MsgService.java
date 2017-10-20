package com.farkas.projectjsf.sessionbeans;
import com.farkas.projectjsf.entities.AppUser;
import com.farkas.projectjsf.entities.Msg;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author Farkas Attila
 */
@Stateless
@LocalBean
public class MsgService {
    
    @PersistenceContext(unitName = "ProjectJSF-ejbPU")
    private EntityManager em;
    
    public List<Msg> readInMsgByUser(AppUser user){
        TypedQuery<Msg> result
                = em.createNamedQuery("Msg.findInMsgByUser", Msg.class)
                .setParameter("toUser", user);
        List<Msg> resultList = result.getResultList();
        return resultList;
    }
    
    public List<Msg> readOutMsgByUser(AppUser user){
        TypedQuery<Msg> result
                = em.createNamedQuery("Msg.findOutMsgByUser", Msg.class)
                .setParameter("fromUser", user);
        List<Msg> resultList = result.getResultList();
        return resultList;
    }
    
    public List<AppUser> readMsgRecipients(Long userId) {
        // find all users except the sender
        TypedQuery<AppUser> result
                = em.createNamedQuery("AppUser.findMsgRecipients", AppUser.class)
                .setParameter("myUserId", userId);
        List<AppUser> resultList = result.getResultList();
        return resultList;
    }
    
    public void saveMessage(Msg msg) {
        em.persist(msg);
    }
    
    public void modifyReadFlag(Msg msg) {
        if (msg != null){
            em.merge(msg);
        }
    }
}