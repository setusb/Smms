package com.smms;

import java.io.*;

/*
 * 创建一个文件，user.txt
 * 将存储数组中的数据仍然存储在数组中
 * 在进程关闭时，将数组内的数据循环并依此写入到文本文件中
 * ！当然，这样有风险，所以得设置一个额外条件，每一次写入完整的数据，都将自动执行一次保存，并在最后进行判断（此功能纯属痴人说梦）
 * 上一个功能无法实现是因为我得考虑修改功能和添加功能，如果每一次都进行判断执行，那么会变得非常繁琐，主要是懒！
 * 当下次启动此程序时，将先判断user.txt的文件是否存在，如果存在则执行读取功能，讲读取功能写入数组中，如果不存在则提醒
 */

/**
 * @author setusb
 * @version 1.0
 * @date 2020/11/12 9:49
 */

public class SuperFile {
    /**
     * 创建文件
     */
    public void createFile(String fileName) {
        //user为用户数据存储文件
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println(file.getName() + "创建成功！");
                System.out.println("已开启文件存储，正在读取文件内容！");
            }
        } else {
            System.out.println("已开启文件存储，正在读取文件内容！");
        }
    }

    /**
     * 写入文件
     */
    public void writeFile(SuperMan sm, String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(sm.getName() + " " + sm.getPassword() + " " + sm.getSex() + " " + sm.getAge() + " " + sm.getBirthday() + " " + sm.getCardId() + " " + sm.getJoiningTime() + " " + sm.getScore() + "\n");
        bw.close();
    }

    /**
     * 清空文件
     */
    public void clearFile(String fileName) {
        File file = new File(fileName);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 本来打算写方法调用的，但是发现，无论如何，都无法实现return全部sm数据出去
     * 只能单条return出去，所以废弃，但，大体思路我觉得是正确的，并且，在SuperDao中已经测试成功
     * 目前，此条代码仅用于纪念，我浪费的一个小时！
     */
/*    public SuperMan readFile() throws IOException {
        FileInputStream fis = new FileInputStream("user.txt");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bfr = new BufferedReader(isr);
        ArrayList<SuperMan> listr = new ArrayList<>();
        String strTmp = "";
        while ((strTmp = bfr.readLine()) != null) {
            SuperMan sm = new SuperMan();
            String[] str = strTmp.split(" ");
            System.out.println(str[0] + " " + str[1] + " " + str[2] + " " + str[3] + " " + str[4] + " " + str[5] + " " + str[6]);
            sm.setName(str[0]);
            sm.setPassword(str[1]);
            sm.setSex(str[2]);
            sm.setAge(Integer.parseInt(str[3]));
            sm.setBirthday(str[4]);
            sm.setCardId(Integer.parseInt(str[5]));
            sm.setJoiningTime(str[6]);
            listr.add(sm);
        }
        for (SuperMan smn : listr) {
            System.out.println(smn);
        }
        bfr.close();
        return null;
    }*/
}
