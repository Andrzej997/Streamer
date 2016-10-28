package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "image_types", schema = "public")
public class ImageTypes extends BaseEntity {

    @Id
    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @OneToMany(mappedBy = "imageTypesByTypeId")
    private Collection<Images> imagesByTypeId;

    public ImageTypes() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageTypes that = (ImageTypes) o;

        if (!typeId.equals(that.typeId)) return false;
        if (!name.equals(that.name)) return false;
        return comments != null ? comments.equals(that.comments) : that.comments == null;

    }

    @Override
    public int hashCode() {
        int result = typeId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImageTypes{" +
                "typeId=" + typeId +
                ", name='" + name + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Collection<Images> getImagesByTypeId() {
        return imagesByTypeId;
    }

    public void setImagesByTypeId(Collection<Images> imagesByTypeId) {
        this.imagesByTypeId = imagesByTypeId;
    }
}
