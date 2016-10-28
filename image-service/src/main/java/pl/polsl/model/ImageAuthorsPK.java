package pl.polsl.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Mateusz on 18.10.2016.
 */
public class ImageAuthorsPK implements Serializable {
    private Long imageId;
    private Long authorId;

    @Id
    @Column(name = "image_id", nullable = false)
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Id
    @Column(name = "author_id", nullable = false)
    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public ImageAuthorsPK(Long imageId, Long authorId) {
        this.imageId = imageId;
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageAuthorsPK that = (ImageAuthorsPK) o;

        if (!imageId.equals(that.imageId)) return false;
        return authorId.equals(that.authorId);

    }

    @Override
    public int hashCode() {
        int result = imageId.hashCode();
        result = 31 * result + authorId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ImageAuthorsPK{" +
                "imageId=" + imageId +
                ", authorId=" + authorId +
                '}';
    }
}
