package com.thingple.version.uuid;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * Created by lism on 2017/12/7.
 */

public class GlobalKeyHandler {

    public UUID get() {
        File file = getFile(getLocation());
        String content = read(file);
        return new UUID(content);
    }

    public void save(UUID uuid) {
        File file = getFile(getLocation());
        write(uuid.toString(), file);
    }

    private File getLocation() {
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        prepareFolder(root);
        File appFolder = new File(root, "x6b3");
        prepareFolder(appFolder);
        return appFolder;
    }

    private void prepareFolder(File folder) {
        if (!folder.exists() || !folder.isDirectory()) {
            boolean res = folder.mkdir();
            Log.d(getClass().getSimpleName() + "#prepareFolder", "创建文件夹:" + (res ? "成功" : "失败"));
        }
    }

    private File getFile(File folder) {
        String name = "fdssss";
        return new File(folder, name);
    }

    private String read(File file) {
        String content = "";
        byte[] data = readByte(file);
        if (data != null) {
            byte temp;
            for (int i = 0; i < data.length; i++) {
                temp = data[i];
                data[i] = (byte) ~temp;
            }
            content = new String(data);
        }
        return content;
    }

    private void write(String content, File file) {
        byte[] data = content.getBytes();
        byte temp;
        for (int i = 0; i < data.length; i++) {
            temp = data[i];
            data[i] = (byte) ~temp;
        }
        writeBytes(data, file);
    }

    private void writeBytes(byte[] data, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file, false);
            os.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] readByte(File file) {
        byte[] res = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)file.length());
        BufferedInputStream in = null;
        try{
            in = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len;
            while(-1 != (len = in.read(buffer,0,buf_size))){
                bos.write(buffer,0,len);
            }
            res = bos.toByteArray();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if (in != null) {
                    in.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}
