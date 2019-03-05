DROP TABLE IF EXISTS `matches`;

CREATE TABLE `matches` (
  `match_id` int(11) NOT NULL AUTO_INCREMENT,
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

DROP TABLE IF EXISTS `user_table`;

CREATE TABLE `user_table` (
  `discord_id` varchar(255) NOT NULL,
  `access_level` varchar(255) NOT NULL,
  `elo` double NOT NULL,
  `match_id` int(11) DEFAULT NULL,
  `number_of_interactions` int(11) NOT NULL,
  `race` varchar(255) DEFAULT NULL,
  `league` varchar(255) DEFAULT NULL,
  `login_authorization_code` varchar(255) DEFAULT NULL,
  `battle_net_id` varchar(255) DEFAULT NULL,
  `battle_net_token_value` varchar(255) DEFAULT NULL,
  `battle_net_last_update` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`discord_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
