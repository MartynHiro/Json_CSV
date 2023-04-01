import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ClientLog {
    private Map<Integer, Integer> productLog = new HashMap<>();

    public void log(int productNum, int amount) {
        productLog.put(productNum + 1, amount);
    }

    public void exportAsCSV(File file) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
//заполняем первую строку файла, а потом переводим нашу мапу в CSV формат
            StringJoiner namesOfProductsString = new StringJoiner(",")
                    .add("product number")
                    .add("amount of product");
            String[] firstLineOfCSV = namesOfProductsString.toString().split(",");
            writer.writeNext(firstLineOfCSV);

            for (Map.Entry<Integer, Integer> purchase : productLog.entrySet()) {
                StringJoiner purchaseString = new StringJoiner(",")
                        .add(Integer.toString(purchase.getKey())) //не забываем что в мапе Integer и сам он не
                        .add(Integer.toString(purchase.getValue())); //приводится к String

                String[] purchaseArrayForCSV = purchaseString.toString().split(",");
                writer.writeNext(purchaseArrayForCSV);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
