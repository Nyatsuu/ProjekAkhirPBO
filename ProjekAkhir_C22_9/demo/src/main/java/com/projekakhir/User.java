package com.projekakhir;

import java.util.Scanner;

public class User {
    private final Scanner scanner;
    private final Keranjang keranjang;

    public User(int userId) {
        scanner = new Scanner(System.in);
        keranjang = new Keranjang(userId);
        keranjang.clearKeranjang();
        keranjang.resetAutoIncrement();
    }

    public void userMenu() {
        int choice;
        do {
            System.out.println("Menu User:");
            System.out.println("1. Lihat Pizza");
            System.out.println("2. Tambah ke Keranjang");
            System.out.println("3. Lihat Keranjang");
            System.out.println("4. Pembayaran");
            System.out.println("5. Logout");
            System.out.print("Pilihan Anda: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Membersihkan \n di buffer

            switch (choice) {
                case 1 -> lihatPizza();
                case 2 -> tambahKeKeranjang();
                case 3 -> lihatKeranjang();
                case 4 -> pembayaran();
                case 5 -> logout();
                default -> System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 5);
    }

    private void lihatPizza() {
        Admin admin = new Admin();
        admin.tampilkanDataPizza();
    }

    private void tambahKeKeranjang() {
        System.out.println("Menu Tambah ke Keranjang:");
        System.out.println("1. Pizza Spesial");
        System.out.println("2. Pizza Biasa");
        System.out.print("Pilihan Anda: ");
        int jenisPizza = scanner.nextInt();
        scanner.nextLine(); // Membersihkan \n di buffer

        String jenisPizzaStr;
        Admin admin = new Admin();
        int pizzaId; // Declare pizzaId here

        switch (jenisPizza) {
            case 1:
                jenisPizzaStr = "Pizza Spesial";
                System.out.println("Anda memilih: " + jenisPizzaStr);
                admin.tampilkanDataPizzaSpesial();
                break;
            case 2:
                jenisPizzaStr = "Pizza Biasa";
                System.out.println("Anda memilih: " + jenisPizzaStr);
                admin.tampilkanDataPizzaBiasa();
                break;
            default:
                System.out.println("Pilihan tidak valid.");
                return;
        }

        System.out.print("Masukkan ID pizza yang ingin ditambahkan ke keranjang: ");
        pizzaId = scanner.nextInt();
        scanner.nextLine(); // Membersihkan \n di buffer

        System.out.print("Masukkan jumlah: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Membersihkan \n di buffer

        // Menambahkan ke keranjang
        String jenisPizzaTable = jenisPizza == 1 ? "PizzaSpesial" : "PizzaBiasa";
        String pizzaName = "Name of the Pizza"; // Sesuaikan dengan logika untuk mendapatkan nama pizza
        keranjang.tambahKeKeranjang(jenisPizzaTable, pizzaName, pizzaId, quantity);
        System.out.println("Pizza berhasil ditambahkan ke keranjang.");
    }

    private void lihatKeranjang() {
        keranjang.lihatKeranjang();
        System.out.println("--------------------"); // Garis pemisah

        int choice;
        do {
            System.out.println("1. Edit Keranjang");
            System.out.println("2. Hapus dari Keranjang");
            System.out.println("3. Kembali");
            System.out.print("Pilihan Anda: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Membersihkan \n di buffer

            switch (choice) {
                case 1:
                    editKeranjang();
                    break;
                case 2:
                    hapusDariKeranjang();
                    break;
                case 3:
                    System.out.println("Kembali ke menu utama.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }
        } while (choice != 3);
    }

    private void editKeranjang() {
        System.out.print("Masukkan ID item keranjang yang ingin diubah: ");
        int id = scanner.nextInt();
        System.out.print("Masukkan jumlah baru: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Membersihkan \n di buffer

        keranjang.editKeranjang(id, quantity, this);
        System.out.println("Item keranjang berhasil diubah.");
    }

    private void hapusDariKeranjang() {
        System.out.print("Masukkan ID item keranjang yang ingin dihapus: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Membersihkan \n di buffer

        keranjang.hapusItemDariKeranjang(id, this);
        System.out.println("Item keranjang berhasil dihapus.");
    }

    private void pembayaran() {
        System.out.println("Isi keranjang Anda:");
        keranjang.lihatKeranjang(); // Menampilkan isi keranjang sebelum pembayaran

        double totalHarga = keranjang.hitungTotalHarga();
        System.out.println("Total harga keranjang: " + totalHarga);

        System.out.print("Masukkan jumlah uang: ");
        double uang = scanner.nextDouble();
        scanner.nextLine(); // Membersihkan \n di buffer

        if (uang < totalHarga) {
            System.out.println("Uang tidak cukup.");
        } else {
            double kembalian = uang - totalHarga;
            System.out.println("Pembayaran berhasil. Kembalian: " + kembalian);
            keranjang.clearKeranjang(); // Mengosongkan keranjang setelah pembayaran
        }
    }

    private void logout() {
        System.out.println("Logout berhasil.");
    }
}

