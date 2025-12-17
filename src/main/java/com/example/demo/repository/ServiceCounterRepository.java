package com.example.demo.reporitory;

public class ServiceCounterRepository{
    extends JpaRepository<ServiceCounter, Long> {

    List<ServiceCounter> findByIsActiveTrue();
}
}