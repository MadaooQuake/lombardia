/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.generators;

/**
 *
 * @author jarek_000
 */
public class HeadersHelper {
    Header[] headers;
    int index;
    
    public HeadersHelper(int number_of_headers) {
        headers = new Header[number_of_headers];
        index = 0;
    }
    
    public int BuildHeader(String full_sql_name, String sql_name, String report_name, float width) {
        headers[index] = new Header(full_sql_name, sql_name, report_name, width);
        index ++;
        return index;
    }
    
    public float[] getHeadersWidth() {
        float[] result = new float[index];
        for (int i=0; i < index; i++) {
            result[i] = headers[i].GetWidth();
        }
        return result;
    }
    
    public String[] getHeaders() {
        
        String[] result = new String[index];
        
        for (int i = 0; i < index; i++) {
            result[i] = headers[i].GetColumnName();
        }

        return result;
    }
    
    public String[] getShortDBHeaders() {
        return getDbHeaders(2);
    }

    public String[] getDbHeaders() {
        return getDbHeaders(0);
    }    
        
    private String[] getDbHeaders(int variant) {
        // variant 0 - dbName as shortcat
        // variant 1 - only dbName
        // cariant 2 - only shortcat (dbName if shortcat not exists)
        String[] tmpArray = new String[index];
        String elem, shortcat, full;
        int emptySize = 0;
        
        for (int i = 0; i < index; i++) {
            elem = headers[i].GetSQLHeader();
            shortcat = headers[i].GetSQLShortHeader();
            if (elem.equals("")) {
                emptySize ++;
            }
            if (! shortcat.equals("")) {
                full = elem + " as " + shortcat;
            } else {
                shortcat = elem;
                full = elem;
            }
            switch (variant) {
                case 0:
                    tmpArray[i] = full;
                    break;
                case 1:
                    tmpArray[i] = elem;
                    break;
                case 2:
                    tmpArray[i] = shortcat; 
            }
        }
        
        String[] result = new String[index - emptySize];
        int magix = 0;
        for (int i = 0; i < index; i++) {
            if (!tmpArray[i].equals("")) {
                result[i - magix] = tmpArray[i];
            } else {
                magix ++;
            }
        }

        return result;        
    }
    
    
    private class Header {
        String full_sql_header;
        String short_sql_header;
        String report_column_name;
        Float column_width;
        
        public Header(  
                        String sql_header,
                        String sql_column_name,
                        String column_name, 
                        Float width) {
            full_sql_header = sql_header;
            short_sql_header = sql_column_name;
            report_column_name = column_name;
            column_width = width;
        }
        
        public String GetSQLHeader() {
            return full_sql_header;
        }
        
        public String GetSQLShortHeader() {
            return short_sql_header;
        }
        
        public String GetColumnName() {
            return report_column_name;
        }
        
        public Float GetWidth() {
            return column_width;
        }
    }
    
}
