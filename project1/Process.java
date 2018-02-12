public class Process implements Comparable<Process> {
  final int pid;
  final int priority;
  int burst_time;
  int end_time;

  Process (int pid, int burst_time, int priority) {
    this.pid = pid;
    this.burst_time = burst_time;
    this.priority = priority;
  }

  // Return JSON of this process
  public String toString () {
    return "PID " + this.pid + ": {\n  burst_time: " + this.burst_time + "\n  priority: " + this.priority + "\n},";
  }

  @Override
  public int compareTo (Process process) {
    return this.burst_time - process.burst_time;
  }

  // Create a deep clone of this process in its current state
  public Process clone () {
    return new Process(this.pid, this.burst_time, this.priority);
  }

  // Process this process for 'this.burst_time' units starting at 'cpu_time'  
  public int compute (ProcessLog process_log, int cpu_time) {
    return compute(process_log, cpu_time, this.burst_time);
  }

  // Process this process for 'quantum' units starting at 'cpu_time'
  public int compute(ProcessLog process_log, int cpu_time, int quantum) {
    if (quantum >= this.burst_time) {
      this.end_time = cpu_time + this.burst_time;
      int time_spent = this.burst_time;
      process_log.add(cpu_time, this.pid, this.burst_time, 0, this.end_time);
      this.burst_time = 0;
      return time_spent;
    } else {
      process_log.add(cpu_time, this.pid, this.burst_time, this.burst_time - quantum, 0);      
      this.burst_time -= quantum;
      return quantum;
    }
  }
}
