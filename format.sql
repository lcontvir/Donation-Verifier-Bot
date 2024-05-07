CREATE TABLE IF NOT EXISTS `DonadorRole` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) DEFAULT NULL,
  `IdDiscordRole` varchar(50) DEFAULT NULL,
  `IdSteamRole` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IdDiscordRole` (`IdDiscordRole`),
  UNIQUE KEY `IdSteamRole` (`IdSteamRole`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Separador
-- Volcando estructura para vista bot.DonadorRoleView
-- Creando tabla temporal para superar errores de dependencia de VIEW
CREATE TABLE IF NOT EXISTS `DonadorRoleView` (
  `Id` INT(11) NOT NULL,
  `Nombre` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `IdDiscordRole` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `IdSteamRole` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci'
) ENGINE=MyISAM;

-- Separador
-- Volcando estructura para tabla bot.Registro
CREATE TABLE IF NOT EXISTS `Registro` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdDiscord` varchar(50) DEFAULT NULL,
  `DiscordName` varchar(50) DEFAULT NULL,
  `SteamId64` varchar(50) DEFAULT NULL,
  `RoleDonador` int(11) DEFAULT NULL,
  `FechaRegistro` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`Id`) USING BTREE,
  KEY `FK_Usuario_Role` (`RoleDonador`) USING BTREE,
  CONSTRAINT `Registro_ibfk_1` FOREIGN KEY (`RoleDonador`) REFERENCES `DonadorRole` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- Separador
-- Volcando estructura para tabla bot.Usuario
CREATE TABLE IF NOT EXISTS `Usuario` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdDiscord` varchar(50) DEFAULT NULL,
  `DiscordName` varchar(50) DEFAULT NULL,
  `SteamId64` varchar(50) DEFAULT NULL,
  `RoleDonador` int(11) DEFAULT NULL,
  `FechaRegistro` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IdDiscord` (`IdDiscord`) USING BTREE,
  UNIQUE KEY `SteamId64` (`SteamId64`),
  KEY `FK_Usuario_Role` (`RoleDonador`),
  CONSTRAINT `FK_Usuario_Role` FOREIGN KEY (`RoleDonador`) REFERENCES `DonadorRole` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Separador
-- Volcando estructura para vista bot.UsuarioDiscordView
-- Creando tabla temporal para superar errores de dependencia de VIEW
CREATE TABLE IF NOT EXISTS `UsuarioDiscordView` (
  `Id` INT(11) NOT NULL,
  `IdDiscord` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `DiscordName` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `SteamId64` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `RolDonador` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `FechaRegistro` TIMESTAMP NULL
) ENGINE=MyISAM;

-- Separador
-- Volcando estructura para vista bot.UsuarioSCPSLView
-- Creando tabla temporal para superar errores de dependencia de VIEW
CREATE TABLE IF NOT EXISTS `UsuarioSCPSLView` (
  `Id` INT(11) NOT NULL,
  `IdDiscord` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `DiscordName` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `SteamId64` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `RolDonador` VARCHAR(50) NULL COLLATE 'utf8mb4_general_ci',
  `FechaRegistro` TIMESTAMP NULL
) ENGINE=MyISAM;

-- Separador
-- Volcando estructura para vista bot.DonadorRoleView
-- Eliminando tabla temporal y crear estructura final de VIEW
DROP TABLE IF EXISTS `DonadorRoleView`;
-- Separador
CREATE OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `DonadorRoleView` AS select `DonadorRole`.`Id` AS `Id`,`DonadorRole`.`Nombre` AS `Nombre`,`DonadorRole`.`IdDiscordRole` AS `IdDiscordRole`,`DonadorRole`.`IdSteamRole` AS `IdSteamRole` from `DonadorRole`;

-- Separador
-- Volcando estructura para vista bot.UsuarioDiscordView
-- Eliminando tabla temporal y crear estructura final de VIEWv
DROP TABLE IF EXISTS `UsuarioDiscordView`;
-- Separador
CREATE OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `UsuarioDiscordView` AS select `u`.`Id` AS `Id`,`u`.`IdDiscord` AS `IdDiscord`,`u`.`DiscordName` AS `DiscordName`,`u`.`SteamId64` AS `SteamId64`,`d`.`Nombre` AS `RolDonador`,`u`.`FechaRegistro` AS `FechaRegistro` from (`Usuario` `u` join `DonadorRole` `d`) where `u`.`RoleDonador` = `d`.`Id`;

-- Separador
-- Volcando estructura para vista bot.UsuarioSCPSLView
-- Eliminando tabla temporal y crear estructura final de VIEW
DROP TABLE IF EXISTS `UsuarioSCPSLView`;
-- Separador
CREATE OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `UsuarioSCPSLView` AS select `u`.`Id` AS `Id`,`u`.`IdDiscord` AS `IdDiscord`,`u`.`DiscordName` AS `DiscordName`,`u`.`SteamId64` AS `SteamId64`,`d`.`IdSteamRole` AS `RolDonador`,`u`.`FechaRegistro` AS `FechaRegistro` from (`Usuario` `u` join `DonadorRole` `d`) where `u`.`RoleDonador` = `d`.`Id`;

-- Separador
-- Volcando estructura para disparador bot.almacenar_registro
CREATE OR REPLACE TRIGGER almacenar_registro
AFTER INSERT ON Usuario
FOR EACH ROW
BEGIN
INSERT INTO Registro (Id, IdDiscord, DiscordName, SteamId64, RoleDonador, FechaRegistro)
VALUES (NEW.Id, NEW.IdDiscord, NEW.DiscordName, NEW.SteamId64, NEW.RoleDonador, NEW.FechaRegistro);
END;

-- Separador
-- Volcando estructura para evento bot.eliminar_registros_caducados
CREATE OR REPLACE EVENT eliminar_registros_caducados
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
DECLARE cutoff_date DATE;
SET cutoff_date = DATE_SUB(CURRENT_DATE(), INTERVAL %EXPIRE_DAYS% DAY);
DELETE FROM Usuario WHERE FechaRegistro < cutoff_date;
END;