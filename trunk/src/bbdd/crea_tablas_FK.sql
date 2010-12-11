CREATE DATABASE  IF NOT EXISTS `ssii` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ssii`;


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


DROP TABLE IF EXISTS `pelicula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pelicula` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Titulo` varchar(120) NOT NULL,
  `Anyo` year(4) DEFAULT '1901',
  `Genero` varchar(45) DEFAULT 'No disponible',
  `Duracion` time DEFAULT '00:00:00',
  `Calificacion` varchar(45) DEFAULT 'No disponible',
  `Nacionalidad` varchar(45) DEFAULT 'No disponible',
  PRIMARY KEY (`ID`),
  KEY `TITULO_PELI` (`Titulo`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Información de las películas. Id, Título, Año, Género, Durac';
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `pelicula` WRITE;
/*!40000 ALTER TABLE `pelicula` DISABLE KEYS */;
/*!40000 ALTER TABLE `pelicula` ENABLE KEYS */;
UNLOCK TABLES;



DROP TABLE IF EXISTS `provincia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `provincia` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Nombre de provincia';
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `provincia` WRITE;
/*!40000 ALTER TABLE `provincia` DISABLE KEYS */;
/*!40000 ALTER TABLE `provincia` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `actuan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actuan` (
  `IDPelicula` int(11) NOT NULL,
  `Actor` varchar(45) NOT NULL,
  PRIMARY KEY (`IDPelicula`,`Actor`),
  CONSTRAINT `ACTUAN_PELICULA` FOREIGN KEY (`IDPelicula`) REFERENCES `pelicula` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Actores en una pelicula';
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `actuan` WRITE;
/*!40000 ALTER TABLE `actuan` DISABLE KEYS */;
/*!40000 ALTER TABLE `actuan` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `descripcion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `descripcion` (
  `IDPelicula` int(11) NOT NULL,
  `Descripcion` text NOT NULL,
  PRIMARY KEY (`IDPelicula`),
  KEY `DESCRIPCION_PELICULA` (`IDPelicula`),
  CONSTRAINT `DESCRIPCION_PELICULA` FOREIGN KEY (`IDPelicula`) REFERENCES `pelicula` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Descripción (sinopsis) de la pelicula';
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `descripcion` WRITE;
/*!40000 ALTER TABLE `descripcion` DISABLE KEYS */;
/*!40000 ALTER TABLE `descripcion` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `sesion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sesion` (
  `IDPelicula` int(11) NOT NULL,
  `IDCine` int(11) NOT NULL,
  `Hora` varchar(45) NOT NULL,
  `Dia` varchar(45) NOT NULL,
  PRIMARY KEY (`IDPelicula`,`IDCine`,`Hora`,`Dia`),
  KEY `PELI_EXHIBIDA` (`IDPelicula`),
  KEY `CINE_EXHIBIDO` (`IDCine`),
  CONSTRAINT `CINE_EXHIBIDO` FOREIGN KEY (`IDCine`) REFERENCES `cine` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `PELI_EXHIBIDA` FOREIGN KEY (`IDPelicula`) REFERENCES `pelicula` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Relaciona películas con los cines en que se exhiben.';
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `sesion` WRITE;
/*!40000 ALTER TABLE `sesion` DISABLE KEYS */;
/*!40000 ALTER TABLE `sesion` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `cine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cine` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) DEFAULT NULL,
  `Direccion` varchar(45) DEFAULT NULL,
  `IDProvincia` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `NOMBRE_CINE` (`Nombre`) USING BTREE,
  KEY `PROV_CINE` (`IDProvincia`),
  CONSTRAINT `PROV_CINE` FOREIGN KEY (`IDProvincia`) REFERENCES `provincia` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Información de los cines. Nombre, Dirección y Provincia';
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `cine` WRITE;
/*!40000 ALTER TABLE `cine` DISABLE KEYS */;
/*!40000 ALTER TABLE `cine` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `dirigen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dirigen` (
  `IDPelicula` int(11) NOT NULL,
  `Director` varchar(45) NOT NULL,
  PRIMARY KEY (`IDPelicula`,`Director`),
  KEY `DIRIGEN_PELICULA` (`IDPelicula`),
  CONSTRAINT `DIRIGEN_PELICULA` FOREIGN KEY (`IDPelicula`) REFERENCES `pelicula` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Directores de películs.Generalmente es uno,pueden ser varios';
/*!40101 SET character_set_client = @saved_cs_client */;


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

