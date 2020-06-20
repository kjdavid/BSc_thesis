package hu.elte.bsc.thesis.repository;

import hu.elte.bsc.thesis.model.StoreItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StoreItemRepository extends CrudRepository<StoreItem,Long> {
    @Query(value = "Select max(COALESCE(si.id,0)) from StoreItem si")
    Optional<Long> findMaxId();


}
