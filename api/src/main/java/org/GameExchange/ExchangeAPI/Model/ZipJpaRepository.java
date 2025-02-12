package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ZipJpaRepository extends JpaRepository<Zip, Integer>{
    @Query("SELECT Z FROM Zips Z WHERE zipCode = :zipCode")
    List<Zip> findByCode(@Param("zipCode") String zipoCode);
}
