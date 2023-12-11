import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DeliveryRunner {
    private static final String TASK_FILE_PATH = "C:\\Users\\Dimash\\Desktop\\Task.txt";
    private static final String COURIER_TASKS_FILE_PATH = "C:\\Users\\Dimash\\Desktop\\CourierTasks.txt";
    private static final String CUSTOMER_FEEDBACK_PATH = "C:\\Users\\Dimash\\Desktop\\CustomerFeedback.txt";

    public static void deliveryMenu (User currentUser) {
        if (currentUser == null || !"delivery".equals(currentUser.getRole())) {
            System.out.println("Access denied. Only delivery runner can access this menu.");
            return; // Exit the method if the user is not a delivery runner
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<Delivery Runner Menu>>>>>>>>>>>>>>>>>>>");
            System.out.println("1. View Task List");
            System.out.println("2. Accept/Decline task");
            System.out.println("3. Update task status");
            System.out.println("4. Check task history");
            System.out.println("5. Read customer review");
            System.out.println("6. Revenue Dashboard");
            System.out.println("0. Exit Delivery Menu");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewTask();
                    break;
                case 2:
                    acceptOrRejectTask(currentUser);
                    break;
                case 3:
                    updateTaskStatus(currentUser);
                    break;
                case 4:
                    viewTaskHistory(currentUser);
                    break;
                case 5:
                    readClientReview(currentUser);
                    break;
                case 6:
                    incomeMonitoringPanel(currentUser);
                    break;
                case 0:
                    System.out.println("returning to the registration menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    
    
    
//////////////////////////////////////////////////VIEW TASK/////////////////////////////////////////////////////////////    
    private static void viewTask() {
        try {
            File taskFile = new File(TASK_FILE_PATH);
            Scanner fileScanner = new Scanner(taskFile);

            int rowNum = 1;

            // Печать заголовка таблицы
            // Printing the table header
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-6s | %-12s | %-12s | %-20s |\n", "№", "Позиция", "ID", "Дата заказа", "Цена", "Статус");
            System.out.println("----------------------------------------------------------------------------------------------------");

            // Чтение и печать данных из файла Task.txt в виде таблицы
            // Reading and printing data from the Task.txt file in the form of a table
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                System.out.printf("| %-10s | %-20s | %-6s | %-12s | RM%-10s | %-20s |\n",
                        rowNum, columns[0], columns[1], columns[2], columns[3], columns[4]);

                rowNum++;
            }

            System.out.println("----------------------------------------------------------------------------------------------------");
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Task file not found.");
        }
    }



    
    
    
////////////////////////////////////////////////ACCEPT or DECLINE TASK////////////////////////////////////////////////// 
    
    private static void  acceptOrRejectTask(User currentUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<Accept/Decline task Menu>>>>>>>>>>>>>>>>>>");
            System.out.println("1. Accept Order");
            System.out.println("2. Decline Order");
            System.out.println("3. Exit");
            System.out.println("-----------------------------------------------------------");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    acceptOrder(currentUser);
                    break;
                case 2:
                    rejectOrder(currentUser);
                    break;
                case 3:
                    System.out.println("returning to the Delivery Runner Menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void acceptOrder(User currentUser) {
        String deliveryPrefix =", " + currentUser.getUsername();
        try {
            File taskFile = new File(TASK_FILE_PATH);
            Scanner fileScanner = new Scanner(taskFile);

            int rowNum = 1;
            System.out.println("----------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-6s | %-12s | %-12s | %-20s |\n", "№", "Позиция", "ID", "Дата заказа", "Цена", "Статус");
            System.out.println("----------------------------------------------------------------------------------------------------------");
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                // Печать данных каждой строки в таблице, если статус "в ожидании принятия" или "отменен"
                // Printing the data of each row in the table if the status is "pending acceptance" or "canceled"
                if (columns[4].equals("в ожидании принятия") || columns[4].equals("отклонен")) {
                    System.out.printf("| %-10s | %-20s | %-6s | %-12s | RM%-11s | %-20s |\n",
                            rowNum, columns[0], columns[1], columns[2], columns[3], columns[4]);
                }

                rowNum++;
            }

            System.out.println("----------------------------------------------------------------------------------------------------------");
            fileScanner.close();

            // Couriers Choice
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the order number to accept or 0 to Exit: ");
            int chosenTaskNumber = scanner.nextInt();

            if (chosenTaskNumber > 0 && chosenTaskNumber < rowNum) {
                // Open the courier file to record the selected task
                FileWriter courierFileWriter = new FileWriter(COURIER_TASKS_FILE_PATH, true);
                BufferedWriter bufferedWriter = new BufferedWriter(courierFileWriter);

                // search for the selected task by number
                fileScanner = new Scanner(taskFile);
                int currentTask = 1;
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] columns = line.split(", ");

                    if (currentTask == chosenTaskNumber) {
                        // Записать выбранную задачу в файл курьера со статусом "принят"
                        bufferedWriter.write(columns[0] + ", " + columns[1] + ", " + columns[2] + ", " + columns[3] + ", принят" + deliveryPrefix +"\n");
                        System.out.println("The task is recorded in the courier's personal file.");
                    }

                    currentTask++;
                }

                bufferedWriter.close();
                fileScanner.close();

                // Удалить выбранную задачу из файла Task.txt и создать новую с статусом "принят"
                updateTaskFile(taskFile, chosenTaskNumber);
            } else {
                System.out.println("An incorrect order number has been selected.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Task file not found. ");
        } catch (IOException e) {
            System.out.println("An error occurred when writing to the courier file or updating the task file.");
        }
    }

    private static void updateTaskFile(File taskFile, int chosenTaskNumber) throws IOException {
        Scanner fileScanner = new Scanner(taskFile);
        StringBuilder newTaskData = new StringBuilder();

        int currentTask = 1;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (currentTask != chosenTaskNumber) {
                newTaskData.append(line).append("\n");
            } else {
                // Создать новую строку с статусом "принят"
                String[] columns = line.split(", ");
                newTaskData.append(columns[0]).append(", ").append(columns[1]).append(", ").append(columns[2])
                        .append(", ").append(columns[3]).append(", принят\n");
            }

            currentTask++;
        }

        fileScanner.close();

        // Записать обновленные данные в файл Task.txt
        FileWriter taskFileWriter = new FileWriter(TASK_FILE_PATH);
        BufferedWriter taskBufferedWriter = new BufferedWriter(taskFileWriter);
        taskBufferedWriter.write(newTaskData.toString());
        taskBufferedWriter.close();
    }



    private static void rejectOrder(User currentUser) {
        String deliveryPrefix =currentUser.getUsername();
        try {
            File courierTasksFile = new File(COURIER_TASKS_FILE_PATH);
            Scanner fileScanner = new Scanner(courierTasksFile);

            int rowNum = 1; // Порядковый номер начинается с 1

            // Печать заголовка таблицы
            System.out.println("----------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-6s | %-12s | %-12s | %-20s |\n", "№", "Позиция", "ID", "Дата заказа", "Цена", "Статус");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            // Чтение и печать данных из файла CourierTasks.txt в виде таблицы
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                // Печать данных каждой строки в таблице, если статус "принят"
                if (columns[4].trim().equals("принят") && columns[5].trim().equals(deliveryPrefix)) {
                    System.out.printf("| %-10s | %-15s | %-6s | %-12s | RM%-11s | %-20s |\n",
                            rowNum, columns[0], columns[1], columns[2], columns[3], columns[4]);
                }

                rowNum++;
            }

            System.out.println("----------------------------------------------------------------------------------------------------------");
            fileScanner.close();


            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the order number to reject or 0 to Exit: ");
            int chosenTaskNumber = scanner.nextInt();

            if (chosenTaskNumber > 0 && chosenTaskNumber < rowNum) {
                // Удаление выбранной задачи из файлов
                updateCourierTasksFile(courierTasksFile, chosenTaskNumber);
                updateTaskFile(chosenTaskNumber);
                System.out.println("The order has been rejected.");
            } else {
                System.out.println("Incorrect order number has been selected.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Courier's Task file was not found.");
        } catch (IOException e) {
            System.out.println("An error occurred when rejecting an order or updating files.");
        }
    }

    private static void updateCourierTasksFile(File courierTasksFile, int chosenTaskNumber) throws IOException {
        Scanner fileScanner = new Scanner(courierTasksFile);
        StringBuilder newCourierTasksData = new StringBuilder();

        int currentTask = 1;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (currentTask != chosenTaskNumber) {
                newCourierTasksData.append(line).append("\n");
            }

            currentTask++;
        }

        fileScanner.close();

        // Запись обновленных данных в файл CourierTasks.txt
        FileWriter courierFileWriter = new FileWriter(COURIER_TASKS_FILE_PATH);
        BufferedWriter bufferedWriter = new BufferedWriter(courierFileWriter);
        bufferedWriter.write(newCourierTasksData.toString());
        bufferedWriter.close();
    }

    private static void updateTaskFile(int chosenTaskNumber) throws IOException {
        File taskFile = new File(TASK_FILE_PATH);
        Scanner fileScanner = new Scanner(taskFile);
        StringBuilder newTaskData = new StringBuilder();

        int currentTask = 1;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (currentTask != chosenTaskNumber) {
                newTaskData.append(line).append("\n");
            } else {
                // Создание новой строки с статусом "отклонен"
                String[] columns = line.split(", ");
                newTaskData.append(columns[0]).append(", ").append(columns[1]).append(", ").append(columns[2])
                        .append(", ").append(columns[3]).append(", отклонен").append(", ").append(columns[4]).append("\n");
            }

            currentTask++;
        }

        fileScanner.close();

        // Запись обновленных данных в файл Task.txt
        FileWriter taskFileWriter = new FileWriter(TASK_FILE_PATH);
        BufferedWriter taskBufferedWriter = new BufferedWriter(taskFileWriter);
        taskBufferedWriter.write(newTaskData.toString());
        taskBufferedWriter.close();
    }



    private static void updateTaskStatus(User currentUser) {
        String deliveryPrefix =currentUser.getUsername();
        try {
            File courierTasksFile = new File(COURIER_TASKS_FILE_PATH);
            Scanner fileScanner = new Scanner(courierTasksFile);

            int rowNum = 1; // Порядковый номер начинается с 1

            // Печать заголовка таблицы
            System.out.println("----------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-6s | %-12s | %-12s | %-20s |\n", "№", "Позиция", "ID", "Дата заказа", "Цена", "Статус");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            // Чтение и печать данных из файла CourierTasks.txt в виде таблицы
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                // Печать данных каждой строки в таблице, если статус "принят" или "в процессе доставки"
                if ((columns[4].trim().equals("принят") || columns[4].trim().equals("в процессе доставки")) && columns[5].trim().equals(deliveryPrefix)) {
                    System.out.printf("| %-10s | %-15s | %-6s | %-12s | RM%-11s | %-20s |\n",
                            rowNum, columns[0], columns[1], columns[2], columns[3], columns[4]);
                }

                rowNum++;
            }

            System.out.println("----------------------------------------------------------------------------------------------------------");
            fileScanner.close();

            // Выбор курьера
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the order number to update the status or 0 to exit: ");
            int chosenTaskNumber = scanner.nextInt();

            if (chosenTaskNumber > 0 && chosenTaskNumber < rowNum) {
                updateTaskStatusInFiles(courierTasksFile, chosenTaskNumber);
                System.out.println("The order status has been updated. ");
            } else {
                System.out.println("An incorrect order number has been selected.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Courier's Task file was not found.");
        } catch (IOException e) {
            System.out.println("Ошибка при обновлении статуса заказа.");
        }
    }

    private static void updateTaskStatusInFiles(File courierTasksFile, int chosenTaskNumber) throws IOException {
        Scanner fileScanner = new Scanner(courierTasksFile);
        StringBuilder newCourierTasksData = new StringBuilder();

        int currentTask = 1;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (currentTask != chosenTaskNumber) {
                newCourierTasksData.append(line).append("\n");
            } else {
                String[] columns = line.split(", ");
                if (columns[4].trim().equals("принят")) {
                    // Обновление статуса на "в процессе доставки"
                    newCourierTasksData.append(columns[0]).append(", ").append(columns[1]).append(", ")
                            .append(columns[2]).append(", ").append(columns[3]).append(", в процессе доставки")
                            .append(", ").append(columns[5]).append("\n");
                    updateStatusInTaskFile(columns[1], "в процессе доставки");
                } else if (columns[4].trim().equals("в процессе доставки")) {
                    // Обновление статуса на "доставлен"
                    newCourierTasksData.append(columns[0]).append(", ").append(columns[1]).append(", ")
                            .append(columns[2]).append(", ").append(columns[3]).append(", доставлен")
                            .append(", ").append(columns[5]).append("\n");
                    updateStatusInTaskFile(columns[1], "доставлен");
                }
            }

            currentTask++;
        }

        fileScanner.close();

        // Запись обновленных данных в файл CourierTasks.txt
        FileWriter courierFileWriter = new FileWriter(COURIER_TASKS_FILE_PATH);
        BufferedWriter bufferedWriter = new BufferedWriter(courierFileWriter);
        bufferedWriter.write(newCourierTasksData.toString());
        bufferedWriter.close();
    }

    private static void updateStatusInTaskFile(String taskId, String newStatus) throws IOException {
        File taskFile = new File(TASK_FILE_PATH);
        Scanner fileScanner = new Scanner(taskFile);
        StringBuilder newTaskData = new StringBuilder();

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split(", ");
            if (columns[1].trim().equals(taskId)) {
                // Обновление статуса в файле Task.txt
                newTaskData.append(columns[0]).append(", ").append(columns[1]).append(", ")
                        .append(columns[2]).append(", ").append(columns[3]).append(", ").append(newStatus).append("\n");
            } else {
                newTaskData.append(line).append("\n");
            }
        }

        fileScanner.close();

        // Запись обновленных данных в файл Task.txt
        FileWriter taskFileWriter = new FileWriter(TASK_FILE_PATH);
        BufferedWriter taskBufferedWriter = new BufferedWriter(taskFileWriter);
        taskBufferedWriter.write(newTaskData.toString());
        taskBufferedWriter.close();
    }





    private static void viewTaskHistory(User currentUser) {
        String deliveryPrefix =currentUser.getUsername();
        try {
            File courierTasksFile = new File(COURIER_TASKS_FILE_PATH);
            Scanner fileScanner = new Scanner(courierTasksFile);

            int rowNum = 1; // Порядковый номер начинается с 1

            // Печать заголовка таблицы
            System.out.println("----------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-15s | %-6s | %-12s | %-12s | %-20s |\n", "№", "Позиция", "ID", "Дата заказа", "Цена", "Статус");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            // Чтение и печать данных из файла CourierTasks.txt в виде таблицы
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                // Печать данных каждой строки в таблице, если статус "доставлен"
                if (columns[4].trim().equals("доставлен") && columns[5].trim().equals(deliveryPrefix)) {
                    System.out.printf("| %-10s | %-20s | %-6s | %-12s | RM%-11s | %-20s |\n",
                            rowNum, columns[0], columns[1], columns[2], columns[3], columns[4]);
                }

                rowNum++;
            }

            System.out.println("----------------------------------------------------------------------------------------------------------");
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Courier's Task file was not found.");
        }
    }

    
    
    
//////////////////////////////////////////////CUSTOMER REVIEW///////////////////////////////////////////////////////////
    
    private static void readClientReview(User currentUser) {
        String deliveryPrefix = currentUser.getUsername();
        try {


            File feedbackFile = new File(CUSTOMER_FEEDBACK_PATH);
            Scanner fileScanner = new Scanner(feedbackFile);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split("/ ");

                if (columns.length >= 5 && columns[4].trim().equals(deliveryPrefix)) {
                    // Печать разделителя
                    System.out.println("-----------------------------------------------------------");
                    // Печать имени клиента
                    System.out.println("Имя клиента: " + columns[2]);
                    // Печать текста отзыва
                    System.out.println("Отзыв:");
                    String feedbackText = columns[3];
                    printAlignedFeedback(feedbackText);
                    System.out.println("-----------------------------------------------------------");
                }
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Файл отзывов клиентов не найден.");
        }
    }

    private static void printAlignedFeedback(String feedbackText) {
        final int MAX_LINE_LENGTH = 58; // Максимальная длина строки
        int charsPrinted = 0;

        for (char c : feedbackText.toCharArray()) {
            System.out.print(c);
            charsPrinted++;

            if (charsPrinted == MAX_LINE_LENGTH) {
                System.out.println();
                charsPrinted = 0;
            }
        }
        if (charsPrinted > 0) {
            System.out.println();
        }
    }

    
    
    
    
///////////////////////////////////////////////////REVENUE DASHBOARD///////////////////////////////////////////////

    private static void incomeMonitoringPanel(User currentUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<Revenue Dashboard>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("1. Income Analysis over a period of time");
            System.out.println("2. Calculating profits for all time");
            System.out.println("3. Completed orders statistics");
            System.out.println("0. Exit");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    incomeAnalysis(currentUser);
                    break;
                case 2:
                    profitCalculations(currentUser);
                    break;
                case 3:
                    completedOrdersStat(currentUser);
                    break;
                case 0:
                    System.out.println("returning to the Delivery Runner Menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void  incomeAnalysis(User currentUser) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите дату начала периода (MM/dd/yyyy):");
        String startDateString = scanner.nextLine();
        Date startDate = parseDate(startDateString);

        System.out.println("Введите дату конца периода (MM/dd/yyyy):");
        String endDateString = scanner.nextLine();
        Date endDate = parseDate(endDateString);

        if (startDate != null && endDate != null) {
            analyzeIncomeForPeriod(startDate, endDate);
        } else {
            System.out.println("Некорректный формат даты. Повторите ввод.");
        }
    }
    private static void analyzeIncomeForPeriod(Date startDate, Date endDate) {
        try {
            File courierTasksFile = new File(COURIER_TASKS_FILE_PATH);
            Scanner fileScanner = new Scanner(courierTasksFile);

            double totalIncome = 0;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                String dateString = columns[2]; // дата заказа - третий столбец
                Date orderDate = parseDate(dateString);

                if (orderDate != null && isDateWithinRange(orderDate, startDate, endDate)) {
                    double price = Double.parseDouble(columns[3]); //цена - четвертый столбец
                    totalIncome += price;
                }
            }

            fileScanner.close();
            System.out.println("----------------------------------------------------------------------------------------------------------");
            System.out.println("Общая прибыль за выбранный период времени: RM" + totalIncome);
            System.out.println("----------------------------------------------------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            System.out.println("Файл задач не найден.");
        }
    }

    private static Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Ошибка парсинга даты: " + e.getMessage());
        }
        return null;
    }
    private static boolean isDateWithinRange(Date dateToCheck, Date startDate, Date endDate) {
        return dateToCheck.compareTo(startDate) >= 0 && dateToCheck.compareTo(endDate) <= 0;
    }




    private static void profitCalculations(User currentUser) {
        String deliveryPrefix =currentUser.getUsername();
        try {
            File courierTasksFile = new File(COURIER_TASKS_FILE_PATH);
            Scanner fileScanner = new Scanner(courierTasksFile);

            double totalProfit = 0;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                if (columns.length >= 6 && columns[4].trim().equals("доставлен") && columns[5].trim().equals(deliveryPrefix)) {
                    double deliveryPrice = Double.parseDouble(columns[3]); // Предполагаем, что цена - четвертый столбец
                    totalProfit += deliveryPrice;
                }
            }

            fileScanner.close();
            System.out.println("----------------------------------------------------------------------------------------------------------");
            System.out.println("Общая прибыль от доставок: RM" + totalProfit);
            System.out.println("----------------------------------------------------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            System.out.println("Courier's Task file was not found.");
        }
    }



    private static void completedOrdersStat(User currentUser) {
        String deliveryPrefix =currentUser.getUsername();
        try {
            File courierTasksFile = new File(COURIER_TASKS_FILE_PATH);
            Scanner fileScanner = new Scanner(courierTasksFile);

            int completedOrdersCount = 0;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                if (columns.length >= 5 && columns[4].trim().equals("доставлен") && columns[5].trim().equals(deliveryPrefix)) {
                    completedOrdersCount++;
                }
            }

            fileScanner.close();

            System.out.println("Количество выполненных заказов: " + completedOrdersCount);


        } catch (FileNotFoundException e) {
            System.out.println("Courier's Task file was not found.");
        }
    }




}
