package view;

import entity.Barang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Home extends javax.swing.JFrame {
    private JTextField fieldNama;
    private JTextField fieldBerat;
    private JComboBox<String> cmbJenis;
    private JTextField fieldHarga;
    private JTextField filedJumlah;
    private JSplitPane splitPane;
    private JPanel panelHome;
    private JTextField fieldSearch;
    private JButton btnSimpan;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnCari;
    private JTable tabelBarang;
    private JComboBox<String> cmbMerk;
    private JTextField fieldID;
    private JTextField filedNo;
    private final Barang barang = new Barang();


    public Home() {
        getData();
        btnSimpan.addActionListener(e -> {
            tambahBarang();
        });
        btnDelete.addActionListener(e -> {
            hapusBarang();
        });
        btnUpdate.addActionListener(e -> {
            updateBarang();
        });
        btnCari.addActionListener(e -> {
            cariBarang();
        });
    }

    private void cariBarang() {
        String keyword = fieldSearch.getText();
        DefaultTableModel model = (DefaultTableModel) tabelBarang.getModel();
        model.setRowCount(0);
        String sql = "SELECT * FROM barang WHERE nama_barang LIKE ? OR merk_barang LIKE ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Object[] row = new Object[8];
                row[0] = resultSet.getString("id");
                row[1] = resultSet.getString("id_barang");
                row[2] = resultSet.getString("nama_barang");
                row[3] = resultSet.getString("jenis_barang");
                row[4] = resultSet.getString("merk_barang");
                row[5] = resultSet.getString("berat");
                row[6] = resultSet.getString("jumlah");
                row[7] = resultSet.getString("harga");

                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
        }
    }

    private void updateBarang() {
        barang.setId(Integer.parseInt(filedNo.getText()));
        barang.setIdBarang(fieldID.getText());  
        barang.setNamaBarang(fieldNama.getText());
        barang.setJenisBarang((String) cmbJenis.getSelectedItem());
        barang.setMerkBarang((String) cmbMerk.getSelectedItem());
        barang.setBeratBarang(fieldBerat.getText());
        barang.setJumlahBarang(filedJumlah.getText());
        barang.setHargaBarang(fieldHarga.getText());

        String sql = "UPDATE barang SET id_barang=?, nama_barang=?, jenis_barang=?, merk_barang=?, berat=?, jumlah=?, harga=? WHERE id=?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, barang.getIdBarang());
            statement.setString(2, barang.getNamaBarang());
            statement.setString(3, barang.getJenisBarang());
            statement.setString(4, barang.getMerkBarang());
            statement.setString(5, barang.getBeratBarang());
            statement.setString(6, barang.getJumlahBarang());
            statement.setString(7, barang.getHargaBarang());
            statement.setInt(8, barang.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Data telah berhasil diperbarui");
                getData(); // Refresh tampilan tabel setelah pembaruan data
                clearData(); // Menghapus data pada field
            } else {
                JOptionPane.showMessageDialog(null, "Gagal memperbarui data: ID tidak ditemukan");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui data: " + ex.getMessage());
        }

    }

    private void hapusBarang() {
        int selectedRow = tabelBarang.getSelectedRow();
        int id;
        id = (int) tabelBarang.getValueAt(selectedRow, 0);
        String sql = "DELETE FROM barang WHERE id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data telah berhasil dihapus");
            getData(); // Refresh tampilan tabel setelah penghapusan data
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus data: " + ex.getMessage());
        }
    }

    private void getData()  {
        DefaultTableModel model = new DefaultTableModel();
        tabelBarang.setModel(model);
        model.addColumn("NO");
        model.addColumn("ID Barang");
        model.addColumn("Nama");
        model.addColumn("Jenis");
        model.addColumn("Merk");
        model.addColumn("Berat");
        model.addColumn("Jumlah");
        model.addColumn("Harga");

        String sql = "SELECT * FROM barang";
        try (Connection conn = Connect.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Object[] row = new Object[8];
                row[0] = resultSet.getString("id");
                row[1] = resultSet.getString("id_barang");
                row[2] = resultSet.getString("nama_barang");
                row[3] = resultSet.getString("jenis_barang");
                row[4] = resultSet.getString("merk_barang");
                row[5] = resultSet.getString("berat");
                row[6] = resultSet.getString("jumlah");
                row[7] = resultSet.getString("harga");

                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());

        }
    }

    private void tambahBarang(){
        barang.setIdBarang(fieldID.getText());
        barang.setNamaBarang(fieldNama.getText());
        barang.setJenisBarang((String) cmbJenis.getSelectedItem());
        barang.setMerkBarang((String) cmbMerk.getSelectedItem());
        barang.setBeratBarang(fieldBerat.getText());
        barang.setJumlahBarang(filedJumlah.getText());
        barang.setHargaBarang(fieldHarga.getText());

        String sql = "INSERT INTO barang (id_barang, nama_barang, jenis_barang, merk_barang, berat,jumlah, harga) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = Connect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1,barang.getIdBarang());
            statement.setString(2, barang.getNamaBarang());
            statement.setString(3, barang.getJenisBarang());
            statement.setString(4, barang.getMerkBarang());
            statement.setString(5, barang.getBeratBarang());
            statement.setString(6, barang.getJumlahBarang());
            statement.setString(7, barang.getHargaBarang());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data telah berhasil disimpan");
            getData(); // Refresh tampilan tabel setelah penyimpanan data
            clearData(); // Menghapus data pada field
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + ex.getMessage());
        }
    }

    private void clearData() {
        fieldID.setText("");
        fieldNama.setText("");
        cmbJenis.setSelectedItem("");
        cmbMerk.setSelectedItem("");
        fieldBerat.setText("");
        filedJumlah.setText("");
        fieldHarga.setText("");
    }

    public JSplitPane getPanelHome() {
        return splitPane;
    }
}
