/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.trainscheduler;

import ui.*;

import javax.swing.SwingUtilities;
/**
 * 負責叫出首頁 HomeFrame
 */


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeFrame());
    }
}
