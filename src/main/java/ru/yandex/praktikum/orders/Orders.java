package ru.yandex.praktikum.orders;

import java.util.List;

public class Orders {
    private List<String> ingredients;

    public Orders(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Orders() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}
