package hu.elte.bsc.thesis.repository;

import hu.elte.bsc.thesis.model.Store;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StoreRepository extends CrudRepository<Store, Long> {
    @Query(value = "Select max(COALESCE(s.id,0)) from Store s")
    Optional<Long> findMaxId();

    Iterable<Store> findAll();

    Optional<Store> findByStoreName(String storeName);
}
