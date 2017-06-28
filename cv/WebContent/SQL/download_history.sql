CREATE DATABASE `cv_database` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE cv_database;

CREATE TABLE `download_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(20) NOT NULL,
  `date` datetime NOT NULL,
  `ip_address` varchar(45) NOT NULL,
  `request_header` varchar(700) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
