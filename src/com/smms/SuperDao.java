package com.smms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

/*
 * 已知问题:
 * - 生日如果按照11//1仍然会通过校验（已修复）2020.11.5
 * - 生日可以不受约束，可以输入13/31，并通过校验（已修复）2020.11.05
 * - 密码可以输入中文（已修复）2020.11.05
 * - 先创建会员信息完毕后无法退出（已修复）2020.11.06
 * - informationRecruitment方法代码行数超过80行无法精简（无法精简） 2020.11.6
 * - 偶尔情况下输入姓名会有延迟（已修复）2020.11.06
 * - 编号重复会重新输入年龄而不会自动重刷编号（已修复）2020.11.06
 * - 生日无法判断2月对于当前年份是否是闰年而判断日份的限制（无法修复）2020.11.12
 * - 无法实现文件类返回值能够返回多个值（已重写）2020.11.12
 * - 写入数据库数据时，会把上一次的数据在写入一次（已修复）2020.11.12
 * - 文件存储功能开启按钮偶尔失效（已修复) 2020.11.12
 * - 无法实现当运行窗口时窗口自动关闭，并启用控制台（无法修复）2020.11.13
 * - 在控制台中运行，创建用户后退出，已几率无法退出（无法修复）[概率问题] 2020.11.13
 * - 在控制台中运行，格式不美观（无法修复）2020.11.17
 *
 * 实现功能:
 * - 在线数据库存储（已实现）2020.11.06
 * - 文本数据库存储（已实现）2020.11.12
 * - Mysql数据库存储（待实现）
 *
 * - 性能选项，提速百分百！2020.11.12
 * - 窗口模块，实现傻瓜式教程！2020.11.13
 * - 积分功能，按老师要求新增积分累计删除添加兑换！2020.11.17
 */

/**
 * @author setusb
 * @version 1.0
 * @date 2020/11/5 18:43
 */

public class SuperDao extends SuperMan {
    /**
     * turnOnFileStorage 开启文件存储功能，默认为开启状态，如需修改，请前往config.properties配置文件中修改 2020.11.12
     * nameFile 文件存储库名字，如需修改，请前往config.properties配置文件中修改 2020.11.12
     * performance 性能，默认3，如需修改，请前往config.properties配置文件中修改 2020.11.12
     * window 窗口提示，在启用时启动窗口确认模块，如需关闭，请前往config.properties配置文件中修改 2020.11.13
     * update 更新日志，在启动时创建一个文本，并告知当前更新的日志，如需关闭，请前往config.properties配置文件中修改 2020.11.13
     * start 启动方式，默认启动完整版，如需修改为精简版，请前往config.properties配置文件中修改 2020.11.17
     */
    boolean turnOnFileStorage = true;
    String nameFile = "databases.txt";
    int performance = 3;
    boolean window = false;
    boolean update = true;
    boolean start = true;
    /**
     * input是输入调用
     * list是在线数据库
     */
    Scanner input = new Scanner(System.in);
    ArrayList<SuperMan> list = new ArrayList<>();

    {
        InputStream in = SuperDao.class.getClassLoader().getResourceAsStream("config.properties");
        Properties prop = new Properties();
        try {
            /*
             * 2020.11.12
             * 这个是连接config.properties配置文件的
             * 可以修改功能详情，请在配置文件中查看
             */
            prop.load(in);
            String a1 = prop.getProperty("turnOnFileStorage");
            String a2 = prop.getProperty("nameFile");
            String a3 = prop.getProperty("performance");
            String a4 = prop.getProperty("window");
            String a5 = prop.getProperty("update");
            String a6 = prop.getProperty("start");
            turnOnFileStorage = Boolean.parseBoolean(a1);
            nameFile = a2;
            performance = Integer.parseInt(a3);
            window = Boolean.parseBoolean(a4);
            update = Boolean.parseBoolean(a5);
            start = Boolean.parseBoolean(a6);
        } catch (IOException e) {
            System.out.println("读取config.properties出现未知错误，请联系开发者！");
        }
        /*
         * 2020.11.12
         * turnOnFileStorage是控制文件存储功能的开关
         * 如果为turn，则执行if中的命令
         * 包括：
         *      - 创建文件
         *      - 读取文件内容
         *      - 将文件内容写入list在线数据库
         *      - 关闭文件流
         */
        if (turnOnFileStorage) {
            SuperFile sf = new SuperFile();
            sf.createFile(nameFile);
            try {
                FileInputStream fis = new FileInputStream(nameFile);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bfr = new BufferedReader(isr);
                ArrayList<SuperMan> listr = new ArrayList<>();
                String strTmp;
                while ((strTmp = bfr.readLine()) != null) {
                    SuperMan sm = new SuperMan();
                    String[] str = strTmp.split(" ");
                    sm.setName(str[0]);
                    sm.setPassword(str[1]);
                    sm.setSex(str[2]);
                    sm.setAge(Integer.parseInt(str[3]));
                    sm.setBirthday(str[4]);
                    sm.setCardId(Integer.parseInt(str[5]));
                    sm.setJoiningTime(str[6]);
                    sm.setScore(Long.parseLong(str[7]));
                    listr.add(sm);
                }
                list.addAll(listr);
                bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("未启用文件存储功能，所有数据将在程序关闭后彻底删除！");
        }
    }

    /**
     * 精简版开始 2020.11.5
     */
    final public void liteStart() {
        boolean a = true;
        while (a) {
            System.out.println("--------------------------【超市管理系统】--------------------------");
            System.out.println("1.会员信息录用 \t2.会员信息查询 \t3.所有会员信息 \t4.查询累计积分");
            System.out.println("5.添加累计积分 \t6.清空累计积分 \t7.兑换累计积分 \t8.退出管理系统");
            System.out.println("-----------------------------------------------------------------");
            System.out.print("请输入对应的编号<1-8>:");
            String options = input.next();
            if (Pattern.matches("[0-9]", options)) {
                switch (options) {
                    case "1":
                        informationRecruitment();
                        break;
                    case "2":
                        memberInformationQuery();
                        break;
                    case "3":
                        allMemberInformation();
                        break;
                    case "4":
                        queryScore();
                        break;
                    case "5":
                        scoreAdd();
                        break;
                    case "6":
                        clearScore();
                        break;
                    case "7":
                        exchangeScore();
                        break;
                    case "8":
                        a = false;
                        if (turnOnFileStorage) {
                            try {
                                writeTxt();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                System.out.println("正在存储数据到文件中，请不要关闭！");
                            }
                        }
                        performanceOptions();
                        break;
                    default:
                        System.out.println("请不要输入无关的编号！");
                        break;
                }
            } else {
                System.out.println("输入的编号只能为1位数并且不能是非数字之外的值！");
            }
        }
    }

    /**
     * 完整版开始 2020.11.6
     */
    final public void start() {
        boolean a = true;
        while (a) {
            System.out.println("--------------------------【超市管理系统】--------------------------");
            System.out.println("1.会员信息录用 \t2.会员信息查询 \t3.所有会员信息 \t4.修改会员密码");
            System.out.println("5.修改会员生日 \t6.注销会员信息 \t7.修改会员编号 \t8.查询累计积分");
            System.out.println("9.添加累计积分 \t10.清空累计积分 \t11.兑换累计积分 \t12.退出管理系统");
            System.out.println("-----------------------------------------------------------------");
            System.out.print("请输入对应的编号<1-12>:");
            String options = input.next();
            if (Pattern.matches("[0-9]*", options)) {
                switch (options) {
                    case "1":
                        informationRecruitment();
                        break;
                    case "2":
                        memberInformationQuery();
                        break;
                    case "3":
                        allMemberInformation();
                        break;
                    case "4":
                        changeMemberPassword();
                        break;
                    case "5":
                        modifyMemberBirthday();
                        break;
                    case "6":
                        cancellationOfMemBerInformation();
                        break;
                    case "7":
                        modifyMemberNumber();
                        break;
                    case "8":
                        queryScore();
                        break;
                    case "9":
                        scoreAdd();
                        break;
                    case "10":
                        clearScore();
                        break;
                    case "11":
                        exchangeScore();
                        break;
                    case "12":
                        a = false;
                        if (turnOnFileStorage) {
                            try {
                                writeTxt();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                System.out.println("正在存储数据到文件中，请不要关闭！");
                            }
                        }
                        performanceOptions();
                        break;
                    default:
                        System.out.println("请不要输入无关的编号！");
                        break;
                }
            } else {
                System.out.println("输入的编号只能为1位数并且不能是非数字之外的值！");
            }
        }
    }

    /**
     * 性能选项 2020.11.12
     * - 因为设计原因，所以考虑到数据安全性
     * - 最低将延迟1秒，这样能保证程序彻底结束并写入文件中
     * - 当然，默认1秒，你可以通过config.properties修改性能速度
     * - 更快的速度可能不更加安全，如果你更在意速度！
     */
    final public void performanceOptions() {
        switch (performance) {
            case 1:
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (turnOnFileStorage) {
                        System.out.println("存储完毕，已关闭");
                    } else {
                        System.out.println("已关闭");
                    }
                }
                break;
            case 2:
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (turnOnFileStorage) {
                        System.out.println("存储完毕，已关闭");
                    } else {
                        System.out.println("已关闭");
                    }
                }
                break;
            case 3:
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (turnOnFileStorage) {
                        System.out.println("存储完毕，已关闭");
                    } else {
                        System.out.println("已关闭");
                    }
                }
                break;
            default:
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (turnOnFileStorage) {
                        System.out.println("存储完毕，已关闭");
                    } else {
                        System.out.println("已关闭");
                    }
                }
                break;
        }
    }

    /**
     * 会员信息录用 2020.11.5
     * - 循环判断编号的while未循环，此问题无法解决，但又不能修复
     * - 原因在于，编号太大，确实很难随机到一样的，所以等同于这个while永远也用不到
     * - 但是，即使这仅仅是确实，还是有一定几率能随机到的，为了解决这个问题，设置了一个while循环判断
     * - 如果移除while循环，可能导致的问题就是，编号随机的和数据库中的相同，但又无法创建一个新的
     * - 就会报错，出问题！
     */
    final public void informationRecruitment() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfAge = new SimpleDateFormat("yyyy");
        boolean is = false;
        SuperMan superMan = new SuperMan();
        /*会员名*/
        System.out.print("请输入创建的会员名:");
        String name = input.next();
        boolean a = false;
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                a = true;
                break;
            }
        }
        if (a) {
            System.out.println("在数据库中已查询到你的用户名重复");
        } else {
            /*密码*/
            superMan.setName(name);
            while (superMan.getName() != null) {
                System.out.print("请输入创建的密码<6-10>:");
                String password = input.next();
                if (Pattern.matches("[A-Za-z0-9]{6,10}$", password)) {
                    superMan.setPassword(password);
                    while (superMan.getPassword() != null) {
                        /*性别*/
                        System.out.print("请输入你的性别<男-女>:");
                        String sex = input.next();
                        if ("男".equals(sex) || "女".equals(sex)) {
                            superMan.setSex(sex);
                            while (superMan.getSex() != null) {
                                /*年龄*/
                                System.out.print("请输入你的年龄<0-999>:");
                                String age = input.next();
                                if (Pattern.matches("[0-9]*$", age) && age.indexOf("0") != 0 && age.length() <= 3) {
                                    int year = Integer.parseInt(sdfAge.format(date));
                                    int yearAge = year - Integer.parseInt(age);
                                    while (true) {
                                        superMan.setAge(Integer.parseInt(age));
                                        /*编号*/
                                        int id = 100000 + (int) (Math.random() * ((1000000 - 100000) + 1));
                                        boolean b = false;
                                        for (SuperMan sm : list) {
                                            if (sm.getCardId() == id) {
                                                b = true;
                                                System.out.println("编号查询重复，请重新输入年龄！");
                                            }
                                        }
                                        if (!b) {
                                            /*生日*/
                                            while (true) {
                                                superMan.setCardId(id);
                                                System.out.print("请输入会员生日<月/日>:");
                                                String birthday = input.next();
                                                if (birthday.length() == 5 && birthday.contains("/")) {
                                                    if (Pattern.matches("^[0-1][0-9][/][0-3][0-9]$", birthday) && birthday.indexOf("/") == 2) {
                                                        int ba = Integer.parseInt(birthday.substring(0, 1));
                                                        int bb = Integer.parseInt(birthday.substring(1, 2));
                                                        int bc = Integer.parseInt(birthday.substring(3, 4));
                                                        int bd = Integer.parseInt(birthday.substring(4, 5));
                                                        boolean be = ba == 1 && bb <= 2 || ba == 0;
                                                        boolean bf = bc == 3 && bd <= 1 || bc <= 2;
                                                        if (be) {
                                                            if (bf) {
                                                                superMan.setBirthday(yearAge + "/" + birthday);
                                                                break;
                                                            } else {
                                                                System.out.println("日份不能超过31，请重新输入！");
                                                            }
                                                        } else {
                                                            System.out.println("月份不能超过12，请重新输入！");
                                                        }
                                                    } else {
                                                        System.out.println("正确的生日格式为[01/01]或[12/31]，请重新输入！");
                                                    }
                                                } else {
                                                    System.out.println("正确的生日格式为[01/01]或[12/31]，请重新输入！");
                                                }
                                            }
                                            superMan.setJoiningTime(sdf.format(date));
                                            superMan.setScore(100);
                                            System.out.println("新会员免费赠送" + superMan.getScore() + "积分已到账！");
                                            list.add(superMan);
                                            clearScreen();
                                            System.out.println("已创建成功，请牢记会员名: " + superMan.getName());
                                            is = true;
                                        }
                                        break;
                                    }
                                } else {
                                    System.out.println("年龄不符合规范，请重新输入！");
                                }
                                if (is) {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("性别不符合规范，请重新输入！");
                        }
                        if (is) {
                            break;
                        }
                    }
                } else {
                    System.out.println("密码不符合规范，请重新输入！");
                }
                if (is) {
                    break;
                }
            }
        }
    }

    /**
     * 会员信息查询 2020.11.5
     */
    final public void memberInformationQuery() {
        boolean a = false;
        System.out.print("请输入会员名:");
        String name = input.next();
        System.out.print("请输入会员密码：");
        String password = input.next();
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                if (sm.getPassword().equals(password)) {
                    System.out.println("ID 姓名 年龄 性别 会员编码 会员生日 入会时间 累计积分");
                    System.out.println(list.indexOf(sm) + "\t\t" + sm.getName() + "\t\t" + sm.getAge() + "\t\t" + sm.getSex() + "\t\t" + sm.getCardId() + "\t\t" + sm.getBirthday() + "\t\t" + sm.getJoiningTime() + "\t\t" + sm.getScore());
                    a = true;
                }
            }
        }
        if (!a) {
            System.out.println("用户名或密码错误！");
        }
        clearScreen();
    }

    /**
     * 所有会员信息查询 2020.11.5
     */
    final public void allMemberInformation() {
        System.out.println("ID 姓名 年龄 性别 会员编码 会员生日 入会时间 累计积分");
        for (SuperMan sm : list) {
            System.out.println(list.indexOf(sm) + "\t\t" + sm.getName() + "\t\t" + sm.getAge() + "\t\t" + sm.getSex() + "\t\t" + sm.getCardId() + "\t\t" + sm.getBirthday() + "\t\t" + sm.getJoiningTime() + "\t\t" + sm.getScore());
        }
        clearScreen();
    }

    /**
     * 修改会员密码 2020.11.6
     */
    final public void changeMemberPassword() {
        boolean a = false;
        System.out.print("请输入会员名:");
        String name = input.next();
        System.out.print("请输入会员密码:");
        String password = input.next();
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                if (sm.getPassword().equals(password)) {
                    while (true) {
                        System.out.print("请输入新密码<6-10>：");
                        String newPassword = input.next();
                        if (newPassword.length() >= 6 && newPassword.length() <= 10 && Pattern.matches("[A-Za-z0-9]*", newPassword)) {
                            sm.setPassword(newPassword);
                            a = true;
                            System.out.println("密码已成功修改，新密码为: " + newPassword);
                            break;
                        } else {
                            System.out.println("密码不符合规则，请重新输入！");
                        }
                    }
                }
            }
        }
        if (!a) {
            System.out.println("用户名或密码错误！");
        }
        clearScreen();
    }

    /**
     * 修改会员生日 2020.11.6
     */
    final public void modifyMemberBirthday() {
        boolean a = false;
        System.out.print("请输入会员名:");
        String name = input.next();
        System.out.print("请输入会员密码:");
        String password = input.next();
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                if (sm.getPassword().equals(password)) {
                    while (true) {
                        System.out.print("请输入修改的生日<月/日>");
                        String newBirthday = input.next();
                        if (newBirthday.length() == 5 && newBirthday.contains("/")) {
                            if (Pattern.matches("^[0-1][0-9][/][0-3][0-9]$", newBirthday) && newBirthday.indexOf("/") == 2) {
                                int nba = Integer.parseInt(newBirthday.substring(0, 1));
                                int nbb = Integer.parseInt(newBirthday.substring(1, 2));
                                int nbc = Integer.parseInt(newBirthday.substring(3, 4));
                                int nbd = Integer.parseInt(newBirthday.substring(4, 5));
                                boolean nbe = nba == 1 && nbb <= 2 || nba == 0;
                                boolean nbf = nbc == 3 && nbd <= 1 || nbc <= 2;
                                if (nbe) {
                                    if (nbf) {
                                        sm.setBirthday(newBirthday);
                                        System.out.println("生日已修改，新生日为: " + newBirthday);
                                        a = true;
                                        break;
                                    } else {
                                        System.out.println("日份不能超过31，请重新输入！");
                                    }
                                } else {
                                    System.out.println("月份不能超过12，请重新输入！");
                                }
                            } else {
                                System.out.println("正确的生日格式为[01/01]或[12/31]，请重新输入！");
                            }
                        } else {
                            System.out.println("正确的生日格式为[01/01]或[12/31]，请重新输入！");
                        }
                    }
                }
            }
        }
        if (!a) {
            System.out.println("用户名或密码错误！");
        }
        clearScreen();
    }

    /**
     * 注销会员信息 2020.11.6
     */
    final public void cancellationOfMemBerInformation() {
        int ine = -1;
        boolean a = false;
        System.out.print("请输入会员名:");
        String name = input.next();
        System.out.print("请输入会员密码:");
        String password = input.next();
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                if (sm.getPassword().equals(password)) {
                    while (true) {
                        System.out.println("你确定注销" + sm.getName() + "会员信息吗？");
                        System.out.print("请输入<yes/on>:");
                        String ins = input.next();
                        if ("yes".equals(ins)) {
                            ine = list.indexOf(sm);
                            a = true;
                            System.out.println("已删除" + sm.getName() + "会员信息！");
                            break;
                        }
                        if ("no".equals(ins)) {
                            a = true;
                            break;
                        } else {
                            System.out.println("只能输入yes或者no！");
                        }
                    }
                }
            }
        }
        if (!a) {
            System.out.println("用户名或密码错误！");
        } else {
            if (ine >= 0) {
                list.remove(ine);
            }
        }
        clearScreen();
    }

    /**
     * 修改会员编号 2020.11.6
     */
    final public void modifyMemberNumber() {
        boolean a = false;
        System.out.print("请输入会员名:");
        String name = input.next();
        System.out.print("请输入会员密码:");
        String password = input.next();
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                if (sm.getPassword().equals(password)) {
                    while (true) {
                        System.out.print("请输入修改的编号<6位编码>:");
                        String id = input.next();
                        if (Pattern.matches("[0-9]*", id) && id.length() == 6) {
                            int ids = Integer.parseInt(id);
                            boolean b = false;
                            for (SuperMan sms : list) {
                                if (sms.getCardId() == ids) {
                                    b = true;
                                    System.out.println("编号查询重复，请重新输入编号！");
                                }
                            }
                            if (!b) {
                                sm.setCardId(ids);
                                System.out.println("编号修改成功，新编号为: " + ids);
                                a = true;
                                start();
                                break;
                            }
                        } else {
                            System.out.println("请按照格式输入修改的编号！");
                        }
                    }
                }
            }
        }
        if (!a) {
            System.out.println("用户名或密码错误！");
        }
        clearScreen();
    }

    /**
     * 清屏 2020.11.6
     */
    final public void clearScreen() {
        System.out.print("\n");
    }

    /**
     * 图形窗口 2020.11.13
     */
    final public void graphicsWindow() {
        JFrame jFrame = new JFrame("温馨提示 - 超市管理系统");
        jFrame.setVisible(true);
        jFrame.setSize(290, 90);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        JTextArea jtf = new JTextArea("此程序无法在窗口运行，请使用控制台启动" +
                "\n如果您不想显示此窗口，请修改jar内部的config文件" +
                "\n如果您是从控制台启动的本程序，请关闭本窗口！");
        jFrame.add(jtf);
        jtf.setEditable(false);
        jFrame.setLayout(new FlowLayout());
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(jFrame, "如果您不是从控制台中启动\n请点击否\n如果是，请点击是");
                if (JOptionPane.OK_OPTION == option) {
                    start();
                    System.exit(0);
                }
            }
        });
    }

    /**
     * 数组存储至文本文件 2020.11.12
     * - 第一版方案是，创建两个在线数据库，我们称它们为list和lists
     * - 我将list列为在线存储数据库，将lists列为判断数据库
     * - 在最后一次写入的时候，用list判断lists，将未存在lists数据库中的数据取出
     * - 添加到txt文件数据库中，但，无法实现这个功能，因为，增强for循环，无法实现
     * - getName直接取出到字符串中，导致无法判断，又因为，set和setusb会导致重复判断直接为false
     * <p>
     * - 第二版方案是，因为我已经在匿名函数取出数据到在线数据库中，那么我直接清空，重新写入
     */
    final public void writeTxt() throws IOException {
        SuperFile sf = new SuperFile();
        sf.clearFile(nameFile);
        for (SuperMan sm : list) {
            sf.writeFile(sm, nameFile);
        }
    }

    /**
     * 查询累计积分 2020.11.17
     */
    final public void queryScore() {
        boolean a = false;
        System.out.print("请输入会员名:");
        String name = input.next();
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                System.out.println("积分为：" + sm.getScore());
                a = true;
            }
        }
        if (!a) {
            System.out.println("用户名错误！");
        }
        clearScreen();
    }

    /**
     * 添加累计积分 2020.11.17
     */
    final public void scoreAdd() {
        boolean a = false;
        boolean b = false;
        System.out.print("请输入会员名:");
        String name = input.next();
        System.out.print("请输入会员密码:");
        String password = input.next();
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                if (sm.getPassword().equals(password)) {
                    a = true;
                    b = true;
                    break;
                }
            }
        }
        if (a) {
            System.out.print("请输入兑换的RMB额度<1Rmb = 100积分>:");
            long scoreEd = input.nextLong();
            long scoreAc = scoreEd * 100;
            for (SuperMan sm : list) {
                if (sm.getName().equals(name)) {
                    System.out.print("兑换前您的积分为:" + sm.getScore() + ",兑换后您的积分为:");
                    sm.setScore(sm.getScore() + scoreAc);
                    System.out.println(sm.getScore());
                }
            }
        }
        if (!b) {
            System.out.println("用户名或密码错误！");
        }
        clearScreen();
    }

    /**
     * 清空累计积分 2020.11.17
     */
    final public void clearScore() {
        boolean a = false;
        System.out.print("请输入会员名:");
        String name = input.next();
        System.out.print("请输入会员密码:");
        String password = input.next();
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                if (sm.getName().equals(name)) {
                    if (sm.getPassword().equals(password)) {
                        a = true;
                        sm.setScore(0);
                        System.out.println("已清空此会员全部积分！");
                        break;
                    }
                }
            }
        }
        if (!a) {
            System.out.println("用户名或密码错误！");
        }
        clearScreen();
    }

    /**
     * 兑换累计积分 2020.11.17
     */
    final public void exchangeScore() {
        boolean a = false;
        boolean b = false;
        long scoreYs = 0;
        System.out.print("请输入会员名:");
        String name = input.next();
        System.out.print("请输入会员密码:");
        String password = input.next();
        for (SuperMan sm : list) {
            if (sm.getName().equals(name)) {
                if (sm.getName().equals(name)) {
                    if (sm.getPassword().equals(password)) {
                        a = true;
                        b = true;
                        scoreYs = sm.getScore();
                        break;
                    }
                }
            }
        }
        if (!a) {
            System.out.println("用户名或密码错误！");
        }
        if (b) {
            System.out.print("请输入你要兑换的积分:");
            long scoreDh = input.nextLong();
            if (scoreYs > scoreDh) {
                for (SuperMan sm : list) {
                    if (sm.getName().equals(name)) {
                        sm.setScore(sm.getScore() - scoreDh);
                        System.out.println("未兑换之前积分余额:" + scoreYs + ",兑换之后积分余额:" + sm.getScore());
                    }
                }
            } else {
                System.out.println("你的积分不足，无法兑换！");
            }
        }
        clearScreen();
    }
}
