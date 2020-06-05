/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : ben_company

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 03/04/2020 16:45:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司或团队id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司或团队名称',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0为团队 1为公司',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司或团队地址',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公司或团队简介',
  `account_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司或团队管理员id',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_time` datetime(6) NOT NULL COMMENT '修改时间',
  `active` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0为删除 1为未删除',
  PRIMARY KEY (`id`),
  KEY `ix_company_user_id` (`account_id`) USING BTREE COMMENT 'user_id 索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for company_mapping_account
-- ----------------------------
DROP TABLE IF EXISTS `company_mapping_account`;
CREATE TABLE `company_mapping_account` (
  `id` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `company_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司id',
  `account_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`),
  KEY `ix_company_id_mapping_user_id_company_id` (`company_id`) USING BTREE,
  KEY `ix_company_id_mapping_user_id_user_id` (`account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务描述',
  `start` datetime(6) NOT NULL COMMENT '任务开始时间',
  `end` datetime(6) NOT NULL COMMENT '任务结束时间',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务颜色',
  `full` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0为精准时间,1为全天',
  `project_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务所属项目id',
  `publish` tinyint(1) NOT NULL COMMENT '0 未发布 1 发布',
  `active` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0为删除 1为未删除',
  `account_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '任务ower id',
  PRIMARY KEY (`id`),
  KEY `ix_job_project_id` (`project_id`) USING BTREE COMMENT '项目id索引',
  CONSTRAINT `fk_job_project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for job_mapping_account
-- ----------------------------
DROP TABLE IF EXISTS `job_mapping_account`;
CREATE TABLE `job_mapping_account` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `job_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'job id',
  `account_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'account id',
  PRIMARY KEY (`id`),
  KEY `ix_job_id_mapping_account_id_job_id` (`job_id`) USING BTREE COMMENT 'account id mapping job_id 索引',
  KEY `ix_job_Id_mapping_account_id_account_id` (`account_id`) USING BTREE COMMENT 'account id mapping job_id 索引',
  CONSTRAINT `fk_job_id_mapping_account_id_account_id` FOREIGN KEY (`account_id`) REFERENCES `ben_account`.`account` (`id`),
  CONSTRAINT `fk_job_id_mapping_account_id_job_id` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `create_time` datetime NOT NULL COMMENT '创建公告时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '创建者id',
  PRIMARY KEY (`id`),
  KEY `ix_create_by` (`create_by`) USING BTREE,
  KEY `ix_title` (`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for message_mapping_account
-- ----------------------------
DROP TABLE IF EXISTS `message_mapping_account`;
CREATE TABLE `message_mapping_account` (
  `id` varchar(255) NOT NULL,
  `message_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `account_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_message_id` (`message_id`) USING BTREE,
  KEY `ix_account_id` (`account_id`) USING BTREE,
  CONSTRAINT `fk_account_id` FOREIGN KEY (`account_id`) REFERENCES `ben_account`.`account` (`id`),
  CONSTRAINT `fk_message_id` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '项目id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '项目名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目描述',
  `team_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '项目所属团队id',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_time` datetime(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间',
  `active` tinyint(1) NOT NULL COMMENT '0为删除 1为未删除',
  PRIMARY KEY (`id`),
  KEY `ix_team_id` (`team_id`) USING BTREE,
  CONSTRAINT `fk_project_team_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for project_mapping_account
-- ----------------------------
DROP TABLE IF EXISTS `project_mapping_account`;
CREATE TABLE `project_mapping_account` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `project_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'project id',
  `account_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'account id',
  PRIMARY KEY (`id`),
  KEY `ix_project_id_mapping_account_Id_project_id` (`project_id`) USING BTREE COMMENT 'project id mapping account id 索引',
  KEY `ix_project_id_mapping_account_id_account_id` (`account_id`) USING BTREE COMMENT 'project id mapping account id 索引',
  CONSTRAINT `fk_project_id_mapping_account_id_account_Id` FOREIGN KEY (`account_id`) REFERENCES `ben_account`.`account` (`id`),
  CONSTRAINT `fk_project_id_mapping_account_id_project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'Team id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'Team 名字',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Team 描述',
  `company_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公司或团队id',
  `create_time` datetime(6) NOT NULL COMMENT '创建时间',
  `update_time` datetime(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间',
  `active` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0为删除 1为未删除',
  PRIMARY KEY (`id`),
  KEY `ix_team_company_id` (`company_id`) USING BTREE COMMENT 'Team 中 company_id索引',
  CONSTRAINT `fk_team_company_id` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for team_mapping_account
-- ----------------------------
DROP TABLE IF EXISTS `team_mapping_account`;
CREATE TABLE `team_mapping_account` (
  `id` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `team_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'team id',
  `account_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'account id',
  PRIMARY KEY (`id`),
  KEY `ix_team_id_mapping_account_id_team_id` (`team_id`) USING BTREE,
  KEY `ix_team_id_mapping_account_id_account_id` (`account_id`) USING BTREE,
  CONSTRAINT `fk_team_Id_mapping_account_id_team_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

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
