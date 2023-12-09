import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class DeliveryRunner {
    private static final String TASK_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\Task.txt";
    private static final String COURIER_TASKS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\CourierTasks.txt";

    public static void Menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Выберите действие:");
            System.out.println("1. Просмотр задачи");
            System.out.println("2. Принять/отклонить задание");
            System.out.println("3. Обновить статус задачи");
            System.out.println("4. Проверить историю задач");
            System.out.println("5. Прочитать отзыв клиента");
            System.out.println("6. Панель мониторинга доходов");
            System.out.println("0. Выйти из программы");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewTask();
                    break;
                case 2:
                    acceptOrRejectTask();
                    break;
                case 3:
                    updateTaskStatus();
                    break;
                case 4:
                    viewTaskHistory();
                    break;
                case 5:
                    readClientReview();
                    break;
                case 6:
                    incomeMonitoringPanel();
                    break;
                case 0:
                    System.out.println("Программа завершена.");
                    return;
                default:
                    System.out.println("Выбрано некорректное действие. Пожалуйста, выберите снова.");
            }
        }
    }

    private static void viewTask() {
        try {
            File taskFile = new File(TASK_FILE_PATH);
            Scanner fileScanner = new Scanner(taskFile);

            while (fileScanner.hasNextLine()) {
                String data = fileScanner.nextLine();
                System.out.println(data);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл задач не найден.");
        }
    }





    private static void  acceptOrRejectTask() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Меню выбора:");
            System.out.println("1. Принять заказ");
            System.out.println("2. Отклонить заказ");
            System.out.println("3. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    acceptOrder();
                    break;
                case 2:
                    rejectOrder();
                    break;
                case 3:
                    System.out.println("Выход из меню выбора.");
                    return;
                default:
                    System.out.println("Выбран некорректный пункт меню.");
            }
        }
    }

    private static void acceptOrder() {
        try {
            File taskFile = new File(TASK_FILE_PATH);
            Scanner fileScanner = new Scanner(taskFile);

            ArrayList<String> tasks = new ArrayList<>();

            // Записать все задачи из Task.txt в список
            while (fileScanner.hasNextLine()) {
                String task = fileScanner.nextLine();
                tasks.add(task);
            }
            fileScanner.close();

            // Вывести список задач с порядковыми номерами
            int taskNumber = 1;
            for (String task : tasks) {
                System.out.println(taskNumber + ". " + task);
                taskNumber++;
            }

            // Получить от пользователя номер задания для принятия
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите номер задания для принятия: ");
            int chosenTaskNumber = scanner.nextInt();

            if (chosenTaskNumber >= 1 && chosenTaskNumber <= tasks.size()) {
                // Открыть файл курьера для записи выбранной задачи
                FileWriter courierFileWriter = new FileWriter(COURIER_TASKS_FILE_PATH, true);
                BufferedWriter bufferedWriter = new BufferedWriter(courierFileWriter);

                // Записать выбранную задачу в файл курьера
                bufferedWriter.write(tasks.get(chosenTaskNumber - 1) + "\n");
                bufferedWriter.close();

                // Удалить выбранную задачу из списка
                tasks.remove(chosenTaskNumber - 1);

                // Записать обновленный список задач обратно в Task.txt
                FileWriter taskFileWriter = new FileWriter(TASK_FILE_PATH);
                BufferedWriter taskBufferedWriter = new BufferedWriter(taskFileWriter);
                for (String task : tasks) {
                    taskBufferedWriter.write(task + "\n");
                }
                taskBufferedWriter.close();

                System.out.println("Задание записано в личный файл курьера и удалено из файла задач.");
            } else {
                System.out.println("Некорректный номер задания.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Файл задач не найден.");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл курьера или удалении задания.");
        }
    }

    private static void rejectOrder() {
        try {
            File courierTasksFile = new File(COURIER_TASKS_FILE_PATH);
            Scanner fileScanner = new Scanner(courierTasksFile);

            ArrayList<String> courierTasks = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                String task = fileScanner.nextLine();
                courierTasks.add(task);
            }
            fileScanner.close();

            int taskNumber = 1;
            for (String task : courierTasks) {
                System.out.println(taskNumber + ". " + task);
                taskNumber++;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите номер заказа для отклонения: ");
            int chosenTaskNumber = scanner.nextInt();
            scanner.nextLine();

            if (chosenTaskNumber >= 1 && chosenTaskNumber <= courierTasks.size()) {
                String removedTask = courierTasks.remove(chosenTaskNumber - 1);

                FileWriter courierFileWriter = new FileWriter(COURIER_TASKS_FILE_PATH);
                BufferedWriter bufferedWriter = new BufferedWriter(courierFileWriter);

                for (String task : courierTasks) {
                    bufferedWriter.write(task + "\n");
                }
                bufferedWriter.close();

                FileWriter taskFileWriter = new FileWriter(TASK_FILE_PATH, true);
                BufferedWriter taskBufferedWriter = new BufferedWriter(taskFileWriter);
                taskBufferedWriter.write(removedTask + "\n");
                taskBufferedWriter.close();

                System.out.println("Заказ отклонен и возвращен в файл задач.");
            } else {
                System.out.println("Некорректный номер заказа.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл заказов курьера не найден.");
        } catch (IOException e) {
            System.out.println("Ошибка при отклонении заказа или возврате в файл задач.");
        }
    }




    private static void updateTaskStatus() {
        // Логика обновления статуса задачи
        // Например: запрос у пользователя нового статуса и обновление файла с заданиями
    }

    private static void viewTaskHistory() {
        try {
            File courierTasksFile = new File(COURIER_TASKS_FILE_PATH);
            if (!courierTasksFile.exists()) {
                System.out.println("История задач курьера пуста.");
                return;
            }

            Scanner fileScanner = new Scanner(courierTasksFile);

            System.out.println("История задач курьера:");
            while (fileScanner.hasNextLine()) {
                String task = fileScanner.nextLine();
                System.out.println(task);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл истории задач курьера не найден.");
        }
    }

    private static void readClientReview() {
        // Логика чтения отзыва клиента
        // Можно считать отзыв из файла или вводить пользователем
    }

    private static void incomeMonitoringPanel() {
        // Логика панели мониторинга доходов
        // Можно выводить информацию о доходах и статистику работы курьера
    }
}
