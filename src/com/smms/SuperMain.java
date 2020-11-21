package com.smms;

/*
 * 精简版:
 * liteStart();
 *
 * 完整版:
 * start();
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author setusb
 * @version 1.0
 * @date 2020/11/5 18:47
 */
public class SuperMain {
    final public static void main(String[] args) {
        SuperDao superDao = new SuperDao();
        if (superDao.window) {
            if (superDao.update) {
                update();
            } else {
                System.out.println("更新日志功能已关闭，建议开启！");
            }
            System.out.println("请关闭窗口并选择 [是] 否则无法进入主程序！");
            superDao.graphicsWindow();
        } else {
            if (superDao.update) {
                update();
            } else {
                System.out.println("更新日志功能已关闭，建议开启！");
            }
            if (superDao.start) {
                superDao.start();
            } else {
                superDao.liteStart();
            }
        }
    }

    /**
     * 更新日志 2020.11.13
     */
    final public static void update() {
        File file = new File("update.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("更新文件创建成功！");
            }
        } else {
            System.out.println("更新文件已存在，请查看更新日志！");
        }
        try {
            final String updateStr = "当前更新内容：\n" +
                    "\t你可以在jar中找到config.properties，修改功能\n" +
                    "\t添加了窗口提示，在启用时启动窗口确认模块，默认关闭\n" +
                    "\t你可以使用文件存储数据，保证数据的不丢失，默认开启\n" +
                    "\t修复部分bug，将方法限定为final优化性能";
            FileWriter fw = new FileWriter("update.txt", false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(updateStr);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
