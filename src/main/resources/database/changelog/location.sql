--liquibase formatted sql
--changeset andrew:2020-01-31
CREATE TABLE IF NOT EXISTS "Location" (
	"PK"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	"Name"	TEXT NOT NULL UNIQUE
);
INSERT INTO "Location" VALUES (1,'Beach');
INSERT INTO "Location" VALUES (2,'Broadway Theater');
INSERT INTO "Location" VALUES (3,'Casino');
INSERT INTO "Location" VALUES (4,'Circus Tent');
INSERT INTO "Location" VALUES (5,'Corporate Party');
INSERT INTO "Location" VALUES (6,'Movie Studio');
INSERT INTO "Location" VALUES (7,'Bank');
INSERT INTO "Location" VALUES (8,'Day Spa');
INSERT INTO "Location" VALUES (9,'Hotel');
INSERT INTO "Location" VALUES (10,'Restaurant');
INSERT INTO "Location" VALUES (11,'Supermarket');
INSERT INTO "Location" VALUES (12,'Service Station');
INSERT INTO "Location" VALUES (13,'Hospital');
INSERT INTO "Location" VALUES (14,'Embassy');
INSERT INTO "Location" VALUES (15,'Military Base');
INSERT INTO "Location" VALUES (16,'Police Station');
INSERT INTO "Location" VALUES (17,'School');
INSERT INTO "Location" VALUES (18,'University');
INSERT INTO "Location" VALUES (19,'Airplane');
INSERT INTO "Location" VALUES (20,'Ocean Liner');
INSERT INTO "Location" VALUES (21,'Passenger Train');
INSERT INTO "Location" VALUES (22,'Pirate Ship');
INSERT INTO "Location" VALUES (23,'Submarine');
INSERT INTO "Location" VALUES (24,'Cathedral');
INSERT INTO "Location" VALUES (25,'Crusader Army');
INSERT INTO "Location" VALUES (26,'Polar Station');
INSERT INTO "Location" VALUES (27,'Space Station');
INSERT INTO "Location" VALUES (28,'Amusement Park');
INSERT INTO "Location" VALUES (29,'Nightclub');
INSERT INTO "Location" VALUES (30,'Zoo');
INSERT INTO "Location" VALUES (31,'Candy Factory');
INSERT INTO "Location" VALUES (32,'Cat Show');
INSERT INTO "Location" VALUES (33,'Cemetery');
INSERT INTO "Location" VALUES (34,'Coal Mine');
INSERT INTO "Location" VALUES (35,'Construction Site');
INSERT INTO "Location" VALUES (36,'Gas Station');
INSERT INTO "Location" VALUES (37,'Harbor Docks');
INSERT INTO "Location" VALUES (38,'Jail');
INSERT INTO "Location" VALUES (39,'Jazz Club');
INSERT INTO "Location" VALUES (40,'Library');
INSERT INTO "Location" VALUES (41,'Race Track');
INSERT INTO "Location" VALUES (42,'Retirement Home');
INSERT INTO "Location" VALUES (43,'Rock Concert');
INSERT INTO "Location" VALUES (44,'Sightseeing Bus');
INSERT INTO "Location" VALUES (45,'Stadium');
INSERT INTO "Location" VALUES (46,'Subway');
INSERT INTO "Location" VALUES (47,'The U.N.');
INSERT INTO "Location" VALUES (48,'Vineyard');
INSERT INTO "Location" VALUES (49,'Wedding');
