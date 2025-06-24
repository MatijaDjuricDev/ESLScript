package rs.netplanet.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.netplanet.Models.ItemModel;

public class FileParser {

    public Map<String, ItemModel> parseToItems(File file) {

        InputStreamReader inputStreamReader;
        Map<String, ItemModel> items = new HashMap<>();
        try {
            // create reader
            inputStreamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // first line because it is header and we don't need it
            String row = bufferedReader.readLine();

            // untill it goes to the end of the file
            while ((row = bufferedReader.readLine()) != null) {

                // split all rows by delimiter
                String[] values = row.split("\\|");

                ItemModel itemModel = new ItemModel();

                // put each value in ItemModel
                itemModel.setCode(values[0]);
                itemModel.setItemCode(values[1]);
                itemModel.setItemName(values[2]);
                itemModel.setSpecial(values[3]);
                itemModel.setPrice(values[4]);
                itemModel.setUnitPrice(values[5]);
                itemModel.setQuantityUnit(values[6]);
                itemModel.setItemPackage(values[7]);
                itemModel.setStock(values[8]);
                itemModel.setCategory(values[9]);
                itemModel.setCenovnik(values[10]);
                itemModel.setVaziOd(values[11]);
                itemModel.setVaziDo(values[12]);
                itemModel.setStaraCena(values[13]);

                // add item to map
                items.put(itemModel.getItemCode(), itemModel);
            }

            // close reader
            bufferedReader.close();

            // return models
            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ItemModel> parseToItemsList(File file) {

        InputStreamReader inputStreamReader;
        List<ItemModel> items = new ArrayList<>();
        try {
            // create reader
            inputStreamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // first line because it is header and we don't need it
            String row = bufferedReader.readLine();

            // untill it goes to the end of the file
            while ((row = bufferedReader.readLine()) != null) {

                // split all rows by delimiter
                String[] values = row.split("\\|");

                ItemModel itemModel = new ItemModel();

                // put each value in ItemModel
                itemModel.setCode(values[0]);
                itemModel.setItemCode(values[1]);
                itemModel.setItemName(values[2]);
                itemModel.setSpecial(values[3]);
                itemModel.setPrice(values[4]);
                itemModel.setUnitPrice(values[5]);
                itemModel.setQuantityUnit(values[6]);
                itemModel.setItemPackage(values[7]);
                itemModel.setStock(values[8]);
                itemModel.setCategory(values[9]);
                itemModel.setCenovnik(values[10]);
                itemModel.setVaziOd(values[11]);
                itemModel.setVaziDo(values[12]);
                itemModel.setStaraCena(values[13]);

                // add item to map
                items.add(itemModel);
            }

            // close reader
            bufferedReader.close();

            // return models
            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
