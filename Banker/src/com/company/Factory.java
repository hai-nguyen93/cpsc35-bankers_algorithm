package com.company;

// Factory.java
//
// Factory class that creates the bank and each bank customer
// Usage:  java Factory 10 5 7

import java.io.*;
import java.util.*;

public class Factory {
    public static void main(String[] args) {
        String filename = "./src/com/company/infile.txt";
        int nResources = args.length;
        int[] resources = new int[nResources];
        for (int i = 0; i < nResources; i++) { resources[i] = Integer.parseInt(args[i].trim()); }

        Bank theBank = new BankImpl(resources);
        int[] maxDemand = new int[nResources];
        int[] allocated = new int[nResources];
        Thread[] workers = new Thread[Customer.COUNT];      // the customers
        int threadNum = 0;

        try {
            File f = new File(filename);
            System.out.println(f.getAbsolutePath());
            Scanner read = new Scanner(f);
            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] tokens = line.split(",");

                for(int i = 0; i < tokens.length/2; ++i){
                    allocated[i] = Integer.parseInt(tokens[i]);
                }
                for(int i = tokens.length/2; i < tokens.length; ++i){
                    maxDemand[i-nResources] = Integer.parseInt(tokens[i]);
                }

                workers[threadNum] = new Thread(new Customer(threadNum, maxDemand, theBank));
                theBank.addCustomer(threadNum, allocated, maxDemand);

                ++threadNum;        //theBank.getCustomer(threadNum);
            }
            read.close();
        } catch (FileNotFoundException fnfe) {
            throw new Error("Unable to find file \"" + filename + "\"");
        } catch (IOException ioe) { throw new Error("Error processing \"" + filename + "\""); }

        System.out.println("FACTORY: created threads");     // start the customers

        for (int i = 0; i < Customer.COUNT; i++) { workers[i].start(); }
        System.out.println("FACTORY: started threads");
    }
}
