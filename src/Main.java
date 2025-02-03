import db.DBManager;
import models.*;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static DBManager dbManager;

    public static void main(String[] args) {
        try {
            dbManager = new DBManager("jdbc:postgresql://localhost:5432/geometry", "postgres", "root");
            Scanner scanner = new Scanner(System.in);

            System.out.println("Добро пожаловать в калькулятор геометрических фигур!\n");

            User user = null;

            while (user == null) {
                System.out.println("Выберите действие:\n1 - Регистрация, 2 - Логин, 0 - Выход");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Введите имя пользователя: ");
                        String username = scanner.next();
                        System.out.print("Введите пароль: ");
                        String password = scanner.next();
                        dbManager.registerUser(username, password);
                        System.out.println("Регистрация успешна!");
                        break;

                    case 2:
                        System.out.print("Введите имя пользователя: ");
                        username = scanner.next();
                        System.out.print("Введите пароль: ");
                        password = scanner.next();
                        user = dbManager.loginUser(username, password);
                        if (user != null) {
                            System.out.println("Добро пожаловать, " + user.getUsername() + "!");
                        } else {
                            System.out.println("Неверные данные. Попробуйте снова.");
                        }
                        break;

                    case 0:
                        System.out.println("До свидания!");
                        return;

                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }

            // Вечное меню выбора фигур
            boolean isRunning = true;
            while (isRunning) {
                System.out.println("\nВыберите фигуру для расчета:");
                System.out.println("1 - Сфера");
                System.out.println("2 - Квадрат");
                System.out.println("3 - Прямоугольник");
                System.out.println("4 - Треугольник");
                System.out.println("5 - Параллелепипед");
                System.out.println("6 - Конус");
                System.out.println("7 - Пирамида");
                System.out.println("8 - Показать историю расчетов");
                System.out.println("0 - Выход");

                int figureChoice = scanner.nextInt();

                switch (figureChoice) {
                    case 1:
                        System.out.print("Введите радиус: ");
                        double radius = scanner.nextDouble();
                        handleFigure(new Sphere(radius), user);
                        break;

                    case 2:
                        System.out.print("Введите сторону: ");
                        double side = scanner.nextDouble();
                        handleFigure(new Square(side), user);
                        break;

                    case 3:
                        System.out.print("Введите длину: ");
                        double length = scanner.nextDouble();
                        System.out.print("Введите ширину: ");
                        double width = scanner.nextDouble();
                        handleFigure(new Rectangle(length, width), user);
                        break;

                    case 4:
                        System.out.print("Введите стороны A, B и C: ");
                        double a = scanner.nextDouble();
                        double b = scanner.nextDouble();
                        double c = scanner.nextDouble();
                        handleFigure(new Triangle(a, b, c), user);
                        break;

                    case 5:
                        System.out.print("Введите длину: ");
                        length = scanner.nextDouble();
                        System.out.print("Введите ширину: ");
                        width = scanner.nextDouble();
                        System.out.print("Введите высоту: ");
                        double height = scanner.nextDouble();
                        handleFigure(new Parallelepiped(length, width, height), user);
                        break;

                    case 6:
                        System.out.print("Введите радиус основания: ");
                        radius = scanner.nextDouble();
                        System.out.print("Введите высоту: ");
                        height = scanner.nextDouble();
                        handleFigure(new Cone(radius, height), user);
                        break;

                    case 7:
                        System.out.print("Введите длину основания: ");
                        length = scanner.nextDouble();
                        System.out.print("Введите ширину основания: ");
                        width = scanner.nextDouble();
                        System.out.print("Введите высоту: ");
                        height = scanner.nextDouble();
                        handleFigure(new Pyramid(length, width, height), user);
                        break;

                    case 8:
                        System.out.println("История расчетов:");
                        dbManager.showHistory(user.getId());
                        break;

                    case 0:
                        System.out.println("Выход из программы. До свидания!");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleFigure(Figure figure, User user) throws SQLException {
        double area = figure.calculateArea();
        double volume = figure.calculateVolume();
        double perimeter = figure.calculatePerimeter();

        System.out.println("\nРезультаты расчетов:");
        System.out.println("Площадь: " + area);
        System.out.println("Объем: " + volume);
        System.out.println("Периметр: " + perimeter);

        dbManager.saveCalculation(user.getId(), figure.getName(), area, volume, perimeter);
        System.out.println("Результаты сохранены в базе данных.");
    }
}
