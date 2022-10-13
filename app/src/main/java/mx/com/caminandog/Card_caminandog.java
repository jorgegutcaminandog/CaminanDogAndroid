package mx.com.caminandog;

public class Card_caminandog  {
    public String card_number;
    public String brand;
    public String id;
    public String expiration_month;
    public String expiration_year;
    public String holder;
    public String ccv;


    public Card_caminandog(){}

    public Card_caminandog( String holder,String card_number, String ccv, String expiration_month, String expiration_year) {
        this.card_number = card_number;
        this.expiration_month = expiration_month;
        this.expiration_year = expiration_year;
        this.holder = holder;
        this.ccv = ccv;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpiration_month() {
        return expiration_month;
    }

    public void setExpiration_month(String expiration_month) {
        this.expiration_month = expiration_month;
    }

    public String getExpiration_year() {
        return expiration_year;
    }

    public void setExpiration_year(String expiration_year) {
        this.expiration_year = expiration_year;
    }
}
