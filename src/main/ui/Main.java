package ui;

import model.MediRecords;

import javax.swing.*;


public class Main extends JFrame {
    public static void main(String[] args) {
        MediRecords mr = new MediRecords();
        new LoginScreenUI();
    }
}