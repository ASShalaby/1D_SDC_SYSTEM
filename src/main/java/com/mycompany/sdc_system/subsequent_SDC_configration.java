/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sdc_system;

/**
 *
 * @author 35387
 */
public class subsequent_SDC_configration {

    SDC_Configuration SDC_configuration;
    double deltaG;
    double rate;
    double probability;
    double CDF;

    // rate_type = 1 -->  toehold attachment rate
    // rate_type = 2 -->  toehold detachment rate
    // rate_type = 3 -->  strabd displacement rate
    int rate_type;

    public void compute_transition_rate(double concentration, int disMul, double det_r_C) {

        double k = 0.001987204259;
        //double scaffold_concentration = 1; //nM
        double T = 40;  //C

        double attachment_constant_rate = 3600000; //(/M/S)

        double detachment_rate = det_r_C; //(/M/S)

        double displacement_rate = attachment_constant_rate * concentration * disMul; //(/M/S)
        if (rate_type == 1) {
            this.rate = attachment_constant_rate * concentration;
        } else if (rate_type == 2) {
            this.rate = detachment_rate;
        } else if (rate_type == 3) {
            this.rate = displacement_rate;

        } else {
            System.out.println("Something wrong happened");
        }
    }
}
