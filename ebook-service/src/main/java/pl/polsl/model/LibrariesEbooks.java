package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "libraries_ebooks", schema = "public")
@IdClass(LibrariesEbooksPK.class)
public class LibrariesEbooks extends BaseEntity {

    @Id
    @Column(name = "library_id", nullable = false)
    private Long libraryId;

    @Id
    @Column(name = "ebook_id", nullable = false)
    private Long ebookId;

    @Basic
    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @ManyToOne
    @JoinColumn(name = "library_id", referencedColumnName = "library_id", nullable = false, insertable = false, updatable = false)
    private UsersLibraries usersLibrariesByLibraryId;

    @ManyToOne
    @JoinColumn(name = "ebook_id", referencedColumnName = "ebook_id", nullable = false, insertable = false, updatable = false)
    private Ebook ebookByEbookId;

    public LibrariesEbooks() {
    }

    public LibrariesEbooks(Long libraryId, Long ebookId) {
        this.libraryId = libraryId;
        this.ebookId = ebookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LibrariesEbooks that = (LibrariesEbooks) o;

        if (!libraryId.equals(that.libraryId)) return false;
        if (!ebookId.equals(that.ebookId)) return false;
        return orderNumber.equals(that.orderNumber);

    }

    @Override
    public int hashCode() {
        int result = libraryId.hashCode();
        result = 31 * result + ebookId.hashCode();
        result = 31 * result + orderNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LibrariesEbooks{" +
                "libraryId=" + libraryId +
                ", ebookId=" + ebookId +
                ", orderNumber=" + orderNumber +
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public UsersLibraries getUsersLibrariesByLibraryId() {
        return usersLibrariesByLibraryId;
    }

    public void setUsersLibrariesByLibraryId(UsersLibraries usersLibrariesByLibraryId) {
        this.usersLibrariesByLibraryId = usersLibrariesByLibraryId;
    }

    public Ebook getEbookByEbookId() {
        return ebookByEbookId;
    }

    public void setEbookByEbookId(Ebook ebookByEbookId) {
        this.ebookByEbookId = ebookByEbookId;
    }
}
