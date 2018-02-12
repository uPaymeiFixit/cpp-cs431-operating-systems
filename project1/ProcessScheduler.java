import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ProcessScheduler {

  final static int swap_time = 3;

  public static ProcessLog firstComeFirstServe (Process[] processes) {
    int cpu_time = 0;
    ProcessLog process_log = new ProcessLog();
    for (Process process : processes) {
      cpu_time += process.compute(process_log, cpu_time);
      cpu_time += swap_time;
    }
    return process_log;
  }

  public static ProcessLog shortestJobFirst (Process[] processes) {
    int cpu_time = 0;
    Arrays.sort(processes);
    ProcessLog process_log = new ProcessLog();
    for (Process process : processes) {
      cpu_time += process.compute(process_log, cpu_time);
      cpu_time += swap_time;     
    }
    return process_log;
  }

  public static ProcessLog roundRobin (Process[] processes, int quantum) {
    int cpu_time = 0;
    ArrayList<Process> processes_list = new ArrayList<>(Arrays.asList(processes));
    ProcessLog process_log = new ProcessLog();
    int i = 0;
    while (processes_list.size() != 0) {
      Process process = processes_list.get(i % processes_list.size());

      cpu_time += process.compute(process_log, cpu_time, quantum);
      if (process.end_time != 0) {
        processes_list.remove(process);
      }

      if (processes_list.size() != 1) {
        cpu_time += swap_time;
      }
      i++;   
    }
    return process_log;
  }

  public static ProcessLog lottery (Process[] processes, int quantum) {
    int cpu_time = 0;
    Random random = new Random();
    ArrayList<Process> processes_list = new ArrayList<>(Arrays.asList(processes));
    ProcessLog process_log = new ProcessLog();
    while (processes_list.size() != 0) {
      
      // Pick lottery number
      int priority_sum = 0;
      for (Process process : processes_list) {
        priority_sum += process.priority;
      }
      int i = random.nextInt(priority_sum);
      priority_sum = 0;
      for (int j = 0; j < processes_list.size(); j++) {
        priority_sum += processes_list.get(j).priority;
        if (priority_sum >= i) {
          i = j;
          break;
        }
      }

      Process process = processes_list.get(i);

      cpu_time += process.compute(process_log, cpu_time, quantum);
      if (process.end_time != 0) {
        processes_list.remove(process);
      }
      
      if (processes_list.size() != 1) {
        cpu_time += swap_time;
      }          
    }
    return process_log;
  }



}
