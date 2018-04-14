ALTER TABLE `cola_sys_user`
MODIFY COLUMN `type`  varchar(5) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户类型' AFTER `avatar`;

UPDATE cola_sys_resource set service_id = 'common-serivce'