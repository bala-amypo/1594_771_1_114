package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class ServiceCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String counterName;
    private String department;
    private Boolean isActive;

    public ServiceCounter() {}

    public Long getId() { return id; }
    public String getCounterName() { return counterName; }
    public String getDepartment() { return department; }
    public Boolean getIsActive() { return isActive; }

    public void setId(Long id) { this.id = id; }
    public void setCounterName(String counterName) { this.counterName = counterName; }
    public void setDepartment(String department) { this.department = department; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
