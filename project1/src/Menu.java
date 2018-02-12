import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
  
  public static void main (String[] args) throws FileNotFoundException {
    // Get file name
    File file = args.length != 0 ? new File(args[0]) : getFileName();
    // Read processes
    Process[] processes = getProcesses(file, true);
    // Run algorithms
    System.out.println("\nRunning algorithms...");
    ProcessLog fcfs = ProcessScheduler.firstComeFirstServe(clone(processes));
    ProcessLog sjf = ProcessScheduler.shortestJobFirst(clone(processes));
    ProcessLog rr25 = ProcessScheduler.roundRobin(clone(processes), 25);
    ProcessLog rr50 = ProcessScheduler.roundRobin(clone(processes), 50);
    ProcessLog lottery = ProcessScheduler.lottery(clone(processes), 50);

    // Create output folder and write output CSV files there
    (new File("output")).mkdir();
    String file_name_suffix = "-" + file.getName().split("\\.(?=[^\\.]+$)")[0] + ".csv";
    fcfs.writeTo("FCFS" + file_name_suffix);
    sjf.writeTo("SJF" + file_name_suffix);
    rr25.writeTo("RR25" + file_name_suffix);
    rr50.writeTo("RR50" + file_name_suffix);
    lottery.writeTo("Lottery50" + file_name_suffix);

    writeAverages("Aaverages" + file_name_suffix, fcfs, sjf, rr25, rr50, lottery);
    System.out.println("Output in ./output/");
  }

  private static void writeAverages (String file_name, ProcessLog fcfs, ProcessLog sjf, ProcessLog rr25, ProcessLog rr50, ProcessLog lottery) throws FileNotFoundException {
    PrintWriter print_writer = new PrintWriter("output/" + file_name);
    print_writer.println("Algorithm, AverageCompletionTime,");
    print_writer.println("     FCFS, " + fcfs.getAverage());
    print_writer.println("      SJF, " + sjf.getAverage());
    print_writer.println("     RR25, " + rr25.getAverage());
    print_writer.println("     RR50, " + rr50.getAverage());
    print_writer.println("Lottery50, " + lottery.getAverage());
    print_writer.close();
  }

  // Deep clone processes array
  private static Process[] clone(Process[] processes) {
    Process[] cloned = new Process[processes.length];
    for (int i = 0; i < processes.length; i++) {
      cloned[i] = processes[i].clone();
    }
    return cloned;
  }

  // Ask the user to enter a file name
  private static File getFileName () {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.print("Enter file name:\n: ");
      String file_name = scanner.nextLine();
      File file = new File(file_name);
      if (file.exists() && !file.isDirectory()) {
        return file;
      }
      System.out.println("Invalid file: " + file_name);
    }
  }

  // Load processes from input file
  private static Process[] getProcesses (File file, boolean verbose) throws FileNotFoundException {
    System.out.println("\nReading processes...\n");
    Scanner scanner = new Scanner(file);
    ArrayList<Process> processes = new ArrayList<>();
    while (scanner.hasNextInt()) {
      int pid = scanner.nextInt();
      int burst_time = scanner.nextInt();
      int priority = scanner.nextInt();
      Process process = new Process(pid, burst_time, priority);
      System.out.println(process);
      processes.add(process);
    }
    Process[] processes_array = processes.toArray(new Process[processes.size()]);
    return processes_array;
  }
}
