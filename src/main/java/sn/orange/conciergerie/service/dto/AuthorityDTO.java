package sn.orange.conciergerie.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO de l'entité Authority (rôle), utilisé par la relation Profil -> Authority.
 */
public class AuthorityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorityDTO)) {
            return false;
        }
        return Objects.equals(name, ((AuthorityDTO) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "AuthorityDTO{name='" + name + "'}";
    }
}