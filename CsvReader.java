import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class reads in product information from a csv file.
 * The format of the csv file should be:
 *    product upc, product name, product price, product quantity, product section, product threshold, product location,
 *    product price
 */
public class CsvReader {
  private ArrayList<Product> productList = new ArrayList<>();
  private static final Logger logger =
      Logger.getLogger(CsvReader.class.getName());
  private static final Handler consoleHandler = new ConsoleHandler();

  /**
   * Constructs a CsvReader
   */
  CsvReader(){
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
  }

  ArrayList<String> readConfiguration(String path) throws IOException{
    Scanner scanner = new Scanner(new FileInputStream(path));
    ArrayList<String> record = new ArrayList<>();
    while(scanner.hasNextLine()){
      record.add(scanner.nextLine());
    }
    scanner.close();
    return record;
  }





  ArrayList<Product> readCsv(String path) throws IOException {
      // FileInputStream can be used for reading raw bytes, like an image.
      Scanner scanner = new Scanner(new FileInputStream(path));
      String[] record;
      while(scanner.hasNextLine()) {
        record = scanner.nextLine().split(",");
        long upc = Long.parseLong(record[0]);
        String name = record[1];
        int price = Integer.parseInt(record[2]);
        int quantity = Integer.parseInt(record[3]);
        String section = record[4];
        int threshold = Integer.parseInt(record[5]);
        int aisleNumber = Integer.parseInt(record[6]);
        int shelveNumber = Integer.parseInt(record[7]);
        int[] location = new int[2];
        location[0] = aisleNumber;
        location[1] = shelveNumber;
        int cost = Integer.parseInt(record[8]);
        Product product = new Product(name, upc, quantity, section, price,cost,threshold, location);
        productList.add(product);
      }
      scanner.close();
      logger.log(Level.FINE, "csv file has been read.");
      return productList;
    }

    ArrayList<User> csvReadUsers(String path){
      ArrayList<User> users = new ArrayList<>();
      try{
        Scanner scanner = new Scanner(new FileInputStream(path));
        String[] record;
        while(scanner.hasNextLine()) {
          record = scanner.nextLine().split(",");
          String username = record[0];
          int password = Integer.parseInt(record[1]);
          String position = record[2];
          User user = new User(username, password, position);
          users.add(user);
        }
        scanner.close();
      }catch(FileNotFoundException e){
        System.out.println("User data not found");
      }
      return users;
    }

    ArrayList<Order> csvReaderOrder(String path){
      ArrayList<Order> orders = new ArrayList<>();
      try{
        Scanner scanner = new Scanner(new FileInputStream(path));
        String[] record;
        while(scanner.hasNextLine()) {
          record = scanner.nextLine().split(",");
          int orderNumber = Integer.parseInt(record[0]);
          String productName = record[1];
          int quantity = Integer.parseInt(record[2]);
          boolean pending = Boolean.parseBoolean(record[3]);
          String date = record[4];
          Order order = new Order(orderNumber, productName, quantity, pending,date);
          orders.add(order);
        }
        scanner.close();
      }catch(FileNotFoundException e){
        System.out.println("User data not found");
      }
      return orders;
    }

    Object[] csvReadAccountBalance(String path){
      Object[] account = new Object[3];
      try{
        Scanner scanner = new Scanner(new FileInputStream(path));
        String[] record;
        while(scanner.hasNextLine()) {
          record = scanner.nextLine().split(",");
          Integer totalRevenue = Integer.parseInt(record[0]);
          Integer totalCost = Integer.parseInt(record[1]);
          String date =  record[2];
          account[0] = totalRevenue;
          account[1] = totalCost;
          account[2] = date;
        }
        scanner.close();
      }catch(FileNotFoundException e){
        System.out.println("User data not found");
      }
      return account;
    }


  }



