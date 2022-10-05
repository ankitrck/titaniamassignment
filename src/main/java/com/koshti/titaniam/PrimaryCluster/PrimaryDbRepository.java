package com.koshti.titaniam.PrimaryCluster;

import com.koshti.titaniam.models.Entities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PrimaryDbRepository extends JpaRepository<Entities, List<Entities>>{

    @Query("select e from Entities e")
    List<Entities> getAll();

    @Query("select e from Entities e where e.id like ?1")
    Entities getOne(int id);

    @Query(value = "insert into Entities (name) values (:name)",nativeQuery = true)
    @Transactional
    @Modifying
    void addOne(@Param("name") String name);
}
