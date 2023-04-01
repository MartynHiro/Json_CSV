
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static final File CONFIGURATIONS_XML = new File("shop.xml");
    public static final File SHOP_CONFIG_TXT = new File("shopConfigs.txt");
    public static final String[] PRODUCTS = {
            "Pepsi",
            "Donut",     //продукты
            "Pie"
    };
    public static final int[] PRICES = {57, 39, 189}; //цены продуктов

    public static void main(String[] args) throws ParserConfigurationException {
        Scanner scan = new Scanner(System.in);
        Basket basket;

        Shop.shopSettingsFromXML(CONFIGURATIONS_XML); //в первую очередь загружаем xml файл с настройками

        basket = MethodsShelf.needToLoad();

        MethodsShelf.inputCycle(scan, basket);

        System.out.println(Objects.requireNonNull(basket).printCart()); //выводим корзину на экран

        MethodsShelf.needToSave(basket);
    }
}