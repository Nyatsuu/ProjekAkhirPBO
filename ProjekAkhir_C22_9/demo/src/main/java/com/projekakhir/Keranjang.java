package com.projekakhir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Keranjang {
    private int userId;

    public Keranjang(int userId) {
        this.userId = userId;
    }

    public void tambahKeKeranjang(String jenisPizza, String topping, int pizzaId, int quantity) {
        String sql = "INSERT INTO tbkeranjang (user_id, jenis_pizza, topping, pizza_id, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, jenisPizza);
            pstmt.setString(3, topping);
            pstmt.setInt(4, pizzaId);
            pstmt.setInt(5, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding to cart: " + e.getMessage());
        }
    }

    public void lihatKeranjang() {
        String sql = "SELECT id, jenis_pizza, topping, quantity FROM tbkeranjang WHERE user_id = ?";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Isi Keranjang:");
            int index = 1;
            while (rs.next()) {
                int id = rs.getInt("id");
                String jenisPizza = rs.getString("jenis_pizza");
                String topping = rs.getString("topping");
                int quantity = rs.getInt("quantity");
                System.out.println(index + ". ID: " + id + ", Jenis Pizza: " + jenisPizza + ", Topping: " + topping + ", Jumlah: " + quantity);
                index++;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching cart items: " + e.getMessage());
        }
    }
    

    public void editKeranjang(int id, int newQuantity, User user) {
        String sql = "UPDATE tbkeranjang SET quantity = ? WHERE id = ?";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Keranjang berhasil diperbarui.");
        } catch (SQLException e) {
            System.out.println("Error updating cart item: " + e.getMessage());
        }
        user.userMenu();
    }

    public void hapusItemDariKeranjang(int id, User user) {
        String sqlDelete = "DELETE FROM tbkeranjang WHERE id = ?";
        String sqlAlter = "ALTER TABLE tbkeranjang AUTO_INCREMENT = 0";

        try (Connection conn = new DB().con;
             PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete);
             Statement stmtAlter = conn.createStatement()) {
            
            // Menghapus item
            pstmtDelete.setInt(1, id);
            int affectedRows = pstmtDelete.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Item berhasil dihapus.");
                // Mengatur ulang AUTO_INCREMENT
                stmtAlter.execute(sqlAlter);
                System.out.println("AUTO_INCREMENT telah diatur ulang.");
            } else {
                System.out.println("Tidak ada item yang dihapus.");
            }
        } catch (SQLException e) {
            System.out.println("Error saat menghapus item: " + e.getMessage());
        }
        user.userMenu();
    }

    public double hitungTotalHarga() {
        double total = 0;
        String sql = "SELECT jenis_pizza, pizza_id, quantity FROM tbkeranjang WHERE user_id = ?";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String jenisPizza = rs.getString("jenis_pizza");
                int pizzaId = rs.getInt("pizza_id");
                int quantity = rs.getInt("quantity");
                double harga = 0;
    
                // Query untuk mengambil harga pizza sesuai dengan jenisnya
                String queryHarga = "";
                if (jenisPizza.equals("PizzaSpesial")) {
                    queryHarga = "SELECT harga FROM pizzaspesial WHERE id = ?";
                } else if (jenisPizza.equals("PizzaBiasa")) {
                    queryHarga = "SELECT harga FROM pizzabiasa WHERE id = ?";
                }
    
                try (PreparedStatement pstmtHarga = conn.prepareStatement(queryHarga)) {
                    pstmtHarga.setInt(1, pizzaId);
                    ResultSet rsHarga = pstmtHarga.executeQuery();
                    if (rsHarga.next()) {
                        harga = rsHarga.getDouble("harga");
                    }
                } catch (SQLException ex) {
                    System.out.println("Error fetching pizza price: " + ex.getMessage());
                }
    
                total += harga * quantity;
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total price: " + e.getMessage());
        }
        return total;
    }
    

    public void clearKeranjang() {
        String sql = "DELETE FROM tbkeranjang WHERE user_id = ?";
        try (Connection conn = new DB().con;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }

    public void resetAutoIncrement() {
        // Contoh query untuk reset auto increment pada MySQL
        String sql = "ALTER TABLE tbkeranjang AUTO_INCREMENT = 1;";
        try (Connection conn = new DB().con;
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error saat mereset auto increment: " + e.getMessage());
        }
    }
}
