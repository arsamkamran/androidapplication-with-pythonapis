package com.example.arsam.cloudcomputing;


/**
 * Created by Arsam on 17/01/2018.
 */

public class CartItemsModel {


    int id;
    String name;
    Float quantity;
    Float price;
    Float total;
    int minus;
    int plus;

    public CartItemsModel(int id, String name, Float quantity, Float price, Float total, int minus, int plus) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.minus = minus;
        this.plus = plus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMinus() {
        return minus;
    }

    public void setMinus(int minus) {
        this.minus = minus;
    }

    public int getPlus() {
        return plus;
    }

    public void setPlus(int plus) {
        this.plus = plus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

}
