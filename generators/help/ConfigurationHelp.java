package lombardia2014.generators.help;

import javax.swing.JLabel;

/**
 *
 * @author Domek
 */
public class ConfigurationHelp implements HelpStrategy {

    String textT = "<html>"
            + "<p><u>Użytkownicy</u> - "
            + "Umożliwia nam dodawanie/usuwanie<br /> użytkownika "
            + "lub pozwala na zmiane hasła. "
            + "<ul>"
            + "<li>Nowy - Otwiera nowy formularz gdzie <br/> tworzymy nowego użytkownka "
            + "i <br/> przytdzielamy mu rolę</li>"
            + "<li>Zmien hasło - można zmienić hasło<br/> użytkownikowi,"
            + " który aktualnie jest<br/> zaznaczony na liście</li>"
             + "<li>Usuń - usuwa użytkownika, <br/>"
            + "który aktualnie jest<br/> zaznaczony na liście</li>"
            + "</ul>"
            + "</p>"
            + "<p><u>Uprawnienia</u> - "
            + "Pozwala na zmianę <br />upraniwnień zaznaczonego użytkownika z listy."
            + "</p>"
            + "<p><u>Operacje</u> - "
            + "Pokazuje przeprowadzone w<br />aplikacji operacje z czego:"
            + "<ul>"
            + "<li>Okres - Pozwala na zmianę okresu<br/> czasu z miesięcznego "
            + "<br/> na roczny</li>"
            + "<li>Drukuj - Pozwala na drukowanie listy<br/> operacji"
            + "<li>Usuń - usuwa wybraną operację</li>"
            + "</ul>"
            + "</p>"
            + "<p><u>Ustawienia</u> - "
            + "Pokazuje przeprowadzone w<br />aplikacji operacje z czego:"
            + "<ul>"
            + "<li>VAT - Określamy jaki ma być VAT<br/> na produktach</li>"
            + "<li>Opłata Minimalna - Określa minimalną<br/> opłatę za wystawienie umowy"
            + "<li>Opłata Manipulacyjna - Określa opłate<br/> jednorazową za wystawienie umowy</li>"
            + "<li>Zyska na dzień - Określa zysk dzienny<br/> za umowę</li>"
            + "<li>Stopa - Określa stopę procentową</li>"
            + "<li>RSO - Określa roczną stopę procentową</li>"
            + "<li>Kwota przedterminowa - Określa kwopę<br />"
            + " za otrzymanie umowy przed czasem</li>"
            + "</ul>"
            + "</p>"
            + "<p><u>Usuwanie wielu</u> - "
            + "Pozwala na usuwanie wieu<br /> umów lub produktów:"
            + "<ul>"
            + "<li>Posiada przełącznik umowa/przedmiot</li>"
            + "<li>Szukaj - Pole pozwala na wpisanie<br/>"
            + "powiązanych z umowami lub<br /> przedmiotami</li>"
            + "<li>Tabela pokazuje wynik, gdzie możemy <br />"
            + "zaznaczyć kilka elementów<br /> i poprzez kliknięcie Usuń <br/>"
            + " usuwamy obiety."
            + "</ul>"
            + "</p>"
            
            + "</html>";
    String titleT = "<html>Konfiguracja<br /></html>";

    @Override
    public void getText(JLabel title, JLabel text) {
        title.setText(titleT);
        text.setText(textT);
    }

}
