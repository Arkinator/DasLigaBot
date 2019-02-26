# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.19)
# Database: das_liga
# Generation Time: 2019-02-26 08:46:03 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table matches
# ------------------------------------------------------------

DROP TABLE IF EXISTS `matches`;

CREATE TABLE `matches` (
  `match_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `final_score_playera` int(11) DEFAULT NULL,
  `final_score_playerb` int(11) DEFAULT NULL,
  `game_status` int(11) NOT NULL,
  `playera` varchar(255) DEFAULT NULL,
  `player_areported_score_for_playera` int(11) DEFAULT NULL,
  `player_areported_score_for_playerb` int(11) DEFAULT NULL,
  `playerb` varchar(255) DEFAULT NULL,
  `player_breported_score_for_playera` int(11) DEFAULT NULL,
  `player_breported_score_for_playerb` int(11) DEFAULT NULL,
  `race_playera` int(11) DEFAULT NULL,
  `race_playerb` int(11) DEFAULT NULL,
  PRIMARY KEY (`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table user_table
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_table`;

CREATE TABLE `user_table` (
  `discord_id` varchar(255) NOT NULL,
  `access_level` int(11) NOT NULL,
  `elo` double NOT NULL,
  `match_id` bigint(20) DEFAULT NULL,
  `number_of_interactions` int(11) NOT NULL,
  `race` int(11) DEFAULT NULL,
  PRIMARY KEY (`discord_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
