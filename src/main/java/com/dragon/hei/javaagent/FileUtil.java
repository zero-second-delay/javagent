package com.dragon.hei.javaagent;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {


    public static void createClassFile(String className, byte[] content) {

        try {
            className = "/Users/lilong/Documents/work/code/test/javagent/target/" + className+".class";

            File f = new File(className);
//            if(!f.exists()){
//                f.createNewFile();
//            }
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(content);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
