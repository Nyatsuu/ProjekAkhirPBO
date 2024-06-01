package com.projekakhir;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Login {

    private Map<String, String> adminAccounts; // Simpan akun admin
    private Map<String, String> userAccounts; // Simpan akun user
    private Scanner scanner;

    public Login() {
        adminAccounts = new HashMap<>();
        userAccounts = new HashMap<>();
        scanner = new Scanner(System.in);

        // Admin akun default
        adminAccounts.put("admin", "admin123");

        // User akun default (dapat diperluas dengan registrasi)
        userAccounts.put("user", "user123");
    }

    // Metode untuk login admin
    public boolean adminLogin() {
        System.out.print("Masukkan username admin: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan password admin: ");
        String password = scanner.nextLine();

        // Periksa kecocokan dengan akun admin yang tersimpan
        if (adminAccounts.containsKey(username) && adminAccounts.get(username).equals(password)) {
            System.out.println("Login admin berhasil.");
            Admin admin = new Admin();
            admin.adminMenu(); // Panggil adminMenu() dari objek Admin
            return true;
        } else {
            System.out.println("Login admin gagal.");
            return false;
        }
    }

    // Metode untuk login user
    public boolean userLogin() {
        int attemptCount = 0; // Menghitung jumlah percobaan login
        final int maxAttempts = 3; // Batas jumlah percobaan login

        while (attemptCount < maxAttempts) {
            System.out.print("Masukkan username user: ");
            String username = scanner.nextLine();
            System.out.print("Masukkan password user: ");
            String password = scanner.nextLine();

            // Periksa kecocokan dengan akun user yang tersimpan di database
            if (UsersController.validateUser(username, password)) {
                System.out.println("Login user berhasil.");
                int userId = UsersController.getUserId(username); // Misalnya menggunakan UsersController
                User user = new User(userId); // Berikan userId ke konstruktor User
                user.userMenu(); // Panggil userMenu() dari objek User
                return true;
            } else {
                attemptCount++;
                System.out.println("Login user gagal. Percobaan ke-" + attemptCount);
                if (attemptCount >= maxAttempts) {
                    System.out.print("Anda telah mencapai batas percobaan login.");
                    System.out.println("Apakah Anda ingin membuat akun baru? (y/n): ");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("y")) {
                        registrasi();
                    }
                    return false;
                }
            }
        }
        return false;
    }

    // Metode untuk registrasi pengguna baru
    public void registrasi() {
        System.out.print("Masukkan username baru: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan password baru: ");
        String password = scanner.nextLine();

        // Periksa apakah username sudah digunakan dengan query ke database
        if (UsersController.usernameExists(username)) {
            System.out.println("Username sudah digunakan. Registrasi gagal.");
        } else {
            UsersController.addUser(username, password);
            System.out.println("Registrasi berhasil.");
        }
    }
}
