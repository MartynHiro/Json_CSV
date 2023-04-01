
import java.io.File;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Basket basket;

        String[] products = {
                "Pepsi",
                "Donut",     //продукты
                "Pie"
        };

        int[] prices = {57, 39, 189}; //цены продуктов

        File fileJson = new File("basket.json");
        File fileCSV = new File("log.csv");

        ClientLog clientLog = new ClientLog(); //создается клиентский лог

        if (Basket.loadFromJsonFile(fileJson) == null) { //если по данному пути нет файла для загрузки, создаем новую корзину
            basket = new Basket(products, prices);
            System.out.println("Создаем новую корзину, так как старая не найдена");
        } else {
            basket = Basket.loadFromJsonFile(fileJson); //если она там есть, то сохраняем старую корзину
            System.out.println("Найдена старая корзина, произведена загрузка");
        }

        while (true) {
            MethodsShelf.startMessage(products, prices);

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
                clientLog.log(productNumber, productCount);
                Objects.requireNonNull(basket).addToCart(productNumber, productCount);

            } catch (Exception x) {
                System.out.println("Вы вводите буквы, а необходимы цифры!!!!!!");
                System.out.println("-------------------------------------------");
                continue;
            }

            if (!MethodsShelf.isNumberCorrect(parts[0], parts[1], products)) {
                RuntimeException x = new IncorrectInputNumbersException(input);
                MethodsShelf.error(x);
            }
        }

        System.out.println(Objects.requireNonNull(basket).printCart()); //выводим корзину на экран

        clientLog.exportAsCSV(fileCSV);//создаем файл лога нашей последней работы

        basket.saveJson(basket, fileJson);

    }
}