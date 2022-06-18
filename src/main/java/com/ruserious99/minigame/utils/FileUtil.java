package com.ruserious99.minigame.utils;

import java.io.*;

public class FileUtil {

    public static void deleteWorldFolder(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] strFiles = file.listFiles();
                if(strFiles == null){return;}
                for (File strFilename : strFiles) {
                    strFilename.delete();
                }
            }
            file.delete();
        }
    }


    public static void copy(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if(!targetLocation.exists()){
                targetLocation.mkdirs();
            }

            String[] files = sourceLocation.list();
            if(files == null)return;

            for (String file :files) {
               File newSource = new File(sourceLocation, file);
               File newTarget = new File(targetLocation, file);

               copy(newSource, newTarget);
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
}