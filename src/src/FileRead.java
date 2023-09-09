package src;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileRead {
	public static List<Process> readProcessesFromFile(String fileName) {
        List<Process> processes = new ArrayList<>();

        /*try (Scanner scanner = new Scanner(new File(fileName))) {
            scanner.nextLine(); // Descarte a linha de cabeçalho

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\s+");
                int pid = Integer.parseInt(parts[0]);
                int arrivalTime = Integer.parseInt(parts[1]);
                int burstTime = Integer.parseInt(parts[2]);
                int priority = Integer.parseInt(parts[3]);
                int type = Integer.parseInt(parts[4]);

                processes.add(new Process(pid, arrivalTime, burstTime, priority, type));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo não encontrado.");
            System.exit(1);
        }

        return processes;*/
        
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                String[] parts = line.split("\\s+");
                //int pid = Integer.parseInt(parts[0]);
                String pid = parts[0];
                int arrivalTime = Integer.parseInt(parts[1]);
                int burstTime = Integer.parseInt(parts[2]);
                int priority = Integer.parseInt(parts[3]);
                int type = Integer.parseInt(parts[4]);
                processes.add(new Process(pid, arrivalTime, burstTime, priority, type));

                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return processes;
    }
}
