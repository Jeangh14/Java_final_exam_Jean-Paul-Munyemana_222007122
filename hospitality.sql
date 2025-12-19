-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 19, 2025 at 08:48 PM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hospitality`
--

-- --------------------------------------------------------

--
-- Table structure for table `guest`
--

DROP TABLE IF EXISTS `guest`;
CREATE TABLE IF NOT EXISTS `guest` (
  `GuestID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `PasswordHash` varchar(255) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `FullName` varchar(100) NOT NULL,
  `Role` enum('Patient','Doctor','Nurse','Admin') NOT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `LastLogin` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`GuestID`),
  UNIQUE KEY `Username` (`Username`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `guest`
--

INSERT INTO `guest` (`GuestID`, `Username`, `PasswordHash`, `Email`, `FullName`, `Role`, `CreatedAt`, `LastLogin`) VALUES
(1, 'kabaka', '1111', 'kabaka.com', 'kabaka lupo', 'Doctor', '2025-12-18 19:21:17', '2025-12-18 19:22:11');

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
CREATE TABLE IF NOT EXISTS `invoice` (
  `InvoiceID` int NOT NULL AUTO_INCREMENT,
  `GuestID` int NOT NULL,
  `Amount` decimal(10,2) NOT NULL,
  `Date` date NOT NULL,
  `Type` enum('Room','Service','Treatment') NOT NULL,
  `Reference` varchar(100) DEFAULT NULL,
  `Status` enum('Paid','Unpaid') DEFAULT 'Unpaid',
  PRIMARY KEY (`InvoiceID`),
  KEY `GuestID` (`GuestID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `ReservationID` int NOT NULL AUTO_INCREMENT,
  `OrderNumber` varchar(50) NOT NULL,
  `GuestID` int NOT NULL,
  `Date` date NOT NULL,
  `Status` enum('Confirmed','Pending','Cancelled') DEFAULT 'Pending',
  `TotalAmount` decimal(10,2) NOT NULL,
  `PaymentMethod` enum('Cash','Card','Insurance') NOT NULL,
  `Notes` text,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ReservationID`),
  UNIQUE KEY `OrderNumber` (`OrderNumber`),
  KEY `GuestID` (`GuestID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reservation_room`
--

DROP TABLE IF EXISTS `reservation_room`;
CREATE TABLE IF NOT EXISTS `reservation_room` (
  `ReservationRoomID` int NOT NULL AUTO_INCREMENT,
  `ReservationID` int NOT NULL,
  `RoomID` int NOT NULL,
  PRIMARY KEY (`ReservationRoomID`),
  KEY `ReservationID` (`ReservationID`),
  KEY `RoomID` (`RoomID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
CREATE TABLE IF NOT EXISTS `room` (
  `RoomID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Description` text,
  `Category` enum('General','Private','ICU','Emergency') NOT NULL,
  `PriceOrValue` decimal(10,2) NOT NULL,
  `Status` enum('Available','Occupied','Maintenance') DEFAULT 'Available',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `GuestID` int DEFAULT NULL,
  PRIMARY KEY (`RoomID`),
  KEY `GuestID` (`GuestID`)
) ENGINE=MyISAM AUTO_INCREMENT=125 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`RoomID`, `Name`, `Description`, `Category`, `PriceOrValue`, `Status`, `CreatedAt`, `GuestID`) VALUES
(115, 'Room 115', 'A stylish room with a king-sized bed and a large wardrobe.', 'General', 180.00, 'Available', '2025-12-19 06:51:15', NULL),
(116, 'Room 116', 'Spacious suite with a balcony offering a panoramic view.', 'Private', 400.00, 'Available', '2025-12-19 06:51:15', NULL),
(117, 'Room 117', 'Standard room with a comfortable queen-sized bed and free Wi-Fi.', 'General', 110.00, 'Available', '2025-12-19 06:51:15', NULL),
(118, 'Room 118', 'Luxury room with a king-sized bed and a jacuzzi.', 'Private', 500.00, 'Available', '2025-12-19 06:51:15', NULL),
(119, 'Room 119', 'Simple yet comfortable room with a double bed and a desk.', 'General', 90.00, 'Available', '2025-12-19 06:51:15', NULL),
(120, 'Room 120', 'Modern room with a double bed, ideal for solo travelers.', 'General', 95.00, 'Available', '2025-12-19 06:51:15', NULL),
(121, 'Room 121', 'Cozy private room with a queen-sized bed and a full bathroom.', 'Private', 230.00, 'Available', '2025-12-19 06:51:15', NULL),
(122, 'Room 122', 'Family-friendly room with two queen-sized beds and a sitting area.', 'General', 160.00, 'Available', '2025-12-19 06:51:15', NULL),
(123, 'Room 123', 'Room with a single bed and a nice reading nook, perfect for students.', 'General', 70.00, 'Available', '2025-12-19 06:51:15', NULL),
(124, 'Room 124', 'A quiet room with a double bed and a desk for business travelers.', 'General', 105.00, 'Available', '2025-12-19 06:51:15', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
CREATE TABLE IF NOT EXISTS `service` (
  `ServiceID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Description` text,
  `Category` enum('Consultation','Surgery','Lab Test','Therapy') NOT NULL,
  `PriceOrValue` decimal(10,2) NOT NULL,
  `Status` enum('Available','Unavailable') DEFAULT 'Available',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `GuestID` int DEFAULT NULL,
  PRIMARY KEY (`ServiceID`),
  KEY `GuestID` (`GuestID`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`ServiceID`, `Name`, `Description`, `Category`, `PriceOrValue`, `Status`, `CreatedAt`, `GuestID`) VALUES
(1, 'In-Room Dining', 'Enjoy a variety of meals and beverages delivered directly to your room at any time.', '', 30.00, 'Available', '2025-12-19 06:53:44', NULL),
(2, 'Spa Massage', 'A relaxing full-body massage to relieve stress and tension.', '', 80.00, 'Available', '2025-12-19 06:53:44', NULL),
(3, 'Daily Housekeeping', 'Daily cleaning and refreshing of your room, including bed-making and bathroom tidying.', '', 25.00, 'Available', '2025-12-19 06:53:44', NULL),
(4, 'Airport Shuttle', 'Free shuttle service from the hotel to the nearest airport, available on request.', '', 0.00, 'Available', '2025-12-19 06:53:44', NULL),
(5, 'Concierge Services', 'Assistance with booking tours, events, restaurants, or special guest requests.', '', 50.00, 'Available', '2025-12-19 06:53:44', NULL),
(6, 'Conference Room Booking', 'Reserve a conference room for meetings, presentations, or events.', '', 200.00, 'Available', '2025-12-19 06:53:44', NULL),
(7, 'City Tour', 'A guided tour of the local attractions, including transportation and a professional guide.', '', 75.00, 'Available', '2025-12-19 06:53:44', NULL),
(8, 'Restaurant Dining', 'Fine dining at the hotel restaurant, serving a variety of gourmet dishes and wines.', '', 50.00, 'Available', '2025-12-19 06:53:44', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `service_invoice`
--

DROP TABLE IF EXISTS `service_invoice`;
CREATE TABLE IF NOT EXISTS `service_invoice` (
  `ServiceInvoiceID` int NOT NULL AUTO_INCREMENT,
  `ServiceID` int NOT NULL,
  `InvoiceID` int NOT NULL,
  PRIMARY KEY (`ServiceInvoiceID`),
  KEY `ServiceID` (`ServiceID`),
  KEY `InvoiceID` (`InvoiceID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
CREATE TABLE IF NOT EXISTS `staff` (
  `StaffID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Identifier` varchar(50) NOT NULL,
  `Status` enum('Active','Inactive') DEFAULT 'Active',
  `Location` varchar(100) DEFAULT NULL,
  `Contact` varchar(100) DEFAULT NULL,
  `AssignedSince` date DEFAULT NULL,
  `GuestID` int DEFAULT NULL,
  PRIMARY KEY (`StaffID`),
  UNIQUE KEY `Identifier` (`Identifier`),
  KEY `GuestID` (`GuestID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
