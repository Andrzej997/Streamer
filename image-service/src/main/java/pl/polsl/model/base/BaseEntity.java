package pl.polsl.model.base;

import pl.polsl.model.UsersView;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Mateusz on 25.10.2016.
 */
@SuppressWarnings("EmptyMethod")
@MappedSuperclass
public abstract class BaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 123456789L;

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

    public Boolean containsUsers() {
        Field[] fields = this.getClass().getFields();
        if (fields == null) {
            return false;
        }
        for (Field field : fields) {
            if (field.getType() == null) {
                continue;
            }
            if (field.getType().equals(UsersView.class)) {
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

    public void injectUser(UsersView usersView) {
        if (usersView == null) {
            return;
        }
        if (this.containsUsers()) {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            if (declaredFields == null) {
                return;
            }
            for (Field field : declaredFields) {
                if (!UsersView.class.equals(field.getType())) {
                    continue;
                }
                try {
                    field.set(this, usersView);
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
