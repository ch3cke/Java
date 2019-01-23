package FileTransfer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileSender {
    private static ArrayList<String> fileList = new ArrayList<String>();

    private String sendFilePath = "E://writeup";

    /**
     * 带参数的构造器，设定传送的文件夹
     *
     * @param filePath
     */

    public FileSender(String filePath) {
        getFilePath(filePath);
    }

    /**
     * 不带参数的构造器。使用默认的传送文件的文件夹
     */
    public FileSender() {
        getFilePath(sendFilePath);
    }

    private static Runnable sendFile(final String filePath) {
        return new Runnable() {
            private Socket socket = null;
            private String ip = "127.0.0.1";
            private int port = 12987;

            @Override
            public void run() {
                System.out.println("开始发送文件:" + filePath);
                File file = new File(filePath);
                if (createConnection()) {
                    int buffSize = 8192;
                    byte[] buf = new byte[buffSize];
                    try {
                        DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        dos.writeUTF(file.getName());
                        dos.flush();
                        dos.writeLong(file.length());
                        dos.flush();

                        int read = 0;
                        int passedlen = 0;
                        long length = file.length();//发送文件的长度
                        while ((read = fis.read(buf)) != -1) {
                            passedlen += read;
                            System.out.println("已经完成文件 [" + file.getName() + "]百分比: " + passedlen * 100L / length + "%");
                            dos.write(buf, 0, read);
                        }
                        dos.flush();
                        fis.close();
                        dos.close();
                        socket.close();
                        System.out.println("文件" + filePath + "传输完成！");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            private boolean createConnection() {
                try {
                    socket = new Socket(ip, port);
                    System.out.println("连接成功");
                    return true;

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
    }

    public static void main(String[] args) {
        new FileSender().service();
    }

    public void service() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Vector<Integer> vector = getRandom(fileList.size());
        for (Integer integer : vector) {
            String filePath = fileList.get(integer.byteValue());
            executorService.execute(sendFile(filePath));
        }
    }

    private void getFilePath(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                getFilePath(files[i].getAbsolutePath());
            } else {
                fileList.add(files[i].getAbsolutePath());
            }
        }
    }

    private Vector<Integer> getRandom(int size) {
        Vector<Integer> v = new Vector<Integer>();
        Random r = new Random();
        boolean b = true;
        while (b) {
            int i = r.nextInt(size);
            if (!v.contains(i)) {
                v.add(i);
            }
            if (v.size() == size) {
                b = false;
            }
        }
        return v;
    }
}
