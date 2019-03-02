CREATE TABLE `discord_server_table` (
  `server_id` int(11) NOT NULL,
  `announcer_channel_id` varchar(255) NOT NULL,
  `command_escaper` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
