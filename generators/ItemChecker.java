/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.generators;

/**
 *
 * @author Domek
 */
public class ItemChecker {

    String prepareParameters = null, values = null, insertItem = null, updateItem = null;
    boolean update = false;
    /*
     full insert into
     queryResult = setQuerry.dbSetQuery("INSERT INTO Items (MODEL,"
     + " BAND, TYPE, WEIGHT, IMEI, VALUE, ATENCION,"
     + " ID_CATEGORY, ID_AGREEMENT) VALUES ('"
     + tmpItem.get("Model") + "','"
     + tmpItem.get("Marka") + "','" + tmpItem.get("Typ") + "','"
     + weight + "'," + tmpItem.get("IMEI") + ","
     + tmpItem.get("Wartość") + ",'" + tmpItem.get("Uwagi")
     + "'," + catID + "," + howMany + ");");
     */

    // check parameters
    // brand and model is required 
    // check BAND // brand :O
    // allway send
    public void setValues(String MODEL, String BAND, String TYPE, String WEIGHT,
            String IMEI, String VALUE, String ATENCION, int ID_CATEGORY,
            int ID_AGREEMENT) {

        // now time to action :)
        insertItem = "INSERT INTO Items (MODEL";
        // now i check what i have
        values = "'" + MODEL + "'";
        checkBrand(BAND);
        checkType(TYPE);
        checkWeight(WEIGHT);
        checkImei(IMEI);
        checkVaue(VALUE);
        checkAtencion(ATENCION);
        insertItem += ", ID_CATEGORY, ID_AGREEMENT) VALUES (" + values
                + "," + ID_CATEGORY + "," + ID_AGREEMENT + ");";
    }

    public void setValues(String MODEL, String BAND, String TYPE, String WEIGHT,
            String IMEI, String VALUE, String ATENCION, int ID_CATEGORY,
            int ID_AGREEMENT, String BUY_DATE) {

        // now time to action :)
        insertItem = "INSERT INTO Items (MODEL";
        // now i check what i have
        values = "'" + MODEL + "'";
        checkBrand(BAND);
        checkType(TYPE);
        checkWeight(WEIGHT);
        checkImei(IMEI);
        checkVaue(VALUE);
        checkAtencion(ATENCION);
        insertItem += ", ID_CATEGORY, ID_AGREEMENT, BUY_DATE) VALUES (" + values
                + "," + ID_CATEGORY + "," + ID_AGREEMENT + ",'" + BUY_DATE + "');";
    }

    private void checkBrand(String Brand_) {
        String brand = Brand_;
        if (brand != null) {
            if (brand.length() > 0) {
                if (update) {

                } else {
                    insertItem += ", BAND";
                    values += ",'" + brand + "'";
                }
            }
        }
    }

    private void checkType(String Type_) {
        String type = Type_;
        if (type != null) {
            if (type.length() > 0) {
                if (update) {
                    values += " , TYPE = '" + type + "' ";
                } else {
                    insertItem += ", TYPE";
                    values += ",'" + type + "'";
                }
            }
        }

    }

    private void checkWeight(String weight_) {
        String weight = weight_;
        if (weight != null) {
            if (weight_.length() > 0) {
                if (update) {
                    values += " , WEIGHT = " + weight + " ";
                } else {
                    insertItem += ", WEIGHT";
                    values += ", " + weight + " ";
                }
            }
        }
    }

    private void checkImei(String imei_) {
        String imei = imei_;
        if (imei != null) {
            if (imei.length() > 0) {
                if (update) {
                    values += " , IMEI = '" + imei + "'";
                } else {
                    insertItem += ", IMEI";
                    values += ", " + imei + " ";
                }
            }
        }
    }

    private void checkVaue(String value_) {
        String value = value_;
        if (value != null) {
            if (value.length() > 0) {
                if (update) {
                    values += " , VALUE = " + value + " ";
                } else {
                    insertItem += ", VALUE";
                    values += ", " + value + " ";
                }
            }
        }
    }

    private void checkAtencion(String atencion_) {
        String atencion = atencion_;
        if (atencion != null) {
            if (atencion.length() > 0) {
                if (update) {
                    values += " , ATENCION = '" + atencion + "'";
                } else {
                    insertItem += ", ATENCION";
                    values += ",'" + atencion + "'";
                }
            }
        }
    }

    public String getInsertItem() {
        return insertItem;
    }

    // save to db
    // create insert into
    // creaate update table
    public String updateItem(String MODEL, String BAND, String TYPE, String WEIGHT,
            String IMEI, String VALUE, String ATENCION, int ID) {
        update = true;
        updateItem = "UPDATE Items SET ";
        values = " MODEL = '" + MODEL + "' ";
        checkBrand(BAND);
        checkType(TYPE);
        checkWeight(WEIGHT);
        checkImei(IMEI);
        checkVaue(VALUE);
        checkAtencion(ATENCION);
        updateItem += values + "WHERE ID = " + ID + ";";
        // now i check all elements :)
        return updateItem;
    }

}
