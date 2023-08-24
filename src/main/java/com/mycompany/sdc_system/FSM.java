/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sdc_system;

import java.util.Scanner;

/**
 *
 * @author 35387
 */
public class FSM {

    int number_of_states;
    int number_of_alphabet_characters;
    int transition_table[][];

    public FSM(int number_of_states, int alphabet_count) {
        this.number_of_states = number_of_states;
        this.number_of_alphabet_characters = alphabet_count;
        transition_table = new int[number_of_states][number_of_alphabet_characters];
    }

    public void intialize_transition_table() {
        Scanner cin = new Scanner(System.in);
        System.out.println("Insert transition table for this FSM");
        System.out.println("Which has " + number_of_states + "  and " + number_of_alphabet_characters + "characters");

        for (int i = 0; i < number_of_states; i++) {
            for (int j = 0; j < number_of_alphabet_characters; j++) {

                System.out.println("State " + i + " Charater " + j);
                transition_table[i][j] = cin.nextInt();
            }
        }

    }

    public void print_transition_table() {
        System.out.println("");
        System.out.println("       FSM TRANSITION TABLE     ");
        System.out.println("===========================================");
        System.out.print("      ");
        for (int j = 0; j < number_of_alphabet_characters; j++) {
            System.out.print(j + "   ");
        }
        System.out.println("");

        for (int i = 0; i < number_of_states; i++) {
            System.out.print(i + " --> ");
            for (int j = 0; j < number_of_alphabet_characters; j++) {
                System.out.print(transition_table[i][j] + "   ");
            }
            System.out.println("");
        }

        System.out.println("===========================================");
        System.out.println("");

    }

    
}
