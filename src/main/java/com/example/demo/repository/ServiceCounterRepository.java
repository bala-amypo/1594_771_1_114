package com.example.demo.reporitory;

public class ServiceCounterRepository{
    extends JpaRepository<ServiceCounter, Long> {

    List<ServiceCounter> findByIsActiveTrue();
}
}
CREATE TABLE service_counter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    counter_name VARCHAR(100),
    department VARCHAR(100),
    is_active BOOLEAN
);
