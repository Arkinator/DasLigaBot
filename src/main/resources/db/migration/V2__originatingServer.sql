ALTER TABLE matches
ADD originating_server_id BIGINT(20) DEFAULT NULL,
ADD originating_channel_id BIGINT(20) DEFAULT NULL;
