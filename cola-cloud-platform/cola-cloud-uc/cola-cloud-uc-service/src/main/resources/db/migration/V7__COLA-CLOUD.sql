ALTER TABLE `cola_sys_resource`
DROP COLUMN `level`,
DROP COLUMN `create_time`,
DROP COLUMN `update_time`,
DROP COLUMN `deleted`,
CHANGE COLUMN `pid` `parent`  bigint(20) NULL DEFAULT NULL COMMENT '父id' AFTER `code`;