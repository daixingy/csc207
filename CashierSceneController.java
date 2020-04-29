import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Created by yingchengluo on 2017-08-04.
 */
public class CashierSceneController  extends Controller implements Initializable {
  @FXML Button searchButton;
  @FXML Button scanFromFile;
  @FXML TextField productField;
  @FXML TableView<Product> tableView;
  @FXML TableColumn<Product, String> nameColumn;
  @FXML TableColumn<Product, Integer> quantityColumn;
  @FXML TableColumn<Product, String> locationColumn;
  @FXML TableColumn<Product, Integer> priceColumn;
  @FXML TableColumn<Product, Long> upcColumn;

  @FXML Button addProduct;
  @FXML Button checkOut;
  @FXML Label totalPrice;
  @FXML TextField productField_shopping;
  @FXML TableView<Product> shoppingList;
  @FXML TableColumn<Product, String> nameColumn_shopping;
  @FXML TableColumn<Product, Integer> priceColumn_shopping;
  @FXML Button logout;

  private FileHandler fileHandler = new FileHandler();
  private boolean hasCheckedOut = false;
  private ObservableList<Product> products = FXCollections.observableArrayList();
  private ObservableList<Product> products_shopping = FXCollections.observableArrayList();

  public CashierSceneController(){
      super();
    }

  public void scanFromFile() throws IOException{
    mainStage = (Stage) searchButton.getScene().getWindow();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Text Files", "*.txt"),
        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
        new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
        new ExtensionFilter("All Files", "*.*"));
    File selectedFile = fileChooser.showOpenDialog(mainStage);
    if (selectedFile != null) {
      String path = selectedFile.getAbsolutePath();
      ArrayList<Product> productArrayList = inventory.findMultipleByUpc(fileHandler.handlePath(path));
      products_shopping.addAll(productArrayList);
    }
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
    if(!hasCheckedOut) {
      Long upc = Long.valueOf(productField_shopping.getText());
      Product product = inventory.findByUpc(upc);
      products_shopping.add(product);
    }
  }

  public void deleteProduct(){
    if(!hasCheckedOut) {
      int shoppingListSize = products_shopping.size();
      if (shoppingListSize != 0) {
        products_shopping.remove(shoppingListSize - 1);
      }
    }
  }

  public void checkOut() throws ParseException{
    int totalPrice = 0;
    for(Product product: products_shopping){
      totalPrice = totalPrice + product.getPrice();
    }
    this.totalPrice.setText(String.valueOf(totalPrice));
    hasCheckedOut = !hasCheckedOut;
  }

  public void paid() throws ParseException{
    if(hasCheckedOut){
      ArrayList<Product> newProducts = new ArrayList<>();
      for(Product product: products_shopping){
        product.setQuantity(product.getQuantity() - 1);
        newProducts.add(product);
        if(product.getQuantity() == product.getThreshold()){
          orderSystem.order(product);
          serializeOrderSystem();
        }

        LocalDate d = LocalDate.now();
        accounting.update(product.getPrice(), product.getCost(), d.toString());
//        int[] accBalance = new int[2];
//        accBalance[0] = product.getPrice();
//        accBalance[1] = product.getCost();
//        csvWriter.writeCsvAccountBalance(accBalance, pathToAccounting);
        serializeAccountBalance();
      }
      serializeInventory();
      products_shopping.clear();
      totalPrice.setText("");
      hasCheckedOut = !hasCheckedOut;
    }
  }

  public void cashierLogout() throws IOException{
    Parent loginPage = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
    Scene loginScene = new Scene(loginPage);
    Stage stage = (Stage) productField.getScene().getWindow();
    stage.setScene(loginScene);
    stage.show();
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    upcColumn.setCellValueFactory(new PropertyValueFactory<>("UPC"));
    tableView.setItems(products);

    nameColumn_shopping.setCellValueFactory(new PropertyValueFactory<>("name"));
    priceColumn_shopping.setCellValueFactory(new PropertyValueFactory<>("price"));
    shoppingList.setItems(products_shopping);
    try {
      inventory = memory.deserializeInventory("./inventory.ser");
    }catch(ClassNotFoundException e){
      System.out.println("Invalid class");
    }
  }
}
