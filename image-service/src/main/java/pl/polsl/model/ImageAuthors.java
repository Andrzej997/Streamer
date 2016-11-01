package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "image_authors", schema = "public")
@IdClass(ImageAuthorsPK.class)
public class ImageAuthors extends BaseEntity {

    @Id
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "image_id", referencedColumnName = "image_id")
    private Images imagesByImageId;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "author_id", referencedColumnName = "artist_id")
    private Artists artistsByAuthorId;

    public ImageAuthors() {
    }

    public ImageAuthors(Long imageId, Long authorId) {
        this.imageId = imageId;
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageAuthors that = (ImageAuthors) o;

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
        return "ImageAuthors{" +
                "imageId=" + imageId +
                ", authorId=" + authorId +
                '}';
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Images getImagesByImageId() {
        return imagesByImageId;
    }

    public void setImagesByImageId(Images imagesByImageId) {
        this.imagesByImageId = imagesByImageId;
    }

    public Artists getArtistsByAuthorId() {
        return artistsByAuthorId;
    }

    public void setArtistsByAuthorId(Artists artistsByAuthorId) {
        this.artistsByAuthorId = artistsByAuthorId;
    }
}
