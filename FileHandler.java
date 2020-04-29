import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by yingchengluo on 2017-08-07.
 */
public class FileHandler {
    FileHandler(){}

    public ArrayList<Long> handlePath(String path) throws IOException{
      ArrayList<Long> upcList = new ArrayList<>();
      Scanner scanner = new Scanner(new FileInputStream(path));
      while(scanner.hasNextLine()){
        String value = scanner.nextLine();
        upcList.add(Long.valueOf(value));
      }
      return upcList;
    }
}
