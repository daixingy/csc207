import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Created by yingchengluo on 2017-08-08.
 */
public abstract class Controller implements Initializable{

  CsvWriter csvWriter = new CsvWriter();
  CsvReader csvReader = new CsvReader();
  FileChooser fileChooser = new FileChooser();
  Memory memory = new Memory();

  private String pathToUserBase;
  private String pathToInventory;
  private String pathToOrderSystem;
  private String pathToAccounting;

  Inventory inventory;
  OrderSystem orderSystem;
  Accounting accounting;
  UserBase userBase;

  Stage mainStage = new Stage();

  public Controller() {
    File inventoryMemory = new File("./inventory.ser");
    File orderSystemMemory = new File("./orderSystem.ser");
    File accountingMemory = new File("./accounting.ser");
    File userBaseMemory = new File("./userBase.ser");
    if (inventoryMemory.exists() && orderSystemMemory.exists() && accountingMemory.exists()
        && userBaseMemory.exists()) {
      try {
        inventory = memory.deserializeInventory("./inventory.ser");
        orderSystem = memory.deserializeOrderSystem("./orderSystem.ser");
        accounting = memory.deserializeAccounting("./accounting.ser");
        userBase = memory.deserializeUserBase("./userBase.ser");
      } catch (ClassNotFoundException e) {
        System.out.println("Not all memory has been initialized");
      }
    } else {
      String rootPath = "./ProjectPhase1/";
      pathToAccounting = rootPath + "AccountBalance.csv";
      pathToInventory = rootPath + "inventory.csv";
      pathToOrderSystem = rootPath + "OrderList.csv";
      pathToUserBase = rootPath + "Users.csv";
      try {
        memory
            .initializeMemory(pathToInventory, pathToOrderSystem, pathToAccounting, pathToUserBase);
        inventory = memory.deserializeInventory("./inventory.ser");
        orderSystem = memory.deserializeOrderSystem("./orderSystem.ser");
        accounting = memory.deserializeAccounting("./accounting.ser");
        userBase = memory.deserializeUserBase("./userBase.ser");
      } catch (IOException e) {
        System.out.println("Invalid configuration files");
      } catch (ClassNotFoundException e) {
        System.out.println("You're doomed");
      }
    }
  }

  public void setPathToInventory(){
    fileChooser.setTitle("Open Inventory CSV File");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("CSV Files", "*.csv"));
    File selectedFile = fileChooser.showOpenDialog(mainStage);
    if (selectedFile != null) {
      pathToInventory = selectedFile.getAbsolutePath();
      try {
        inventory = new Inventory(pathToInventory);
      } catch (IOException e) {
        System.out.println("Invalid path");
      }
    }
  }

  public void setPathToUserBase(){
    fileChooser.setTitle("Open User DataBase CSV File");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("CSV Files", "*.csv"));
    File selectedFile = fileChooser.showOpenDialog(mainStage);
    if (selectedFile != null) {
      pathToUserBase = selectedFile.getAbsolutePath();
      userBase = new UserBase(pathToUserBase);
    }
  }

  public void setPathToOrderSystem(){
    fileChooser.setTitle("Open User Order System CSV File");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("CSV Files", "*.csv"));
    File selectedFile = fileChooser.showOpenDialog(mainStage);
    if (selectedFile != null) {
      pathToOrderSystem = selectedFile.getAbsolutePath();
      orderSystem = new OrderSystem(pathToOrderSystem);
    }
  }

  public void setPathToAccountring(){
    fileChooser.setTitle("Open User Accounting CSV File");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("CSV Files", "*.csv"));
    File selectedFile = fileChooser.showOpenDialog(mainStage);
    if (selectedFile != null) {
      pathToAccounting = selectedFile.getAbsolutePath();
      accounting = new Accounting(pathToAccounting);
    }
  }

  public void serializeInventory(){
    try {
      memory.saveToFile(inventory, "./inventory.ser");
    }catch (IOException e){
      System.out.println("Invalid inventory.ser path");
    }
  }

  public void serializeOrderSystem(){
    try {
      memory.saveToFile(orderSystem, "./orderSystem.ser");
    }catch (IOException e){
      System.out.println("Invalid orderSystem.ser path");
    }
  }

  public void serializeAccountBalance(){
    try{
      memory.saveToFile(accounting,"./accounting.ser");
    } catch (IOException e){
      System.out.println("Invalid accounting.ser path");
    }
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {}

}
