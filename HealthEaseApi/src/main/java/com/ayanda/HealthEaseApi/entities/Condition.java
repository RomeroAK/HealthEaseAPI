package com.ayanda.HealthEaseApi.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "conditions")
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conditionId;
    private String name;
    private String startDate;
    private String notes;
    private boolean isChronic;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getConditionId() {
        return conditionId;
    }

    public void setConditionId(Long conditionId) {
        this.conditionId = conditionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isChronic() {
        return isChronic;
    }

    public void setChronic(boolean chronic) {
        isChronic = chronic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
