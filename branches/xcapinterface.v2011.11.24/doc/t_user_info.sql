﻿# SQL-Front 5.1  (Build 4.16)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: 10.24.1.244    Database: ucenter
# ------------------------------------------------------
# Server version 5.1.48-log

#
# Source for table t_user_info
#

DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `icon` varchar(255) DEFAULT NULL,
  `passwd` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `user_name` varchar(255) NOT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) NOT NULL,
  `passwd_a1` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `imsi` varchar(255) DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `parent_user_id` bigint(20) DEFAULT NULL,
  `passport` varchar(255) DEFAULT NULL,
  `magic_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

#
# Dumping data for table t_user_info
#

LOCK TABLES `t_user_info` WRITE;
/*!40000 ALTER TABLE `t_user_info` DISABLE KEYS */;
INSERT INTO `t_user_info` VALUES (2,NULL,NULL,0,'13632715400','13632715400','13632715400',NULL,NULL,'imsi012345678912','imei',0,NULL,NULL,'F8sUmrH997S5Vf0dovlHs0Drw1fp72LBQ3KUEcG5iho*','888888');
INSERT INTO `t_user_info` VALUES (3,NULL,NULL,0,'15989380390','15989380390','15989380390',NULL,NULL,NULL,'355266040152835',0,NULL,NULL,'aN0NLbAsvkd5Y3l6H1c1isHaU6gEbRupQBq4Q92hUmM*','888888');
INSERT INTO `t_user_info` VALUES (4,NULL,NULL,0,'13714532530','13714532530','13714532530',NULL,NULL,NULL,'358328030613957',0,NULL,NULL,'OGXzXQYGzK2P8St8ShWHYYhya0cGcbx1CQ6GEPl3kQQ*','888888');
INSERT INTO `t_user_info` VALUES (5,NULL,NULL,0,'13418582940','13418582940','13418582940',NULL,NULL,NULL,'356514040002776',0,NULL,NULL,'UBIGxZGfxPjmt_NG2mGdIxC6rR8Z5VAM--HdmduWvt4*','888888');
INSERT INTO `t_user_info` VALUES (6,NULL,NULL,0,'8613714532530','8613714532530','8613714532530',NULL,NULL,'460004522190061','358701040868318',0,NULL,NULL,'YPRG0MWas7e6lM6sQ9CL4mxbtGk49jB-7iRLx4Icfl5SwAeSqwV0bw**','61A324');
INSERT INTO `t_user_info` VALUES (7,NULL,NULL,0,'8613418582940','8613418582940','8613418582940',NULL,NULL,'460020185258737','356514040002776',0,NULL,NULL,'DG4wonB-wEpgEb5OmGEzjoJqn5-tyhFEvJfoXXQD1sxSwAeSqwV0bw**','fail-default');
INSERT INTO `t_user_info` VALUES (8,NULL,NULL,0,'8613510776274','8613510776274','8613510776274',NULL,NULL,'460000450142696','357194041711284',0,NULL,NULL,'4Dy_oP6sS1rfbig4UsmvHzrkjcBUiCi9mrKlkCbEyv9SwAeSqwV0bw**','E71C9C');
INSERT INTO `t_user_info` VALUES (9,NULL,NULL,0,'8613723493373','8613723493373','8613723493373',NULL,NULL,'460003412219469','355302045814309',0,NULL,NULL,'qhvEdhRSH6O7q3kyjJ4feVuXsZxGAgNJMBbFoWe4p-JSwAeSqwV0bw**','888888');
INSERT INTO `t_user_info` VALUES (10,NULL,NULL,0,'8613823514039','8613823514039','8613823514039',NULL,NULL,'460003513203946','354957032982897',0,NULL,NULL,'8MczM9677R6h4JZ4YIEUNAXNfAm0e66Q7VsDZPPku4ZSwAeSqwV0bw**','fail-default');
INSERT INTO `t_user_info` VALUES (11,NULL,NULL,0,'8613923461151','8613923461151','8613923461151',NULL,NULL,'460002999908610','001068000000006',0,NULL,NULL,'lBCzuDXqQptyckKnNdfMagmZUEjBkoAztj3VbvE90t5SwAeSqwV0bw**','888888');
INSERT INTO `t_user_info` VALUES (12,NULL,NULL,0,'8615989380390','8615989380390','8615989380390',NULL,NULL,'460029200110778','355266040152835',0,NULL,NULL,'_MfVVlhCuyfw-nlZpt6AqncsOzPC3jjYQ3Pw6wyJoAZSwAeSqwV0bw**','130AE8');
INSERT INTO `t_user_info` VALUES (13,NULL,NULL,0,'8613480783139','8613480783139','8613480783139',NULL,NULL,'460020807476645','358701040868318',0,NULL,NULL,'DDcs3x7JwQQwqvOT751dhyp3s2od75lFbuwRL9UfCpJSwAeSqwV0bw**','724CA0');
INSERT INTO `t_user_info` VALUES (14,NULL,NULL,0,'8613480782916','8613480782916','8613480782916',NULL,NULL,'460020807476640','357194041713843',0,NULL,NULL,'zkTe7_bDTQowqvOT751dh5D6cSjcrrd3Px3uCo6889NSwAeSqwV0bw**','3F4ECF');
INSERT INTO `t_user_info` VALUES (15,NULL,NULL,0,'8613480733921','8613480733921','8613480733921',NULL,NULL,'460020807475270','358701040928872',0,NULL,NULL,'q-5cNwHjuSmigz4upDSTAbz9TnI8YYnseENltm9MztlSwAeSqwV0bw**','888888');
INSERT INTO `t_user_info` VALUES (16,NULL,NULL,0,'8615012601304','8615012601304','8615012601304',NULL,NULL,'460023126001304','354957031085809',0,NULL,NULL,'0FyoIDW2UyZAN6hEH8Y7DZNYU3A8BYi_ig8UxOxAkM9SwAeSqwV0bw**','fail-default');
INSERT INTO `t_user_info` VALUES (17,NULL,NULL,0,'8613798584767','8613798584767','8613798584767',NULL,NULL,'460008522954663','358701040928872',0,NULL,NULL,'Xadac3VYtOJM1VWVHK4WIoVTiodz03MKmpRHSX31v4xSwAeSqwV0bw**','888888');
INSERT INTO `t_user_info` VALUES (18,NULL,NULL,0,'8613923406916','8613923406916','8613923406916',NULL,NULL,'460002319367382','357194041713843',0,NULL,NULL,'eE60nQE4So54sNTD59vl-JzFNkMl1lxDqbgptVFd1-FSwAeSqwV0bw**','FD492B');
INSERT INTO `t_user_info` VALUES (19,NULL,NULL,0,'8613828724021','8613828724021','8613828724021',NULL,NULL,'460002805396501','354705040004695',0,NULL,NULL,'pHJlE_jdISlHeopL0RSI_QZ8T7ZzPcTXyKRTRUHHjMNSwAeSqwV0bw**','fail-default');
/*!40000 ALTER TABLE `t_user_info` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
