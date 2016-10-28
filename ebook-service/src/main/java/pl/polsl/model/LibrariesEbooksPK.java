package pl.polsl.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Mateusz on 18.10.2016.
 */
public class LibrariesEbooksPK implements Serializable {

    @Id
    @Column(name = "library_id", nullable = false)
    private Long libraryId;

    @Id
    @Column(name = "ebook_id", nullable = false)
    private Long ebookId;

    public LibrariesEbooksPK() {
    }

    public LibrariesEbooksPK(Long libraryId, Long ebookId) {
        this.libraryId = libraryId;
        this.ebookId = ebookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LibrariesEbooksPK that = (LibrariesEbooksPK) o;

        if (!libraryId.equals(that.libraryId)) return false;
        return ebookId.equals(that.ebookId);

    }

    @Override
    public int hashCode() {
        int result = libraryId.hashCode();
        result = 31 * result + ebookId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LibrariesEbooksPK{" +
                "libraryId=" + libraryId +
                ", ebookId=" + ebookId +
                '}';
    }

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

}
