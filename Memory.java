import com.sun.tools.corba.se.idl.constExpr.Or;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by yingchengluo on 2017-08-08.
 */
public class Memory {

  /**
   * Writes the students to file at filePath.
   * @param filePath the file to write the records to
   * @throws IOException
   */
  public void saveToFile(Object stuff, String filePath) throws IOException {

    OutputStream file = new FileOutputStream(filePath);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    output.writeObject(stuff);
    output.close();
  }

  public void initializeMemory(String pathToInventory, String pathToOrderSystem, String pathToAccounting,
      String pathToUserBase) throws IOException{
    Inventory inventory = new Inventory(pathToInventory);
    OrderSystem orderSystem = new OrderSystem(pathToOrderSystem);
    Accounting accounting = new Accounting(pathToAccounting);
    UserBase userBase = new UserBase(pathToUserBase);
    saveToFile(inventory, "./inventory.ser");
    saveToFile(orderSystem, "./orderSystem.ser");
    saveToFile(accounting, "./accounting.ser");
    saveToFile(userBase, "./userBase.ser");
  }

  public Inventory deserializeInventory(String path) throws ClassNotFoundException {
    try {
      InputStream file = new FileInputStream(path);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      //deserialize the Map
      Inventory inventory = (Inventory) input.readObject();
      input.close();
      return inventory;
    } catch (IOException ex) {
      System.out.println("Can't read from Inventory input");
    }
    return null;
  }

  public OrderSystem deserializeOrderSystem(String path) throws ClassNotFoundException{
    try {
      InputStream file = new FileInputStream(path);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      //deserialize the Map
      OrderSystem orderSystem = (OrderSystem) input.readObject();
      input.close();
      return orderSystem;
    } catch (IOException ex) {
      System.out.println("Can't read OrderSystem input");
    }
    return null;
  }

  public Accounting deserializeAccounting(String path) throws ClassNotFoundException{
    try {
      InputStream file = new FileInputStream(path);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      //deserialize the Map
      Accounting accounting = (Accounting) input.readObject();
      input.close();
      return accounting;
    } catch (IOException ex) {
      System.out.println("Can't read from input");
    }
    return null;
  }

  public UserBase deserializeUserBase(String path) throws ClassNotFoundException{
    try {
      InputStream file = new FileInputStream(path);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      //deserialize the Map
      UserBase userBase = (UserBase) input.readObject();
      input.close();
      return userBase;
    } catch (IOException ex) {
      System.out.println("Can't read from input");
    }
    return null;
  }

  public static void main(String args[]){
    Memory memory = new Memory();
    UserBase userBase = new UserBase("/Users/oliviaccy/IdeaProjects/group_0130/ProjectPhase1/Users.csv");
    try{
      memory.saveToFile(userBase, "./userBase.ser");

    }catch(IOException r){

    }
  }

}

