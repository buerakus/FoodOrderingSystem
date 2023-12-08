import java.io.*;
import java.io.File;
import java.util.Scanner;



public class DeliveryRunner {
    private static final String TASK_FILE_PATH = "C:\\Users\\Dimash\\Desktop\\Task.txt";
    private static final String COURIER_TASKS_FILE_PATH = "C:\\Users\\Dimash\\Desktop\\CourierTasks.txt";

    public static void main(String[] args) {
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





    private static void acceptOrRejectTask() {
        try {
            // Чтение задач из файла Task.txt
            File taskFile = new File(TASK_FILE_PATH);
            Scanner fileScanner1 = new Scanner(taskFile);

            int taskNumber = 1;

            while (fileScanner1.hasNextLine()) {
                String task = fileScanner1.nextLine();
                System.out.println(taskNumber + ". " + task);
                taskNumber++;
            }

            // Выбор задачи курьером
            System.out.println("Выберите номер задания для принятия: ");
            Scanner scanner = new Scanner(System.in);
            int selectedTaskNumber = scanner.nextInt();

            // Чтение выбранной задачи
            fileScanner1 = new Scanner(taskFile);
            for (int i = 1; i <= selectedTaskNumber; i++) {
                if (fileScanner1.hasNextLine()) {
                    String task = fileScanner1.nextLine();
                    if (i == selectedTaskNumber) {
                        // Запись выбранной задачи в файл CourierTasks.txt
                        FileWriter courierFileWriter = new FileWriter(COURIER_TASKS_FILE_PATH, true);
                        BufferedWriter bufferedWriter = new BufferedWriter(courierFileWriter);
                        bufferedWriter.write(task + "\n");
                        bufferedWriter.close();
                        System.out.println("Задание записано в личный файл курьера.");
                        break;
                    }
                }
            }

            fileScanner1.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл задач не найден.");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл курьера.");
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
