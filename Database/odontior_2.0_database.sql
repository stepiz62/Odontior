/*
	Copyright (c) 2015, Stefano Pizzocaro. All rights reserved. Use is subject to license terms.

	This file is part of Odontior 2.0 database.

	Odontior 2.0 database is free software: you can redistribute it and/or modify
	it under the terms of the GNU Lesser General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	Odontior 2.0 database is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General Public License
	along with Odontior 2.0 database.  If not, see <http://www.gnu.org/licenses/>.
 */

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE IF NOT EXISTS `odontiordb`;
USE `odontiordb`;


CREATE TABLE IF NOT EXISTS `application` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `locale_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Index 2` (`locale_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `application` (`id`, `locale_id`) VALUES
	(2, 9);

CREATE TABLE IF NOT EXISTS `appointment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  `daypart_id` int(11) DEFAULT NULL,
  `duration` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `intervention_id` int(11) DEFAULT NULL,
  `practitioner_id` int(11) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `macro` smallint(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `CUSTOMER` (`patient_id`),
  KEY `INTERVENTION` (`intervention_id`),
  KEY `OPERATOR` (`practitioner_id`),
  KEY `DAYPART` (`daypart_id`),
  KEY `DAY_ID` (`date`),
  KEY `LOCATION` (`location_id`),
  CONSTRAINT `FK_appointment_armchair` FOREIGN KEY (`location_id`) REFERENCES `armchair` (`id`) ON UPDATE NO ACTION,
  CONSTRAINT `FK_appointment_intervention` FOREIGN KEY (`intervention_id`) REFERENCES `intervention` (`id`) ON UPDATE NO ACTION,
  CONSTRAINT `FK_appointment_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON UPDATE NO ACTION,
  CONSTRAINT `FK_appointment_practitioner` FOREIGN KEY (`practitioner_id`) REFERENCES `practitioner` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `appointminterv` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `intervention_id` int(11) NOT NULL,
  `appointment_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `APPOINTMENT` (`appointment_id`),
  KEY `INTERVENTION` (`intervention_id`),
  CONSTRAINT `FK_appointminterv_appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`) ON UPDATE NO ACTION,
  CONSTRAINT `FK_appointminterv_intervention` FOREIGN KEY (`intervention_id`) REFERENCES `intervention` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `appuser` (
  `userName` varchar(30) DEFAULT NULL,
  `lastName` varchar(20) DEFAULT NULL,
  `firstName` varchar(20) DEFAULT NULL,
  `forcePwdChange` smallint(6) DEFAULT '1',
  `updatePwdDate` date DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

INSERT INTO `appuser` (`userName`, `lastName`, `firstName`, `forcePwdChange`, `updatePwdDate`, `password`, `id`) VALUES
	('admin', 'admin', 'admin', 0, '2015-03-31', '9a590183c06bd2a855dcdfda4d46b5cf', 1),
	('user1', NULL, NULL, 0, '2015-04-01', '43aaf24bb96430f4f570e6b2d0c4398c', 2),
	('user2', NULL, NULL, 0, '2015-04-04', 'dce3a4a848d3258fc3e69c35062adf7b', 3);

CREATE TABLE IF NOT EXISTS `armchair` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(64) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `clinicalcard` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL,
  `number` smallint(6) NOT NULL,
  `openingdate` date DEFAULT NULL,
  `closingdate` date DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `out_double` double DEFAULT NULL,
  `out_date` date DEFAULT NULL,
  `out_text` varchar(50) DEFAULT NULL,
  `out_long` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `CUSTOMER` (`patient_id`),
  CONSTRAINT `FK_clinicalcard_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `country` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notes` varchar(255) DEFAULT NULL,
  `title` varchar(10) DEFAULT NULL,
  `lastname` varchar(24) NOT NULL,
  `firstname` varchar(24) NOT NULL,
  `address` varchar(48) NOT NULL,
  `zipcode` varchar(10) NOT NULL,
  `city` varchar(32) NOT NULL,
  `country_id` int(11) DEFAULT NULL,
  `state_id` int(11) DEFAULT NULL,
  `email` varchar(48) DEFAULT NULL,
  `mobilePhone` varchar(20) DEFAULT NULL,
  `workPhone` varchar(20) DEFAULT NULL,
  `homePhone` varchar(20) DEFAULT NULL,
  `taxcode` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `daypart` (
  `id` int(11) NOT NULL,
  `timelabel` varchar(7) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `TIMELABEL` (`timelabel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `daypart` (`id`, `timelabel`) VALUES
	(1, '0:00'),
	(2, '0:05'),
	(3, '0:10'),
	(4, '0:15'),
	(5, '0:20'),
	(6, '0:25'),
	(7, '0:30'),
	(8, '0:35'),
	(9, '0:40'),
	(10, '0:45'),
	(11, '0:50'),
	(12, '0:55'),
	(121, '10:00'),
	(122, '10:05'),
	(123, '10:10'),
	(124, '10:15'),
	(125, '10:20'),
	(126, '10:25'),
	(127, '10:30'),
	(128, '10:35'),
	(129, '10:40'),
	(130, '10:45'),
	(131, '10:50'),
	(132, '10:55'),
	(133, '11:00'),
	(134, '11:05'),
	(135, '11:10'),
	(136, '11:15'),
	(137, '11:20'),
	(138, '11:25'),
	(139, '11:30'),
	(140, '11:35'),
	(141, '11:40'),
	(142, '11:45'),
	(143, '11:50'),
	(144, '11:55'),
	(145, '12:00'),
	(146, '12:05'),
	(147, '12:10'),
	(148, '12:15'),
	(149, '12:20'),
	(150, '12:25'),
	(151, '12:30'),
	(152, '12:35'),
	(153, '12:40'),
	(154, '12:45'),
	(155, '12:50'),
	(156, '12:55'),
	(157, '13:00'),
	(158, '13:05'),
	(159, '13:10'),
	(160, '13:15'),
	(161, '13:20'),
	(162, '13:25'),
	(163, '13:30'),
	(164, '13:35'),
	(165, '13:40'),
	(166, '13:45'),
	(167, '13:50'),
	(168, '13:55'),
	(169, '14:00'),
	(170, '14:05'),
	(171, '14:10'),
	(172, '14:15'),
	(173, '14:20'),
	(174, '14:25'),
	(175, '14:30'),
	(176, '14:35'),
	(177, '14:40'),
	(178, '14:45'),
	(179, '14:50'),
	(180, '14:55'),
	(181, '15:00'),
	(182, '15:05'),
	(183, '15:10'),
	(184, '15:15'),
	(185, '15:20'),
	(186, '15:25'),
	(187, '15:30'),
	(188, '15:35'),
	(189, '15:40'),
	(190, '15:45'),
	(191, '15:50'),
	(192, '15:55'),
	(193, '16:00'),
	(194, '16:05'),
	(195, '16:10'),
	(196, '16:15'),
	(197, '16:20'),
	(198, '16:25'),
	(199, '16:30'),
	(200, '16:35'),
	(201, '16:40'),
	(202, '16:45'),
	(203, '16:50'),
	(204, '16:55'),
	(205, '17:00'),
	(206, '17:05'),
	(207, '17:10'),
	(208, '17:15'),
	(209, '17:20'),
	(210, '17:25'),
	(211, '17:30'),
	(212, '17:35'),
	(213, '17:40'),
	(214, '17:45'),
	(215, '17:50'),
	(216, '17:55'),
	(217, '18:00'),
	(218, '18:05'),
	(219, '18:10'),
	(220, '18:15'),
	(221, '18:20'),
	(222, '18:25'),
	(223, '18:30'),
	(224, '18:35'),
	(225, '18:40'),
	(226, '18:45'),
	(227, '18:50'),
	(228, '18:55'),
	(229, '19:00'),
	(230, '19:05'),
	(231, '19:10'),
	(232, '19:15'),
	(233, '19:20'),
	(234, '19:25'),
	(235, '19:30'),
	(236, '19:35'),
	(237, '19:40'),
	(238, '19:45'),
	(239, '19:50'),
	(240, '19:55'),
	(13, '1:00'),
	(14, '1:05'),
	(15, '1:10'),
	(16, '1:15'),
	(17, '1:20'),
	(18, '1:25'),
	(19, '1:30'),
	(20, '1:35'),
	(21, '1:40'),
	(22, '1:45'),
	(23, '1:50'),
	(24, '1:55'),
	(241, '20:00'),
	(242, '20:05'),
	(243, '20:10'),
	(244, '20:15'),
	(245, '20:20'),
	(246, '20:25'),
	(247, '20:30'),
	(248, '20:35'),
	(249, '20:40'),
	(250, '20:45'),
	(251, '20:50'),
	(252, '20:55'),
	(253, '21:00'),
	(254, '21:05'),
	(255, '21:10'),
	(256, '21:15'),
	(257, '21:20'),
	(258, '21:25'),
	(259, '21:30'),
	(260, '21:35'),
	(261, '21:40'),
	(262, '21:45'),
	(263, '21:50'),
	(264, '21:55'),
	(265, '22:00'),
	(266, '22:05'),
	(267, '22:10'),
	(268, '22:15'),
	(269, '22:20'),
	(270, '22:25'),
	(271, '22:30'),
	(272, '22:35'),
	(273, '22:40'),
	(274, '22:45'),
	(275, '22:50'),
	(276, '22:55'),
	(277, '23:00'),
	(278, '23:05'),
	(279, '23:10'),
	(280, '23:15'),
	(281, '23:20'),
	(282, '23:25'),
	(283, '23:30'),
	(284, '23:35'),
	(285, '23:40'),
	(286, '23:45'),
	(287, '23:50'),
	(288, '23:55'),
	(25, '2:00'),
	(26, '2:05'),
	(27, '2:10'),
	(28, '2:15'),
	(29, '2:20'),
	(30, '2:25'),
	(31, '2:30'),
	(32, '2:35'),
	(33, '2:40'),
	(34, '2:45'),
	(35, '2:50'),
	(36, '2:55'),
	(37, '3:00'),
	(38, '3:05'),
	(39, '3:10'),
	(40, '3:15'),
	(41, '3:20'),
	(42, '3:25'),
	(43, '3:30'),
	(44, '3:35'),
	(45, '3:40'),
	(46, '3:45'),
	(47, '3:50'),
	(48, '3:55'),
	(49, '4:00'),
	(50, '4:05'),
	(51, '4:10'),
	(52, '4:15'),
	(53, '4:20'),
	(54, '4:25'),
	(55, '4:30'),
	(56, '4:35'),
	(57, '4:40'),
	(58, '4:45'),
	(59, '4:50'),
	(60, '4:55'),
	(61, '5:00'),
	(62, '5:05'),
	(63, '5:10'),
	(64, '5:15'),
	(65, '5:20'),
	(66, '5:25'),
	(67, '5:30'),
	(68, '5:35'),
	(69, '5:40'),
	(70, '5:45'),
	(71, '5:50'),
	(72, '5:55'),
	(73, '6:00'),
	(74, '6:05'),
	(75, '6:10'),
	(76, '6:15'),
	(77, '6:20'),
	(78, '6:25'),
	(79, '6:30'),
	(80, '6:35'),
	(81, '6:40'),
	(82, '6:45'),
	(83, '6:50'),
	(84, '6:55'),
	(85, '7:00'),
	(86, '7:05'),
	(87, '7:10'),
	(88, '7:15'),
	(89, '7:20'),
	(90, '7:25'),
	(91, '7:30'),
	(92, '7:35'),
	(93, '7:40'),
	(94, '7:45'),
	(95, '7:50'),
	(96, '7:55'),
	(97, '8:00'),
	(98, '8:05'),
	(99, '8:10'),
	(100, '8:15'),
	(101, '8:20'),
	(102, '8:25'),
	(103, '8:30'),
	(104, '8:35'),
	(105, '8:40'),
	(106, '8:45'),
	(107, '8:50'),
	(108, '8:55'),
	(109, '9:00'),
	(110, '9:05'),
	(111, '9:10'),
	(112, '9:15'),
	(113, '9:20'),
	(114, '9:25'),
	(115, '9:30'),
	(116, '9:35'),
	(117, '9:40'),
	(118, '9:45'),
	(119, '9:50'),
	(120, '9:55');

CREATE TABLE IF NOT EXISTS `estimate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `taxratedextra` double DEFAULT NULL,
  `ntaxratedextra` double DEFAULT NULL,
  `tre_descr` varchar(50) DEFAULT NULL,
  `ntre_descr` varchar(50) DEFAULT NULL,
  `patientcustomer_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `taxrate` double NOT NULL,
  `note` varchar(1024) DEFAULT NULL,
  `insurance_id` int(11) DEFAULT NULL,
  `alreadyspent` double DEFAULT NULL,
  `transactiondate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `CUSTOMER` (`patientcustomer_id`),
  CONSTRAINT `FK_estimate_patientcustomer` FOREIGN KEY (`patientcustomer_id`) REFERENCES `patientcustomer` (`patcust_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `estimateitem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `estimate_id` int(11) NOT NULL,
  `intervention_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `INVOICE_ID` (`estimate_id`),
  KEY `INTERVENTION` (`id`),
  CONSTRAINT `FK_estimateitem_estimate` FOREIGN KEY (`estimate_id`) REFERENCES `estimate` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clinicalcard_id` int(11) NOT NULL,
  `tooth_id` int(11) NOT NULL,
  `image` mediumblob,
  `imagepreview` blob,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TOOTH` (`tooth_id`),
  KEY `CLINICALCARD` (`clinicalcard_id`),
  KEY `TIMESTAMP` (`timestamp`),
  CONSTRAINT `FK_image_clinicalcard` FOREIGN KEY (`clinicalcard_id`) REFERENCES `clinicalcard` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `insurance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `insurancecompany_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `sincedate` date NOT NULL,
  `name` varchar(32) NOT NULL,
  `contact` varchar(32) DEFAULT NULL,
  `contactphone` varchar(20) DEFAULT NULL,
  `supervisor` varchar(32) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  `reimbursementplan_id` int(11) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `benefitperiod_start` date DEFAULT NULL,
  `benefitperiod_end` date DEFAULT NULL,
  `deductible` double DEFAULT NULL,
  `maxamount` double DEFAULT NULL,
  `percentage` double DEFAULT NULL,
  `alreadyspent` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_insurance_customer` (`customer_id`),
  CONSTRAINT `FK_insurance_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `insurancecompany` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(48) DEFAULT NULL,
  `group_` varchar(32) DEFAULT NULL,
  `address` varchar(48) DEFAULT NULL,
  `city` varchar(32) DEFAULT NULL,
  `state_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL,
  `zipcode` varchar(10) DEFAULT NULL,
  `email` varchar(48) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `contact` varchar(32) DEFAULT NULL,
  `title` varchar(10) DEFAULT NULL,
  `contactphone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `insurancetype` (
  `id` int(11) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `insurancetype` (`id`, `description`) VALUES
	(1, 'Custom'),
	(2, 'Deductible / Maximum'),
	(3, 'Coinsurance provision'),
	(4, 'Reimbursement plan');

CREATE TABLE IF NOT EXISTS `intervention` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clinicalcard_id` int(11) DEFAULT NULL,
  `treatment_id` int(11) DEFAULT NULL,
  `tooth_id` int(11) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `execution` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `OPERATION` (`treatment_id`),
  KEY `CLINICALCARD` (`clinicalcard_id`),
  KEY `TOOTH_ID` (`tooth_id`),
  CONSTRAINT `FK_intervention_clinicalcard` FOREIGN KEY (`clinicalcard_id`) REFERENCES `clinicalcard` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `invoice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `taxratedextra` double DEFAULT NULL,
  `ntaxratedextra` double DEFAULT NULL,
  `tre_descr` varchar(50) DEFAULT NULL,
  `ntre_descr` varchar(50) DEFAULT NULL,
  `patientcustomer_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `taxrate` double NOT NULL,
  `note` varchar(1024) DEFAULT NULL,
  `number` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `CUSTOMER` (`patientcustomer_id`),
  CONSTRAINT `FK_invoice_patientcustomer` FOREIGN KEY (`patientcustomer_id`) REFERENCES `patientcustomer` (`patcust_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `invoiceitem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `invoice_id` int(11) NOT NULL,
  `intervention_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `INVOICE_ID` (`invoice_id`),
  KEY `INTERVENTION` (`id`),
  CONSTRAINT `FK_invoiceitem_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `invoicenumber` (
  `lastnumber` int(11) NOT NULL,
  `lastdate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `invoicenumber` (`lastnumber`, `lastdate`) VALUES
	(0, '2000-01-01');

CREATE TABLE IF NOT EXISTS `locale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(50) DEFAULT NULL,
  `language` varchar(50) DEFAULT NULL,
  `literal` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

INSERT INTO `locale` (`id`, `country`, `language`, `literal`) VALUES
	(1, 'CN', 'zh', 'SIMPLIFIED CHINESE'),
	(2, 'TW', 'zh', 'TRADITIONAL CHINESE'),
	(3, 'FR', 'fr', 'FRANCE'),
	(4, 'DE', 'de', 'GERMANY'),
	(5, 'IT', 'it', 'ITALY'),
	(6, 'JP', 'ja', 'JAPAN'),
	(7, 'KR', 'ko', 'KOREA'),
	(8, 'GB', 'en', 'UK'),
	(9, 'US', 'en', 'US'),
	(10, 'CA', 'en', 'CANADA'),
	(11, 'CA', 'fr', 'CANADA FRENCH');

CREATE TABLE IF NOT EXISTS `patient` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lastname` varchar(24) NOT NULL,
  `firstname` varchar(24) NOT NULL,
  `sex` smallint(6) NOT NULL,
  `dateOfBirth` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `SURNAME` (`id`,`lastname`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `patientcustomer` (
  `patcust_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `isThePatient` smallint(6) NOT NULL,
  `relationship` varchar(48) DEFAULT NULL,
  PRIMARY KEY (`patcust_id`),
  KEY `FK_patientcustomer_patient` (`patient_id`),
  KEY `FK_patientcustomer_customer` (`customer_id`),
  CONSTRAINT `FK_patientcustomer_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `practitioner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `data` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `quadrant` (
  `id` int(11) NOT NULL,
  `label` varchar(12) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `quadrant` (`id`, `label`) VALUES
	(1, 'I'),
	(2, 'I I'),
	(3, 'I I I'),
	(4, 'IV');

CREATE TABLE IF NOT EXISTS `reimbursement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reimbursementplan_id` int(11) NOT NULL DEFAULT '0',
  `treatment_id` int(11) NOT NULL DEFAULT '0',
  `percentage` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_reimbursement_reimbursementplan` (`reimbursementplan_id`),
  KEY `FK_reimbursement_treatment` (`treatment_id`),
  CONSTRAINT `FK_reimbursement_reimbursementplan` FOREIGN KEY (`reimbursementplan_id`) REFERENCES `reimbursementplan` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_reimbursement_treatment` FOREIGN KEY (`treatment_id`) REFERENCES `treatment` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=189 DEFAULT CHARSET=utf8;

CREATE TABLE `reimbursementplan` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`insurancecompany_id` INT(11) NOT NULL,
	`name` VARCHAR(48) NOT NULL,
	`note` VARCHAR(512) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `uniqueName` (`name`),
	KEY `FK_reimbursementplan_insurancecompany` (`insurancecompany_id`),
	CONSTRAINT `FK_reimbursementplan_insurancecompany` FOREIGN KEY (`insurancecompany_id`) REFERENCES `insurancecompany` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `reservednote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(32) DEFAULT NULL,
  `text` varchar(1024) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `practitioner_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `Description` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

INSERT INTO `role` (`id`, `name`, `Description`) VALUES
	(1, 'Administrators', 'They have write access to Users dialog and Application features'),
	(2, 'Practitioners', 'They have access to dentistry'),
	(3, 'Doctors', 'They have access to all medical data'),
	(4, 'Administrative personnel', 'They have access to financial data and dentistry'),
	(5, 'Schedulers', 'They manage appointments');

CREATE TABLE IF NOT EXISTS `state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(48) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `tooth` (
  `id` int(11) NOT NULL,
  `symbol` varchar(16) DEFAULT NULL,
  `quadrant_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `QUARTER` (`quadrant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tooth` (`id`, `symbol`, `quadrant_id`) VALUES
	(1, '11', 1),
	(2, '12', 1),
	(3, '13', 1),
	(4, '14', 1),
	(5, '15', 1),
	(6, '16', 1),
	(7, '17', 1),
	(8, '18', 1),
	(9, '21', 2),
	(10, '22', 2),
	(11, '23', 2),
	(12, '24', 2),
	(13, '25', 2),
	(14, '26', 2),
	(15, '27', 2),
	(16, '28', 2),
	(17, '31', 3),
	(18, '32', 3),
	(19, '33', 3),
	(20, '34', 3),
	(21, '35', 3),
	(22, '36', 3),
	(23, '37', 3),
	(24, '38', 3),
	(25, '41', 4),
	(26, '42', 4),
	(27, '43', 4),
	(28, '44', 4),
	(29, '45', 4),
	(30, '46', 4),
	(31, '47', 4),
	(32, '48', 4),
	(33, 'mouth', NULL),
	(34, '51', 1),
	(35, '52', 1),
	(36, '53', 1),
	(37, '54', 1),
	(38, '55', 1),
	(39, '61', 2),
	(40, '62', 2),
	(41, '63', 2),
	(42, '64', 2),
	(43, '65', 2),
	(44, '71', 3),
	(45, '72', 3),
	(46, '73', 3),
	(47, '74', 3),
	(48, '75', 3),
	(49, '81', 4),
	(50, '82', 4),
	(51, '83', 4),
	(52, '84', 4),
	(53, '85', 4);

CREATE TABLE IF NOT EXISTS `treatment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `symbol` varchar(12) DEFAULT NULL,
  `descr` varchar(48) DEFAULT NULL,
  `dflt_duration` int(11) unsigned DEFAULT NULL,
  `dflt_price` double DEFAULT NULL,
  `max_price` double DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `userrole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `roleID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

INSERT INTO `userrole` (`id`, `userID`, `roleID`) VALUES
	(1, 1, 1),
	(9, 5, 1),
	(30, 2, 4),
	(31, 2, 3),
	(32, 2, 2),
	(33, 2, 5),
	(34, 3, 2),
	(35, 6, 4),
	(36, 6, 3),
	(37, 6, 2),
	(38, 6, 5),
	(39, 7, 2);

CREATE TABLE IF NOT EXISTS `waitinglist` (
  `ExtID` int(11) DEFAULT NULL,
  `PreviousID` int(11) DEFAULT NULL,
  `NextID` int(11) DEFAULT NULL,
  KEY `FK_waitinglist_appointment` (`ExtID`),
  CONSTRAINT `FK_waitinglist_appointment` FOREIGN KEY (`ExtID`) REFERENCES `appointment` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `clinicalcardcounts` (
	`clinicalcard_id` INT(11) NULL,
	`patient_id` INT(11) NOT NULL,
	`tooth_id` INT(11) NULL,
	`executed` BIGINT(1) NULL,
	`unexecuted` BIGINT(20) NULL,
	`images` BIGINT(20) NULL
) ENGINE=MyISAM;

CREATE TABLE `clinicalcardschema` (
	`id` INT(11) NOT NULL,
	`symbol` VARCHAR(16) NULL COLLATE 'utf8_general_ci',
	`quadrant` VARCHAR(12) NULL COLLATE 'utf8_general_ci'
) ENGINE=MyISAM;

CREATE TABLE `imagebase` (
	`id` INT(11) NOT NULL,
	`clinicalcard_id` INT(11) NOT NULL,
	`tooth_id` INT(11) NOT NULL,
	`image` MEDIUMBLOB NULL,
	`imagepreview` BLOB NULL,
	`timestamp` DATETIME NULL,
	`patient_id` INT(11) NOT NULL,
	`number` SMALLINT(6) NOT NULL
) ENGINE=MyISAM;

CREATE TABLE `interventionbase` (
	`id` INT(11) NOT NULL,
	`clinicalcard_id` INT(11) NULL,
	`treatment_id` INT(11) NULL,
	`tooth_id` INT(11) NULL,
	`timestamp` DATETIME NULL,
	`duration` INT(11) NULL,
	`note` VARCHAR(255) NULL COLLATE 'utf8_general_ci',
	`price` DOUBLE NULL,
	`execution` DATETIME NULL,
	`patient_id` INT(11) NOT NULL,
	`number` SMALLINT(6) NOT NULL
) ENGINE=MyISAM;

CREATE TABLE `interventionchecker` (
	`intervention_id` INT(11) NOT NULL,
	UNIQUE INDEX `InterventionUniqueness` (`intervention_id`)
);

CREATE TABLE `semanticschecker` (
	`day` DATE NOT NULL,
	`part_id` INT(11) NOT NULL,
	`location_id` INT(11) NOT NULL,
	`patient_id` INT(11) NOT NULL,
	`practitioner_id` INT(11) NULL DEFAULT NULL,
	UNIQUE INDEX `TimeSlotCoverage` (`day`, `part_id`, `location_id`),
	UNIQUE INDEX `PatientLocUniqueness` (`day`, `part_id`, `patient_id`),
	UNIQUE INDEX `PractitionerLocUniqueness` (`day`, `part_id`, `practitioner_id`)
);


DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `setcardnumber`(IN `patientId` INT, OUT `newNumber` INT)
BEGIN
declare value int;
SELECT max(number) FROM clinicalcard where patient_id = patientId into value FOR UPDATE ;
SET newNumber =  if (isnull(value), 1, value + 1);
END//
DELIMITER ;


DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `setinvoicenumber`(IN `forcedNumber` INT, IN `checkingDate` DATE, OUT `newNumber` INT)
BEGIN
declare value int;
declare yearChange int;
declare lastYear int;
declare recQty int;
select count(*)  FROM invoicenumber into recQty FOR UPDATE ;
if (recQty=0) then 
	Insert into invoicenumber (lastnumber) values(0);
end if;
SELECT year(lastDate) FROM invoicenumber into lastYear FOR UPDATE ;
set yearChange = checkingDate is not null and year(checkingDate) != lastYear;
SELECT lastnumber FROM invoicenumber into value FOR UPDATE ;
SET newNumber =  if (yearChange, 1, if(forcedNumber >= 0, forcedNumber, value + 1));
UPDATE invoicenumber SET lastnumber = newNumber, lastDate = checkingDate;
END//
DELIMITER ;

DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `setinvoicenumbershared`(IN `forcedNumber` INT, IN `checkingDate` DATE, OUT `newNumber` INT, IN `sharingKeyPrm` VARCHAR(32))
BEGIN
declare value int;
declare yearChange int;
declare lastYear int;
declare recQty int;
select count(*)  FROM invoicenumber where sharingKey = sharingKeyPrm into recQty FOR UPDATE ;
if (recQty=0) then 
	Insert into invoicenumber (lastnumber, sharingKey) values(0, sharingKeyPrm);
end if;
SELECT year(lastDate) FROM invoicenumber where sharingKey = sharingKeyPrm into lastYear FOR UPDATE ;
set yearChange = checkingDate is not null and year(checkingDate) != lastYear;
SELECT lastnumber FROM invoicenumber  where sharingKey = sharingKeyPrm into value FOR UPDATE ;
SET newNumber =  if (yearChange, 1, if(forcedNumber >= 0, forcedNumber, value + 1));
UPDATE invoicenumber SET lastnumber = newNumber, lastDate = checkingDate where sharingKey = sharingKeyPrm ;
END//
DELIMITER ;

DROP TABLE IF EXISTS `clinicalcardcounts`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` VIEW `clinicalcardcounts` AS SELECT clinicalcard_id
	,patient_id
	,tooth_id
	,1 executed
	, NULL unexecuted
	, NULL images
FROM interventionbase
WHERE execution IS NOT NULL UNION ALL
SELECT clinicalcard_id
	,patient_id
	,tooth_id
	, NULL executed
	,1 unexecuted
	, NULL images
FROM interventionbase
WHERE execution IS NULL UNION ALL
SELECT clinicalcard_id
	,patient_id
	,tooth_id
	, NULL executed
	, NULL unexecuted
	,1 images
FROM imagebase
ORDER BY tooth_id ;

DROP TABLE IF EXISTS `clinicalcardschema`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` VIEW `clinicalcardschema` AS SELECT tooth.id, tooth.symbol, quadrant.label AS quadrant
FROM tooth
LEFT JOIN quadrant ON tooth.quadrant_id = quadrant.id ;

DROP TABLE IF EXISTS `imagebase`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` VIEW `imagebase` AS SELECT image.*, clinicalcard.patient_id, clinicalcard.number
 FROM clinicalcard 
    INNER JOIN image 
   ON  clinicalcard.id = image.clinicalcard_id ;

DROP TABLE IF EXISTS `interventionbase`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` VIEW `interventionbase` AS SELECT intervention.*
	,clinicalcard.patient_id
	,clinicalcard.number
FROM clinicalcard
INNER JOIN intervention ON clinicalcard.id = intervention.clinicalcard_id ;
