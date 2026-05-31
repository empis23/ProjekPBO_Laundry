-- SQL Dump/Schema for Laundry Management Application
-- Database: db_laundry

CREATE DATABASE IF NOT EXISTS db_laundry;
USE db_laundry;

-- 1. Table structure for table `admin`
CREATE TABLE IF NOT EXISTS `admin` (
  `id_admin` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id_admin`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert default admin (username: admin, password: admin123)
-- Adjust password as needed. The AuthController checks password as-is (plain text comparison).
INSERT INTO `admin` (`id_admin`, `username`, `password`) VALUES
(1, 'admin', 'admin123')
ON DUPLICATE KEY UPDATE `username`=`username`;

-- 2. Table structure for table `pelanggan`
CREATE TABLE IF NOT EXISTS `pelanggan` (
  `id_pelanggan` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(100) NOT NULL,
  `no_hp` varchar(20) NOT NULL,
  `alamat` text NOT NULL,
  PRIMARY KEY (`id_pelanggan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Table structure for table `layanan`
CREATE TABLE IF NOT EXISTS `layanan` (
  `id_layanan` int(11) NOT NULL AUTO_INCREMENT,
  `nama_layanan` varchar(100) NOT NULL,
  `harga_per_kg` double NOT NULL,
  `estimasi` varchar(50) NOT NULL,
  PRIMARY KEY (`id_layanan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Table structure for table `transaksi`
CREATE TABLE IF NOT EXISTS `transaksi` (
  `id_transaksi` int(11) NOT NULL AUTO_INCREMENT,
  `id_pelanggan` int(11) NOT NULL,
  `id_layanan` int(11) NOT NULL,
  `tanggal_masuk` date NOT NULL,
  `berat` double NOT NULL,
  `total_harga` double NOT NULL,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`id_transaksi`),
  KEY `fk_transaksi_pelanggan` (`id_pelanggan`),
  KEY `fk_transaksi_layanan` (`id_layanan`),
  CONSTRAINT `fk_transaksi_layanan` FOREIGN KEY (`id_layanan`) REFERENCES `layanan` (`id_layanan`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_transaksi_pelanggan` FOREIGN KEY (`id_pelanggan`) REFERENCES `pelanggan` (`id_pelanggan`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
