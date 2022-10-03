import java.io.*;

public class Basket {

    final private Product[] product;
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

    public void saveBin(File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void loadFromBinFile(File file){
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
