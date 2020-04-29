import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Manager Controller
 */
public class ManagerSceneController extends Controller implements Initializable {
  @FXML ListView<String> profit;
  @FXML ListView<String> revenue;
  @FXML ListView<String> costPrice;
  @FXML Label date;

  //set orders tab
  @FXML TableView<Order> pendingOrders;
  @FXML TableColumn<Order, String> nameColumn_order;
  @FXML TableColumn<Order, Integer> quantityColumn_order;
  @FXML TableColumn<Order, Integer> numColumn_order;
  @FXML TableColumn<Order, String> dateColumn_order;
  @FXML TextField orderNumber;
  @FXML TableView<Order> orderHistory;
  @FXML TableColumn<Order, String> nameColumn_hist;
  @FXML TableColumn<Order, Integer> quantityColumn_hist;
  @FXML TableColumn<Order, Integer> numColumn_hist;
  @FXML TableColumn<Order, Boolean> statusColumn_hist;
  @FXML TableColumn<Order, String> dateColumn_hist;
  //set sale tab
  @FXML TextField product_setSale;
  @FXML DatePicker startDatePicker = new DatePicker();
  @FXML DatePicker endDatePicker = new DatePicker();
  @FXML TextField startDate;
  @FXML TextField endDate;
  @FXML TextField discount;

  //add product
  @FXML TextField name;
  @FXML TextField upc;
  @FXML TextField quantity;
  @FXML TextField section;
  @FXML TextField price;
  @FXML TextField cost;
  @FXML TextField threshold;
  @FXML TextField aisle;
  @FXML TextField shelve;
  //check inventory
  @FXML TextField productField;
  @FXML TableView<Product> tableView;
  @FXML TableColumn<Product, String> nameColumn;
  @FXML TableColumn<Product, Integer> quantityColumn;
  @FXML TableColumn<Product, String> locationColumn;
  @FXML TableColumn<Product, Integer> priceColumn;
  @FXML TableColumn<Product, Long> upcColumn;

  @FXML Button addFromFile;
  private ObservableList<Order> orders = FXCollections.observableArrayList();
  private ObservableList<Order> hist = FXCollections.observableArrayList();
  private ObservableList<Product> products = FXCollections.observableArrayList();
  private ObservableList<String> sale = FXCollections.observableArrayList();
  private ObservableList<String> rev = FXCollections.observableArrayList();
  private ObservableList<String> amt = FXCollections.observableArrayList();
  private ObservableList<String> dat = FXCollections.observableArrayList();

  public ManagerSceneController(){
      super();
  }

  public void seePendingOrders(){
    ObservableList<Order> pendingProducts = FXCollections.observableArrayList();
    for(Order order: orderSystem.getOrderList()) {
      if (order.getPending()) {
        pendingProducts.add(order);
      }}
      pendingOrders.setItems(pendingProducts);
    }

  public void seeOrderHistory(){
    hist.clear();
    hist.addAll(orderSystem.getOrderList());
    orderHistory.setItems(hist);
  }

  public void deleteArrivedOrder(){
    try {
      int orderNum = Integer.parseInt(orderNumber.getText());
      orderSystem.hasArrived(orderNum);
      serializeOrderSystem();
      if (orderSystem.searchOrder(orderNum) != null) {
        Order arrivedProduct = orderSystem.searchOrder(orderNum);
        receiveOrder(arrivedProduct.getProductName(), arrivedProduct.getQuantity());
      }
      seeOrderHistory();
      seePendingOrders();
    }catch (NumberFormatException e){
      System.out.println("Products can't be found.");
    }
  }

  private void checkDate(){
    date.setText(accounting.getDate());
  }
  private void checkCost(){
    amt.clear();
    amt.add("$" + accounting.getTotalCost());
    costPrice.setItems(amt);
  }

  private void checkProfit(){
    rev.clear();
    if(accounting.realTimeProfit()>0){
      rev.add("$" + accounting.realTimeProfit());
    }else{
      rev.add("-$" + accounting.realTimeProfit());
    }
    profit.setItems(rev);
  }

  private void checkRevenue(){
    sale.clear();
    sale.add("$" + accounting.getTotalRevenue());
    revenue.setItems(sale);
  }

  public void refreshSalesPage(){
    checkRevenue();
    checkProfit();
    checkCost();
    checkDate();;
  }

  /**
   * Receive some product
   * @param productName the name of the product
   * @param quantity the quantity of the product
   */
  private void receiveOrder(String productName, int quantity){
    Product product = inventory.findByName(productName);
    if(!inventory.productList.contains(product)){
      inventory.productList.add(product);
    }
    product.setQuantity(product.getQuantity() + quantity);
    serializeInventory();
  }

  public void setSale() throws ParseException{
    Product product = inventory.findByName(product_setSale.getText());
    String discount = this.discount.getText();
    LocalDate starDate = startDatePicker.getValue();
    LocalDate endDate = endDatePicker.getValue();
    product.getPriceSystem().setSaleByPriceDate(discount, starDate, endDate);
    serializeInventory();
  }


  public void showInventory(){
    products.clear();
    this.products.addAll(inventory.getProductList());
  }

  public void searchProduct(){
    products.clear();
    if(!productField.getText().equals("")) {
      ArrayList<Product> products = inventory.findByPartialName(productField.getText());
      if(!products.isEmpty()) {
        this.products.addAll(products);
      }
    }
  }

  public void addProduct(){
    long newUpc = Long.parseLong(upc.getText());
    String newName = name.getText();
    int newQuantity = Integer.parseInt(quantity.getText());
    String newSection = section.getText();
    int newPrice = Integer.parseInt(price.getText());
    int newCost = Integer.parseInt(cost.getText());
    int newThreshold = Integer.parseInt(threshold.getText());
    int newAisle = Integer.parseInt(aisle.getText());
    int newShelf = Integer.parseInt(shelve.getText());
    int[] newLocation = {newAisle, newShelf};
    inventory.addProducts(newUpc, newName, newQuantity, newSection, newPrice, newCost, newThreshold, newLocation);
    serializeInventory();
  }

  public void managerLogout() throws IOException{
    Parent loginPage = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
    Scene loginScene = new Scene(loginPage);
    Stage stage = (Stage) revenue.getScene().getWindow();
    stage.setScene(loginScene);
    stage.show();
  }

  public void addNewItemsFromFile(){
    mainStage = (Stage) name.getScene().getWindow();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("CSV Files", "*.csv"));
    File selectedFile = fileChooser.showOpenDialog(mainStage);
    if (selectedFile != null) {
      String path = selectedFile.getAbsolutePath();
      try{
        Inventory newInventory = new Inventory(path);
        inventory.mergeWithInventory(newInventory);
        serializeInventory();
      }catch (IOException e){
        System.out.println("Invalid new inventory file");
      }
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    nameColumn_order.setCellValueFactory(new PropertyValueFactory<>("productName"));
    quantityColumn_order.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    numColumn_order.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
    dateColumn_order.setCellValueFactory(new PropertyValueFactory<>("date"));
    pendingOrders.setItems(orders);

    nameColumn_hist.setCellValueFactory(new PropertyValueFactory<>("productName"));
    quantityColumn_hist.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    numColumn_hist.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
    statusColumn_hist.setCellValueFactory(new PropertyValueFactory<>("pending"));
    dateColumn_hist.setCellValueFactory(new PropertyValueFactory<>("date"));
    orderHistory.setItems(hist);

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    upcColumn.setCellValueFactory(new PropertyValueFactory<>("UPC"));
    tableView.setItems(products);
  }
}
