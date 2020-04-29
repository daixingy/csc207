import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * a price system that records price of a product. Sales date should be in the format of "yyyy-MM-dd"
 *
 */
public class PriceSystem implements Serializable {

  private int salesPrice;
  private int currentPrice;
  private int initialPrice;
  private HashMap<Integer, ArrayList<LocalDate>> priceHistory = new HashMap<>();
  private LocalDate currentDate;
  private boolean onSale;

  /**
   * Constructs a PriceSystem
   *
   * @param initialPrice the initial price of a product
   *
   */
  PriceSystem(int initialPrice) {
    this.initialPrice = initialPrice;
    this.salesPrice = initialPrice;
    this.currentPrice = initialPrice;
    currentDate = LocalDate.now();
  }

  /**
   * check if it's possible that a product is onSale during this period
   *
   * @param startDate the start date of this sale period of this product
   * @param endDate the end date of this sale period of this product
   *
   */
  private boolean checkSalePeriod(LocalDate startDate, LocalDate endDate) {

    if ((currentDate.compareTo(startDate) > 0) && (currentDate.compareTo(endDate) < 0)) {
      return true;
    }

    onSale = false;
    return false;

  }

  /**
   * set a product to be on sale during a specific period either by replacing its price with
   * new price or discounting its initial price.
   *
   * @param salesPrice the new price or the discount percentage
   * @param startDate the start date of this sale period of this product
   * @param endDate the end date of this sale period of this product
   *
   */
  void setSaleByPrice(Object salesPrice, String startDate, String endDate) throws ParseException {
    if (salesPrice instanceof Integer) {
      this.salesPrice = (int) salesPrice;
    } else if (salesPrice instanceof String) {
      int discount = Integer.parseInt((String) salesPrice);
      this.salesPrice = initialPrice * discount / 100;
    } else {
      System.out.println("Invalid argument");
    }
    ArrayList<LocalDate> saleDates = new ArrayList<>();
    DateTimeFormatter date = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
    LocalDate start_Date = LocalDate.parse(startDate, date);
    LocalDate end_Date = LocalDate.parse(endDate);
    saleDates.add(start_Date);
    saleDates.add(end_Date);
    priceHistory.put(this.salesPrice, saleDates);
    onSale = true;
  }

  private static String checkSalesFormat(String salesPrice){
    if(salesPrice.contains("%")){
      String pattern = "/d*%";
      Pattern p = Pattern.compile(pattern);
      Matcher matcher = p.matcher(salesPrice);
      if(matcher.matches()){
        return "ratio";
      }else {
        return "price";
      }
    }else{
      String pattern = "/d*";
      Pattern p = Pattern.compile(pattern);
      Matcher matcher = p.matcher(salesPrice);
      if(matcher.matches()){
        return "price";
      }else {
        return "price";
      }
    }
  }

  void setSaleByPriceDate(String salesPrice, LocalDate startDate, LocalDate endDate){
    String result = checkSalesFormat(salesPrice);
    switch (result){
      case "ratio":
        int discount = Integer.parseInt(salesPrice.substring(0, salesPrice.length() - 1));
        this.salesPrice = initialPrice * discount / 100;
        ArrayList<LocalDate> saleDates1 = new ArrayList<>();
        saleDates1.add(startDate);
        saleDates1.add(endDate);
        priceHistory.put(this.salesPrice, saleDates1);
        onSale = true;
        break;
      case "price":
        this.salesPrice = Integer.parseInt(salesPrice);
        ArrayList<LocalDate> saleDates2 = new ArrayList<>();
        saleDates2.add(startDate);
        saleDates2.add(endDate);
        priceHistory.put(this.salesPrice, saleDates2);
        onSale = true;
        break;
      case "invalid":
        break;

    }
  }

  /**
   * return the current price of this product
   */
  int showPrice() throws ParseException {

    if (priceHistory.size() < 1) {
      return initialPrice;
    }

    ArrayList<LocalDate> saleDates = priceHistory.get(salesPrice);
    if (checkSalePeriod(saleDates.get(0), saleDates.get(1)) && onSale) {
      this.currentPrice = salesPrice;
    } else {
      this.currentPrice = initialPrice;
    }
    return currentPrice;

  }

  public int getInitialPrice(){
    return this.initialPrice;
  }


  public int getCurrentPrice(){
    return this.currentPrice;
  }

}
