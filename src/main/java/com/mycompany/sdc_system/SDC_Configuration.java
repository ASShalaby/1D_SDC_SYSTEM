/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sdc_system;

import java.util.ArrayList;
import java.util.Random;
import javax.crypto.AEADBadTagException;

/**
 *
 * @author 35387
 */
public class SDC_Configuration {

    PotentialDomainOccupation[] configuration_Sequence;
    double energy;
    ArrayList<subsequent_SDC_configration> follow_up_SDC_configurations = new ArrayList<subsequent_SDC_configration>();

    public SDC_Configuration(PotentialDomainOccupation[] configuration_Sequence) {
        this.configuration_Sequence = configuration_Sequence;
    }

    public SDC_Configuration() {
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public String jointCondition(SDC_Configuration HAHA, int jointNumber) {
        boolean match = HAHA.configuration_Sequence[jointNumber].left_match;
        if (match) {
            return "match";
        }
        boolean hangleft_hangright = HAHA.configuration_Sequence[jointNumber - 1].has_Right_Hanging && HAHA.configuration_Sequence[jointNumber].has_Left_Hanging;
        if (hangleft_hangright) {
            return "HL_HR";
        }
        boolean hangleftonly = HAHA.configuration_Sequence[jointNumber - 1].has_Right_Hanging && HAHA.configuration_Sequence[jointNumber].left_toehold_available;
        if (hangleftonly) {
            return "HL";
        }
        boolean hangrightonly = HAHA.configuration_Sequence[jointNumber - 1].Right_toehold_available && HAHA.configuration_Sequence[jointNumber].has_Left_Hanging;
        if (hangrightonly) {
            return "HR";
        }
        boolean puremismatchNOhang = HAHA.configuration_Sequence[jointNumber - 1].Right_toehold_available && HAHA.configuration_Sequence[jointNumber].left_toehold_available;
        if (puremismatchNOhang) {
            return "mismatch";
        } else {
            System.out.println("5RA");
            return "A7A";
        }
    }

    public void AUX(SDC_Configuration A) {
        PotentialDomainOccupation[] config_seq;
        config_seq = new PotentialDomainOccupation[A.configuration_Sequence.length];
        this.energy = A.energy;

        for (int k = 0; k < A.configuration_Sequence.length; k++) {

            config_seq[k] = new PotentialDomainOccupation();

            config_seq[k].leftStrand.name = A.configuration_Sequence[k].leftStrand.name;
            config_seq[k].middleStrand.name = A.configuration_Sequence[k].middleStrand.name;
            config_seq[k].rightStrand.name = A.configuration_Sequence[k].rightStrand.name;
            config_seq[k].has_Free_Middle = A.configuration_Sequence[k].has_Free_Middle;
            config_seq[k].left_toehold_available = A.configuration_Sequence[k].left_toehold_available;
            config_seq[k].Right_toehold_available = A.configuration_Sequence[k].Right_toehold_available;
            config_seq[k].has_Free_Middle = A.configuration_Sequence[k].has_Free_Middle;
            config_seq[k].has_Left_Hanging = A.configuration_Sequence[k].has_Left_Hanging;
            config_seq[k].has_Right_Hanging = A.configuration_Sequence[k].has_Right_Hanging;
            config_seq[k].left_toehold_available = A.configuration_Sequence[k].left_toehold_available;
            config_seq[k].Right_toehold_available = A.configuration_Sequence[k].Right_toehold_available;
            config_seq[k].left_match = A.configuration_Sequence[k].left_match;
            config_seq[k].right_match = A.configuration_Sequence[k].right_match;

        }
        this.configuration_Sequence = config_seq;

    }

    public void compute_follow_up_configurations_V2(Strand[][] competing_strands, Domain All_Sites_Data[], int Temp, int disMul) {

        //40,45,50,55,60,65,70
        double det_Rate_arr[] = new double[31];
        det_Rate_arr[0] = 0.001152;
        det_Rate_arr[5] = 0.013788;
        det_Rate_arr[10] = 0.154092;
        det_Rate_arr[15] = 1.612527;
        det_Rate_arr[20] = 15.740918;
        det_Rate_arr[25] = 144.735875;
        det_Rate_arr[30] = 1206.163023;
        

        double length_det_Rate_arr[] = new double[21];
        length_det_Rate_arr[6] = 2.8983;
        length_det_Rate_arr[7] = 0.22007;
        length_det_Rate_arr[8] = 0.0163686;
        length_det_Rate_arr[9] = 0.0011984;
        length_det_Rate_arr[10] = 8.666434 * Math.pow(10, -5);
        length_det_Rate_arr[11] = 6.2042829 * Math.pow(10, -6);
        length_det_Rate_arr[12] = 4.4049259 * Math.pow(10, -7);
        length_det_Rate_arr[13] = 3.1056976 * Math.pow(10, -8);
        length_det_Rate_arr[14] = 2.176718 * Math.pow(10, -9);
        length_det_Rate_arr[15] = 1.517832 * Math.pow(10, -10);

        
        

        SDC_Configuration sdc_config = new SDC_Configuration();
        subsequent_SDC_configration subseq_config;
        int extra_pairs = 0;
        PotentialDomainOccupation[] config_seq;

        double total = 0;

        for (int i = 1; i < this.configuration_Sequence.length; i++) {

            sdc_config = new SDC_Configuration();
            sdc_config.AUX(this);
            extra_pairs = 0;
            config_seq = new PotentialDomainOccupation[this.configuration_Sequence.length];
            Strand middleToTheLeftOPreviousJoint = new Strand();
            Strand middleToTheRigthOfNextJoint = new Strand();

            String currentJointcondition = jointCondition(this, i);
            Strand rightOfJoint = sdc_config.configuration_Sequence[i].leftStrand;
            //System.out.println("right Of Joint = " +rightOfJoint.name);

            Strand leftOfJoint = sdc_config.configuration_Sequence[i - 1].rightStrand;
            //System.out.println("left Of Joint = " + leftOfJoint.name);

            Strand middleToTheLeftOfJoint = sdc_config.configuration_Sequence[i - 1].middleStrand;
            //System.out.println("middle To The Left Of Joint = " +middleToTheLeftOfJoint.name);

            Strand middleToTheRigthOfJoint = sdc_config.configuration_Sequence[i].middleStrand;
            //System.out.println("middle To The Rigth Of Joint = " +middleToTheRigthOfJoint.name);

            if (i < this.configuration_Sequence.length - 1) {
                middleToTheRigthOfNextJoint = sdc_config.configuration_Sequence[i + 1].middleStrand;
            }
            if (i > 1) {
                middleToTheLeftOPreviousJoint = sdc_config.configuration_Sequence[i - 2].middleStrand;
            }

            if (currentJointcondition == "match") {
                // System.out.println("match");
                continue;
            } else if (currentJointcondition == "HL_HR") {
                //System.out.println("HL_HR");
                //There is 4 possibilities
                // 1- left falls 
                extra_pairs = 0;

                sdc_config = new SDC_Configuration();
                sdc_config.AUX(this);

                extra_pairs = sdc_config.configuration_Sequence[i - 1].rightFall(All_Sites_Data[i - 1]);
                subseq_config = new subsequent_SDC_configration();
                subseq_config.SDC_configuration = sdc_config;
                subseq_config.deltaG = extra_pairs;
                subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                subseq_config.rate_type = 2;
                subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);

                total += subseq_config.rate;
                this.follow_up_SDC_configurations.add(subseq_config);

                // 2- left compete and displace
                extra_pairs = 0;
                sdc_config = new SDC_Configuration();
                sdc_config.AUX(this);
                //sdc_config = new SDC_Configuration(config_seq);

                if (i == this.configuration_Sequence.length - 1) {
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].rightFall(All_Sites_Data[i - 1]);
                    extra_pairs += sdc_config.configuration_Sequence[i].middleFall(sdc_config.configuration_Sequence[i - 1], All_Sites_Data[i], null);
                    extra_pairs += sdc_config.configuration_Sequence[i].attach_middle(sdc_config.configuration_Sequence[i - 1], leftOfJoint, All_Sites_Data[i], null);
                } else {
                    String nextJointcondition_OLD = jointCondition(this, i + 1);

                    extra_pairs += sdc_config.configuration_Sequence[i - 1].rightFall(All_Sites_Data[i - 1]);
                    extra_pairs += sdc_config.configuration_Sequence[i].middleFall(sdc_config.configuration_Sequence[i - 1], All_Sites_Data[i], sdc_config.configuration_Sequence[i + 1]);
                    //String nextJointcondition_CURRENT = jointCondition(sdc_config, i + 1);
                    //if (nextJointcondition_CURRENT == "match") {
                    // } else 
                    if (nextJointcondition_OLD == "match") {
                        extra_pairs += sdc_config.configuration_Sequence[i + 1].attach_left(middleToTheRigthOfJoint, All_Sites_Data[i + 1]);
                    }
                    extra_pairs += sdc_config.configuration_Sequence[i].attach_middle(sdc_config.configuration_Sequence[i - 1], leftOfJoint, All_Sites_Data[i], sdc_config.configuration_Sequence[i + 1]);
                }

                subseq_config = new subsequent_SDC_configration();
                subseq_config.SDC_configuration = sdc_config;
                subseq_config.deltaG = extra_pairs;
                subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                subseq_config.rate_type = 3;
                subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);
                total += subseq_config.rate;
                this.follow_up_SDC_configurations.add(subseq_config);

                // 3- right falls   
                extra_pairs = 0;

                //sdc_config = new SDC_Configuration(config_seq);
                sdc_config = new SDC_Configuration();
                sdc_config.AUX(this);

                extra_pairs = sdc_config.configuration_Sequence[i].leftFall(All_Sites_Data[i]);
                subseq_config = new subsequent_SDC_configration();
                subseq_config.SDC_configuration = sdc_config;
                subseq_config.deltaG = extra_pairs;
                subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                subseq_config.rate_type = 2;
                subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);
                total += subseq_config.rate;
                this.follow_up_SDC_configurations.add(subseq_config);

                // 4- right compete and displace
                extra_pairs = 0;

                sdc_config = new SDC_Configuration();
                sdc_config.AUX(this);

                if (i != 1) {
                    String previousJointcondition_OLD = jointCondition(this, i - 1);

                    extra_pairs += sdc_config.configuration_Sequence[i].leftFall(All_Sites_Data[i]);
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].middleFall(sdc_config.configuration_Sequence[i - 2], All_Sites_Data[i - 1], sdc_config.configuration_Sequence[i]);

                    //String previousJointcondition_CURRENT = jointCondition(sdc_config, i - 1);
                    if (previousJointcondition_OLD == "match") {
                        extra_pairs += sdc_config.configuration_Sequence[i - 2].attach_right(middleToTheLeftOfJoint, All_Sites_Data[i - 2]);
                    }
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].attach_middle(sdc_config.configuration_Sequence[i - 2], rightOfJoint, All_Sites_Data[i - 1], sdc_config.configuration_Sequence[i]);
                } else if (i == 1) {

                    extra_pairs += sdc_config.configuration_Sequence[i].leftFall(All_Sites_Data[i]);
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].middleFall(null, All_Sites_Data[i - 1], sdc_config.configuration_Sequence[i]);
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].attach_middle(null, rightOfJoint, All_Sites_Data[i - 1], sdc_config.configuration_Sequence[i]);
                }
                subseq_config = new subsequent_SDC_configration();
                subseq_config.SDC_configuration = sdc_config;
                subseq_config.deltaG = extra_pairs;
                subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                subseq_config.rate_type = 3;
                // very important i-1
                subseq_config.compute_transition_rate(All_Sites_Data[i - 1].concentration, disMul, det_Rate_arr[Temp - 40]);
                total += subseq_config.rate;
                this.follow_up_SDC_configurations.add(subseq_config);

            } else if (currentJointcondition == "HL") {
                //System.out.println("HL");

                //There is 3 possibilities
                // 1- left falls 
                extra_pairs = 0;
                sdc_config = new SDC_Configuration();
                sdc_config.AUX(this);

                extra_pairs = sdc_config.configuration_Sequence[i - 1].rightFall(All_Sites_Data[i - 1]);
                subseq_config = new subsequent_SDC_configration();
                subseq_config.SDC_configuration = sdc_config;
                subseq_config.deltaG = extra_pairs;
                subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                subseq_config.rate_type = 2;
                subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);
                total += subseq_config.rate;
                this.follow_up_SDC_configurations.add(subseq_config);

                // 2- left compete and displace
                extra_pairs = 0;
                sdc_config = new SDC_Configuration();
                sdc_config.AUX(this);
                //sdc_config = new SDC_Configuration(config_seq);

                if (i == this.configuration_Sequence.length - 1) {
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].rightFall(All_Sites_Data[i - 1]);
                    extra_pairs += sdc_config.configuration_Sequence[i].middleFall(sdc_config.configuration_Sequence[i - 1], All_Sites_Data[i], null);
                    extra_pairs += sdc_config.configuration_Sequence[i].attach_middle(sdc_config.configuration_Sequence[i - 1], leftOfJoint, All_Sites_Data[i], null);
                } else {
                    String nextJointcondition_OLD = jointCondition(this, i + 1);

                    extra_pairs += sdc_config.configuration_Sequence[i - 1].rightFall(All_Sites_Data[i - 1]);
                    extra_pairs += sdc_config.configuration_Sequence[i].middleFall(sdc_config.configuration_Sequence[i - 1], All_Sites_Data[i], sdc_config.configuration_Sequence[i + 1]);
                    //String nextJointcondition_CURRENT = jointCondition(sdc_config, i + 1);
                    //if (nextJointcondition_CURRENT == "match") {
                    // } else 
                    if (nextJointcondition_OLD == "match") {
                        extra_pairs += sdc_config.configuration_Sequence[i + 1].attach_left(middleToTheRigthOfJoint, All_Sites_Data[i + 1]);
                    }
                    extra_pairs += sdc_config.configuration_Sequence[i].attach_middle(sdc_config.configuration_Sequence[i - 1], leftOfJoint, All_Sites_Data[i], sdc_config.configuration_Sequence[i + 1]);
                }

                subseq_config = new subsequent_SDC_configration();
                subseq_config.SDC_configuration = sdc_config;
                subseq_config.deltaG = extra_pairs;
                subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                subseq_config.rate_type = 3;
                subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);
                total += subseq_config.rate;
                this.follow_up_SDC_configurations.add(subseq_config);

                // 3- right to joint attach 
                extra_pairs = 0;

                for (int a = 0; a < competing_strands[i - 1].length; a++) {
                    extra_pairs = 0;
                    Strand potentialAttachToRight = competing_strands[i - 1][a];

                    if (potentialAttachToRight.getSecondComponent() == middleToTheRigthOfJoint.getFirstComponent()) {

                        sdc_config = new SDC_Configuration();
                        sdc_config.AUX(this);

                        extra_pairs += sdc_config.configuration_Sequence[i].attach_left(potentialAttachToRight, All_Sites_Data[i]);
                        subseq_config = new subsequent_SDC_configration();
                        subseq_config.SDC_configuration = sdc_config;

                        subseq_config.deltaG = extra_pairs;
                        subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                        subseq_config.rate_type = 1;
                        subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);
                        total += subseq_config.rate;
                        this.follow_up_SDC_configurations.add(subseq_config);
                    }
                }

            } else if (currentJointcondition == "HR") {
                //System.out.println("HR");
                //There is 3 possibilities
                // 1- right falls   
                extra_pairs = 0;

                sdc_config = new SDC_Configuration();
                sdc_config.AUX(this);

                extra_pairs = sdc_config.configuration_Sequence[i].leftFall(All_Sites_Data[i]);
                subseq_config = new subsequent_SDC_configration();
                subseq_config.SDC_configuration = sdc_config;
                subseq_config.deltaG = extra_pairs;
                subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                subseq_config.rate_type = 2;
                subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);
                total += subseq_config.rate;
                this.follow_up_SDC_configurations.add(subseq_config);

                // 2- right compete and displace
                extra_pairs = 0;

                sdc_config = new SDC_Configuration();
                sdc_config.AUX(this);

                if (i != 1) {
                    String previousJointcondition_OLD = jointCondition(this, i - 1);

                    extra_pairs += sdc_config.configuration_Sequence[i].leftFall(All_Sites_Data[i]);
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].middleFall(sdc_config.configuration_Sequence[i - 2], All_Sites_Data[i - 1], sdc_config.configuration_Sequence[i]);

                    //String previousJointcondition_CURRENT = jointCondition(sdc_config, i - 1);
                    if (previousJointcondition_OLD == "match") {
                        extra_pairs += sdc_config.configuration_Sequence[i - 2].attach_right(middleToTheLeftOfJoint, All_Sites_Data[i - 2]);
                    }
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].attach_middle(sdc_config.configuration_Sequence[i - 2], rightOfJoint, All_Sites_Data[i - 1], sdc_config.configuration_Sequence[i]);
                } else if (i == 1) {

                    extra_pairs += sdc_config.configuration_Sequence[i].leftFall(All_Sites_Data[i]);
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].middleFall(null, All_Sites_Data[i - 1], sdc_config.configuration_Sequence[i]);
                    extra_pairs += sdc_config.configuration_Sequence[i - 1].attach_middle(null, rightOfJoint, All_Sites_Data[i - 1], sdc_config.configuration_Sequence[i]);
                }
                subseq_config = new subsequent_SDC_configration();
                subseq_config.SDC_configuration = sdc_config;
                subseq_config.deltaG = extra_pairs;
                subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                subseq_config.rate_type = 3;
                subseq_config.compute_transition_rate(All_Sites_Data[i - 1].concentration, disMul, det_Rate_arr[Temp - 40]);
                total += subseq_config.rate;
                this.follow_up_SDC_configurations.add(subseq_config);

                // 3- Left to joint attatch 
                for (int a = 0; a < competing_strands[i].length; a++) {
                    extra_pairs = 0;
                    Strand potentialAttachToLeft = competing_strands[i][a];

                    if (potentialAttachToLeft.getFirstComponent() == middleToTheLeftOfJoint.getSecondComponent()) {

                        sdc_config = new SDC_Configuration();
                        sdc_config.AUX(this);
                        extra_pairs += sdc_config.configuration_Sequence[i - 1].attach_right(potentialAttachToLeft, All_Sites_Data[i - 1]);
                        subseq_config = new subsequent_SDC_configration();
                        subseq_config.SDC_configuration = sdc_config;

                        subseq_config.deltaG = extra_pairs;
                        subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                        subseq_config.rate_type = 1;
                        subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);
                        total += subseq_config.rate;
                        this.follow_up_SDC_configurations.add(subseq_config);
                    }
                }

            } else if (currentJointcondition == "mismatch") {
                //System.out.println("mismatch");
                //There is 2 possibilities

                // 1- Left to joint attatch 
                for (int a = 0; a < competing_strands[i].length; a++) {

                    extra_pairs = 0;
                    Strand potentialAttachToLeft = competing_strands[i][a];

                    if (potentialAttachToLeft.getFirstComponent() == middleToTheLeftOfJoint.getSecondComponent()) {
                        sdc_config = new SDC_Configuration();
                        sdc_config.AUX(this);
                        extra_pairs += sdc_config.configuration_Sequence[i - 1].attach_right(potentialAttachToLeft, All_Sites_Data[i - 1]);
                        subseq_config = new subsequent_SDC_configration();
                        subseq_config.SDC_configuration = sdc_config;

                        subseq_config.deltaG = extra_pairs;
                        subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                        subseq_config.rate_type = 1;
                        subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);
                        total += subseq_config.rate;
                        this.follow_up_SDC_configurations.add(subseq_config);
                    }
                }

                // 2- right to joint attach 
                extra_pairs = 0;

                for (int a = 0; a < competing_strands[i - 1].length; a++) {

                    extra_pairs = 0;

                    // only for covers; this is so bad should be modified later.
                    if (i == 1) {
                        a = 0;
                    }
                    Strand potentialAttachToRight = competing_strands[i - 1][a];

                    if (potentialAttachToRight.getSecondComponent() == middleToTheRigthOfJoint.getFirstComponent()) {
                        sdc_config = new SDC_Configuration();
                        sdc_config.AUX(this);

                        extra_pairs += sdc_config.configuration_Sequence[i].attach_left(potentialAttachToRight, All_Sites_Data[i]);
                        subseq_config = new subsequent_SDC_configration();
                        subseq_config.SDC_configuration = sdc_config;

                        subseq_config.deltaG = extra_pairs;
                        subseq_config.SDC_configuration.energy = this.energy + subseq_config.deltaG;
                        subseq_config.rate_type = 1;
                        subseq_config.compute_transition_rate(All_Sites_Data[i].concentration, disMul, det_Rate_arr[Temp - 40]);
                        total += subseq_config.rate;
                        this.follow_up_SDC_configurations.add(subseq_config);
                    }
                }

            } else {
                System.out.println("some Shit");
            }

        }

        subsequent_SDC_configration x;

        for (int i = 0; i < this.follow_up_SDC_configurations.size(); i++) {
            x = this.follow_up_SDC_configurations.get(i);
            x.probability = x.rate / total;

            if (i == 0) {
                x.CDF = x.probability;
            }
            if (i > 0) {
                x.CDF = x.probability + this.follow_up_SDC_configurations.get(i - 1).CDF;
            }
        }
    }

    public SDC_Configuration select_random_next() {
        double r = new Random().nextDouble();
        subsequent_SDC_configration s = new subsequent_SDC_configration();

        for (subsequent_SDC_configration x : this.follow_up_SDC_configurations) {
            if (r < x.CDF) {
                s = x;
                break;
            }
        }
        return s.SDC_configuration;
    }

//    public double compute_rate(double delta_G, double concentration) {
//
//        double K = 0.001987204259;
//        double T = 300;
//
//        //return concentration * Math.exp(-1 * deltaG / (K * T));
//        return concentration * Math.exp(-1 * delta_G / (T));
//
//        //return  1;
//    }
//    public void print_configurationASLY() {
//
//        if (this.configuration_Sequence == null) {
//            System.out.println("NULL");
//            return;
//        }
//        for (int i = 0; i < this.configuration_Sequence.length; i++) {
//            if (this.configuration_Sequence[i].leftStrand.name == "") {
//                if (this.configuration_Sequence[i].rightStrand.name == "") {
//                    System.out.print("[" + this.configuration_Sequence[i].middleStrand.getName() + "]");
//                } else {
//                    System.out.print("[" + this.configuration_Sequence[i].middleStrand.getName() + "<-" + this.configuration_Sequence[i].rightStrand.getName() + "]");
//
//                }
//            } else {
//                if (this.configuration_Sequence[i].rightStrand.name == "") {
//                    System.out.print("[" + this.configuration_Sequence[i].leftStrand.getName() + "->" + this.configuration_Sequence[i].middleStrand.getName() + "]");
//
//                } else {
//                    System.out.print("[" + this.configuration_Sequence[i].leftStrand.getName() + "->" + this.configuration_Sequence[i].middleStrand.getName() + "<-" + this.configuration_Sequence[i].rightStrand.getName() + "]");
//
//                }
//            }
//
//        }
//
//        System.out.print("  with energy = " + this.energy);
//        System.out.println("");
//
//    }
    public void print_configuration() {

        if (this.configuration_Sequence == null) {
            System.out.println("NULL");
            return;
        }
        for (int i = 0; i < this.configuration_Sequence.length; i++) {

            //System.out.print("[" + this.configuration_Sequence[i].leftStrand.getName() + "->" + this.configuration_Sequence[i].middleStrand.getName() + "<-" + this.configuration_Sequence[i].rightStrand.getName() + "] ");
            if (this.configuration_Sequence[i].leftStrand.name == "") {
                if (this.configuration_Sequence[i].rightStrand.name == "") {
                    System.out.print("[" + this.configuration_Sequence[i].middleStrand.getName() + "]");
                } else {
                    System.out.print("[" + this.configuration_Sequence[i].middleStrand.getName() + "<-" + this.configuration_Sequence[i].rightStrand.getName() + "] ");

                }
            } else {
                if (this.configuration_Sequence[i].rightStrand.name == "") {
                    System.out.print("[" + this.configuration_Sequence[i].leftStrand.getName() + "->" + this.configuration_Sequence[i].middleStrand.getName() + "] ");

                } else {
                    System.out.print("[" + this.configuration_Sequence[i].leftStrand.getName() + "->" + this.configuration_Sequence[i].middleStrand.getName() + "<-" + this.configuration_Sequence[i].rightStrand.getName() + "] ");

                }
            }

        }

        System.out.print("  with energy = " + this.energy);
        System.out.println("");

    }

    public void print_follow_up_configurations() {
        for (subsequent_SDC_configration x : this.follow_up_SDC_configurations) {
            x.SDC_configuration.print_configuration();
            System.out.println("   with probability = " + x.probability + " with CDF = " + x.CDF);
        }
    }

//    public int configuration_to_index(int base) {
//        int index = 0;
//        char c;
//        int char_to_int;
//
//        for (int i = 0; i < this.configuration_Sequence.length; i++) {
//            if (i == 0) {
//                c = configuration_Sequence[i].getSecondComponent();
//            } else {
//                c = configuration_Sequence[i].getFirstComponent();
//            }
//            if (c == '-') {
//                char_to_int = 0;
//            } else {
//                char_to_int = c - '0' + 1;
//            }
//            index += char_to_int * (int) Math.pow(base, i);
//        }
//        return index;
//    }
}
