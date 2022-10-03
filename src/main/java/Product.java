public class Product {

    private final String name;
    private final int price;
    int inBasket = 0;

    public Product(String name, int price, int inBascet) {
        this.name = name;
        this.price = price;
        this.inBasket = inBascet;
    }

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getInBasket() {
        return inBasket;
    }

    public void sumInBasket(int sum) {
        this.inBasket += sum;
    }

}