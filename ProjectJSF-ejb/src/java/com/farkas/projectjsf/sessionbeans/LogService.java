package com.farkas.projectjsf.sessionbeans;

import com.farkas.projectjsf.entities.AppUser;
import com.farkas.projectjsf.entities.AppUserPsw;
import com.farkas.projectjsf.entities.Department;
import com.farkas.projectjsf.exceptions.InvalidPassword;
import com.farkas.projectjsf.exceptions.InvalidUsername;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class LogService {

    @PersistenceContext(unitName = "ProjectJSF-ejbPU")
    private EntityManager em;

    private AppUser findAppUserByUsername(String username) {
        TypedQuery<AppUser> result
            = em.createNamedQuery("AppUser.findUserByUsername", AppUser.class)
            .setParameter("username", username);
        List<AppUser> resultList = result.getResultList();
        return (resultList.isEmpty() ? null : resultList.get(0));
    }

    private boolean validatePsw(AppUser appUser, String psw) {
        TypedQuery<AppUserPsw> result
            = em.createNamedQuery("AppUserPsw.findPswByAppUserObj", AppUserPsw.class)
            .setParameter("appuserobj", appUser);
        List<AppUserPsw> resultList = result.getResultList();
        AppUserPsw appUserPsw = (resultList.isEmpty() ? null : resultList.get(0));
        return (appUserPsw != null && appUserPsw.getPsw().equals(psw));
    }

    public AppUser authenticateUser(String username, String pwd)
            throws InvalidUsername, InvalidPassword {
        AppUser appUser = findAppUserByUsername(username);
        if (appUser == null) {
            throw new InvalidUsername();
        } else {
            if (validatePsw(appUser, pwd)){
                return appUser;
            } else {
                throw new InvalidPassword();
            }
        }
    }

}