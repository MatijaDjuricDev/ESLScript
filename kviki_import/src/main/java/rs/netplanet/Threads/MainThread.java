package rs.netplanet.Threads;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import rs.netplanet.Models.ItemModel;
import rs.netplanet.Services.BowtzAPIService;
import rs.netplanet.Services.FTPService;
import rs.netplanet.Services.FileParser;
import rs.netplanet.Services.FilesService;

public class MainThread extends TimerTask {

    FTPService ftpService;
    FilesService filesService;
    BowtzAPIService bowtzAPIService;
    FileParser fileParser;
    File mainFile, lastFile;
    List<ItemModel> differentItems;
    String fileName = "Item_.txt";

    public MainThread() {
        // create ftp service-connect to ftp, create local services
        ftpService = new FTPService("esl.netplanet.rs", "kviki@esl.netplanet.rs", "7GybydK2S64yGbZ6BxNa");
        filesService = new FilesService();
        bowtzAPIService = new BowtzAPIService("dbae5ab7f641392d13b6de1866c212d9", "Kviki 8",
                "Kviki");
        // BowtzAPIService bowtzAPIService = new
        // BowtzAPIService("dbae5ab7f641392d13b6de1866c212d9", "Kviki 8",
        // "Kviki");
        fileParser = new FileParser();
    }

    void findDifferentItems() {
        // parse file to ItemModels
        final Map<String, ItemModel> currentItems = fileParser.parseToItems(mainFile);
        final Map<String, ItemModel> lastItems = fileParser.parseToItems(lastFile);

        // different items
        differentItems = new ArrayList<>();

        // compare each item
        currentItems.forEach((itemCode, item) -> {
            try {
                ItemModel lastItem = lastItems.get(itemCode);

                // if anything is different from last item
                if (!lastItem.getCategory().equals(item.getCategory()) ||
                        !lastItem.getCenovnik().equals(item.getCenovnik()) ||
                        !lastItem.getCode().equals(item.getCode()) ||
                        !lastItem.getItemName().equals(item.getItemName()) ||
                        !lastItem.getItemPackage().equals(item.getItemPackage()) ||
                        !lastItem.getPrice().equals(item.getPrice()) ||
                        !lastItem.getQuantityUnit().equals(item.getQuantityUnit()) ||
                        !lastItem.getSpecial().equals(item.getSpecial()) ||
                        !lastItem.getStaraCena().equals(item.getStaraCena()) ||
                        !lastItem.getStock().equals(item.getStock()) ||
                        !lastItem.getUnitPrice().equals(item.getUnitPrice()) ||
                        !lastItem.getVaziDo().equals(item.getVaziDo()) ||
                        !lastItem.getVaziOd().equals(item.getVaziOd())) {
                    // put it in a list for API update
                    differentItems.add(item);
                }
            } catch (NullPointerException e) {
                differentItems.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    void manageFiles() {
        // name a file with timestamp
        String destinationFileName = "processed/Item_"
                + (new Date()).toString().replaceAll(" ", "_").replaceAll(":", "-") + ".txt";

        File destinationCopy = new File(destinationFileName);

        try {
            // copy file to processed
            Files.copy(mainFile.toPath(), destinationCopy.toPath());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            // find last file processed
            lastFile = filesService.getTheLastestFile();

            // check if file changed on ftp
            System.out.println("\nChecking last updated time");
            if (ftpService.checkIfFileChanged(fileName)) {
                System.out.println("Remote file changed");
                System.out.println("Downloading file");
                // download file and close ftp
                mainFile = ftpService.downloadFile(fileName);

                findDifferentItems();

                if (differentItems.size() > 0) {
                    System.out.println("Found differences, calling API");
                    // send those items through API
                    bowtzAPIService.sendItems(differentItems);
                } else {
                    System.out.println("No differences found.");
                }

                manageFiles();
            } else {
                System.out.println("Remote file not changed");
            }
        } catch (FileNotFoundException e) {
            // if there was no files processed

            // download file and close ftp
            mainFile = ftpService.downloadFile(fileName);

            // send all items to API
            System.out.println("Sending whole file");
            bowtzAPIService.sendWholeFile(mainFile, fileParser);

            manageFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
