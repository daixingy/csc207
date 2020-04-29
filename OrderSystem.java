import java.awt.SecondaryLoop;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An order system, which can order products
 */
class OrderSystem implements Serializable{
  private Integer order_index = 0;
  private ArrayList<Order> orderList = new ArrayList<>();
  private HashMap<Product, String> productHistory;
  private static final Logger logger =
      Logger.getLogger(OrderSystem.class.getName());
  private static final Handler consoleHandler = new ConsoleHandler();

  OrderSystem(String pathToOrderList){
    CsvReader csvReader = new CsvReader();
    orderList = csvReader.csvReaderOrder(pathToOrderList);

    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
    try{
      FileHandler fileHandler = new FileHandler("log.txt");
      logger.addHandler(fileHandler);
    }catch(IOException e){
      logger.log(Level.SEVERE, e.getMessage());
    }
    logger.log(Level.FINE, "OrderSystem has been initialized");
  }

  /**
   * reorder a product when its current quantity is below its threshold
   *
   * @param product  the product needed to be reordered
   */
   void order(Product product){
     LocalDate d = LocalDate.now();
     int quantity = 5 * product.getThreshold();
     Order order = new Order(order_index, product.getName(), quantity, true,d.toString());
     orderList.add(order);
     order_index = order_index + 1;
     logger.log(Level.FINE, "order" + product + "has been recorded.");
  }

  /**
   *  a order with number orderNum has been finished which means the newly ordered product has been
   *  arrived at the grocery store.
   *
   * @param orderNum the index of the order
   *
   */
  void hasArrived(int orderNum){
    for(Order order :orderList){
      if(orderNum == order.getOrderNumber() && order.getPending()){
        order.changeStatus();
        break;
      }
    }
  }

  public Order searchOrder(int orderNum){
    for(Order order: orderList){
      if(order.getOrderNumber() == orderNum){
        return order;
      }
    }
    return null;
  }

  public ArrayList<Order> getOrderList(){
    return orderList;
  }

}
