package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "ebook_authors", schema = "public")
@IdClass(EbookAuthorsPK.class)
public class EbookAuthors extends BaseEntity {

    @Id
    @Column(name = "ebook_id", nullable = false)
    private Long ebookId;

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @ManyToOne
    @JoinColumn(name = "ebook_id", referencedColumnName = "ebook_id", nullable = false, insertable = false, updatable = false)
    private Ebook ebookByEbookId;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "writer_id", nullable = false, insertable = false, updatable = false)
    private Writers writersByAuthorId;

    public EbookAuthors() {
    }

    public EbookAuthors(Long ebookId, Long authorId) {
        this.ebookId = ebookId;
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EbookAuthors that = (EbookAuthors) o;

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
        return "EbookAuthors{" +
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

    public Ebook getEbookByEbookId() {
        return ebookByEbookId;
    }

    public void setEbookByEbookId(Ebook ebookByEbookId) {
        this.ebookByEbookId = ebookByEbookId;
    }

    public Writers getWritersByAuthorId() {
        return writersByAuthorId;
    }

    public void setWritersByAuthorId(Writers writersByAuthorId) {
        this.writersByAuthorId = writersByAuthorId;
    }
}
