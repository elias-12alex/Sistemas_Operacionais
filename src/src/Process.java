package src;


class Process {
    String pid;
    int arrivalTime;
    int burstTime;
    int priority;
    int type; // 1: CPU bound, 2: I/O bound, 3: ambos

    public Process(String pid, int arrivalTime, int burstTime, int priority, int type) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.type = type;
    }
}