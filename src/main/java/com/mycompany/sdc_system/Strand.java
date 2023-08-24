/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sdc_system;

/**
 *
 * @author 35387
 */
public class Strand {

    String name = "";

    public String getName() {
        return name;
    }

    public void setName(char l, char r) {
        this.name = "(" + l + "," + r + ")";
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public char getFirstComponent() {
        return name.charAt(1);
    }
    
    public char getSecondComponent() {
        return name.charAt(3);
    }
    
}
