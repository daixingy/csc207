import java.io.Serializable;
import java.text.ParseException;

/**
 * Products of grocery store. A product has name, UPC(price), location.
 */
public class Product implements Serializable{
  private String name;
  private long upc;
  int[] location = new int[2]; //[aisle][shelve]
  private int quantity;
  private int threshold;
  private int cost;
  private String section;
  private PriceSystem priceSystem;

  Product(){}
  /**
   * Create a new product.
   * @param name the name of the product
   * @param upc the Universal Price Code of this product
   * @param quantity the current quantity of this product
   * @param section the section of this product
   */
  Product(String name, long upc, int quantity,String section, int price){
    this.name = name;
    this.upc = upc;
    this.quantity = quantity;
    this.section = section;
    this.priceSystem = new PriceSystem(price);
  }

  Product(String name,long upc, int quantity, String section, int price, int cost, int threshold, int[] location ){
    this.name = name;
    this.upc = upc;
    this.quantity = quantity;
    this.section = section;
    this.priceSystem = new PriceSystem(price);
    this.cost = cost;
    this.threshold = threshold;
    this.location = location;
  }

  /**
   * set up a price system for this product
   *
   * @param price the initial price of this product*
   */
  void setPriceSystem(int price){
    this.priceSystem = new PriceSystem(price);
  }

  /**
   * change the name of this product to a new name
   *
   * @param name the new name of this product
   */
  public void setName(String name){
    this.name = name;
  }

  /**
   * change the upc of this product to a new upc
   *
   * @param upc the new upc of this product
   */
  void setUpc(int upc){
    this.upc = upc;
  }


  /**
   * change the threshold of this product to a new threshold
   *
   * @param threshold the new threshold of this product
   */
  void setThreshold(int threshold){
    this.threshold = threshold;
  }


  /**
   * change the quantity of this product to a new quantity
   *
   * @param quantity the new quantity of this product
   */
  public void setQuantity(int quantity){
    this.quantity = quantity;
  }

  /**
   * return the current price of this product
   *
   */
  public int getPrice() throws ParseException{
    return priceSystem.showPrice();
  }

  public String getSection(){
    return this.section;
  }

  /**
   * return the upc of this product
   *
   */

  public long getUPC(){
    return upc;
  }


  /**
   * Set the location of a product.
   * @param location the location of this product
   */
  void setLocation(int[] location){
    this.location = location;
  }

  public PriceSystem getPriceSystem(){
    return priceSystem;
  }

  public int[] getNumberLocation(){
    return location;
  }

  /**
   * Get the location of this product
   * @return the location of this product
   */
  public String getLocation(){
    return "Aisle " + location[0] + " Shelve " + location[1];
  }

  public String getName(){
    return name;
  }

  public int getQuantity(){
    return quantity;
  }

  public int getThreshold(){return threshold;}

  public int getCost(){return cost;}
  /**
   * return a string representation of this product
   */
  @Override
  public String toString() {
    try {
      return Long.toString(upc) + "," + name + "," + Integer.toString(priceSystem.showPrice()) +
      "," + Integer.toString(quantity) +"," + section +"," + Integer.toString(threshold) + ","
          + Integer.toString(location[0]) + "," + Integer.toString(location[1]) ;
    }catch(ParseException e){
      return "Parsing error";
    }
  }


}
