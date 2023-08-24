/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.sdc_system;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author 35387
 */
public class SDC_SYSTEM {

    int number_of_domains;
    int number_of_states;
    FSM fsm;
    String input;
    Strand[][] competing_strands;

    SDC_Configuration current_SDC_configuration;
    SDC_Configuration All_Configurations[];

    boolean uniform = true;
    boolean includeCovers = false;
    boolean includeConcen = false;

    // for uniform length for scafold and toehold
    int scaffold_length = 24;
    int toehold_length = 12;
    double concentration = 0.0000001;  //M
    double scaffold_concentration = 0.00000001;  //M

    // for different length for scafold and toehold
    int longest_scaffold_length = 40;
    int shortest_scaffold_length = 15;
    int min_scaffold_step = 3;

    int longest_toehold_length = 15;
    int shortest_toehold_length = 5;
    int min_toehold_step = 2;
    double highest_concentration = 0.00001;
    double lowest_concentration = 0.0000001;

    // in case of non_uniform lengths.
    Domain[] All_Domains_Data;

    int coverMultiple = 1;
    int Temp;
    int disRateMul;

    public SDC_SYSTEM(String input, FSM F, boolean uniform, boolean include_covers, int coverMul, boolean include_concen, int T, int disRate_Mul) {

        this.fsm = F;
        this.input = input;
        // Because we are hardwiring the first character of the input.
        this.number_of_domains = this.input.length() + 1;
        this.number_of_states = F.number_of_states;
        this.uniform = uniform;
        this.includeCovers = include_covers;
        this.includeConcen = include_concen;
        this.coverMultiple = coverMul;
        this.Temp = T;
        this.disRateMul = disRate_Mul;

        set_competing_strands_info();
        Auto_setting_All_Sites_Data();
        //manual_setting_All_Sites_Data();
        //print_competing_strands_info();

        PotentialDomainOccupation[] initial_configuration = new PotentialDomainOccupation[this.number_of_domains];

        for (int i = 0; i < number_of_domains; i++) {
            initial_configuration[i] = new PotentialDomainOccupation();
            //initial_configuration[i].setName('-', '-');
        }
        this.current_SDC_configuration = new SDC_Configuration(initial_configuration);
        this.current_SDC_configuration.energy = 0;
        //    current_SDC_configuration.print_configuration();
        //System.out.println("");
        //System.out.println(current_SDC_configuration.configuration_to_index(number_of_states));
        //int possible_configuraions = 2 * (int) Math.pow(number_of_states + 1, number_of_domains - 1);
        //System.out.println(possible_configuraions);
        //All_Configurations = new SDC_Configuration[possible_configuraions];
    }

    private void set_competing_strands_info() {
        Strand x;
        competing_strands = new Strand[number_of_domains][];

        // for site number 0
        x = new Strand();
        x.setName('0', '0');
        competing_strands[0] = new Strand[1];
        competing_strands[0][0] = x;

        // for next sites 
        for (int i = 0; i < input.length(); i++) {
            if (this.includeCovers == true) {
                competing_strands[i + 1] = new Strand[number_of_states + coverMultiple];

            } else {
                competing_strands[i + 1] = new Strand[number_of_states];
            }
            for (int j = 0; j < this.number_of_states; j++) {

                x = new Strand();
                x.setName((char) (j + '0'), (char) (this.fsm.transition_table[j][input.charAt(i) - 48] + '0'));
                competing_strands[i + 1][j] = x;
            }

            if (this.includeCovers == true) {

                for (int q = 0; q < coverMultiple; q++) {
                    x = new Strand();
                    x.setName("(c,d)");
                    competing_strands[i + 1][number_of_states + q] = x;

                }
            }
        }

    }

    public void print_competing_strands_info() {

        for (int i = 0; i < competing_strands.length; i++) {
            System.out.println("Competing strands at site " + i + " are ");
            for (int j = 0; j < competing_strands[i].length; j++) {
                System.out.println(competing_strands[i][j].name);
            }
        }
    }

    void Auto_setting_All_Sites_Data() {

        All_Domains_Data = new Domain[number_of_domains];

        if (includeConcen == true) {
            for (int i = 0; i < number_of_domains; i++) {
                All_Domains_Data[i] = new Domain();
                All_Domains_Data[i].Left_length = toehold_length;
                All_Domains_Data[i].Right_length = toehold_length;
                All_Domains_Data[i].Scaffold_length = scaffold_length;
                All_Domains_Data[i].Site_Number = i;
                if (i > 0) {
                    All_Domains_Data[i].concentration = concentration + concentration * (i + 1);
                } else {
                    All_Domains_Data[i].concentration = concentration;
                }

            }
        } else if (uniform == true) {
            for (int i = 0; i < number_of_domains; i++) {
                All_Domains_Data[i] = new Domain();
                All_Domains_Data[i].Left_length = toehold_length;
                All_Domains_Data[i].Right_length = toehold_length;
                All_Domains_Data[i].Scaffold_length = scaffold_length;
                All_Domains_Data[i].Site_Number = i;
                All_Domains_Data[i].concentration = concentration;
            }

//        } else {
//
//            //int scaffold_difference = this.longest_scaffold_length - this.shortest_scaffold_length;
////            int required_scaffold_step;
////            int scaffold_partitioning_factor;
////            int x = scaffold_difference / (number_of_sites - 2);
////            if (x >= min_scaffold_step) {
////                required_scaffold_step = x;
////                scaffold_partitioning_factor = 1;
////
////            } else {
////                required_scaffold_step = min_scaffold_step;
////                scaffold_partitioning_factor = x * min_scaffold_step;
////            }
////
////            int toehold_difference = this.longest_toehold_length - this.shortest_toehold_length;
////            int required_toehold_step;
////            int toehold_partitioning_factor;
////            int y = toehold_difference / (number_of_sites - 3);
////            if (y >= min_toehold_step) {
////                required_toehold_step = y;
////                toehold_partitioning_factor = 1;
////
////            } else {
////                required_toehold_step = min_toehold_step;
////                toehold_partitioning_factor = y * min_toehold_step;
////            }
////
//            double concentration_difference = this.highest_concentration - this.lowest_concentration;
//            double required_concentration_step = concentration_difference / number_of_domains;
//            for (int i = 0; i < number_of_domains; i++) {
//                All_Domains_Data[i] = new Domain();
//
//                if (i == 0) {
//                    All_Domains_Data[i].Left_length = longest_toehold_length;
//                    All_Domains_Data[i].Right_length = longest_toehold_length;
//                } else {
//                    All_Domains_Data[i].Left_length = All_Domains_Data[i - 1].Right_length;
//
//                    if (longest_toehold_length - i * min_toehold_step > shortest_toehold_length) {
//                        All_Domains_Data[i].Right_length = longest_toehold_length - i * min_toehold_step;
//                    } else {
//                        All_Domains_Data[i].Right_length = shortest_toehold_length;
//                    }
//
//                }
//                if (longest_scaffold_length - i * min_scaffold_step > shortest_scaffold_length) {
//                    All_Domains_Data[i].Scaffold_length = longest_scaffold_length - i * min_scaffold_step;
//                } else {
//                    All_Domains_Data[i].Scaffold_length = shortest_scaffold_length;
//                }
//                All_Domains_Data[i].Site_Number = i;
//                //All_Sites_Data[i].concentration = lowest_concentration + i * required_concentration_step;
//                All_Domains_Data[i].concentration = concentration;
//
//            }
        } else {
            int length_arr[] = new int[]{17,17,17,17,17,17,17, 16,16, 16,16,16,16,15, 15,15,15,15,14, 14,14,14,14,14,13, 13,13,13, 13,12,12, 12,12, 11, 11,11,10,10, 10,10,10,10, 9,9, 9, 8,8, 7,7,7, 7};
            for (int i = 0; i < number_of_domains; i++) {
                All_Domains_Data[i] = new Domain();
                All_Domains_Data[i].Left_length = length_arr[i];
                All_Domains_Data[i].Right_length = length_arr[i + 1];
                All_Domains_Data[i].Scaffold_length = scaffold_length;
                All_Domains_Data[i].Site_Number = i;
                All_Domains_Data[i].concentration = concentration;
            }
        }
    }

    public void manual_setting_All_Sites_Data() {

        Scanner cin = new Scanner(System.in);
        System.out.println("Filling Scaffold length first");
        All_Domains_Data = new Domain[number_of_domains];
        for (int i = 0; i < number_of_domains; i++) {
            All_Domains_Data[i] = new Domain();
            System.out.println("Site number " + i);
            System.out.print("please enter Scaffold length ");
            All_Domains_Data[i].Scaffold_length = cin.nextInt();
            All_Domains_Data[i].Site_Number = i;
            All_Domains_Data[i].concentration = concentration;
        }

        System.out.println("Filling toehold length second");
        for (int i = 0; i < number_of_domains; i++) {
            System.out.println("Site number " + i);
            System.out.print("please enter only right toehold length  [Left will be made automatically] ");

            All_Domains_Data[i].Right_length = cin.nextInt();
            if (i == 0) {
                All_Domains_Data[i].Left_length = All_Domains_Data[i].Right_length;
            } else {
                All_Domains_Data[i].Left_length = All_Domains_Data[i - 1].Right_length;
            }
        }
    }

    public void print_All_Sites_Data() {

        for (int i = 0; i < number_of_domains; i++) {

            System.out.println("site # " + i + "   " + All_Domains_Data[i].Scaffold_length + "  " + All_Domains_Data[i].Left_length + "   " + All_Domains_Data[i].Right_length + "   " + All_Domains_Data[i].concentration);
        }

    }

    public int start() {

        System.out.println("Generating a random configuration");

        current_SDC_configuration.configuration_Sequence[0].middleStrand = competing_strands[0][0];
        current_SDC_configuration.energy += scaffold_length;
        for (int i = 1; i < number_of_domains; i++) {
            Random rand = new Random();
            int int_random = rand.nextInt(number_of_states);
            current_SDC_configuration.configuration_Sequence[i].middleStrand = competing_strands[i][int_random];
        }

        int i;
        for (i = 0;; i++) {
            System.out.print(i + "  ");
            //System.out.println(i);
            current_SDC_configuration.print_configuration();
            // if (current_SDC_configuration == finalone) {
            //   break;
            //}
            //int x = current_SDC_configuration.configuration_to_index(number_of_states);
            //System.out.println(x);
            if (false) {
                ////All_Configurations[x] != null
                //SDC_Configuration S = All_Configurations[x];
                //current_SDC_configuration = S.select_random_next();
            } else {
                //current_SDC_configuration.compute_follow_up_configurations_V2(competing_strands, All_Domains_Data);
                //All_Configurations[x] = current_SDC_configuration;
                //System.out.println("");
                //current_SDC_configuration.print_follow_up_configurations();

                current_SDC_configuration = current_SDC_configuration.select_random_next();
                if (current_SDC_configuration == null) {
                    System.out.println("FINISHED");
                    break;
                }

            }

        }
        return i;
    }

    public int startV2() {

        //System.out.println("Generating a random configuration");
        int x = current_SDC_configuration.configuration_Sequence[0].attach_middle(null, competing_strands[0][0], All_Domains_Data[0], null);
        current_SDC_configuration.energy += x;
        int int_random;
        for (int i = 1; i < number_of_domains; i++) {
            Random rand = new Random();
            if (includeCovers) {
                int_random = rand.nextInt(number_of_states + coverMultiple);
            } else {
                int_random = rand.nextInt(number_of_states);
            }
            x = current_SDC_configuration.configuration_Sequence[i].attach_middle(current_SDC_configuration.configuration_Sequence[i - 1], competing_strands[i][int_random], All_Domains_Data[i], null);
            current_SDC_configuration.energy += x;
        }

        this.includeCovers = false;
        set_competing_strands_info();
        //this.print_competing_strands_info();

        int i;
        for (i = 0;; i++) {
            //System.out.print(i + " ");
            //current_SDC_configuration.print_configuration();
            current_SDC_configuration.compute_follow_up_configurations_V2(competing_strands, All_Domains_Data, Temp, disRateMul);

            //System.out.println("follow size =   " + current_SDC_configuration.follow_up_SDC_configurations.size());
            //System.out.println("follow up configurations are");
            //current_SDC_configuration.print_follow_up_configurations();
            current_SDC_configuration = current_SDC_configuration.select_random_next();
            //System.out.println("");
            if (current_SDC_configuration == null) {
                //  System.out.println("FINISHED");
                break;
            }
        }
        return i;
    }

    static int findRandom() {

        // Generate the random number
        int num = (1 + (int) (Math.random() * 100)) % 2;

        // Return the generated number
        return num;
    }

// Function to generate a random
// binary string of length N
    static String generateBinaryString(int N) {

        // Stores the empty string
        String S = "";

        // Iterate over the range [0, N - 1]
        for (int i = 0; i < N; i++) {

            // Store the random number
            int x = findRandom();

            // Append it to the string
            S = S + String.valueOf(x);
        }

        // Print the resulting string
        return S;
    }

// This code is contributed by AnkThon
    public static void main(String[] args) throws IOException {
        int y = 0;
        FSM F = new FSM(2, 2);
        F.intialize_transition_table();
        F.print_transition_table();
//        SDC_SYSTEM S0 = new SDC_SYSTEM(generateBinaryString(5), F, true, false, 1000, true, 55, 30);
//        S0.print_All_Sites_Data();
//        S0.print_competing_strands_info();
//        System.out.println(S0.startV2());
//        System.out.println("===========");
//
        String x = new String();
        int inputLength = 50;
        int arr_T[] = new int[]{60};
        int disRate[] = new int[]{10};
        int coverCon[] = new int[]{2, 10, 20, 30, 40, 50, 100};

        int num_iter = 100;

        PrintWriter out0 = null;
        PrintWriter out1[] = new PrintWriter[coverCon.length];
        PrintWriter out2 = null;
        PrintWriter out3[] = new PrintWriter[coverCon.length];
        PrintWriter out4 = null;

        for (int g = 0; g < arr_T.length; g++) {
            System.out.println("we are at Temp = " + arr_T[g]);
            for (int h = 0; h < disRate.length; h++) {
                System.out.println("we are at dis. rate = " + disRate[h]);

//                out0 = new PrintWriter("0-Sim_" + arr_T[g] + "c_toehold=12_scaffold=24_concentration=100nM_disRate=" + disRate[h] + "attRate.txt");
//                out0.print("No tricks");
//                out0.println();
//                out0.println();
//                for (int p = 0; p < coverCon.length; p++) {
//                    //ONLY COVERS
//                    out1[p] = new PrintWriter("2" + p + "-Sim_" + arr_T[g] + "=c_" + "CoverCon= " + coverCon[p] + "toehold=12_scaffold=24_concentration=100nM_disRate=" + disRate[h] + "attRate.txt");
//                    out1[p].print(coverCon[p] + "x covers");
//                    out1[p].println();
//                    out1[p].println();
//                }
//                // Concen
//                out2 = new PrintWriter("1-Sim_" + arr_T[g] + "=c_" + "ratioCon= Additive" + "toehold=12_scaffold=24_concentration=100nM_disRate=" + disRate[h] + "attRate.txt");
//                out2.print("  additive concentrations");
//                out2.println();
//                out2.println();
                // Concen
//                for (int p = 0; p < coverCon.length; p++) {
//                    //both
//                    out3[p] = new PrintWriter("2" + p + "Sim_" + arr_T[g] + "=c_" + "CoverCon= " + coverCon[p] + " _ratioCon= Additive" + "toehold=12_scaffold=24_concentration=100nM_disRate=" + disRate[h] + "attRate.txt");
//                    out3[p].print("additive concentrations, " + coverCon[p] + "x covers");
//                    out3[p].println();
//                    out3[p].println();
//                }
                //for (int i = 4; i < inputLength; i += 5) {
                int i = 4 + 5+5+5+5+5+5+5+5+5;
                out4 = new PrintWriter("1-Sim_len = " + (i + 1) + "  " + arr_T[g] + "=c_" + "Varying Toehold Length" + "toehold=12_scaffold=24_concentration=100nM_disRate=" + disRate[h] + "attRate.txt");
                out4.print("  Varying Toehold Length");
                out4.println();
                out4.println();
//                    out0.print(i + 1 + ": ");
//                    for (int l = 0; l < coverCon.length; l++) {
//                        out1[l].print(i + 1 + ":");
//                    }
//                    out2.print(i + 1 + ":");
//                    for (int p = 0; p < coverCon.length; p++) {
//                        out3[p].print(i + 1 + ":");
//                    }
                out4.print(i + 1 + ": ");

                System.out.println(" we are at length " + (i + 1));
                for (int j = 0; j < num_iter; j++) {

//                        //default (NO TRICKS)
//                        SDC_SYSTEM S0 = new SDC_SYSTEM(generateBinaryString(i), F, true, false, 0, false, arr_T[g], disRate[h]);
//                        out0.print(S0.startV2() + " ");
//
//                        //ONLY COVERS
//                        SDC_SYSTEM S1[] = new SDC_SYSTEM[coverCon.length];
//                        for (int l = 0; l < coverCon.length; l++) {
//                            S1[l] = new SDC_SYSTEM(generateBinaryString(i), F, true, true, coverCon[l], false, arr_T[g], disRate[h]);
//                            out1[l].print(S1[l].startV2() + " ");
//                        }
//
//                        //ONLY CONCEN.
//                        SDC_SYSTEM S2 = new SDC_SYSTEM(generateBinaryString(i), F, true, false, 0, true, arr_T[g], disRate[h]);
//                        out2.print(S2.startV2() + " ");
                    //ONLY toehold length.
                    SDC_SYSTEM S4 = new SDC_SYSTEM(generateBinaryString(i), F, false, false, 0, false, arr_T[g], disRate[h]);
                    //S4.print_All_Sites_Data();
                    out4.print(S4.startV2() + " ");

//                        //covers + con
//                        SDC_SYSTEM S3[] = new SDC_SYSTEM[coverCon.length];
//                        for (int p = 0; p < coverCon.length; p++) {
//
//                            //ONLY COVERS
//                            S3[p] = new SDC_SYSTEM(generateBinaryString(i), F, true, true, coverCon[p], true, arr_T[g], disRate[h]);
//                            out3[p].print(S3[p].startV2() + " ");
//
//                        }
                }

//                    out0.println();
//
//                    //ONLY COVERS
//                    for (int l = 0; l < coverCon.length; l++) {
//                        out1[l].println();
//                    }
//
//                    //ONLY CONCEN.
//                    out2.println();
//
//                    // covers + con
//                    for (int p = 0; p < coverCon.length; p++) {
//                        //ONLY COVERS
//                        out3[p].println();
//                    }
                // ONLY toehold length
                //SDC_SYSTEM s4 = new SDC_SYSTEM(generateBinaryString(20), F, true, true);
                // toehold length + covers
                //SDC_SYSTEM S5 = new SDC_SYSTEM(generateBinaryString(20), F, true, true);
                // toehold length + con
                //SDC_SYSTEM S6 = new SDC_SYSTEM(generateBinaryString(20), F, true, true);
                // ALL
                //SDC_SYSTEM S7 = new SDC_SYSTEM(generateBinaryString(20), F, true, true);
            }

        }
//        out0.close();
//        for (int l = 0; l < coverCon.length; l++) {
//            out1[l].close();
//        }
//        out2.close();
//        for (int p = 0; p < coverCon.length; p++) {
//            //ONLY COVERS
//            out3[p].close();
//        }
        out4.close();
    }
}

//}
