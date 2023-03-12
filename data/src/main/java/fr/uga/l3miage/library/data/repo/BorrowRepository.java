package fr.uga.l3miage.library.data.repo;

import fr.uga.l3miage.library.data.domain.Borrow;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.text.SimpleDateFormat ;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar ;
import java.util.Date ;

import java.util.List;

@Repository
public class BorrowRepository implements CRUDRepository<String, Borrow> {

    private final EntityManager entityManager;

    @Autowired
    public BorrowRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Borrow save(Borrow entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Borrow get(String id) {
        return entityManager.find(Borrow.class, id);
    }

    @Override
    public void delete(Borrow entity) {
        entityManager.remove(entity);
    }

    @Override
    public List<Borrow> all() {
        return entityManager.createQuery("from Borrow", Borrow.class).getResultList();
    }

    /**
     * Trouver des emprunts en cours pour un emprunteur donné
     *
     * @param userId l'id de l'emprunteur
     * @return la liste des emprunts en cours
     */
    public List<Borrow> findInProgressByUser(String userId) {
        String jpql = "select b from Borrow b where b.borrower.id = ?1 and b.finished = false";
        List<Borrow> res = entityManager.createQuery(jpql, Borrow.class)
                .setParameter(1, userId)
                .getResultList();
        return res;
    }

    /**
     * Compte le nombre total de livres emprunté par un utilisateur.
     *
     * @param userId l'id de l'emprunteur
     * @return le nombre de livre
     */
    public int countBorrowedBooksByUser(String userId) {
        String jpql = "select b from Borrow b where b.borrower.id = ?1"; // requête équivalente: "select b from Borrow b join b.borrower u where u.id = ?1";
        List<Borrow> res = entityManager.createQuery(jpql, Borrow.class)
                .setParameter(1, userId)
                .getResultList();
        return res.size();
    }

    /**
     * Compte le nombre total de livres non rendu par un utilisateur.
     *
     * @param userId l'id de l'emprunteur
     * @return le nombre de livre
     */
    public int countCurrentBorrowedBooksByUser(String userId) {
        String jpql = "select bs from Borrow bw join bw.books bs where bw.borrower.id = ?1 and bw.finished = false";
        List<Borrow> res = entityManager.createQuery(jpql, Borrow.class)
                .setParameter(1, userId)
                .getResultList();
        return res.size();
    }

    // On choisit ici de trié du plus grand retard au plus petit
    /**
     * Recherche tous les emprunt en retard trié
     *
     * @return la liste des emprunt en retard
     */
    public List<Borrow> foundAllLateBorrow() {
        Date d = Date.from(ZonedDateTime.now().toInstant());
        String jpql = "select b from Borrow b where b.requestedReturn < ?1 order by b.requestedReturn";
        List<Borrow> res = entityManager.createQuery(jpql, Borrow.class)
                .setParameter(1, d)
                .getResultList();
        return res;
    }

    /**
     * Calcul les emprunts qui seront en retard entre maintenant et x jours.
     *
     * @param days le nombre de jour avant que l'emprunt soit en retard
     * @return les emprunt qui sont bientôt en retard
     */
    public List<Borrow> findAllBorrowThatWillLateWithin(int days) {
        // creating the present date in java
        Date d = Date.from(ZonedDateTime.now().plus(days, ChronoUnit.DAYS).toInstant());
        String jpql = "select b from Borrow b where b.requestedReturn < ?1"; // requête équivalente: "select b from Borrow b join b.borrower u where u.id = ?1";
        List<Borrow> res = entityManager.createQuery(jpql, Borrow.class)
                .setParameter(1, d)
                .getResultList();
        return res;
    }

}
