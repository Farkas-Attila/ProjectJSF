package com.farkas.projectjsf.namedbeans;

import com.farkas.projectjsf.entities.AppUser;
import com.farkas.projectjsf.entities.Department;
import com.farkas.projectjsf.exceptions.DuplicateName;
import com.farkas.projectjsf.sessionbeans.AdminService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 * @author Farkas Attila
 */
@Named(value = "adminServices")
@ViewScoped
public class AdminServiceBean implements Serializable {

    @EJB
    private AdminService service;

    private List<AppUser> users;
    private List<Department> departments;
    private List<AppUser> filteredUsers;

    private AppUser selectedUser;
    private Department selectedDepartment;
    private String editType;
    // E: edit user   A: add new user
    private String password;

    public AdminServiceBean() {
    }

    public void readAllData() {
        readAllUsers();
        readAllDepartments();
    }

    public void readAllUsers() {
        users.clear();
        try {
            List<AppUser> userDB = service.readAllUsers();
            if (userDB != null) {
                users.addAll(userDB);
            }
        } catch (EJBException e) {
        }
    }

    public void readAllDepartments() {
        departments.clear();
        try {
            List<Department> depDB = service.readAllDepartments();
            if (depDB != null) {
                departments.addAll(depDB);
            }
        } catch (EJBException e) {
        }
    }

    @PostConstruct
    private void init() {
        users = new ArrayList<>();
        departments = new ArrayList<>();
        filteredUsers = new ArrayList<>();
        readAllData();
        addUserMode();
        initAddDepartment();
    }

    public void addUserMode() {
        // add new user mode
        editType = "A";
        selectedUser = new AppUser();
        setUsertype("U");
        password = "";
    }

    public void editUserMode(AppUser user) {
        editType = "E";
        password = "";
        try {
            selectedUser = user.clone();
        } catch (CloneNotSupportedException ex) {
        }
    }

    // DYNAMIC PASSWORD VALIDATION
    public void pswValidator(FacesContext context, UIComponent component, Object value) {
        boolean isValid = false;
        if (value != null) {
            String psw = ((String) value).trim();
            if ((psw.length() > 4) || (isEditUserMode() && psw.isEmpty())) {
                isValid = true;
            }
        }
        if (!isValid) {
            String msg = "Password: minimum length is 5 characters";
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
        }
    }

    public boolean isEditUserMode() {
        return (editType.equals("E"));
    }

    public boolean isAddUserMode() {
        return (editType.equals("A"));
    }

    public void initAddDepartment() {
        selectedDepartment = new Department();
    }

    public void saveUser(ActionEvent e) {
        // save user to DB
        RequestContext reqContext = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        password = password.trim();
        try {
            if (isAddUserMode()) {
                service.addNewUser(selectedUser, password);
            }
            if (isEditUserMode()) {
                service.modifyUser(selectedUser, (password.isEmpty() ? null : password));
            }
            reqContext.addCallbackParam("saveFailed", false);
            password = "";
        } catch (DuplicateName | EJBException ex) {
            reqContext.addCallbackParam("saveFailed", true);
            String errStr = "Save failed.";
            errStr += (ex instanceof DuplicateName ? " Duplicate user name." : "");
            facesContext.addMessage("growl-user",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, errStr, errStr));
        }
    }

    public void saveDepartment(ActionEvent e) {
        // save department to DB
        RequestContext reqContext = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            service.addNewDepartment(selectedDepartment);
            reqContext.addCallbackParam("saveFailed", false);
        } catch (DuplicateName | EJBException ex) {
            reqContext.addCallbackParam("saveFailed", true);
            String errStr = "Save failed.";
            errStr += (ex instanceof DuplicateName ? " Duplicate department name." : "");
            facesContext.addMessage("growl-dep",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, errStr, errStr));
        }
    }

    // ********** GETERS/SETTERS **********
    // LIST OF ALL USERS 
    public List<AppUser> getUsers() {
        return users;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    // LIST OF ALL DEARTMENTS
    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    // LIST OF FILTERED USERS
    public List<AppUser> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<AppUser> users) {
        this.filteredUsers = users;
    }

    // SELECTED USER
    public String getUsername() {
        return selectedUser.getUsername();
    }

    public void setUsername(String username) {
        selectedUser.setUsername(username.trim());
    }

    public String getFullname() {
        return selectedUser.getFullname();
    }

    public void setFullname(String fullname) {
        selectedUser.setFullname(fullname.trim());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return selectedUser.getUsertype();
    }

    public void setUsertype(String usertype) {
        selectedUser.setUsertype(usertype);
    }

    public Department getDepartment() {
        return selectedUser.getDepartment();
    }

    public void setDepartment(Department department) {
        selectedUser.setDepartment(department);
    }

    // ADD NEW DEPARTMENT (GETTER/SETTER)
    public String getDepartmentName() {
        return selectedDepartment.getName();
    }

    public void setDepartmentName(String name) {
        selectedDepartment.setName(name.trim());
    }

}
