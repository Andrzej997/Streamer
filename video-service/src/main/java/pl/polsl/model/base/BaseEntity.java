package pl.polsl.model.base;

import pl.polsl.model.Users;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Mateusz on 25.10.2016.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 123456789l;

    @Transient
    private Long version;

    @PreUpdate
    protected void preUpdate() {

    }

    @PrePersist
    protected void prePersist() {

    }

    @PreRemove
    protected void preRemove() {

    }

    @PostPersist
    protected void postPersist() {

    }

    @PostUpdate
    protected void postUpdate() {

    }

    @PostRemove
    protected void postRemove() {

    }

    @PostLoad
    protected void postLoad() {

    }

    private Boolean containsUsers() {
        Field[] fields = this.getClass().getFields();
        if (fields == null) {
            return false;
        }
        for (Field field : fields) {
            if (field.getClass() == null) {
                continue;
            }
            if (field.getClass().equals(Users.class)) {
                return true;
            }
        }
        return false;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
