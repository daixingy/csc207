import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
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
 * Created by Ashley on 17/8/7.
 */
public class ReshelverSceneController extends Controller implements Initializable {

  @FXML Button Check;
  @FXML Button Change;
  @FXML TextField product1;
  @FXML TextField product2;
  @FXML TextField newLocation;
  @FXML TableColumn<Product, String> nameColumn;
  @FXML TableColumn<Product, int[]> locationColumn;
  @FXML TableView<Product> tableView;
  @FXML Button logout;

  private ObservableList<Product> products = FXCollections.observableArrayList();
  private int[] new_Location = new int[2];


  public ReshelverSceneController(){
    super();
  }

  public void checkLocation(){
    products.clear();
    ArrayList<Product> products = inventory.findByPartialName(product1.getText());
    if(!products.isEmpty()) {
      this.products.addAll(products);
    }
    }

  public void reshelveProduct(){
    String name = product2.getText();
    Product product = inventory.findByName(name);
    String[] location = newLocation.getText().split(",");
    new_Location[0] = Integer.valueOf(location[0]);
    new_Location[1] = Integer.valueOf(location[1]);
    product.setLocation(new_Location);
    serializeInventory();
  }

  public void reshelverLogout() throws IOException{
    Parent loginPage = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
    Scene loginScene = new Scene(loginPage);
    Stage stage = (Stage) product1.getScene().getWindow();
    stage.setScene(loginScene);
    stage.show();
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    tableView.setItems(products);


  }
}
