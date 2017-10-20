package com.farkas.projectjsf.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

/**
 * @author Farkas Attila
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "AppUser.findUserByUsername",
            query = "SELECT u FROM AppUser u WHERE u.username = :username"),
    @NamedQuery(name = "AppUser.findAllUser",
            query = "SELECT u FROM AppUser u"),
    @NamedQuery(name = "AppUser.findMsgRecipients",
            query = "SELECT u FROM AppUser u WHERE u.id <> :myUserId")
})
public class AppUser implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String fullname;

    // U-user, A-administrator
    @Column(nullable = false)
    private String usertype;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT", nullable = false)
    private Department department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppUser)) {
            return false;
        }
        AppUser other = (AppUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        // for OmniFaces converter
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }

    @Override
    public AppUser clone() throws CloneNotSupportedException {
        return (AppUser) super.clone();
    }

}
