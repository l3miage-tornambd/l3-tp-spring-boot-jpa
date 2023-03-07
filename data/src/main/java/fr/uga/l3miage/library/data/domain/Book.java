package fr.uga.l3miage.library.data.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = "all-books", query = "from Book b order by b.title"),
        @NamedQuery(name = "find-books-by-title", query = "select b from Book b where lower(b.title) like lower(concat('%',?1,'%'))"),
        @NamedQuery(name = "find-books-by-author-and-title", query = "select b from Author a join a.books b where a.id = ?1 and lower(b.title) like lower(concat('%',?2,'%')) order by b.title"),
        @NamedQuery(name = "find-books-by-authors-name", query = "select b from Book b join b.authors a where lower(a.fullName) like lower(concat('%',?1,'%'))"),
        @NamedQuery(name = "find-books-by-several-authors", query = "from Book b where size(b.authors) > ?1")
})
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    private long isbn;

    private String publisher;

    @Column(name = "releasedate", nullable = false)
    private short year;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Language language;

    @ManyToMany(mappedBy = "books")
    private Set<Author> authors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        if (this.authors == null) {
            this.authors = new HashSet<>();
        }
        this.authors.add(author);
    }

    public enum Language {
        FRENCH,
        ENGLISH
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn == book.isbn && year == book.year && Objects.equals(title, book.title) && Objects.equals(publisher, book.publisher) && language == book.language && Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, isbn, publisher, year, language, authors);
    }
}
