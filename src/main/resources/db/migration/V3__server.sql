CREATE TABLE `discord_server` (
  `server_id` BIGINT(20) NOT NULL,
  `announcer_channel_id` BIGINT(20) NOT NULL,
  `command_escaper` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
