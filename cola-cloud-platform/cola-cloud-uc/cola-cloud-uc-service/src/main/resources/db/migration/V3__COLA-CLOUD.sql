ALTER TABLE `cola_sys_resource`
CHANGE COLUMN `portal_url` `route`  varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL AFTER `description`,
ADD COLUMN `service_id`  varchar(100) NULL AFTER `description`;

