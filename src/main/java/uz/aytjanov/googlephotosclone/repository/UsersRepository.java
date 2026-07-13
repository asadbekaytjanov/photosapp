package uz.aytjanov.googlephotosclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.aytjanov.googlephotosclone.entity.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findById(Long id);
}