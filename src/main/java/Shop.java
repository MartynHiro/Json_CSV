import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public abstract class Shop {
    private static boolean isLoadNeed;
    private static String fileNameForLoad;
    private static String fileFormatForLoad;
    private static boolean isSaveNeed;
    private static String fileNameForSave;
    private static String fileFormatForSave;
    private static boolean isLogNeed;
    private static String logName;

    public static String getLogName() {
        return logName;
    }

    public static boolean getIsLogNeed() {
        return isLogNeed;
    }

    public static String getFileNameForSave() {
        return fileNameForSave;
    }

    public static String getFileFormatForSave() {
        return fileFormatForSave;
    }

    public static boolean getIsSaveNeed() {
        return isSaveNeed;
    }

    public static String getFileNameForLoad() {
        return fileNameForLoad;
    }

    public static boolean getIsLoadNeed() {
        return isLoadNeed;
    }

    public static String getFileFormatForLoad() {
        return fileFormatForLoad;
    }

    public static void shopSettingsFromXML(File settingsFile) throws ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc;

        try {
            doc = dbf.newDocumentBuilder().parse(settingsFile);
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        Node rootNode = doc.getFirstChild(); //тег config
        NodeList children = rootNode.getChildNodes();// теги load/save/log

        openAllBranches(children);

        Shop.setConfigs();
    }

    private static void openAllBranches(NodeList children) {
        NodeList grandchildren;
        for (int i = 0; i < children.getLength(); i++) {

            Node child = children.item(i); //смотрим каждый элемент (нод)

            if (child.getNodeType() != Node.ELEMENT_NODE) { //надо убедиться что это не отступ/пробел, а именно тег
                continue; //так избежим вложенности
            }

            grandchildren = child.getChildNodes(); //здесь будут enabled/fileName/format

            for (int j = 0; j < grandchildren.getLength(); j++) {

                Node grandchild = grandchildren.item(j);

                if (grandchild.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                if (grandchild.getTextContent() != null) {
                    saveConfigsInTxt(grandchild.getTextContent());
                }
            }
        }
    }

    private static void saveConfigsInTxt(String config) {
        try (BufferedWriter writer = new BufferedWriter((new FileWriter(Main.SHOP_CONFIG_TXT, true)))) {
//построчно сохраняем настройки
            writer.write(config);
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    private static void setConfigs() {
        try (BufferedReader reader = new BufferedReader((new FileReader(Main.SHOP_CONFIG_TXT)))) {

            String firstLine = reader.readLine();  //нужна ли загрузка
            isLoadNeed = firstLine.equals("true");

            fileNameForLoad = reader.readLine(); //файл для загрузки

            fileFormatForLoad = reader.readLine(); //формат файла для загрузки

            String fourthLine = reader.readLine(); //нужно ли сохранение
            isSaveNeed = fourthLine.equals("true");

            fileNameForSave = reader.readLine(); //файл для сохранения

            fileFormatForSave = reader.readLine(); //формат файла для сохранения

            String seventhLine = reader.readLine(); //нужно ли сохранять лог
            isLogNeed = seventhLine.equals("true");

            logName = reader.readLine(); //файл для лога


        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        }
    }
}

