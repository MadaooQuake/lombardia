package lombardia2014.generators;

import java.util.regex.Pattern;

/**
 *
 * @author Matsu
 */
public class PriceParser {

    int number;
    String słownie = "";
    String[] numberDouble = null;

    public PriceParser(String number) {
        // prepare number :)
        // check dot 

        numberDouble = number.split(Pattern.quote("."));

        słownie = translation(Integer.parseInt(numberDouble[0]));
        słownie += " złotych i ";
        if(numberDouble.length > 1) {
        słownie += translation(Integer.parseInt(numberDouble[1]));
        słownie += " groszy";
        } else {
            słownie += "zero groszy";
        }
    }

    public String translation(int number) {
        String słownie2 = "";
        String[] jedności = {"", "jeden ", "dwa ", "trzy ", "cztery ",
            "pięć ", "sześć ", "siedem ", "osiem ", "dziewięć "};

        String[] nastki = {"", "jedenaście ", "dwanaście ", "trzynaście ",
            "czternaście ", "piętnaście ", "szesnaście ", "siedemnaście ",
            "osiemnaście ", "dziewiętnaście "};

        String[] dziesiątki = {"", "dziesięć ", "dwadzieścia ",
            "trzydzieści ", "czterdzieści ", "pięćdziesiąt ",
            "sześćdziesiąt ", "siedemdziesiąt ", "osiemdziesiąt ",
            "dziewięćdziesiąt "};

        String[] setki = {"", "sto ", "dwieście ", "trzysta ", "czterysta ",
            "pięćset ", "sześćset ", "siedemset ", "osiemset ",
            "dziewięćset "};

        String[][] grupy = {{"", "", ""},
        {"tysiąc ", "tysięce ", "tysięcy "},
        {"milion ", "miliony ", "milionów "},
        {"miliard ", "miliardy ", "miliardów "},
        {"bilion ", "biliony ", "bilionów "},
        {"biliard ", "biliardy ", "biliardów "},
        {"trylion ", "tryliony ", "trylionów "}};

        // Initialize variables
        long j = 0/* jedności */, n = 0/* nastki */, d = 0/* dziesiątki */,
                s = 0/* setki */, g = 0/* grupy */, k = 0/* końcówwki */, p = 0 /*poprawka*/;
        String znak = "";

// OPERACJA DOTYCZąCA ZNAKU
        if (number < 0) {
            znak = "minus ";
            number = -number; // bezwględna wartość ponieważ, jeśli będziemy
// operować na liczbie z minusem tablica będzie
// przyjmowała wartości ujemne i zwróci nam błąd
        }
        if (number == 0) {
            znak = "zero";
        }

// PĘTLA GŁÓWNA
        while (number != 0) {
            s = number % 1000 / 100;
            d = number % 100 / 10;
            j = number % 10;

            if (d == 1 && j > 0) // if zajmujący się nastkami
            {
                n = j;
                d = 0;
                j = 0;
            } else {
                n = 0;
            }

// <---- KOŃCÓWKI
            if (j == 1 && s + d + n == 0) {
                k = 0;

                if (s + d == 0) // jeśli nie będzie dziesiątek ani setek, wtedy
                // otrzymamy samą grupę
                { // przykładowo 1000 - wyświetli nam się "tysiąc", jeśli
// zakomentujemy tego if'a to otrzymamy "jeden tysiąc"
                    j = 0;
                }
            } else if (j == 2) {
                k = 1;
            } else if (j == 3) {
                k = 1;
            } else if (j == 4) {
                k = 1;
            } else {
                k = 2;
            }

// KONIEC KOŃCÓWEK -->
// DROBNA POPRAWKA - ŻEBY NIE ZWRACAŁO PUSTYCH LICZB(WARTOŚCI)
            p = 0;
            if (d == 0 && s == 0 && j == 0) {
                p = g;
                g = 0;
                k = 0;
            }

            słownie2 = setki[(int) s] + dziesiątki[(int) d] + nastki[(int) n]
                    + jedności[(int) j] + grupy[(int) g][(int) k] + słownie2;

// POZBYWAMY SIĘ TYCH LICZBY KTÓRE JUŻ PRZEROBILIŚMY czyli
// przykładowo z 132132 zostaje nam 132 do obróbki
            number = number / 1000;
// ORAZ ZWIĘKSZAMY G KTÓRE ODPOWIEDZIALNE JEST ZA NUMER POLA W
// TABLICY WIELOWYMIAROWEJ
            g = g + 1 + p;
        }
// KONIEC PĘTLI GŁÓWNEJ

// DODANIE ZNAKU I ZWRÓCENIE METODY
        słownie2 = znak + słownie2;
        return słownie2;

    }

    public String getTranslationNumber() {
        return słownie;
    }
}
