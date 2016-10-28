package pl.polsl.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Mateusz on 18.10.2016.
 */
public class EbookAuthorsPK implements Serializable {

    @Id
    @Column(name = "ebook_id", nullable = false)
    private Long ebookId;

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    public EbookAuthorsPK() {
    }

    public EbookAuthorsPK(Long ebookId, Long authorId) {
        this.ebookId = ebookId;
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EbookAuthorsPK that = (EbookAuthorsPK) o;

        if (!ebookId.equals(that.ebookId)) return false;
        return authorId.equals(that.authorId);

    }

    @Override
    public int hashCode() {
        int result = ebookId.hashCode();
        result = 31 * result + authorId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EbookAuthorsPK{" +
                "ebookId=" + ebookId +
                ", authorId=" + authorId +
                '}';
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

}
