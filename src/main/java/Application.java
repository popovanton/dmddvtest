import service.TaxService;

import javax.xml.soap.SOAPException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class Application {

    private static String[] states = new String[] {
            "Налогоплательщик зарегистрирован в ЕГРН и имел статус действующего в указанную дату",
            "Налогоплательщик зарегистрирован в ЕГРН, но не имел статус действующего в указанную дату",
            "Налогоплательщик зарегистрирован в ЕГРН",
            "Налогоплательщик с указанным ИНН зарегистрирован в ЕГРН, КПП не соответствует ИНН или не указан",
            "Налогоплательщик с указанным ИНН не зарегистрирован в ЕГРН",
            "Некорректный ИНН",
            "Недопустимое количество символов ИНН",
            "Недопустимое количество символов КПП",
            "Недопустимые символы в ИНН",
            "Недопустимые символы в КПП",
            "КПП не должен использоваться при проверке ИП",
            "Некорректный формат даты",
            "Некорректная дата (ранее 01.01.1991 или позднее текущей даты)"
    };


    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
        LocalDate localDate = LocalDate.now();
        String dt = localDate.format(formatter);
        TaxService taxService = new TaxService();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные для поиска через пробел или \"exit\" для выхода.");
        String dataInput;
        while (!(dataInput = scanner.nextLine()).equals("exit")) {
            int i = 0;
            String[] information = new String[]{
                    "INN=",
                    "KPP=",
                    "DT=" + dt
            };
            String[] dataToCheck = dataInput.split(" ");
            if (dataToCheck.length > 3) {
                System.out.println("\n Введите корректные данные для поиска через пробел или \"exit\" для выхода.");
                continue;
            }
            for (String s : dataToCheck) {
                information[i] = information[i++] + s;
            }
            try {
                int state = Integer.parseInt(taxService.getState(information));
                System.out.println(state);
                System.out.println(states[state]);
            } catch (SOAPException e) {
                System.out.println("Invalid INN or inner problems occured");
                e.printStackTrace();
            }
            System.out.println("\n Введите данные для поиска через пробел или \"exit\" для выхода.");
        }
    }
}
