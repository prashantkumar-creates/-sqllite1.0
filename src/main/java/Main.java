import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args){
    if (args.length < 2) {
      System.out.println("Missing <database path> and <command>");
      return;
    }

    String databaseFilePath = args[0];
    String command = args[1];

    switch (command) {
      case ".dbinfo" -> {
        try {
       //   byte[] header = Files.readAllBytes(Path.of(databaseFilePath));
          ByteBuffer fileContents = ByteBuffer
							.wrap(Files.readAllBytes(Path.of(databaseFilePath)))
							.order(ByteOrder.BIG_ENDIAN);

        
         int pageSize = fileContents.position(16).getShort() & 0xFFFF;
				
					System.out.printf("database page size: %d\n", pageSize);
					ByteBuffer firstPage = fileContents.position(100);
					int pageType = firstPage.get() & 0x00FF;
					//leaf b-tree page
					assert pageType == 0x0d;
					short freeblocks = firstPage.getShort();
					short cellCounts = firstPage.getShort();
					System.out.printf("number of tables: %d\n", cellCounts);
        } catch (IOException e) {
          System.out.println("Error reading file: " + e.getMessage());
        }
      }
      default -> System.out.println("Missing or invalid command passed: " + command);
    }
  }
}
