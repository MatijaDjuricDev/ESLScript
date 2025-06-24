package rs.netplanet.Models;

import org.json.JSONObject;

public class ItemModel {
    String itemName,
            itemCode,
            code,
            special,
            price,
            unitPrice,
            quantityUnit,
            itemPackage,
            stock,
            category,
            cenovnik,
            vaziOd,
            vaziDo,
            staraCena;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getItemPackage() {
        return itemPackage;
    }

    public void setItemPackage(String itemPackage) {
        this.itemPackage = itemPackage;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCenovnik() {
        return cenovnik;
    }

    public void setCenovnik(String cenovnik) {
        this.cenovnik = cenovnik;
    }

    public String getVaziOd() {
        return vaziOd;
    }

    public void setVaziOd(String vaziOd) {
        this.vaziOd = vaziOd;
    }

    public String getVaziDo() {
        return vaziDo;
    }

    public void setVaziDo(String vaziDo) {
        this.vaziDo = vaziDo;
    }

    public String getStaraCena() {
        return staraCena;
    }

    public void setStaraCena(String staraCena) {
        this.staraCena = staraCena;
    }

    public JSONObject parseToJSON() {
        JSONObject jsonItem = new JSONObject();

        jsonItem.put("item_name", itemName);
        jsonItem.put("item_code", itemCode);
        jsonItem.put("Code", code);
        jsonItem.put("Special", special);
        jsonItem.put("Price", price);
        jsonItem.put("UnitPrice", unitPrice);
        jsonItem.put("QuantityUnit", quantityUnit);
        jsonItem.put("Package", itemPackage);
        jsonItem.put("Stock", stock);
        jsonItem.put("Category", category);
        jsonItem.put("Cenovnik", cenovnik);
        jsonItem.put("Vazi_od", vaziOd);
        jsonItem.put("Vazi_do", vaziDo);
        jsonItem.put("StaraCena", staraCena);

        return jsonItem;
    }

    @Override
    public String toString() {
        return itemName + " " +
                itemCode + " " +
                code + " " +
                special + " " +
                price + " " +
                unitPrice + " " +
                quantityUnit + " " +
                itemPackage + " " +
                stock + " " +
                category + " " +
                cenovnik + " " +
                vaziOd + " " +
                vaziDo + " " +
                staraCena;
    }

}
