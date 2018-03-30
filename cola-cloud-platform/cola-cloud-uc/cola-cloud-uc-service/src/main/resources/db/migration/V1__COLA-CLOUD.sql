/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : cola-bak

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-30 18:40:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cola_attachment
-- ----------------------------
DROP TABLE IF EXISTS `cola_attachment`;
CREATE TABLE `cola_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '文件上传时的名称',
  `content_type` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '文件mime类型',
  `attachment_key` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '文件唯一标识',
  `size` bigint(20) DEFAULT NULL COMMENT '文件大小,单位字节',
  `create_by` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '上传人',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  `biz_id` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务类型',
  `biz_code` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '业务标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1 COMMENT='附件表';

-- ----------------------------
-- Records of cola_attachment
-- ----------------------------

-- ----------------------------
-- Table structure for cola_audit
-- ----------------------------
DROP TABLE IF EXISTS `cola_audit`;
CREATE TABLE `cola_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `type` varchar(225) COLLATE utf8_bin DEFAULT NULL COMMENT '日志类型:登录日志;系统日志;',
  `name` varchar(225) COLLATE utf8_bin DEFAULT NULL COMMENT '业务名称',
  `request_url` varchar(225) COLLATE utf8_bin DEFAULT NULL COMMENT '请求地址',
  `request_type` varchar(225) COLLATE utf8_bin DEFAULT NULL COMMENT '请求方法类型',
  `request_class` varchar(225) COLLATE utf8_bin DEFAULT NULL COMMENT '调用类',
  `request_method` varchar(225) COLLATE utf8_bin DEFAULT NULL COMMENT '调用方法',
  `request_ip` varchar(225) COLLATE utf8_bin DEFAULT NULL COMMENT '请求IP',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建用户Id',
  `user_name` varchar(225) COLLATE utf8_bin DEFAULT NULL COMMENT '创建用户名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `time` int(11) DEFAULT NULL COMMENT '消耗时间',
  `success` int(11) DEFAULT NULL COMMENT '是否成功',
  `exception` varchar(2250) COLLATE utf8_bin DEFAULT NULL COMMENT '异常信息',
  `request_parameter` varbinary(2250) DEFAULT NULL COMMENT '调用参数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=978699 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='系统日志表';

-- ----------------------------
-- Records of cola_audit
-- ----------------------------

-- ----------------------------
-- Table structure for cola_dict
-- ----------------------------
DROP TABLE IF EXISTS `cola_dict`;
CREATE TABLE `cola_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '提示',
  `enable` char(1) COLLATE utf8_bin DEFAULT 'Y',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INX_DICT_CODE` (`code`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典表';

-- ----------------------------
-- Records of cola_dict
-- ----------------------------

-- ----------------------------
-- Table structure for cola_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `cola_dict_item`;
CREATE TABLE `cola_dict_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '数据字典项名称',
  `value` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '数据字典值',
  `description` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `enable` char(1) COLLATE utf8_bin DEFAULT 'Y' COMMENT '是否有效',
  `order_no` int(5) DEFAULT NULL COMMENT '排序号',
  `code` varchar(255) COLLATE utf8_bin NOT NULL,
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_by` bigint(10) DEFAULT NULL COMMENT '修改者',
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `modify_ip` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改IP',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典表';

-- ----------------------------
-- Records of cola_dict_item
-- ----------------------------

-- ----------------------------
-- Table structure for cola_sys_authority
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_authority`;
CREATE TABLE `cola_sys_authority` (
  `sys_role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `sys_resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='权限-资源角色关联表';

-- ----------------------------
-- Records of cola_sys_authority
-- ----------------------------
INSERT INTO `cola_sys_authority` VALUES ('4', '1', '68');
INSERT INTO `cola_sys_authority` VALUES ('4', '2', '69');
INSERT INTO `cola_sys_authority` VALUES ('4', '3', '70');
INSERT INTO `cola_sys_authority` VALUES ('4', '318', '71');

-- ----------------------------
-- Table structure for cola_sys_authorize
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_authorize`;
CREATE TABLE `cola_sys_authorize` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `authorize_target` bigint(20) DEFAULT NULL COMMENT '授权对象',
  `authorize_type` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '授权对象类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='授权表（用户与角色对应表）';

-- ----------------------------
-- Records of cola_sys_authorize
-- ----------------------------
INSERT INTO `cola_sys_authorize` VALUES ('5', '4', '127', '0');

-- ----------------------------
-- Table structure for cola_sys_employee
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_employee`;
CREATE TABLE `cola_sys_employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sys_user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `sys_org_id` bigint(50) NOT NULL COMMENT '组织ID',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `sys_post_id` bigint(20) DEFAULT NULL COMMENT '岗位ID',
  `level` varchar(10) DEFAULT NULL COMMENT '级别',
  `idcard` varchar(20) DEFAULT NULL COMMENT '身份证',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '员工状态:1.在职;-1:离职;',
  `picture` varchar(1000) DEFAULT NULL COMMENT '员工照片',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1 COMMENT='员工表（组织与系统用户对应表）';

-- ----------------------------
-- Records of cola_sys_employee
-- ----------------------------
INSERT INTO `cola_sys_employee` VALUES ('24', '127', '1', null, null, '1', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for cola_sys_member
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_member`;
CREATE TABLE `cola_sys_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sys_user_id` bigint(20) NOT NULL COMMENT '用户表',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户表',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `creation_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1 COMMENT='成员表（租户与系统用户对应表）';

-- ----------------------------
-- Records of cola_sys_member
-- ----------------------------

-- ----------------------------
-- Table structure for cola_sys_message
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_message`;
CREATE TABLE `cola_sys_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `sys_user_id` bigint(20) NOT NULL COMMENT '用户id',
  `title` varchar(50) NOT NULL COMMENT '标题',
  `content` varchar(1024) NOT NULL COMMENT '内容',
  `email_status` char(1) NOT NULL DEFAULT '0' COMMENT '是否发送邮件：-1不发送 0 待发送，1已发送',
  `sms_status` char(1) NOT NULL DEFAULT '0' COMMENT '是否发送短信：-1不发送 0 待发送，1已发送',
  `template_code` char(10) DEFAULT NULL COMMENT '短信模板',
  `sms_params` varchar(256) DEFAULT NULL COMMENT '短信模板参数',
  `read_flag` char(1) NOT NULL DEFAULT 'N' COMMENT '读标志：N未读，Y已读',
  `delete_flag` char(1) NOT NULL DEFAULT 'N' COMMENT '删除标志：N未删除，Y已删除',
  `extend` varchar(1024) DEFAULT NULL COMMENT '扩展字段',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='站内信表';

-- ----------------------------
-- Records of cola_sys_message
-- ----------------------------
INSERT INTO `cola_sys_message` VALUES ('1', '127', '测试标题', '测试内容', '0', '0', null, null, 'N', 'N', null, '2018-03-16 10:57:18', '127');
INSERT INTO `cola_sys_message` VALUES ('2', '127', '测试标题', '<h1>html内容</h1>', '1', '0', null, null, 'N', 'N', null, '2018-03-16 15:24:50', '127');

-- ----------------------------
-- Table structure for cola_sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_organization`;
CREATE TABLE `cola_sys_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `description` varchar(2000) DEFAULT NULL COMMENT '描述',
  `pid` bigint(20) DEFAULT NULL COMMENT '父节点',
  `code` varchar(50) NOT NULL COMMENT '部门编号',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `deleted` varchar(1) DEFAULT 'N' COMMENT '删除状态: Y-已删除,N-未删除',
  `status` varchar(5) DEFAULT '20' COMMENT '状态：10：停用，20：启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=229 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='组织架构';

-- ----------------------------
-- Records of cola_sys_organization
-- ----------------------------
INSERT INTO `cola_sys_organization` VALUES ('1', '组织机构', '', null, 'root', null, 'N', '20');

-- ----------------------------
-- Table structure for cola_sys_post
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_post`;
CREATE TABLE `cola_sys_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '岗位名称',
  `code` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '岗位编码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `description` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '所属租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `IDX_ROLE_CODE` (`code`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='系统岗位表';

-- ----------------------------
-- Records of cola_sys_post
-- ----------------------------
INSERT INTO `cola_sys_post` VALUES ('4', '系统管理员', 'admin', '2017-12-11 00:00:00', '2017-12-11 00:00:00', '', null);

-- ----------------------------
-- Table structure for cola_sys_register
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_register`;
CREATE TABLE `cola_sys_register` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `register_sys_user_id` bigint(20) DEFAULT NULL COMMENT '注册用户主键',
  `recommend_sys_user_id` bigint(20) DEFAULT NULL COMMENT '推荐人主键',
  `source_code` varchar(20) DEFAULT NULL COMMENT '注册来源编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='注册信息表';

-- ----------------------------
-- Records of cola_sys_register
-- ----------------------------

-- ----------------------------
-- Table structure for cola_sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_resource`;
CREATE TABLE `cola_sys_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '权限的标识',
  `pid` bigint(20) DEFAULT NULL COMMENT '父id',
  `name` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '权限名称',
  `url` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限请求路径',
  `icon` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '图标',
  `level` int(11) DEFAULT NULL COMMENT '菜单层级',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` char(1) COLLATE utf8_bin DEFAULT 'Y' COMMENT '是否可用：Y-可用；N-不可用',
  `deleted` char(1) COLLATE utf8_bin DEFAULT 'N' COMMENT '是否删除:  Y-已删除; N-未删除',
  `load_type` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '加载类型',
  `type` int(11) DEFAULT NULL,
  `description` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `portal_url` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_RESOURCE_CODE` (`code`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=342 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='资源表';

-- ----------------------------
-- Records of cola_sys_resource
-- ----------------------------
INSERT INTO `cola_sys_resource` VALUES ('279', 'dict', '14', '字典管理', '/dict', '', '0', '5', '2017-12-07 09:49:13', '2017-12-07 09:49:13', 'Y', 'N', null, null, null, 'home');
INSERT INTO `cola_sys_resource` VALUES ('318', 'root', null, 'root', 'root', null, null, '0', '2018-03-15 14:08:16', '2018-03-15 14:08:16', 'Y', 'N', null, '0', null, 'home');
INSERT INTO `cola_sys_resource` VALUES ('319', 'authority', '318', '权限管理', null, 'trophy', null, '0', '2018-03-15 14:29:32', '2018-03-15 14:29:32', 'Y', 'N', null, '1', '权限管理', null);
INSERT INTO `cola_sys_resource` VALUES ('320', 'sysSource', '319', '资源管理', '/authority/sysSource', '', null, '0', '2018-03-15 14:30:28', '2018-03-15 14:30:28', 'Y', 'N', null, '2', '', 'authority/sysSource');
INSERT INTO `cola_sys_resource` VALUES ('321', 'role', '319', '角色管理', '/authority/role', '', null, '0', '2018-03-15 14:57:49', '2018-03-15 14:57:49', 'Y', 'N', null, '2', '', 'authority/role');
INSERT INTO `cola_sys_resource` VALUES ('322', 'home', '318', '首页', '/', 'home', null, '0', '2018-03-15 15:10:15', '2018-03-15 15:10:15', 'Y', 'N', null, '2', '', 'home');
INSERT INTO `cola_sys_resource` VALUES ('326', 'roleCU', '319', '编辑查看新增角色', '/authority/roleCU', null, null, '0', '2018-03-15 15:49:55', '2018-03-15 15:49:55', 'Y', 'N', null, '3', '', 'authority/role/roleCU');
INSERT INTO `cola_sys_resource` VALUES ('327', 'company', '318', '企业组织管理', null, 'fork', null, '0', '2018-03-15 16:42:16', '2018-03-15 16:42:16', 'Y', 'N', null, '1', '企业组织管理', null);
INSERT INTO `cola_sys_resource` VALUES ('328', 'organization', '327', '组织管理', '/company/organization', '', null, '0', '2018-03-15 16:42:55', '2018-03-15 16:42:55', 'Y', 'N', null, '2', '', 'company/organization');
INSERT INTO `cola_sys_resource` VALUES ('329', 'organizationCU', '327', '组织增改查', '/company/organization/organizationCU', null, null, '0', '2018-03-15 16:43:56', '2018-03-15 16:43:56', 'Y', 'N', null, '3', '', 'company/organization/organizationCU');
INSERT INTO `cola_sys_resource` VALUES ('331', 'inEm', '319', '内部员工', '/authority/inEm', '', null, '0', '2018-03-19 11:19:26', '2018-03-19 11:19:26', 'Y', 'N', null, '2', '', 'authority/inEmAccount');
INSERT INTO `cola_sys_resource` VALUES ('332', 'inEmRecordCU', '319', '内部员工档案新增编辑', '/inEmRecord/emRecordCU', null, null, '0', '2018-03-22 14:08:14', '2018-03-22 14:08:14', 'Y', 'N', null, '3', '', 'authority/inEmRecord/emRecordCU');

-- ----------------------------
-- Table structure for cola_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_role`;
CREATE TABLE `cola_sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '租户用户的角色',
  `name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '角色名称',
  `code` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '角色编码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` char(1) COLLATE utf8_bin DEFAULT 'Y' COMMENT '状态: Y-激活状态    N-锁定状态',
  `deleted` char(1) COLLATE utf8_bin DEFAULT 'N' COMMENT '删除状态: Y-已删除,N-未删除',
  `description` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `IDX_ROLE_CODE` (`code`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of cola_sys_role
-- ----------------------------
INSERT INTO `cola_sys_role` VALUES ('4', '系统管理员', 'admin', '2017-12-11 00:00:00', '2017-12-11 00:00:00', 'Y', 'N', '', null);

-- ----------------------------
-- Table structure for cola_sys_social
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_social`;
CREATE TABLE `cola_sys_social` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL COMMENT '接入类型',
  `token` varchar(100) NOT NULL COMMENT '接入TOKEN',
  `sys_user_id` int(20) NOT NULL COMMENT '关联用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COMMENT='社会化接入表';

-- ----------------------------
-- Records of cola_sys_social
-- ----------------------------

-- ----------------------------
-- Table structure for cola_sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_tenant`;
CREATE TABLE `cola_sys_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '租户id',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '租户名称',
  `description` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '简介',
  `logo` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'logo图片地址',
  `code` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '租户编码',
  `address` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '地址',
  `begin_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '租户生效时间',
  `end_time` datetime DEFAULT NULL COMMENT '租户到期时间',
  `check_info` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '审核详情',
  `check_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '审核通过时间',
  `max_user` int(11) DEFAULT NULL COMMENT '最大用户数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int(11) DEFAULT '1' COMMENT '状态: 1-启用, 0-禁用',
  `tel` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '电话',
  `checker_id` bigint(20) DEFAULT NULL COMMENT '审核管理员id',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建管理员id',
  `domain` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '域名',
  `url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '网址',
  `deleted` char(1) COLLATE utf8_bin DEFAULT 'N' COMMENT '删除状态: Y-已删除,N-未删除',
  `administrator` bigint(20) NOT NULL COMMENT '管理员ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='租户表';

-- ----------------------------
-- Records of cola_sys_tenant
-- ----------------------------

-- ----------------------------
-- Table structure for cola_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `cola_sys_user`;
CREATE TABLE `cola_sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '登录账号',
  `name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '名称',
  `password` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '密码',
  `salt` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '密码盐',
  `phone_number` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '电话号码',
  `email` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '电子邮箱',
  `status` char(3) CHARACTER SET utf8 DEFAULT '1' COMMENT '状态: 1-可用，0-禁用，-1-锁定',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` char(1) COLLATE utf8_bin DEFAULT 'N' COMMENT '删除状态: Y-已删除,N-未删除',
  `avatar` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `type` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '用户类型 1-养殖户 2-保险业务员',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `INX_USER_USERNAME` (`username`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

-- ----------------------------
-- Records of cola_sys_user
-- ----------------------------
INSERT INTO `cola_sys_user` VALUES ('127', 'admin', '系统管理员', '$2a$10$VaREiVUrOUBusZrs4rTLm.FvVtO8BLiMbf26zFgK4G8NIbzzV2dCS', '311', '13870931273', '350006811@qq.com', '1', '2017-12-11 06:30:47', '2017-12-11 06:30:47', 'N', 'attachment/image/10f4444833a4c11a456f0ab453482d2a', null);
