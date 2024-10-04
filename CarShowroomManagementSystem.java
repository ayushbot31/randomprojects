import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
    private String carName;
    private String carModel;
    private int carYear;
    private String carColor;
    private double carPrice;
    private boolean isBooked;

    public Car(String carName, String carModel, int carYear, String carColor, double carPrice) {
        this.carName = carName;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carColor = carColor;
        this.carPrice = carPrice;
        this.isBooked = false;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getCarYear() {
        return carYear;
    }

    public String getCarColor() {
        return carColor;
    }

    public double getCarPrice() {
        return carPrice;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}

public class CarShowroomManagementSystem {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public CarShowroomManagementSystem(String adminUsername, String adminPassword) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/car_showroom", "root","PLQRPPYJ2");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCar(String carName, String carModel, int carYear, String carColor, double carPrice) {
        String query = "INSERT INTO cars (car_name, car_model, car_year, car_color, car_price, is_booked) VALUES (?,?,?,?,?,?)";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, carName);
            preparedStatement.setString(2, carModel);
            preparedStatement.setInt(3, carYear);
            preparedStatement.setString(4, carColor);
            preparedStatement.setDouble(5, carPrice);
            preparedStatement.setBoolean(6, false);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCar(String carName) {
        String query = "DELETE FROM cars WHERE car_name = ?";
        
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, carName);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Car removed successfully.");
            } else {
                System.out.println("No car found with the given name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayBookedCars() {
        List<Car> bookedCars = new ArrayList<>();
        String query = "SELECT * FROM cars WHERE is_booked = true";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String carName = resultSet.getString("car_name");
                String carModel = resultSet.getString("car_model");
                int carYear = resultSet.getInt("car_year");
                String carColor = resultSet.getString("car_color");
                double carPrice = resultSet.getDouble("car_price");
                boolean isBooked = resultSet.getBoolean("is_booked");

                Car car = new Car(carName, carModel, carYear, carColor, carPrice);
                car.setBooked(isBooked);
                bookedCars.add(car);
            }

            if (bookedCars.isEmpty()) {
                System.out.println("No cars are currently booked.");
            } else {
                System.out.println("Booked Cars:");
                for (Car car : bookedCars) {
                    System.out.println(car.getCarName() + " " + car.getCarModel() + " " + car.getCarYear() + " " + car.getCarColor() + " " + car.getCarPrice() + " " + car.isBooked());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Car> getCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String carName = resultSet.getString("car_name");
                String carModel = resultSet.getString("car_model");
                int carYear = resultSet.getInt("car_year");
                String carColor = resultSet.getString("car_color");
                double carPrice = resultSet.getDouble("car_price");
                boolean isBooked = resultSet.getBoolean("is_booked");

                Car car = new Car(carName, carModel, carYear, carColor, carPrice);
                car.setBooked(isBooked);
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public void bookCar(String carName) {
        String query = "UPDATE cars SET is_booked =? WHERE car_name =?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setString(2, carName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unbookCar(String carName) {
        String query = "UPDATE cars SET is_booked =? WHERE car_name =?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setString(2, carName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayCars() {
        List<Car> cars = getCars();

        for (Car car : cars) {
            System.out.println(car.getCarName() + " " + car.getCarModel() + " " + car.getCarYear() + " " + car.getCarColor() + " " + car.getCarPrice() + " " + car.isBooked());
        }
    }

    public boolean isAdmin(String username, String password) {
        String query = "SELECT * FROM admins WHERE username =? AND password =?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        CarShowroomManagementSystem system = new CarShowroomManagementSystem("admin", "admin123");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to the Car Showroom Management System!");
            System.out.println("1. Add car");
            System.out.println("2. Display cars");
            System.out.println("3. Book car");
            System.out.println("4. Unbook car");
            System.out.println("5. Remove car"); // Adding option for removing car
            System.out.println("6. Display booked cars"); // Adding option for displaying booked cars
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
        
            if (choice == 1) {
                System.out.print("Enter car name: ");
                scanner.nextLine();
                String carName = scanner.nextLine();
                System.out.print("Enter car model: ");
                String carModel = scanner.nextLine();
                System.out.print("Enter car year: ");
                int carYear = scanner.nextInt();
                System.out.print("Enter car color: ");
                scanner.nextLine();
                String carColor = scanner.nextLine();
                System.out.print("Enter car price: ");
                double carPrice = scanner.nextDouble();

                system.addCar(carName, carModel, carYear, carColor, carPrice);
            } else if (choice == 2) {
                system.displayCars();
            } else if (choice == 3) {
                System.out.print("Enter car name to book: ");
                scanner.nextLine();
                String carName = scanner.nextLine();
                system.bookCar(carName);
            } else if (choice == 4) {
                System.out.print("Enter car name to unbook: ");
                scanner.nextLine();
                String carName = scanner.nextLine();
                system.unbookCar(carName);
            } else if (choice == 5) {
                System.out.print("Enter car name to remove: ");
                scanner.nextLine();
                String carName = scanner.nextLine();
                system.removeCar(carName);
            } else if (choice == 6) {
                system.displayBookedCars();
            } else if (choice == 7) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }        

        scanner.close();
    }
}