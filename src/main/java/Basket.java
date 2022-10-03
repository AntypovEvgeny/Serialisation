import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Basket {

    final protected Product[] product;
    private int sumProduct = 0;

    public Basket(Product[] product) {
        this.product = product.clone();
    }

    public void addToCart(int productNum, int amount) {
        product[productNum].sumInBasket(amount);
        sumProduct += amount * product[productNum].getPrice();
    }

    public void printCart() {
        System.out.println("Ваша корзина:");
        for (Product i : product) {
            if (i.getInBasket() != 0) {
                System.out.println(i.getName() + " " + i.getInBasket() +
                        " шт " + i.getPrice() + " pуб/шт" + " В сумме " +
                        (i.getInBasket() * i.getPrice()));
            }

        }
        System.out.println("Итого " + sumProduct + " руб.");
    }

    public void saveTxt(File textFile) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            Stream.of(product).forEach(p ->
                    out.printf(p.getName() + " " + p.getPrice() + " " + p.getInBasket() + " "));
        }
    }

    public void saveJson(File textFile) {
        try (FileWriter writer = new FileWriter(textFile)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.setPrettyPrinting().create();
            writer.write(gson.toJson(this, Basket.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws FileNotFoundException, ParseException {
        try (Scanner scanner = new Scanner(textFile)) {
            List<Product> product = new ArrayList<>();
            String name;
            int price;
            int inBasket;
            NumberFormat nf = NumberFormat.getInstance();
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(" ");
                name = parts[0];
                price = nf.parse(parts[1]).intValue();
                inBasket = Integer.parseInt(parts[2]);
                product.add(new Product(name, price, inBasket));
            }
            return new Basket(product.toArray(Product[]::new));
        }
    }

    public static Basket loadFromJson(File textFile) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(textFile);
        return gson.fromJson(reader, Basket.class);
    }
}