package rs.netplanet.Services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTPClient;

public class FTPService {

    FTPClient ftpClient;
    String host, username, password;

    public FTPService(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.ftpClient = new FTPClient();
    }

    void connectAndLogIn() {
        try {
            this.ftpClient.connect(host);
            this.ftpClient.login(username, password);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void logoutAndDisconnect() {
        try {
            this.ftpClient.logout();
            this.ftpClient.disconnect();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfFileChanged(String fileName) {
        Date modificationTime = null, localModificationTime = null;
        try {
            connectAndLogIn();
            String modificationTimeString = this.ftpClient.getModificationTime(fileName);
            logoutAndDisconnect();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            File file = new File(fileName);
            try {
                modificationTime = new Date(
                        dateFormat.parse(modificationTimeString).getTime());
                localModificationTime = new Date(file.lastModified());
                System.out.println("Local modification time: " + localModificationTime);
                System.out.println("Remote modification time: " + modificationTime);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (modificationTime.after(localModificationTime)) {
            return true;
        } else {
            return false;
        }
    }

    public File downloadFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            connectAndLogIn();
            this.ftpClient.retrieveFile("/" + fileName, fileOutputStream);
            String modificationTimeString = this.ftpClient.getModificationTime(fileName);
            logoutAndDisconnect();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                file.setLastModified(dateFormat.parse(modificationTimeString).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
