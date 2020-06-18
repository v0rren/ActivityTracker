package it.uniroma3.siw.taskmanager.repository;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface is a CrudRepository for repository operations on Credentials.
 *
 * @see Credentials
 */
@Repository
public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    /**
     * Retrieve Credentials by its username
     * @param username the username of the Credentials to retrieve
     * @return an Optional for the Credentials with the passed username
     */
    public Optional<Credentials> findByUserName(String username);
    
    public Optional<Credentials> findByUser(User user);
}



