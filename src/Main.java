import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;
    private List<Rental> rentalHistory;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;

        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
        this.rentalHistory = new ArrayList<>();
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable(LocalDate startDate, LocalDate endDate) {
        // Check rental history for overlapping dates
        for (Rental rental : rentalHistory) {
            if (rental.isOverlapping(startDate, endDate)) {
                return false;
            }
        }
        return true;
    }

    public void rent(LocalDate startDate, LocalDate endDate, Customer customer) {
        isAvailable = false;
        rentalHistory.add(new Rental(this, customer, startDate, endDate));
    }

    public void returnCar() {
        isAvailable = true;
    }

    public List<Rental> getRentalHistory() {
        return rentalHistory;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private LocalDate startDate;
    private LocalDate endDate;

    public Rental(Car car, Customer customer, LocalDate startDate, LocalDate endDate) {
        this.car = car;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isOverlapping(LocalDate start, LocalDate end) {
        return !(end.isBefore(startDate) || start.isAfter(endDate));
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, LocalDate startDate, LocalDate endDate) {
        if (car.isAvailable(startDate, endDate)) {
            car.rent(startDate, endDate, customer);
            rentals.add(new Rental(car, customer, startDate, endDate));
            System.out.println("Car rented successfully.");
        } else {
            System.out.println("Car is not available for the selected dates.");
            LocalDate nextAvailableDate = findNextAvailableDate(car, startDate, endDate);
            if (nextAvailableDate != null) {
                System.out.println("Next available date for " + car.getBrand() + " " + car.getModel() + " is: "
                        + nextAvailableDate);
            }
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not rented.");
        }
    }

    public LocalDate findNextAvailableDate(Car car, LocalDate startDate, LocalDate endDate) {
        // Loop to find next available date
        LocalDate nextAvailableDate = endDate.plusDays(1);
        while (!car.isAvailable(nextAvailableDate,
                nextAvailableDate.plusDays((int) (endDate.toEpochDay() - startDate.toEpochDay())))) {
            nextAvailableDate = nextAvailableDate.plusDays(1);
        }
        return nextAvailableDate;
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();

                System.out.println("\nAvailable Cars:");
                for (Car car : cars) {
                    if (car.isAvailable(LocalDate.now(), LocalDate.now())) {
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }

                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = scanner.nextLine();

                System.out.print("Enter the start date (yyyy-mm-dd): ");
                String startDateStr = scanner.nextLine();
                LocalDate startDate = LocalDate.parse(startDateStr, formatter);

                System.out.print("Enter the end date (yyyy-mm-dd): ");
                String endDateStr = scanner.nextLine();
                LocalDate endDate = LocalDate.parse(endDateStr, formatter);

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId)) {
                        selectedCar = car;
                        break;
                    }
                }

                if (selectedCar != null) {
                    if (selectedCar.isAvailable(startDate, endDate)) {
                        double totalPrice = selectedCar
                                .calculatePrice((int) (endDate.toEpochDay() - startDate.toEpochDay()));
                        System.out.println("\n== Rental Information ==\n");
                        System.out.println("Customer ID: " + newCustomer.getCustomerId());
                        System.out.println("Customer Name: " + newCustomer.getName());
                        System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                        System.out.println("Rental Days: " + (endDate.toEpochDay() - startDate.toEpochDay()));
                        System.out.printf("Total Price: $%.2f%n", totalPrice);

                        System.out.print("\nConfirm rental (Y/N): ");
                        String confirm = scanner.nextLine();

                        if (confirm.equalsIgnoreCase("Y")) {
                            rentCar(selectedCar, newCustomer, startDate, endDate);
                        } else {

                        }
                    } else {
                        // If the car is not available, find and display the next available date
                        System.out.println("Car is not available for the selected dates.");
                        LocalDate nextAvailableDate = findNextAvailableDate(selectedCar, startDate, endDate);
                        if (nextAvailableDate != null) {
                            System.out.println("Next available date for " + selectedCar.getBrand() + " "
                                    + selectedCar.getModel() + " is: " + nextAvailableDate);
                        }
                    }
                } else {
                    System.out.println("\nInvalid car selection.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine();

                Car carToReturn = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && !car.isAvailable(LocalDate.now(), LocalDate.now())) {
                        carToReturn = car;
                        break;
                    }
                }

                if (carToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getCar() == carToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        // Add some cars to the system
        Car car1 = new Car("C001", "Toyota", "Camry", 60.0);
        Car car2 = new Car("C002", "Honda", "Civic", 55.0);
        Car car3 = new Car("C003", "Ford", "Mustang", 100.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        // Start the menu
        rentalSystem.menu();
    }
}