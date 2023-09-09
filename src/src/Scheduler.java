package src;
import java.util.*;

public class Scheduler {

	public static void runFCFS(List<Process> processes) {
	    // Implementação do algoritmo FCFS
	    // Ordena os processos com base no tempo de chegada
	    processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

	    int currentTime = 0;
	    double totalExecutionTime = 0;
	    double totalWaitingTime = 0;
	    List<String> executionOrderString = new ArrayList<>();
	    
	    for (Process process : processes) {
	        if (currentTime < process.arrivalTime) {
	            currentTime = process.arrivalTime;
	        }

	        executionOrderString.add(process.pid);
	        totalExecutionTime += currentTime + process.burstTime; // Correção aqui
	        totalWaitingTime += currentTime - process.arrivalTime;

	        currentTime += process.burstTime;
	    }

	    double averageExecutionTime = totalExecutionTime / processes.size();
	    double averageWaitingTime = totalWaitingTime / processes.size();

        System.out.println("\n---------------------------------------- Algoritmo FCFS ----------------------------------------");
	    System.out.println("\nOrdem de execução dos processos: " + executionOrderString);
	    System.out.println("Tempo médio de execução: " + averageExecutionTime);
	    System.out.println("Tempo médio de espera: " + averageWaitingTime);
	}


    public static void runRR(List<Process> processes, int quantum) {     // Implementação do algoritmo RR
    	
        Queue<Process> readyQueue = new LinkedList<>();
        //List<Integer> executionOrder = new ArrayList<>();
        List<String> executionOrderString = new ArrayList<>();

        int currentTime = 0;
        double totalExecutionTime = 0;
        double totalWaitingTime = 0;
        int totalProcessesExecuted = 0; // Novo contador

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.poll();
                executionOrderString.add(currentProcess.pid);
                int remainingTime = currentProcess.burstTime;

                if (remainingTime <= quantum) {
                    currentTime += remainingTime;
                    totalExecutionTime += remainingTime;
                    totalWaitingTime += currentTime - currentProcess.arrivalTime - currentProcess.burstTime;
                    totalProcessesExecuted++; // Incrementa o contador
                } else {
                    currentTime += quantum;
                    totalExecutionTime += quantum;
                    totalWaitingTime += currentTime - currentProcess.arrivalTime - quantum;
                    readyQueue.add(new Process(currentProcess.pid, currentProcess.arrivalTime, remainingTime - quantum, currentProcess.priority, currentProcess.type));
                }
            } else {
                currentTime++;
            }
        }

        double averageExecutionTime = totalProcessesExecuted > 0 ? totalExecutionTime / totalProcessesExecuted : 0;
        double averageWaitingTime = totalProcessesExecuted > 0 ? totalWaitingTime / totalProcessesExecuted : 0;

        System.out.println("\n---------------------------------------- Algoritmo RR ----------------------------------------");
        System.out.println("\nOrdem de execução dos processos: " + executionOrderString);
        System.out.println("Tempo médio de execução: " + averageExecutionTime);
        System.out.println("Tempo médio de espera: " + averageWaitingTime);
    }

    public static void runSJF(List<Process> processes) {
        // Implementação do algoritmo SJF
        List<Process> readyQueue = new ArrayList<>();
        List<String> executionOrderString = new ArrayList<>();
        
        int currentTime = 0;
        double totalExecutionTime = 0;
        double totalWaitingTime = 0;

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(p -> p.burstTime)); // Ordena a fila pronta pelo tempo de burst

                Process currentProcess = readyQueue.remove(0);
                executionOrderString.add(currentProcess.pid);
                int remainingTime = currentProcess.burstTime;

                currentTime += remainingTime;
    	        totalExecutionTime += currentTime; 
                totalWaitingTime += currentTime - currentProcess.arrivalTime - remainingTime;
            } else {
                currentTime++;
            }
        }

        double averageExecutionTime = executionOrderString.size() > 0 ? totalExecutionTime / executionOrderString.size() : 0;
        double averageWaitingTime = executionOrderString.size() > 0 ? totalWaitingTime / executionOrderString.size() : 0;

        System.out.println("\n---------------------------------------- Algoritmo SJF ----------------------------------------");
        System.out.println("\nOrdem de execução dos processos: " + executionOrderString);
        System.out.println("Tempo médio de execução: " + averageExecutionTime);
        System.out.println("Tempo médio de espera: " + averageWaitingTime);
    }



    public static void runSRTF(List<Process> processes) {
        // Implementação do algoritmo SRTF preemptivo

        List<Process> readyQueue = new ArrayList<>();
        List<String> executionOrderString = new ArrayList<>();

        int currentTime = 0;
        double totalExecutionTime = 0;
        double totalWaitingTime = 0;

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            if (!readyQueue.isEmpty()) {
                // Ordena a fila pronta pelo tempo de burst
                readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));

                Process currentProcess = readyQueue.get(0);
                executionOrderString.add(currentProcess.pid);

                int remainingTime = currentProcess.burstTime;
                int executionInterval = 0; // Contador para o tempo de execução atual

                while (remainingTime > 0) {
                    currentTime++;
                    remainingTime--;
                    executionInterval++;

                    if (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime &&
                        processes.get(0).burstTime < currentProcess.burstTime) {
                        // Preempção: Coloca o processo de maior burst de volta na fila
                        readyQueue.add(currentProcess);
                        currentProcess = processes.remove(0);
                        readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));
                        executionInterval = 0; // Reinicia o contador de execução
                    }
                }

                totalExecutionTime += executionInterval;
                totalWaitingTime += currentTime - currentProcess.arrivalTime - executionInterval;
                
                // Remova o processo atual da lista de processos para evitar vazamento de memória
                readyQueue.remove(currentProcess);
            } else {
                currentTime++;
            }
        }

        double averageExecutionTime = executionOrderString.size() > 0 ? totalExecutionTime / executionOrderString.size() : 0;
        double averageWaitingTime = executionOrderString.size() > 0 ? totalWaitingTime / executionOrderString.size() : 0;

        System.out.println("\n---------------------------------------- Algoritmo SRTF ----------------------------------------");
        System.out.println("\nOrdem de execução dos processos: " + executionOrderString);
        System.out.println("Tempo médio de execução: " + averageExecutionTime);
        System.out.println("Tempo médio de espera: " + averageWaitingTime);
    }

    public static void runPrioc(List<Process> processes, int quantum) {
        Queue<Process> readyQueue = new LinkedList<>();
        List<String> executionOrderString = new ArrayList<>();

        int currentTime = 0;
        double totalExecutionTime = 0;
        double totalWaitingTime = 0;

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.poll();
                executionOrderString.add(currentProcess.pid);
                int remainingTime = currentProcess.burstTime;

                if (remainingTime <= quantum) {
                    currentTime += remainingTime;
                    totalExecutionTime += remainingTime;
                    totalWaitingTime += currentTime - currentProcess.arrivalTime - currentProcess.burstTime;
                    
                } else {
                    currentTime += quantum;
                    totalExecutionTime += quantum;
                    totalWaitingTime += currentTime - currentProcess.arrivalTime - quantum;
                    currentProcess.burstTime -= quantum;
                    readyQueue.add(currentProcess);
                }
                readyQueue.remove(currentProcess);
            } else {
                currentTime++;
            }
        }

        double averageExecutionTime = executionOrderString.size() > 0 ? totalExecutionTime / executionOrderString.size() : 0;
        double averageWaitingTime = executionOrderString.size() > 0 ? totalWaitingTime / executionOrderString.size() : 0;


        System.out.println("\n---------------------------------------- Algoritmo Prioc ----------------------------------------");
        System.out.println("\nOrdem de execução dos processos: " + executionOrderString);
        System.out.println("Tempo médio de execução: " + averageExecutionTime);
        System.out.println("Tempo médio de espera: " + averageWaitingTime);
    }




 // Implementação do algoritmo Priop com escalonamento preemptivo
    public static void runPriop(List<Process> processes) {
        List<Process> readyQueue = new ArrayList<>();
        //List<Integer> executionOrder = new ArrayList<>();
        List<String> executionOrderString = new ArrayList<>();

        int currentTime = 0;
        double totalExecutionTime = 0;
        double totalWaitingTime = 0;
        Process currentProcess = null;

        while (!processes.isEmpty() || currentProcess != null || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            if (currentProcess == null && !readyQueue.isEmpty()) {
                // Se não houver processo em execução, escolha o processo com a maior prioridade (menor valor)
                readyQueue.sort(Comparator.comparingInt(p -> p.priority));
                currentProcess = readyQueue.remove(0);
            } else if (currentProcess != null && !readyQueue.isEmpty() && readyQueue.get(0).priority < currentProcess.priority) {
                // Se houver um processo em execução e um processo com uma prioridade maior (menor valor) na fila pronta, preempção
                readyQueue.add(currentProcess);
                currentProcess = readyQueue.remove(0);
            }

            if (currentProcess != null) {
                executionOrderString.add(currentProcess.pid);
                int remainingTime = currentProcess.burstTime;

                currentTime++;
                remainingTime--;

                currentProcess.burstTime = remainingTime;

                totalExecutionTime++;
                totalWaitingTime += currentTime - currentProcess.arrivalTime - 1;

                if (remainingTime == 0) {
                    currentProcess = null;
                }
            } else {
                currentTime++;
            }
        }

        double averageExecutionTime = executionOrderString.size() > 0 ? totalExecutionTime / executionOrderString.size() : 0;
        double averageWaitingTime = executionOrderString.size() > 0 ? totalWaitingTime / executionOrderString.size() : 0;

        System.out.println("\n---------------------------------------- Algoritmo Priop ----------------------------------------");
        System.out.println("\nOrdem de execução dos processos: " + executionOrderString);
        System.out.println("Tempo médio de execução: " + averageExecutionTime);
        System.out.println("Tempo médio de espera: " + averageWaitingTime);
    }


}