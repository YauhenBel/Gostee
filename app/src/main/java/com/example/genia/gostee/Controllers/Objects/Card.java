package com.example.genia.gostee.Controllers.Objects;

public class Card {
    private String
            card_id = null,
            description = null,
            working_hours = null,
            working_days = null,
            name = null,
            individual_icon = null;


    private Integer
            circle_number = null,
            type = null,
            count = null;


    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndividual_icon() {
        return individual_icon;
    }

    public void setIndividual_icon(String individual_icon) {
        this.individual_icon = individual_icon;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(String working_hours) {
        this.working_hours = working_hours;
    }

    public String getWorking_days() {
        return working_days;
    }

    public void setWorking_days(String working_days) {
        this.working_days = working_days;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCircle_number() {
        return circle_number;
    }

    public void setCircle_number(Integer circle_number) {
        this.circle_number = circle_number;
    }
}
