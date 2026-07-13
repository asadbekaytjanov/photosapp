package uz.aytjanov.googlephotosclone.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.aytjanov.googlephotosclone.entity.Photo;

@Repository
public interface PhotosRepository extends JpaRepository<Photo, Integer> {
    Iterable<Photo> findByUserId(Long userId);
    Photo findById(Long id);
    void removeById(Long id);
}