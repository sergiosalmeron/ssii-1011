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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-12-08 17:47:05
