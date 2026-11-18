/*
 * Course: ITCS325 - Operating Systems (First Semester 2026-2027)
 * Project: Priority Scheduling with Round-Robin
 * Group Members:
 * 1. Yasser Ammar Al Masri - 202407061
 * 2. [Member 2 Name] - [Student ID]
 * 3. [Member 3 Name] - [Student ID]
 * 4. [Member 4 Name] - [Student ID]
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("CPU Scheduling: Priority with Round-Robin");
        int quantum = readPositiveInt(scanner, "Enter the time quantum for Round-Robin scheduling: ");
        if (quantum == -1) {
            System.out.println("No quantum provided. Exiting program.");
            scanner.close();
            return;
        }

        List<Process> processes = new ArrayList<>();
        Set<Integer> usedPids = new HashSet<>();
        System.out.println("\nEnter process details as: [PID, Arrival_Time, Burst_Time, Priority]");
        System.out.println("(Smaller priority number = higher priority. Enter 0000 or 0 0 0 0 to stop)");
        while (true) {
            System.out.print("Process " + (processes.size() + 1) + " > ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.equals("0000")) {
                break;
            }

            String[] parts = line.split("\\s+");
            if (parts.length != 4) {
                System.out.println("Invalid input: enter exactly 4 integers (PID Arrival Burst Priority) or 0000.");
                continue;
            }
            Integer pid = parseIntOrNull(parts[0]);
            Integer arrivalTime = parseIntOrNull(parts[1]);
            Integer burstTime = parseIntOrNull(parts[2]);
            Integer priority = parseIntOrNull(parts[3]);
            if (pid == null || arrivalTime == null || burstTime == null || priority == null) {
                System.out.println("Invalid input: all four fields must be integers.");
                continue;
            }
            if (pid == 0 && arrivalTime == 0 && burstTime == 0 && priority == 0) {
                break;
            }
            if (pid <= 0 || burstTime <= 0 || arrivalTime < 0 || priority < 0) {
                System.out.println(
                        "Invalid input: PID and Burst Time must be > 0; Arrival Time and Priority must be >= 0.");
                continue;
            }
            if (!usedPids.add(pid)) {
                System.out.println("Invalid input: PID " + pid + " already exists. PID must be unique.");
                continue;
            }

            processes.add(new Process(pid, arrivalTime, burstTime, priority));
            System.out.println("Added P" + pid + ".");
        }
        if (processes.isEmpty()) {
            System.out.println("No processes were entered. Exiting.");
            scanner.close();
            return;
        }
        List<GanttBlock> ganttChart = Scheduler.schedule(processes, quantum);
        Scheduler.printGanttChart(ganttChart);
        Scheduler.printResults(processes);
        scanner.close();
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return -1;
            }
            String line = scanner.nextLine().trim();
            Integer value = parseIntOrNull(line);
            if (value != null && value > 0) {
                return value;
            }
            System.out.println("Invalid input. Please enter a positive integer.");
        }
    }

    private static Integer parseIntOrNull(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}