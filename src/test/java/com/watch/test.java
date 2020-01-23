package com.watch;

import java.io.File;

public class test {
    public static void main(String[] args) throws Exception {
        File file=new File("web.xml");
        file.createNewFile();
    }
}
