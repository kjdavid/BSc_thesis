/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.bsc.thesis.repository;

import hu.elte.bsc.thesis.model.Item;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author kjdavid <kjdavid96 at gmail.com>
 */
public interface ItemRepository extends CrudRepository<Item, Long> {
    Iterable<Item> findAll();

    Optional<Item> findByBarcode(String barcode);

    @Query(value = "Select max(COALESCE(i.id,0)) from Item i")
    Optional<Long> findMaxId();
}
