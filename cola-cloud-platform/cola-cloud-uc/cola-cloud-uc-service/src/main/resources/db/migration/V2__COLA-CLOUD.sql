ALTER TABLE `cola_dict`
MODIFY COLUMN `code`  varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '编号' AFTER `id`,
MODIFY COLUMN `name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称' AFTER `code`,
ADD COLUMN `parent`  varchar(40) NULL AFTER `name`,
ADD COLUMN `order_no`  int(5) NULL AFTER `parent`;


DROP TABLE cola_dict_item
