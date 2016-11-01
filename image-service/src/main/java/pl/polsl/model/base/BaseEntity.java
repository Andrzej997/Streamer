package pl.polsl.model.base;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Mateusz on 25.10.2016.
 */
@SuppressWarnings("EmptyMethod")
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
