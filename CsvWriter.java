import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by yingchengluo on 2017-08-07.
 */
public class CsvWriter {

  private static final String DELIMITER = ",";
  private static final String LINE_SEPARATER = "\n";

  CsvWriter(){}

  public void writeInventory(ArrayList<Product> products, String path){
    FileWriter fileWriter = null;
    try{
      fileWriter = new FileWriter(path);
      for(Product product: products){
        fileWriter.append(String.valueOf(product.getUPC()));
        fileWriter.append(DELIMITER);
        fileWriter.append(product.getName());
        fileWriter.append(DELIMITER);
        fileWriter.append(String.valueOf(product.getPrice()));
        fileWriter.append(DELIMITER);
        fileWriter.append(String.valueOf(product.getQuantity()));
        fileWriter.append(DELIMITER);
        fileWriter.append(product.getSection());
        fileWriter.append(DELIMITER);
        fileWriter.append(String.valueOf(product.getThreshold()));
        fileWriter.append(DELIMITER);
        fileWriter.append(String.valueOf(product.getNumberLocation()[0]));
        fileWriter.append(DELIMITER);
        fileWriter.append(String.valueOf(product.getNumberLocation()[1]));
        fileWriter.append(DELIMITER);
        fileWriter.append(String.valueOf(product.getCost()));
        fileWriter.append(LINE_SEPARATER);
      }
    }catch(Exception e){
      System.out.println("Invalid directory or Parsing");
      e.printStackTrace();
    }finally {
      try{
        fileWriter.flush();
        fileWriter.close();
      }catch (IOException e){
        System.out.println("Error with flushing and closing");
        e.printStackTrace();
      }catch (NullPointerException e){
        System.out.println("fileWriter hasn't been initialized");
        e.printStackTrace();
      }
    }
  }

  public void writeCsvOrder(ArrayList<Order> orders, String path){
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
    String formattedString = date.format(formatter);
    FileWriter fileWriter = null;
    try{
      fileWriter = new FileWriter(path);
      for(Order order: orders){
        fileWriter.append(String.valueOf(order.getOrderNumber()));
        fileWriter.append(DELIMITER);
        fileWriter.append(order.getProductName());
        fileWriter.append(DELIMITER);
        fileWriter.append(String.valueOf(order.getQuantity()));
        fileWriter.append(DELIMITER);
        fileWriter.append(String.valueOf(order.getPending()));
        fileWriter.append(formattedString);
        fileWriter.append(LINE_SEPARATER);
      }
    }catch(Exception e){
      System.out.println("Invalid directory or Parsing");
      e.printStackTrace();
    }finally {
      try{
        fileWriter.flush();
        fileWriter.close();
      }catch (IOException e){
        System.out.println("Error with flushing and closing");
        e.printStackTrace();
      }catch (NullPointerException e){
        System.out.println("fileWriter hasn't been initialized");
        e.printStackTrace();
      }
    }
  }

  public void writeCsvAccountBalance(int[] accountBalance, String path){
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
    String formattedString = date.format(formatter);
    FileWriter fileWriter = null;
    try{
      fileWriter = new FileWriter(path);
      fileWriter.append(String.valueOf(accountBalance[0]));
      fileWriter.append(DELIMITER);
      fileWriter.append(String.valueOf(accountBalance[1]));
      fileWriter.append(DELIMITER);
      fileWriter.append(formattedString);
      fileWriter.append(LINE_SEPARATER);
    }catch(Exception e){
      System.out.println("Invalid directory or Parsing");
      e.printStackTrace();
    }finally {
      try{
        fileWriter.flush();
        fileWriter.close();
      }catch (IOException e){
        System.out.println("Error with flushing and closing");
        e.printStackTrace();
      }catch (NullPointerException e){
        System.out.println("fileWriter hasn't been initialized");
        e.printStackTrace();
      }
    }
  }


}
