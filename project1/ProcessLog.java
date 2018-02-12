import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class ProcessLog {
  public ArrayList<ProcessEntry> entries = new ArrayList<>(); 

  public void add (int cpu_time, int pid, int start_burst_time, int end_burst_time, int completion_time) {
    ProcessEntry entry = new ProcessEntry(cpu_time, pid, start_burst_time, end_burst_time, completion_time);
    entries.add(entry);
  }

  // Write process scheduler output to file
  public void writeTo (String file_name) throws FileNotFoundException {
    PrintWriter print_writer = new PrintWriter("output/" + file_name);
    print_writer.println("CpuTime, PID, StartingBurstTime, EndingBurstTime, CompletionTime,");
    for (ProcessEntry entry : this.entries) {
      print_writer.printf("%7d, %3d, %17d, ", entry.cpu_time, entry.pid, entry.start_burst_time);
      print_writer.printf("%15d, %14d,\n", entry.end_burst_time, entry.completion_time);
    }
    print_writer.close();
  }

  public double getAverage () {
    double sum = 0;
    for (ProcessEntry entry : this.entries) {
      sum += entry.completion_time;
    }
    return sum / this.entries.size();
  }
}

class ProcessEntry {
  public int cpu_time;
  public int pid; 
  public int start_burst_time;
  public int end_burst_time;
  public int completion_time;

  public ProcessEntry (int cpu_time, int pid, int start_burst_time, int end_burst_time, int completion_time) {
    this.cpu_time = cpu_time;
    this.pid = pid;
    this.start_burst_time = start_burst_time;
    this.end_burst_time = end_burst_time;
    this.completion_time = completion_time;
  }
}
