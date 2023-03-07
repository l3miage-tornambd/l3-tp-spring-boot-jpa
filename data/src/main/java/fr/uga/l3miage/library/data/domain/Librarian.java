package fr.uga.l3miage.library.data.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@DiscriminatorValue("libra")
public class Librarian extends Person {


    @ManyToOne
    @OneToMany(mappedBy = "manager")
    private Librarian manager;

    public Librarian getManager() {
        return manager;
    }

    public void setManager(Librarian manager) {
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Librarian librarian)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(manager, librarian.manager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), manager);
    }
}
