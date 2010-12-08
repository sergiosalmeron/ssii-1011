CREATE DATABASE  IF NOT EXISTS `ssii` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ssii`;
-- MySQL dump 10.13  Distrib 5.1.40, for Win32 (ia32)
--
-- Host: localhost    Database: ssii
-- ------------------------------------------------------
-- Server version	5.1.53-community

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
-- Table structure for table `pelicula`
--

DROP TABLE IF EXISTS `pelicula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pelicula` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Titulo` varchar(120) NOT NULL,
  `Año` year(4) DEFAULT '1901',
  `Genero` varchar(45) DEFAULT 'No disponible',
  `Duracion` time DEFAULT '00:00:00',
  `Calificacion` varchar(45) DEFAULT 'No disponible',
  `Nacionalidad` varchar(45) DEFAULT 'No disponible',
  PRIMARY KEY (`ID`),
  KEY `TITULO_PELI` (`Titulo`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=200 DEFAULT CHARSET=utf8 COMMENT='Información de las películas. Id, Título, Año, Género, Durac';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pelicula`
--

LOCK TABLES `pelicula` WRITE;
/*!40000 ALTER TABLE `pelicula` DISABLE KEYS */;
INSERT INTO `pelicula` VALUES (199,'Valentino y el Clan del Can',2000,'null','00:00:00','prueba','null'),(198,'Uncle Boonmee recuerda sus vidas pasadas (Loong Boonmee raleuk chat)',2000,'null','00:00:00','prueba','null'),(197,'Un cine como tú en un país como este',2000,'null','00:00:00','prueba','null'),(196,'Toy Story 3',2000,'null','00:00:00','prueba','null'),(195,'The Way',2000,'null','00:00:00','prueba','null'),(194,'The Town',2000,'null','00:00:00','prueba','null'),(193,'Tamara Drewe',2000,'null','00:00:00','prueba','null'),(192,'Skyline',2000,'null','00:00:00','prueba','null'),(190,'Senso',2000,'null','00:00:00','prueba','null'),(191,'Siete mesas de billar francés',2000,'null','00:00:00','prueba','null'),(189,'Salidos de cuentas (Due Date)',2000,'null','00:00:00','prueba','null'),(188,'Rocco y sus hermanos (Rocco e il suoi fratelli)',2000,'null','00:00:00','prueba','null'),(187,'Propios y extraños',2000,'null','00:00:00','prueba','null'),(186,'Poesía (Poetry)',2000,'null','00:00:00','prueba','null'),(185,'Planes para mañana',2000,'null','00:00:00','prueba','null'),(184,'Origen (Inception)',2000,'null','00:00:00','prueba','null'),(183,'Neds',2000,'null','00:00:00','prueba','null'),(182,'Mystikal',2000,'null','00:00:00','prueba','null'),(181,'My Father, My Lord (Hofshat Kaits)',2000,'null','00:00:00','prueba','null'),(180,'Muerte en Venecia (Morte a Venezia)',2000,'null','00:00:00','prueba','null'),(179,'Megamind',2000,'null','00:00:00','prueba','null'),(178,'Madres & hijas (Mother & Child)',2000,'null','00:00:00','prueba','null'),(177,'Los últimos días del mundo (Les derniers jours du monde)',2000,'null','00:00:00','prueba','null'),(176,'Los seductores (L´Arnacoeur)',2000,'null','00:00:00','prueba','null'),(175,'Los otros dos (The other guys)',2000,'null','00:00:00','prueba','null'),(174,'Los ojos de Julia',2000,'null','00:00:00','prueba','null'),(173,'Lope',2000,'null','00:00:00','prueba','null'),(172,'Las crónicas de Narnia: La travesía del viajero del alba (The Chronicles of Narnia: The Voyage of the Dawn Treader)',2000,'null','00:00:00','prueba','null'),(171,'Las aventuras de Don Quijote',2000,'null','00:00:00','prueba','null'),(170,'Ladrones (Takers)',2000,'null','00:00:00','prueba','null'),(169,'La última cima',2000,'null','00:00:00','prueba','null'),(168,'La Tropa de Trapo en el país donde siempre brilla el sol',2000,'null','00:00:00','prueba','null'),(167,'La red social (The social network)',2000,'null','00:00:00','prueba','null'),(166,'La mujer del puerto',2000,'null','00:00:00','prueba','null'),(165,'La Bella y la Bestia 3D (Beauty and the Best 3D)',2000,'null','00:00:00','prueba','null'),(164,'Imparable (Unstoppable)',2000,'null','00:00:00','prueba','null'),(163,'Híncame el diente (Vampires Suck)',2000,'null','00:00:00','prueba','null'),(162,'Héctor y Bruno (Siempre hay tiempo)',2000,'null','00:00:00','prueba','null'),(161,'Harry Potter y las reliquias de la muerte - Parte 1 (Harry Potter and the deathly hallows - Part 1)',2000,'null','00:00:00','prueba','null'),(160,'Gru, mi villano favorito (Despicable Me)',2000,'null','00:00:00','prueba','null'),(159,'Ga\'Hoole: La leyenda de los guardianes (Legend of the Guardians: The Owls of Ga\'Hoole)',2000,'null','00:00:00','prueba','null'),(158,'Flamenco, flamenco',2000,'null','00:00:00','prueba','null'),(157,'Exit Through the Gift Shop',2000,'null','00:00:00','prueba','null'),(156,'Entrelobos',2000,'null','00:00:00','prueba','null'),(155,'Entre nosotros (Alle Anderen)',2000,'null','00:00:00','prueba','null'),(154,'En el camino (Na putu)',2000,'null','00:00:00','prueba','null'),(153,'El tesoro del rey Midas',2000,'null','00:00:00','prueba','null'),(152,'El inocente (L\'innocente)',2000,'null','00:00:00','prueba','null'),(151,'El idioma imposible',2000,'null','00:00:00','prueba','null'),(150,'El grito 2 (The Grudge 2)',2000,'null','00:00:00','prueba','null'),(149,'El gatopardo (Il gattopardo)',2000,'null','00:00:00','prueba','null'),(148,'El aprendiz de brujo (The Sorcerer\'s Apprentice)',2000,'null','00:00:00','prueba','null'),(147,'Diario de Greg (Diary of a wimpy kid)',2000,'null','00:00:00','prueba','null'),(146,'Cyrus',2000,'null','00:00:00','prueba','null'),(145,'Copia certificada (Copie conforme)',2000,'null','00:00:00','prueba','null'),(144,'Conocerás al hombre de tus sueños (You Will Meet a Tall Dark Stranger)',2000,'null','00:00:00','prueba','null'),(143,'Chloe',2000,'null','00:00:00','prueba','null'),(142,'Caza a la espía (Fair Game)',2000,'null','00:00:00','prueba','null'),(141,'Carancho',2000,'null','00:00:00','prueba','null'),(140,'Campanilla y el gran rescate (Tinker Bell and the Great Fairy Rescue)',2000,'null','00:00:00','prueba','null'),(139,'Bon appétit',2000,'null','00:00:00','prueba','null'),(138,'Biutiful',2000,'null','00:00:00','prueba','null'),(137,'Bendito canalla',2000,'null','00:00:00','prueba','null'),(136,'Annie Hall',2000,'null','00:00:00','prueba','null'),(135,'Adele y el misterio de la momia (Les aventures extraordinaires d´Adèle Blanc-Sec)',2000,'null','00:00:00','prueba','null'),(134,'3 metros sobre el cielo',2000,'null','00:00:00','prueba','null'),(133,'18 comidas',2000,'null','00:00:00','prueba','null'),(132,'¿Dónde se nacionaliza la marea?',2000,'null','00:00:00','prueba','null'),(131,'¿Cuánto pesa su edificio, Sr. Foster? (How Much Does Your Building Weigh, Mr Foster?)',2000,'null','00:00:00','prueba','null');
/*!40000 ALTER TABLE `pelicula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provincia`
--

DROP TABLE IF EXISTS `provincia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `provincia` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COMMENT='Nombre de provincia';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provincia`
--

LOCK TABLES `provincia` WRITE;
/*!40000 ALTER TABLE `provincia` DISABLE KEYS */;
INSERT INTO `provincia` VALUES (1,'ACoruña'),(2,'Álava'),(3,'Albacete'),(4,'Alicante/Alacant'),(5,'Almería'),(6,'Asturias'),(7,'Ávila'),(8,'Badajoz'),(9,'Barcelona'),(10,'Burgos'),(11,'Cáceres'),(12,'Cádiz'),(13,'Cantabria'),(14,'Castellón/Castelló'),(15,'CiudadReal'),(16,'Córdoba'),(17,'Cuenca'),(18,'Girona'),(19,'Granada'),(20,'Guadalajara'),(21,'Guipúzcoa'),(22,'Huelva'),(23,'Huesca'),(24,'Illes Balears'),(25,'Jaén'),(26,'La Rioja'),(27,'Las Palmas'),(28,'León'),(29,'Lleida'),(30,'Lugo'),(31,'Madrid'),(32,'Málaga'),(33,'Murcia'),(34,'Navarra'),(35,'Ourense'),(36,'Palencia'),(37,'Pontevedra'),(38,'Salamanca'),(39,'Santa Cruz de Tenerife'),(40,'Segovia'),(41,'Sevilla'),(42,'Soria'),(43,'Tarragona'),(44,'Teruel'),(45,'Toledo'),(46,'Valencia/València'),(47,'Valladolid'),(48,'Vizcaya'),(49,'Zamora'),(50,'Zaragoza');
/*!40000 ALTER TABLE `provincia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actuan`
--

DROP TABLE IF EXISTS `actuan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actuan` (
  `IDPelicula` int(11) NOT NULL,
  `Actor` varchar(45) NOT NULL,
  PRIMARY KEY (`IDPelicula`,`Actor`),
  KEY `ACTUAN_PELICULA` (`IDPelicula`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Actores en una pelicula';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actuan`
--

LOCK TABLES `actuan` WRITE;
/*!40000 ALTER TABLE `actuan` DISABLE KEYS */;
/*!40000 ALTER TABLE `actuan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `descripcion`
--

DROP TABLE IF EXISTS `descripcion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `descripcion` (
  `IDPelicula` int(11) NOT NULL,
  `Descripcion` text NOT NULL,
  PRIMARY KEY (`IDPelicula`),
  KEY `DESCRIPCION_PELICULA` (`IDPelicula`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Descripción (sinopsis) de la pelicula';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `descripcion`
--

LOCK TABLES `descripcion` WRITE;
/*!40000 ALTER TABLE `descripcion` DISABLE KEYS */;
/*!40000 ALTER TABLE `descripcion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesion`
--

DROP TABLE IF EXISTS `sesion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sesion` (
  `IDPelícula` int(11) NOT NULL,
  `IDCine` int(11) NOT NULL,
  `Hora` varchar(45) NOT NULL,
  `Dia` varchar(45) NOT NULL,
  PRIMARY KEY (`IDPelícula`,`IDCine`,`Hora`,`Dia`),
  KEY `PELI_EXHIBIDA` (`IDPelícula`),
  KEY `CINE_EXHIBIDO` (`IDCine`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Relaciona películas con los cines en que se exhiben.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesion`
--

LOCK TABLES `sesion` WRITE;
/*!40000 ALTER TABLE `sesion` DISABLE KEYS */;
/*!40000 ALTER TABLE `sesion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cine`
--

DROP TABLE IF EXISTS `cine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cine` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) DEFAULT NULL,
  `Direccion` varchar(45) DEFAULT NULL,
  `Provincia` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `CINE_PROVINCIA` (`Provincia`),
  KEY `NOMBRE_CINE` (`Nombre`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Información de los cines. Nombre, Dirección y Provincia';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cine`
--

LOCK TABLES `cine` WRITE;
/*!40000 ALTER TABLE `cine` DISABLE KEYS */;
INSERT INTO `cine` VALUES (1,'Cine Tomás Luis de Victoria','Calle Lesquinas, 4 , 05001, Ávila',7),(2,'Cines Estrella Ávila','Carretera Villacastín, s/n , 05004, Ávila',7);
/*!40000 ALTER TABLE `cine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dirigen`
--

DROP TABLE IF EXISTS `dirigen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dirigen` (
  `IDPelicula` int(11) NOT NULL,
  `Director` varchar(45) NOT NULL,
  PRIMARY KEY (`IDPelicula`,`Director`),
  KEY `DIRIGEN_PELICULA` (`IDPelicula`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Directores de películs.Generalmente es uno,pueden ser varios';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dirigen`
--

LOCK TABLES `dirigen` WRITE;
/*!40000 ALTER TABLE `dirigen` DISABLE KEYS */;
/*!40000 ALTER TABLE `dirigen` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-12-08 17:47:19
