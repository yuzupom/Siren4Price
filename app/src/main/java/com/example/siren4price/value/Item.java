package com.example.siren4price.value;

/**
 * Created by k_asano on 16/01/06.
 */
public class Item {
    public static final int BANGLE = 0;
    public static final int LEAF = 1;
    public static final int SCROLL = 2;
    public static final int ROD = 3;
    public static final int POT = 4;

    protected String name;
    protected int buy, sell, category;

    public Item(String name, int category, int buy, int sell){
        this.name = name;
        this.buy = buy;
        this.sell = sell;
        this.category = category;
    }

    public String getName(){
        return name;
    }

    public int getBuyPrice(){
        return buy;
    }

    public int getSellPrice(){
        return sell;
    }

    public int getItemCategory(){
        return category;
    }
}
