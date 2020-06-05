/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : ben_account

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 18/05/2020 13:48:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''用户id'',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '''' COMMENT ''用户名'',
  `gender` tinyint(1) NOT NULL DEFAULT ''3'' COMMENT ''1 男 2女 3 未知'',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''用户邮箱'',
  `phone_number` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''用户手机'',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '''' COMMENT ''密码'',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''头像地址'',
  `create_time` datetime NOT NULL COMMENT ''创建账户时间'',
  `is_admin` tinyint(1) NOT NULL DEFAULT ''0'' COMMENT ''0 不是admin 1 是admin'',
  `is_active` tinyint(1) NOT NULL DEFAULT ''0'' COMMENT ''0 为未激活状态 1 激活状态'',
  PRIMARY KEY (`id`),
  KEY `ix_account_email` (`email`) USING BTREE COMMENT ''邮箱索引'',
  KEY `ix_account_phone_number` (`phone_number`) USING BTREE COMMENT ''手机索引''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for account_role_mapping
-- ----------------------------
DROP TABLE IF EXISTS `account_role_mapping`;
CREATE TABLE `account_role_mapping` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `account_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_account_id` (`account_id`) USING BTREE,
  KEY `ix_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''权限id'',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''权限名称'',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''描述'',
  `perm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''权限表达式'',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''对于菜单有url的'',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''图标'',
  PRIMARY KEY (`id`),
  KEY `ix_perm` (`perm`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of permission
-- ----------------------------
BEGIN;
INSERT INTO `permission` VALUES (''40289f7a70dd72d10170dd72eb010000'', ''添加团队'', ''用于添加团队'', ''sys:team:add'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170dd749d0170dd74b6100000'', ''编辑团队'', ''用于编辑团队'', ''sys:team:edit'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170dd749d0170dd74b6370001'', ''删除团队'', ''用于删除团队'', ''sys:team:del'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170dd749d0170dd74b6390002'', ''查看团队'', ''用于查看团队'', ''sys:team:view'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df09520170df096ce40000'', ''编辑公司'', ''用于编辑公司信息'', ''sys:company:edit'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df0a140170df0a2dc80000'', ''查看公司'', ''用于查看公司信息'', ''sys:company:view'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df0d9e0170df0db8a40000'', ''添加项目'', ''用于添加项目'', ''sys:project:add'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df0d9e0170df0db8c40001'', ''删除项目'', ''用于删除项目'', ''sys:project:del'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df0d9e0170df0db8c60002'', ''编辑项目'', ''用于编辑项目'', ''sys:project:edit'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df0d9e0170df0db8c90003'', ''查看项目'', ''用于查看项目'', ''sys:project:view'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df0ec70170df0ee1910000'', ''添加任务'', ''用于添加任务'', ''sys:job:add'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df0ec70170df0ee1af0001'', ''删除任务'', ''用于删除任务'', ''sys:job:del'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df0ec70170df0ee1b20002'', ''编辑任务'', ''用于编辑任务'', ''sys:job:edit'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df0ec70170df0ee1b40003'', ''查看任务'', ''用于查看任务'', ''sys:job:view'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df10a10170df10bfd10000'', ''删除账户'', ''用于删除账户'', ''sys:account:del'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df10a10170df10bff70001'', ''编辑账户'', ''用于编辑账户'', ''sys:account:edit'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df10a10170df10bffa0002'', ''查看账户'', ''用于查看账户'', ''sys:account:view'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df121d0170df1238590000'', ''添加雇员'', ''用于添加雇员'', ''sys:employee:add'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df121d0170df12387b0001'', ''删除雇员'', ''用于删除雇员'', ''sys:employee:del'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df121d0170df12387e0002'', ''移除雇员'', ''用于移除雇员'', ''sys:employee:remove'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df121d0170df1238810003'', ''编辑雇员'', ''用于编辑雇员'', ''sys:employee:edit'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df121d0170df1238830004'', ''查看雇员'', ''用于查看雇员'', ''sys:employee:view'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df15fb0170df161abd0000'', ''更换项目'', ''用于更换项目所属团队'', ''sys:project:change'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df20cf0170df20e9fe0000'', ''角色管理'', ''设置菜单中含有角色管理选项'', ''sys:permission:view'', ''/permission'', ''mdi-account-check'');
INSERT INTO `permission` VALUES (''ff80808170df226c0170df22868e0000'', ''编辑角色权限'', ''修改对应角色的权限集'', ''sys:role:permission:edit'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df226c0170df2286ab0001'', ''修改角色'', ''修改角色信息'', ''sys:role:edit'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df226c0170df2286ad0002'', ''删除角色'', ''删除角色'', ''sys:role:del'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df25050170df2520420000'', ''修改密码'', ''用于修改密码'', ''sys:password:view'', ''/password'', ''mdi-lock-question'');
INSERT INTO `permission` VALUES (''ff80808170df25050170df2520610001'', ''主题选择'', ''用于选择主题'', ''sys:theme:view'', ''/theme'', ''mdi-format-color-fill'');
INSERT INTO `permission` VALUES (''ff80808170df27080170df2726170000'', ''推送通知'', ''用于发送通知'', ''sys:message:send'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170df27650170df2782bf0000'', ''推送排班'', ''用于发送排班信息'', ''sys:job:send'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170e2ff870170e2ffa5a00000'', ''查看概览统计图'', ''用于查看概览统计图'', ''sys:overview:chart'', NULL, NULL);
INSERT INTO `permission` VALUES (''ff80808170f33bd10170f33bec8c0000'', ''添加角色'', ''用于添加角色'', ''sys:role:add'', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''角色id'',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''角色名称'',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT ''描述'',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT ''创造的人的id'',
  `active` tinyint(1) NOT NULL COMMENT ''是否删除'',
  PRIMARY KEY (`id`),
  KEY `ix_role_create_by` (`create_by`) USING BTREE COMMENT ''创建者索引'',
  CONSTRAINT `fx_role_account` FOREIGN KEY (`create_by`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES (''40289f7a70dd5ca70170dd5cbfe60000'', ''admin'', ''管理员'', ''666'', 1);
INSERT INTO `role` VALUES (''40289f7a70dd5d910170dd5daa4b0000'', ''雇员'', ''雇员'', ''666'', 1);
INSERT INTO `role` VALUES (''8aaa81727144560f017144600d630000'', ''经理'', ''项目经理'', ''8aaa816d7141c5d3017141ca4bf30000'', 1);
INSERT INTO `role` VALUES (''8aaa8179714a9ecc01714f429bb70004'', ''维护员'', NULL, ''8aaa8179714a9ecc01714f3202d20000'', 1);
INSERT INTO `role` VALUES (''8aaa8179716c3fb201716c4fd9020002'', ''员工'', ''员工'', ''8aaa8179716436700171690a6e330000'', 0);
INSERT INTO `role` VALUES (''8aaa8179716c3fb201716c5849410004'', ''员工'', ''111'', ''8aaa8179716436700171690a6e330000'', 1);
COMMIT;

-- ----------------------------
-- Table structure for role_perm_mapping
-- ----------------------------
DROP TABLE IF EXISTS `role_perm_mapping`;
CREATE TABLE `role_perm_mapping` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `perm_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_role_id` (`role_id`) USING BTREE,
  KEY `ix_perm_id` (`perm_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of role_perm_mapping
-- ----------------------------
BEGIN;
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0ecc0026'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df09520170df096ce40000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0ed10027'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df0a140170df0a2dc80000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0ed60028'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df0d9e0170df0db8a40000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0edc0029'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df0d9e0170df0db8c40001'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0ee1002a'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170dd749d0170dd74b6100000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0ee6002b'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170dd749d0170dd74b6370001'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0eeb002c'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170dd749d0170dd74b6390002'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0ef0002d'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df0d9e0170df0db8c60002'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0ef5002e'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df0d9e0170df0db8c90003'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0efa002f'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df0ec70170df0ee1910000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0eff0030'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df0ec70170df0ee1af0001'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f040031'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df0ec70170df0ee1b20002'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f090032'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df0ec70170df0ee1b40003'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f0e0033'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df10a10170df10bfd10000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f140034'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df10a10170df10bff70001'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f1a0035'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df10a10170df10bffa0002'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f260036'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df121d0170df1238590000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f2b0037'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df121d0170df12387b0001'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f300038'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df121d0170df12387e0002'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f360039'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df121d0170df1238810003'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f3b003a'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df121d0170df1238830004'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f41003b'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df15fb0170df161abd0000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f45003c'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df20cf0170df20e9fe0000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f4b003d'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df226c0170df22868e0000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f50003e'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df226c0170df2286ab0001'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f55003f'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df226c0170df2286ad0002'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f5a0040'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df25050170df2520420000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f5e0041'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df25050170df2520610001'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f630042'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df27080170df2726170000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f680043'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170df27650170df2782bf0000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f6d0044'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170e2ff870170e2ffa5a00000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179714a9ecc01714f4e0f720045'', ''8aaa8179714a9ecc01714f429bb70004'', ''ff80808170f33bd10170f33bec8c0000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179716c3fb201716c5208090003'', ''8aaa8179716c3fb201716c4fd9020002'', ''40289f7a70dd72d10170dd72eb010000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179716c3fb201716c61f6fa0005'', ''8aaa8179716c3fb201716c5849410004'', ''40289f7a70dd72d10170dd72eb010000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179716c3fb201716c61f7000006'', ''8aaa8179716c3fb201716c5849410004'', ''ff80808170dd749d0170dd74b6100000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179716c3fb201716c61f7080007'', ''8aaa8179716c3fb201716c5849410004'', ''ff80808170dd749d0170dd74b6370001'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa8179716c3fb201716c61f70e0008'', ''8aaa8179716c3fb201716c5849410004'', ''ff80808170dd749d0170dd74b6390002'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa827171c5f2cb0171cba0f24f0000'', ''8aaa81727144560f017144600d630000'', ''ff80808170df0a140170df0a2dc80000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa827171c5f2cb0171cba0f25d0001'', ''8aaa81727144560f017144600d630000'', ''ff80808170df0ec70170df0ee1b40003'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa827171c5f2cb0171cba0f2640002'', ''8aaa81727144560f017144600d630000'', ''ff80808170df10a10170df10bffa0002'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa827171c5f2cb0171cba0f26c0003'', ''8aaa81727144560f017144600d630000'', ''ff80808170df25050170df2520610001'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa827171c5f2cb0171cba0f2740004'', ''8aaa81727144560f017144600d630000'', ''ff80808170dd749d0170dd74b6100000'');
INSERT INTO `role_perm_mapping` VALUES (''8aaa827171c5f2cb0171cba0f27b0005'', ''8aaa81727144560f017144600d630000'', ''ff80808170e2ff870170e2ffa5a00000'');
COMMIT;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
