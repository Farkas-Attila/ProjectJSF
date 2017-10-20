package com.farkas.projectjsf.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@NamedQueries({
    @NamedQuery(name = "Msg.findInMsgByUser",
            query = "SELECT m FROM Msg m WHERE m.touser = :toUser ORDER BY m.msgdate DESC"),
    @NamedQuery(name = "Msg.findOutMsgByUser",
            query = "SELECT m FROM Msg m WHERE m.fromuser = :fromUser ORDER BY m.msgdate DESC")
})
public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "FROMUSER_ID", nullable = false)
    private AppUser fromuser;

    @ManyToOne
    @JoinColumn(name = "TOUSER_ID", nullable = false)
    private AppUser touser;

    private String msgtext;

    @Temporal(TemporalType.TIMESTAMP)
    private Date msgdate;

    private boolean readflag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getFromuser() {
        return fromuser;
    }

    public void setFromuser(AppUser fromuser) {
        this.fromuser = fromuser;
    }

    public AppUser getTouser() {
        return touser;
    }

    public void setTouser(AppUser touser) {
        this.touser = touser;
    }

    public String getMsgtext() {
        return msgtext;
    }

    public void setMsgtext(String msgtext) {
        this.msgtext = msgtext;
    }

    public Date getMsgdate() {
        return msgdate;
    }

    public void setMsgdate(Date msgdate) {
        this.msgdate = msgdate;
    }

    public boolean isReadflag() {
        return readflag;
    }

    public void setReadflag(boolean readflag) {
        this.readflag = readflag;
    }
    
}
