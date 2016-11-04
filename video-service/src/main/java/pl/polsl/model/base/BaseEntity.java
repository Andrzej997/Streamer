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

    public Boolean containsUsers() {
        Field[] fields = this.getClass().getFields();
        if (fields == null) {
            return false;
        }
        for (Field field : fields) {
            if (field.getType() == null) {
                continue;
            }
            if (field.getType().equals(Users.class)) {
                return true;
            }
        }
        return false;
    }

    public Long getUsersId() {
        if (this.containsUsers()) {
            Field f = null;
            try {
                f = this.getClass().getField("userId");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if (f == null) {
                return null;
            }
            try {
                return f.getLong(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void injectUser(Users users) {
        if (users == null) {
            return;
        }
        if (this.containsUsers()) {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            if (declaredFields == null) {
                return;
            }
            for (Field field : declaredFields) {
                if (!Users.class.equals(field.getType())) {
                    continue;
                }
                try {
                    field.set(this, users);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
