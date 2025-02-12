package org.GameExchange.ExchangeAPI.Model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressJpaRepository extends JpaRepository<Address, Integer>{
    // @Query("SELECT CASE WHEN COUNT(A) > 0 THEN TRUE ELSE FALSE END FROM Addresses A WHERE addressId = :addressId")
    // boolean checkAddressExists(@Param("addressId") int addressId);

    @Query("""
        SELECT A FROM Addresses A
            CROSS JOIN Cities C
            CROSS JOIN States S 
            CROSS JOIN Zips Z 
            WHERE A.addressLine1 LIKE :addrLn1 AND
                A.addressLine2 LIKE :addrLn2 AND
                C.cityName LIKE :cityName AND
                S.stateAbbr LIKE :stateAbbr AND
                Z.zipCode LIKE :zipCode
        """)
    List<Address> getAddress(@Param("addrLn1")String addrLn1, @Param("addrLn2")String addrLn2, @Param("cityName") String cityName, @Param("stateAbbr") String stateAbbr, @Param("zipCode")String zipCode);


    

@Query("""
        SELECT A FROM Addresses A
            CROSS JOIN Cities C
            CROSS JOIN States S 
            CROSS JOIN Zips Z 
            WHERE A.addressLine1 LIKE :addrLn1 AND
                C.cityName LIKE :cityName AND
                S.stateAbbr LIKE :stateAbbr AND
                Z.zipCode LIKE :zipCode
        """)
    List<Address> getAddress(@Param("addrLn1")String addrLn1, @Param("cityName") String cityName, @Param("stateAbbr") String stateAbbr, @Param("zipCode")String zipCode);

}

