package com.tiago.m295_lb.repositories;

import com.tiago.m295_lb.models.Keeper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IKeeperRepository extends JpaRepository<Keeper, Integer> {
}
