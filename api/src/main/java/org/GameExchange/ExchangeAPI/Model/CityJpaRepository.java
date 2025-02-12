package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CityJpaRepository extends JpaRepository<City, Integer>{

    @Query("SELECT C FROM Cities C WHERE cityName = :cityName")
    List<City> findByName(@Param("cityName") String cityName);
}