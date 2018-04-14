/*
Navicat MySQL Data Transfer

Source Server         : localhost1
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : cola-cloud

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-04-13 15:45:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cola_oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `cola_oauth_client`;
CREATE TABLE `cola_oauth_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `last_modified_by` bigint(20) DEFAULT NULL,
  `last_modified_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `access_token_validity_seconds` int(11) DEFAULT NULL,
  `client_id` varchar(200) DEFAULT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  `refresh_token_validity_seconds` int(11) DEFAULT NULL,
  `grant_type` varchar(255) NOT NULL COMMENT '授权类型',
  `resource_ids` varchar(255) DEFAULT NULL COMMENT '资源ID',
  `redirect_uri` varchar(2000) DEFAULT NULL COMMENT '跳转URL',
  `enable` bit(1) DEFAULT b'1' COMMENT '是否启用',
  PRIMARY KEY (`id`),
  KEY `cola_oauth_client_client_id_index` (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cola_oauth_client
-- ----------------------------
INSERT INTO `cola_oauth_client` VALUES ('1', '-1', '2018-04-13 13:47:49', '-1', '2018-04-13 13:47:49', null, 'browser', null, null, 'refresh_token,password', null, null, '');
INSERT INTO `cola_oauth_client` VALUES ('2', '-1', '2018-04-13 14:14:12', '-1', '2018-04-13 14:14:12', null, 'server', 'server', null, 'refresh_token,client_credentials', null, null, '');

-- ----------------------------
-- Table structure for cola_oauth_client_scope
-- ----------------------------
DROP TABLE IF EXISTS `cola_oauth_client_scope`;
CREATE TABLE `cola_oauth_client_scope` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `client_id` bigint(20) DEFAULT NULL COMMENT '客户端ID',
  `scope` varchar(50) NOT NULL COMMENT '授权范围',
  `auto_approve` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `cola_oauth_client_scope_client_id_index` (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cola_oauth_client_scope
-- ----------------------------
INSERT INTO `cola_oauth_client_scope` VALUES ('1', '1', 'ui', '\0');
INSERT INTO `cola_oauth_client_scope` VALUES ('2', '2', 'server', '\0');
