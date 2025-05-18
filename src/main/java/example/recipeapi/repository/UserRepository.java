package example.recipeapi.repository;

import example.recipeapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    public boolean existsByUsername(String username);
}
