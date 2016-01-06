package com.example.siren4price.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;

import com.example.siren4price.R;
import com.example.siren4price.manager.ItemManager;
import com.example.siren4price.value.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnCheckedChangeListener{
    private EditText editSearch;
    private Button buttonSearch,buttonClear;
    private RadioButton radioBuy,radioSell;
    private CheckBox checkBangle,checkLeaf,checkScroll,checkRod,checkPot,checkAll;
    private CheckBox[] checkCategories = new CheckBox[5];
    private ItemManager itemManager;
    private ItemListAdapter itemListAdapter;
    private boolean searching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemManager = new ItemManager(this);

        editSearch = (EditText) findViewById(R.id.text_price);
        buttonSearch = (Button) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        buttonClear = (Button) findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClear();
            }
        });

        ArrayList<Item> allItemList = new ArrayList<>();
        itemManager.loadAllItem(allItemList);

        ListView lv = (ListView) findViewById(R.id.list_items);
        itemListAdapter = new ItemListAdapter(this, allItemList);
        lv.setAdapter(itemListAdapter);

        radioBuy = (RadioButton) findViewById(R.id.radio_buy);
        radioSell = (RadioButton) findViewById(R.id.radio_sell);

        checkBangle = (CheckBox) findViewById(R.id.checkbox_bangle);
        checkBangle.setOnCheckedChangeListener(this);
        checkCategories[Item.BANGLE] = checkBangle;

        checkLeaf = (CheckBox) findViewById(R.id.checkbox_leaf);
        checkLeaf.setOnCheckedChangeListener(this);
        checkCategories[Item.LEAF] = checkLeaf;

        checkScroll = (CheckBox) findViewById(R.id.checkbox_scroll);
        checkScroll.setOnCheckedChangeListener(this);
        checkCategories[Item.SCROLL] = checkScroll;

        checkRod = (CheckBox) findViewById(R.id.checkbox_rod);
        checkRod.setOnCheckedChangeListener(this);
        checkCategories[Item.ROD] = checkRod;

        checkPot = (CheckBox) findViewById(R.id.checkbox_pot);
        checkPot.setOnCheckedChangeListener(this);
        checkCategories[Item.POT] = checkPot;

        checkAll = (CheckBox) findViewById(R.id.checkbox_all);
        checkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.getText().equals("全部")){
                    for(CheckBox b: checkCategories){
                        b.setChecked(true);
                    }
                    checkAll.setText("クリア");
                }else{
                    for(CheckBox b:checkCategories){
                        b.setChecked(false);
                    }
                    checkAll.setText("全部");
                }
                checkAll.setChecked(false);
                createItemList();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked) {
            if (!checkAll.isChecked()) {
                checkAll.setText("全部");
            }
        }
        createItemList();
    }

    private void createItemList(){
        ArrayList<Item> list = new ArrayList<>();
        for(int i = 0; i < checkCategories.length; i++){
            if (checkCategories[i].isChecked()) {
                if(searching){
                    itemManager.loadItemsFromCategoryAndPrice(i, Integer.parseInt(editSearch.getText().toString()),radioBuy.isChecked(),list);
                }else {
                    itemManager.loadItemsFromCategory(i, list);
                }
            }
        }
        itemListAdapter.setItemList(list);
        itemListAdapter.notifyDataSetChanged();
    }

    private void search(){
        searching = true;
        createItemList();
    }

    private void searchClear(){
        editSearch.setText("");
        searching = false;
        createItemList();

    }
}
