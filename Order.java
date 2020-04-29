import java.io.Serializable;

/**
 * Created by yingchengluo on 2017-08-07.
 */
public class Order implements Serializable{

  private int orderNumber;
  private String productName;
  private int quantity;
  private boolean pending;
  private String date;

  public Order(int orderNumber, String productName, int quantity, boolean pending, String date){
    this.orderNumber = orderNumber;
    this.productName = productName;
    this.quantity = quantity;
    this.pending = pending;
    this.date = date;

  }
  public int getOrderNumber(){return orderNumber;}

  public String getProductName(){return productName;}

  public int getQuantity(){return quantity;}

  public boolean getPending(){return pending;}

  void changeStatus(){
    pending = !pending;
  }

  public String getDate(){return date;}
}
