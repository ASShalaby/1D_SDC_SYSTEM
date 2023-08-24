/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sdc_system;

/**
 *
 * @author 35387
 */
public class PotentialDomainOccupation {

    Strand middleStrand;
    Strand leftStrand;
    Strand rightStrand;

    // to check whether a domain is empty or not
    boolean has_Free_Middle;
    boolean has_Left_Hanging;
    boolean has_Right_Hanging;
    boolean left_toehold_available;
    boolean Right_toehold_available;
    boolean left_match;
    boolean right_match;

    public PotentialDomainOccupation() {
        middleStrand = new Strand();
        middleStrand.setName("(-,-)");
        leftStrand = new Strand();
        rightStrand = new Strand();
        has_Free_Middle = true;
        left_toehold_available = false;
        Right_toehold_available = false;
        has_Left_Hanging = false;
        has_Right_Hanging = false;
        left_match = false;
        right_match = false;
    }

    public void printMetaData() {
        System.out.println("has_Free_Middle " + has_Free_Middle);
        System.out.println("left_toehold_available " + left_toehold_available);
        System.out.println("Right_toehold_available " + Right_toehold_available);
        System.out.println("has_Left_Hanging " + has_Left_Hanging);
        System.out.println("has_Right_Hanging " + has_Right_Hanging);
        System.out.println("left_match " + left_match);
        System.out.println("right_match " + right_match);

    }

    public int attach_middle(PotentialDomainOccupation L, Strand s, Domain D, PotentialDomainOccupation R) {
        int extraPairs = 0;
        if (has_Free_Middle) {
            this.middleStrand = s;
            extraPairs += D.Scaffold_length;
            has_Free_Middle = false;

            // there is a match on Left side
            if (L != null && L.Right_toehold_available && L.middleStrand.getSecondComponent() == s.getFirstComponent()) {
                left_toehold_available = false;
                has_Left_Hanging = false;
                L.Right_toehold_available = false;
                L.has_Right_Hanging = false;
                this.left_match = true;
                L.right_match = true;
                extraPairs += D.Left_length;
            } // there is a mismatch
            else {
                this.left_match = false;
                has_Left_Hanging = false;
                left_toehold_available = true;
                if (L != null) {
                    L.right_match = false;
                }
            }
            // there is a match on right side
            if (R != null && R.left_toehold_available && s.getSecondComponent() == R.middleStrand.getFirstComponent()) {
                Right_toehold_available = false;
                has_Right_Hanging = false;
                R.left_toehold_available = false;
                R.has_Left_Hanging = false;
                this.right_match = true;
                R.left_match = true;
                extraPairs += D.Right_length;
            } else {
                this.right_match = false;
                has_Right_Hanging = false;
                Right_toehold_available = true;
                if (R != null) {
                    R.left_match = false;
                }
            }

            //System.out.println("Middle attached with extra pairs = " + extraPairs);
        }
        return extraPairs;
    }

    public int attach_left(Strand s, Domain D) {
        int extraPairs = 0;
        if (!has_Free_Middle && left_toehold_available) {
            //System.out.println("left attached");
            this.leftStrand = s;
            extraPairs += D.Left_length;
            left_toehold_available = false;
            has_Left_Hanging = true;
        }
        return extraPairs;
    }

    public int attach_right(Strand s, Domain D) {
        int extraPairs = 0;
        if (!has_Free_Middle && Right_toehold_available) {
            //System.out.println("right attached");
            this.rightStrand = s;
            extraPairs += D.Right_length;
            Right_toehold_available = false;
            has_Right_Hanging = true;
        }
        return extraPairs;
    }

    public int middleFall(PotentialDomainOccupation L, Domain D, PotentialDomainOccupation R) {
        int extraPairs = 0;
        //System.out.println("middle fall");
        extraPairs -= D.Scaffold_length;

        // VERY IMPORTANT
        if (this.has_Left_Hanging) {
            //System.out.println("Very important 1 ");
            extraPairs -= D.Left_length;
        }
        if (this.has_Right_Hanging) {
            //System.out.println("Very important 2 ");
            extraPairs -= D.Right_length;
        }

        // ACTION ON ME
        middleStrand = new Strand();
        middleStrand.setName("(-,-)");
        leftStrand = new Strand();
        rightStrand = new Strand();
        has_Free_Middle = true;
        left_toehold_available = false;
        Right_toehold_available = false;
        has_Left_Hanging = false;
        has_Right_Hanging = false;
        left_match = false;
        right_match = false;

        // ACTION ON LEFT
        if (L != null && L.right_match == true) {
            L.right_match = false;
            L.Right_toehold_available = true;
            extraPairs -= D.Left_length;
        }
        // ACTION ON Right
        if (R != null && R.left_match == true) {
            R.left_match = false;
            R.left_toehold_available = true;
            extraPairs -= D.Right_length;
        }
        //System.out.println("middle fall by " + extraPairs);
        return extraPairs;
    }

    public int leftFall(Domain D) {
        int extraPairs = 0;
        this.leftStrand.name = "";
        left_toehold_available = true;
        has_Left_Hanging = false;
        extraPairs -= D.Left_length;
        //System.out.println("left fall by " + extraPairs);

        return extraPairs;
    }

    public int rightFall(Domain D) {
        int extraPairs = 0;
        this.rightStrand.name = "";
        Right_toehold_available = true;
        has_Right_Hanging = false;
        extraPairs -= D.Right_length;
        //System.out.println("right fall by " + extraPairs);

        return extraPairs;
    }

    public void beDisplacedFromRightToLeft(Strand s) {
        this.middleStrand = s;
        this.rightStrand.name = "";
        this.Right_toehold_available = false;
        this.leftStrand.name = "";
    }

    public void beDisplacedFromLeftToRight(Strand s) {
        this.middleStrand = s;
        this.leftStrand.name = "";
        this.left_toehold_available = false;
        this.has_Left_Hanging = false;
        this.left_match = true;

        this.rightStrand.name = "";
    }

    public void previousMiddleBeMyLeft(Strand s) {
        this.leftStrand = s;
        this.left_toehold_available = false;
    }

    public void nextMiddleBeMyRight(Strand s) {
        this.rightStrand = s;
        this.Right_toehold_available = false;
    }

}
