package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonJpaRepository extends JpaRepository<Person, Integer>{

    @Query("SELECT CASE WHEN COUNT(P) > 0 THEN TRUE ELSE FALSE END FROM People P WHERE P.emailAddr = :emailAddr")
    boolean checkUserExist(@Param("emailAddr") String emailAddr);

    @Query("SELECT CASE WHEN COUNT(P) > 0 THEN TRUE ELSE FALSE END FROM People P WHERE P.emailAddr = :emailAddr AND P.password = :password")
    boolean checkCreds(@Param("emailAddr") String emailAddr, @Param("password") String password);

    @Query("SELECT P FROM People P WHERE P.emailAddr = :emailAddr AND P.password = :password")
    List<Person> getPersonByCreds(@Param("emailAddr") String emailAddr, @Param("password") String password);
    
}
