package pl.polsl.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Mateusz on 18.10.2016.
 */
public class AlbumsImagesPK implements Serializable {

    @Id
    @Column(name = "album_id", nullable = false)
    private Long albumId;

    @Id
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumsImagesPK that = (AlbumsImagesPK) o;

        if (!albumId.equals(that.albumId)) return false;
        return imageId.equals(that.imageId);

    }

    @Override
    public int hashCode() {
        int result = albumId.hashCode();
        result = 31 * result + imageId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AlbumsImagesPK{" +
                "albumId=" + albumId +
                ", imageId=" + imageId +
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

}
