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


DROP TABLE IF EXISTS `actualizado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actualizado` (
  `tabla` varchar(11) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`tabla`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Tabla que guarda cuándo fué actualizada cada tabla por últim';
/*!40101 SET character_set_client = @saved_cs_client */;



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
  `Url` varchar(150) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Url_UNIQUE` (`Url`),
  KEY `TITULO_PELI` (`Titulo`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Información de las películas. Id, Título, Año, Género, Durac';
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `provincia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `provincia` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Nombre de provincia';
/*!40101 SET character_set_client = @saved_cs_client */;


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


DROP TABLE IF EXISTS `cine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cine` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) DEFAULT NULL,
  `Direccion` text,
  `IDProvincia` int(11) DEFAULT NULL,
  `Url` varchar(150) NOT NULL,
  `IDCiudad` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Url_UNIQUE` (`Url`),
  KEY `NOMBRE_CINE` (`Nombre`) USING BTREE,
  KEY `PROV_CINE` (`IDProvincia`),
  KEY `CIUDAD_CINE` (`IDCiudad`),
  CONSTRAINT `CIUDAD_CINE` FOREIGN KEY (`IDCiudad`) REFERENCES `ciudad` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `PROV_CINE` FOREIGN KEY (`IDProvincia`) REFERENCES `provincia` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Información de los cines. Nombre, Dirección y Provincia';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `ciudad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ciudad` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(70) NOT NULL,
  `IDProvincia` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CiudadProvincia` (`IDProvincia`),
  CONSTRAINT `CiudadProvincia` FOREIGN KEY (`IDProvincia`) REFERENCES `provincia` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Información de las ciudades. ID, Nombre y Provincia en la que se encuentra';
/*!40101 SET character_set_client = @saved_cs_client */;

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




CREATE DATABASE  IF NOT EXISTS `usersFB` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `usersFB`;


DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `ID` int(15) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) NOT NULL,
  `IDProvincia` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `PROV_USER` FOREIGN KEY (`IDProvincia`) REFERENCES `ssii`.`provincia` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ID, nombre y provincia de users registrados en la app de FB';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `temperamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temperamentos` (
  `IDUser` int(15) NOT NULL,
  `TempGuardian` DECIMAL(6,4) NOT NULL,
  `TempArtesano` DECIMAL(6,4) NOT NULL,
  `TempIdealista` DECIMAL(6,4) NOT NULL,
  `TempRacional` DECIMAL(6,4) NOT NULL,
  PRIMARY KEY (`IDUser`),
  CONSTRAINT `TEMP_USER` FOREIGN KEY (`IDUser`) REFERENCES `users` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ID y temperamentos de los usuarios';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
