-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: sistema_steam
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `compras`
--

DROP TABLE IF EXISTS `compras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compras` (
  `id_compra` bigint NOT NULL AUTO_INCREMENT,
  `fecha_compra` datetime NOT NULL,
  `total` float NOT NULL,
  `id_usuario` bigint NOT NULL,
  PRIMARY KEY (`id_compra`),
  KEY `FK_compras_id_usuario` (`id_usuario`),
  CONSTRAINT `FK_compras_id_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compras`
--

LOCK TABLES `compras` WRITE;
/*!40000 ALTER TABLE `compras` DISABLE KEYS */;
INSERT INTO `compras` VALUES (1,'2022-06-26 23:33:26',475.588,1),(2,'2022-06-27 05:17:38',4603.8,1),(3,'2022-06-27 05:17:47',2899.92,7),(4,'2022-06-27 05:17:58',5245.31,3);
/*!40000 ALTER TABLE `compras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detallescompra`
--

DROP TABLE IF EXISTS `detallescompra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detallescompra` (
  `id_detallecompra` bigint NOT NULL AUTO_INCREMENT,
  `importe` float NOT NULL,
  `numero_copias` int NOT NULL,
  `precio` float NOT NULL,
  `id_compra` bigint NOT NULL,
  `id_videojuego` bigint NOT NULL,
  PRIMARY KEY (`id_detallecompra`),
  KEY `FK_detallescompra_id_videojuego` (`id_videojuego`),
  KEY `FK_detallescompra_id_compra` (`id_compra`),
  CONSTRAINT `FK_detallescompra_id_compra` FOREIGN KEY (`id_compra`) REFERENCES `compras` (`id_compra`),
  CONSTRAINT `FK_detallescompra_id_videojuego` FOREIGN KEY (`id_videojuego`) REFERENCES `videojuegos` (`id_videojuego`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detallescompra`
--

LOCK TABLES `detallescompra` WRITE;
/*!40000 ALTER TABLE `detallescompra` DISABLE KEYS */;
INSERT INTO `detallescompra` VALUES (1,409.99,1,409.99,1,1),(2,1639.96,4,409.99,2,1),(3,768.9,1,768.9,2,8),(4,929.95,5,185.99,2,6),(5,349.99,1,349.99,2,5),(6,279.99,1,279.99,2,3),(7,2099.94,6,349.99,3,5),(8,399.99,1,399.99,3,2),(9,1599.96,4,399.99,4,2),(10,2231.88,12,185.99,4,6),(11,409.99,1,409.99,4,1),(12,279.99,1,279.99,4,3);
/*!40000 ALTER TABLE `detallescompra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(12) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `nombre` (`nombre`),
  UNIQUE KEY `telefono` (`telefono`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Shroud','55555555'),(3,'Ninja','1234567890'),(4,'lMahesvara','6442067560'),(5,'Summit1g','558412312'),(7,'Se1ya','7372281838'),(8,'Skyshock','1236172373');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videojuegos`
--

DROP TABLE IF EXISTS `videojuegos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videojuegos` (
  `id_videojuego` bigint NOT NULL AUTO_INCREMENT,
  `desarrolladora` varchar(100) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `precio` float NOT NULL,
  `stock` int NOT NULL,
  PRIMARY KEY (`id_videojuego`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videojuegos`
--

LOCK TABLES `videojuegos` WRITE;
/*!40000 ALTER TABLE `videojuegos` DISABLE KEYS */;
INSERT INTO `videojuegos` VALUES (1,'Facepunch Studios','Rust',409.99,194),(2,'Rare Ltd','Sea of Thieves',399.99,195),(3,'Studio Wildcard','ARK: Survival Evolved',279.99,198),(4,'CAPCOM','Resident Evil Village',1199,200),(5,'CD PROJEKT RED','The Witcher 3: Wild Hunt',349.99,193),(6,'Endnight Games','The Forest',185.99,183),(8,'Rockstar','GTA V',768.9,199);
/*!40000 ALTER TABLE `videojuegos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-26 22:24:58
