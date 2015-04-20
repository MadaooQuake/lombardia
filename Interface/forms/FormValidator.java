/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Domek
 */
public class FormValidator {

    String pattern = null;
    Matcher matcher = null;
    Pattern replace = null;
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");
    // what alidate : items, elements, addresses

    /**
     *
     * @param lenghtName
     * @param name
     * @return
     */
    public boolean checkName(int lenghtName, String name) {
        if (lenghtName > 0) {
            pattern = "[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]{2,30}";
            replace = Pattern.compile(pattern);
            matcher = replace.matcher(name);
            if (matcher.matches() == false) {
                JOptionPane.showMessageDialog(null,
                        "Podane imię jest niepoprawne",
                        "Nieprawidłowa warotśc!",
                        JOptionPane.ERROR_MESSAGE);
            }
            return matcher.matches();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Pole z imieniem posiada nieprawidłowe"
                    + " wartości lub jest puste! ",
                    "Nieprawidłowa warotśc!",
                    JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     *
     * @param lenghtSurmame
     * @param surmame
     * @return
     */
    public boolean checkSurename(int lenghtSurmame, String surmame) {
        if (lenghtSurmame > 0) {
            pattern = "[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\-]{2,50}";
            replace = Pattern.compile(pattern);
            matcher = replace.matcher(surmame);
            if (matcher.matches() == false) {
                JOptionPane.showMessageDialog(null,
                        "Podane nazwisko jest niepoprawne",
                        "Nieprawidłowa warotśc!",
                        JOptionPane.ERROR_MESSAGE);
            }
            return matcher.matches();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Pole z nazwiskiem posiada nieprawidłowe"
                    + " wartości lub jest puste! ",
                    "Nieprawidłowa warotśc!",
                    JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     *
     * @param getLenghtPesel
     * @param pesel
     * @return
     */
    public boolean checkPesel(int getLenghtPesel, String pesel) {
        if (getLenghtPesel == 11) {
            pattern = "(\\d*)";
            replace = Pattern.compile(pattern);
            matcher = replace.matcher(pesel);
            if (matcher.matches() == false) {
                JOptionPane.showMessageDialog(null,
                        "Pole pesel jest puste lub jest błąd w numerze pesel",
                        "Nieprawidłowa warotśc!",
                        JOptionPane.ERROR_MESSAGE);

            }
            return matcher.matches();
        }
        return pesel.isEmpty();
    }

    /**
     *
     * @param lenghtPrice
     * @param price
     * @return
     */
    public boolean checkPrice(int lenghtPrice, String price) {
        if (lenghtPrice > 0) {
            pattern = "[(\\d*)[.]?\\d{1,2}]{1,8}";
            replace = Pattern.compile(pattern);
            matcher = replace.matcher(price);
            if (matcher.matches() == false) {
                JOptionPane.showMessageDialog(null,
                        "Podana kwota jest za duża lub jest niepoprawna.",
                        "Nieprawidłowa warotśc!",
                        JOptionPane.ERROR_MESSAGE);
            }
            return matcher.matches();
        }
        return false;
    }

    /**
     *
     * @param lengthWeigh
     * @param weight
     * @return
     */
    public boolean checkWeight(int lengthWeigh, String weight) {
        if (lengthWeigh > 0) {
            pattern = "(\\d*)[.]?\\d{1,3}";
            replace = Pattern.compile(pattern);
            matcher = replace.matcher(weight);
            if (matcher.matches() == false) {
                JOptionPane.showMessageDialog(null,
                        "Podana waga jest niepoprawna",
                        "Nieprawidłowa warotśc!",
                        JOptionPane.ERROR_MESSAGE);
            }
            return matcher.matches();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Pole z Wagą posiada nieprawidołową wartość lub jest puste."
                    + "System utworzy formularz nie uwzględniając tego pola.",
                    "Nieprawidłowa warotśc!",
                    JOptionPane.WARNING_MESSAGE);
        }
        return true;
    }

    /**
     *
     * @param IMEI
     * @return
     */
    public boolean checkIMEI(int IMEI) {
        return true;
    }

    /**
     *
     * @param lengthValue
     * @param value
     * @param flag
     * @return
     */
    public boolean checkValue(int lengthValue, String value, int... flag) {
        int flagValue = 0;
        if (flag.length > 0) {
            flagValue = flag[0];
        }
        if (lengthValue > 0) {
            pattern = "[(\\d*)[.]?\\d{1,2}]{1,8}";
            replace = Pattern.compile(pattern);
            matcher = replace.matcher(value);
            if (matcher.matches() == false) {
                JOptionPane.showMessageDialog(null,
                        "Podana kwota jest za duża.",
                        "Nieprawidłowa warotśc!",
                        JOptionPane.ERROR_MESSAGE);
            }
            return matcher.matches();
        } else if (flagValue != 1) {
            JOptionPane.showMessageDialog(null,
                    "Pole wartoś lub łączna wartość posiada nieprawidłowe"
                    + " wartości lub jest puste.",
                    "Nieprawidłowa warotśc!",
                    JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     *
     * @param Jawery
     * @return
     */
    public boolean checkJaweryElements(String Jawery) {
        try {
            Integer.parseInt(Jawery);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Pola z ilością wyrobów jubilerskich są nieprawidłowe",
                    "Nieprawidłowa warotśc!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Data checker
     *
     * @param curretDate
     * @param selectedDate
     * @return
     */
    public boolean checkCreditFinishDate(Date curretDate, Date selectedDate) {
        String lastDate = null;
        String currentDate = null;
        if (curretDate != null && selectedDate != null) {
            lastDate = ft.format(selectedDate);
            currentDate = ft.format(curretDate);
            currentDate = currentDate.substring(0, currentDate.lastIndexOf(" "));
            lastDate = lastDate.substring(0, lastDate.lastIndexOf(" "));
            // check years
            int start = Integer.parseInt(
                    currentDate.substring(6, currentDate.length()));
            int stop = Integer.parseInt(
                    lastDate.substring(6, lastDate.length()));

            if (stop >= start) {
                // check month
                start = Integer.parseInt(currentDate.substring(3, 5));
                stop = Integer.parseInt(lastDate.substring(3, 5));

                if (stop == start) {
                    //check days 
                    start = Integer.parseInt(currentDate.substring(0, 2));
                    stop = Integer.parseInt(lastDate.substring(0, 2));
                    if (stop >= start) {
                        return true;
                    }
                } else if (stop > start) {
                    return true;
                }
            }

            JOptionPane.showMessageDialog(null,
                    "Nie można utworzyć umowy z datą zakonćzenia w przeszłości",
                    "Nieprawidłowa warotść!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return false;
    }
    
    /**
     * checkin phone number in three steps
     * 1. check if contains +48 (if not add as default +48) 
     * 2. chek length phone number (nine signs + three)
     * 3. return 
     */
    
    /**
     * check country code
     */
    public String checkCountryCode(String number) {
        String addCountryCode = null;
        if(number.startsWith("+")) {
            addCountryCode = number;
        } else {
            addCountryCode = "+48" + number;
        }
        
        return addCountryCode;
    }
    
    /**
     * check lenght 
     */
    public boolean checkLenghtnumber(String number) {
        return (number.length() == 11);
    }
    
}
