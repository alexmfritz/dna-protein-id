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
    }

    // Return array index of provided nucleotide character
    // A = 0, C = 1, G = 2, T = 3
    public static int indexNucleotide(char nucleotide) {
        return nucleotide == 'A' ? 0 : nucleotide == 'C' ? 1 : nucleotide == 'G' ? 2 : 3;
    }

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

    public static double getTotalMass(int[] nucleoCount, int dashCount) {
        double totalMass = dashCount * JUNK_MASS;

        for (int i = 0; i < NUCLEOTIDES; i++) {
            totalMass += nucleoCount[i] * NUCLEOTIDE_MASSES[i];
        }

        return totalMass;
    }
}
