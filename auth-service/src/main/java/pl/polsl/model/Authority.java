package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Mateusz on 06.04.2017.
 */
@Entity
@Table(name = "AUTHORITY", schema = "users_schema")
public class Authority implements GrantedAuthority {

    @Id
    @Column(name = "authority_id", nullable = false)
    @GenericGenerator(
            name = "generator",
            strategy = "sequence-identity",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence",
                            value = "DEFAULTDBSEQ"
                    )

            })
    @GeneratedValue(generator = "generator")
    private Long authorityId;

    @Basic
    @Column(name = "authority", nullable = false)
    @NotNull
    private String authority;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id",
            nullable = false, insertable = false, updatable = false)
    private Users user;

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authorityId=" + authorityId +
                ", authority='" + authority + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority1 = (Authority) o;

        if (authorityId != null ? !authorityId.equals(authority1.authorityId) : authority1.authorityId != null)
            return false;
        if (authority != null ? !authority.equals(authority1.authority) : authority1.authority != null) return false;
        return userId != null ? userId.equals(authority1.userId) : authority1.userId == null;

    }

    @Override
    public int hashCode() {
        int result = authorityId != null ? authorityId.hashCode() : 0;
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
