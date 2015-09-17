-- MySQL dump 10.13  Distrib 5.6.24, for linux-glibc2.5 (x86_64)
--
-- Host: localhost    Database: killbill
-- ------------------------------------------------------
-- Server version	5.5.44-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `_invoice_payment_control_plugin_auto_pay_off`
--

DROP TABLE IF EXISTS `_invoice_payment_control_plugin_auto_pay_off`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_invoice_payment_control_plugin_auto_pay_off` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `attempt_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `payment_external_key` varchar(255) COLLATE utf8_bin NOT NULL,
  `transaction_external_key` varchar(255) COLLATE utf8_bin NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `plugin_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `payment_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `payment_method_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `amount` decimal(15,9) DEFAULT NULL,
  `currency` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `_invoice_payment_control_plugin_auto_pay_off_account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_invoice_payment_control_plugin_auto_pay_off`
--

LOCK TABLES `_invoice_payment_control_plugin_auto_pay_off` WRITE;
/*!40000 ALTER TABLE `_invoice_payment_control_plugin_auto_pay_off` DISABLE KEYS */;
/*!40000 ALTER TABLE `_invoice_payment_control_plugin_auto_pay_off` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_email_history`
--

DROP TABLE IF EXISTS `account_email_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_email_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `email` varchar(128) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `account_email_target_record_id` (`target_record_id`),
  KEY `account_email_history_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_email_history`
--

LOCK TABLES `account_email_history` WRITE;
/*!40000 ALTER TABLE `account_email_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_email_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_emails`
--

DROP TABLE IF EXISTS `account_emails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_emails` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `email` varchar(128) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `account_email_id` (`id`),
  KEY `account_email_account_id_email` (`account_id`,`email`),
  KEY `account_emails_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_emails`
--

LOCK TABLES `account_emails` WRITE;
/*!40000 ALTER TABLE `account_emails` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_emails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_history`
--

DROP TABLE IF EXISTS `account_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `external_key` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `first_name_length` int(11) DEFAULT NULL,
  `currency` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `billing_cycle_day_local` int(11) DEFAULT NULL,
  `payment_method_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `time_zone` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `locale` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `address1` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `address2` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `company_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `city` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `state_or_province` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `country` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `postal_code` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `migrated` tinyint(1) DEFAULT '0',
  `is_notified_for_invoices` tinyint(1) NOT NULL,
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `account_history_target_record_id` (`target_record_id`),
  KEY `account_history_tenant_record_id` (`tenant_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_history`
--

LOCK TABLES `account_history` WRITE;
/*!40000 ALTER TABLE `account_history` DISABLE KEYS */;
INSERT INTO `account_history` VALUES (1,'c59d999c-d500-4f5d-a4fb-9d7c65b7c8d6',1,'jgomez','jgomez@velocitypartners.net','Javier Gomez',NULL,'USD',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'INSERT','admin','2015-09-17 14:46:13','admin','2015-09-17 14:46:13',1),(2,'6db20ebe-e0f8-4221-8775-626e77871ff6',2,'maguero','maguero@velocitypartners.net','Matias Aguero',NULL,'USD',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'INSERT','admin','2015-09-17 14:46:24','admin','2015-09-17 14:46:24',1),(3,'d48423ce-899c-456f-b3d3-f1fe32cffa3c',3,'jjabour','jjabour@velocitypartners.net','Joe Jabour',NULL,'USD',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'INSERT','admin','2015-09-17 14:47:25','admin','2015-09-17 14:47:25',1),(4,'f10c0187-eda3-482d-85a3-b187305b7c93',4,'kbanman','kbanman@velocitypartners.net','Kelly Banman',NULL,'USD',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'INSERT','admin','2015-09-17 14:47:44','admin','2015-09-17 14:47:44',1),(5,'ebaf85e7-7c85-4e27-9d20-4c9e02710f08',5,'bfoote','bfoote@velocitypartners.net','Bethany Foote',NULL,'USD',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'INSERT','admin','2015-09-17 14:48:00','admin','2015-09-17 14:48:00',1),(6,'f43b3518-99e4-4814-810a-20d31f7a3188',6,'mmarshall','mmarshall@velocitypartners.net','Mackenzie Marshall',NULL,'USD',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'INSERT','admin','2015-09-17 14:48:46','admin','2015-09-17 14:48:46',1),(7,'f922a49b-6ee6-4082-b2e9-03c79d231f30',1,'jgomez','jgomez@velocitypartners.net','Javier Gomez',NULL,'USD',0,'fb00388c-379d-4546-a3c1-3227adfe9359',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'UPDATE','admin','2015-09-17 14:51:22','admin','2015-09-17 14:51:22',1),(8,'eb4c0d0d-2bff-4e18-bf9d-87944b9c05c1',2,'maguero','maguero@velocitypartners.net','Matias Aguero',NULL,'USD',0,'f0c2d9ba-ad44-4b30-8b1b-e09cced9babf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'UPDATE','admin','2015-09-17 14:51:31','admin','2015-09-17 14:51:31',1),(9,'b15551cb-737b-4f39-8252-68bad64a4c7e',3,'jjabour','jjabour@velocitypartners.net','Joe Jabour',NULL,'USD',0,'25859391-f185-4bd5-9f99-10bc7154d16e',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'UPDATE','admin','2015-09-17 14:51:39','admin','2015-09-17 14:51:39',1),(10,'0a20322f-bc8e-4cc0-8006-10e1a6668738',4,'kbanman','kbanman@velocitypartners.net','Kelly Banman',NULL,'USD',0,'890ea252-9c59-4998-96e4-74214ffdba95',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'UPDATE','admin','2015-09-17 14:51:47','admin','2015-09-17 14:51:47',1),(11,'fec4e309-a6bb-4a31-9951-26804ccbf983',5,'bfoote','bfoote@velocitypartners.net','Bethany Foote',NULL,'USD',0,'50842bfd-9e1c-4556-aa20-729840f0ac72',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'UPDATE','admin','2015-09-17 14:51:55','admin','2015-09-17 14:51:55',1),(12,'8486dfcd-aece-40bb-82fe-c5b4c5161026',6,'mmarshall','mmarshall@velocitypartners.net','Mackenzie Marshall',NULL,'USD',0,'d114dee6-fb03-454c-8244-e949d57ed1ae',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'UPDATE','admin','2015-09-17 14:52:02','admin','2015-09-17 14:52:02',1);
/*!40000 ALTER TABLE `account_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `external_key` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `first_name_length` int(11) DEFAULT NULL,
  `currency` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `billing_cycle_day_local` int(11) DEFAULT NULL,
  `payment_method_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `time_zone` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `locale` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `address1` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `address2` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `company_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `city` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `state_or_province` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `country` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `postal_code` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `migrated` tinyint(1) DEFAULT '0',
  `is_notified_for_invoices` tinyint(1) NOT NULL,
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `accounts_id` (`id`),
  UNIQUE KEY `accounts_external_key` (`external_key`,`tenant_record_id`),
  KEY `accounts_tenant_record_id` (`tenant_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'45928693-0679-4e38-82e9-b6d8a7375f50','jgomez','jgomez@velocitypartners.net','Javier Gomez',NULL,'USD',0,'fb00388c-379d-4546-a3c1-3227adfe9359',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-09-17 14:46:13','admin','2015-09-17 14:51:22','admin',1),(2,'45abc0d7-df8d-46c3-a621-d19367e43b44','maguero','maguero@velocitypartners.net','Matias Aguero',NULL,'USD',0,'f0c2d9ba-ad44-4b30-8b1b-e09cced9babf',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-09-17 14:46:24','admin','2015-09-17 14:51:31','admin',1),(3,'51c7c760-bdac-44e4-a944-28e8cfba9dd4','jjabour','jjabour@velocitypartners.net','Joe Jabour',NULL,'USD',0,'25859391-f185-4bd5-9f99-10bc7154d16e',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-09-17 14:47:25','admin','2015-09-17 14:51:39','admin',1),(4,'bd78357b-3c3c-4135-ada1-d9925e3ba43c','kbanman','kbanman@velocitypartners.net','Kelly Banman',NULL,'USD',0,'890ea252-9c59-4998-96e4-74214ffdba95',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-09-17 14:47:44','admin','2015-09-17 14:51:47','admin',1),(5,'41dbe4ca-0333-4d33-ac8e-6d0e48237ad5','bfoote','bfoote@velocitypartners.net','Bethany Foote',NULL,'USD',0,'50842bfd-9e1c-4556-aa20-729840f0ac72',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-09-17 14:48:00','admin','2015-09-17 14:51:55','admin',1),(6,'76fdd52b-a99b-4439-ab8c-9eb8aceacc5b','mmarshall','mmarshall@velocitypartners.net','Mackenzie Marshall',NULL,'USD',0,'d114dee6-fb03-454c-8244-e949d57ed1ae',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2015-09-17 14:48:46','admin','2015-09-17 14:52:02','admin',1);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_log` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `table_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `reason_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `comments` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `account_record_id` bigint(20) unsigned DEFAULT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `audit_log_fetch_target_record_id` (`table_name`,`target_record_id`),
  KEY `audit_log_user_name` (`created_by`),
  KEY `audit_log_tenant_account_record_id` (`tenant_record_id`,`account_record_id`),
  KEY `audit_log_via_history` (`target_record_id`,`table_name`,`tenant_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES (1,'855be26c-c3c4-4822-9880-31eed4ff4eb8',1,'TENANT','INSERT','2015-09-17 14:46:08','admin',NULL,NULL,'848883dc-31e5-4e8d-ab1a-3ecfc646a258',NULL,0),(2,'4b27bc69-8eaf-4782-8534-a7935acb2254',1,'ACCOUNT_HISTORY','INSERT','2015-09-17 14:46:13','admin',NULL,NULL,'b58e5cca-cbd9-4528-8627-cabce84be3ee',1,1),(3,'269b97d0-9453-460a-8951-bbbed671706c',2,'ACCOUNT_HISTORY','INSERT','2015-09-17 14:46:24','admin',NULL,NULL,'2dba5a65-b9e7-4f40-819b-f0417df241ec',2,1),(4,'a8440618-a8a5-4e76-96a6-2984e8aacf58',3,'ACCOUNT_HISTORY','INSERT','2015-09-17 14:47:25','admin',NULL,NULL,'351e8be1-17b6-4136-990a-b642e9b0bee3',3,1),(5,'0249d8d7-3d7b-41e8-9d5a-dd33ef69a68d',4,'ACCOUNT_HISTORY','INSERT','2015-09-17 14:47:44','admin',NULL,NULL,'a6c4e68d-d7f2-4923-85f5-d69ca87d403e',4,1),(6,'7dfd694a-8bb2-49e8-b91f-583e9f548702',5,'ACCOUNT_HISTORY','INSERT','2015-09-17 14:48:00','admin',NULL,NULL,'128fe57c-9390-4bde-b172-c17e93f6cd10',5,1),(7,'37e8ee35-500d-459e-8cac-bb144f1583a7',6,'ACCOUNT_HISTORY','INSERT','2015-09-17 14:48:46','admin',NULL,NULL,'221d0a13-6f29-4b90-b62f-147c267541bc',6,1),(8,'9d8c1c80-f8c7-4a26-9972-93de10e8310a',1,'PAYMENT_METHOD_HISTORY','INSERT','2015-09-17 14:51:22','admin',NULL,NULL,'7d41be81-03d1-411f-b167-d514240668a8',1,1),(9,'adff0b68-a39c-4014-bbbe-40af073a7e5c',7,'ACCOUNT_HISTORY','UPDATE','2015-09-17 14:51:22','admin',NULL,NULL,'7d41be81-03d1-411f-b167-d514240668a8',1,1),(10,'cdb3f868-03e5-41aa-bf72-fe8d09f4b485',2,'PAYMENT_METHOD_HISTORY','INSERT','2015-09-17 14:51:31','admin',NULL,NULL,'d9231f04-93be-47fd-9e2f-ec131268f851',2,1),(11,'34ed73a8-0d13-4b9a-9b47-f5f0e4bcac95',8,'ACCOUNT_HISTORY','UPDATE','2015-09-17 14:51:31','admin',NULL,NULL,'d9231f04-93be-47fd-9e2f-ec131268f851',2,1),(12,'4b547b12-a64a-4c7c-8ca8-bbc9f3a8b08f',3,'PAYMENT_METHOD_HISTORY','INSERT','2015-09-17 14:51:39','admin',NULL,NULL,'f4b88584-15b5-44ba-a7b9-3013e0e0cc85',3,1),(13,'0bc31f00-ad21-489b-a64d-862615de5593',9,'ACCOUNT_HISTORY','UPDATE','2015-09-17 14:51:39','admin',NULL,NULL,'f4b88584-15b5-44ba-a7b9-3013e0e0cc85',3,1),(14,'6ba4cdc8-6435-47c1-a4b4-896a952d7dcc',4,'PAYMENT_METHOD_HISTORY','INSERT','2015-09-17 14:51:47','admin',NULL,NULL,'6fece257-87be-48c5-9691-43c19fae943f',4,1),(15,'c41ad359-68c8-4186-b5a1-7eae9034a43f',10,'ACCOUNT_HISTORY','UPDATE','2015-09-17 14:51:47','admin',NULL,NULL,'6fece257-87be-48c5-9691-43c19fae943f',4,1),(16,'342a8369-289a-4733-9b26-137da323d385',5,'PAYMENT_METHOD_HISTORY','INSERT','2015-09-17 14:51:55','admin',NULL,NULL,'302f829d-f6f2-4a00-a6b7-eae9b505b0c1',5,1),(17,'9c9fc8db-5940-45e5-b6be-b7fdf51279eb',11,'ACCOUNT_HISTORY','UPDATE','2015-09-17 14:51:55','admin',NULL,NULL,'302f829d-f6f2-4a00-a6b7-eae9b505b0c1',5,1),(18,'563d73d5-b473-4351-a789-9087e9b63bfd',6,'PAYMENT_METHOD_HISTORY','INSERT','2015-09-17 14:52:02','admin',NULL,NULL,'d6c14490-0942-4bfb-9067-7a3d5cfff07c',6,1),(19,'95495f25-822b-45de-a305-b77f81f954aa',12,'ACCOUNT_HISTORY','UPDATE','2015-09-17 14:52:02','admin',NULL,NULL,'d6c14490-0942-4bfb-9067-7a3d5cfff07c',6,1);
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blocking_states`
--

DROP TABLE IF EXISTS `blocking_states`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blocking_states` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `blockable_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `type` varchar(20) COLLATE utf8_bin NOT NULL,
  `state` varchar(50) COLLATE utf8_bin NOT NULL,
  `service` varchar(20) COLLATE utf8_bin NOT NULL,
  `block_change` tinyint(1) NOT NULL,
  `block_entitlement` tinyint(1) NOT NULL,
  `block_billing` tinyint(1) NOT NULL,
  `effective_date` datetime NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `blocking_states_id` (`blockable_id`),
  KEY `blocking_states_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blocking_states`
--

LOCK TABLES `blocking_states` WRITE;
/*!40000 ALTER TABLE `blocking_states` DISABLE KEYS */;
/*!40000 ALTER TABLE `blocking_states` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bundles`
--

DROP TABLE IF EXISTS `bundles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bundles` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `external_key` varchar(64) COLLATE utf8_bin NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `last_sys_update_date` datetime DEFAULT NULL,
  `original_created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `bundles_id` (`id`),
  KEY `bundles_key` (`external_key`),
  KEY `bundles_account` (`account_id`),
  KEY `bundles_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bundles`
--

LOCK TABLES `bundles` WRITE;
/*!40000 ALTER TABLE `bundles` DISABLE KEYS */;
/*!40000 ALTER TABLE `bundles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus_events`
--

DROP TABLE IF EXISTS `bus_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus_events` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `class_name` varchar(128) COLLATE utf8_bin NOT NULL,
  `event_json` varchar(2048) COLLATE utf8_bin NOT NULL,
  `user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `creating_owner` varchar(50) COLLATE utf8_bin NOT NULL,
  `processing_owner` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `processing_available_date` datetime DEFAULT NULL,
  `processing_state` varchar(14) COLLATE utf8_bin DEFAULT 'AVAILABLE',
  `error_count` int(10) unsigned DEFAULT '0',
  `search_key1` bigint(20) unsigned DEFAULT NULL,
  `search_key2` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `idx_bus_where` (`processing_state`,`processing_owner`,`processing_available_date`),
  KEY `bus_events_tenant_account_record_id` (`search_key2`,`search_key1`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus_events`
--

LOCK TABLES `bus_events` WRITE;
/*!40000 ALTER TABLE `bus_events` DISABLE KEYS */;
/*!40000 ALTER TABLE `bus_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus_events_history`
--

DROP TABLE IF EXISTS `bus_events_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus_events_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `class_name` varchar(128) COLLATE utf8_bin NOT NULL,
  `event_json` varchar(2048) COLLATE utf8_bin NOT NULL,
  `user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `creating_owner` varchar(50) COLLATE utf8_bin NOT NULL,
  `processing_owner` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `processing_available_date` datetime DEFAULT NULL,
  `processing_state` varchar(14) COLLATE utf8_bin DEFAULT 'AVAILABLE',
  `error_count` int(10) unsigned DEFAULT '0',
  `search_key1` bigint(20) unsigned DEFAULT NULL,
  `search_key2` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus_events_history`
--

LOCK TABLES `bus_events_history` WRITE;
/*!40000 ALTER TABLE `bus_events_history` DISABLE KEYS */;
INSERT INTO `bus_events_history` VALUES (1,'org.killbill.billing.account.api.user.DefaultAccountCreationEvent','{\"data\":{\"externalKey\":\"jgomez\",\"name\":\"Javier Gomez\",\"firstNameLength\":null,\"email\":\"jgomez@velocitypartners.net\",\"billCycleDayLocal\":0,\"currency\":\"USD\",\"paymentMethodId\":null,\"timeZone\":null,\"locale\":null,\"address1\":null,\"address2\":null,\"companyName\":null,\"city\":null,\"stateOrProvince\":null,\"postalCode\":null,\"country\":null,\"phone\":null,\"isMigrated\":null,\"isNotifiedForInvoices\":false},\"id\":\"45928693-0679-4e38-82e9-b6d8a7375f50\",\"searchKey1\":1,\"searchKey2\":1,\"userToken\":\"b58e5cca-cbd9-4528-8627-cabce84be3ee\"}','b58e5cca-cbd9-4528-8627-cabce84be3ee','2015-09-17 14:46:13','jgomez','jgomez','2015-09-17 14:46:13','PROCESSED',0,1,1),(2,'org.killbill.billing.account.api.user.DefaultAccountCreationEvent','{\"data\":{\"externalKey\":\"maguero\",\"name\":\"Matias Aguero\",\"firstNameLength\":null,\"email\":\"maguero@velocitypartners.net\",\"billCycleDayLocal\":0,\"currency\":\"USD\",\"paymentMethodId\":null,\"timeZone\":null,\"locale\":null,\"address1\":null,\"address2\":null,\"companyName\":null,\"city\":null,\"stateOrProvince\":null,\"postalCode\":null,\"country\":null,\"phone\":null,\"isMigrated\":null,\"isNotifiedForInvoices\":false},\"id\":\"45abc0d7-df8d-46c3-a621-d19367e43b44\",\"searchKey1\":2,\"searchKey2\":1,\"userToken\":\"2dba5a65-b9e7-4f40-819b-f0417df241ec\"}','2dba5a65-b9e7-4f40-819b-f0417df241ec','2015-09-17 14:46:24','jgomez','jgomez','2015-09-17 14:46:24','PROCESSED',0,2,1),(3,'org.killbill.billing.account.api.user.DefaultAccountCreationEvent','{\"data\":{\"externalKey\":\"jjabour\",\"name\":\"Joe Jabour\",\"firstNameLength\":null,\"email\":\"jjabour@velocitypartners.net\",\"billCycleDayLocal\":0,\"currency\":\"USD\",\"paymentMethodId\":null,\"timeZone\":null,\"locale\":null,\"address1\":null,\"address2\":null,\"companyName\":null,\"city\":null,\"stateOrProvince\":null,\"postalCode\":null,\"country\":null,\"phone\":null,\"isMigrated\":null,\"isNotifiedForInvoices\":false},\"id\":\"51c7c760-bdac-44e4-a944-28e8cfba9dd4\",\"searchKey1\":3,\"searchKey2\":1,\"userToken\":\"351e8be1-17b6-4136-990a-b642e9b0bee3\"}','351e8be1-17b6-4136-990a-b642e9b0bee3','2015-09-17 14:47:25','jgomez','jgomez','2015-09-17 14:47:25','PROCESSED',0,3,1),(4,'org.killbill.billing.account.api.user.DefaultAccountCreationEvent','{\"data\":{\"externalKey\":\"kbanman\",\"name\":\"Kelly Banman\",\"firstNameLength\":null,\"email\":\"kbanman@velocitypartners.net\",\"billCycleDayLocal\":0,\"currency\":\"USD\",\"paymentMethodId\":null,\"timeZone\":null,\"locale\":null,\"address1\":null,\"address2\":null,\"companyName\":null,\"city\":null,\"stateOrProvince\":null,\"postalCode\":null,\"country\":null,\"phone\":null,\"isMigrated\":null,\"isNotifiedForInvoices\":false},\"id\":\"bd78357b-3c3c-4135-ada1-d9925e3ba43c\",\"searchKey1\":4,\"searchKey2\":1,\"userToken\":\"a6c4e68d-d7f2-4923-85f5-d69ca87d403e\"}','a6c4e68d-d7f2-4923-85f5-d69ca87d403e','2015-09-17 14:47:44','jgomez','jgomez','2015-09-17 14:47:44','PROCESSED',0,4,1),(5,'org.killbill.billing.account.api.user.DefaultAccountCreationEvent','{\"data\":{\"externalKey\":\"bfoote\",\"name\":\"Bethany Foote\",\"firstNameLength\":null,\"email\":\"bfoote@velocitypartners.net\",\"billCycleDayLocal\":0,\"currency\":\"USD\",\"paymentMethodId\":null,\"timeZone\":null,\"locale\":null,\"address1\":null,\"address2\":null,\"companyName\":null,\"city\":null,\"stateOrProvince\":null,\"postalCode\":null,\"country\":null,\"phone\":null,\"isMigrated\":null,\"isNotifiedForInvoices\":false},\"id\":\"41dbe4ca-0333-4d33-ac8e-6d0e48237ad5\",\"searchKey1\":5,\"searchKey2\":1,\"userToken\":\"128fe57c-9390-4bde-b172-c17e93f6cd10\"}','128fe57c-9390-4bde-b172-c17e93f6cd10','2015-09-17 14:48:00','jgomez','jgomez','2015-09-17 14:48:00','PROCESSED',0,5,1),(6,'org.killbill.billing.account.api.user.DefaultAccountCreationEvent','{\"data\":{\"externalKey\":\"mmarshall\",\"name\":\"Mackenzie Marshall\",\"firstNameLength\":null,\"email\":\"mmarshall@velocitypartners.net\",\"billCycleDayLocal\":0,\"currency\":\"USD\",\"paymentMethodId\":null,\"timeZone\":null,\"locale\":null,\"address1\":null,\"address2\":null,\"companyName\":null,\"city\":null,\"stateOrProvince\":null,\"postalCode\":null,\"country\":null,\"phone\":null,\"isMigrated\":null,\"isNotifiedForInvoices\":false},\"id\":\"76fdd52b-a99b-4439-ab8c-9eb8aceacc5b\",\"searchKey1\":6,\"searchKey2\":1,\"userToken\":\"221d0a13-6f29-4b90-b62f-147c267541bc\"}','221d0a13-6f29-4b90-b62f-147c267541bc','2015-09-17 14:48:46','jgomez','jgomez','2015-09-17 14:48:46','PROCESSED',0,6,1),(7,'org.killbill.billing.account.api.user.DefaultAccountChangeEvent','{\"accountId\":\"45928693-0679-4e38-82e9-b6d8a7375f50\",\"searchKey1\":1,\"searchKey2\":1,\"userToken\":\"7d41be81-03d1-411f-b167-d514240668a8\",\"changedFields\":[{\"fieldName\":\"paymentMethodId\",\"oldValue\":null,\"newValue\":\"fb00388c-379d-4546-a3c1-3227adfe9359\",\"changeDate\":\"2015-09-17T14:51:22.730Z\"}]}','7d41be81-03d1-411f-b167-d514240668a8','2015-09-17 14:51:22','jgomez','jgomez','2015-09-17 14:51:22','PROCESSED',0,1,1),(8,'org.killbill.billing.account.api.user.DefaultAccountChangeEvent','{\"accountId\":\"45abc0d7-df8d-46c3-a621-d19367e43b44\",\"searchKey1\":2,\"searchKey2\":1,\"userToken\":\"d9231f04-93be-47fd-9e2f-ec131268f851\",\"changedFields\":[{\"fieldName\":\"paymentMethodId\",\"oldValue\":null,\"newValue\":\"f0c2d9ba-ad44-4b30-8b1b-e09cced9babf\",\"changeDate\":\"2015-09-17T14:51:31.476Z\"}]}','d9231f04-93be-47fd-9e2f-ec131268f851','2015-09-17 14:51:31','jgomez','jgomez','2015-09-17 14:51:31','PROCESSED',0,2,1),(9,'org.killbill.billing.account.api.user.DefaultAccountChangeEvent','{\"accountId\":\"51c7c760-bdac-44e4-a944-28e8cfba9dd4\",\"searchKey1\":3,\"searchKey2\":1,\"userToken\":\"f4b88584-15b5-44ba-a7b9-3013e0e0cc85\",\"changedFields\":[{\"fieldName\":\"paymentMethodId\",\"oldValue\":null,\"newValue\":\"25859391-f185-4bd5-9f99-10bc7154d16e\",\"changeDate\":\"2015-09-17T14:51:39.291Z\"}]}','f4b88584-15b5-44ba-a7b9-3013e0e0cc85','2015-09-17 14:51:39','jgomez','jgomez','2015-09-17 14:51:39','PROCESSED',0,3,1),(10,'org.killbill.billing.account.api.user.DefaultAccountChangeEvent','{\"accountId\":\"bd78357b-3c3c-4135-ada1-d9925e3ba43c\",\"searchKey1\":4,\"searchKey2\":1,\"userToken\":\"6fece257-87be-48c5-9691-43c19fae943f\",\"changedFields\":[{\"fieldName\":\"paymentMethodId\",\"oldValue\":null,\"newValue\":\"890ea252-9c59-4998-96e4-74214ffdba95\",\"changeDate\":\"2015-09-17T14:51:47.685Z\"}]}','6fece257-87be-48c5-9691-43c19fae943f','2015-09-17 14:51:47','jgomez','jgomez','2015-09-17 14:51:47','PROCESSED',0,4,1),(11,'org.killbill.billing.account.api.user.DefaultAccountChangeEvent','{\"accountId\":\"41dbe4ca-0333-4d33-ac8e-6d0e48237ad5\",\"searchKey1\":5,\"searchKey2\":1,\"userToken\":\"302f829d-f6f2-4a00-a6b7-eae9b505b0c1\",\"changedFields\":[{\"fieldName\":\"paymentMethodId\",\"oldValue\":null,\"newValue\":\"50842bfd-9e1c-4556-aa20-729840f0ac72\",\"changeDate\":\"2015-09-17T14:51:55.229Z\"}]}','302f829d-f6f2-4a00-a6b7-eae9b505b0c1','2015-09-17 14:51:55','jgomez','jgomez','2015-09-17 14:51:55','PROCESSED',0,5,1),(12,'org.killbill.billing.account.api.user.DefaultAccountChangeEvent','{\"accountId\":\"76fdd52b-a99b-4439-ab8c-9eb8aceacc5b\",\"searchKey1\":6,\"searchKey2\":1,\"userToken\":\"d6c14490-0942-4bfb-9067-7a3d5cfff07c\",\"changedFields\":[{\"fieldName\":\"paymentMethodId\",\"oldValue\":null,\"newValue\":\"d114dee6-fb03-454c-8244-e949d57ed1ae\",\"changeDate\":\"2015-09-17T14:52:02.442Z\"}]}','d6c14490-0942-4bfb-9067-7a3d5cfff07c','2015-09-17 14:52:02','jgomez','jgomez','2015-09-17 14:52:02','PROCESSED',0,6,1);
/*!40000 ALTER TABLE `bus_events_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus_ext_events`
--

DROP TABLE IF EXISTS `bus_ext_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus_ext_events` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `class_name` varchar(128) COLLATE utf8_bin NOT NULL,
  `event_json` varchar(2048) COLLATE utf8_bin NOT NULL,
  `user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `creating_owner` varchar(50) COLLATE utf8_bin NOT NULL,
  `processing_owner` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `processing_available_date` datetime DEFAULT NULL,
  `processing_state` varchar(14) COLLATE utf8_bin DEFAULT 'AVAILABLE',
  `error_count` int(10) unsigned DEFAULT '0',
  `search_key1` bigint(20) unsigned DEFAULT NULL,
  `search_key2` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `idx_bus_ext_where` (`processing_state`,`processing_owner`,`processing_available_date`),
  KEY `bus_ext_events_tenant_account_record_id` (`search_key2`,`search_key1`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus_ext_events`
--

LOCK TABLES `bus_ext_events` WRITE;
/*!40000 ALTER TABLE `bus_ext_events` DISABLE KEYS */;
/*!40000 ALTER TABLE `bus_ext_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus_ext_events_history`
--

DROP TABLE IF EXISTS `bus_ext_events_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus_ext_events_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `class_name` varchar(128) COLLATE utf8_bin NOT NULL,
  `event_json` varchar(2048) COLLATE utf8_bin NOT NULL,
  `user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `creating_owner` varchar(50) COLLATE utf8_bin NOT NULL,
  `processing_owner` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `processing_available_date` datetime DEFAULT NULL,
  `processing_state` varchar(14) COLLATE utf8_bin DEFAULT 'AVAILABLE',
  `error_count` int(10) unsigned DEFAULT '0',
  `search_key1` bigint(20) unsigned DEFAULT NULL,
  `search_key2` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus_ext_events_history`
--

LOCK TABLES `bus_ext_events_history` WRITE;
/*!40000 ALTER TABLE `bus_ext_events_history` DISABLE KEYS */;
INSERT INTO `bus_ext_events_history` VALUES (1,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"45928693-0679-4e38-82e9-b6d8a7375f50\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CREATION\",\"accountId\":\"45928693-0679-4e38-82e9-b6d8a7375f50\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','b58e5cca-cbd9-4528-8627-cabce84be3ee','2015-09-17 14:46:13','jgomez','jgomez','2015-09-17 14:46:13','PROCESSED',0,1,1),(2,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"45abc0d7-df8d-46c3-a621-d19367e43b44\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CREATION\",\"accountId\":\"45abc0d7-df8d-46c3-a621-d19367e43b44\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','2dba5a65-b9e7-4f40-819b-f0417df241ec','2015-09-17 14:46:24','jgomez','jgomez','2015-09-17 14:46:24','PROCESSED',0,2,1),(3,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"51c7c760-bdac-44e4-a944-28e8cfba9dd4\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CREATION\",\"accountId\":\"51c7c760-bdac-44e4-a944-28e8cfba9dd4\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','351e8be1-17b6-4136-990a-b642e9b0bee3','2015-09-17 14:47:25','jgomez','jgomez','2015-09-17 14:47:25','PROCESSED',0,3,1),(4,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"bd78357b-3c3c-4135-ada1-d9925e3ba43c\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CREATION\",\"accountId\":\"bd78357b-3c3c-4135-ada1-d9925e3ba43c\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','a6c4e68d-d7f2-4923-85f5-d69ca87d403e','2015-09-17 14:47:44','jgomez','jgomez','2015-09-17 14:47:44','PROCESSED',0,4,1),(5,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"41dbe4ca-0333-4d33-ac8e-6d0e48237ad5\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CREATION\",\"accountId\":\"41dbe4ca-0333-4d33-ac8e-6d0e48237ad5\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','128fe57c-9390-4bde-b172-c17e93f6cd10','2015-09-17 14:48:00','jgomez','jgomez','2015-09-17 14:48:00','PROCESSED',0,5,1),(6,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"76fdd52b-a99b-4439-ab8c-9eb8aceacc5b\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CREATION\",\"accountId\":\"76fdd52b-a99b-4439-ab8c-9eb8aceacc5b\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','221d0a13-6f29-4b90-b62f-147c267541bc','2015-09-17 14:48:46','jgomez','jgomez','2015-09-17 14:48:46','PROCESSED',0,6,1),(7,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"45928693-0679-4e38-82e9-b6d8a7375f50\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CHANGE\",\"accountId\":\"45928693-0679-4e38-82e9-b6d8a7375f50\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','7d41be81-03d1-411f-b167-d514240668a8','2015-09-17 14:51:22','jgomez','jgomez','2015-09-17 14:51:22','PROCESSED',0,1,1),(8,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"45abc0d7-df8d-46c3-a621-d19367e43b44\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CHANGE\",\"accountId\":\"45abc0d7-df8d-46c3-a621-d19367e43b44\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','d9231f04-93be-47fd-9e2f-ec131268f851','2015-09-17 14:51:31','jgomez','jgomez','2015-09-17 14:51:31','PROCESSED',0,2,1),(9,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"51c7c760-bdac-44e4-a944-28e8cfba9dd4\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CHANGE\",\"accountId\":\"51c7c760-bdac-44e4-a944-28e8cfba9dd4\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','f4b88584-15b5-44ba-a7b9-3013e0e0cc85','2015-09-17 14:51:39','jgomez','jgomez','2015-09-17 14:51:39','PROCESSED',0,3,1),(10,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"bd78357b-3c3c-4135-ada1-d9925e3ba43c\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CHANGE\",\"accountId\":\"bd78357b-3c3c-4135-ada1-d9925e3ba43c\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','6fece257-87be-48c5-9691-43c19fae943f','2015-09-17 14:51:47','jgomez','jgomez','2015-09-17 14:51:47','PROCESSED',0,4,1),(11,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"41dbe4ca-0333-4d33-ac8e-6d0e48237ad5\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CHANGE\",\"accountId\":\"41dbe4ca-0333-4d33-ac8e-6d0e48237ad5\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','302f829d-f6f2-4a00-a6b7-eae9b505b0c1','2015-09-17 14:51:55','jgomez','jgomez','2015-09-17 14:51:55','PROCESSED',0,5,1),(12,'org.killbill.billing.beatrix.extbus.DefaultBusExternalEvent','{\"objectId\":\"76fdd52b-a99b-4439-ab8c-9eb8aceacc5b\",\"objectType\":\"ACCOUNT\",\"eventType\":\"ACCOUNT_CHANGE\",\"accountId\":\"76fdd52b-a99b-4439-ab8c-9eb8aceacc5b\",\"tenantId\":\"7fbe21d4-f1f9-450a-963d-4490dbab4666\",\"metaData\":null}','d6c14490-0942-4bfb-9067-7a3d5cfff07c','2015-09-17 14:52:02','jgomez','jgomez','2015-09-17 14:52:02','PROCESSED',0,6,1);
/*!40000 ALTER TABLE `bus_ext_events_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalog_override_phase_definition`
--

DROP TABLE IF EXISTS `catalog_override_phase_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalog_override_phase_definition` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `parent_phase_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `currency` varchar(3) COLLATE utf8_bin NOT NULL,
  `fixed_price` decimal(15,9) DEFAULT NULL,
  `recurring_price` decimal(15,9) DEFAULT NULL,
  `effective_date` datetime NOT NULL,
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `catalog_override_phase_definition_idx` (`tenant_record_id`,`parent_phase_name`,`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalog_override_phase_definition`
--

LOCK TABLES `catalog_override_phase_definition` WRITE;
/*!40000 ALTER TABLE `catalog_override_phase_definition` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalog_override_phase_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalog_override_plan_definition`
--

DROP TABLE IF EXISTS `catalog_override_plan_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalog_override_plan_definition` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `parent_plan_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `effective_date` datetime NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `catalog_override_plan_definition_tenant_record_id` (`tenant_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalog_override_plan_definition`
--

LOCK TABLES `catalog_override_plan_definition` WRITE;
/*!40000 ALTER TABLE `catalog_override_plan_definition` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalog_override_plan_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalog_override_plan_phase`
--

DROP TABLE IF EXISTS `catalog_override_plan_phase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalog_override_plan_phase` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `phase_number` smallint(5) unsigned NOT NULL,
  `phase_def_record_id` bigint(20) unsigned NOT NULL,
  `target_plan_def_record_id` bigint(20) unsigned NOT NULL,
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `catalog_override_plan_phase_idx` (`tenant_record_id`,`phase_number`,`phase_def_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalog_override_plan_phase`
--

LOCK TABLES `catalog_override_plan_phase` WRITE;
/*!40000 ALTER TABLE `catalog_override_plan_phase` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalog_override_plan_phase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupons`
--

DROP TABLE IF EXISTS `coupons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coupons` (
  `record_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `coupon_code` varchar(36) COLLATE utf8_bin NOT NULL,
  `coupon_name` varchar(100) COLLATE utf8_bin NOT NULL,
  `discount_type` varchar(15) COLLATE utf8_bin NOT NULL,
  `percentage_discount` float NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `duration` varchar(15) COLLATE utf8_bin NOT NULL,
  `number_of_invoices` int(3) DEFAULT '0',
  `start_date` date NOT NULL,
  `expiration_date` date DEFAULT NULL,
  `kb_tenant_id` char(36) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `coupons_code` (`coupon_code`),
  KEY `coupons_tenant_record_id` (`kb_tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupons`
--

LOCK TABLES `coupons` WRITE;
/*!40000 ALTER TABLE `coupons` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupons_applied`
--

DROP TABLE IF EXISTS `coupons_applied`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coupons_applied` (
  `record_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `coupon_code` varchar(36) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_date` date NOT NULL,
  `number_of_invoices` int(3) DEFAULT '0',
  `max_invoices` int(3) NOT NULL DEFAULT '0',
  `notes` varchar(250) COLLATE utf8_bin DEFAULT NULL,
  `kb_subscription_id` char(36) COLLATE utf8_bin NOT NULL,
  `kb_account_id` char(36) COLLATE utf8_bin NOT NULL,
  `kb_tenant_id` char(36) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `coupons_applied_unique` (`coupon_code`,`kb_subscription_id`),
  KEY `coupons_applied_code` (`coupon_code`),
  KEY `coupons_applied_account` (`kb_account_id`),
  KEY `coupons_tenant_record_id` (`kb_tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupons_applied`
--

LOCK TABLES `coupons_applied` WRITE;
/*!40000 ALTER TABLE `coupons_applied` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupons_applied` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupons_products`
--

DROP TABLE IF EXISTS `coupons_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coupons_products` (
  `record_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `coupon_code` varchar(36) COLLATE utf8_bin NOT NULL,
  `product_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `kb_tenant_id` char(36) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`record_id`),
  KEY `coupons_products_code` (`coupon_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupons_products`
--

LOCK TABLES `coupons_products` WRITE;
/*!40000 ALTER TABLE `coupons_products` DISABLE KEYS */;
/*!40000 ALTER TABLE `coupons_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_field_history`
--

DROP TABLE IF EXISTS `custom_field_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `object_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `object_type` varchar(30) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `field_name` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `field_value` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `custom_field_history_target_record_id` (`target_record_id`),
  KEY `custom_field_history_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_field_history`
--

LOCK TABLES `custom_field_history` WRITE;
/*!40000 ALTER TABLE `custom_field_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_field_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_fields`
--

DROP TABLE IF EXISTS `custom_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_fields` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `object_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `object_type` varchar(30) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `field_name` varchar(30) COLLATE utf8_bin NOT NULL,
  `field_value` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `custom_fields_id` (`id`),
  KEY `custom_fields_object_id_object_type` (`object_id`,`object_type`),
  KEY `custom_fields_tenant_account_record_id` (`tenant_record_id`,`account_record_id`),
  KEY `custom_field_history_object_id_object_type` (`object_id`,`object_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_fields`
--

LOCK TABLES `custom_fields` WRITE;
/*!40000 ALTER TABLE `custom_fields` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_items`
--

DROP TABLE IF EXISTS `invoice_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_items` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `type` varchar(24) COLLATE utf8_bin NOT NULL,
  `invoice_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `bundle_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `subscription_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `plan_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `phase_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `usage_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date DEFAULT NULL,
  `amount` decimal(15,9) NOT NULL,
  `rate` decimal(15,9) DEFAULT NULL,
  `currency` varchar(3) COLLATE utf8_bin NOT NULL,
  `linked_item_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `invoice_items_id` (`id`),
  KEY `invoice_items_subscription_id` (`subscription_id`),
  KEY `invoice_items_invoice_id` (`invoice_id`),
  KEY `invoice_items_account_id` (`account_id`),
  KEY `invoice_items_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_items`
--

LOCK TABLES `invoice_items` WRITE;
/*!40000 ALTER TABLE `invoice_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_payments`
--

DROP TABLE IF EXISTS `invoice_payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_payments` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `type` varchar(24) COLLATE utf8_bin NOT NULL,
  `invoice_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `payment_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `payment_date` datetime NOT NULL,
  `amount` decimal(15,9) NOT NULL,
  `currency` varchar(3) COLLATE utf8_bin NOT NULL,
  `processed_currency` varchar(3) COLLATE utf8_bin NOT NULL,
  `payment_cookie_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `linked_invoice_payment_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `invoice_payments_id` (`id`),
  UNIQUE KEY `idx_invoice_payments` (`payment_id`,`type`),
  KEY `invoice_payments_invoice_id` (`invoice_id`),
  KEY `invoice_payments_reversals` (`linked_invoice_payment_id`),
  KEY `invoice_payments_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_payments`
--

LOCK TABLES `invoice_payments` WRITE;
/*!40000 ALTER TABLE `invoice_payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoices` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `invoice_date` date NOT NULL,
  `target_date` date NOT NULL,
  `currency` varchar(3) COLLATE utf8_bin NOT NULL,
  `migrated` tinyint(1) NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `invoices_id` (`id`),
  KEY `invoices_account_target` (`account_id`,`target_date`),
  KEY `invoices_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `class_name` varchar(256) COLLATE utf8_bin NOT NULL,
  `event_json` varchar(2048) COLLATE utf8_bin NOT NULL,
  `user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `creating_owner` varchar(50) COLLATE utf8_bin NOT NULL,
  `processing_owner` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `processing_available_date` datetime DEFAULT NULL,
  `processing_state` varchar(14) COLLATE utf8_bin DEFAULT 'AVAILABLE',
  `error_count` int(10) unsigned DEFAULT '0',
  `search_key1` bigint(20) unsigned NOT NULL,
  `search_key2` bigint(20) unsigned NOT NULL DEFAULT '0',
  `queue_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `effective_date` datetime NOT NULL,
  `future_user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `idx_comp_where` (`effective_date`,`processing_state`,`processing_owner`,`processing_available_date`),
  KEY `idx_update` (`processing_state`,`processing_owner`,`processing_available_date`),
  KEY `idx_get_ready` (`effective_date`,`created_date`),
  KEY `notifications_tenant_account_record_id` (`search_key2`,`search_key1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications_history`
--

DROP TABLE IF EXISTS `notifications_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `class_name` varchar(256) COLLATE utf8_bin NOT NULL,
  `event_json` varchar(2048) COLLATE utf8_bin NOT NULL,
  `user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `creating_owner` varchar(50) COLLATE utf8_bin NOT NULL,
  `processing_owner` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `processing_available_date` datetime DEFAULT NULL,
  `processing_state` varchar(14) COLLATE utf8_bin DEFAULT 'AVAILABLE',
  `error_count` int(10) unsigned DEFAULT '0',
  `search_key1` bigint(20) unsigned NOT NULL,
  `search_key2` bigint(20) unsigned NOT NULL DEFAULT '0',
  `queue_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `effective_date` datetime NOT NULL,
  `future_user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications_history`
--

LOCK TABLES `notifications_history` WRITE;
/*!40000 ALTER TABLE `notifications_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_attempt_history`
--

DROP TABLE IF EXISTS `payment_attempt_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_attempt_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `payment_method_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `payment_external_key` varchar(128) COLLATE utf8_bin NOT NULL,
  `transaction_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `transaction_external_key` varchar(128) COLLATE utf8_bin NOT NULL,
  `transaction_type` varchar(32) COLLATE utf8_bin NOT NULL,
  `state_name` varchar(32) COLLATE utf8_bin NOT NULL,
  `amount` decimal(15,9) DEFAULT NULL,
  `currency` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `plugin_name` varchar(1024) COLLATE utf8_bin NOT NULL,
  `plugin_properties` mediumblob,
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `payment_attempt_history_target_record_id` (`target_record_id`),
  KEY `payment_attempt_history_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_attempt_history`
--

LOCK TABLES `payment_attempt_history` WRITE;
/*!40000 ALTER TABLE `payment_attempt_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_attempt_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_attempts`
--

DROP TABLE IF EXISTS `payment_attempts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_attempts` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `payment_method_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `payment_external_key` varchar(128) COLLATE utf8_bin NOT NULL,
  `transaction_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `transaction_external_key` varchar(128) COLLATE utf8_bin NOT NULL,
  `transaction_type` varchar(32) COLLATE utf8_bin NOT NULL,
  `state_name` varchar(32) COLLATE utf8_bin NOT NULL,
  `amount` decimal(15,9) DEFAULT NULL,
  `currency` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `plugin_name` varchar(1024) COLLATE utf8_bin NOT NULL,
  `plugin_properties` mediumblob,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `payment_attempts_id` (`id`),
  KEY `payment_attempts_payment` (`transaction_id`),
  KEY `payment_attempts_payment_key` (`payment_external_key`),
  KEY `payment_attempts_payment_state` (`state_name`),
  KEY `payment_attempts_payment_transaction_key` (`transaction_external_key`),
  KEY `payment_attempts_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_attempts`
--

LOCK TABLES `payment_attempts` WRITE;
/*!40000 ALTER TABLE `payment_attempts` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_attempts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_history`
--

DROP TABLE IF EXISTS `payment_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `payment_method_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `external_key` varchar(255) COLLATE utf8_bin NOT NULL,
  `state_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `last_success_state_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `payment_history_target_record_id` (`target_record_id`),
  KEY `payment_history_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_history`
--

LOCK TABLES `payment_history` WRITE;
/*!40000 ALTER TABLE `payment_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_method_history`
--

DROP TABLE IF EXISTS `payment_method_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_method_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `external_key` varchar(255) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `plugin_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `payment_method_history_target_record_id` (`target_record_id`),
  KEY `payment_method_history_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_method_history`
--

LOCK TABLES `payment_method_history` WRITE;
/*!40000 ALTER TABLE `payment_method_history` DISABLE KEYS */;
INSERT INTO `payment_method_history` VALUES (1,'326482d5-371f-4b27-a48b-18f669c189d0','fb00388c-379d-4546-a3c1-3227adfe9359',1,'45928693-0679-4e38-82e9-b6d8a7375f50','__EXTERNAL_PAYMENT__',1,'INSERT','admin','2015-09-17 14:51:22','admin','2015-09-17 14:51:22',1,1),(2,'753c4030-92f7-4278-8758-3d588935b856','f0c2d9ba-ad44-4b30-8b1b-e09cced9babf',2,'45abc0d7-df8d-46c3-a621-d19367e43b44','__EXTERNAL_PAYMENT__',1,'INSERT','admin','2015-09-17 14:51:31','admin','2015-09-17 14:51:31',2,1),(3,'0173bea7-79b4-4848-beae-1db3d799e500','25859391-f185-4bd5-9f99-10bc7154d16e',3,'51c7c760-bdac-44e4-a944-28e8cfba9dd4','__EXTERNAL_PAYMENT__',1,'INSERT','admin','2015-09-17 14:51:39','admin','2015-09-17 14:51:39',3,1),(4,'75d2f69b-7a00-4cff-8161-7c85e8c1058c','890ea252-9c59-4998-96e4-74214ffdba95',4,'bd78357b-3c3c-4135-ada1-d9925e3ba43c','__EXTERNAL_PAYMENT__',1,'INSERT','admin','2015-09-17 14:51:47','admin','2015-09-17 14:51:47',4,1),(5,'b4e9a543-b0f1-4b83-a22f-e7ac93813470','50842bfd-9e1c-4556-aa20-729840f0ac72',5,'41dbe4ca-0333-4d33-ac8e-6d0e48237ad5','__EXTERNAL_PAYMENT__',1,'INSERT','admin','2015-09-17 14:51:55','admin','2015-09-17 14:51:55',5,1),(6,'99f60539-baba-4169-8b84-857959a7c85a','d114dee6-fb03-454c-8244-e949d57ed1ae',6,'76fdd52b-a99b-4439-ab8c-9eb8aceacc5b','__EXTERNAL_PAYMENT__',1,'INSERT','admin','2015-09-17 14:52:02','admin','2015-09-17 14:52:02',6,1);
/*!40000 ALTER TABLE `payment_method_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_methods`
--

DROP TABLE IF EXISTS `payment_methods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_methods` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `external_key` varchar(255) COLLATE utf8_bin NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `plugin_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `payment_methods_id` (`id`),
  UNIQUE KEY `payment_methods_external_key` (`external_key`,`tenant_record_id`),
  KEY `payment_methods_plugin_name` (`plugin_name`),
  KEY `payment_methods_active_accnt` (`is_active`,`account_id`),
  KEY `payment_methods_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_methods`
--

LOCK TABLES `payment_methods` WRITE;
/*!40000 ALTER TABLE `payment_methods` DISABLE KEYS */;
INSERT INTO `payment_methods` VALUES (1,'fb00388c-379d-4546-a3c1-3227adfe9359','fb00388c-379d-4546-a3c1-3227adfe9359','45928693-0679-4e38-82e9-b6d8a7375f50','__EXTERNAL_PAYMENT__',1,'admin','2015-09-17 14:51:22','admin','2015-09-17 14:51:22',1,1),(2,'f0c2d9ba-ad44-4b30-8b1b-e09cced9babf','f0c2d9ba-ad44-4b30-8b1b-e09cced9babf','45abc0d7-df8d-46c3-a621-d19367e43b44','__EXTERNAL_PAYMENT__',1,'admin','2015-09-17 14:51:31','admin','2015-09-17 14:51:31',2,1),(3,'25859391-f185-4bd5-9f99-10bc7154d16e','25859391-f185-4bd5-9f99-10bc7154d16e','51c7c760-bdac-44e4-a944-28e8cfba9dd4','__EXTERNAL_PAYMENT__',1,'admin','2015-09-17 14:51:39','admin','2015-09-17 14:51:39',3,1),(4,'890ea252-9c59-4998-96e4-74214ffdba95','890ea252-9c59-4998-96e4-74214ffdba95','bd78357b-3c3c-4135-ada1-d9925e3ba43c','__EXTERNAL_PAYMENT__',1,'admin','2015-09-17 14:51:47','admin','2015-09-17 14:51:47',4,1),(5,'50842bfd-9e1c-4556-aa20-729840f0ac72','50842bfd-9e1c-4556-aa20-729840f0ac72','41dbe4ca-0333-4d33-ac8e-6d0e48237ad5','__EXTERNAL_PAYMENT__',1,'admin','2015-09-17 14:51:55','admin','2015-09-17 14:51:55',5,1),(6,'d114dee6-fb03-454c-8244-e949d57ed1ae','d114dee6-fb03-454c-8244-e949d57ed1ae','76fdd52b-a99b-4439-ab8c-9eb8aceacc5b','__EXTERNAL_PAYMENT__',1,'admin','2015-09-17 14:52:02','admin','2015-09-17 14:52:02',6,1);
/*!40000 ALTER TABLE `payment_methods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_transaction_history`
--

DROP TABLE IF EXISTS `payment_transaction_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_transaction_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `attempt_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `transaction_external_key` varchar(255) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `transaction_type` varchar(32) COLLATE utf8_bin NOT NULL,
  `effective_date` datetime NOT NULL,
  `transaction_status` varchar(50) COLLATE utf8_bin NOT NULL,
  `amount` decimal(15,9) DEFAULT NULL,
  `currency` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `processed_amount` decimal(15,9) DEFAULT NULL,
  `processed_currency` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `payment_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `gateway_error_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `gateway_error_msg` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `transaction_history_target_record_id` (`target_record_id`),
  KEY `transaction_history_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_transaction_history`
--

LOCK TABLES `payment_transaction_history` WRITE;
/*!40000 ALTER TABLE `payment_transaction_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_transaction_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_transactions`
--

DROP TABLE IF EXISTS `payment_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_transactions` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `attempt_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `transaction_external_key` varchar(255) COLLATE utf8_bin NOT NULL,
  `transaction_type` varchar(32) COLLATE utf8_bin NOT NULL,
  `effective_date` datetime NOT NULL,
  `transaction_status` varchar(50) COLLATE utf8_bin NOT NULL,
  `amount` decimal(15,9) DEFAULT NULL,
  `currency` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `processed_amount` decimal(15,9) DEFAULT NULL,
  `processed_currency` varchar(3) COLLATE utf8_bin DEFAULT NULL,
  `payment_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `gateway_error_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `gateway_error_msg` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `transactions_id` (`id`),
  KEY `transactions_payment_id` (`payment_id`),
  KEY `transactions_key` (`transaction_external_key`),
  KEY `transactions_status` (`transaction_status`),
  KEY `transactions_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_transactions`
--

LOCK TABLES `payment_transactions` WRITE;
/*!40000 ALTER TABLE `payment_transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payments` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `account_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `payment_method_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `external_key` varchar(255) COLLATE utf8_bin NOT NULL,
  `state_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `last_success_state_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `payments_id` (`id`),
  UNIQUE KEY `payments_key` (`external_key`,`tenant_record_id`),
  KEY `payments_accnt` (`account_id`),
  KEY `payments_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles_permissions`
--

DROP TABLE IF EXISTS `roles_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles_permissions` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `permission` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `roles_permissions_idx` (`role_name`,`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles_permissions`
--

LOCK TABLES `roles_permissions` WRITE;
/*!40000 ALTER TABLE `roles_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `roles_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rolled_up_usage`
--

DROP TABLE IF EXISTS `rolled_up_usage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rolled_up_usage` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `subscription_id` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `unit_type` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `record_date` date NOT NULL,
  `amount` bigint(20) NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `rolled_up_usage_id` (`id`),
  KEY `rolled_up_usage_subscription_id` (`subscription_id`),
  KEY `rolled_up_usage_tenant_account_record_id` (`tenant_record_id`,`account_record_id`),
  KEY `rolled_up_usage_account_record_id` (`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rolled_up_usage`
--

LOCK TABLES `rolled_up_usage` WRITE;
/*!40000 ALTER TABLE `rolled_up_usage` DISABLE KEYS */;
/*!40000 ALTER TABLE `rolled_up_usage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sessions` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `start_timestamp` datetime NOT NULL,
  `last_access_time` datetime DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `host` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `session_data` mediumblob,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES (1,'2015-09-17 14:02:20','2015-09-17 14:02:20',1800000,'127.0.0.1','╛М\0sr\0java.util.HashMapзац`я\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0\0w\0\0\0\0\0\0\0x'),(2,'2015-09-17 14:16:51','2015-09-17 14:16:51',1800000,'127.0.0.1','╛М\0sr\0java.util.HashMapзац`я\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0\0w\0\0\0\0\0\0\0x'),(3,'2015-09-17 14:46:08','2015-09-17 14:52:01',1800000,'127.0.0.1','╛М\0sr\0java.util.HashMapзац`я\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0\0w\0\0\0\0\0\0\0x');
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription_events`
--

DROP TABLE IF EXISTS `subscription_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription_events` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `event_type` varchar(9) COLLATE utf8_bin NOT NULL,
  `user_type` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `requested_date` datetime NOT NULL,
  `effective_date` datetime NOT NULL,
  `subscription_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `plan_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `phase_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `price_list_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `current_version` int(11) DEFAULT '1',
  `is_active` tinyint(1) DEFAULT '1',
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `subscription_events_id` (`id`),
  KEY `idx_ent_1` (`subscription_id`,`is_active`,`effective_date`),
  KEY `idx_ent_2` (`subscription_id`,`effective_date`,`created_date`,`requested_date`,`id`),
  KEY `subscription_events_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription_events`
--

LOCK TABLES `subscription_events` WRITE;
/*!40000 ALTER TABLE `subscription_events` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscriptions` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `bundle_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `category` varchar(32) COLLATE utf8_bin NOT NULL,
  `start_date` datetime NOT NULL,
  `bundle_start_date` datetime NOT NULL,
  `active_version` int(11) DEFAULT '1',
  `charged_through_date` datetime DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `subscriptions_id` (`id`),
  KEY `subscriptions_bundle_id` (`bundle_id`),
  KEY `subscriptions_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions`
--

LOCK TABLES `subscriptions` WRITE;
/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_definition_history`
--

DROP TABLE IF EXISTS `tag_definition_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_definition_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `name` varchar(30) COLLATE utf8_bin NOT NULL,
  `description` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned DEFAULT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `tag_definition_history_id` (`id`),
  KEY `tag_definition_history_target_record_id` (`target_record_id`),
  KEY `tag_definition_history_name` (`name`),
  KEY `tag_definition_history_tenant_record_id` (`tenant_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_definition_history`
--

LOCK TABLES `tag_definition_history` WRITE;
/*!40000 ALTER TABLE `tag_definition_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag_definition_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_definitions`
--

DROP TABLE IF EXISTS `tag_definitions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_definitions` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `name` varchar(20) COLLATE utf8_bin NOT NULL,
  `description` varchar(200) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `tag_definitions_id` (`id`),
  KEY `tag_definitions_tenant_record_id` (`tenant_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_definitions`
--

LOCK TABLES `tag_definitions` WRITE;
/*!40000 ALTER TABLE `tag_definitions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag_definitions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_history`
--

DROP TABLE IF EXISTS `tag_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_history` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned NOT NULL,
  `object_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `object_type` varchar(30) COLLATE utf8_bin NOT NULL,
  `tag_definition_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `change_type` varchar(6) COLLATE utf8_bin NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `tag_history_target_record_id` (`target_record_id`),
  KEY `tag_history_tenant_account_record_id` (`tenant_record_id`,`account_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_history`
--

LOCK TABLES `tag_history` WRITE;
/*!40000 ALTER TABLE `tag_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `tag_definition_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `object_id` varchar(36) COLLATE utf8_bin NOT NULL,
  `object_type` varchar(30) COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime NOT NULL,
  `account_record_id` bigint(20) unsigned NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `tags_id` (`id`),
  KEY `tags_by_object` (`object_id`),
  KEY `tags_tenant_account_record_id` (`tenant_record_id`,`account_record_id`),
  KEY `tag_history_by_object` (`object_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_broadcasts`
--

DROP TABLE IF EXISTS `tenant_broadcasts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tenant_broadcasts` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `target_record_id` bigint(20) unsigned DEFAULT NULL,
  `target_table_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `type` varchar(64) COLLATE utf8_bin NOT NULL,
  `user_token` varchar(36) COLLATE utf8_bin DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `tenant_broadcasts_key` (`tenant_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_broadcasts`
--

LOCK TABLES `tenant_broadcasts` WRITE;
/*!40000 ALTER TABLE `tenant_broadcasts` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenant_broadcasts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_kvs`
--

DROP TABLE IF EXISTS `tenant_kvs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tenant_kvs` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `tenant_record_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `tenant_key` varchar(255) COLLATE utf8_bin NOT NULL,
  `tenant_value` mediumtext COLLATE utf8_bin NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `tenant_kvs_key` (`tenant_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_kvs`
--

LOCK TABLES `tenant_kvs` WRITE;
/*!40000 ALTER TABLE `tenant_kvs` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenant_kvs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenants`
--

DROP TABLE IF EXISTS `tenants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tenants` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `external_key` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `api_key` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `api_secret` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `api_salt` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  UNIQUE KEY `tenants_id` (`id`),
  UNIQUE KEY `tenants_api_key` (`api_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenants`
--

LOCK TABLES `tenants` WRITE;
/*!40000 ALTER TABLE `tenants` DISABLE KEYS */;
INSERT INTO `tenants` VALUES (1,'7fbe21d4-f1f9-450a-963d-4490dbab4666',NULL,'hootsuite','B5eW1uRmjNueMdMKz07Db8eUm3DqVA24za37eRDL0Vnr1jk1v1qVaEtxcHXEZY2+tvYeWdjh1yVJYKqTvdgJ1Q==','Ck71hvXXL/DaC/z9gghUuA==','2015-09-17 14:46:08','admin','2015-09-17 14:46:08','admin');
/*!40000 ALTER TABLE `tenants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `role_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `user_roles_idx` (`username`,`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `password_salt` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_date` datetime NOT NULL,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `record_id` (`record_id`),
  KEY `users_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-09-17 11:55:46
