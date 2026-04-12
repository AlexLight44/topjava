package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class MealTo {

    private Integer id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public MealTo(Integer id, boolean excess, String description, LocalDateTime dateTime, int calories) {
        this.calories = calories;
        this.excess = excess;
        this.description = description;
        this.dateTime = dateTime;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MealTo mealTo = (MealTo) o;
        return Objects.equals(id, mealTo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
