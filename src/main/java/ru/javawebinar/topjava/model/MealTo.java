package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class MealTo {

    private int id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this(null, dateTime, description, calories, excess);
    }

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.calories = calories;
        this.excess = excess;
        this.description = description;
        this.dateTime = dateTime;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
