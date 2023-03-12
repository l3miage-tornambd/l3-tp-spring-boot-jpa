package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Author;
import fr.uga.l3miage.library.data.domain.Book;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepository implements CRUDRepository<Long, Author> {

    private final EntityManager entityManager;

    @Autowired
    public AuthorRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Author save(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public Author get(Long id) {
        return entityManager.find(Author.class, id);
    }


    @Override
    public void delete(Author author) {
        entityManager.remove(author);
    }

    /**
     * Renvoie tous les auteurs
     *
     * @return une liste d'auteurs trié par nom
     */
    @Override
    public List<Author> all() {
        String jpql = "from Author order by fullName";
        List<Author> res = entityManager.createQuery(jpql, Author.class).getResultList();
        return res;
    }

    /**
     * Recherche un auteur par nom (ou partie du nom) de façon insensible  à la casse.
     *
     * @param namePart tout ou partie du nom de l'auteur
     * @return une liste d'auteurs trié par nom
     */
    public List<Author> searchByName(String namePart) {
        String jpql = "from Author a where lower(a.fullName) like lower(concat('%',?1,'%'))";
        List<Author> res = entityManager.createQuery(jpql, Author.class)
                .setParameter(1,namePart)
                .getResultList();
        return res;
    }

    /**
     * Recherche si l'auteur a au moins un livre co-écrit avec un autre auteur
     *
     * @return true si l'auteur partage
     */
    public boolean checkAuthorByIdHavingCoAuthoredBooks(long authorId) {
        //2 versions qui marchent:
        //String jpql = "select a from Author a join a.books b join b.authors ba where a.id = ?1 and size(ba) > 1";
        String jpql = "select b from Book b join b.authors a where a.id = ?1 and size(a) > 1 ";
        List<Author> authors = entityManager.createQuery(jpql, Author.class)
                .setParameter(1, authorId).getResultList();
       return authors.size() > 0;
    }

}
