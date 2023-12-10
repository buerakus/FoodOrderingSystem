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

    public static void deliveryMenu (User currentUser) {
        if (currentUser == null || !"delivery".equals(currentUser.getRole())) {
            System.out.println("Access denied. Only delivery can access this menu.");
            return; // Exit the method if the user is not a vendor
        }

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

            int rowNum = 1; // Порядковый номер начинается с 1

            // Печать заголовка таблицы
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-6s | %-12s | %-12s | %-20s |\n", "№", "Позиция", "ID", "Дата заказа", "Цена", "Статус");
            System.out.println("----------------------------------------------------------------------------------------------------");

            // Чтение и печать данных из файла Task.txt в виде таблицы
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                // Печать данных каждой строки в таблице
                System.out.printf("| %-10s | %-20s | %-6s | %-12s | RM%-10s | %-20s |\n",
                        rowNum, columns[0], columns[1], columns[2], columns[3], columns[4]);

                rowNum++;
            }

            System.out.println("----------------------------------------------------------------------------------------------------");
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл задач не найден.");
        }
    }



    private static void  acceptOrRejectTask(User currentUser) {
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
                    acceptOrder(currentUser);
                    break;
                case 2:
                    rejectOrder(currentUser);
                    break;
                case 3:
                    System.out.println("Выход из меню выбора.");
                    return;
                default:
                    System.out.println("Выбран некорректный пункт меню.");
            }
        }
    }

    private static void acceptOrder(User currentUser) {
        String deliveryPrefix =", " + currentUser.getUsername();
        try {
            File taskFile = new File(TASK_FILE_PATH);
            Scanner fileScanner = new Scanner(taskFile);

            int rowNum = 1; // Порядковый номер начинается с 1

            // Печать заголовка таблицы
            System.out.println("----------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-6s | %-12s | %-12s | %-20s |\n", "№", "Позиция", "ID", "Дата заказа", "Цена", "Статус");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            // Чтение и печать данных из файла Task.txt в виде таблицы
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] columns = line.split(", ");

                // Печать данных каждой строки в таблице, если статус "в ожидании принятия" или "отменен"
                if (columns[4].equals("в ожидании принятия") || columns[4].equals("отклонен")) {
                    System.out.printf("| %-10s | %-20s | %-6s | %-12s | RM%-11s | %-20s |\n",
                            rowNum, columns[0], columns[1], columns[2], columns[3], columns[4]);
                }

                rowNum++;
            }

            System.out.println("----------------------------------------------------------------------------------------------------------");
            fileScanner.close();

            // Выбор курьера
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите номер заказа для принятия или 0 для выхода: ");
            int chosenTaskNumber = scanner.nextInt();

            if (chosenTaskNumber > 0 && chosenTaskNumber < rowNum) {
                // Открыть файл курьера для записи выбранной задачи
                FileWriter courierFileWriter = new FileWriter(COURIER_TASKS_FILE_PATH, true);
                BufferedWriter bufferedWriter = new BufferedWriter(courierFileWriter);

                // Повторно открыть файл с заданиями для поиска выбранной задачи по номеру
                fileScanner = new Scanner(taskFile);
                int currentTask = 1;
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] columns = line.split(", ");

                    if (currentTask == chosenTaskNumber) {
                        // Записать выбранную задачу в файл курьера со статусом "принят"
                        bufferedWriter.write(columns[0] + ", " + columns[1] + ", " + columns[2] + ", " + columns[3] + ", принят" + deliveryPrefix +"\n");
                        System.out.println("Задание записано в личный файл курьера.");
                    }

                    currentTask++;
                }

                bufferedWriter.close();
                fileScanner.close();

                // Удалить выбранную задачу из файла Task.txt и создать новую с статусом "принят"
                updateTaskFile(taskFile, chosenTaskNumber);
            } else {
                System.out.println("Выбран некорректный номер заказа.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Файл задач не найден.");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл курьера или обновлении файла задач.");
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

            // Выбор курьера
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите номер заказа для отклонения или 0 для выхода: ");
            int chosenTaskNumber = scanner.nextInt();

            if (chosenTaskNumber > 0 && chosenTaskNumber < rowNum) {
                // Удаление выбранной задачи из файлов
                updateCourierTasksFile(courierTasksFile, chosenTaskNumber);
                updateTaskFile(chosenTaskNumber);
                System.out.println("Заказ отклонен.");
            } else {
                System.out.println("Выбран некорректный номер заказа.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Файл задач курьера не найден.");
        } catch (IOException e) {
            System.out.println("Ошибка при отклонении заказа или обновлении файлов.");
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
            System.out.println("Введите номер заказа для обновления статуса или 0 для выхода: ");
            int chosenTaskNumber = scanner.nextInt();

            if (chosenTaskNumber > 0 && chosenTaskNumber < rowNum) {
                updateTaskStatusInFiles(courierTasksFile, chosenTaskNumber);
                System.out.println("Статус заказа обновлен.");
            } else {
                System.out.println("Выбран некорректный номер заказа.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Файл задач курьера не найден.");
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
            System.out.println("Файл задач курьера не найден.");
        }
    }


    private static void readClientReview(User currentUser) {
        // Логика чтения отзыва клиента
        // Можно считать отзыв из файла или вводить пользователем
    }

    private static void incomeMonitoringPanel(User currentUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Выберите действие:");
            System.out.println("1. Анализ доходов");
            System.out.println("2. Подсчет прибыли");
            System.out.println("3. Статистика выполненных заказов");
            System.out.println("0. Выход");
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
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Некорректный выбор. Попробуйте снова.");
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
            System.out.println("Файл задач курьера не найден.");
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
            System.out.println("Файл задач курьера не найден.");
        }
    }




}
