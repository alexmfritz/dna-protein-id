import java.util.*;
import java.io.*;

public class DNA {
    public static final int MIN_CODONS = 5;
    public static final int MIN_CG = 30;
    public static final int NUCLEOTIDES = 4;
    public static final int NUCLEOTIDES_PER_CODON = 3;
    public static final double[] NUCLEOTIDE_MASSES = {135.128, 111.10, 151.128, 125.107};
    public static final double JUNK_MASS = 100.0;
    

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("This program reports information about DNA");
        System.out.println("nucleotide sequences that may encode proteins.");

        Scanner console = new Scanner(System.in);
        System.out.print("Input file name? ");
        String iFilePath = console.nextLine();
        System.out.print("Output file name? ");
        String oFilePath = console.nextLine();

        File iFile = new File(iFilePath);
        File oFile = new File(oFilePath);

        Scanner input = new Scanner(iFile);
        PrintStream output = new PrintStream(oFile);

        while (input.hasNextLine()) {
            String name = input.nextLine();
            String sequence = input.nextLine().toUpperCase();

            int[] nucleoCount = countNucleotides(sequence);
            int dashCount = sequence.length() - sequence.replace("-", "").length();

            double totalMass = getTotalMass(nucleoCount, dashCount);
            double[] massPercentages = getTotalMassPercentages(nucleoCount, totalMass);

            String sequenceNoDashes = sequence.replace("-", "");
            String[] codons = getCodons(sequenceNoDashes);
            boolean isValidProtein = isProtein(codons, massPercentages);

            outputResults(output, name, sequence, nucleoCount, totalMass, massPercentages, codons, isValidProtein);
        }
    }

    // Returns array index of provided nucleotide character
    // A = 0, C = 1, G = 2, T = 3
    public static int indexNucleotide(char nucleotide) {
        return nucleotide == 'A' ? 0 : nucleotide == 'C' ? 1 : nucleotide == 'G' ? 2 : 3;
    }

    // Returns occurences of each nucleotide (A, C, G, T) in the provided sequence
    // Ignores all dash characters
    // Returns int array the length of NUCLEOTIDES with counts in same order as A, C, G, T 
    public static int[] countNucleotides(String sequence) {
        int[] nucleoCount = new int[NUCLEOTIDES];

        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if (c != '-') {
                nucleoCount[indexNucleotide(c)]++;
            }
        }

        return nucleoCount;
    }

    // Returns the raw total mass of the sequence including junk mass from dashes
    // Value is representative of mass before percentage calculation
    public static double getTotalMass(int[] nucleoCount, int dashCount) {
        double totalMass = dashCount * JUNK_MASS;

        for (int i = 0; i < NUCLEOTIDES; i++) {
            totalMass += nucleoCount[i] * NUCLEOTIDE_MASSES[i];
        }

        return totalMass;
    }

    // Returns rounded (to 1 decimal place) mass percentages for each nucleotide
    // Returns double array the length of NUCLEOTIDES with mass percentages in same order as A, C, G, T 
    public static double[] getTotalMassPercentages(int[] nucleoCount, double totalMass) {
        double[] massPercentages = new double[NUCLEOTIDES];

        for (int i = 0; i < NUCLEOTIDES; i++) {
            double rawPercentage = nucleoCount[i] * NUCLEOTIDE_MASSES[i] / totalMass * 100.0;
            massPercentages[i] = Math.round(rawPercentage * 10.0) / 10.0;
        }

        return massPercentages;
    }

    // Takes in String sequence of nucleotides without dashes included
    // Returns an array of Strings organized into 3-nucleotide Codons
    // Where each Codon is a group of nucleotides defined by NUCLEOTIDES_PER_CODON characters
    public static String[] getCodons(String sequence) {
        int codonCount = sequence.length() / NUCLEOTIDES_PER_CODON;
        String[] codons = new String[codonCount];

        for (int i = 0; i < codonCount; i++) {
            int start = i * NUCLEOTIDES_PER_CODON;

            codons[i] = sequence.substring(start, start + NUCLEOTIDES_PER_CODON);
        }

        return codons;
    }

    // Return whether a nucleotide sequence is a valid protein
    // Valid proteins must start with ATG codon, end with either TAA, TAG, or TGA codons
    // Valid proteins must also have a minimum count of codons and the mass of C + G combined must be 
    // equal to or greater than MIN_CG
    public static boolean isProtein(String[] codons, double[] massPercentages) {
        if (codons.length < MIN_CODONS) return false;

        String firstCodon = codons[0];
        String lastCodon = codons[codons.length - 1];
        
        boolean ATG = firstCodon.equals("ATG");
        boolean others = lastCodon.equals("TAA") || lastCodon.equals("TAG") || lastCodon.equals("TGA");
        boolean minMass = massPercentages[1] + massPercentages[2] >= MIN_CG;

        return ATG && others && minMass;
    }

    // Outputs all required data for single nucleotide sequence to provided PrintStream
    public static void outputResults(PrintStream output, String name, String sequence, int[] nucleoCount, double totalMass, double[] massPercentages, String[] codons, boolean isProtein) {
        output.println("Region name: " + name);
        output.println("Nucleotides: " + sequence);
        output.println("Nuc. Counts: " + Arrays.toString(nucleoCount));
        output.println("Total Mass%: " + Arrays.toString(massPercentages) + " of " + Math.round(totalMass * 10.0) / 10.0);
        output.println("Codons List: " + Arrays.toString(codons));
        output.println("Is Protein?: " + (isProtein ? "YES" : "NO"));
        output.println();
    }
}
