package com.farkas.projectjsf.sessionbeans;

import com.farkas.projectjsf.entities.AppUser;
import com.farkas.projectjsf.entities.AppUserPsw;
import com.farkas.projectjsf.entities.Department;
import com.farkas.projectjsf.exceptions.DuplicateName;
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
public class AdminService {

    @PersistenceContext(unitName = "ProjectJSF-ejbPU")
    private EntityManager em;

    public List<AppUser> readAllUsers() {
        TypedQuery<AppUser> result
                = em.createNamedQuery("AppUser.findAllUser", AppUser.class);
        List<AppUser> resultList = result.getResultList();
        return resultList;
    }

    public List<Department> readAllDepartments() {
        TypedQuery<Department> result
                = em.createNamedQuery("Department.findAllDepartments", Department.class);
        List<Department> resultList = result.getResultList();
        return resultList;
    }

    public void addNewUser(AppUser user, String password) throws DuplicateName {
        if (isDuplicateUsername(user.getUsername())) {
            throw new DuplicateName();
        }
        AppUserPsw psw = new AppUserPsw();
        psw.setPsw(password);
        psw.setAppUser(user);
        em.persist(user);
        em.persist(psw);
    }

    public void modifyUser(AppUser user, String password) {
        if (user != null && user.getId() != null) {
            em.merge(user);
            if (password != null) {
                AppUserPsw appUserPsw = findAppUserPsw(user);
                appUserPsw.setPsw(password);
                em.merge(appUserPsw);
            }
        }
    }

    public AppUserPsw findAppUserPsw(AppUser user) {
        TypedQuery<AppUserPsw> result
                = em.createNamedQuery("AppUserPsw.findPswByAppUserObj", AppUserPsw.class)
                .setParameter("appuserobj", user);
        List<AppUserPsw> resultList = result.getResultList();
        return (resultList.isEmpty() ? null : resultList.get(0));
    }

    private boolean isDuplicateUsername(String username) {
        TypedQuery<AppUser> result
                = em.createNamedQuery("AppUser.findUserByUsername", AppUser.class)
                .setParameter("username", username);
        List<AppUser> resultList = result.getResultList();
        return (!resultList.isEmpty());
    }

    public void addNewDepartment(Department dep) throws DuplicateName {
        if (isDuplicateDepartmentName(dep.getName())) {
            throw new DuplicateName();
        }
        em.persist(dep);
    }

    private boolean isDuplicateDepartmentName(String name) {
        TypedQuery<Department> result
                = em.createNamedQuery("Department.findDepartmentByName", Department.class)
                .setParameter("name", name);
        List<Department> resultList = result.getResultList();
        return (!resultList.isEmpty());
    }

}
