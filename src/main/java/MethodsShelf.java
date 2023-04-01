import java.io.File;
import java.util.Objects;
import java.util.Scanner;

public abstract class MethodsShelf {
    public static void error(RuntimeException exception) { // error output
        System.out.println(exception + "\n" + "---------------------------------------------------------------------");
    }

    public static boolean isNumberCorrect(String firstNumber, String secondNumber, String[] products) {
        int productNumber = Integer.parseInt(firstNumber);
        int productCount = Integer.parseInt(secondNumber);
        if (productNumber > 0 && productCount > 0) {   // method to check is number < 0 or too large
            return productNumber <= products.length; //true
        }
        return false;
    }

    public static void inputCycle(Scanner scan, Basket basket) {
        while (true) {
            MethodsShelf.startMessage(Main.PRODUCTS, Main.PRICES);

            String input = scan.nextLine();

            if ("end".equals(input)) {
                break;
            }

            if (!MethodsShelf.isSpaceSecond(input)) {
                RuntimeException x = new AmountOfInputNumbersException(input);
                MethodsShelf.error(x);
                continue;
            }

            String[] parts = input.split(" ");

            try {
                int productNumber = Integer.parseInt(parts[0]) - 1;
                int productCount = Integer.parseInt(parts[1]);
                //записываем покупку в лог и корзину
                ClientLog.log(productNumber, productCount);
                Objects.requireNonNull(basket).addToCart(productNumber, productCount);

            } catch (Exception x) {
                System.out.println("Вы вводите буквы, а необходимы цифры!!!!!!");
                System.out.println("-------------------------------------------");
                continue;
            }

            if (!MethodsShelf.isNumberCorrect(parts[0], parts[1], Main.PRODUCTS)) {
                RuntimeException x = new IncorrectInputNumbersException(input);
                MethodsShelf.error(x);
            }
        }
    }

    public static Basket needToLoad() {
        Basket basket;
        if (!Shop.getIsLoadNeed()) { //исходя из настроек в shop.xml надо ли загружать корзину и какого формата
            basket = new Basket(Main.PRODUCTS, Main.PRICES);
            System.out.println("Создана новая корзина");

        } else if (Shop.getFileFormatForLoad().equals("json")) {
            basket = Basket.loadFromJsonFile(new File(Shop.getFileNameForLoad()));//имя берем тоже из xml файла
        } else {
            basket = Basket.loadFromTxtFile(new File(Shop.getFileNameForLoad()));
        }
        return basket;
    }

    public static void needToSave(Basket basket) {
        if (Shop.getIsLogNeed()) { //узнаем из xml надо ли сохранять лог
            ClientLog.exportAsCSV(new File(Shop.getLogName()));
        }

        //узнаем из xml надо ли сохранять корзину
        if (Shop.getIsSaveNeed() && Shop.getFileFormatForSave().equals("json")) {
            basket.saveJson(basket, new File(Shop.getFileNameForSave()));
        } else if (Shop.getIsSaveNeed()) {
            basket.saveTxt(new File(Shop.getFileNameForSave()));
        } else {
            System.out.println("Конец работы без сохранения корзины");
        }
    }

    public static boolean isSpaceSecond(String text) { //method to check how many spaces in the text and the second character is a space?
        int spacesCounter = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                spacesCounter++;
            }
        }
        if (spacesCounter == 1) {
            char secondSymbol = text.charAt(1);
            return ' ' == secondSymbol; // true
        }
        return false;
    }

    public static void startMessage(String[] products, int[] prices) { //initial message output
        System.out.println("Список возможных товаров для покупки: ");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + "." + products[i] + " - " + prices[i] + " руб/шт");
        }
        System.out.println("Выберите номер товара и количество через пробел, либо введите 'end'");
    }
}
