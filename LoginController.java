import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;
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

public class LoginController extends Controller{

  @FXML private TextField usernameField;
  @FXML private TextField passwordField;
  @FXML private Label warningLabel;

  public LoginController() {
    super();
  }




  public void validatePassword() throws IOException {
    String userName = usernameField.getText();
    int password = Integer.parseInt(passwordField.getText());
    Object user = userBase.checkUser(userName, password);
    if(user == null){
      warningLabel.setText("Invalid user name or password");
    }else{
      User theUser = (User) user;
      String position = theUser.getPosition();
      if(position.equals("Cashier")){
        Parent cashierPage = FXMLLoader.load(getClass().getResource("Cashier.fxml"));
        Scene cashierScene = new Scene(cashierPage);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(cashierScene);
        stage.show();
      }else if(position.equals("Reshelver")){
        Parent cashierPage = FXMLLoader.load(getClass().getResource("Reshelver.fxml"));
        Scene cashierScene = new Scene(cashierPage);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(cashierScene);
        stage.show();
      }else if(position.equals("Manager")){
        Parent cashierPage = FXMLLoader.load(getClass().getResource("Manager.fxml"));
        Scene cashierScene = new Scene(cashierPage);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(cashierScene);
        stage.show();
      }
    }
  }

}
