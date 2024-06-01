package com.projekakhir;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Login login = new Login();

            int choice;

            do {
                choice = displayMainMenu(scanner);

                switch (choice) {
                    case 1:
                        login.adminLogin();
                        break;
                    case 2:
                        login.userLogin();
                        break;
                    case 3:
                        login.registrasi();
                        break;
                    case 4:
                        System.out.println("Terima kasih telah menggunakan program ini.");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                        break;
                }

            } while (choice != 4);
        }
    }

    public static int displayMainMenu(Scanner scanner) {
        int choice = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Menu Utama:");
                System.out.println("1. Login Admin");
                System.out.println("2. Login User");
                System.out.println("3. Registrasi User");
                System.out.println("4. Keluar");
                System.out.print("Pilihan Anda: ");
                choice = scanner.nextInt();

                if (choice > 0) {
                    validInput = true;
                } else {
                    System.out.println("Pilihan harus lebih besar dari 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Harap masukkan angka.");
                scanner.next(); // clear the invalid input
            }
        }
        return choice;
    }

    public static int getInputNumber(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Harap masukkan angka yang valid.");
            scanner.next(); // Membersihkan input yang salah
            return -1; // Mengembalikan nilai default atau bisa juga lempar eksepsi
        }
        int number = scanner.nextInt();
        if (number <= 0) {
            System.out.println("Angka harus lebih besar dari 0.");
            return -1; // Mengembalikan nilai default atau bisa juga lempar eksepsi
        }
        return number;
    }
}



