import java.io.IOException;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

/**
 * Created by Ashley on 17/8/5.
 */
public class Accounting implements Serializable{

  private Integer totalRevenue;
  private Integer totalCost;
  private String date;

  Accounting(String pathToAccountBalance){
    CsvReader csvReader = new CsvReader();
    Object[] account = csvReader.csvReadAccountBalance(pathToAccountBalance);
    totalRevenue = (Integer) account[0];
    totalCost = (Integer) account[1];
    date = (String) account[2];
  }

  void update(int price, int cost, String date) {
    this.date = date;
    totalRevenue += price;
    totalCost += cost;
  }



  int realTimeProfit(){
    return totalRevenue - totalCost;
  }

  public int getTotalRevenue(){
    return this.totalRevenue;
  }

  public int getTotalCost(){
    return this.totalCost;
  }

  public String getDate(){return this.date;}
}
