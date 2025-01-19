package org.GameExchange.ExchangeAPI.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonJpaRepository extends JpaRepository<Person, Integer>{

    @Query("SELECT CASE WHEN COUNT(P) > 0 THEN TRUE ELSE FALSE END FROM People P WHERE P.emailAddr = :emailAddr")
    boolean checkUserExist(@Param("emailAddr") String emailAddr);
    
}
