package com.farkas.projectjsf.entities;

import com.farkas.projectjsf.sessionbeans.CryptoConverter;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Version;

/**
 * @author Farkas Attila
 */
@Entity
@NamedQuery(name = "AppUserPsw.findPswByAppUserObj",
            query = "SELECT p FROM AppUserPsw p WHERE p.appUser = :appuserobj")
public class AppUserPsw implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @OneToOne
    @JoinColumn(name = "APPUSER_ID", nullable = false, unique = true)
    private AppUser appUser;

    @Convert(converter = CryptoConverter.class)
    @Column(nullable = false)
    private String psw;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
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
        if (!(object instanceof AppUserPsw)) {
            return false;
        }
        AppUserPsw other = (AppUserPsw) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.farkas.projectjsf.entities.AppUserPsw[ id=" + id + " ]";
    }

}
