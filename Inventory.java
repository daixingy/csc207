import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an inventory that records all products. One can search in the inventory for a product
 * given its name or upc.
 */
public class Inventory implements Serializable{
  ArrayList<Product>productList;
  private static final Logger logger =
      Logger.getLogger(Inventory.class.getName());
  private static final Handler consoleHandler = new ConsoleHandler();

  /**
   * Constructor
   * @param initialInventoryPath the path for initial inventory file
   * @throws IOException IOException
   */
  Inventory(String initialInventoryPath) throws IOException{
    CsvReader reader = new CsvReader();
    this.productList = reader.readCsv(initialInventoryPath);

    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
    try{
      FileHandler fileHandler = new FileHandler("log.txt");
      logger.addHandler(fileHandler);
    }catch(IOException e){
      logger.log(Level.SEVERE, e.getMessage());
    }

    logger.log(Level.FINE, "Inventory has been initialized");
  }

  public void addNewThings(ArrayList<Product> products){
    productList.addAll(products);
  }

  public ArrayList<Product> getProductList(){
    return productList;
  }

  ArrayList<Product> findMultipleByUpc(ArrayList<Long> upcList){
    ArrayList<Product> products = new ArrayList<>();
    for(Long upc: upcList){
      products.add(findByUpc(upc));
    }
    return products;
  }
  /**
   * Find product by upc
   * @param upc the upc of the product that you wish to find
   * @return the product that you wish to find
   */
  Product findByUpc(long upc){
    for(Product product:productList){
      if(product.getUPC() == upc){
        return product;
      }
    }
    return null;
  }

  private static boolean partialSearchName(String productName, String name){
    int searchMeLength = productName.length();
    int findMeLength = name.length();
    boolean foundIt = false;
    for (int i = 0; i <= (searchMeLength - findMeLength); i++) {
      if (productName.regionMatches(true, i, name, 0, findMeLength)) {
        foundIt = true;
        break;
      }
    }
    return foundIt;
  }
  /**
   * Find product by partial name, ignore case
   * @param name the partial name of the product that you wish to find
   * @return the product that you wish to find
   */
  ArrayList<Product> findByPartialName(String name){
    ArrayList<Product> products = new ArrayList<>();
    for(Product product:productList) {
      if(partialSearchName(product.getName(), name)){
        products.add(product);
      }
    }
    return products;
  }

  Product findByName(String name){
    for(Product product:productList){
      if(product.getName().equals(name)) {
        return product;
      }
    }
    return null;
  }

  /**
   * Save inventory to a file
   * @param filePath the path of saved inventory
   * @throws IOException IOException
   */
  void saveInventory(String filePath) throws IOException {

    OutputStream file = new FileOutputStream(filePath);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    for (Product product : productList) {
      output.writeObject(product);
    }
    output.close();
    logger.log(Level.FINER, "Inventory has been saved to " + filePath);
  }
    @Override
    public String toString() {
      String result = "";
      for (Product r : productList) {
        result += r.toString() + "\n";
      }
      return result;
    }




  /**
   * Add products into inventory
   * @param upc upc of an incoming product
   * @param name  name of an incoming product
   * @param quantity quantity of an incoming product
   * @param section section of an incoming product
   * @param price initial price of of an incoming product
   */
  void addProducts(long upc, String name, int quantity, String section, int price, int cost, int threshold, int[] location ){
    if(findByUpc(upc) == null){
      Product newProduct = new Product(name, upc, quantity, section, price, cost, threshold, location);
      productList.add(newProduct);
    }else{
      Product product = findByUpc(upc);
      product.setQuantity(product.getQuantity()+ quantity);
    }
    logger.log(Level.FINE, Integer.toString(quantity) + name + "has been added into inventory");
  }

  /**
   * return a list of products which are on sale
   *
   */
  private ArrayList<Product> getSaleProducts(){
    ArrayList<Product> saleProducts = new ArrayList<>();
    for(Product p : productList){
      if(p.getPriceSystem().getCurrentPrice() != p.getPriceSystem().getInitialPrice() && p.getPriceSystem().getCurrentPrice()
          != 0 && p.getPriceSystem().getInitialPrice() != 0){
        saleProducts.add(p);
      }
    }
    return saleProducts;
  }

  void mergeWithInventory(Inventory inventory){
    this.productList.addAll(inventory.getProductList());
  }

}
