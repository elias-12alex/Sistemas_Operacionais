/*
 * 	Trabalho1_Sistemas_Operacionais - UFAM
 * 	Escalonamento_de_Processos
 * 	Autor_@eliasalex_
 * 
 */

package src;

import java.util.*;

public class Main extends FileRead {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Digite o nome do arquivo de entrada: ");
            String fileName = scanner.nextLine();

            List<Process> processes = readProcessesFromFile(fileName);

            if (processes.isEmpty()) {
                System.out.println("Arquivo de entrada vazio ou inválido. Tente novamente.");
                continue;
            }

            System.out.print("Escolha o algoritmo de escalonamento (FCFS, RR, SJF, SRTF, Prioc, Priop): ");
            String algorithm = scanner.nextLine();

            int quantum = 0;

            if (algorithm.equals("RR") || algorithm.equals("Priop")) {
                System.out.print("Digite o valor do quantum: ");
                quantum = scanner.nextInt();
            }

            switch (algorithm) {
                case "FCFS":
                    Scheduler.runFCFS(new ArrayList<>(processes));
                    break;
                case "RR":
                    Scheduler.runRR(new ArrayList<>(processes), quantum);
                    break;
                case "SJF":
                    Scheduler.runSJF(new ArrayList<>(processes));
                    break;
                case "SRTF":
                    Scheduler.runSRTF(new ArrayList<>(processes));
                    break;
                case "Prioc":
                    Scheduler.runPrioc(new ArrayList<>(processes), quantum);
                    break;
                case "Priop":
                    Scheduler.runPriop(new ArrayList<>(processes));
                    break;
                default:
                    System.out.println("Algoritmo de escalonamento inválido.");
            }

            System.out.print("Pressione ENTER para continuar\n");
            scanner.nextLine(); // Limpa o buffer do scanner

            System.out.print("Deseja executar novamente (S/N)? ");
            String response = scanner.nextLine().trim().toUpperCase();
            if (!response.equals("S")) {
                break;
            }
        }
        scanner.close();
    }
}
