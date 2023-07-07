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
    private JButton btnKeluar;
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
        btnKeluar.addActionListener(e -> {
            logout();
        });
    }

    private void logout() {
        Login form1 = new Login();
        form1.setVisible(true);
        form1.setEnabled(true);
        this.dispose();
    }

    private void cariBarang() {
        String keyword = fieldSearch.getText();
        DefaultTableModel model = (DefaultTableModel) tabelBarang.getModel();
        model.setRowCount(0);
        String sql = "SELECT * FROM barang WHERE nama_barang LIKE ? OR merk_barang LIKE ? OR id_barang LIKE ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            statement.setString(3, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Object[] row = new Object[7];
                row[0] = resultSet.getString("id_barang");
                row[1] = resultSet.getString("nama_barang");
                row[2] = resultSet.getString("jenis_barang");
                row[3] = resultSet.getString("merk_barang");
                row[4] = resultSet.getString("berat");
                row[5] = resultSet.getString("jumlah");
                row[6] = resultSet.getString("harga");

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

        String sql = "UPDATE barang SET nama_barang=?, jenis_barang=?, merk_barang=?, berat=?, jumlah=?, harga=? WHERE id_barang=?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, barang.getIdBarang());
            statement.setString(2, barang.getNamaBarang());
            statement.setString(3, barang.getJenisBarang());
            statement.setString(4, barang.getMerkBarang());
            statement.setString(5, barang.getBeratBarang());
            statement.setString(6, barang.getJumlahBarang());
            statement.setString(7, barang.getHargaBarang());

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
        String idBarang = fieldSearch.getText();
        if(idBarang != null && !idBarang.isEmpty()) {
            try {
                Statement statement = (Statement) Connect.getConnection().createStatement();
                statement.executeUpdate("DELETE FROM barang WHERE id_barang = ('" + idBarang + "');");
                JOptionPane.showMessageDialog(null, "data berhasil di hapus");
            } catch (Exception t) {
                JOptionPane.showMessageDialog(null, "data gagal dihapus");
            }
        }
        getData();

    }

    private void getData()  {
        DefaultTableModel model = new DefaultTableModel();
        tabelBarang.setModel(model);
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
                Object[] row = new Object[7];
                row[0] = resultSet.getString("id_barang");
                row[1] = resultSet.getString("nama_barang");
                row[2] = resultSet.getString("jenis_barang");
                row[3] = resultSet.getString("merk_barang");
                row[4] = resultSet.getString("berat");
                row[5] = resultSet.getString("jumlah");
                row[6] = resultSet.getString("harga");

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
