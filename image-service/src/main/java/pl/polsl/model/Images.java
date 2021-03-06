package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "images", schema = "public")
public class Images extends BaseEntity {

    @Id
    @Column(name = "image_id", nullable = false)
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
    private Long imageId;

    @Basic
    @Column(name = "title", nullable = false, length = 1024)
    private String title;

    @Basic
    @Column(name = "image_file_id", nullable = false)
    private Long imageFileId;

    @Basic
    @Column(name = "native_width", nullable = false)
    private Integer nativeWidth;

    @Basic
    @Column(name = "native_height", nullable = false)
    private Integer nativeHeight;

    @Basic
    @Column(name = "resolution", nullable = false)
    private Integer resolution;

    @Basic
    @Column(name = "depth")
    private Short depth;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @Basic
    @Column(name = "rating")
    private Float rating;

    @Basic
    @Column(name = "type_id")
    private Long typeId;

    @Basic
    @Column(name = "year")
    private Short year;

    @Basic
    @Column(name = "owner_id")
    private Long ownerId;

    @Basic
    @Column(name = "rating_times")
    private Long ratingTimes;

    @OneToMany(mappedBy = "imagesByImageId", cascade = CascadeType.ALL)
    private Collection<AlbumsImages> albumsImagesByImageId;

    @OneToMany(mappedBy = "imagesByImageId", cascade = CascadeType.ALL)
    private Collection<ImageAuthors> imageAuthorsesByImageId;

    @ManyToOne
    @JoinColumn(name = "image_file_id", referencedColumnName = "image_file_id", nullable = false, insertable = false, updatable = false)
    private ImageFiles imageFilesByImageFileId;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "type_id", insertable = false, updatable = false)
    private ImageTypes imageTypesByTypeId;

    @Transient
    private UsersView usersViewByOwnerId;

    public Images() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Images images = (Images) o;

        if (!imageId.equals(images.imageId)) return false;
        if (!title.equals(images.title)) return false;
        if (!imageFileId.equals(images.imageFileId)) return false;
        if (!nativeWidth.equals(images.nativeWidth)) return false;
        if (!nativeHeight.equals(images.nativeHeight)) return false;
        if (!resolution.equals(images.resolution)) return false;
        if (depth != null ? !depth.equals(images.depth) : images.depth != null) return false;
        if (comments != null ? !comments.equals(images.comments) : images.comments != null) return false;
        if (rating != null ? !rating.equals(images.rating) : images.rating != null) return false;
        if (typeId != null ? !typeId.equals(images.typeId) : images.typeId != null) return false;
        if (year != null ? !year.equals(images.year) : images.year != null) return false;
        return ownerId != null ? ownerId.equals(images.ownerId) : images.ownerId == null;

    }

    @Override
    public int hashCode() {
        int result = imageId.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + imageFileId.hashCode();
        result = 31 * result + nativeWidth.hashCode();
        result = 31 * result + nativeHeight.hashCode();
        result = 31 * result + resolution.hashCode();
        result = 31 * result + (depth != null ? depth.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (typeId != null ? typeId.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Images{" +
                "imageId=" + imageId +
                ", title='" + title + '\'' +
                ", imageFileId=" + imageFileId +
                ", nativeWidth=" + nativeWidth +
                ", nativeHeight=" + nativeHeight +
                ", resolution=" + resolution +
                ", depth=" + depth +
                ", comments='" + comments + '\'' +
                ", rating=" + rating +
                ", typeId=" + typeId +
                ", year=" + year +
                ", ownerId=" + ownerId +
                '}';
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getImageFileId() {
        return imageFileId;
    }

    public void setImageFileId(Long imageFileId) {
        this.imageFileId = imageFileId;
    }

    public Integer getNativeWidth() {
        return nativeWidth;
    }

    public void setNativeWidth(Integer nativeWidth) {
        this.nativeWidth = nativeWidth;
    }

    public Integer getNativeHeight() {
        return nativeHeight;
    }

    public void setNativeHeight(Integer nativeHeight) {
        this.nativeHeight = nativeHeight;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public Short getDepth() {
        return depth;
    }

    public void setDepth(Short depth) {
        this.depth = depth;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    public Collection<AlbumsImages> getAlbumsImagesByImageId() {
        return albumsImagesByImageId;
    }

    public void setAlbumsImagesByImageId(Collection<AlbumsImages> albumsImagesByImageId) {
        this.albumsImagesByImageId = albumsImagesByImageId;
    }

    public Collection<ImageAuthors> getImageAuthorsesByImageId() {
        return imageAuthorsesByImageId;
    }

    public void setImageAuthorsesByImageId(Collection<ImageAuthors> imageAuthorsesByImageId) {
        this.imageAuthorsesByImageId = imageAuthorsesByImageId;
    }

    public ImageFiles getImageFilesByImageFileId() {
        return imageFilesByImageFileId;
    }

    public void setImageFilesByImageFileId(ImageFiles imageFilesByImageFileId) {
        this.imageFilesByImageFileId = imageFilesByImageFileId;
    }

    public ImageTypes getImageTypesByTypeId() {
        return imageTypesByTypeId;
    }

    public void setImageTypesByTypeId(ImageTypes imageTypesByTypeId) {
        this.imageTypesByTypeId = imageTypesByTypeId;
    }

    public UsersView getUsersViewByOwnerId() {
        return usersViewByOwnerId;
    }

    public void setUsersViewByOwnerId(UsersView usersViewByOwnerId) {
        this.usersViewByOwnerId = usersViewByOwnerId;
    }
}
