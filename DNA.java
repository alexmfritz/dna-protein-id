import java.util.*;
import java.io.*;

public class DNA {
    

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
}
