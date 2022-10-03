import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;


public class Main {

    public static Product[] product = {
            new Product("Хлеб", 100),
            new Product("Яблоки", 200),
            new Product("Молоко", 300)
    };

    private static boolean basketLoadEnable = false;
    private static String basketLoadFileName = "";
    private static FileFormat basketLoadFormat = FileFormat.JSON;

    private static boolean basketSaveEnable = false;
    private static String basketSaveFileName = "";
    private static FileFormat basketSaveFormat = FileFormat.JSON;

    private static boolean logSaveEnable = false;
    private static String logFileName = "";

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ParseException {

        Basket card;
        int productNumber;
        int productCount;
        String nodeName;
        String parameterName;
        String parameterValue;

        Scanner scanner = new Scanner(System.in);
        ClientLog clientLog = new ClientLog();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (Node.ELEMENT_NODE == currentNode.getNodeType()) {
                nodeName = currentNode.getNodeName();
                NodeList map = currentNode.getChildNodes();
                for (int a = 0; a < map.getLength(); a++) {
                    Node parameter = map.item(a);
                    if (Node.ELEMENT_NODE == parameter.getNodeType()) {
                        parameterName = parameter.getNodeName();
                        parameterValue = parameter.getNodeValue();
                        setParameters(nodeName, parameterName, parameterValue);
                    }

                }
            }
        }

        File basketFileForLoad = new File(basketLoadFileName);
        File basketFileForSave = new File(basketSaveFileName);
        File logFile = new File(logFileName);

        if (basketFileForLoad.exists() && basketLoadEnable) {
            System.out.println("Загрузить корзину(нажмите enter)? ");
            if (scanner.nextLine().equals("")) {
                if (basketLoadFormat == FileFormat.JSON) {
                    card = Basket.loadFromJson(basketFileForLoad);
                } else {
                    card = Basket.loadFromTxtFile(basketFileForLoad);
                }
            } else {
                card = new Basket(product);
            }
        } else {
            card = new Basket(product);
        }

        while (true) {
            System.out.println("Список доступных товаров для покупки");

            for (int i = 0; i < product.length; i++) {
                System.out.println((i + 1) + ". " + product[i].getName() + " " + product[i].getPrice() + " pуб/шт");
            }

            System.out.println("Выберите товар и количество или введите `end`");
            String inputString = scanner.nextLine(); // "1 10"
            String[] parts = inputString.split(" ");
            if (parts.length == 2) {
                try {
                    productNumber = Integer.parseInt(parts[0]);
                    productCount = Integer.parseInt(parts[1]);
                    if (productNumber <= 0 || productNumber > product.length) {
                        System.out.print("Неверный номер товара");
                        continue;
                    }
                    if (productCount <= 0) {
                        continue;
                    }
                    card.addToCart(productNumber - 1, productCount);
                    if (basketSaveEnable) {
                        if (basketSaveFormat == FileFormat.JSON) {
                            card.saveJson(basketFileForSave);
                        }
                        if (basketSaveFormat == FileFormat.TXT) {
                            card.saveTxt(basketFileForSave);
                        }
                    }
                    clientLog.log(productNumber - 1, productCount);
                } catch (NumberFormatException nfe) {
                    System.out.println("Необходимо указать два целых числа");
                }
            } else if (inputString.equals("end")) {
                break;
            }
            System.out.println("Укажите следующую пару чисел");
        }
        if (logSaveEnable) {
            clientLog.exportAsCSV(logFile);
        }
        scanner.close();
        card.printCart();
    }

    private static void setParameters(String nodeName, String parameterName, String parameterValue) {
        if (nodeName.equals("load")) {
            if (parameterName.equals("enabled")) {
                basketLoadEnable = parameterValue.equals("true");
            }
            if (parameterName.equals("fileName")) {
                basketLoadFileName = parameterValue;
            }
            if (parameterName.equals("format")) {
                if (parameterValue.equals("json")) {
                    basketLoadFormat = FileFormat.JSON;
                } else basketLoadFormat = FileFormat.TXT;
            }
        }
        if (nodeName.equals("save")) {
            if (parameterName.equals("enabled")) {
                basketSaveEnable = parameterValue.equals("true");
            }
            if (parameterName.equals("fileName")) {
                basketSaveFileName = parameterValue;
            }
            if (parameterName.equals("format")) {
                basketSaveFormat = FileFormat.JSON;
            } else {
                basketSaveFormat = FileFormat.TXT;
            }
        }
        if (nodeName.equals("log")) {
            if (parameterName.equals("enabled")) {
                logSaveEnable = parameterValue.equals("true");
            }
            if (parameterName.equals("fileName")) {
                logFileName = parameterValue;
            }
        }
    }
}