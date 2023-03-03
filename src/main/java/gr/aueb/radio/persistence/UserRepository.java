package gr.aueb.radio.persistence;

import gr.aueb.radio.domains.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;

@RequestScoped
public class UserRepository implements PanacheRepositoryBase<User, Integer> {
    public User findByUsername(String username) {
        PanacheQuery<User> query = find("select u from User u where u.username = :username", Parameters.with("username", username).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }

    public User findByEmail(String email) {
        PanacheQuery<User> query = find("select u from User u where u.email = :email", Parameters.with("email", email).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }

    }
}
