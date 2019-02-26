ALTER TABLE matches
ADD originating_server_id varchar(255) DEFAULT NULL,
ADD originating_channel_id varchar(255) DEFAULT NULL;
