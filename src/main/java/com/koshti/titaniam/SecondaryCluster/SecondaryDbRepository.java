package com.koshti.titaniam.SecondaryCluster;


import com.koshti.titaniam.models.Entities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SecondaryDbRepository extends JpaRepository<Entities, List<Entities>> {
    @Query("select e from Entities e")
    List<Entities> getAll();

    @Query("select e from Entities e where e.id like ?1")
    Entities getOne(int id);

    @Query(value = "insert into Entities (name) values (:name)",nativeQuery = true)
    @Transactional
    @Modifying
    void addOne(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("Delete from Entities e")
    int truncateALL();

    @Query(value = "insert into Entities (id, name) values (:id, :name)",nativeQuery = true)
    @Transactional
    @Modifying
    void addAll(@Param("id") int id, @Param("name") String name);
}
