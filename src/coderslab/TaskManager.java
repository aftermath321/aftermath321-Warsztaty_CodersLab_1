package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

  static final String nazwaPliku = "tasks.csv";
  static final String[] menuGlowne = {"add", "remove", "list", "exit"};
  static String[][] tasks;

  public static void main(String[] args) {

    selectOption();
  }

  public static void saveFile(String[][] tabela2) {
    Path path1 = Paths.get("tasks.csv");
    String[] saved = new String[tasks.length];
    for (int i = 0; i < tabela2.length; i++) {
      saved[i] = String.join(", ", tabela2[i]);
    }
    try {
      Files.write(path1, Arrays.asList(saved));
    } catch (IOException ex) {
      System.out.println("Nie można zapisać pliku.");
    }
  }

  public static void removeTask(String[][] tabela, int index) {
    try {
      if (index < tabela.length) {
        tasks = ArrayUtils.remove(tabela, index);
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("No such records, out of bounds");
    }
  }

  public static int removeNumber() {
    Scanner scan3 = new Scanner(System.in);
    System.out.println("Please enter number you wish to remove");
    String liczba = scan3.nextLine();
    while (!zeroValidation(liczba)) {
      System.out.println("Number must equal or be greather than 0");
      scan3.nextLine();
    }
    return Integer.parseInt(liczba);
  }

  public static boolean zeroValidation(String number) {
    if (NumberUtils.isParsable(number)) {
      return Integer.parseInt(number) >= 0;
    }
    return false;
  }

  public static void addTask() {
    Scanner scan2 = new Scanner(System.in);
    System.out.println("Please add description:");
    String description = scan2.nextLine();
    System.out.println("Please add Due Date:");
    String deadLine = scan2.nextLine();
    System.out.println("Please select whether it is imporant (true/false):");
    String urgent = scan2.nextLine();
    tasks = Arrays.copyOf(tasks, tasks.length + 1);
    tasks[tasks.length - 1] = new String[3];
    tasks[tasks.length - 1][0] = description;
    tasks[tasks.length - 1][1] = deadLine;
    tasks[tasks.length - 1][2] = urgent;
  }

  public static String[][] odczyt(String fileName) {
    Path path = Paths.get(nazwaPliku);
    if (!Files.exists(path)) {
      System.out.println("File not exist.");
      System.exit(0);
    }
    String[][] tabela = null;
    try {
      List<String> dane = Files.readAllLines(path);
      tabela = new String[dane.size()][dane.get(0).split(",").length];

      for (int i = 0; i < dane.size(); i++) {
        String[] splitted = dane.get(i).split(",");
        for (int j = 0; j < splitted.length; j++) {
          tabela[i][j] = splitted[j];
        }
      }
    } catch (IOException ex) {
      System.err.println("Can't read the file");
    }
    return tabela;
  }

  public static void textMenu(String[] tab) {
    System.out.println(ConsoleColors.BLUE);
    System.out.println("Please select an option: " + ConsoleColors.RESET);
    for (String option : tab) {
      System.out.println(option);
    }
  }

  public static void selectOption() {
    tasks = odczyt(nazwaPliku);
    textMenu(menuGlowne);
    Scanner scan1 = new Scanner(System.in);
    while (scan1.hasNextLine()) {
      String input = scan1.nextLine();
      switch (input) {
        case "list":
          listTasks(tasks);
          break;
        case "remove":
          removeTask(tasks, removeNumber());
          break;
        case "exit":
          saveFile(tasks);
          System.out.println(ConsoleColors.RED);
          System.out.println("Bye, bye.");
          System.exit(0);
        case "add":
          addTask();
          break;
        default:
          System.out.println("Please select a correct option");
      }
      textMenu(menuGlowne);
    }
  }

  public static void listTasks(String[][] tabela) {
    for (int i = 0; i < tabela.length; i++) {
      System.out.print(i + " : ");
      for (int j = 0; j < tabela[i].length; j++) {
        System.out.print(tabela[i][j] + " ");
      }
      System.out.println();
    }
  }
}
