package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "albums_images", schema = "public")
@IdClass(AlbumsImagesPK.class)
public class AlbumsImages extends BaseEntity {

    @Id
    @Column(name = "album_id", nullable = false)
    private Long albumId;

    @Id
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Basic
    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "album_id", nullable = false, insertable = false, updatable = false)
    private Albums albumsByAlbumId;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "image_id", nullable = false, insertable = false, updatable = false)
    private Images imagesByImageId;

    public AlbumsImages() {
    }

    public AlbumsImages(Long albumId, Long imageId) {
        this.albumId = albumId;
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumsImages that = (AlbumsImages) o;

        if (!albumId.equals(that.albumId)) return false;
        if (!imageId.equals(that.imageId)) return false;
        return orderNumber.equals(that.orderNumber);

    }

    @Override
    public int hashCode() {
        int result = albumId.hashCode();
        result = 31 * result + imageId.hashCode();
        result = 31 * result + orderNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AlbumsImages{" +
                "albumId=" + albumId +
                ", imageId=" + imageId +
                ", orderNumber=" + orderNumber +
                '}';
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Albums getAlbumsByAlbumId() {
        return albumsByAlbumId;
    }

    public void setAlbumsByAlbumId(Albums albumsByAlbumId) {
        this.albumsByAlbumId = albumsByAlbumId;
    }

    public Images getImagesByImageId() {
        return imagesByImageId;
    }

    public void setImagesByImageId(Images imagesByImageId) {
        this.imagesByImageId = imagesByImageId;
    }
}
