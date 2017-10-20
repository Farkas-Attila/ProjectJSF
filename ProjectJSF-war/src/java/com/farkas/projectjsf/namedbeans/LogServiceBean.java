package com.farkas.projectjsf.namedbeans;

import com.farkas.projectjsf.entities.AppUser;
import com.farkas.projectjsf.exceptions.InvalidPassword;
import com.farkas.projectjsf.exceptions.InvalidUsername;
import com.farkas.projectjsf.sessionbeans.LogService;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

/**
 * @author Farkas Attila
 */
@Named(value = "logService")
@SessionScoped
public class LogServiceBean implements Serializable {

    @EJB
    private LogService service;

    private String username;
    private String pwd;
    private AppUser appUser;

    public boolean isLoggedIn() {
        return (appUser != null);
    }

    public boolean isAdmin() {
        return (appUser != null && appUser.getUsertype().equals("A"));
    }

    public void checkAuthentication(ComponentSystemEvent event) {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        FacesContext context = FacesContext.getCurrentInstance();
        ConfigurableNavigationHandler handler
                = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
        if (!viewId.contains("login")) {
            if (!isLoggedIn()) {
FacesContext.getCurrentInstance().getViewRoot().setViewId("/login.xhtml");
                handler.performNavigation("login?faces-redirect=true");
            } else if (viewId.contains("users-admin") && !this.isAdmin()) {
FacesContext.getCurrentInstance().getViewRoot().setViewId("/login.xhtml");
                handler.performNavigation("login?faces-redirect=true");
            }
        }
    }

    public void resetAuthUserData() {
        appUser = null;
    }

    public String userAuthentication() {
        username = username.trim();
        pwd = pwd.trim();
        resetAuthUserData();
        String navigationStr = "";
        try {
            if (username.isEmpty() || pwd.isEmpty()) {
                FacesContext cont = FacesContext.getCurrentInstance();
                String errStr = "Invalid user name or password";
                cont.addMessage("growl-login", new FacesMessage(FacesMessage.SEVERITY_ERROR, errStr, errStr));
            } else {
                appUser = service.authenticateUser(username, pwd);
                if (appUser != null) {
                    navigationStr = "messages";
                }
            }
        } catch (InvalidPassword | InvalidUsername e) {
            FacesContext cont = FacesContext.getCurrentInstance();
            if (e instanceof InvalidUsername) {
                String errStr = "Invalid user name";
                cont.addMessage("growl-login", new FacesMessage(FacesMessage.SEVERITY_ERROR, errStr, errStr));
            }
            if (e instanceof InvalidPassword) {
                String errStr = "Invalid password";
                cont.addMessage("growl-login", new FacesMessage(FacesMessage.SEVERITY_ERROR, errStr, errStr));
            }
        } finally {
            pwd = "";
        }
        return navigationStr;
    }

    public String logout() {
        pwd = "";
        resetAuthUserData();
        return "login";
    }

    // GETTERS & SETTERS
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Long getAppUserId() {
        return appUser.getId();
    }

    public AppUser getAppUser() {
        return appUser;
    }

}
