package com.example.siren4price.manager;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.siren4price.SQLite.DBOpenHelper;
import com.example.siren4price.value.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by k_asano on 16/01/06.
 */
public class ItemManager {
    private Context context;
    private static SQLiteDatabase db;
    private final String[] ALL_COLUMNS = {
                    DBOpenHelper.KEY_ID,
                    DBOpenHelper.KEY_CATEGORY,
                    DBOpenHelper.KEY_NAME,
                    DBOpenHelper.KEY_BUY
                };

    public ItemManager(Context c){
        context = c;
        DBOpenHelper helper = new DBOpenHelper(c);
        db = helper.getWritableDatabase();
    }

    public void loadAllItem(ArrayList<Item> list){
        Cursor c = db.query(DBOpenHelper.TABLE_ITEM, ALL_COLUMNS, null, null, null, null, "category ASC, id ASC");
        if(c.moveToFirst()){
            do{
                list.add(cursor2Item(c));
            }while(c.moveToNext());
        }else{
            createAllItems(list);
            ContentValues cv = new ContentValues();
            for(int i = 0; i < list.size(); i++){
                Item item = list.get(i);
                cv.put(DBOpenHelper.KEY_NAME, item.getName());
                cv.put(DBOpenHelper.KEY_CATEGORY, item.getItemCategory());
                cv.put(DBOpenHelper.KEY_BUY, item.getBuyPrice());
                db.insert(DBOpenHelper.TABLE_ITEM, null, cv);
            }
        }
        c.close();
    }

    public void loadItemsFromCategory(int category, ArrayList<Item> list){
        Cursor c = db.query(DBOpenHelper.TABLE_ITEM, ALL_COLUMNS, DBOpenHelper.KEY_CATEGORY + "=?", new String[]{"" + category},null,null,"id");
        if(c.moveToNext()){
            do{
                list.add(cursor2Item(c));
            }while(c.moveToNext());
        }
        c.close();
    }

    public void loadItemsFromCategoryAndPrice(int category, int price, boolean buyPrice, ArrayList<Item> list){
        ArrayList<Item> tmpList = new ArrayList<>();
        ArrayList<Item> addList = new ArrayList<>();

        loadItemsFromCategory(category, tmpList);
        for(int j = 0; j < tmpList.size(); j++){
            Item i = tmpList.get(j);

            System.out.println(category + ":" + i.getName());
            int iPrice;
            if(buyPrice){
                iPrice = i.getBuyPrice();
            }else{
                iPrice = i.getSellPrice();
            }

            switch(i.getItemCategory()) {
                case Item.BANGLE:
                case Item.LEAF:
                case Item.SCROLL:
                    if (iPrice != price) {
                        // 祝福
                        String name = null;
                        int buy = 0, sell = 0;
                        if (iPrice * 11 / 10 == price) {
                            name = "【祝】" + i.getName();
                            buy = i.getBuyPrice() * 11 / 10;
                            sell = i.getSellPrice() * 11 / 10;
                        }
                        // 呪い
                        else if (iPrice * 4 / 5 == price) {
                            name = "【呪】" + i.getName();
                            buy = i.getBuyPrice() * 4 / 5;
                            sell = i.getSellPrice() * 4 / 5;
                        }

                        if (name != null) {
                            addList.add(new Item(name, category, buy, sell));
                        }
                    }else{
                        addList.add(i);
                    }
                    break;
                case Item.ROD:
                case Item.POT:
                    double dPrice = iPrice;

                    String name = null;
                    int buy = 0, sell = 0;

                    for (int fix = 0; fix < 7; fix++) {
                        iPrice = (int) (dPrice * (1 + 0.05 * fix));
                        if (iPrice == price) {
                            name = i.getName();
                            buy = (int) (i.getBuyPrice() * (1 + 0.05 * fix));
                            sell = (int) (i.getSellPrice() * (1 + 0.05 * fix));
                        } else if (iPrice != price) {
                            // 祝福
                            if (iPrice * 11 / 10 == price) {
                                name = "【祝】" + i.getName();
                                buy = (int) (i.getBuyPrice() * (1 + 0.05 * fix) * 11 / 10);
                                sell = (int) (i.getSellPrice() * (1 + 0.05 * fix) * 11 / 10);
                            }
                            // 呪い
                            else if (iPrice * 4 / 5 == price) {
                                name = "【呪】" + i.getName();
                                buy = (int) (i.getBuyPrice() * (1 + 0.05 * fix) * 4 / 5);
                                sell = (int) (i.getSellPrice() * (1 + 0.05 * fix) * 4 / 5);
                            }
                        }

                        if (name != null) {
                            addList.add(new Item(name + "[" + fix + "]", category, buy, sell));
                            break;
                        }
                    }
                    break;
            }
        }

        list.addAll(addList);
    }

    private Item cursor2Item(Cursor c){
        int buy = c.getInt(c.getColumnIndex(DBOpenHelper.KEY_BUY));
        int sell = buy * 35/100;
        return new Item(
                        c.getString(c.getColumnIndex(DBOpenHelper.KEY_NAME)),
                        c.getInt(c.getColumnIndex(DBOpenHelper.KEY_CATEGORY)),
                        buy,
                        sell
                    );
    }
    private void createAllItems(ArrayList<Item> list){
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open("items.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String str;
            String[] strs;
            while((str = br.readLine()) != null){
                strs = str.split(",");
                list.add(new Item(
                        strs[1],
                        Integer.parseInt(strs[0]),
                        Integer.parseInt(strs[2]),
                        Integer.parseInt(strs[2]) * 35/100
                ));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
