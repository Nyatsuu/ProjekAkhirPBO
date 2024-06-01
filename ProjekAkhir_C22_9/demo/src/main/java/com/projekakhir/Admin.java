package com.projekakhir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin {

    private final Scanner scanner;
    private List<String> specialPizzaToppings = new ArrayList<>();

    public Admin() {
        scanner = new Scanner(System.in);
        // Initialize the list with some example toppings
        specialPizzaToppings.add("Pepperoni");
        specialPizzaToppings.add("Mushrooms");
        specialPizzaToppings.add("Onions");
    }

    // Metode untuk menampilkan menu admin
    public void adminMenu() {
        int choice;
        do {
            System.out.println("Menu Admin:");
            System.out.println("1. Tambah Pizza");
            System.out.println("2. Tampilkan Data Pizza");
            System.out.println("3. Update Data Pizza");
            System.out.println("4. Hapus Data Pizza");
            System.out.println("5. Logout");
            System.out.print("Pilihan Anda: ");
            
            while (!scanner.hasNextInt()) {
                System.out.println("Harap masukkan angka yang valid.");
                scanner.next(); // Membersihkan input yang salah
                System.out.print("Pilihan Anda: ");
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Membersihkan \n di buffer

            if (choice <= 0) {
                System.out.println("Masukkan angka yang lebih besar dari 0.");
                continue;
            }

            switch (choice) {
                case 1 -> tambahPizza();
                case 2 -> tampilkanDataPizza();
                case 3 -> updateDataPizza();
                case 4 -> hapusDataPizza();
                case 5 -> System.out.println("Logout dari akun admin.");
                default -> System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 5);
    }

    // Metode untuk menambah pizza
    private void tambahPizza() {
        System.out.println("Pilihan jenis pizza:");
        System.out.println("1. Pizza Spesial");
        System.out.println("2. Pizza Biasa");
        System.out.print("Pilihan Anda: ");
        int jenisPizza;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("Masukkan angka yang valid.");
                scanner.next(); // Membersihkan input yang salah
                System.out.print("Pilihan Anda: ");
            }
            jenisPizza = scanner.nextInt();
            scanner.nextLine(); // Membersihkan \n di buffer
            if (jenisPizza != 1 && jenisPizza != 2) {
                System.out.println("Pilihan tidak valid. Silakan pilih 1 atau 2.");
            }
        } while (jenisPizza != 1 && jenisPizza != 2);

        try {
            System.out.print("Input topping: ");
            String topping = scanner.nextLine();
            double harga;
            do {
                System.out.print("Input harga (harus angka positif): ");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Harga harus berupa angka.");
                    scanner.next(); // Membersihkan input yang salah
                    System.out.print("Input harga (harus angka positif): ");
                }
                harga = scanner.nextDouble();
                scanner.nextLine(); // Membersihkan \n di buffer
                if (harga <= 0) {
                    System.out.println("Harga harus lebih besar dari 0.");
                }
            } while (harga <= 0);

            // Menambahkan pizza tanpa deskripsi
            Pizza.addPizza(jenisPizza == 1 ? "PizzaSpesial" : "PizzaBiasa", topping, harga);
            System.out.println((jenisPizza == 1 ? "Pizza Spesial" : "Pizza Biasa") + " berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat menambahkan pizza: " + e.getMessage());
        }
    }

    // Metode untuk menampilkan data pizza
    public void tampilkanDataPizza() {
        int pilihan;
        do {
            System.out.println("Pilih jenis pizza untuk ditampilkan:");
            System.out.println("1. Pizza Spesial");
            System.out.println("2. Pizza Biasa");
            System.out.println("3. Keluar");
            System.out.print("Masukkan pilihan Anda: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Harap masukkan angka yang valid.");
                scanner.next(); // Membersihkan input yang salah
                System.out.println("Pilih jenis pizza untuk ditampilkan:");
                System.out.println("1. Pizza Spesial");
                System.out.println("2. Pizza Biasa");
                System.out.println("3. Keluar");
                System.out.print("Masukkan pilihan Anda: ");
            }
            pilihan = scanner.nextInt();
            scanner.nextLine(); // Membersihkan \n di buffer

            if (pilihan <= 0) {
                System.out.println("Masukkan angka yang lebih besar dari 0.");
                continue;
            }

            switch (pilihan) {
                case 1 -> tampilkanPizza("PizzaSpesial", PizzaSpesial.class);
                case 2 -> tampilkanPizza("PizzaBiasa", PizzaBiasa.class);
                case 3 -> System.out.println("Kembali ke menu utama.");
                default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (pilihan != 3);
    }

    private <T extends Pizza> void tampilkanPizza(String table, Class<T> pizzaClass) {
        try {
            List<T> pizzas = Pizza.loadAllPizzas(table, pizzaClass);
            System.out.println("Data " + table + ":");
            for (int i = 0; i < pizzas.size(); i++) {
                Pizza pizza = pizzas.get(i);
                System.out.println((i + 1) + ". Topping: " + pizza.getTopping() + ", Harga: " + pizza.getPrice());
            }
        } catch (Exception e) {
            System.out.println("Error fetching pizza data: " + e.getMessage());
        }
    }

    // Metode untuk mengupdate data pizza
    private void updateDataPizza() {
        int jenisPizza;
        do {
            System.out.println("Pilihan jenis pizza:");
            System.out.println("1. Pizza Spesial");
            System.out.println("2. Pizza Biasa");
            System.out.print("Pilihan Anda: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Masukkan angka yang valid.");
                scanner.next();
                System.out.println("Pilihan jenis pizza:");
                System.out.println("1. Pizza Spesial");
                System.out.println("2. Pizza Biasa");
                System.out.print("Pilihan Anda: ");
            }
            jenisPizza = scanner.nextInt();
            scanner.nextLine();
            if (jenisPizza != 1 && jenisPizza != 2) {
                System.out.println("Pilihan tidak valid. Silakan pilih 1 atau 2.");
            }
        } while (jenisPizza != 1 && jenisPizza != 2);

        String table = jenisPizza == 1 ? "PizzaSpesial" : "PizzaBiasa";

        // Menampilkan pizza yang tersedia untuk dipilih
        if (jenisPizza == 1) {
            tampilkanDataPizzaSpesial();
        } else {
            tampilkanDataPizzaBiasa();
        }

        int nomorPizza;
        int maxIndex = jenisPizza == 1 ? getPizzaCount("PizzaSpesial") : getPizzaCount("PizzaBiasa");
        do {
            System.out.print("Masukkan nomor pizza yang akan diupdate (1-" + maxIndex + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Masukkan angka yang valid.");
                scanner.next();
                System.out.print("Masukkan nomor pizza yang akan diupdate (1-" + maxIndex + "): ");
            }
            nomorPizza = scanner.nextInt();
            scanner.nextLine();
            if (nomorPizza < 1 || nomorPizza > maxIndex) {
                System.out.println("Nomor pizza tidak valid. Silakan masukkan nomor yang ada dalam daftar.");
            }
        } while (nomorPizza < 1 || nomorPizza > maxIndex);

        System.out.print("Masukkan topping baru: ");
        String toppingBaru = scanner.nextLine();
        while (!toppingBaru.matches("[a-zA-Z ]+")) {
            System.out.println("Input harus berupa huruf. Silakan masukkan kembali:");
            toppingBaru = scanner.nextLine();
        }

        double hargaBaru;
        do {
            System.out.print("Masukkan harga baru (minimal Rp1): ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Harga harus berupa angka. Silakan masukkan kembali.");
                scanner.next();
                System.out.print("Masukkan harga baru (minimal Rp1): ");
            }
            hargaBaru = scanner.nextDouble();
            scanner.nextLine();
            if (hargaBaru < 1) {
                System.out.println("Harga tidak boleh kurang dari Rp1.");
            }
        } while (hargaBaru < 1);

        try {
            Pizza.updatePizzaByIndex(table, nomorPizza - 1, toppingBaru, hargaBaru);
            System.out.println("Data pizza berhasil diupdate.");
        } catch (Exception e) {
            System.out.println("Gagal mengupdate data pizza: " + e.getMessage());
        }

        // Menampilkan data pizza yang baru diupdate
        if (jenisPizza == 1) {
            tampilkanDataPizzaSpesial();
        } else {
            tampilkanDataPizzaBiasa();
        }
    }

    private int getPizzaCount(String table) {
        String sql = "SELECT COUNT(*) AS count FROM " + table;
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching pizza count: " + e.getMessage());
        }
        return 0;
    }

    // Metode untuk menghapus data pizza
    private void hapusDataPizza() {
        System.out.println("Pilihan jenis pizza:");
        System.out.println("1. Pizza Spesial");
        System.out.println("2. Pizza Biasa");
        System.out.print("Pilihan Anda: ");
        int jenisPizza;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("Masukkan angka yang valid.");
                scanner.next(); // Membersihkan input yang salah
                System.out.println("Pilihan jenis pizza:");
                System.out.println("1. Pizza Spesial");
                System.out.println("2. Pizza Biasa");
                System.out.print("Pilihan Anda: ");
            }
            jenisPizza = scanner.nextInt();
            scanner.nextLine(); // Membersihkan \n di buffer
            if (jenisPizza != 1 && jenisPizza != 2) {
                System.out.println("Pilihan tidak valid. Silakan pilih 1 atau 2.");
            }
        } while (jenisPizza != 1 && jenisPizza != 2);

        String table = jenisPizza == 1 ? "PizzaSpesial" : "PizzaBiasa";

        // Menampilkan daftar pizza
        if (jenisPizza == 1) {
            tampilkanDataPizzaSpesial();
        } else {
            tampilkanDataPizzaBiasa();
        }

        int maxIndex = getPizzaCount(table);
        int nomorPizza;
        do {
            System.out.print("Masukkan nomor pizza yang akan dihapus (1-" + maxIndex + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Masukkan angka yang valid.");
                scanner.next(); // Membersihkan input yang salah
                System.out.print("Masukkan nomor pizza yang akan dihapus (1-" + maxIndex + "): ");
            }
            nomorPizza = scanner.nextInt();
            scanner.nextLine(); // Membersihkan \n di buffer
            if (nomorPizza < 1 || nomorPizza > maxIndex) {
                System.out.println("Nomor pizza tidak valid. Silakan masukkan nomor yang ada dalam daftar.");
            }
        } while (nomorPizza < 1 || nomorPizza > maxIndex);

        try {
            Pizza.deletePizzaByIndex(table, nomorPizza - 1);
            System.out.println("Data pizza berhasil dihapus.");
        } catch (SQLException e) {
            System.out.println("Gagal menghapus pizza: " + e.getMessage());
        }
    }

    public void tampilkanDataPizzaSpesial() {
        String sql = "SELECT topping, harga FROM PizzaSpesial";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("Data Pizza Spesial:");
            int index = 1;
            while (rs.next()) {
                String topping = rs.getString("topping");
                double harga = rs.getDouble("harga");
                System.out.println(index + ". Topping: " + topping + ", Harga: " + harga);
                index++;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Pizza Spesial data: " + e.getMessage());
        }
    }

    // Metode untuk menampilkan data pizza biasa dari database
    public void tampilkanDataPizzaBiasa() {
        String sql = "SELECT topping, harga FROM PizzaBiasa";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("Data Pizza Biasa:");
            int index = 1;
            while (rs.next()) {
                String topping = rs.getString("topping");
                double harga = rs.getDouble("harga");
                System.out.println(index + ". Topping: " + topping + ", Harga: " + harga);
                index++;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Pizza Biasa data: " + e.getMessage());
        }
    }

    public String getToppingPizzaSpesial(int pizzaId) {
        // Implementation to retrieve the topping based on pizzaId
        // This is a placeholder, adjust according to your data structure
        return "Topping for Pizza ID " + pizzaId;
    }
    
    public String getToppingPizzaBiasa(int pizzaId) {
        String topping = "";
        String sql = "SELECT topping FROM PizzaBiasa WHERE id = ?";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pizzaId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                topping = rs.getString("topping");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching topping for Pizza Biasa: " + e.getMessage());
        }
        return topping;
    }
    

}
